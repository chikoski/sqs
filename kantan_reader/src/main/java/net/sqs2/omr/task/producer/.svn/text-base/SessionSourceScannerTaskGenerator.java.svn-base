/**
 *  SessionSourceScannerTaskGenerator.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2007/01/31
 Author hiroya
 */
package net.sqs2.omr.task.producer;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Logger;

import net.sqs2.omr.session.SessionStopException;
import net.sqs2.omr.session.event.MarkReaderSessionMonitor;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.RowID;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSourceContentIndexer;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.source.SpreadSheet;
import net.sqs2.omr.task.AbstractTask;
import net.sqs2.omr.task.PageTask;
import net.sqs2.omr.task.TaskAccessor;
import net.sqs2.omr.task.TaskHolder;

public class SessionSourceScannerTaskGenerator extends SessionSourceScanner implements Runnable{

	private MarkReaderSessionMonitor monitor;
	private long newFileIgnoreThreshold;
	private TaskHolder taskHolder;

	public SessionSourceScannerTaskGenerator(SessionSource sessionSource, 
			MarkReaderSessionMonitor monitor,
			long newFileIgnoreThreshold,
			TaskHolder taskHolder
			)throws IOException{
		super(sessionSource);
		this.monitor = monitor;
		this.newFileIgnoreThreshold = newFileIgnoreThreshold;
		this.taskHolder = taskHolder;
	}
	
	public AbstractSessionSourceScannerWorker createWorker()throws IOException{
		return new SessionSourceScannerWorker(this.sessionSource, this.taskHolder,
				this.monitor, this.newFileIgnoreThreshold);
	}

	public void run() {

		super.run();
		
	}
	
	static protected class SessionSourceScannerWorker extends AbstractSessionSourceScannerWorker{

		private static final boolean INFO = false;

		int numAdded = 0;
		int numReused = 0; 
		int numRetry = 0; 
		
		private SessionSource sessionSource;
		private TaskHolder taskHolder;
		private MarkReaderSessionMonitor monitor;
		private long newFileIgnoreThreshold;

		private TaskAccessor taskAccessor;
		
		private SpreadSheet currentSpreadSheet = null;
		private long now;
		SessionSourceScannerWorker(SessionSource sessionSource, TaskHolder taskHolder, MarkReaderSessionMonitor monitor, long newFileIgnoreThreshold)throws IOException{
			this.now = Calendar.getInstance().getTimeInMillis();
			this.sessionSource = sessionSource;
			this.taskHolder = taskHolder;
			this.monitor = monitor;
			this.newFileIgnoreThreshold = newFileIgnoreThreshold;
			this.taskAccessor = new TaskAccessor(sessionSource.getRootDirectory());
		}
		
		void work(SourceDirectory sourceDirectory, int pageNumber, PageID pageID, int rowIndex)throws SessionStopException{
			PageTask task = sourceDirectory.createTask(pageNumber, pageID, this.sessionSource.getSessionID());

			/*
			if(isIgnorableTask(task)){
				return;
			}*/
			this.taskHolder.incrementNumTargetTasks(1);

			 PageTask preparedTask = prepareTask(task);
			 if(preparedTask != null){
				 this.taskAccessor.put(preparedTask);
				 this.taskHolder.addPreparedTask(preparedTask);
				 this.monitor.notifyTaskProduced(preparedTask);
			 }
			endScanningPageID(sourceDirectory, pageID, rowIndex);
		}

		private void endScanningPageID(SourceDirectory sourceDirectory, PageID pageID, int rowIndex) {
			SessionSourceContentIndexer sessionSourceIndexer = this.sessionSource.getSessionSourceContentIndexer();
			sessionSourceIndexer.putRowID(pageID, new RowID(this.currentSpreadSheet, rowIndex));
		}
		
		void startScanningSourceDirectory(SourceDirectory sourceDirectory){
			this.currentSpreadSheet = new SpreadSheet(sourceDirectory);
			SessionSourceContentIndexer sessionSourceIndexer = this.sessionSource.getSessionSourceContentIndexer();
			sessionSourceIndexer.addSpreadSheet(this.currentSpreadSheet);
		}
	
		private PageTask prepareTask(PageTask task)throws SessionStopException{
			PageTask storedTask = null;
			try{
				storedTask = (PageTask)this.taskAccessor.get(task.toString());
			}catch(Exception ignore){
			}
			
			if(isOnceExecutedTask(storedTask)){
				if(hasError(storedTask)){
					
					this.monitor.notifyErrorTaskReproduced(storedTask);

					if(! isExecutionRequiredTaskWithError(task, storedTask)){
						return null;
					}
					
				}else if(storedTask.getTaskResult() != null){
					if(! isExecutionRequiredTask(task, storedTask)){						
						return null;
					}
				}else{
					this.numAdded++;
					Logger.getLogger("session").info("==========ADD\t" + task);
				}
			}else{
				this.numAdded++;
				Logger.getLogger("session").info("==========ADD\t" + task);
			}
			return task;
		}

		private boolean isOnceExecutedTask(AbstractTask storedTask) {
			return storedTask != null;
		}

		private boolean isIgnorableTask(PageTask task) {
			if(isConcurrentFileModificationSuspected(task)){
				if(INFO){
					Logger.getLogger("source").info("IGNORE\t"+task);
				}
				return true;
			}
			/*
			if(isPrepareTaskd(task)){
				if(INFO){
					Logger.getLogger("source").info("PREPARED\t"+task);
				}
				return true;
			}
			if(isLeasedTask(task)){
				if(INFO){
					Logger.getLogger("source").info("LEASED\t"+task);
				}
				return true;
			}
			*/
			return false;
		}

		private boolean isExecutionRequiredTaskWithError(AbstractTask task, AbstractTask storedTask) {
			if(this.sessionSource.getSessionID() == storedTask.getSessionID()){
				if(INFO){
					Logger.getLogger("source").info("IGNORE ERROR\t" + task);
				}
				return false;
			}else{
				this.numRetry++;
				if(INFO){
					Logger.getLogger("source").info("==========RETRY ERROR\t" + task);
				}
				return true;
			}
		}

		private boolean isExecutionRequiredTask(AbstractTask task, AbstractTask storedTask) {
			if(this.sessionSource.getSessionID() == storedTask.getSessionID()){
				if(INFO){
					//Logger.getLogger("source").info("IGNORE\t" + task);
				}
				return false;
			}else{
				if(INFO){
					//Logger.getLogger("source").info("REUSE\t" + task);
				}
				this.numReused++;
				this.taskHolder.setNumReusedTasks(this.numReused);
				return false;
			}
		}

		/*
		private void addNewTask(AbstractTask task) {
		}*/

		private boolean isPreparedTask(AbstractTask task){
			return this.taskHolder.isPreparedTask(task);
		}

		private boolean isLeasedTask(AbstractTask task){
			return this.taskHolder.isLeasedTask(task);
		}

		private boolean hasError(AbstractTask task){
			return task.getTaskError() != null;
		}

		private boolean isConcurrentFileModificationSuspected(PageTask task){
			return (now - this.newFileIgnoreThreshold) < task.getPageID().getFileResourceID().getLastModified();
		}

		void finishScan(){
			StringBuilder sb = new StringBuilder(64);
			sb.append("\nnumReused = " + numReused);
			sb.append("\nnumAdded = " + numAdded);
			sb.append("\nnumRetry = " + numRetry);
			Logger.getLogger("session").info("TaskProducer\n\t" + sb.toString());
		}
	}
}
