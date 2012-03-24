package net.sqs2.omr.session;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sqs2.omr.app.NetworkPeer;
import net.sqs2.omr.task.TaskException;
import net.sqs2.omr.task.broker.TaskExecutorEnv;

public class MarkReaderSessions extends Sessions {
	
	private static Map<File,Session> sourceDirectoryRootToSessionMap = new HashMap<File,Session>();

	public static synchronized Session create(File sourceDirectoryRoot, NetworkPeer peer, 
			TaskExecutorEnv localTaskExecutorEnv, SessionThreadManager sessionThreadManager) 
	throws IOException,TaskException{
		
		Session session = new MarkReaderSession(sourceDirectoryRoot, peer, localTaskExecutorEnv, sessionThreadManager);
		Sessions.add(session);
		sourceDirectoryRootToSessionMap.put(sourceDirectoryRoot, session);
		try{
			Thread.sleep(1);
		}catch(InterruptedException ignore){}
		return session;
	}
	
	public static Session get(File sourceDirectoryRoot){
		Session session = sourceDirectoryRootToSessionMap.get(sourceDirectoryRoot);
		return session;
	}
	
	public static void remove(File sourceDirectoryRoot){
		Session session = sourceDirectoryRootToSessionMap.remove(sourceDirectoryRoot);
		if(session == null){
			throw new RuntimeException("no such session:"+sourceDirectoryRoot.getAbsolutePath());
		}
		Sessions.remove(session.getSessionID());
	}

}
