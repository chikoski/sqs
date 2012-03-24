/*

 RemoteWindowDecorator.java

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

 Created on 2006/01/09

 */
package net.sqs2.swing.process;

import java.awt.Toolkit;

import java.awt.Window;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import net.sqs2.net.NetworkUtil;

public class RemoteWindowPopupDecorator {
	public static final String SINGLETON_SERVICE_NAME = "SingletonService";

	public static final String RMI_APP_SINGLETON_SERVICE_NAME = SINGLETON_SERVICE_NAME;

	private static RemoteWindowAccessorImpl singleton = null;

	public static Window inactivate(int rmiPort) {
		return inactivate(rmiPort, SINGLETON_SERVICE_NAME);
	}

	/**
	 * Usage: RemoteWindowDecorator.activate(port, window);
	 * 
	 * @param window
	 * @param rmiPort
	 */
	public static boolean activate(Window window, int rmiPort) {
		try {
			if (singleton == null) {
				singleton = new RemoteWindowAccessorImpl(window);
				return activate(singleton, rmiPort, SINGLETON_SERVICE_NAME);
			}
		} catch (RemoteException ex) {
			throw new RuntimeException(ex);
		}
		return false;
	}

	public static boolean activate(final Remote defaultService, final int rmiPort, final String bindingName) {
		try{
			
			return Executors.newSingleThreadExecutor().submit(new Callable<Boolean>(){
				public Boolean call(){
					Registry registry = null;
					Remote remote = null;
					try {
						try {
							registry = LocateRegistry.getRegistry(NetworkUtil.Inet4.LOOPBACK_ADDRESS, rmiPort);
							Logger.getLogger("RemoteWindowPopupDecorator").info("DNS lookup: starting...");
							remote = registry.lookup(bindingName);
							Logger.getLogger("RemoteWindowPopupDecorator").info("DNS lookup: done.");
							activate(remote);
							// toFront, exit
							return false;
						} catch (NotBoundException ignore) {
							registry.bind(bindingName, defaultService);
							// reuse RMI service
							// do nothing, go ahead
							return true;
						}
					} catch (Exception ex) {
						// new RMI service
						// do nothing, go ahead
						try {
							registry = LocateRegistry.createRegistry(rmiPort);
						} catch (RemoteException ex2) {
							// ex2.printStackTrace();
						}
						try {
							registry.rebind(bindingName, defaultService);
						} catch (RemoteException ex2) {
							ex2.printStackTrace();
						}
						return true;
					}
				}
			}).get().booleanValue();

		}catch(ExecutionException ex){
		}catch(InterruptedException ex){
		}
		return false;
	}
	
	private static void activate(Remote remote) {
		RemoteWindowAccessor remoteService = (RemoteWindowAccessor) remote;
		Toolkit.getDefaultToolkit().beep();
		try {
			remoteService.toFront();
		} catch (RemoteException ignore) {
			ignore.printStackTrace();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		Logger.getLogger("remote").info("RemoteWindow.toFront()");
		Logger.getLogger("remote").info("Exit.");
		// throw new RuntimeException("EXIT");
		System.exit(0);
	}

	public static Window inactivate(int rmiPort, String bindingName) {
		Window window = null;
		try {
			if (singleton != null) {
				window = singleton.getWindow();
				UnicastRemoteObject.unexportObject(singleton, true);
			}
			Registry registry = LocateRegistry.getRegistry(rmiPort);
			registry.unbind(bindingName);
		} catch (NotBoundException ignore) {
		} catch (RemoteException ignore) {
		}
		return window;
	}
}
