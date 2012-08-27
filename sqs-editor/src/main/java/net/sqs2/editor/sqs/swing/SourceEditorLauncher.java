package net.sqs2.editor.sqs.swing;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import net.sqs2.editor.httpd.SQSHttpdManager;

public class SourceEditorLauncher {

	public static final String TITLE = "SQS SourceEditor2.1";

	public static void main(String[] args) throws Exception {
		
		System.setProperty("file.encoding", "UTF-8");

		if (args.length == 0) {
			new SQSSourceEditorMediator();
		} else if (args.length == 1) {
			URL url = null;
			try {
				url = new URL(args[0]);
				new SQSSourceEditorMediator(url);
			} catch (MalformedURLException ignore) {
				new SQSSourceEditorMediator(new File(args[0]));
			}
		}
		
		//SQSHttpdManager.getHttpd();
	}
}
