package net.sqs2.omr.session.init;

import java.util.EventObject;

public class SessionSourceInitializationEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessionSourceInitializationEvent(Object source) {
		super(source);
	}

}
