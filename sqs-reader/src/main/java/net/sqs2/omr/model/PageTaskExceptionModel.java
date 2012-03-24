package net.sqs2.omr.model;

import java.io.Serializable;

public class PageTaskExceptionModel implements Serializable, Comparable<PageTaskExceptionModel>{

	private static final long serialVersionUID = 1L;
	protected PageID pageID;
	protected String message;
	
	public PageTaskExceptionModel(PageID pageID) {
		this.pageID = pageID;
	}
	
	public PageTaskExceptionModel(PageID pageID, String message) {
		this(pageID);
		this.message = message;
	}
	
	public PageID getPageID(){
		return pageID;
	}
	
	@Override
	public int hashCode() {
		return this.pageID.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		try {
			return this.pageID.equals(((PageTaskExceptionModel) o).pageID);
		} catch (Exception ex) {
			return false;
		}
	}

	public int compareTo(PageTaskExceptionModel c) {
		try {
			return this.pageID.compareTo((c).pageID);
		} catch (Exception ex) {
			return -1;
		}
	}
	
	public String getLocalizedMessage(){
		return message;
	}


}