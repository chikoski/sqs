/**
 *  SessionTaskDaemons.java

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

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.sqs2.lang.GroupThreadFactory;
import net.sqs2.net.MulticastNetworkConnection;
import net.sqs2.net.RMIRegistryMulticastAdvertisingService;
import net.sqs2.omr.model.MarkReaderConfiguration;
import net.sqs2.omr.model.MarkReaderConstants;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.model.PageTaskHolder;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.sound.SoundManager;


public class SessionTaskDaemons {

	public static final int TASK_PRODUCER_EXEC_INTERVAL_IN_MILLIS = 10000;// msec
	public static final int TASK_CONSUMER_EXEC_INTERVAL_IN_MILLIS = 5;// msec
	public static final int RECYCLE_INTERVAL_IN_SEC = 11; // sec

	private GroupThreadFactory GROUP_THREAD_FACTORY_NORMAL_PRIORITY = 
		new GroupThreadFactory("SessionTaskDaemons",
				Thread.NORM_PRIORITY - 1, true);

	private GroupThreadFactory GROUP_THREAD_FACTORY_HIGH_PRIORITY = 
		new GroupThreadFactory("SessionTaskDaemons", 
				Thread.NORM_PRIORITY + 1, true);
	
	private GroupThreadFactory GROUP_THREAD_FACTORY_LOW_PRIORITY = 
		new GroupThreadFactory("SessionTaskDaemons",
				Thread.MIN_PRIORITY, true);

	private ScheduledExecutorService remoteTaskRecycleService;
	private ScheduledExecutorService localTaskRecycleService;

	private ScheduledExecutorService sessionSourceFactoryService;
	private ScheduledExecutorService pageTaskProducerService;
	private ScheduledExecutorService pageTaskConsumerService;
	private ScheduledExecutorService resultOutputWorkerService;

	private Future<?> localTaskRecycleFuture;
	private Future<?> remoteTaskRecycleFuture;
	private Future<SessionSource> sessionSourceFactoryFuture;
	private Future<?> pageTaskProduceFuture;
	private Future<?> pageTaskConsumeFuture;
	private Future<?> resultOutputWorkerFuture;

	private RMIRegistryMulticastAdvertisingService advertisingService = null;
	private SessionSourceServerDispatcher dispatcher;
	private PageTaskHolder pageTaskHolder;
	private String rmiBindingName;
	
	public SessionTaskDaemons(long sessionID, AbstractRemoteTaskTracker<OMRPageTask, SessionSourceServerDispatcher> taskTracker, SessionSourceServerDispatcher dispatcher, PageTaskHolder pageTaskHolder) {
		this.advertisingService = createAdvertisingService(sessionID, taskTracker, dispatcher.getKey());
		this.dispatcher = dispatcher;
		this.remoteTaskRecycleService = Executors.newScheduledThreadPool(1, GROUP_THREAD_FACTORY_LOW_PRIORITY);
		this.localTaskRecycleService = Executors.newScheduledThreadPool(1, GROUP_THREAD_FACTORY_LOW_PRIORITY);
		this.pageTaskHolder = pageTaskHolder;		
		this.rmiBindingName = taskTracker.getRMIBindingName();
		startTaskRecycleThread();
	}
	
	private void startTaskRecycleThread() {
		this.remoteTaskRecycleFuture = this.remoteTaskRecycleService.scheduleWithFixedDelay(new RemotePageTaskRecycleService(this.pageTaskHolder),
				RECYCLE_INTERVAL_IN_SEC, RECYCLE_INTERVAL_IN_SEC, TimeUnit.SECONDS);

		this.localTaskRecycleFuture = this.localTaskRecycleService.scheduleWithFixedDelay(new LocalPageTaskRecycleService(this.pageTaskHolder),
				RECYCLE_INTERVAL_IN_SEC + 1, RECYCLE_INTERVAL_IN_SEC, TimeUnit.SECONDS);
	}
	
	void start(){
		this.sessionSourceFactoryService = Executors.newScheduledThreadPool(1, GROUP_THREAD_FACTORY_NORMAL_PRIORITY);
		this.pageTaskProducerService = Executors.newScheduledThreadPool(1, GROUP_THREAD_FACTORY_NORMAL_PRIORITY);
		this.pageTaskConsumerService = Executors.newScheduledThreadPool(1, GROUP_THREAD_FACTORY_HIGH_PRIORITY);
		this.resultOutputWorkerService = Executors.newScheduledThreadPool(1, GROUP_THREAD_FACTORY_NORMAL_PRIORITY);
		startAdvertisement();
	}

	Future<SessionSource> startAndWaitSessionSourceFactory(Callable<SessionSource> task) {
		this.sessionSourceFactoryFuture = this.sessionSourceFactoryService.submit(task);
		return this.sessionSourceFactoryFuture;
	}

	Future<?> startPageTaskProducer(Runnable task) {
		this.pageTaskProduceFuture = this.pageTaskProducerService.submit(task);
		return this.pageTaskProduceFuture;
	}

	Future<?> startAndWaitPageTaskCommitService(Callable<Void> task) {
		this.pageTaskConsumeFuture = this.pageTaskConsumerService.submit(task);
		return this.pageTaskConsumeFuture;
	}

	Future<?> startAndWaitResultOutputWorker(Callable<Void> task) {
		this.resultOutputWorkerFuture = this.resultOutputWorkerService.submit(task);
		return this.resultOutputWorkerFuture;
	}

	void stop() {
		stopAdvertisement();
		
		stopFuture(this.sessionSourceFactoryFuture);
		this.sessionSourceFactoryFuture = null;
		stopFuture(this.pageTaskProduceFuture);
		this.pageTaskProduceFuture = null;
		stopFuture(this.pageTaskConsumeFuture);
		this.pageTaskConsumeFuture = null;
		stopFuture(this.resultOutputWorkerFuture);
		this.resultOutputWorkerFuture = null;
		
	}

	private void stopFuture(Future<?> future) {
		if (future != null) {
			future.cancel(true);
		}
	}

	void close() {
		stopFuture(this.localTaskRecycleFuture);
		this.localTaskRecycleFuture = null;
		stopFuture(this.remoteTaskRecycleFuture);
		this.remoteTaskRecycleFuture = null;

		this.localTaskRecycleService.shutdown();
		this.remoteTaskRecycleService.shutdown();
		
		this.sessionSourceFactoryService.shutdown();
		this.pageTaskProducerService.shutdown();
		this.pageTaskConsumerService.shutdown();
		this.resultOutputWorkerService.shutdown();
		finishAdvertisement();
	}
	
	private RMIRegistryMulticastAdvertisingService createAdvertisingService(long sessionID, AbstractRemoteTaskTracker<OMRPageTask, SessionSourceServerDispatcher> taskTrackeer, long key) {
		MulticastNetworkConnection multicastNetworkConnection = taskTrackeer.getMulticastNetworkConnection();
		int rmiPort = taskTrackeer.getRMIPort();
		if (multicastNetworkConnection != null) {
			try {
				RMIRegistryMulticastAdvertisingService ret = new RMIRegistryMulticastAdvertisingService(
						multicastNetworkConnection, key, sessionID,
						MarkReaderConstants.ADVERTISE_SERVICE_THREAD_PRIORITY, rmiPort,
						MarkReaderConstants.SESSION_SOURCE_ADVERTISE_DELAY_IN_SEC){
					
					@Override
					public void logAdvertisement(){
						SoundManager.getInstance().play("pi78.wav");
					}
				};
				return ret;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	public String getRMIBindingName() {
		return rmiBindingName;
	}

	private void startAdvertisement() {
		try {
			if (this.advertisingService != null && MarkReaderConfiguration.isEnabled(MarkReaderConfiguration.KEY_PARALLEL)) {
				String bindingName = getRMIBindingName();
				this.advertisingService.startAdvertising(dispatcher.getLocalServer(), bindingName);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void stopAdvertisement() {
		if (this.advertisingService != null) {
			this.advertisingService.stopAdvertising(rmiBindingName);
			this.advertisingService = null;
		}
	}
	
	private void finishAdvertisement() {
		stopAdvertisement();
	}
}
