/*

 SQSProxySelector.java

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

 Created on 2009/01/11

 */
package net.sqs2.omr.result.httpd;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;

public class SQSProxySelector extends ProxySelector {

	private static final String IPV4_LOOPBACK_ADDRESS = "127.0.0.1";
	private static final String LOCALHOST_HOSTNAME = "localhost";

	ProxySelector defsel = null;

	SQSProxySelector(ProxySelector def) {
		this.defsel = def;
	}

	@Override
	public java.util.List<Proxy> select(URI uri) {
		if (uri == null) {
			throw new IllegalArgumentException("URI can't be null.");
		}

		String host = uri.getHost();

		if (IPV4_LOOPBACK_ADDRESS.equals(host) || LOCALHOST_HOSTNAME.equals(host)) {
			ArrayList<Proxy> l = new ArrayList<Proxy>();
			l.add(Proxy.NO_PROXY);
			return l;
		}

		if (this.defsel != null) {
			return this.defsel.select(uri);
		} else {
			ArrayList<Proxy> l = new ArrayList<Proxy>();
			l.add(Proxy.NO_PROXY);
			return l;
		}
	}

	@Override
	public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
		if (this.defsel != null) {
			this.defsel.connectFailed(uri, sa, ioe);
		}
	}
}
