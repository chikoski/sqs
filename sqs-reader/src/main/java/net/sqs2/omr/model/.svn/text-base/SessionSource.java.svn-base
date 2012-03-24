package net.sqs2.omr.model;

import java.io.IOException;


public interface SessionSource extends AbstractSessionSource{
	
	public abstract boolean hasInitialized();
	
	public abstract void setSessionSourceState(SessionSourceState sessionSourceState);	
	
	public abstract void flush() throws IOException;	
	public abstract void close() throws IOException;

	public abstract SourceDirectoryConfiguration getConfiguration(String path, long lastModified) throws IOException,ConfigSchemeException;

	public abstract ContentIndexer getContentIndexer();
	public abstract ContentAccessor getContentAccessor();
	
	public abstract void incrementCurrentSourceDirectorySerialID();
	public abstract int getCurrentSourceDirectorySerialID();
	public abstract void resetSourceDirectorySerialID();
}