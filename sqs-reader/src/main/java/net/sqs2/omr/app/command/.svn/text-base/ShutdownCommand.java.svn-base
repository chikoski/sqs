/*

 ShutdownAppService.java

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
package net.sqs2.omr.app.command;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;

import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.session.service.AbstractTaskTracker;
import net.sqs2.omr.session.service.AbstractRemoteTaskTracker;
import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.omr.session.service.MarkReaderSessions;
import net.sqs2.omr.session.service.SessionSourceServerDispatcher;
import net.sqs2.omr.session.service.SessionSourceServerImpl;

public class ShutdownCommand implements Callable<Void>{
	private AbstractRemoteTaskTracker<OMRPageTask, SessionSourceServerDispatcher> remoteTaskTracker;
	private AbstractTaskTracker<OMRPageTask, SessionSourceServerDispatcher> localTaskTracker;
	
	public ShutdownCommand(AbstractRemoteTaskTracker<OMRPageTask, SessionSourceServerDispatcher> remoteTaskTracker, 
			AbstractTaskTracker<OMRPageTask, SessionSourceServerDispatcher> localTaskTracker){
		this.remoteTaskTracker = remoteTaskTracker;
		this.localTaskTracker = localTaskTracker;
	}
	
	public Void call(){
		Collection<MarkReaderSession> sessions = MarkReaderSessions.getSessions();
		Iterator<MarkReaderSession> sessionsIterator = sessions.iterator();
		MarkReaderSession[] sessionsArray = new MarkReaderSession[sessions.size()];

		for (int i = 0; i < sessions.size(); i++) {
			sessionsArray[i] = sessionsIterator.next();
		}
		for (MarkReaderSession session : sessionsArray) {
			new StopSessionCommand(session.getSourceDirectoryRootFile()).call();
			new CloseSessionSourceCommand(session.getSourceDirectoryRootFile()).call();
		}
		this.remoteTaskTracker.shutdown();
		if(this.localTaskTracker != null){
			this.localTaskTracker.stop();
			this.localTaskTracker.shutdown();
		}
		unexportSessionService();
		SessionSourceServerImpl.shutdown();
		return null;
	}
	
	private void unexportSessionService() {
		try {
			SessionSourceServerImpl sessionService = SessionSourceServerImpl.getInstance();
			if (sessionService != null) {
				sessionService.close();
				UnicastRemoteObject.unexportObject(sessionService, false);
			}
		} catch (RemoteException ignore) {
		}
	}

}