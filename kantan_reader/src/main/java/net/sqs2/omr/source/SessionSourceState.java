package net.sqs2.omr.source;

public class SessionSourceState {
	private final static int NOT_INITIALIZED_STATE = 0;
	private final static int INITIALIZING_STATE = 1;
	private final static int INITIALIZED_STATE = 2;
	private final static int PREPARING_STATE = 3;
	private final static int PREPARED_STATE = 4;
	private final static int FINISHED_STATE = 5;
	
	private final static int STOPPED_STATE = 6;
	
	final private int state;

	public final static SessionSourceState NOT_INITIALIZED = new SessionSourceState(NOT_INITIALIZED_STATE); // no directory specified

	public final static SessionSourceState INITIALIZING = new SessionSourceState(INITIALIZING_STATE); // directory specified, sourceDirectory scanning
	public final static SessionSourceState INITIALIZED = new SessionSourceState(INITIALIZED_STATE); // directory specified, sourceDirectory scanned

	public final static SessionSourceState PREPARING = new SessionSourceState(PREPARING_STATE); // directory specified, sourceDirectory scanned, task generating.
	
	public final static SessionSourceState PREPARED = new SessionSourceState(PREPARED_STATE); // directory specified, sourceDirectory scanned, task generating completed.
	public final static SessionSourceState FINISHED = new SessionSourceState(FINISHED_STATE); // all task finished.
	
	public final static SessionSourceState STOPPED = new SessionSourceState(STOPPED_STATE); // all task stopped.
	
	private SessionSourceState(int state){
		this.state = state;
	}

	public boolean equals(Object o){
		return this.state == ((SessionSourceState)o).state;
	}


}
