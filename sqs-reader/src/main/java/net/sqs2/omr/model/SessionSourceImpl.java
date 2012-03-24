package net.sqs2.omr.model;

import java.io.File;
import java.io.IOException;


public class SessionSourceImpl extends AbstractSessionSourceImpl implements SessionSource{

	private ContentAccessor contentAccessor;
	private ContentIndexer contentIndexer;
	private SourceDirectoryConfigurations configHandlers;
	private int currentSourceDirectorySerialID = -1;

	
	SessionSourceImpl(long sessionID, File rootDirectory) throws IOException{
		super(sessionID, rootDirectory);
		this.configHandlers = new SourceDirectoryConfigurations(new SourceDirectoryConfigurationFactoryImpl());
		this.contentIndexer = new ContentIndexerImpl();
		resetSourceDirectorySerialID();
		this.contentAccessor = new ContentAccessorImpl(this.rootDirectory);
	}
	
	public void incrementCurrentSourceDirectorySerialID(){
		currentSourceDirectorySerialID++;
	}
	
	public void resetSourceDirectorySerialID(){
		currentSourceDirectorySerialID = 0;
	}
	
	public int getCurrentSourceDirectorySerialID(){
		return currentSourceDirectorySerialID;
	}
	
	public void close() throws IOException{
		this.contentAccessor.flush();
		this.contentAccessor.close();
		this.contentIndexer.clear();
		resetSourceDirectorySerialID();
	}

	public void flush() throws IOException{
		this.contentAccessor.flush();
	}

	public SourceDirectoryConfiguration getConfiguration(String path, long lastModified) throws IOException,ConfigSchemeException {
		return this.configHandlers.getConfiguration(getRootDirectory(), path, lastModified);
	}

	public ContentIndexer getContentIndexer() {
		return this.contentIndexer;
	}

	public ContentAccessor getContentAccessor() {
		return this.contentAccessor;
	}

}
