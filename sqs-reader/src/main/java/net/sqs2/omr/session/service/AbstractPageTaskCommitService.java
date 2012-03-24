package net.sqs2.omr.session.service;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.model.SessionSourceState;

public abstract class AbstractPageTaskCommitService implements PageTaskCommitService, Callable<Void> {

	protected MarkReaderSession markReaderSession;

	public AbstractPageTaskCommitService(MarkReaderSession markReaderSession) throws IOException {
		this.markReaderSession = markReaderSession;
	}

	public Void call() throws IOException {
		while (true) {
			OMRPageTask task = this.markReaderSession.getTaskHolder().pollSubmittedTask();
			if (task != null) {
				storeTask(task);
				this.markReaderSession.notifyStoreTask(task);
			} else if (this.canFinish()) {
				break;
			} else {
				// no submitted tasks and still remains leased tasks, wait 500 msec.
				try {
					Thread.sleep(500);
				} catch (InterruptedException ignore) {
				}
				continue;
			}
			Logger.getLogger(getClass().getName()).info(this.markReaderSession.toString());
		}
		return null;
	}

	public boolean canFinish() throws IOException {
		boolean isProcessing = markReaderSession.getSessionSourceState().equals(SessionSourceState.PROCESSING);
		boolean isEmpty = this.markReaderSession.getTaskHolder().isEmpty();
		if (isProcessing && isEmpty) {
			return true;
		}
		return false;
	}

	void storeTask(OMRPageTask task) {
		try {
			commit(task);
		} catch (Exception ex) {
			//throw new RuntimeException(ex);//TODO: handle errors
			//FIXME!
		}
	}

	abstract public void commit(OMRPageTask task) throws RemoteException;
}
