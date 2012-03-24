/*

 RMIRegistryService.java

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

import java.net.SocketException;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sqs2.net.NetworkUtil.Inet4;

public class RMIRegistryUtil {
	
	private static Map<Integer,Registry> exportedRegistryPortMap = new HashMap<Integer,Registry>();

	public static int export(Remote remoteService, int rmiPort, String bindingName) throws RemoteException, SocketException {
		Registry registry = null;
		registry = LocateRegistry.getRegistry(Inet4.getHostAddress(), rmiPort);
		String serviceURL = createURL(rmiPort, bindingName);
		try {
			registry.rebind(bindingName, remoteService);
		} catch (java.rmi.ConnectException ignore) {
			registry = LocateRegistry.createRegistry(rmiPort);
			registry.rebind(bindingName, remoteService);
			exportedRegistryPortMap.put(rmiPort, registry);
		}
		Logger.getLogger("net").log(Level.INFO, " Export: " + serviceURL);
		return rmiPort;
	}

	public static boolean unexport(int rmiPort, String bindingName) throws RemoteException, SocketException {
		Registry registry = null;
		registry = LocateRegistry.getRegistry(Inet4.getHostAddress(), rmiPort);
		try {
			if(exportedRegistryPortMap.containsKey(rmiPort)){
				String serviceURL = createURL(rmiPort, bindingName);
				Logger.getLogger("net").log(Level.INFO, " UnExport: " + serviceURL);
				registry.unbind(serviceURL);
				exportedRegistryPortMap.remove(rmiPort);
				Logger.getLogger("...done");
			}
		} catch (NotBoundException ignore) {
			return false;
		}
		return true;
	}

	public static String createURL(int port, String bindingName) throws SocketException {
		String url = "rmi://" + NetworkUtil.Inet4.getHostAddress() + ":" + port + bindingName;
		return url;
	}
}
