/**
 *  PageTaskHolder.java

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
package net.sqs2.omr.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PageTaskHolder implements Externalizable {

	private static final long serialVersionUID = 0L;

	private int numTargetTasks;
	private int numReusedTasks;
	private int numExternalizingTasks;

	transient private BlockingQueue<OMRPageTask> preparedTaskQueue = null;
	transient private Set<OMRPageTask> preparedTaskSet = null;
	transient private DelayQueue<OMRPageTask> localLeasedTaskQueue = null;
	transient private DelayQueue<OMRPageTask> remoteLeasedTaskQueue = null;
	transient private BlockingQueue<OMRPageTask> submittedTaskQueue = null;
	transient private int externalizedTaskIndex = 0;

	public PageTaskHolder() {
		initQueue();
	}
	
	public String toString(){
		return "PageTaskHolder[R="+this.numReusedTasks+" p="+this.preparedTaskQueue.size()+" l="+this.localLeasedTaskQueue.size()+" r="+this.remoteLeasedTaskQueue.size()+" s="+this.submittedTaskQueue.size()+"]";
	}
	
	public synchronized PageTaskNumberCounter createTaskNumberCounter(){
		return new PageTaskNumberCounter(
				this.getNumTotalTasks(),
				this.getNumPreparedTasks(),
				this.getNumReusedTasks(),
				this.getNumLocalLeasedTasks(),
				this.getNumRemoteLeasedTasks(),
				this.getNumSubmittedTasks());
	}
	
	private void initQueue() {
		this.preparedTaskQueue = new LinkedBlockingQueue<OMRPageTask>();
		this.preparedTaskSet = Collections.synchronizedSet(new HashSet<OMRPageTask>());
		this.localLeasedTaskQueue = new DelayQueue<OMRPageTask>();
		this.remoteLeasedTaskQueue = new DelayQueue<OMRPageTask>();
		this.submittedTaskQueue = new LinkedBlockingQueue<OMRPageTask>();
	}

	public void readExternal(ObjectInput in) throws IOException, java.lang.ClassNotFoundException {
		initQueue();
	}

	public void writeExternal(ObjectOutput in) throws IOException {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.sqs_xml.omr.session.TaskHolder#getNumTargetTasks()
	 */
	public int getNumTargetTasks() {
		return this.numTargetTasks;
	}
	
	public int getNumTotalTasks(){
		return this.numReusedTasks + this.numTargetTasks;
	}


	public synchronized void incrementNumTargetTasks(int numTargetTasks) {
		this.numTargetTasks += numTargetTasks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.sqs_xml.omr.session.TaskHolder#getNumReusedTasks()
	 */
	public int getNumReusedTasks() {
		return this.numReusedTasks;
	}

	public synchronized void setNumReusedTasks(int numReusedTasks) {
		this.numReusedTasks = numReusedTasks;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.sqs_xml.omr.session.TaskHolder#getNumPreparedTasks()
	 */
	public int getNumPreparedTasks() {
		return this.preparedTaskSet.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.sqs_xml.omr.session.PageTaskHolder#getNumLocalLeasedTasks()
	 */
	public int getNumLocalLeasedTasks() {
		return this.localLeasedTaskQueue.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.sqs_xml.omr.session.TaskHolder#getNumRemoteLeasedTasks()
	 */
	public int getNumRemoteLeasedTasks() {
		return this.remoteLeasedTaskQueue.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.sqs_xml.omr.session.TaskHolder#getNumSubmittedTasks()
	 */
	public int getNumSubmittedTasks() {
		return this.submittedTaskQueue.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.sqs_xml.omr.session.TaskHolder#getNumExternalizedTasks()
	 */
	public int getNumExternalizingTasks() {
		return this.numExternalizingTasks;
	}

	public int incrementNumExternalizingTasks() {
		this.externalizedTaskIndex++;
		return this.numExternalizingTasks++;
	}

	synchronized void setNumExternalizingTasks(int numExternalizingTasks) {
		this.numExternalizingTasks = numExternalizingTasks;
	}

	public synchronized void stop() {
		this.numReusedTasks = 0;
		this.preparedTaskQueue.clear();
		this.preparedTaskSet.clear();
		this.localLeasedTaskQueue.clear();
		this.remoteLeasedTaskQueue.clear();
		// this.submittedTaskQueue.clear();
	}

	public synchronized void clear() {
		this.numReusedTasks = 0;
		this.numTargetTasks = 0;
		this.numExternalizingTasks = 0;
		this.externalizedTaskIndex = 0;
		this.preparedTaskQueue.clear();
		this.preparedTaskSet.clear();
		this.localLeasedTaskQueue.clear();
		this.remoteLeasedTaskQueue.clear();
		this.submittedTaskQueue.clear();
	}

	public synchronized void addPreparedTask(OMRPageTask task) {
		this.preparedTaskSet.add(task);
		this.preparedTaskQueue.offer(task);
	}

	public synchronized OMRPageTask leaseTask(long timeout) throws InterruptedException {
		OMRPageTask task = this.preparedTaskQueue.poll(timeout, TimeUnit.MILLISECONDS);
		if(task != null){
			this.preparedTaskSet.remove(task);
		}
		return task;
	}

	public synchronized void addLeaseLocalTask(OMRPageTask task) {
		this.localLeasedTaskQueue.add(task);
	}

	public synchronized void addLeaseRemoteTask(OMRPageTask task) {
		this.remoteLeasedTaskQueue.add(task);
	}

	public boolean isPreparedTask(Ticket task) {
		return this.preparedTaskSet.contains(task);
	}

	public boolean isEmpty() {
		return this.preparedTaskQueue.isEmpty() && this.localLeasedTaskQueue.isEmpty()
				&& this.remoteLeasedTaskQueue.isEmpty() && this.submittedTaskQueue.isEmpty();
	}

	public synchronized OMRPageTask takeLocalLeasedExpiredTask() throws InterruptedException {
		return this.localLeasedTaskQueue.poll(100, TimeUnit.MILLISECONDS);
	}

	public synchronized OMRPageTask takeRemoteLeasedExpiredTask() throws InterruptedException {
		return this.remoteLeasedTaskQueue.poll(100, TimeUnit.MILLISECONDS);
	}

	public boolean isLeasedTask(Ticket task) {
		return this.localLeasedTaskQueue.contains(task) || this.remoteLeasedTaskQueue.contains(task);
	}

	public synchronized Ticket submitTask(OMRPageTask task) {
	
		if (this.localLeasedTaskQueue.remove(task)) {
			this.submittedTaskQueue.add(task);
			return task;
		}
		
		if (this.remoteLeasedTaskQueue.remove(task)) {
			this.submittedTaskQueue.add(task);
			return task;
		}	

		Logger.getLogger(getClass().getName()).warning("submitted unknown task: " + task);
		return null;
	}

	public synchronized OMRPageTask pollSubmittedTask() {
		try {
			OMRPageTask task = this.submittedTaskQueue.poll(5, TimeUnit.MILLISECONDS);
			return task;
		} catch (InterruptedException ignore) {
			return null;
		}
	}

}
