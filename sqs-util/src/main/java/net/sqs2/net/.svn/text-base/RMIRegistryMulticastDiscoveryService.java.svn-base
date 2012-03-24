/*

 RMIMuticastDiscoveryService.java

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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public abstract class RMIRegistryMulticastDiscoveryService extends MulticastDiscoveryService {

	public RMIRegistryMulticastDiscoveryService(MulticastNetworkConnection con, int bufferLength,
			int threadPriority) throws UnknownHostException, IOException {
		super(con, threadPriority, bufferLength);  
	}

	public void startDiscovery(){
		super.startDiscovery(new DatagramPacketHandler(){ 
			@Override
			public void processDatagramPacket(DatagramPacket datagramPacket){
				InetAddress addr = datagramPacket.getAddress();
				byte[] data = datagramPacket.getData();
				byte[] src = new byte[datagramPacket.getLength()];
				for(int i=0; i < src.length; i++){
					src[i] = data[datagramPacket.getOffset()+i];
				}
				String message = new String(src);
				processMessage(addr, message);
			}
		});
	}
	
	public void processMessage(InetAddress addr, String message) {
		String[] messages = message.split("\t");
		if (messages.length != 3) {
			Logger.getLogger(getClass().getName()).warning("ignore multicast message:" + message);
			return;
		}
		String rmiURL = messages[0];
		long key = Long.parseLong(messages[1]);
		long sessionID = Long.parseLong(messages[2]);
		processMessage(addr, rmiURL, key, sessionID);
	}

	public abstract void processMessage(InetAddress addr, String rmiURL, long key, long sessionID);

}
