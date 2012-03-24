/**
 * 
 */
package net.sqs2.omr.session.service;

import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.sqs2.omr.model.Ticket;

public abstract class TaskExecutor<T extends Ticket, D extends ServerDispatcher> implements Runnable {
	/**
	 * 
	 */
	private final AbstractTaskTracker<T,D> taskTracker;
	D sessionServiceDispatcher;

	public TaskExecutor(AbstractTaskTracker<T,D> taskTracker,
			D sessionServiceDispatcher) {
		this.taskTracker = taskTracker;
		this.sessionServiceDispatcher = sessionServiceDispatcher;
	}

	public D getSessionSourceServerDispatcher() {
		return this.sessionServiceDispatcher;
	}

	public void run() {
		try {
			if (this.sessionServiceDispatcher.isRemote()) {
				// remote session
				if (AbstractTaskTracker.DEBUG_CLUSTER_MODE) {
					// Logger.getLogger(getClass().getName()).info("execute remote task in debug mode");
				} else if (this.sessionServiceDispatcher.getLocalServer() != null
						&& this.sessionServiceDispatcher.getLocalServer().existsRunningLocalSessions()) {
					sleep(1000);
					return;
				}

			} else {
				// local session
				if (!this.sessionServiceDispatcher.hasInitialized()) {
					sleep(1000);
					return;
					/*
					 * }else if(0 <this.executorEnv.getSessionService().
					 * getNumRemoteSlaveExecutors()){ sleep(100); return;
					 */
				}
			}
			if (!leaseExecuteOfferTask()) {
				Thread.yield();
				return;
			}
		} catch (RemoteException ex) {
			// ignore.printStackTrace();
			Logger.getLogger("executor").warning("RemoteSession closed.");
			this.taskTracker.stop();
		}
	}

	private void sleep(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException ignore) {
		}
	}
	
	private boolean leaseExecuteOfferTask() throws RemoteException {
		T task = leaseTask();
		if (task == null) {
			return false;
		}

		boolean offer = false;
		do {
			try {
				offer = this.taskTracker.getDownloadedTaskQueue().offer(
						createExecutable(task), (long)50,
						TimeUnit.MILLISECONDS);
			} catch (InterruptedException ignore) {
				Thread.yield();
			} catch (ClassCastException ignore) {
				ignore.printStackTrace();
			} catch (NullPointerException ignore) {
				ignore.printStackTrace();
			} catch (IllegalArgumentException ignore) {
				ignore.printStackTrace();
			}
		} while (!offer);

		return true;
	}

	abstract protected T leaseTask()throws RemoteException;
	abstract protected AbstractExecutable<T,D> createExecutable(T task);
}
