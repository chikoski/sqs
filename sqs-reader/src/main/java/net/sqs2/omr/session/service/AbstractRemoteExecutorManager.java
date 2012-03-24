/*

 RemoteExecutorManager.java

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

import java.net.InetAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.logging.Logger;

import net.sqs2.net.NetworkUtil;
import net.sqs2.omr.model.Ticket;
import net.sqs2.util.StringUtil;

import org.apache.commons.collections15.map.LRUMap;

public abstract class AbstractRemoteExecutorManager<T extends Ticket, D extends ServerDispatcher> {

	private Map<String, AbstractTaskTracker<T,D>> remoteTaskTrackerMap;
	private TaskTracker<T,D> taskTracker;
	private int maximumNumberOfConnections;
	
	public AbstractRemoteExecutorManager(TaskTracker<T,D> taskTracker, int maximumNumberOfConnections) {
		this.taskTracker = taskTracker;
		this.maximumNumberOfConnections = maximumNumberOfConnections;
		this.remoteTaskTrackerMap = new LRUMap<String, AbstractTaskTracker<T,D>>(this.maximumNumberOfConnections) {
			private static final long serialVersionUID = 0L;

			@Override
			protected boolean removeLRU(LinkEntry<String, AbstractTaskTracker<T,D>> entry) {
				// release resources held by entry
				(entry.getValue()).stop();
				return true; // actually delete entry
			}
		};

		new RemoteTaskTrackerManager<T,D>(this.remoteTaskTrackerMap);
	}

	public synchronized void connect(String uri, long key, long sessionID) {

		try {
			if (NetworkUtil.isMyAddress(InetAddress.getAllByName(new URI(uri).getHost()))) {
				if(! AbstractTaskTracker.DEBUG_CLUSTER_MODE){ 
					return;
				}
			}

			if (this.maximumNumberOfConnections <= this.remoteTaskTrackerMap.size()) {
				Logger.getLogger("executor").info(
						"num remoteTaskBroders exceeded:" + this.remoteTaskTrackerMap.size());
				return;
			}

			AbstractTaskTracker<T,D> remoteTaskTracker = this.remoteTaskTrackerMap.get(uri);

			if (remoteTaskTracker != null
					&& (remoteTaskTracker.isConnected() == false || remoteTaskTracker.getKey() != key)) {
				this.remoteTaskTrackerMap.remove(uri);
				Logger.getLogger("executor").info("Remove old taskTracker=" + uri);
				remoteTaskTracker = null;
			}

			if (remoteTaskTracker == null) {
				Logger.getLogger("executor").info("Create new taskTracker=" + uri);
				remoteTaskTracker = createRemoteTaskTracker(uri, key, sessionID);
			} else {
				//Logger.getLogger("executor").info("Reuse connected taskTracker=" + uri);
			}
		} catch (SocketException ex) {
			Logger.getLogger("executor").severe("SocketException:" + uri);
		} catch (UnknownHostException ex) {
			Logger.getLogger("executor").severe("URISyntaxException:" + uri);
		} catch (URISyntaxException ex) {
			Logger.getLogger("executor").severe("UnknownHostException:" + uri);
		}
	}

	private AbstractTaskTracker<T,D> createRemoteTaskTracker(String uriString, long remoteKey, long sessionID) {
		try {
			URI uri = new URI(uriString);
			Registry registry = LocateRegistry.getRegistry(uri.getHost(), uri.getPort());
			Logger.getLogger(getClass().getName()).info("REGISTRY:\n"+StringUtil.join(registry.list(), "\n")+"\nLOOKUP:\n"+uri.getPath());
			RemoteSessionSourceServer remoteSessionService = (RemoteSessionSourceServer)registry.lookup(uri.getPath());
			long result = remoteSessionService.ping(remoteKey); // authentication
			if (AbstractTaskTracker.DEBUG_CLUSTER_MODE) {
				Logger.getLogger("executor").info("RemoteSessionService.URI=" + uri);
				Logger.getLogger("executor").info("Hello=" + result);
			}
			AbstractTaskTracker<T,D> remoteTaskTracker =
				createTaskTracker(this.taskTracker, remoteSessionService, remoteKey);
			
			this.remoteTaskTrackerMap.put(uriString, remoteTaskTracker);
			return remoteTaskTracker;
		} catch (ConnectException ex) {
			Logger.getLogger("executor").severe("ConnectException:" + uriString);
		} catch (RemoteException ex) {
			Logger.getLogger("executor").severe("RemoteException:" + uriString);
		} catch (NullPointerException ex) {
			Logger.getLogger("executor").severe("NullPointerException:" + uriString);
		} catch (NotBoundException ex) {
			ex.printStackTrace();
			Logger.getLogger("executor").severe("NotBoundException:" + uriString);
		} catch (URISyntaxException ex) {
			Logger.getLogger("executor").severe("URISyntaxException:" + ex.getMessage());
		}
		return null;
	}
	
	public void shutdown() {
		for (AbstractTaskTracker<T,D> sessionExecutor : this.remoteTaskTrackerMap.values()) {
			if (sessionExecutor != null) {
				sessionExecutor.shutdown();
			}
		}
	}
	
	public abstract AbstractTaskTracker<T,D> createTaskTracker(TaskTracker<T,D> taskTracker, 
			RemoteSessionSourceServer remoteSessionService, long remoteKey);
	

}
