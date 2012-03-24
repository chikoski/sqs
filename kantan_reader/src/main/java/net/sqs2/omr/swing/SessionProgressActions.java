package net.sqs2.omr.swing;

import java.net.URL;

import net.sqs2.browser.Browser;
import net.sqs2.omr.httpd.SQSHttpdManager;

public class SessionProgressActions {
	
	public static void openResultBrowser()throws Exception{
		browseURL(SQSHttpdManager.getEXIgridHttpd().getBaseURI()+"/e");
	}
	
	private static void browseURL(String url) {
		try {
			Browser.showDocument(new URL(url));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
