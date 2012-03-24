/**
 * 
 */
package net.sqs2.omr.session.service;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.sqs2.lang.GroupThreadFactory;
import net.sqs2.omr.model.Ticket;

class RemoteTaskTrackerManager <T extends Ticket, D extends ServerDispatcher>{
	Map<String, AbstractTaskTracker<T,D>> taskTrackerMap;

	RemoteTaskTrackerManager(Map<String, AbstractTaskTracker<T,D>> remoteTaskTrackerMap) {
		this(remoteTaskTrackerMap, 60);
	}

	RemoteTaskTrackerManager(Map<String, AbstractTaskTracker<T,D>> remoteTaskTrackerMap, int executionDelayInSec) {
		this.taskTrackerMap = remoteTaskTrackerMap;
		Executors.newSingleThreadScheduledExecutor(
				new GroupThreadFactory("RemoteExecutor", Thread.NORM_PRIORITY, true))
				.scheduleWithFixedDelay(new Runnable() {
					public void run() {
						cleanupConnections();
					}
				}, executionDelayInSec, executionDelayInSec, TimeUnit.SECONDS);
	}

	private void cleanupConnections() {
		for (Map.Entry<String, AbstractTaskTracker<T,D>> e : this.taskTrackerMap.entrySet()) {
			String uri = e.getKey();
			AbstractTaskTracker<T,D> taskTracker = e.getValue();
			if (taskTracker.isConnected() == false) {
				this.taskTrackerMap.remove(uri);
				Logger.getLogger("executor").info("Remove old taskTracker=" + uri);
			}
		}
	}
}
