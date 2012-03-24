package net.sqs2.omr.source;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.sqs2.omr.master.FormMasterFactories;
import net.sqs2.omr.master.PageMasterException;
import net.sqs2.omr.master.PageMasterFactory;
import net.sqs2.omr.task.TaskException;

public class SessionSources {
	private static Map<Long,SessionSource> sessionIDToSessionSourceMap = new HashMap<Long,SessionSource>();
	
	public static SessionSource get(long sessionID){
		SessionSource sessionSource = sessionIDToSessionSourceMap.get(sessionID);
		/*
		if(sessionSource == null){
			throw new RuntimeException("no sessionID: "+sessionID);
		}
		*/
		return sessionSource;
	}
	
	private static void add(SessionSource sessionSource){
		sessionIDToSessionSourceMap.put(sessionSource.getSessionID(), sessionSource);
	}

	public static void remove(long sessionID){
		sessionIDToSessionSourceMap.remove(sessionID);
	}
	
	public static SessionSource create(long sessionID, File directoryRoot,
			FormMasterFactories formMasterFactorySet, 
			boolean isSearchPageMasterFromAncestorDirectory,
			ConfigHandlerFactory configHandlerFactory,
			SourceInitializationMonitor sessionSourceInitializerMonitor) throws IOException,TaskException,SessionSourceException, PageMasterException{
		SessionSourceInitializer sessionSourceInitializer = new SessionSourceInitializer(formMasterFactorySet, isSearchPageMasterFromAncestorDirectory);
		SessionSource sessionSource = new SessionSource(sessionID, directoryRoot);
		sessionSourceInitializer.initialize(sessionSource, sessionSourceInitializerMonitor);
		Logger.getAnonymousLogger().info("create new sessionID: "+sessionID);
		SessionSources.add(sessionSource);
		return sessionSource;
	}
}
