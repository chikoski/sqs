package net.sqs2.omr.session.init;

public class SessionSourceInitializationErrorEvent extends
		SessionSourceInitializationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Exception exception;
	
	public SessionSourceInitializationErrorEvent(Object source, Exception error) {
		super(source);
		this.exception = error;
	}

	public Exception getException() {
		return exception;
	}
}
