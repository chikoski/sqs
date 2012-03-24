package net.sqs2.omr.model;

import java.io.File;


public class AbstractSessionSourceImpl implements AbstractSessionSource {
	protected long sessionID;
	protected File rootDirectory;
	protected SessionSourceState sessionSourceState;

	public AbstractSessionSourceImpl(long sessionID, File rootDirectory){
		this.sessionID = sessionID;
		this.rootDirectory = rootDirectory;
		this.sessionSourceState = SessionSourceState.NOT_INITIALIZED;
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.IA#getSessionID()
	 */
	public long getSessionID() {
		return this.sessionID;
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.IA#getRootDirectory()
	 */
	public File getRootDirectory() {
		return this.rootDirectory;
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.IA#getSessionSourceState()
	 */
	public SessionSourceState getSessionSourceState() {
		return this.sessionSourceState;
	}
	
	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.IA#setSessionSourceState(net.sqs2.omr.session.source.SessionSourceState)
	 */
	@Override
	public void setSessionSourceState(SessionSourceState sessionSourceState){
		this.sessionSourceState = sessionSourceState;	
	}

	public boolean hasInitialized(){
		return this.sessionSourceState != SessionSourceState.NOT_INITIALIZED &&
			this.sessionSourceState != SessionSourceState.INITIALIZING;
	}

}
