package net.sqs2.omr.task.broker;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.sqs2.lang.GroupThreadFactory;
import net.sqs2.omr.app.MarkReaderConstants;
import net.sqs2.omr.task.ExecutableTaskWrapper;
import net.sqs2.sound.SoundManager;


public class TaskExecutorPeer {
	public static final int PAGETASK_EXECUTOR_THREAD_PRIORIY = Thread.NORM_PRIORITY - 1;

	private ArrayBlockingQueue<ExecutableTaskWrapper> downloadedTaskQueue;
	private ArrayBlockingQueue<ExecutableTaskWrapper> uploadingTaskQueue;
	
	private ScheduledExecutorService executorThreadPool;
	private ScheduledExecutorService uploaderThreadPool;
	
	private Future<?>[] executorFutures = null;
	private Future<?>[] uploaderFutures = null;

	int numExecutorThreads;
	int numUploaderThreads;
	TaskExecutorLogic taskExecutorLogic;
	
	boolean isRunning = false;
	
	public TaskExecutorPeer(TaskExecutorLogic taskExecutorLogic) {

		this.numExecutorThreads = Runtime.getRuntime().availableProcessors();
        this.numUploaderThreads = 1;//Runtime.getRuntime().availableProcessors();
		
        this.taskExecutorLogic = taskExecutorLogic;
		
		this.downloadedTaskQueue = new ArrayBlockingQueue<ExecutableTaskWrapper>(numExecutorThreads+1);
		this.uploadingTaskQueue = new ArrayBlockingQueue<ExecutableTaskWrapper>(numExecutorThreads+1);
		start();
	}

	class ExecutorThread implements Runnable{

		TaskExecutorLogic logic;

		ExecutorThread(TaskExecutorLogic logic){
			this.logic = logic;
		}

		public void run(){
			ExecutableTaskWrapper task = null;
			try{
				task = downloadedTaskQueue.poll(50, TimeUnit.MILLISECONDS);
				if(task == null){
					return;
				}
				this.logic.execute(task);
				
				SoundManager.getInstance().play(MarkReaderConstants.TASK_EXECUTION_SOUND_FILENAME);	

				boolean offer = false;
				do{
					try{
						offer = uploadingTaskQueue.offer(task, 50, TimeUnit.MILLISECONDS);
					}catch(InterruptedException ignore){
					}catch(NullPointerException ignore){
					}
				}while(isRunning && ! offer);
				
			}catch(InterruptedException ignore){
			}catch(RuntimeException ex){
				ex.printStackTrace();
			}catch(NoSuchObjectException ignore){
			}catch(RemoteException ignore){
				// ignore exception from RMI connection handling. we will drop the uploading task but never stop uploading thread itself.
				// ignore.printStackTrace();
			}
		}
	}

	protected class PageTaskUploader implements Runnable{
		
		PageTaskUploader(){}
		
		public void run(){
			try{
				ExecutableTaskWrapper task = uploadingTaskQueue.poll(100, TimeUnit.MILLISECONDS);
				if(task == null){
					return;
				}
				task.getTaskExecutorEnv().getSessionService().submitPageTask(task.getTaskExecutorEnv().getKey(), task.getTask().getSessionID(), task.getTask());
			}catch(InterruptedException ignore){
			}catch(RemoteException ignore){
				// ignore exception in uploading. we will drop the uploading task but never stop uploading thread itself.
			}
		}
	}
	
	void start(){
		this.isRunning = true;
		if(this.executorThreadPool == null){
			GroupThreadFactory groupThreadFactory1 = new GroupThreadFactory("TaskExecutorManager#ExecutorThread", PAGETASK_EXECUTOR_THREAD_PRIORIY - 1, true);
			this.executorThreadPool = Executors.newScheduledThreadPool(this.numExecutorThreads, groupThreadFactory1);
		}
		
		if(this.uploaderThreadPool == null){
			GroupThreadFactory groupThreadFactory2 = new GroupThreadFactory("TaskExecutorManager#UploaderThread", PAGETASK_EXECUTOR_THREAD_PRIORIY - 2, true);
			this.uploaderThreadPool = Executors.newScheduledThreadPool(this.numUploaderThreads, groupThreadFactory2);
		}

		if(this.executorFutures == null){
			this.executorFutures = new Future[this.numExecutorThreads];
			ExecutorThread executorThread = new ExecutorThread(this.taskExecutorLogic);
			for(int i = 0; i < this.numExecutorThreads; i++){
				this.executorFutures[i] = this.executorThreadPool.scheduleWithFixedDelay(executorThread,
						13 * i, 9, TimeUnit.MILLISECONDS);
			}
		}
		if(this.uploaderFutures == null){
			this.uploaderFutures = new Future[this.numUploaderThreads];
			PageTaskUploader pageTaskUploader = new PageTaskUploader();
			for(int i = 0; i < this.numUploaderThreads; i++){
				this.uploaderFutures[i] = this.uploaderThreadPool.scheduleWithFixedDelay(pageTaskUploader,
						15 * i, 11, TimeUnit.MILLISECONDS);			
			}
		}
	}

	void stop() {
		this.isRunning = false;
		this.uploadingTaskQueue.clear();
		this.downloadedTaskQueue.clear();

		if(this.executorFutures != null){
			for(int i = 0; i < this.executorFutures.length; i++){
				this.executorFutures[i].cancel(true);
			}
			this.executorFutures = null;
		}
		
		if(this.uploaderFutures != null){
			for(int i = 0; i < this.uploaderFutures.length; i++){
				this.uploaderFutures[i].cancel(true);
			}
			this.uploaderFutures = null;
		}
		
		if(this.uploaderThreadPool != null){
			this.uploaderThreadPool.shutdown();
			this.uploaderThreadPool = null;
		}
		
		if(this.executorThreadPool != null){
			this.executorThreadPool.shutdown();
			this.executorThreadPool = null;
		}
	}
	
	BlockingQueue<ExecutableTaskWrapper> getDownloadedTaskQueue(){
		return this.downloadedTaskQueue;
	}
}
