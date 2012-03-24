/*

 MarkReaderSessions.java

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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sqs2.omr.model.OMRPageTask;

import org.apache.commons.collections15.Bag;
import org.apache.commons.collections15.bag.HashBag;

public class MarkReaderSessions{

	private static Map<Long, MarkReaderSession> sessionIDToSessionMap = new HashMap<Long, MarkReaderSession>();
	private static Map<File, MarkReaderSession> sourceDirectoryRootToSessionMap = new HashMap<File, MarkReaderSession>();
	private static Bag<File> sessionNameBag = new HashBag<File>();

	public static synchronized MarkReaderSession create(File sourceDirectoryRoot, AbstractRemoteTaskTracker<OMRPageTask, SessionSourceServerDispatcher> taskTracker, 
			SessionSourceServerDispatcher dispatcher,
			OutputEventRecieversFactory outputEventConsumersFactory) throws IOException{
		MarkReaderSession session = new MarkReaderSession(sourceDirectoryRoot, taskTracker, dispatcher, outputEventConsumersFactory);
		sessionNameBag.add(sourceDirectoryRoot);
		sourceDirectoryRootToSessionMap.put(sourceDirectoryRoot, session);
		MarkReaderSessions.add(session);
		return session;
	}

	public static MarkReaderSession get(long sessionID) {
		return sessionIDToSessionMap.get(sessionID);
	}

	public static Collection<MarkReaderSession> getSessions() {
		return sessionIDToSessionMap.values();
	}

	public static void add(MarkReaderSession session) {
		sessionIDToSessionMap.put(session.getSessionID(), session);
	}

	private static void remove(long sessionID) {
		sessionIDToSessionMap.remove(sessionID);
	}
	
	public static synchronized int countSessionsBySourceDirectory(File sourceDirectoryRootFile) {
		int numSameNameSessions = sessionNameBag.getCount(sourceDirectoryRootFile);
		return numSameNameSessions;
	}

	public static synchronized boolean contains(File sourceDirectoryRoot) {
		return sourceDirectoryRootToSessionMap.containsKey(sourceDirectoryRoot);
	}

	/**
	 * @param sourceDirectoryRoot
	 * @return null when there is no session you have specified.
	 */
	public static synchronized MarkReaderSession get(File sourceDirectoryRoot) {
		MarkReaderSession session = sourceDirectoryRootToSessionMap.get(sourceDirectoryRoot);
		return session;
	}

	public static synchronized void remove(File sourceDirectoryRoot) {
		MarkReaderSession session = sourceDirectoryRootToSessionMap.remove(sourceDirectoryRoot);
		if (session == null) {
			throw new RuntimeException("no such session:" + sourceDirectoryRoot.getAbsolutePath());
		}
		sessionNameBag.remove(sourceDirectoryRoot);
		MarkReaderSessions.remove(session.getSessionID());
	}

}
