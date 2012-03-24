/*

 MulticastAdvertisingService.java

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
import java.util.concurrent.TimeUnit;

public class MulticastAdvertisingService extends AbstractMulticastService {
	
	public MulticastAdvertisingService(MulticastNetworkConnection connection, int threadPriority) {
		super(connection, threadPriority);
	}
	
	public class DatagramSender implements Runnable {
		DatagramPacketFactory datagramPacketFactory;

		public DatagramSender(DatagramPacketFactory datagramPacketFactory){
			this.datagramPacketFactory = datagramPacketFactory;
		}
		
		public void run() {
			try {
				connection.getSocket().send(this.datagramPacketFactory.createDatagramPacket());
				logAdvertisement();
			} catch (IOException ignore) {
			}
		}
	}
	
	public void logAdvertisement(){}

	public void startAdvertise(DatagramPacketFactory datagramPacketFactory, int delay) {
		DatagramSender datagramSender = new DatagramSender(datagramPacketFactory);
		start("MulticastAdvertisingService");
		setFuture(this.executorService.scheduleWithFixedDelay(datagramSender, 0, 
				delay, TimeUnit.SECONDS));
	}
}
