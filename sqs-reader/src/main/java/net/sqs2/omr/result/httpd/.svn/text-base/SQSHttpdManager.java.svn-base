/*

 SQSHttpdManager.java

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

 Created on 2004/10/19

 */
package net.sqs2.omr.result.httpd;

import java.net.ProxySelector;
import java.util.concurrent.Executors;

import net.sqs2.net.NetworkUtil;

public class SQSHttpdManager {

	private static MarkReaderHttpd httpd = null;
	public static final int HTTP_PORT_FOR_LOCAL = 6970;
	public static final int HTTP_PORT_FOR_REMOTE = 6971;
	
	public static MarkReaderHttpd getMarkReaderHttpd() throws Exception {
		synchronized (SQSHttpdManager.class) {
			if (SQSHttpdManager.httpd == null) {
				SQSHttpdManager.httpd = new MarkReaderHttpd(HTTP_PORT_FOR_LOCAL, HTTP_PORT_FOR_REMOTE);
				try {
					SQSHttpdManager.httpd.start();
				} catch (java.net.BindException ignore) {
				}
				while (!SQSHttpdManager.httpd.isStarted()) {
					Thread.yield();
				}
				ProxySelector.setDefault(new SQSProxySelector(ProxySelector.getDefault()));
			}
		}
		return SQSHttpdManager.httpd;
	}
	
	public static String getBaseURI() {
		return "http://"+NetworkUtil.Inet4.LOOPBACK_ADDRESS+":" + HTTP_PORT_FOR_LOCAL;
	}

	public static void initHttpds() {
		Executors.newSingleThreadExecutor().submit(new java.lang.Runnable() {
			public void run() {
				try {
					SQSHttpdManager.getMarkReaderHttpd();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
}
