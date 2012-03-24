package net.sqs2.omr.session.service;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.sqs2.lang.GroupThreadFactory;
import net.sqs2.omr.model.Ticket;


public class TaskTracker<T extends Ticket, D extends ServerDispatcher> {
	public static final int TASK_EXECUTOR_THREAD_PRIORIY = Thread.NORM_PRIORITY - 1;

	private ArrayBlockingQueue<AbstractExecutable<T,D>> downloadedTaskQueue;
	private ArrayBlockingQueue<AbstractExecutable<T,D>> uploadingTaskQueue;

	private ScheduledExecutorService executorThreadPool;
	private ScheduledExecutorService uploaderThreadPool;

	private Future<?>[] executorFutures = null;
	private Future<?>[] uploaderFutures = null;

	int numExecutorThreads;
	int numUploaderThreads;

	boolean isRunning = false;
	
	public TaskTracker() {

		this.numExecutorThreads = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
		this.numUploaderThreads = 1;

		this.downloadedTaskQueue = new ArrayBlockingQueue<AbstractExecutable<T,D>>(this.numExecutorThreads);
		this.uploadingTaskQueue = new ArrayBlockingQueue<AbstractExecutable<T,D>>(this.numExecutorThreads);
		start();
	}

	class ExecutorThread implements Runnable {

		ExecutorThread() {
		}

		public void run() {
			AbstractExecutable<T,D> task = null;
			try {
				task = TaskTracker.this.downloadedTaskQueue.poll(100, TimeUnit.MILLISECONDS);
				if (task == null) {
					return;
				}
				task.execute();
				boolean offer = false;
				do {
					try {
						offer = TaskTracker.this.uploadingTaskQueue.offer(task, 100,
								TimeUnit.MILLISECONDS);
					} catch (InterruptedException ignore) {
					} catch (NullPointerException ignore) {
					}
				} while (TaskTracker.this.isRunning && !offer);

			} catch (InterruptedException ignore) {
			} catch (RuntimeException ex) {
				ex.printStackTrace();
			} catch (NoSuchObjectException ignore) {
			} catch (RemoteException ignore) {
				// ignore exception from RMI connection handling. we will drop
				// the uploading task but never stop uploading thread itself.
				// ignore.printStackTrace();
			}
		}
	}

	protected class TaskUploader implements Runnable {

		TaskUploader() {
		}

		public void run() {
			try {
				AbstractExecutable<T,D> executable = TaskTracker.this.uploadingTaskQueue.poll(100,
						TimeUnit.MILLISECONDS);
				if (executable == null) {
					return;
				}
				executable.emit();
			} catch (InterruptedException ignore) {
			} catch (RemoteException ignore) {
				// ignore exception in uploading. we will drop the uploading
				// task but never stop uploading thread itself.
			}
		}
	}

	void start() {
		this.isRunning = true;
		if (this.executorThreadPool == null) {
			GroupThreadFactory executorGroupThreadFactory = new GroupThreadFactory(
					"TaskExecutorManager#ExecutorThread", TASK_EXECUTOR_THREAD_PRIORIY - 1, true);
			this.executorThreadPool = Executors.newScheduledThreadPool(this.numExecutorThreads,
					executorGroupThreadFactory);
		}

		if (this.uploaderThreadPool == null) {
			GroupThreadFactory uploaderGroupThreadFactory = new GroupThreadFactory(
					"TaskExecutorManager#UploaderThread", TASK_EXECUTOR_THREAD_PRIORIY - 2, true);
			this.uploaderThreadPool = Executors.newScheduledThreadPool(this.numUploaderThreads,
					uploaderGroupThreadFactory);
		}

		if (this.executorFutures == null) {
			this.executorFutures = new Future[this.numExecutorThreads];
			ExecutorThread executorThread = new ExecutorThread();
			for (int i = 0; i < this.numExecutorThreads; i++) {
				this.executorFutures[i] = this.executorThreadPool.scheduleWithFixedDelay(executorThread,
						14 * i, 11, TimeUnit.MILLISECONDS);
			}
		}
		if (this.uploaderFutures == null) {
			this.uploaderFutures = new Future[this.numUploaderThreads];
			TaskUploader taskUploader = new TaskUploader();
			for (int i = 0; i < this.numUploaderThreads; i++) {
				this.uploaderFutures[i] = this.uploaderThreadPool.scheduleWithFixedDelay(taskUploader,
						15 * i, 13, TimeUnit.MILLISECONDS);
			}
		}
	}

	void stop() {
		this.isRunning = false;
		this.uploadingTaskQueue.clear();
		this.downloadedTaskQueue.clear();

		if (this.executorFutures != null) {
			for (int i = 0; i < this.executorFutures.length; i++) {
				this.executorFutures[i].cancel(true);
			}
			this.executorFutures = null;
		}

		if (this.uploaderFutures != null) {
			for (int i = 0; i < this.uploaderFutures.length; i++) {
				this.uploaderFutures[i].cancel(true);
			}
			this.uploaderFutures = null;
		}

		if (this.uploaderThreadPool != null) {
			this.uploaderThreadPool.shutdown();
			this.uploaderThreadPool = null;
		}

		if (this.executorThreadPool != null) {
			this.executorThreadPool.shutdown();
			this.executorThreadPool = null;
		}
	}

	public BlockingQueue<AbstractExecutable<T,D>> getDownloadedTaskQueue() {
		return this.downloadedTaskQueue;
	}

}
