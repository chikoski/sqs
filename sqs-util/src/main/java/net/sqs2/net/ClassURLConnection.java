package net.sqs2.net;

import java.io.BufferedInputStream;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import javax.jnlp.DownloadService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

public class ClassURLConnection extends URLConnection {

	static DownloadService ds;
	static{
		try { 
			ds = (DownloadService)ServiceManager.lookup("javax.jnlp.DownloadService"); 
		} catch (UnavailableServiceException e) { 
			ds = null; 
		}
	}

	public ClassURLConnection(URL url) {
		super(url);
	}

	@Override
	public String getContentType() {
		return guessContentTypeFromName(this.url.getPath());
	}

	@Override
	public synchronized InputStream getInputStream() throws IOException {
		if ("class".equals(this.url.getProtocol())) {
			if (!this.connected) {
				connect();
			}
			this.setDefaultUseCaches(true);
			String path = this.url.getPath();
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
			
			ClassLoader classLoader = this.getClass().getClassLoader();
			
			if (classLoader == null) {
				String message = "Cannot load : " + this.url.getProtocol() + " / " + path;
				Logger.getLogger(getClass().getName()).severe(message);
				throw new IOException(message);
			}

			return classLoader.getResourceAsStream(path);
			
		} else if ("http".equals(this.url.getProtocol())) {
			
			if (ds != null) { 
		        try { 
		            // determine if a particular resource is cached
		            boolean cached = ds.isResourceCached(url, null);
		            if(!cached){
		            	// Logger.getLogger(getClass().getName()).info("download jar:"+this.url.toString());
		            	ds.loadResource(url, null, null);
		            }else{
		            	// Logger.getLogger(getClass().getName()).info("cached jar:"+this.url.toString());
		            }
		        } catch (Exception e) { 
		        	e.printStackTrace(); 
		        }
		        
		        return null;
		        
			}else{
				Logger.getLogger(getClass().getName()).info("DownloadService is disabled:"+this.url.toString());
				return super.getInputStream();
				//return new sun.net.www.protocol.http.HttpURLConnection(url, null).getInputStream();
			}
		} else if ("file".equals(this.url.getProtocol())) {
			return new BufferedInputStream(new FileInputStream(this.url.getPath()));
			
		}
		System.err.println("'protocol:" + this.url + "----" + this.url.getProtocol() + "----"
				+ this.url.getPath());
		this.connected = false;
		throw new IOException();
	}

	@Override
	public synchronized void connect() throws IOException {
		this.connected = true;
	}
}
