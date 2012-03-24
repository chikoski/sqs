/*

 RemoteTaskBrokerManager.java

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

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.logging.Logger;

import net.sqs2.net.NetworkUtil;
import net.sqs2.omr.session.RemoteSessionService;

import org.apache.commons.collections15.map.LRUMap;

public class RemoteTaskExecutorManager {

	private static boolean DEBUG = true; 
	private Map<String,TaskBroker> remoteTaskBrokerMap;
	TaskExecutorPeer manager;
	private int maximumNumberOfConnections;

	public RemoteTaskExecutorManager(TaskExecutorPeer manager, int maximumNumberOfConnections){
		this.manager = manager;
		this.maximumNumberOfConnections = maximumNumberOfConnections;
		this.remoteTaskBrokerMap = new LRUMap<String,TaskBroker>(this.maximumNumberOfConnections){
			private static final long serialVersionUID = 0L;
			protected boolean removeLRU(LinkEntry<String,TaskBroker> entry) {
				// release resources held by entry
				((TaskBroker)entry.getValue()).stop();
				return true;  // actually delete entry
			}
		};
	}

	public synchronized void connect(String uri, long key, long sessionID){
		TaskBroker remoteTaskBroker = this.remoteTaskBrokerMap.get(uri);
		if(remoteTaskBroker == null){
			remoteTaskBroker = createRemoteTaskBroker(uri, key, sessionID);
			if(remoteTaskBroker != null){
				this.remoteTaskBrokerMap.put(uri, remoteTaskBroker);
			}
		}else if(remoteTaskBroker.isConnected() == false || remoteTaskBroker.getKey() != key){
			this.remoteTaskBrokerMap.remove(uri);
			Logger.getLogger("executor").info("Remove old taskBroker="+uri);
		}else{
			Logger.getLogger("executor").info("reuse="+uri);
		}
	}

	private TaskBroker createRemoteTaskBroker(String uriString, long remoteKey, long sessionID){
		try{
			URI uri = new URI(uriString);
			
			if(NetworkUtil.isMyAddress(InetAddress.getAllByName(uri.getHost()))){
				return null;
			}
			
			RemoteSessionService sessionService = (RemoteSessionService)Naming.lookup(uri.toString()); // connect to remote SessionService
			long result = sessionService.ping(remoteKey); // authentication
			if(DEBUG){
				Logger.getLogger("executor").info("RemoteSessionService.URI="+uri);
				Logger.getLogger("executor").info("Hello="+result);
			}
			TaskBroker remoteTaskBroker = new TaskBroker("Remote", this.manager, new TaskExecutorEnv(null, sessionService, remoteKey, sessionID));
			return remoteTaskBroker;
		}catch(UnknownHostException ex){
			Logger.getLogger("executor").severe("UnknownHostException:"+ex.getMessage());
		}catch(SocketException ex){
			Logger.getLogger("executor").severe("SocketException:"+ex.getMessage());
		}catch(URISyntaxException ex){
			Logger.getLogger("executor").severe("URISyntaxException:"+ex.getMessage());
		}catch(ConnectException ex){
			Logger.getLogger("executor").severe("ConnectException:"+ex.getMessage());
		}catch(RemoteException ex){
			Logger.getLogger("executor").severe("RemoteException:"+ex.getMessage());
		}catch(MalformedURLException ex){
			Logger.getLogger("executor").severe("MalformedURLException:"+ex.getMessage());
		}catch(NotBoundException ex){
			Logger.getLogger("executor").severe("NotBoundException:"+ex.getMessage());
		}
		return null;
	}

	public void shutdown(){
		for(TaskBroker sessionExecutor : this.remoteTaskBrokerMap.values()){
			if(sessionExecutor != null){
				sessionExecutor.shutdown();
			}
		}
	}
}
