/*

 RMIMulticastAdvertisingService.java

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
package net.sqs2.net;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.logging.Logger;

public class RMIRegistryMulticastAdvertisingService extends MulticastAdvertisingService {
	long key;
	long sessionID;
	int rmiPort;
	int interval;

	public RMIRegistryMulticastAdvertisingService(MulticastNetworkConnection connection, long key, long sessionID,
			int threadPriority, int rmiPort, int interval) throws UnknownHostException, IOException {
		super(connection, threadPriority);
		this.key = key;
		this.sessionID = sessionID;
		this.rmiPort = rmiPort;
		this.interval = interval;
	}
	
	private String createMessage(String name)throws SocketException {
		String url = RMIRegistryUtil.createURL(this.rmiPort, name);
		return url + "\t" + this.key + "\t" + this.sessionID;
	}
	
	public void startAdvertising(Remote remote, final String bindingName) {
		try {
			if (RMIRegistryUtil.export(remote, this.rmiPort, bindingName) == -1) {
				Logger.getLogger("net").warning("RMI registry already bounded.");
			} else {
				final String message = createMessage(bindingName);
				startAdvertise(
						new DatagramPacketFactory(){
							public DatagramPacket createDatagramPacket(){
								return new DatagramPacket(message.getBytes(), message.length(),
										connection.getGroup(), connection.getPort());
							}
						}
						, this.interval);
				Logger.getLogger("net").info("RMI registry advertised.");
			}
		} catch (RemoteException ignore) {
			Logger.getLogger("net").warning(ignore.getMessage());
		} catch (SocketException ignore) {
			Logger.getLogger("net").warning(ignore.getMessage());
		}
	}

	public void stopAdvertising(String name) {
		try {
			if (RMIRegistryUtil.unexport(this.rmiPort, name)) {
				//Logger.getLogger("net").log(Level.WARNING, "RMI registry disabled.");
			}
			
			super.stop();
			
		} catch (RemoteException ex) {
			ex.printStackTrace();
			Logger.getLogger(getClass().getName()).warning(ex.getMessage());
		} catch (SocketException ex) {
			//ex.printStackTrace();
			Logger.getLogger(getClass().getName()).warning(ex.getMessage());
		}
	}

}
