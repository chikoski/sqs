/**
 * AbstractTaskTracker.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2007/01/31
 Author hiroya
 */

package net.sqs2.omr.session.service;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.sqs2.lang.GroupThreadFactory;
import net.sqs2.omr.model.Ticket;

public abstract class AbstractTaskTracker<T extends Ticket, D extends ServerDispatcher> {

	public static final int EXECUTOR_THREAD_PRIORIY = Thread.NORM_PRIORITY - 2;
	public static boolean DEBUG_CLUSTER_MODE = false;

	private String name;
	TaskTracker<T,D> taskTracker = null;
	protected D dispatcher;

	private ScheduledExecutorService executorThreadPool;
	private Future<?>[] executorFutures = null;

	private boolean isConnected = false;

	public AbstractTaskTracker() {
		super();
	}

	public AbstractTaskTracker(String name, TaskTracker<T,D> taskTracker, D dispatcher) {
		this.name = name;
		this.taskTracker = taskTracker;
		this.dispatcher = dispatcher;
		start();
	}
	
	public D getDispatcher(){
		return this.dispatcher;
	}

	public void start() {
		setConnected(true);
		int numExecutorThreads = 1;
		GroupThreadFactory groupThreadFactory = new GroupThreadFactory(this.name + ":TaskDownloaderThread",
				EXECUTOR_THREAD_PRIORIY, true);
		if (this.executorThreadPool == null) {
			this.executorThreadPool = Executors.newScheduledThreadPool(numExecutorThreads,
					groupThreadFactory);
		}
		if (this.executorFutures == null) {
			this.executorFutures = new Future[numExecutorThreads];
			for (int i = 0; i < numExecutorThreads; i++) {
				this.executorFutures[i] = this.executorThreadPool.scheduleWithFixedDelay(
						createTaskExecutor(),
						200 * i, 10, TimeUnit.MILLISECONDS);
			}
		}
		this.taskTracker.start();
	}
	
	protected abstract TaskExecutor<T,D> createTaskExecutor();

	public void stop() {
		if (this.executorFutures != null) {
			for (int i = 0; i < this.executorFutures.length; i++) {
				if (this.executorFutures[i] != null) {
					this.executorFutures[i].cancel(true);
				}
			}
			this.executorFutures = null;
		}
		if (this.executorThreadPool != null) {
			this.executorThreadPool.shutdown();
			this.executorThreadPool = null;
		}
	}

	public void shutdown() {
		stop();
		setConnected(false);
		this.taskTracker.stop();
		if (this.dispatcher != null) {
			this.dispatcher.close();
			this.dispatcher = null;
		}
	}
	
	BlockingQueue<AbstractExecutable<T,D>> getDownloadedTaskQueue() {
		return this.taskTracker.getDownloadedTaskQueue();
	}

	public long getKey() {
		return this.dispatcher.getKey();
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public boolean isConnected() {
		try {
			this.dispatcher.getServer().ping(this.dispatcher.getKey());
			return this.isConnected;
		} catch (RemoteException ignore) {
			stop();
			return false;
		}
	}

}
