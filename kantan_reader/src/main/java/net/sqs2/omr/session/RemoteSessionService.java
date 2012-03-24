/*

 SessionService.java

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

import java.rmi.Remote;
import java.rmi.RemoteException;

import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.source.SourceDirectoryConfiguration;
import net.sqs2.omr.task.PageTask;
import net.sqs2.omr.util.FileResource;
import net.sqs2.util.FileResourceID;

public interface RemoteSessionService extends Remote {

	public long ping(long key) throws RemoteException;
	public int getNumRemoteSlaveExecutors()throws RemoteException;

	public PageTask leaseTask(long key) throws RemoteException;

	public PageMaster getMaster(long key, long sessionID, FileResourceID fileResourceID)throws RemoteException;
	public SourceDirectoryConfiguration getConfigration(long key, long sessionID, FileResourceID fileResourceID) throws RemoteException;
	public FileResource getFileResource(long key, long sessionID, FileResourceID fileResourceID) throws RemoteException;

	public void submitPageTask(long key, long sessionID, PageTask pageTask) throws RemoteException;

}
