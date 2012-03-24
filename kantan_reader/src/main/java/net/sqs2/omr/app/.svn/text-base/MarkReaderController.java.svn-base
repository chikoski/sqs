package net.sqs2.omr.app;

import java.io.File;
import java.io.IOException;

import net.sqs2.omr.session.Session;
import net.sqs2.omr.session.SessionException;
import net.sqs2.omr.task.TaskException;

public interface MarkReaderController {
	
	public Session createOrReuseSession(File sourceDirectoryRoot)throws SessionException,IOException,TaskException;
	public int countSessionsBySourceDirectory(File sourceDirectoryRootFile);
	
	public void userOpen(Session session) throws TaskException,IOException;
	public void userClear(File sourceDirectoryRoot) throws IOException,TaskException;
	public void userStart(File sourceDirectoryRootFile) throws IOException,TaskException;
	public void userStop(File sourceDirectoryRootFile);
	public void userClose(File sourceDirectoryRoot);
	public void userShutdown();
	public void userExit();
}
