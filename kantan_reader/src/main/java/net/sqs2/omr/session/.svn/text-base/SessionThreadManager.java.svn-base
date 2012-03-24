/**
 *  SessionThreadManager.java

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

package net.sqs2.omr.session;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.sqs2.lang.GroupThreadFactory;
import net.sqs2.omr.task.PageTask;
import net.sqs2.omr.task.TaskHolder;

public class SessionThreadManager{

	public static final int TASK_PRODUCER_EXEC_INTERVAL_IN_MILLIS = 10000;// msec
	public static final int TASK_CONSUMER_EXEC_INTERVAL_IN_MILLIS = 5;// msec
	public static final int RECYCLE_INTERVAL_IN_SEC = 11; //sec

	private GroupThreadFactory groupThreadFactoryNormal, groupThreadFactoryHigh;

	private ScheduledExecutorService remoteTaskRecycleService;
	private ScheduledExecutorService taskProducerService;
	private ScheduledExecutorService localTaskRecycleService;
	private ScheduledExecutorService taskConsumerService;

	private Future<?> taskProducerFuture;
	private Future<?> localTaskRecycleFuture;
	private Future<?> remoteTaskRecycleFuture;
	private Future<?> taskConsumerFuture;

	public SessionThreadManager(){
		this.groupThreadFactoryNormal = new GroupThreadFactory("net.sqs2.exigrid.session.SessionThreadManager", Thread.NORM_PRIORITY - 1, true);
		this.groupThreadFactoryHigh = new GroupThreadFactory("net.sqs2.exigrid.session.SessionThreadManager", Thread.MAX_PRIORITY, true);
		this.remoteTaskRecycleService = Executors.newScheduledThreadPool(1, this.groupThreadFactoryNormal);
		this.localTaskRecycleService = Executors.newScheduledThreadPool(1, this.groupThreadFactoryNormal);
		this.taskProducerService = Executors.newSingleThreadScheduledExecutor(this.groupThreadFactoryNormal);
		this.taskConsumerService = Executors.newScheduledThreadPool(1, this.groupThreadFactoryHigh);
	}

	void startTaskRecycleThread(final TaskHolder taskHolder) {		
		this.remoteTaskRecycleFuture = this.remoteTaskRecycleService.scheduleWithFixedDelay(new Runnable(){
			public void run(){
				try{
					PageTask task;
					while((task = taskHolder.takeRemoteLeasedTask()) != null){
						taskHolder.addPreparedTask(task);
					}
				}catch(InterruptedException ignore){
				}
			}
		}, RECYCLE_INTERVAL_IN_SEC, RECYCLE_INTERVAL_IN_SEC, TimeUnit.SECONDS);
		
		this.localTaskRecycleFuture = this.localTaskRecycleService.scheduleWithFixedDelay(new Runnable(){
			public void run(){
				try{
					PageTask task;
					while((task = taskHolder.takeLocalLeasedTask()) != null){
						taskHolder.addPreparedTask(task);
					}
				}catch(InterruptedException ignore){
				}
			}
		}, RECYCLE_INTERVAL_IN_SEC+1, RECYCLE_INTERVAL_IN_SEC, TimeUnit.SECONDS);
	}

	/**
	 * 
	 * 	@param task
	 * 	@return
 	*/
	Future<?> startTaskProducer(Runnable task){
		return (this.taskProducerFuture = this.taskProducerService.submit(task));
	}

	Future<?> startAndWaitTaskConsumer(Runnable task){
		return this.taskConsumerFuture = this.taskConsumerService.submit(task);
		/*
		return (this.taskConsumerFuture = this.taskConsumerService.scheduleWithFixedDelay(task,
				0,
				TASK_CONSUMER_EXEC_INTERVAL_IN_MILLIS,
				TimeUnit.MILLISECONDS));
				*/
	}

	void stop(){
		if(this.localTaskRecycleFuture != null){
			this.localTaskRecycleFuture.cancel(true);
		}
		if(this.taskProducerFuture != null){
			this.taskProducerFuture.cancel(true);
		}
		if(this.remoteTaskRecycleFuture != null){
			this.remoteTaskRecycleFuture.cancel(true);
		}
		if(this.taskConsumerFuture != null){
			this.taskConsumerFuture.cancel(true);
		}
	}

	void shutdown(){
		this.taskProducerService.shutdown();
		this.remoteTaskRecycleService.shutdown();
		this.localTaskRecycleService.shutdown();
		this.taskConsumerService.shutdown();
	}
}
