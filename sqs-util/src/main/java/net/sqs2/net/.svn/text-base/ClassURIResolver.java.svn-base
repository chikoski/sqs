package net.sqs2.net;

import java.io.IOException;


import java.io.InputStream;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.apache.xmlgraphics.util.uri.CommonURIResolver;

public class ClassURIResolver extends CommonURIResolver implements URIResolver {

	public ClassURIResolver(){
	}
	
	public Source resolve(String href, String base) {
		try {
			String urlString = null;
			if (0 < href.indexOf(":") || base == null) {
				urlString = href;
			} else if (0 < base.indexOf(":")) {
				if(base.endsWith("/")){
					urlString = base + href;
				}else{
					urlString = base.substring(0, base.lastIndexOf('/')+1) + href;
				}
			} else {
				throw new RuntimeException("invalid url: " + base + " " + href);
			}
			URL url = new URL(urlString);
			ClassURLConnection connection = null;
			
			if("class".equals(url.getProtocol())){
				connection = new ClassURLConnection(url);
				InputStream inputStream = connection.getInputStream();
				if (inputStream != null) {
					return new StreamSource(inputStream, urlString);
				}
				throw new RuntimeException("class url connection failed:" + url);
			}else{
				return super.resolve(href, base);
			}
		} catch (IOException ex) {
			throw new RuntimeException("class url connection failed:" + base + "," + href);
		}
	}
}
