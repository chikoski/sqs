/**
 * 
 */
package net.sqs2.omr.task;

import java.rmi.RemoteException;

import net.sqs2.omr.source.SourceDirectoryConfiguration;
import net.sqs2.omr.task.broker.TaskExecutorEnv;

public class ExecutableTaskWrapper{

	private PageTask task;
	private TaskExecutorEnv executorEnv;

	public ExecutableTaskWrapper(PageTask task, TaskExecutorEnv pageTaskResource){
		this.task = task;
		this.executorEnv = pageTaskResource;
	}

	public PageTask getTask(){
		return task;
	}

	public SourceDirectoryConfiguration getConfiguration()throws RemoteException{
		return this.executorEnv.getConfiguration(task.getSessionID(), task.getConfigHandlerFileResourceID());
	}

	public TaskExecutorEnv getTaskExecutorEnv(){
		return this.executorEnv;
	}
}