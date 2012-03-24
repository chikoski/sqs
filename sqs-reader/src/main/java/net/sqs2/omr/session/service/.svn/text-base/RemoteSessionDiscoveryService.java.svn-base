/*

 SessionDiscoveryService.java

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
package net.sqs2.omr.session.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import net.sqs2.net.MulticastNetworkConnection;
import net.sqs2.net.RMIRegistryMulticastDiscoveryService;
import net.sqs2.omr.model.Ticket;

public abstract class RemoteSessionDiscoveryService<T extends Ticket, D extends ServerDispatcher>
extends RMIRegistryMulticastDiscoveryService {

	private AbstractRemoteExecutorManager<T,D> remoteExecutorManager;

	public RemoteSessionDiscoveryService(MulticastNetworkConnection connection, int threadPriority,
			int bufferLength, 
			AbstractRemoteExecutorManager<T,D> remoteExecutorManager)
	throws UnknownHostException,
			IOException {
		super(connection, bufferLength, threadPriority);
		this.remoteExecutorManager = remoteExecutorManager;
		startDiscovery();
	}

	@Override
	public void processMessage(InetAddress addr, String rmiURL, long key, long sessionID){	
		if(isAcceptableMessage(addr, rmiURL, key, sessionID)){
			this.remoteExecutorManager.connect(rmiURL, key, sessionID);
		}
	}

	public abstract boolean isAcceptableMessage(InetAddress addr, String message, long key, long sessionID);
	
}
