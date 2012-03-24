/*

 AbstractRemoteTaskTracker.java

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
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.logging.Logger;

import net.sqs2.net.MulticastNetworkConnection;
import net.sqs2.net.NetworkUtil;
import net.sqs2.omr.base.MarkReaderJarURIContext;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.MarkReaderConstants;
import net.sqs2.omr.model.Ticket;
import net.sqs2.sound.SoundManager;

public abstract class AbstractRemoteTaskTracker<T extends Ticket, D extends ServerDispatcher> {

	private int rmiPort;
	private String rmiBindingName;

	private RemoteSessionDiscoveryService<T,D> remoteSessionDiscoveryService;
	private AbstractRemoteExecutorManager<T,D> remoteExecutorManager;
	private MulticastNetworkConnection multicastNetworkConnection;
	private TaskTracker<T,D> taskTracker;

	public AbstractRemoteTaskTracker(int rmiPort, String rmiBindingName) {
		
		this.taskTracker = new TaskTracker<T,D>();// Class.forName(getSessionExecutorCoreClassName()).newInstance());

		this.rmiPort = rmiPort;
		this.rmiBindingName = rmiBindingName;

		try {
			this.multicastNetworkConnection = new MulticastNetworkConnection(
					getSessionServiceMulticastAddress(), getSessionServiceMulticastPort());
			this.remoteExecutorManager = createRemoteExecutorManager();			
			
			this.remoteSessionDiscoveryService = new RemoteSessionDiscoveryService<T,D>(this.multicastNetworkConnection,
					MarkReaderConstants.DISCOVERY_SERVICE_THREAD_PRIORITY,
					MarkReaderConstants.SESSION_SOURCE_ADVERTISE_DATAGRAM_PACKET_BUFFER_LENGTH,
					this.remoteExecutorManager){
				@Override
				public boolean isAcceptableMessage(InetAddress addr, String string, long key, long sessionID) {
					try {
						if (AbstractTaskTracker.DEBUG_CLUSTER_MODE){
							SoundManager snd = SoundManager.getInstance();
							snd.play(new URL(MarkReaderJarURIContext.getSoundBaseURI()+ "buble05.wav"));
							return true;
						}
						if (addr.isLoopbackAddress() || addr.equals(NetworkUtil.Inet4.getAddress())) {
							return false;
						}
						return true;
					} catch (SocketException ignore) {
						ignore.printStackTrace();
					} catch (MalformedURLException ignore){
					}
					return false;
				}
			};
			Logger.getLogger("prallel").info("MulticastGroup:Port = "+getSessionServiceMulticastAddress()+":"+getSessionServiceMulticastPort());
		} catch (IOException e) {
			if (e instanceof SocketException) {
				Logger.getLogger("parallel").warning("multicast disabled:"+((SocketException) e).getMessage());
				//e.printStackTrace();
			} else {
				Logger.getLogger("parallel").warning("multicast disabled.");
				e.printStackTrace();
			}
		}
	}

	public TaskTracker<T,D> getTaskTracker() {
		return this.taskTracker;
	}

	public String getSessionServiceMulticastAddress() {
		return AppConstants.MULTICAST_ADDRESS;
	}

	public int getSessionServiceMulticastPort() {
		return MarkReaderConstants.MULTICAST_PORT;
	}

	public int getRMIPort() {
		return this.rmiPort;
	}

	public String getRMIBindingName() {
		return this.rmiBindingName;
	}

	public MulticastNetworkConnection getMulticastNetworkConnection() {
		return this.multicastNetworkConnection;
	}

	public void shutdown() {
		if (this.remoteSessionDiscoveryService != null) {
			this.remoteSessionDiscoveryService.shutdown();
			if (this.remoteExecutorManager != null) {
				this.remoteExecutorManager.shutdown();
			}
		}
	}
	
	public abstract AbstractRemoteExecutorManager<T,D> createRemoteExecutorManager();
	
}
