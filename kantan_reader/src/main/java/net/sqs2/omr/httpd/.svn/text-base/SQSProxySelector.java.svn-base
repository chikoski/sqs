package net.sqs2.omr.httpd;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;

public class SQSProxySelector extends ProxySelector {
	
	ProxySelector defsel = null;
	
	SQSProxySelector(ProxySelector def) {
		defsel = def;
	}
		
	public java.util.List<Proxy> select(URI uri) {
		if (uri == null) {
			throw new IllegalArgumentException("URI can't be null.");
		}

		String host = uri.getHost();
		
		if("127.0.0.1".equals(host) || "localhost".equals(host)){
			ArrayList<Proxy> l = new ArrayList<Proxy>();
			l.add(Proxy.NO_PROXY);
			return l;
		}

		if (defsel != null) {
			return defsel.select(uri);
		} else {
			ArrayList<Proxy> l = new ArrayList<Proxy>();
			l.add(Proxy.NO_PROXY);
			return l;
		}
	}

	@Override
	public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
		if (defsel != null){
			defsel.connectFailed(uri, sa, ioe);
		}
	}
}
