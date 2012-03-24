/*

 SessionProgressModel.java

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
package net.sqs2.omr.swing;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.sqs2.lang.GroupThreadFactory;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.session.event.MarkReaderSessionMonitor;
import net.sqs2.omr.session.event.MarkReaderSessionMonitorAdapter;
import net.sqs2.omr.source.SourceDirectoryConfiguration;
import net.sqs2.omr.task.PageTask;
import net.sqs2.omr.task.TaskHolder;

public class SessionProgressModel extends MarkReaderSessionMonitorAdapter implements Serializable, MarkReaderSessionMonitor {
	
	File sourceDirectoryRoot = null;
	Map<String,PageTaskErrorEntry> errorPathToTaskErrorEntryMap;

	public final static String PROPERTY_LABEL_STATUS_MESSAGE = "statusMessage";
	public final static String PROPERTY_LABEL_TIME_REMAINS_STRING = "timeRemainsString";
	public final static String PROPERTY_LABEL_PAGE_PAR_SEC_STRING = "pageParSecString";
	public final static String PROPERTY_LABEL_TIME_ELAPSED_STRING = "timeElapsedString";
	
	private boolean isReusedTasksAvailable = false;
	private boolean isGridAvailable = false;
	
	
	private final static long serialVersionUID = 0L;

	final static private DecimalFormat DECIMAL_FORMAT_00 = new DecimalFormat("00");

	final static private DecimalFormat DECIMAL_FORMAT_0_00 = new DecimalFormat("0.00");

	protected Color colTotal = Color.DARK_GRAY;

	protected Color colReused = Color.GRAY;

	protected Color colPrepared = Color.LIGHT_GRAY;

	protected Color colLeasedLocal = Color.YELLOW;

	protected Color colLeasedRemote = Color.CYAN;

	protected Color colSubmitted = Color.BLUE;

	protected Color colError = Color.MAGENTA;

	protected Color colExternalized = 	new Color(20, 210, 20);;

	protected String statusMessage = "";

	Color[] COLORS = new Color[] { colTotal, colReused, colPrepared,
			colLeasedLocal, colLeasedRemote, colSubmitted, colError,
			colExternalized };

	private long timeStarted = 0L;

	private String timeElapsedString;

	private String timeRemainsString = null;

	private String pageParSecString = null;

	private TaskHolder holder;

	transient protected Future<?> timer = null;
	
	transient ScheduledExecutorService scheduledExecutorService;
	
	private Set<MarkReaderSessionMonitor> monitors = new HashSet<MarkReaderSessionMonitor>();
	
	public SessionProgressModel(File sourceDirectoryRoot, TaskHolder holder) {
		this.sourceDirectoryRoot = sourceDirectoryRoot;
		this.holder = holder;
		this.errorPathToTaskErrorEntryMap = new HashMap<String,PageTaskErrorEntry>();
	}
	
	public void setReusedTasksAvailable(boolean isReusedTasksAvailable){
		this.isReusedTasksAvailable = isReusedTasksAvailable;
	}
	
	public boolean isReusedTasksAvailable(){
		return isReusedTasksAvailable;
	}
	
	public void setGridAvailable(boolean isGridAvailable){
		this.isGridAvailable = isGridAvailable;
	}

	public boolean isGridAvailable(){
		return isGridAvailable;
	}

	public File getSourceDirectoryRoot(){
		return this.sourceDirectoryRoot;
	}
	
	public void addSessionMonitor(MarkReaderSessionMonitor monitor){
		this.monitors.add(monitor);
	}

	public void notifySessionStarted(File sourceDirectoryRootPath) {
		Logger.getLogger("progressModel").info("started");
		setStatusMessage("session started....");
		startTimer();
	
		for(MarkReaderSessionMonitor monitor: monitors){
			monitor.notifySessionStarted(sourceDirectoryRootPath);
		}
	}

	public void notifySessionFinished(File sourceDirectoryRootPath) {
		Logger.getLogger("progressModel").info("finished");
		setStatusMessage("session finished.");
		stopTimer();

		for(MarkReaderSessionMonitor monitor: monitors){
			monitor.notifySessionFinished(sourceDirectoryRootPath);
		}
	}

	public void notifySessionStopped(File sourceDirectoryRootPath) {
		Logger.getLogger("progressModel").info("stopped");
		setStatusMessage("session stopped.");
		stopTimer();

		for(MarkReaderSessionMonitor monitor: monitors){
			monitor.notifySessionStopped(sourceDirectoryRootPath);
		}
	}

	public void notifyTaskProduced(PageTask pageTask) {
		setStatusMessage("prepare: " + pageTask.getPageID().getFileResourceID().getRelativePath());
		for(MarkReaderSessionMonitor monitor: monitors){
			monitor.notifyTaskProduced(pageTask);
		}
	}

	public void notifyStoreTask(PageTask pageTask) {
		setStatusMessage("process: " + pageTask.getPageID().getFileResourceID().getRelativePath());
		
		for(MarkReaderSessionMonitor monitor: monitors){
			monitor.notifyStoreTask(pageTask);
		}
	}

	public void notifyFoundMaster(PageMaster master) {
		Logger.getLogger("source").info("[*** Found Master]\t" + master.getRelativePath());
		setStatusMessage("master: " + master.getRelativePath());
		
		for(MarkReaderSessionMonitor monitor: monitors){
			monitor.notifyFoundMaster(master);
		}
	}

	public void notifyFoundConfig(SourceDirectoryConfiguration config) {
		Logger.getLogger("source").info("[*** Found Config]\t" + config);
		
		for(MarkReaderSessionMonitor monitor: monitors){
			monitor.notifyFoundConfig(config);
		}
	}

	public void notifyFoundImages(int numAddedImages, File sourceDirectory) {
		Logger.getLogger("source").info("[*** Found " + numAddedImages + " imgaes]\t" + sourceDirectory);
		setStatusMessage("found: "+ numAddedImages +" images in " + sourceDirectory.getName());
	
		for(MarkReaderSessionMonitor monitor: monitors){
			monitor.notifyFoundImages(numAddedImages, sourceDirectory);
		}
	}

	public void notifyScanDirectory(File sourceDirectoryFile) {
		Logger.getLogger("source").info("scan: " + sourceDirectoryFile);
		
		for(MarkReaderSessionMonitor monitor: monitors){
			monitor.notifyScanDirectory(sourceDirectoryFile);
		}
	}

	public void notifySourceInitializeDone(File sourceDirectoryRootFile) {
		Logger.getLogger("source").info("done: " + sourceDirectoryRootFile);

		for(MarkReaderSessionMonitor monitor: monitors){
			monitor.notifySourceInitializeDone(sourceDirectoryRootFile);
		}
	}

	public void setStatusMessage(String statusMessage) {
		//this.firePropertyChange(PROPERTY_LABEL_STATUS_MESSAGE, this.statusMessage, statusMessage);
		this.statusMessage = statusMessage;
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	private void startTimer() {
		/*
		if (this.timer != null) {
			this.timeStarted = System.currentTimeMillis();
			stopTimer();
		}
		*/

		GroupThreadFactory groupThreadFactory = new GroupThreadFactory(
				"SessionProgressModel#Timer", Thread.NORM_PRIORITY, true);
		this.scheduledExecutorService = Executors.newScheduledThreadPool(1, groupThreadFactory);

		setTimeStarted(System.currentTimeMillis());
		
		this.timer = this.scheduledExecutorService.scheduleAtFixedRate(
				new Runnable() {
					public void run() {
						long timeElapsed = System.currentTimeMillis() - getTimeStarted();
						setTimeElapsedString(createTimeElapsedString(timeElapsed));
						
						int numProcessedTasks = getNumExternalizingPages() + getNumErrorPages();
						double pageParSec = createPageParSec(timeElapsed, numProcessedTasks);
						
						setPageParSecString(createPageParSecString(pageParSec, numProcessedTasks));
						if(pageParSec != 0){
							setTimeRemainsString(createTimeRemainsString(timeElapsed, pageParSec));
						}	
					}

					private double createPageParSec(long timeElapsed, int numProcessedTasks) {
						if (0 < numProcessedTasks && 1000 < timeElapsed) {
							return (double) (numProcessedTasks) * 1000 / timeElapsed;
						} else {
							return 0;
						}
					}

					private String createPageParSecString(double pageParSec, int numProcessedTasks) {
						if (0 < numProcessedTasks) {
							return SessionProgressModel.DECIMAL_FORMAT_0_00.format(pageParSec);
						} else {
							return null;
						}
					}

					private String createTimeRemainsString(long timeElapsed, double pageParSec) {
						StringBuilder timeRemainsSB = new StringBuilder(8);
						double timeRemains = (getNumPreparedPages() + getNumExternalizingPages()) / pageParSec;
						if (1 < timeRemains) {
							if (3600 <= timeRemains && 3000L < timeElapsed) {
								timeRemainsSB.append(SessionProgressModel.DECIMAL_FORMAT_00.format(timeRemains / 3600));
								timeRemainsSB.append(":");
							}
							timeRemainsSB.append(SessionProgressModel.DECIMAL_FORMAT_00.format(timeRemains / 60 % 60));
							timeRemainsSB.append(":");
							timeRemainsSB.append(SessionProgressModel.DECIMAL_FORMAT_00.format(timeRemains % 60));
							return timeRemainsSB.toString();
						} else {
							return null;
						}
					}

					private String createTimeElapsedString(long timeElapsed) {
						StringBuilder timeElapsedSB = new StringBuilder(8);
						if (0 < timeElapsed / 1000 / 60 / 60) {
							timeElapsedSB
							.append(SessionProgressModel.DECIMAL_FORMAT_00.format(timeElapsed / 1000 / 60 / 60));
							timeElapsedSB.append(":");
						}
						timeElapsedSB.append(SessionProgressModel.DECIMAL_FORMAT_00.format(timeElapsed / 1000 / 60 % 60));
						timeElapsedSB.append(":");
						timeElapsedSB.append(SessionProgressModel.DECIMAL_FORMAT_00.format(timeElapsed / 1000 % 60));
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
		if (this.scheduledExecutorService != null) {
			this.scheduledExecutorService.shutdown();
		}
	}
	
	public long getTimeStarted(){
		return this.timeStarted;
	}

	public void setTimeStarted(long timeStarted){
		this.timeStarted = timeStarted;
	}

	public Map<String, PageTaskErrorEntry> getErrorPathToTaskErrorEntryMap() {
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
		//	return this.holder.getNumErrorTasks();
		return this.errorPathToTaskErrorEntryMap.size();
	}

	public void setTimeRemainsString(String timeRemainsString) {
		//this.firePropertyChange(PROPERTY_LABEL_TIME_REMAINS_STRING, this.timeRemainsString, timeRemainsString);
		this.timeRemainsString = timeRemainsString;
	}

	public void setPageParSecString(String pageParSecString) {
		//this.firePropertyChange(PROPERTY_LABEL_PAGE_PAR_SEC_STRING, this.pageParSecString, pageParSecString);
		this.pageParSecString = pageParSecString;
	}
	
	public void setTimeElapsedString(String timeElapsedString) {
		//this.firePropertyChange(PROPERTY_LABEL_TIME_ELAPSED_STRING, this.timeElapsedString, timeElapsedString);
		this.timeElapsedString = timeElapsedString;
	}
}
