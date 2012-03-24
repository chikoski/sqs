/**
 *  SessionExecutorResource.java

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

 Created on 2007/01/31
 Author hiroya
 */

package net.sqs2.omr.task.broker;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.session.RemoteSessionService;
import net.sqs2.omr.session.SessionServiceImpl;
import net.sqs2.omr.source.SourceDirectoryConfiguration;
import net.sqs2.omr.task.AbstractTask;
import net.sqs2.omr.util.FileResource;
import net.sqs2.util.FileResourceID;

import org.apache.commons.collections15.map.LRUMap;

public class TaskExecutorEnv{

	public static final int IMAGE_BYTEARRAY_CACHE_SIZE = 3; // size
	public static final long NOT_INITIALIZED = -1L;
	
	private SessionServiceImpl localSessionService;
	private RemoteSessionService remoteSessionService;
	private TaskBroker taskBroker;
	private long key;
	//private long sessionID = TaskExecutorEnv.NOT_INITIALIZED;
	
	private boolean hasInitialized = false;
	
	private Map<FileResourceID, PageMaster> pageMasterCache = new LRUMap<FileResourceID, PageMaster>();
	private Map<FileResourceID, SourceDirectoryConfiguration> configHandlerCache = new HashMap<FileResourceID, SourceDirectoryConfiguration>();
	private Map<FileResourceID, FileResource> fileResourceCache = new LRUMap<FileResourceID, FileResource>();

	public TaskExecutorEnv(SessionServiceImpl localSessionService, RemoteSessionService remoteSessionService, long key, long sessionID){
		this.localSessionService = localSessionService;	
		this.remoteSessionService = remoteSessionService;	
		this.key = key;
		//this.sessionID = sessionID;
	}

	public TaskExecutorEnv(SessionServiceImpl localSessionService, RemoteSessionService remoteSessionService, long key){
		this.localSessionService = localSessionService;	
		this.remoteSessionService = remoteSessionService;	
		this.key = key;
	}
	
	
	public void setInitialized(){
		this.hasInitialized = true;
		/*
		//this.sessionID = sessionID;
		if(this.sessionID == TaskExecutorEnv.NOT_INITIALIZED){
			this.sessionID = sessionID;
		}else if(this.sessionID != sessionID){
			throw new RuntimeException("multiple initialization");
		}
		
	*/
	
	}

	public boolean hasInitialized(){
		return this.hasInitialized;
	}

	public long getKey(){
		return this.key;
	}

	/*
	public long getSessionID(){
		return this.sessionID;
	}
	*/
	
	public void setConnected(boolean isConnected){
		this.taskBroker.setConnected(isConnected);
	}

	boolean isRemote(){
		return (this.remoteSessionService != null);
	}


	SessionServiceImpl getLocalSessionService(){
		return this.localSessionService;
	}
	
	RemoteSessionService getSessionService(){
		if(this.localSessionService != null){
			return this.localSessionService;
		}else if(this.remoteSessionService != null){
			return this.remoteSessionService;
		}else{
			throw new RuntimeException("localSessionService == null and remoteSessionService == null");
		}
	}

	public byte[] getImageByteArray(long sessionID, FileResourceID fileResourceID)throws RemoteException{
		synchronized(this.fileResourceCache){
			FileResource fileResource = this.fileResourceCache.get(fileResourceID);
			if(fileResource == null){
				fileResource = getSessionService().getFileResource(getKey(), sessionID, fileResourceID); 
				this.fileResourceCache.put(fileResourceID, fileResource);
			}
			return fileResource.getBytes();
		}
	}

	public PageMaster getPageMaster(long sessionID, AbstractTask pageTask)throws RemoteException{
		return getPageMaster(sessionID, pageTask.getMasterFileResourceID());
	}

	public PageMaster getPageMaster(long sessionID, FileResourceID fileResourceID)throws RemoteException{
		synchronized(this.pageMasterCache){
			PageMaster master = this.pageMasterCache.get(fileResourceID); 
			if(master == null){		    
				master = (PageMaster)getSessionService().getMaster(getKey(), sessionID, fileResourceID);
				this.pageMasterCache.put(fileResourceID, master);
			}
			return master;
		}
	}

	public SourceDirectoryConfiguration getConfiguration(long sessionID, FileResourceID fileResourceID)throws RemoteException{
		synchronized(this.configHandlerCache){
			SourceDirectoryConfiguration configHandler = this.configHandlerCache.get(fileResourceID); 
			if(configHandler == null){
				configHandler = getSessionService().getConfigration(getKey(), sessionID, fileResourceID);
				this.configHandlerCache.put(fileResourceID, configHandler);
			}
			return configHandler;
		}
	}

	void close(){
		if(this.remoteSessionService != null){
			this.remoteSessionService = null;
		}
		if(this.localSessionService != null){
			this.localSessionService = null;
		}
	}
}