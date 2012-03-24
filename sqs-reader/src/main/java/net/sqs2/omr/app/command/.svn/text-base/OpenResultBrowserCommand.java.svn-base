package net.sqs2.omr.app.command;

import java.awt.Desktop;
import java.net.URI;

import net.sqs2.omr.result.httpd.SQSHttpdManager;
import net.sqs2.omr.util.URLSafeRLEBase64;

public class OpenResultBrowserCommand implements java.util.concurrent.Callable<Object>{
	
	private long sid;
	public OpenResultBrowserCommand(long sid){
		this.sid = sid;
	}
	
	public Object call(){
		browseURL(SQSHttpdManager.getBaseURI() + "/c/"+ URLSafeRLEBase64.encode(sid));
		return null;
	}

	private static void browseURL(String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
