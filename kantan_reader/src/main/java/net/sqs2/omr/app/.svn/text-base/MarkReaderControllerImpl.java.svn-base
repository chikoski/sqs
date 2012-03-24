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

import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import net.sqs2.net.NetworkUtil;
import net.sqs2.omr.cache.CacheConstants;
import net.sqs2.omr.httpd.SQSHttpdManager;
import net.sqs2.omr.session.MarkReaderSessions;
import net.sqs2.omr.session.Session;
import net.sqs2.omr.session.SessionException;
import net.sqs2.omr.session.SessionServiceImpl;
import net.sqs2.omr.session.SessionThreadManager;
import net.sqs2.omr.session.Sessions;
import net.sqs2.omr.task.TaskException;
import net.sqs2.omr.task.broker.TaskBroker;
import net.sqs2.omr.task.broker.TaskExecutorEnv;
import net.sqs2.swing.process.RemoteWindowDecorator;

import org.apache.commons.collections15.Bag;
import org.apache.commons.collections15.bag.HashBag;

public class MarkReaderControllerImpl implements MarkReaderController{
    
	private TaskExecutorEnv localTaskExecutorEnv;
	private NetworkPeer networkPeer;
	private TaskBroker localTaskBroker;
	protected SessionThreadManager sessionThreadManager = null;

	private static Map<Integer,MarkReaderControllerImpl> map = new HashMap<Integer,MarkReaderControllerImpl>();
	private Bag<File> sessionNameBag = new HashBag<File>();

	transient Preferences prefs = null;

	public synchronized static MarkReaderControllerImpl getInstance(int rmiPort)throws UnknownHostException, IOException {
		MarkReaderControllerImpl app = map.get(rmiPort);
		if(app == null){
			app = new MarkReaderControllerImpl(rmiPort);
			map.put(rmiPort, app);
		}
		return app;
	}
	
	private MarkReaderControllerImpl(int rmiPort) throws UnknownHostException, IOException {
		
        System.setProperty("java.rmi.server.hostname", getHostname());

        long key = MarkReaderConstants.RANDOM.nextLong();
		Logger.getAnonymousLogger().warning("master key="+key);

		SessionServiceImpl sessionService = SessionServiceImpl.createInstance(key, MarkReaderConstants.CLIENT_TIMEOUT_IN_SEC);
		this.localTaskExecutorEnv = new TaskExecutorEnv(sessionService, null, key);

		this.networkPeer = new NetworkPeer(rmiPort, MarkReaderConstants.SESSION_SERVICE_PATH);

		this.sessionThreadManager = new SessionThreadManager();

		exportSessionService();

        this.localTaskBroker = new TaskBroker("Local", this.networkPeer.getTaskExecutorPeer(), localTaskExecutorEnv);        
	}
	
	public NetworkPeer getNetworkPeer(){
		return this.networkPeer;
	}

	private String getHostname(){
        try {
        	return NetworkUtil.getInet4HostAddress();
        } catch (SocketException ex) {
            return "localhost";
        }
	}
	
	private void exportSessionService() {
		try{
			SessionServiceImpl sessionService = SessionServiceImpl.getInstance();
			if(sessionService != null){
				sessionService.close();
				Naming.rebind(MarkReaderConstants.SESSION_SERVICE_PATH, sessionService);
			}
		}catch(IOException ignore){
		}
	}

	private void unexportSessionService() {
		try{
			SessionServiceImpl sessionService = SessionServiceImpl.getInstance();
			if(sessionService != null){
				sessionService.close();
				UnicastRemoteObject.unexportObject(sessionService, false);
			}
		}catch(RemoteException ignore){
		}
	}


	/*
	private String getPageMasterFactoryClassName(){
		return net.sqs2.exigrid.master.MultiFormMasterFactory.class.getCanonicalName();
	}

	private String getConfigHandlerFactoryClassName(){ 
		return net.sqs2.omr.source.config.ConfigHandlerFactoryImpl.class.getCanonicalName();
	}

	private String getSessionExecutorCoreClassName(){
		return net.sqs2.omr.logic.PageTaskExecutorCoreImpl.class.getCanonicalName();
	}
	
	private String getTaskConsumerClassName(){
		return net.sqs2.exigrid.task.consumer.TaskConsumerRowGenerator.class.getCanonicalName();
	}

	private String getSessionClassName() {
		return net.sqs2.exigrid.session.MarkReaderSession.class.getCanonicalName();
	}
	*/
	
	
	public synchronized Session createOrReuseSession(File sourceDirectoryRoot)throws SessionException,IOException,TaskException{
		if(sourceDirectoryRoot == null || ! sourceDirectoryRoot.isDirectory() || sourceDirectoryRoot.getName().endsWith(CacheConstants.CACHE_ROOT_DIRNAME)){
			throw new SessionException(sourceDirectoryRoot, "DIRECTORY ERROR in: "+sourceDirectoryRoot.getAbsolutePath());
		}
		Session session = MarkReaderSessions.get(sourceDirectoryRoot);
		final File resultDirectoryRoot = createResultDirectoryRoot(sourceDirectoryRoot);
		if(! checkDirectoryExistence(sourceDirectoryRoot, resultDirectoryRoot)){
			throw new SessionException(sourceDirectoryRoot, "WRITE ERROR in: "+sourceDirectoryRoot.getAbsolutePath());
		}
		/*
		if(! checkSeemsValidDirectory(sourceDirectoryRoot)){
			throw new SourceDirectoryException(sourceDirectoryRoot, "NO_PDF in: "+sourceDirectoryRoot.getAbsolutePath());
		}*/
		storeSourceDirectoryRootInPrefs(sourceDirectoryRoot);
		if(session != null){
			// PREVIOUS_SESSION			
			return session;
		}
		return MarkReaderSessions.create(sourceDirectoryRoot, this.networkPeer, this.localTaskExecutorEnv, this.sessionThreadManager);
	}

	public synchronized int countSessionsBySourceDirectory(File sourceDirectoryRootFile){
		int numSameNameSessions = sessionNameBag.getCount(sourceDirectoryRootFile);
		return numSameNameSessions;
	}

	public synchronized void userClear(File sourceDirectoryRoot) throws IOException,TaskException{
		Session session = MarkReaderSessions.get(sourceDirectoryRoot);
		session.resetCache(sourceDirectoryRoot);
	}

	public synchronized void userStart(File sourceDirectoryRootFile) throws IOException,TaskException{		
		Session session = MarkReaderSessions.get(sourceDirectoryRootFile);
		this.localTaskBroker.start();
		session.startSession();
		this.sessionNameBag.add(sourceDirectoryRootFile);
	}

	public synchronized void userStop(File sourceDirectoryRootFile) {
		this.localTaskBroker.stop();
		Session session = MarkReaderSessions.get(sourceDirectoryRootFile);
		session.stopSession();
		session.notifySessionStopped(sourceDirectoryRootFile);
	}
	
	public synchronized void userClose(File sourceDirectoryRoot){
		Session session = MarkReaderSessions.get(sourceDirectoryRoot);
		if(! session.isStoppedOrFinished()){
			userStop(sourceDirectoryRoot);
		}
		this.sessionNameBag.remove(sourceDirectoryRoot);
		MarkReaderSessions.remove(sourceDirectoryRoot);
	}

	public synchronized void userShutdown() {
		Collection<Session> sessions = Sessions.getSessions();
		Iterator<Session> sessionsIterator = sessions.iterator();
		Session[] sessionsArray = new Session[sessions.size()];
		
		for(int i = 0 ; i < sessions.size(); i++){
			sessionsArray[i] = sessionsIterator.next();
		}
		for(Session session: sessionsArray){
			userStop(session.getSourceDirectoryRootFile());
			userClose(session.getSourceDirectoryRootFile());
		}
		this.networkPeer.shutdown();
		this.localTaskBroker.shutdown();
		
		unexportSessionService();
	}
	
	public void userExit(){
		Window window = RemoteWindowDecorator.inactivate(getNetworkPeer().getRMIPort());
		window.setVisible(false);
		try{
			SQSHttpdManager.getEXIgridHttpd().stop();
		}catch(Exception ignore){
			ignore.printStackTrace();
		}
		userShutdown();
		System.runFinalization();
		System.exit(0);
	}
	
	protected File createResultDirectoryRoot(File sourceDirectoryRoot) {
		return new File(sourceDirectoryRoot.getAbsoluteFile(), CacheConstants.CACHE_ROOT_DIRNAME);
	}
	
	public Preferences getPreferences() {
		if(this.prefs == null){
			this.prefs = Preferences.userNodeForPackage(this.getClass());
		}
		return this.prefs;
	}

	private void storeSourceDirectoryRootInPrefs(final File sourceDirectoryRoot) {
		getPreferences().put(MarkReaderConstants.SOURCE_DIRECTORY_ROOT_KEY, sourceDirectoryRoot.getAbsolutePath());
		try{
			getPreferences().flush();
		}catch(IllegalArgumentException ignore){
			Logger.getLogger("swing").info(ignore.getMessage());
		}catch(BackingStoreException ignore){
			Logger.getLogger("swing").severe(ignore.getMessage());
		}
	}
		
	private boolean checkSeemsValidDirectory(final File sourceDirectoryRoot){
		for(String filename: sourceDirectoryRoot.list()){
			if(filename.endsWith(".pdf")){
				return true;
			}
		}
		
		return false;
	}
		
	private boolean checkDirectoryExistence(final File sourceDirectoryRoot, final File resultDirectoryRoot) {
		if(! sourceDirectoryRoot.exists() || ! sourceDirectoryRoot.isDirectory() || ! sourceDirectoryRoot.canRead()
				 || ! sourceDirectoryRoot.canWrite()){ //sourceDirectoryRoot.getName().endsWith(PageTaskResult.RESULT_FOLDER_SUFFIX)
			return false;
		}
		return true;
	}

	public synchronized void userOpen(Session session) throws TaskException,IOException{
		File sourceDirectoryRootFile = session.getSourceDirectoryRootFile();
		if(! session.isNew()){
			if(session.isStoppedOrFinished()){
				userStart(sourceDirectoryRootFile);
				return;
			}
			return;
		}
		userStart(sourceDirectoryRootFile);
	}

}
