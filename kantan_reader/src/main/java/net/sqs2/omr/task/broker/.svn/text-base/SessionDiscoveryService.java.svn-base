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
package net.sqs2.omr.task.broker;

import java.io.IOException;
import java.net.UnknownHostException;

import net.sqs2.net.MulticastNetworkConnection;
import net.sqs2.net.RMIRegistryMulticastDiscoveryService;

public class SessionDiscoveryService extends RMIRegistryMulticastDiscoveryService{

	private RemoteTaskExecutorManager remoteTaskBrokerManager;

	public SessionDiscoveryService(MulticastNetworkConnection con,
			int threadPriority,
			int bufferLength, 
			RemoteTaskExecutorManager remoteTaskBrokerManager)throws UnknownHostException,IOException {		
		super(con, bufferLength, threadPriority);
		this.remoteTaskBrokerManager = remoteTaskBrokerManager;
		discover();
	}

	public void processMessage(String omrSessionServiceURL, long key, long sessionID){
		//System.err.println(".");//Logger
		this.remoteTaskBrokerManager.connect(omrSessionServiceURL, key, sessionID);
	}
}
