package net.sqs2.omr.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Sessions {
	
	private static Map<Long,Session> sessionIDToSessionMap = new HashMap<Long,Session>();

	
	public static Session get(long sessionID){
		Session session = sessionIDToSessionMap.get(sessionID);
		return session;
	}
	
	public static Collection<Session> getSessions(){
		return sessionIDToSessionMap.values();
	}
	
	public static void add(Session session){
		sessionIDToSessionMap.put(session.getSessionID(), session);
	}

	public static void remove(long sessionID){
		sessionIDToSessionMap.remove(sessionID);
	}
		
/*
	
	private static Sessions singleton = null;
	private File defaultSourceDirectoryRoot;

	private PageMasterFactory pageMasterFactory;
	private ConfigHandlerFactory configHandlerFactory;


	public Sessions(PageMasterFactory pageMasterFactory, 
			ConfigHandlerFactory configHandlerFactory,
			MulticastNetworkConnection multicastNetworkConnection, 
			String serviceName){
		if(singleton == null){
			singleton = this;
		}else{
			throw new RuntimeException("duplicate instantiation");
		}
		this.pageMasterFactory = pageMasterFactory;
		this.configHandlerFactory = configHandlerFactory;
		//this.serviceName = serviceName;
		//this.taskHolder = new TaskHolder();

		try{
			this.fanfare = new Fanfare(new URL(MarkReaderJarURIContext.getSoundBaseURI()+"whistle00.wav"));
		}catch(MalformedURLException ignore){}
	}
	

	public synchronized void stopSession() {
		
		this.notify();
		
		unexportSessionService();

		if(this.sessionThreadManager != null){
			this.sessionThreadManager.stop();
		}
		if(this.sessionService != null){
			this.sessionService.close();
		}
		
		this.sessionThreadManager = null;
	}

	public void shutdown() {
		unexportSessionService();
		if(this.sessionThreadManager != null){
			this.sessionThreadManager.shutdown();
		}
		PersistentCacheManager.shutdownAll();
		this.sessionService = null;
	}
	

	public synchronized RemoteSessionService getSessionService() {
		return this.sessionService;
	}
*/
}
