/*

 MulticastDiscoveryService.java

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
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

public class MulticastDiscoveryService extends AbstractMulticastService {
	protected int multicastDatagramDataLength;

	public MulticastDiscoveryService(MulticastNetworkConnection connection, 
			int threadPriority, int multicastDatagramDataLegth) {
		super(connection, threadPriority);
		this.multicastDatagramDataLength = multicastDatagramDataLegth;
	}
	
	
	public void startDiscovery(final DatagramPacketHandler datagramPacketHandler) {
		start("MulticastDiscovyeryService");
		final byte[] multicastData = new byte[MulticastDiscoveryService.this.multicastDatagramDataLength];
		final MulticastSocket socket = MulticastDiscoveryService.this.connection.getSocket();
		setFuture(this.executorService.scheduleWithFixedDelay(new Runnable() {
				public void run() {
					try {
						DatagramPacket datagramPacket = new DatagramPacket(multicastData, multicastData.length);
						socket.receive(datagramPacket);
						datagramPacketHandler.processDatagramPacket(datagramPacket);				
					} catch (SocketTimeoutException ignore) {
					} catch (IOException e) {
						//e.printStackTrace();
						shutdown();
					}
				}
		}, 1L, 1L, TimeUnit.SECONDS));
	}
}
