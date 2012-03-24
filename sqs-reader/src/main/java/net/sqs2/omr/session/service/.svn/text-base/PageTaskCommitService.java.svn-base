package net.sqs2.omr.session.service;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;

import net.sqs2.omr.model.OMRPageTask;

public interface PageTaskCommitService extends Callable<Void>{
	public void setup(File sourceDirectoryRoot) throws IOException;

	public void commit(OMRPageTask task) throws RemoteException;
}
