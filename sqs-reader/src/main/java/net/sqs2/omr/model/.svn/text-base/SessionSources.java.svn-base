package net.sqs2.omr.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.collections15.map.ListOrderedMap;

public class SessionSources {
	private static ListOrderedMap<Long, SessionSource> sessionIDToSessionSourceMap = new ListOrderedMap<Long, SessionSource>();

	public static SessionSource getInstance(long sessionID) {
		SessionSource sessionSource = sessionIDToSessionSourceMap.get(sessionID);
		return sessionSource;
	}
	
	public static SessionSource getInitializedInstance(long sessionID) {
		SessionSource sessionSource = getInstance(sessionID);
		if(! sessionSource.hasInitialized()){
			throw new RuntimeException("NOT_INITIALIZED SessionSource:"+sessionSource.getRootDirectory().getAbsolutePath());
		}
		return sessionSource;
	}
	
	public static SessionSource create(long sessionID, File rootDirectory) throws IOException{
		if(getInitializedInstance(sessionID) != null){
			throw new RuntimeException("already existed sessionID:"+sessionID);
		}
		SessionSource sessionSource = new SessionSourceImpl(sessionID, rootDirectory);
		sessionIDToSessionSourceMap.put(sessionSource.getSessionID(), sessionSource);
		return sessionSource;
	}
	
	public static void close(long sessionID) throws IOException{
		SessionSource sessionSource = getInitializedInstance(sessionID); 
		if(sessionSource == null){
			throw new RuntimeException("already existed sessionID:"+sessionID);
		}
		close(sessionSource);
	}
	
	public static void close(SessionSource sessionSource) throws IOException{
		sessionSource.close();
		sessionIDToSessionSourceMap.remove(sessionSource.getSessionID());
	}

	public static List<Long> getSessionIDList(){
		return sessionIDToSessionSourceMap.asList();
	}
	
}
