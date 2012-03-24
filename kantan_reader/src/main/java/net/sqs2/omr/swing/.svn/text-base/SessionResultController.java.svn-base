package net.sqs2.omr.swing;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.sqs2.browser.Browser;
import net.sqs2.omr.app.App;
import net.sqs2.util.FileUtil;

import org.mortbay.util.URIUtil;

public class SessionResultController {
	
	File sourceDirectory;
	SessionResultPanel sessionResultPanel;
	SessionResultController(File sourceDirectory, SessionResultPanel sessionResultPanel){
		this.sourceDirectory = sourceDirectory;
		this.sessionResultPanel = sessionResultPanel;
	}
	
	private void showBySuffix(String suffix){
		try{
			List<File> files = FileUtil.find(sourceDirectory.getAbsolutePath() + File.separatorChar + App.getResultDirectoryName(), suffix);
			if(0 < files.size()){
				File file = files.get(0);
				
				URL url = convertFileToURL(file);
				Browser.showDocument(url);
				
				/*
				//new Java6DesktopBrowserLauncher().showDocument(file);
				String encoding = null;
				if(File.separatorChar == '/'){
					encoding = "UTF-8";
				}else if(File.separatorChar == '\\'){
					encoding = "MS932";
				}
				String urlString = "file://"+HTMLUtil.encodePath(file.getAbsolutePath());
				System.err.println("urlString "+urlString);
				Browser.showDocument(urlString);
				*/
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private URL convertFileToURL(File file) throws MalformedURLException {
		URL url = null;
		if(File.separatorChar == '\\'){
			url = new URL("file:///"+URIUtil.encodeString(null, file.getAbsolutePath().replace("\\", "/"), "MS932"));
		}else{
			url = file.toURI().toURL();
		}
		return url;
	}
	
	private void showIndex(String type){
		File file = new File(sourceDirectory.getAbsolutePath() + File.separatorChar + App.getResultDirectoryName()+File.separatorChar+type+File.separatorChar+"index.html");
		try{
			if(file.exists()){
				URL url = convertFileToURL(file);
				Browser.showDocument(url);
			}
		}catch(MalformedURLException ex){
			ex.printStackTrace();
		}	
	}
	
	public void showExcel(){
		showBySuffix(".xls");
	}
	
	public void showCSV(){
		showBySuffix("tsv.txt");
	}
	
	public void showTextarea(){
		showIndex("TEXTAREA");
	}
	
	public void showChart(){
		showIndex("CHART");
	}

}
