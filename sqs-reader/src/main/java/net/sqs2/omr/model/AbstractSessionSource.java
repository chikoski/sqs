package net.sqs2.omr.model;

import java.io.File;

public interface AbstractSessionSource {

	public abstract long getSessionID();

	public abstract File getRootDirectory();

	public abstract SessionSourceState getSessionSourceState();

	public abstract void setSessionSourceState(SessionSourceState sessionSourceState);

}