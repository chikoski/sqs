package net.sqs2.omr.session.service;

import java.util.EventObject;

import net.sqs2.omr.model.OMRPageTask;

public class PageTaskCommittedEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OMRPageTask pageTask;
	
	public PageTaskCommittedEvent(Object source, OMRPageTask pageTask) {
		super(source);
		this.pageTask = pageTask;
	}

	public OMRPageTask getPageTask() {
		return pageTask;
	}

}
