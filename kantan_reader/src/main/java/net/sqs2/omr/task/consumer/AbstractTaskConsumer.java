package net.sqs2.omr.task.consumer;

import java.io.IOException;
import java.rmi.RemoteException;

import net.sqs2.omr.session.Session;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.task.PageTask;

public abstract class AbstractTaskConsumer implements TaskConsumer{

	private Session session;

	public AbstractTaskConsumer(Session session)throws IOException{
		this.session = session; 
	}

	public void run() {
		try{
			while(true){
				PageTask task = this.session.getTaskHolder().pollSubmittedTask();
				if(task != null){
					storeTask(task);
					this.session.notifyStoreTask(task);
				}else if(this.canFinish()){
					this.session.getSessionSource().setFinished();
					this.session.exportSession();
					this.session.finishSession();
					break;
				}else{
					// no submitted task, still remains leased tasks, wait 1 sec.
					try{
						Thread.sleep(1000);
					}catch(InterruptedException ignore){}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			this.session.stopSession();
		}
	}
	
	public boolean canFinish() throws IOException{
		SessionSource sessionSource = this.session.getSessionSource();
		boolean isPrepared = sessionSource.isPrepared();
		boolean isEmpty = this.session.getTaskHolder().isEmpty();
		if(isPrepared && isEmpty){
			return true; 
		}
		return false;
	}
	
	void storeTask(PageTask task) {
		try{			
	    	consumeTask(task);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	abstract public void consumeTask(PageTask task)throws RemoteException;
}
