/*

 SessionServiceImpl.java

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
package net.sqs2.omr.session;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.source.SessionSources;
import net.sqs2.omr.source.SourceDirectoryConfiguration;
import net.sqs2.omr.task.PageTask;
import net.sqs2.omr.task.TaskHolder;
import net.sqs2.omr.util.FileResource;
import net.sqs2.sound.SoundManager;
import net.sqs2.util.FileResourceID;

public class SessionServiceImpl extends UnicastRemoteObject implements RemoteSessionService,Serializable{
	private static final long serialVersionUID = 0L;

	private long key;
	
	private Map<String,ClientInfo> clientInfoMap = null;
	private ScheduledExecutorService clientConnectionManagerService = Executors.newScheduledThreadPool(1);
	private Future<?> clientConnectionManager;

	private static SessionServiceImpl singleton = null;
	
	class ClientInfo{
		String host;
		int numLease = 0;
		int numSubmit = 0;
		long lastMessageTime = 0L;
		
		ClientInfo(){
			updateMessageTime();
		}
		
		public int getNumLease() {
        	return numLease;
        }
		public int getNumSubmit() {
        	return numSubmit;
        }
		public void incrementNumLease(){
			numLease++;
		}
		public void incrementNumSubmit(){
			numSubmit++;
		}
		
		public void updateMessageTime(){
			this.lastMessageTime = System.currentTimeMillis();
		}
		
		public long lastMessageTime(){
			return this.lastMessageTime;
		}
		
		public boolean isOlderThan(long time){
			return this.lastMessageTime < time;
		}
	}
	
	public static SessionServiceImpl createInstance(long key, final int remoteClientTimeout) throws RemoteException {
		if(singleton != null){
			throw new RuntimeException("duplicate instantiation error");
		}
		return singleton = new SessionServiceImpl(key, remoteClientTimeout);
	}
	
	public static SessionServiceImpl getInstance() throws RemoteException {
		return singleton;
	}
	
	private SessionServiceImpl(long key, final int remoteClientTimeout) throws RemoteException {
		super();
		this.key = key;
		Logger.getLogger("session").info("Start SessionServiceImpl");
		this.clientInfoMap = Collections.synchronizedSortedMap(new TreeMap<String,ClientInfo>());
		this.clientConnectionManager = this.clientConnectionManagerService.scheduleWithFixedDelay(new Runnable(){
				public void run(){
					long time = System.currentTimeMillis() - remoteClientTimeout * 1000;
					for(Map.Entry<String, ClientInfo> entry: clientInfoMap.entrySet()){
						String clientHost = entry.getKey();
						if(entry.getValue().isOlderThan(time)){
							Logger.getAnonymousLogger().warning("disconnected: "+clientHost);
							clientInfoMap.remove(clientHost);
						}
					}
					
				}
			}, 10, 10, TimeUnit.SECONDS);
	}

	public void close(){
		this.clientConnectionManager.cancel(true);
		this.clientConnectionManagerService.shutdown();
	}
	
	public long ping(long key) throws RemoteException{
		checkKey(key);
		return System.currentTimeMillis();
	}

	private void checkKey(long key) throws RemoteException{
		if(this.key != key){
			Logger.getAnonymousLogger().warning(key+" != "+this.key);
			throw new RemoteException("Invalid Key");
		}
	}

	public synchronized PageTask leaseTask(long key) throws RemoteException {
		checkKey(key);
		try {
			String clientHost = null;
			try {
				clientHost = UnicastRemoteObject.getClientHost();
				ClientInfo clientInfo = getClientInfo(clientHost);
				clientInfo.incrementNumLease();
			} catch (ServerNotActiveException ex) {
			}
			
			for(Session session: Sessions.getSessions()){
				long sessionID = session.getSessionID();
				TaskHolder taskHolder = Sessions.get(sessionID).getTaskHolder();
				if(taskHolder.getNumPreparedTasks() == 0){
					continue;
				}
				PageTask task = taskHolder.addLeaseTask(); // may block
				if(task != null){
					task.setLeased();
					if(clientHost == null){
						Sessions.get(sessionID).getTaskHolder().addLeaseLocalTask(task);
					}else{
						Sessions.get(sessionID).getTaskHolder().addLeaseRemoteTask(task);
					}
				}
				return task;
			}
			return null;
		} catch (InterruptedException ex) {
			return null;
		}
	}

	public void submitPageTask(long key, long sessionID, PageTask task)throws RemoteException {
		checkKey(key);
		try {
			String clientHost = UnicastRemoteObject.getClientHost();
			ClientInfo clientInfo = getClientInfo(clientHost);
			clientInfo.incrementNumSubmit();
			clientInfo.updateMessageTime();
		} catch (ServerNotActiveException ex) {						
		}
		Sessions.get(sessionID).getTaskHolder().submitTask(task);
		SoundManager.getInstance().play("pyoro37_b.wav");	
	}

	private ClientInfo getClientInfo(String clientHost) {
	    ClientInfo clientInfo = this.clientInfoMap.get(clientHost);
	    if(clientInfo == null){
	    	clientInfo = new ClientInfo() ;
	    	this.clientInfoMap.put(clientHost, clientInfo);
	    }
	    return clientInfo;
    }

	public PageMaster getMaster(long key, long sessionID, FileResourceID fileResourceID)throws RemoteException{
		checkKey(key);
		return SessionSources.get(sessionID).getSessionSourceContentAccessor().getPageMasterAccessor().get(fileResourceID.getRelativePath());
	}

	public SourceDirectoryConfiguration getConfigration(long key, long sessionID, FileResourceID fileResourceID) throws RemoteException{
		checkKey(key);
		return SessionSources.get(sessionID).getSessionSourceContentIndexer().getConfigHandler(fileResourceID);
	}

	public FileResource getFileResource(long key, long sessionID, FileResourceID fileResourceID) throws RemoteException {
		checkKey(key);
		try{
			FileResource fileResource = SessionSources.get(sessionID).getSessionSourceContentAccessor().getFileContentCache().get(fileResourceID);
			return fileResource;
		}catch(IOException ex){
			return null;
		}		
	}

	public long getLastModified(long key, long sessionID, String relativePath) throws RemoteException{
		checkKey(key);
		return SessionSources.get(sessionID).getSessionSourceContentAccessor().getFileContentCache().getLastModified(relativePath);
	}

	public int getNumRemoteSlaveExecutors()throws RemoteException{
		return this.clientInfoMap.size();
	}

	public void removeRemoteSlaveExecutor(String clientHost)throws RemoteException{
		this.clientInfoMap.remove(clientHost);
	}
	
	public boolean isPreparingTasks(long sessionID){
		return SessionSources.get(sessionID).isPreparing();
	}

	public boolean existsRunningLocalSessions(){
		for(Session session: Sessions.getSessions()){
			if(0 < session.getTaskHolder().getNumPreparedTasks()){
				return true;
			}
		}
		return false;
	}
}
