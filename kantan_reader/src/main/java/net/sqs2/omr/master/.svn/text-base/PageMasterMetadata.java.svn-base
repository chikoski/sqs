package net.sqs2.omr.master;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageMasterMetadata implements Serializable{
	private final static long serialVersionUID = 1L;
	
	private File sourceDirectoryRoot;
	private String masterPath;
	private long lastModified;
	private String type;
	private int priority;
	private List<String> metadataList = null;
	
	public PageMasterMetadata(File sourceDirectoryRoot, String masterPath, String type, int priority, long lastModified){
		this.sourceDirectoryRoot = sourceDirectoryRoot;
		this.masterPath = masterPath; 
		this.type = type;
		this.priority = priority;
		this.lastModified = lastModified;
		
		this.metadataList = new ArrayList<String>();	
	}
	
	public File getSourceDirectoryRoot() {
		return sourceDirectoryRoot;
	}

	public String getMasterPath() {
		return masterPath;
	}
	
	public long getLastModified(){
		return lastModified;
	}

	public int getPriority(){
		return this.priority;
	}
	
	public String getType(){
		return this.type;
	}
	
	public void addMetadata(String metadata){
		metadataList.add(metadata);
	}
	
	public List<String> getMetadataList(){
		return this.metadataList;
	}
}
