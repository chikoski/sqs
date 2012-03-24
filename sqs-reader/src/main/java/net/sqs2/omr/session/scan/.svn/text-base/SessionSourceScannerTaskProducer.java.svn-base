/**
 *  SessionSourceScannerTaskProducer.java

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
package net.sqs2.omr.session.scan;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Logger;

import net.sqs2.event.EventSource;
import net.sqs2.omr.model.AbstractPageTask;
import net.sqs2.omr.model.ContentIndexer;
import net.sqs2.omr.model.MarkReaderConstants;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTaskAccessor;
import net.sqs2.omr.model.PageTaskFactory;
import net.sqs2.omr.model.PageTaskHolder;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.Ticket;
import net.sqs2.omr.session.model.SessionStopException;

public class SessionSourceScannerTaskProducer extends PageNumberedSessionSourceScanner implements Runnable {

	private EventSource<PageTaskProducedEvent> eventSource;
	private PageTaskHolder taskHolder;

	public SessionSourceScannerTaskProducer(SessionSource sessionSource, EventSource<PageTaskProducedEvent> eventSource,
			PageTaskHolder taskHolder) throws IOException {
		super(sessionSource);
		this.eventSource = eventSource;
		this.taskHolder = taskHolder;
	}

	@Override
	public AbstractSessionSourceScannerWorker createWorker() throws IOException {
		return new SessionSourceScannerWorker(this.sessionSource, this.taskHolder, this.eventSource,
				MarkReaderConstants.SESSION_SOURCE_NEWFILE_IGNORE_SEC_THRESHOLD_IN_SEC);
	}

	@Override
	public void run() {

		super.run();

	}

	static protected class SessionSourceScannerWorker extends AbstractSessionSourceScannerWorker {

		private static final boolean INFO = false;

		int numAdded = 0;
		int numReused = 0;
		int numRetry = 0;

		private SessionSource sessionSource;
		private PageTaskHolder taskHolder;
		private EventSource<PageTaskProducedEvent> eventSource;
		private long newFileIgnoreSecThreshold;

		private PageTaskAccessor taskAccessor;
		private long now;

		SessionSourceScannerWorker(SessionSource sessionSource, PageTaskHolder taskHolder,
				EventSource<PageTaskProducedEvent> eventSource, long newFileIgnoreSecThreshold) throws IOException {
			this.now = Calendar.getInstance().getTimeInMillis();
			this.sessionSource = sessionSource;
			this.taskHolder = taskHolder;
			this.eventSource = eventSource;
			this.newFileIgnoreSecThreshold = newFileIgnoreSecThreshold;
			this.taskAccessor = sessionSource.getContentAccessor().getPageTaskAccessor();
		}

		@Override
		void work(SourceDirectory sourceDirectory, PageID pageID, int pageNumber, int rowIndex) throws SessionStopException {
			OMRPageTask task = PageTaskFactory.createPageTask(sourceDirectory, pageNumber, pageID, this.sessionSource.getSessionID());
			if(isIgnorableTask(task)){
				return;
			}

			OMRPageTask preparedTask = prepareTask(task);
			if (preparedTask != null) {
				this.taskHolder.incrementNumTargetTasks(1);
				this.taskAccessor.put(preparedTask);
				this.taskHolder.addPreparedTask(preparedTask);
				this.eventSource.fireEvent(new PageTaskProducedEvent(this, preparedTask));
			}
			ContentIndexer contentIndexer = this.sessionSource.getContentIndexer();
			contentIndexer.putRowIndex(pageID, rowIndex);
		}

		@Override
		void startScanningSourceDirectory(SourceDirectory sourceDirectory) {
		}

		private OMRPageTask prepareTask(OMRPageTask task) throws SessionStopException {
			OMRPageTask storedTask = null;
			try {
				storedTask = this.taskAccessor.get(task.toString());
			} catch (Exception ignore) {
			}

			if (isOnceExecutedTask(storedTask)) {
				if (hasError(storedTask)) {

					this.eventSource.fireEvent(new PageTaskProducedEvent(this, storedTask, false));

					if (!isExecutionRequiredTaskWithError(task, storedTask)) {
						return null;
					}

				} else if (storedTask.getPageTaskResult() != null) {
					if (!isExecutionRequiredTask(task, storedTask)) {
						return null;
					}
				} else {
					this.numAdded++;
					Logger.getLogger("session").info("==========ADD\t" + task);
				}
			} else {
				this.numAdded++;
				Logger.getLogger("session").info("==========ADD\t" + task);
			}
			return task;
		}

		private boolean isOnceExecutedTask(Ticket storedTask) {
			return storedTask != null;
		}

		private boolean isIgnorableTask(OMRPageTask task) {
			if (isConcurrentFileModificationSuspected(task)) {
				if (INFO) {
					Logger.getLogger("source").info("IGNORE\t" + task);
				}
				return true;
			}
			return false;
		}

		private boolean isExecutionRequiredTaskWithError(AbstractPageTask task, AbstractPageTask storedTask) {
			if (this.sessionSource.getSessionID() == storedTask.getSessionID()) {
				if (INFO) {
					Logger.getLogger("source").info("IGNORE ERROR\t" + task);
				}
				return false;
			} else {
				this.numRetry++;
				if (INFO) {
					Logger.getLogger("source").info("==========RETRY ERROR\t" + task);
				}
				return true;
			}
		}

		private boolean isExecutionRequiredTask(Ticket task, AbstractPageTask storedTask) {
			if (this.sessionSource.getSessionID() == storedTask.getSessionID()) {
				if (INFO) {
					// Logger.getLogger("source").info("IGNORE\t" + task);
				}
				return false;
			} else {
				if (INFO) {
					// Logger.getLogger("source").info("REUSE\t" + task);
				}
				this.numReused++;
				this.taskHolder.setNumReusedTasks(this.numReused);
				return false;
			}
		}

		private boolean hasError(AbstractPageTask task) {
			return task.getPageTaskException() != null;
		}

		private boolean isConcurrentFileModificationSuspected(OMRPageTask task) {
			return this.newFileIgnoreSecThreshold != -1 && (this.now - task.getPageID().getFileResourceID().getLastModified()
					<= this.newFileIgnoreSecThreshold);
		}

		@Override
		void finishScan() {
			StringBuilder sb = new StringBuilder(64);
			sb.append("\nnumReused = " + this.numReused);
			sb.append("\nnumAdded = " + this.numAdded);
			sb.append("\nnumRetry = " + this.numRetry);
			Logger.getLogger("session").info("TaskProducer\n\t" + sb.toString());
		}
	}
}
