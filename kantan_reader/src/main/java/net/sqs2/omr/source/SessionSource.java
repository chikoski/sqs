package net.sqs2.omr.source;

import java.io.File;
import java.io.IOException;

import net.sqs2.omr.source.config.ConfigHandlerFactoryImpl;
import net.sqs2.omr.task.TaskException;

public class SessionSource {
	
	private long sessionID;
	File rootDirectory;
	
	private SessionSourceState sessionSourceState;
	private SessionSourceContentAccessor sessionSourceContentAccessor;
	private SessionSourceContentIndexer sessionSourceContentIndexer;
	private ConfigHandlers configHandlers;

	public SessionSource(long sessionID, File rootDirectory){
		this.sessionID = sessionID;
		this.rootDirectory = rootDirectory;
		this.sessionSourceState = SessionSourceState.NOT_INITIALIZED;
	}
	
	public boolean hasStarted(){
		return this.sessionSourceState != SessionSourceState.NOT_INITIALIZED;	
	}
	
	public boolean isInitialized(){
		return this.sessionSourceState == SessionSourceState.INITIALIZED;
	}
	
	public void initialize()throws IOException,TaskException{ 
		this.sessionSourceState = SessionSourceState.INITIALIZING;
		this.sessionSourceContentAccessor = new SessionSourceContentAccessor(this.rootDirectory);
		this.sessionSourceContentIndexer = new SessionSourceContentIndexer();
		this.configHandlers = new ConfigHandlers(new ConfigHandlerFactoryImpl());
		this.sessionSourceState = SessionSourceState.INITIALIZED;
	}
		
	public void setPreparing(){
		if(this.sessionSourceState != SessionSourceState.INITIALIZED){
			throw new RuntimeException("SessionSourceState NOT INITIALIZED");
		}
		this.sessionSourceState = SessionSourceState.PREPARING;
	}

	public boolean isPreparing(){
		return this.sessionSourceState == SessionSourceState.PREPARING;
	}
	
	public void setPrepared(){
		if(this.sessionSourceState != SessionSourceState.PREPARING){
			throw new RuntimeException("SessionSourceState NOT RUNNGING");
		}
		this.sessionSourceState = SessionSourceState.PREPARED;
	}
	
	public boolean isPrepared(){
		return this.sessionSourceState == SessionSourceState.PREPARED;
	}
	
	public void setFinished(){
		if(this.sessionSourceState != SessionSourceState.PREPARED){
			throw new RuntimeException("SessionSourceState NOT PREPARED");
		}
		this.sessionSourceState = SessionSourceState.FINISHED;
	}
	
	public boolean hasFinished(){
		return this.sessionSourceState == SessionSourceState.FINISHED;	
	}
	
	public boolean hasStopped(){
		return this.sessionSourceState == SessionSourceState.STOPPED;	
	}
	
	public long getSessionID(){
		return this.sessionID;
	}
	
	public File getRootDirectory(){
		return this.rootDirectory;
	}
	
	public SourceDirectoryConfiguration getConfigHandler(String path, long lastModified)throws IOException{
		return this.configHandlers.getConfigHandler(this.rootDirectory, path, lastModified);
	}
	
	public SourceDirectoryConfiguration getDefaultConfigHandler(){
		return this.configHandlers.getDefaultConfigHandler();
	}
	
	public SessionSourceState getSessionSourceState(){
		return this.sessionSourceState;
	}
	
	public SessionSourceContentIndexer getSessionSourceContentIndexer(){
		return this.sessionSourceContentIndexer;
	}
	
	public SessionSourceContentAccessor getSessionSourceContentAccessor(){
		return this.sessionSourceContentAccessor;
	}	
	
}
