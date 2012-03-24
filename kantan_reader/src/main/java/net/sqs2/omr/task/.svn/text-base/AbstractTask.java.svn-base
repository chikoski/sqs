package net.sqs2.omr.task;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import net.sqs2.util.FileResourceID;

public class AbstractTask implements Serializable{
	
	private static final long serialVersionUID = 0L;

	public static final int LEASE_TIMEOUT_IN_SEC = 13;
	protected String sourceDirectoryRootPath;
	protected FileResourceID masterResourceID;
	protected FileResourceID configResourceID;
	protected long expiredTime = 0L;
	protected long sessionID = 0L;
	protected TaskResult taskResult = null;
	protected TaskError taskError = null;

	AbstractTask(){}
	
	AbstractTask(String sourceDirectoryRootPath,
			FileResourceID masterResourceID, FileResourceID configResourceID, long sessionID){
		this.sourceDirectoryRootPath = sourceDirectoryRootPath;
		this.masterResourceID = masterResourceID;
		this.configResourceID = configResourceID;
		this.sessionID = sessionID;
	}
	
	public String getSourceDirectoryRootPath() {
		return this.sourceDirectoryRootPath;
	}

	public FileResourceID getMasterFileResourceID() {
		return this.masterResourceID;
	}

	public FileResourceID getConfigHandlerFileResourceID() {
		return this.configResourceID;
	}

	public TaskResult getTaskResult() {
		return this.taskResult;
	}

	public void setTaskResult(TaskResult taskResult) {
		this.taskResult = taskResult;
	}

	public long getSessionID() {
		return this.sessionID;
	}

	public void setLeased() {
		this.expiredTime = System.currentTimeMillis() + LEASE_TIMEOUT_IN_SEC * 1000;
	}

	public long getDelay(TimeUnit unit) {
		return unit.convert(this.expiredTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	public TaskError getTaskError() {
		return this.taskError;
	}

	public void setTaskError(TaskError taskError) {
		this.taskError = taskError;
	}


}