/*

 MulticastNetworkConnection.java

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
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class MulticastNetworkConnection {

	private static Map<Integer, MulticastNetworkConnection> singletonMap = new HashMap<Integer, MulticastNetworkConnection>();
	protected String mcastAddr;
	protected int mcastPort;
	protected InetAddress group;
	protected MulticastSocket socket;

	public MulticastNetworkConnection(String mcastAddr, int mcastPort) throws UnknownHostException,
			IOException {
		this.mcastAddr = mcastAddr;
		this.mcastPort = mcastPort;
		this.group = InetAddress.getByName(mcastAddr);
		this.socket = new MulticastSocket(mcastPort);
		this.socket.setSoTimeout(1000);
		this.socket.setTimeToLive(1);
		this.socket.joinGroup(this.group);
	}

	public static MulticastNetworkConnection getInstance(String mcastAddr, int port) throws UnknownHostException, IOException {
		MulticastNetworkConnection connection = null;
		synchronized (MulticastNetworkConnection.singletonMap) {
			if ((connection = MulticastNetworkConnection.singletonMap.get(port)) == null) {
				connection = new MulticastNetworkConnection(mcastAddr, port);
				MulticastNetworkConnection.singletonMap.put(port, connection);
			}
			return connection;
		}
	}

	public MulticastSocket getSocket() {
		return this.socket;
	}

	public InetAddress getGroup() {
		return this.group;
	}

	public int getPort() {
		return this.mcastPort;
	}

	public void close() throws IOException {
		this.socket.leaveGroup(this.group);
		this.socket.close();
	}
}
