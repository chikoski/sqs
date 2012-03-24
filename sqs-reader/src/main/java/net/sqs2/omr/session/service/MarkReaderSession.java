/*

 MarkReaderSession.java

 Copyright 2009 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */
package net.sqs2.omr.session.service;

import java.io.File;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.sqs2.event.EventSource;
import net.sqs2.lang.GroupThreadFactory;
import net.sqs2.omr.master.FormMasterException;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.model.PageTaskException;
import net.sqs2.omr.model.PageTaskHolder;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SessionSourceImpl;
import net.sqs2.omr.model.SessionSourceState;
import net.sqs2.omr.model.SessionSources;
import net.sqs2.omr.session.event.SessionEvent;
import net.sqs2.omr.session.init.SessionSourceException;
import net.sqs2.omr.session.init.SessionSourceInitializationErrorEvent;
import net.sqs2.omr.session.init.SessionSourceInitializationEvent;
import net.sqs2.omr.session.init.SessionSourceInitializeCommand;
import net.sqs2.omr.session.init.SourceDirectoryInitializeCommand;
import net.sqs2.omr.session.model.SessionStopException;
import net.sqs2.omr.session.scan.PageTaskProducedEvent;
import net.sqs2.omr.session.scan.SessionSourceScannerTaskProducer;

public class MarkReaderSession{

	private static ExecutorService executorService = 
		Executors.newSingleThreadExecutor(new GroupThreadFactory("MarkReaderSession", 
				Thread.NORM_PRIORITY, true));
	private Future<?> sessionFuture;
	private SessionTaskDaemons sessionTaskDaemons;
	private SessionSource sessionSource = null;
	private SessionSourceServerDispatcher sessionSourceServerDispatcher;

	private AbstractRemoteTaskTracker<OMRPageTask, SessionSourceServerDispatcher> taskTracker;
	
	private long sessionID;
	private File sourceDirectoryRootFile;

	private PageTaskExecutionProgressModel model;
	private PageTaskHolder pageTaskHolder;
	
	private EventSource<SessionEvent> sessionEventChannel = new EventSource<SessionEvent>();
	private EventSource<SessionSourceInitializationEvent> sessionSourceInitializationEventChannel = new EventSource<SessionSourceInitializationEvent>();
	private EventSource<PageTaskProducedEvent> pageTaskProducedEventChannel = new EventSource<PageTaskProducedEvent>();
	private EventSource<PageTaskCommittedEvent> pageTaskCommitedEventChannel = new EventSource<PageTaskCommittedEvent>();
	
	protected PageTaskCommitRowGenerateService pageTaskCommitService;
	protected OutputEventRecieversFactory outputEventReceiversFactory;
	
	public MarkReaderSession(File sourceDirectoryRoot, 
			AbstractRemoteTaskTracker<OMRPageTask, SessionSourceServerDispatcher> taskTracker,
			SessionSourceServerDispatcher sessionSourceServerDispatcher,
			OutputEventRecieversFactory outputEventRecieversFactory)
			throws IOException {
		this.sourceDirectoryRootFile = sourceDirectoryRoot;
		this.taskTracker = taskTracker;
		this.sessionSourceServerDispatcher = sessionSourceServerDispatcher;
		this.sessionID = createUniqueSessionID();
		this.pageTaskHolder = new PageTaskHolder();
		this.model = new PageTaskExecutionProgressModel(this.sourceDirectoryRootFile, this.pageTaskHolder);
		this.pageTaskCommitService = new PageTaskCommitRowGenerateService(this);
		this.outputEventReceiversFactory = outputEventRecieversFactory;
	}
	
	private synchronized long createUniqueSessionID(){
		try{
			Thread.sleep(2);
		}catch(InterruptedException ex){}
		return System.currentTimeMillis();
	}
	
	public EventSource<PageTaskProducedEvent> getPageTaskProducedEventSource() {
		return pageTaskProducedEventChannel;
	}

	public EventSource<PageTaskCommittedEvent> getPageTaskCommitedEventSource() {
		return pageTaskCommitedEventChannel;
	}

	public EventSource<SessionEvent> getSessionEventSource(){
		return sessionEventChannel;
	}

	public EventSource<SessionSourceInitializationEvent> getSessionSourceInitializationEventSource(){
		return this.sessionSourceInitializationEventChannel;
	}
	
	class MarkReaderSessionWorker implements Callable<Void>{
	public Void call() {
		try {
			try{
				createSessionSource();
				sessionSource.setSessionSourceState(SessionSourceState.PROCESSING);

				scanSessionSourceAndProduceTasks();
				sessionSourceServerDispatcher.setInitialized();
				
				model.setTimeOfPageTaskProduced(System.currentTimeMillis());
				
				receiveAllPageTaskResults();
				
				model.setTimeOfPageTaskRecieveFinished(System.currentTimeMillis());
				
				startExportingResult();
				
				finishSession();
				return null;
				
			} catch (ExecutionException ex) {
				throw ex.getCause();
			}

		} catch (SessionSourceException ignore) {
			ignore.printStackTrace();
		} catch (SessionStopException ignore) {
			ignore.printStackTrace();
		} catch (CancellationException ignore) {
		} catch (IOException ignore) {
			ignore.printStackTrace();
		} catch (InterruptedException ignore) {
			ignore.printStackTrace();
		} catch (PageTaskException ignore) {
			ignore.printStackTrace();
		} catch (FormMasterException ex) {
			sessionSourceInitializationEventChannel.fireEvent(new SessionSourceInitializationErrorEvent(this, ex));
		} catch (Throwable ignore) {
			ignore.printStackTrace();
		}
		
		stopSession();
		
		return null;
	}
	}
	
	private void receiveAllPageTaskResults() throws IOException,
			InterruptedException, ExecutionException {
		this.pageTaskCommitService.setup(this.sourceDirectoryRootFile);
		Future<?> taskCommitFuture = this.sessionTaskDaemons.startAndWaitPageTaskCommitService(pageTaskCommitService); 
		taskCommitFuture.get();
	}

	private void createSessionSource() throws IOException, PageTaskException,
			SessionSourceException, FormMasterException, SessionStopException,
			InterruptedException, ExecutionException {
		this.sessionSource = createSessionSource(this.sessionID, this.sourceDirectoryRootFile);
	}
	
	private void startExportingResult() throws IOException, InterruptedException, ExecutionException {
		
		this.sessionEventChannel.fireEvent(new SessionEvent(this.sessionID));
		//FIXME: notifyExportingResultStarted();
		
		OutputResultTask sessionResultReportService = new OutputResultTask(this.sessionSource, this.outputEventReceiversFactory );
		Future<?> sessionResultReportFuture = this.sessionTaskDaemons.startAndWaitResultOutputWorker(sessionResultReportService);
		sessionResultReportFuture.get();
		
		this.sessionEventChannel.fireEvent(new SessionEvent(this.sessionID));
		//FIXME: notifyExportResultDirectoryFinished(this.sourceDirectoryRootFile);
	}

	private void scanSessionSourceAndProduceTasks() throws IOException, InterruptedException,
			ExecutionException {
		SessionSourceScannerTaskProducer taskProducer = new SessionSourceScannerTaskProducer(this.sessionSource,
				this.pageTaskProducedEventChannel,
				this.pageTaskHolder);
		Future<?> taskProducerFuture = this.sessionTaskDaemons.startPageTaskProducer(taskProducer);
		taskProducerFuture.get();
	}

	private SessionSource createSessionSource(long sessionID, File sourceDirectoryRootFile) throws IOException, PageTaskException,
			SessionSourceException, FormMasterException, SessionStopException,
			InterruptedException, ExecutionException {
		SessionSource sessionSource = SessionSources.create(sessionID, sourceDirectoryRootFile);
		new SourceDirectoryInitializeCommand(sourceDirectoryRootFile).call();
		SessionSourceInitializeCommand sessionSourceInitializeService = new SessionSourceInitializeCommand(sessionSource, sessionSourceInitializationEventChannel);
		Future<SessionSource> sessionSourceFactoryFuture = this.sessionTaskDaemons.startAndWaitSessionSourceFactory(sessionSourceInitializeService);
		return sessionSourceFactoryFuture.get();
	}

	public synchronized void startSession() throws IOException {
		this.sessionTaskDaemons = new SessionTaskDaemons(this.sessionID, this.taskTracker, this.sessionSourceServerDispatcher, this.pageTaskHolder);
		this.sessionTaskDaemons.start();
		this.sessionFuture = executorService.submit(new MarkReaderSessionWorker());// async execution call 
		
		this.sessionEventChannel.fireEvent(new SessionEvent(this.sessionID));
		//FIXME: notifySessionStarted(this.sourceDirectoryRootFile);
	}
	

	private void closeSession() {
		this.pageTaskHolder.stop();
		if(this.sessionTaskDaemons != null){
			this.sessionTaskDaemons.stop();
			this.sessionTaskDaemons.close();
			this.sessionTaskDaemons = null;
		}
		if (this.sessionFuture != null) {
			this.sessionFuture.cancel(true);
		}
	}

	
	public synchronized void stopSession() {
		if(this.sessionSource != null){
			this.sessionSource.setSessionSourceState(SessionSourceState.STOPPED);
		}
		closeSession();
		this.sessionEventChannel.fireEvent(new SessionEvent(this.sessionID));
		//FIXME: notifySessionStopped(this.sourceDirectoryRootFile);
	}
	
	
	public void finishSession() throws IOException{
		if(this.sessionSource != null){
			this.sessionSource.getContentAccessor().flush();
			this.sessionSource.setSessionSourceState(SessionSourceState.FINISHED);
		}
		closeSession();
		this.sessionEventChannel.fireEvent(new SessionEvent(this.sessionID));
		//FIXME: notifySessionFinished(this.model);
	}
	
	public synchronized void closeSessionSource() {
		try{
			if(this.sessionSource != null){
				this.sessionSource.resetSourceDirectorySerialID();
				SessionSources.close(sessionSource);
				MarkReaderSessions.remove(this.sourceDirectoryRootFile);
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public SessionSourceState getSessionSourceState(){
		if(this.sessionSource == null){
			return SessionSourceState.NOT_INITIALIZED;
		}else{
			return this.sessionSource.getSessionSourceState();
		}
	}
	
	public long getSessionID() {
		return this.sessionID;
	}
	
	public SessionSource getSessionSource() {
		return this.sessionSource;
	}

	public PageTaskExecutionProgressModel getPageTaskExecutionProgressModel() {
		return this.model;
	}

	public long getKey() {
		return this.sessionSourceServerDispatcher.getKey();
	}
	
	public PageTaskHolder getTaskHolder() {
		return this.pageTaskHolder;
	}

	public synchronized void notifyStoreTask(OMRPageTask pageTask) {
		this.pageTaskCommitedEventChannel.fireEvent(new PageTaskCommittedEvent(this, pageTask));
	}

	public File getSourceDirectoryRootFile() {
		return this.sourceDirectoryRootFile;
	}
	
	@Override
	public String toString(){
		return this.pageTaskHolder.toString();
	}
}
