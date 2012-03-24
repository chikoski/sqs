package net.sqs2.omr.session.event;

public interface OutputEventReciever {

	public abstract void startSession(SessionEvent sessionEvent);

	public abstract void endSession(SessionEvent sessionEvent);

	public abstract void startMaster(MasterEvent masterEvent);

	public abstract void endMaster(MasterEvent masterEvent);

	public abstract void startSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent);

	public abstract void endSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent);
}
