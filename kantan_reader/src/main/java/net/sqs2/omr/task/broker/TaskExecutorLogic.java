/**
 * 
 */
package net.sqs2.omr.task.broker;

import java.rmi.RemoteException;

import net.sqs2.omr.task.ExecutableTaskWrapper;

public interface TaskExecutorLogic{

	public void execute(ExecutableTaskWrapper executable)throws RemoteException;

}