package net.sqs2.omr.session.init;

public class SessionSourceInitializationLifeCycleEvent extends SessionSourceInitializationEvent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	enum LIFECYCLE {STARTED,DONE};
	LIFECYCLE lifecycle;
	public SessionSourceInitializationLifeCycleEvent(Object source, LIFECYCLE lifecycle) {
		super(source);
		this.lifecycle = lifecycle;
	}
	public LIFECYCLE getLifecycle() {
		return lifecycle;
	}

}
