package net.sqs2.omr.model;

import java.io.Serializable;

import net.sqs2.util.FileResourceID;

public class AbstractPageTask extends Ticket implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String sourceDirectoryRootPath;
	protected FileResourceID defaultPageMasterFileResourceID;
	protected FileResourceID configResourceID;
	protected PageTaskResult taskResult = null;
	protected Ticket ticket;
	
	protected PageTaskException pageTaskException = null;

	public AbstractPageTask() {
		this.ticket = new Ticket();
	}

	public AbstractPageTask(long sessionID) {
		this.sessionID = sessionID;
	}

	public AbstractPageTask(String sourceDirectoryRootPath,
			FileResourceID configResourceID,
			FileResourceID defaultPageMasterFileResourceID, long sessionID) {
		this(sessionID);
		this.sourceDirectoryRootPath = sourceDirectoryRootPath;
		this.configResourceID = configResourceID;
		this.defaultPageMasterFileResourceID = defaultPageMasterFileResourceID;
	}

	public long getSessionID() {
		return this.sessionID;
	}

	public String getSourceDirectoryRootPath() {
		return this.sourceDirectoryRootPath;
	}

	public FileResourceID getConfigHandlerFileResourceID() {
		return this.configResourceID;
	}

	public PageTaskResult getPageTaskResult() {
		return this.taskResult;
	}

	public PageTaskException getPageTaskException() {
		return this.pageTaskException;
	}

	public void setPageTaskException(PageTaskException taskException) {
		this.pageTaskException = taskException;
	}

	public void setTaskResult(PageTaskResult taskResult) {
		this.taskResult = taskResult;
	}

	public FileResourceID getDefaultPageMasterFileResourceID(){
		return this.defaultPageMasterFileResourceID;
	}

	public void setDefaultPageMasterFileResourceID(FileResourceID defaultPageMasterFileResourceID){
		this.defaultPageMasterFileResourceID = defaultPageMasterFileResourceID;
	}

}
