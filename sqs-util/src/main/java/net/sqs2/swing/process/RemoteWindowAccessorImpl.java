/*

 RemoteWindowAccessorImpl.java

 Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2006/01/10

 */
package net.sqs2.swing.process;

import java.awt.Window;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

import net.sqs2.net.NetworkUtil;

class RemoteWindowAccessorImpl extends UnicastRemoteObject implements RemoteWindowAccessor {
	private static final long serialVersionUID = 0L;

	private Window window;

	public RemoteWindowAccessorImpl(Window window) throws RemoteException {
		this.window = window;
	}

	public Window getWindow() {
		return this.window;
	}

	public void toFront() throws RemoteException {
		try {
			InetAddress clientAddress = InetAddress.getByName(UnicastRemoteObject.getClientHost());
			if (clientAddress.isLoopbackAddress() || clientAddress.equals(NetworkUtil.Inet4.getAddress())) {
				this.window.toFront();
			}else{
				Logger.getLogger(this.getClass().getName()).warn("insecure remote message from:"+clientAddress.getHostAddress());
			}
		} catch (SocketException ignore) {
		} catch (ServerNotActiveException ignore) {
		} catch (UnknownHostException ignore) {
		}
		return;
	}
}
