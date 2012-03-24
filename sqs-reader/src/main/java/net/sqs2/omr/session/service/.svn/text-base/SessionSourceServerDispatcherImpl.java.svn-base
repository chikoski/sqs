/**
 *  SessionSourceServerDispatcherImpl.java

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

package net.sqs2.omr.session.service;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.SourceDirectoryConfiguration;
import net.sqs2.omr.util.FileContents;
import net.sqs2.util.FileResourceID;

import org.apache.commons.collections15.map.LRUMap;

public class SessionSourceServerDispatcherImpl extends ServerDispatcherImpl implements SessionSourceServerDispatcher{

	public static final int IMAGE_BYTEARRAY_CACHE_SIZE = 3; // size
	private Map<FileResourceID, FormMaster> formMasterCache = new LRUMap<FileResourceID, FormMaster>();
	private Map<FileResourceID, SourceDirectoryConfiguration> configHandlerCache = new HashMap<FileResourceID, SourceDirectoryConfiguration>();
	private Map<FileResourceID, FileContents> fileResourceCache = new LRUMap<FileResourceID, FileContents>(32);

	public SessionSourceServerDispatcherImpl(LocalSessionSourceServer localSessionService, RemoteSessionSourceServer remoteSessionService,
			long key){
		super(localSessionService, remoteSessionService, key);
	}
	
	@Override
	public byte[] getFileContentByteArray(long sessionID, FileResourceID fileResourceID) throws RemoteException, IOException {
		synchronized (this.fileResourceCache) {
			FileContents fileResource = this.fileResourceCache.get(fileResourceID);
			if (fileResource == null) {
				fileResource = getServer().getFileContentByteArray(getKey(), sessionID, fileResourceID);
				this.fileResourceCache.put(fileResourceID, fileResource);
			}
			return fileResource.getBytes();
		}
	}

	@Override
	public FormMaster getFormMaster(long sessionID, FileResourceID fileResourceID) throws RemoteException {
		synchronized (this.formMasterCache) {
			FormMaster master = this.formMasterCache.get(fileResourceID);
			if (master == null) {
				master = getServer().getFormMaster(getKey(), sessionID, fileResourceID);
				this.formMasterCache.put(fileResourceID, master);
			}
			return master;
		}
	}

	@Override
	public SourceDirectoryConfiguration getConfiguration(long sessionID, FileResourceID fileResourceID) throws RemoteException {
		synchronized (this.configHandlerCache) {
			SourceDirectoryConfiguration configHandler = this.configHandlerCache.get(fileResourceID);
			if (configHandler == null) {
				configHandler = getServer().getConfigration(getKey(), sessionID, fileResourceID);
				this.configHandlerCache.put(fileResourceID, configHandler);
			}
			return configHandler;
		}
	}

}
