/*

 PageTaskExecutionProgressModel.java

 Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2006/01/10

 */
package net.sqs2.omr.session.service;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.sqs2.event.EventListener;
import net.sqs2.lang.GroupThreadFactory;
import net.sqs2.omr.base.Messages;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.model.PageTaskHolder;
import net.sqs2.omr.model.PageTaskNumberCounter;
import net.sqs2.omr.session.model.PageTaskExceptionEntry;
import net.sqs2.omr.session.scan.PageTaskProducedEvent;

public class PageTaskExecutionProgressModel extends Observable implements Serializable{

	File sourceDirectoryRoot = null;
	boolean isNowUpdating = false;
	Future<?> sessionTimer = null;
	
	public Future<?> getSessionTimer(){
		return sessionTimer;
	}

	Map<String, PageTaskExceptionEntry> errorPathToTaskErrorEntryMap;

	public final static String PROPERTY_LABEL_STATUS_MESSAGE = "statusMessage";
	public final static String PROPERTY_LABEL_TIME_REMAINS_STRING = "timeRemainsString";
	public final static String PROPERTY_LABEL_PAGE_PAR_SEC_STRING = "pageParSecString";
	public final static String PROPERTY_LABEL_TIME_ELAPSED_STRING = "timeElapsedString";

	private boolean isReusedTasksAvailable = false;
	private boolean isParallelExecutionModeEnabled = false;

	private final static long serialVersionUID = 0L;

	final static private DecimalFormat DECIMAL_FORMAT_0_00 = new DecimalFormat("0.00");

	private long timeStarted = 0L;
	private long timePageTaskProduced = 0L;
	private long timePageTaskFinished = 0L;

	protected String statusMessage = "";
	
	private String timeElapsedString;

	private String timeRemainsString = null;

	private String pageParSecString = null;

	private PageTaskHolder holder;

	transient protected Future<?> timer = null;

	transient ScheduledExecutorService scheduledExecutorService;

	public PageTaskExecutionProgressModel(File sourceDirectoryRoot, PageTaskHolder holder) {
		this.sourceDirectoryRoot = sourceDirectoryRoot;
		this.holder = holder;
		this.errorPathToTaskErrorEntryMap = new HashMap<String, PageTaskExceptionEntry>();
	}
	
	public synchronized PageTaskNumberCounter createTaskNumberCounter(){
		return this.holder.createTaskNumberCounter();
	}

	public void setReusedTasksAvailable(boolean isReusedTasksAvailable) {
		this.isReusedTasksAvailable = isReusedTasksAvailable;
	}

	public boolean isReusedTasksAvailable() {
		return this.isReusedTasksAvailable;
	}

	public void setGridAvailable(boolean isGridAvailable) {
		this.isParallelExecutionModeEnabled = isGridAvailable;
	}

	public boolean isGridAvailable() {
		return this.isParallelExecutionModeEnabled;
	}

	public File getSourceDirectoryRoot() {
		return this.sourceDirectoryRoot;
	}

	
	public void setStatusMessage(String statusMessage) {
		// this.firePropertyChange(PROPERTY_LABEL_STATUS_MESSAGE,
		// this.statusMessage, statusMessage);
		this.statusMessage = statusMessage;
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	private void startTimer() {

		this.timeElapsedString = null;
		this.timeRemainsString = null;
		this.pageParSecString = null;

		GroupThreadFactory groupThreadFactory = new GroupThreadFactory("SessionProgressModel#Timer",
				Thread.NORM_PRIORITY - 1, true);
		this.scheduledExecutorService = Executors.newScheduledThreadPool(1, groupThreadFactory);

		setTimeStarted(System.currentTimeMillis());

		this.timer = this.scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				long timeElapsed = System.currentTimeMillis() - getTimeStarted();
				setTimeElapsedString(createTimeElapsedString(timeElapsed));

				int numProcessedTasks = getNumExternalizingPages() + getNumErrorPages();
				float pageParSec = createPageParSec(numProcessedTasks);

				setPageParSecString(createPageParSecString(pageParSec, numProcessedTasks));
				if (pageParSec != 0f) {
					setTimeRemainsString(createTimeRemainsString(timeElapsed, pageParSec));
				}
			}

			private float createPageParSec(int numProcessedTasks) {

				if (0 == numProcessedTasks){
					return 0f;
				}
				if (0 == getTimePageTaskProduced()){
					return 0f;
				}

				long elapsedTimePageTaskExecution = System.currentTimeMillis() - getTimePageTaskProduced();
				//int totalPages = getNumTargetPages();
				//int reusedPages = getNumReusedPages();
				//int targetPages = totalPages - reusedPages;
				return (numProcessedTasks * 1000.0f / elapsedTimePageTaskExecution);
			}

			private String createPageParSecString(double pageParSec, int numProcessedTasks) {
				if (0 < numProcessedTasks) {
					return PageTaskExecutionProgressModel.DECIMAL_FORMAT_0_00.format(pageParSec);
				} else {
					return null;
				}
			}

			private String createTimeRemainsString(long timeElapsed, double pageParSec) {
				StringBuilder timeRemainsSB = new StringBuilder(8);
				if(pageParSec == 0){
					return null;
				}
				long timeRemains = (long)Math.floor((getNumPreparedPages() + getNumSubmittedPages()) / pageParSec);
				if (timeRemains <= 0) {
					return null;
				}
				if (3600 <= timeRemains) {
					timeRemainsSB.append((int)(timeRemains / 3600));
					timeRemainsSB.append(Messages.HOUR_LABEL);
					timeRemainsSB.append(" ");
				}
				if (60 <= timeRemains) {
					timeRemainsSB.append(timeRemains / 60 % 60);
					timeRemainsSB.append(Messages.MIN_LABEL);
					timeRemainsSB.append(" ");
				}
					
				timeRemainsSB.append(timeRemains % 60);
				timeRemainsSB.append(Messages.SEC_LABEL);
				timeRemainsSB.append(" ");

				return timeRemainsSB.toString();
			}

			private String createTimeElapsedString(long timeElapsed) {
				StringBuilder timeElapsedSB = new StringBuilder(8);
				if (0 < timeElapsed / 1000 / 60 / 60) {
					timeElapsedSB.append((timeElapsed / 1000 / 60 / 60));
					timeElapsedSB.append(Messages.HOUR_LABEL);
					timeElapsedSB.append(" ");
				}
				
				if (0 < timeElapsed / 1000 / 60 ) {
					timeElapsedSB.append((timeElapsed / 1000 / 60 % 60));
					timeElapsedSB.append(Messages.MIN_LABEL);
					timeElapsedSB.append(" ");
				}
				if (0 < timeElapsed / 1000 ) {
					timeElapsedSB.append(timeElapsed / 1000 % 60);
					timeElapsedSB.append(Messages.SEC_LABEL);
				}
				return timeElapsedSB.toString();
			}
		}, 3, 1, TimeUnit.SECONDS);
	}

	private void stopTimer() {
		synchronized (this) {
			if (this.timer != null) {
				this.timer.cancel(true);
				this.timer = null;
			}
		}
		this.timeRemainsString = null;
		if (this.scheduledExecutorService != null) {
			this.scheduledExecutorService.shutdown();
		}
	}
	
	public long getTimeStarted() {
		return this.timeStarted;
	}

	public long getTimePageTaskProduced() {
		return this.timePageTaskProduced;
	}

	public long getTimePageTaskFinished() {
		return this.timePageTaskFinished;
	}

	public void setTimeStarted(long timeStarted) {
		this.timeStarted = timeStarted;
	}

	public void setTimeOfPageTaskProduced(long timePageTaskProduced) {
		this.timePageTaskProduced = timePageTaskProduced;
	}
	
	public void setTimeOfPageTaskRecieveFinished(long timePageTaskFinished) {
		this.timePageTaskFinished = timePageTaskFinished;
	}

	public Map<String, PageTaskExceptionEntry> getErrorPathToTaskErrorEntryMap() {
		return this.errorPathToTaskErrorEntryMap;
	}

	public String getTimeRemainsString() {
		return this.timeRemainsString;
	}

	public String getPageParSecString() {
		return this.pageParSecString;
	}

	public String getTimeElapsedString() {
		return this.timeElapsedString;
	}

	public int getNumTotalPages() {
		return this.holder.getNumTotalTasks();
	}

	public int getNumTargetPages() {
		return this.holder.getNumTargetTasks();
	}

	public int getNumReusedPages() {
		return this.holder.getNumReusedTasks();
	}

	public int getNumPreparedPages() {
		return this.holder.getNumPreparedTasks();
	}

	public int getNumSubmittedPages() {
		return this.holder.getNumSubmittedTasks();
	}

	public int getNumLocalLeasedPages() {
		return this.holder.getNumLocalLeasedTasks();
	}

	public int getNumRemoteLeasedPages() {
		return this.holder.getNumRemoteLeasedTasks();
	}

	public int getNumExternalizingPages() {
		return this.holder.getNumExternalizingTasks();
	}

	public int getNumErrorPages() {
		// return this.holder.getNumErrorTasks();
		return this.errorPathToTaskErrorEntryMap.size();
	}

	public void setTimeRemainsString(String timeRemainsString) {
		// this.firePropertyChange(PROPERTY_LABEL_TIME_REMAINS_STRING,
		// this.timeRemainsString, timeRemainsString);
		this.timeRemainsString = timeRemainsString;
	}

	public void setPageParSecString(String pageParSecString) {
		// this.firePropertyChange(PROPERTY_LABEL_PAGE_PAR_SEC_STRING,
		// this.pageParSecString, pageParSecString);
		this.pageParSecString = pageParSecString;
	}

	public void setTimeElapsedString(String timeElapsedString) {
		// this.firePropertyChange(PROPERTY_LABEL_TIME_ELAPSED_STRING,
		// this.timeElapsedString, timeElapsedString);
		this.timeElapsedString = timeElapsedString;
	}
	
	public class SessionEventStatusUpdater implements SessionEventListener{

		@Override
		public void notifySessionStarted(File sourceDirectoryRootPath) {
			Logger.getLogger("progressModel").info("started");
			setStatusMessage("session started....");
			startTimer();
		}

		@Override
		public void notifySessionFinished(PageTaskExecutionProgressModel model) {
			Logger.getLogger("progressModel").info("finished");
			setStatusMessage("session finished.");
			stopTimer();
		}

		@Override
		public void notifySessionStopped(File sourceDirectoryRootPath) {
			Logger.getLogger("progressModel").info("stopped");
			setStatusMessage("session stopped.");
			stopTimer();
		}

		@Override
		public void notifyExportingResultStarted(
				File sourceDirectoryRootFile) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void notifyExportResultDirectoryFinished(
				File sourceDirectoryRootFile) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void notifySessionResult(File sourceDirectoryRootFile) {
			// TODO Auto-generated method stub
			
		}
	}

	
	/*
	public class SessionSourceInitializationEventListenerImpl implements SessionSourceInitializationEventListener{

		@Override
		public void formMasterFileFound(FormMaster master) {
			Logger.getLogger("source").info("[*** Found Master]\t" + master.getRelativePath());
			setStatusMessage("master: " + master.getRelativePath());
		}

		@Override
		public void configFileFound(SourceDirectoryConfiguration config) {
			Logger.getLogger("source").info("[*** Found Config]\t" + config);
		}

		@Override
		public void imageFilesFound(int numAddedImages, File sourceDirectory) {
			Logger.getLogger("source").info("[*** Found " + numAddedImages + " imgaes]\t" + sourceDirectory);
			setStatusMessage("found: " + numAddedImages + " images in " + sourceDirectory.getName());
		}

		@Override
		public void scanningSourceDirectoryStarted(File sourceDirectoryFile) {
			Logger.getLogger("source").info("scan: " + sourceDirectoryFile);
		}

		@Override
		public void unreadableSourceDirectoryErrorThrown(
				SourceDirectory sourceDirectory) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void numOfImagesErrorThrown(SpreadSheet spreadSheet,
				int numImages) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void invalidFormMasterErrorThrown(FormMasterException ex) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void scanningSourceDirectoryDone(File subSourceDirectoryRoot) {
			// TODO Auto-generated method stub
			
		}
		
	}*/

	class PageTaskConsumedEventListenerImpl implements EventListener<PageTaskCommittedEvent>{
		@Override
		public void eventHappened(PageTaskCommittedEvent pageTaskEvent) {
			OMRPageTask pageTask = pageTaskEvent.getPageTask();
			setStatusMessage("process: " + pageTask.getPageID().getFileResourceID().getRelativePath());
		}
	}
	
	class PageTaskProducedEventListenerImpl implements EventListener<PageTaskProducedEvent>{

		@Override
		public void eventHappened(PageTaskProducedEvent e) {
			OMRPageTask pageTask = e.getPageTask();
			setStatusMessage("prepare: " + pageTask.getPageID().getFileResourceID().getRelativePath());
		}
	}

}
