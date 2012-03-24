package net.sqs2.net;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class ClassURLStreamHandler extends URLStreamHandler {

	@Override
	protected URLConnection openConnection(URL url) throws IOException {
		return new ClassURLConnection(url);
	}

}
