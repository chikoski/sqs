package net.sqs2.omr.task.consumer;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import net.sqs2.omr.task.PageTask;

public interface TaskConsumer extends Runnable{
	public void setup(File sourceDirectoryRoot)throws IOException;
	public void consumeTask(PageTask task)throws RemoteException;
}
