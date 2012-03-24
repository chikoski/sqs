/*

 MarkReaderApp.java

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

 Created on 2007/01/11

 */
package net.sqs2.omr.app;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.logging.Logger;

import net.sqs2.net.NetworkUtil;
import net.sqs2.omr.app.command.ClearSessionCommand;
import net.sqs2.omr.app.command.CloseSessionSourceCommand;
import net.sqs2.omr.app.command.CreateSessionCommand;
import net.sqs2.omr.app.command.ShutdownCommand;
import net.sqs2.omr.app.command.StopSessionCommand;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.MarkReaderConstants;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.session.service.AbstractTaskTracker;
import net.sqs2.omr.session.service.AbstractRemoteExecutorManager;
import net.sqs2.omr.session.service.AbstractRemoteTaskTracker;
import net.sqs2.omr.session.service.TaskTracker;
import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.omr.session.service.OutputEventRecieversFactory;
import net.sqs2.omr.session.service.PageTaskExecutable;
import net.sqs2.omr.session.service.RemoteSessionSourceServer;
import net.sqs2.omr.session.service.SessionSourceServerDispatcher;
import net.sqs2.omr.session.service.SessionSourceServerDispatcherImpl;
import net.sqs2.omr.session.service.SessionSourceServerImpl;
import net.sqs2.omr.session.service.TaskExecutor;

public class MarkReaderApp{
	
	private int rmiPort;
	private SessionSourceServerDispatcher sessionSourceServerDispatcher;
	private AbstractRemoteTaskTracker<OMRPageTask, SessionSourceServerDispatcher> remoteTaskTracker;
	private AbstractTaskTracker<OMRPageTask, SessionSourceServerDispatcher> localTaskTracker;
		
	public static final String SESSION_SERVICE_PATH = '/'+AppConstants.APP_ID + '/'+ MarkReaderConstants.SESSION_SERVICE_NAME;

	public class PageTaskExecutor extends TaskExecutor<OMRPageTask, SessionSourceServerDispatcher> implements Runnable {
		
		public PageTaskExecutor(AbstractTaskTracker<OMRPageTask, SessionSourceServerDispatcher>
				taskTracker,
				SessionSourceServerDispatcher sessionSourceServerDispacher){
			super(taskTracker, sessionSourceServerDispacher);
		}
		
		protected OMRPageTask leaseTask()throws RemoteException{
			return 
			this.getSessionSourceServerDispatcher().getServer().leaseTask(this.getSessionSourceServerDispatcher().getKey()); 
			// FIXME: may cause blocking
		}
		
		protected PageTaskExecutable createExecutable(OMRPageTask pageTask){	
			return new PageTaskExecutable(pageTask, this.getSessionSourceServerDispatcher());
		}
		
	}
	
	public MarkReaderApp(int rmiPort, boolean isLocalTaskExecutionEnabled) throws UnknownHostException, IOException {
		this.rmiPort = rmiPort;
		
		long key = new Random().nextLong();
		Logger.getLogger(getClass().getName()).info("MarkReaderController key=" + key);

		System.setProperty("java.rmi.server.hostname", getHostname());
		
		SessionSourceServerImpl sessionSourceServer = SessionSourceServerImpl.createInstance(key, MarkReaderConstants.CLIENT_TIMEOUT_IN_SEC);
		this.sessionSourceServerDispatcher = new SessionSourceServerDispatcherImpl(sessionSourceServer, null, key);
		this.remoteTaskTracker = createTaskTracker();
		
		if(isLocalTaskExecutionEnabled){
			this.localTaskTracker = createLocalTaskTracker();
		}
	}

	private AbstractTaskTracker<OMRPageTask, SessionSourceServerDispatcher> createLocalTaskTracker() {
		return new AbstractTaskTracker<OMRPageTask, SessionSourceServerDispatcher>
		("Local", this.remoteTaskTracker.getTaskTracker(),
				this.sessionSourceServerDispatcher){
			public TaskExecutor<OMRPageTask,SessionSourceServerDispatcher> createTaskExecutor(){
				return new PageTaskExecutor(this, getDispatcher());
			}
		};
	}

	private AbstractRemoteTaskTracker<OMRPageTask, SessionSourceServerDispatcher> createTaskTracker() {
		return new AbstractRemoteTaskTracker<OMRPageTask, SessionSourceServerDispatcher>(this.rmiPort, MarkReaderApp.SESSION_SERVICE_PATH){
			@Override
			public AbstractRemoteExecutorManager<OMRPageTask, SessionSourceServerDispatcher> createRemoteExecutorManager() {
				return new AbstractRemoteExecutorManager<OMRPageTask, SessionSourceServerDispatcher>(this.getTaskTracker(),
						MarkReaderConstants.PAGETASK_EXECUTORS_MAX_EXECUTORS){
					
					public AbstractTaskTracker<OMRPageTask, SessionSourceServerDispatcher>
							createTaskTracker(TaskTracker<OMRPageTask,SessionSourceServerDispatcher> taskTracker, 
							RemoteSessionSourceServer remoteSessionServer,
							long remoteKey){
						SessionSourceServerDispatcher dispacher = new SessionSourceServerDispatcherImpl(null, remoteSessionServer, remoteKey);
						return new AbstractTaskTracker<OMRPageTask, SessionSourceServerDispatcher>("Remote", taskTracker, dispacher){
							public TaskExecutor<OMRPageTask,SessionSourceServerDispatcher> createTaskExecutor(){
								return new PageTaskExecutor(this, getDispatcher());
							}
						};
					}
				};
			}
		};
	}	

	private String getHostname() {
		try {
			return NetworkUtil.Inet4.getHostAddress();
		} catch (SocketException ex) {
			return "localhost";
		}
	}

	public int getRMIPort() {
		return this.rmiPort;
	}

	public synchronized MarkReaderSession createSession(File sourceDirectoryRoot, OutputEventRecieversFactory outputEventConsumersFactory) throws IOException{
		return new CreateSessionCommand(remoteTaskTracker, sessionSourceServerDispatcher, sourceDirectoryRoot,
				outputEventConsumersFactory).call();
	}

	public void clearSession(File sourceDirectoryRoot) throws IOException{
		new ClearSessionCommand(sourceDirectoryRoot).call();
	}

	public synchronized void stopSession(File sourceDirectoryRoot){
		new StopSessionCommand(sourceDirectoryRoot).call();
	}

	public synchronized void closeSessionSource(File sourceDirectoryRoot) {
		new CloseSessionSourceCommand(sourceDirectoryRoot).call();
	}

	public synchronized void shutdown() {
		new ShutdownCommand(this.remoteTaskTracker, this.localTaskTracker).call();
	}
}
