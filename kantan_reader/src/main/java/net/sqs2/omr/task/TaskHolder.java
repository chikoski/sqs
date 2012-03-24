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
package net.sqs2.omr.task;

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


public class TaskHolder implements Externalizable{
	
	private static final long serialVersionUID = 0L;

	private int numTargetTasks;
	private int numReusedTasks;	
	private int numExternalizingTasks;

	transient private BlockingQueue<PageTask> preparedTaskQueue = null;
	transient private Set<PageTask> preparedTaskSet = null;
	transient private DelayQueue<PageTask> localLeasedTaskQueue = null;
	transient private DelayQueue<PageTask> remoteLeasedTaskQueue = null;
	transient private BlockingQueue<PageTask> submittedTaskQueue = null;
	//transient private Map<Integer,String> errorMap = null;
	transient private int externalizedTaskIndex = 0;

	public TaskHolder(){
		initQueue();
	}
	
	private void initQueue(){
		this.preparedTaskQueue = new LinkedBlockingQueue<PageTask>();
		this.preparedTaskSet = Collections.synchronizedSet(new HashSet<PageTask>());
		this.localLeasedTaskQueue = new DelayQueue<PageTask>();
		this.remoteLeasedTaskQueue = new DelayQueue<PageTask>();
		this.submittedTaskQueue = new LinkedBlockingQueue<PageTask>();
		//this.errorMap = new LinkedHashMap<Integer, String>();
	}
	
	public void readExternal(ObjectInput in) throws IOException, java.lang.ClassNotFoundException{
		initQueue();
    }

	public void writeExternal(ObjectOutput in) throws IOException{
		// do nothing
    }

	/* (non-Javadoc)
	 * @see net.sf.sqs_xml.omr.session.TaskHolder#getNumTargetTasks()
	 */
	public int getNumTargetTasks(){
		return this.numTargetTasks;
	}

	public void incrementNumTargetTasks(int numTargetTasks){
		this.numTargetTasks += numTargetTasks;
	}

	/* (non-Javadoc)
	 * @see net.sf.sqs_xml.omr.session.PageTaskHolder#getNumTotalTasks()
	public int getNumTotalTasks(){
		return getNumTargetTasks(); // + getNumReusedTasks()
	}
	 */

	/* (non-Javadoc)
	 * @see net.sf.sqs_xml.omr.session.TaskHolder#getNumReusedTasks()
	 */
	public int getNumReusedTasks(){
		return this.numReusedTasks;
	}

	public void setNumReusedTasks(int numReusedTasks){
		this.numReusedTasks = numReusedTasks;
	}

	/* (non-Javadoc)
	 * @see net.sf.sqs_xml.omr.session.TaskHolder#getNumPreparedTasks()
	 */
	public int getNumPreparedTasks(){
		return this.preparedTaskSet.size(); 
	}

	/* (non-Javadoc)
	 * @see net.sf.sqs_xml.omr.session.PageTaskHolder#getNumLocalLeasedTasks()
	 */
	public int getNumLocalLeasedTasks(){
		return this.localLeasedTaskQueue.size();
	}

	/* (non-Javadoc)
	 * @see net.sf.sqs_xml.omr.session.TaskHolder#getNumRemoteLeasedTasks()
	 */
	public int getNumRemoteLeasedTasks(){
		return this.remoteLeasedTaskQueue.size();
	}

	/* (non-Javadoc)
	 * @see net.sf.sqs_xml.omr.session.TaskHolder#getNumSubmittedTasks()
	 */
	public int getNumSubmittedTasks(){
		return this.submittedTaskQueue.size();
	}

	/* (non-Javadoc)
	 * @see net.sf.sqs_xml.omr.session.TaskHolder#getNumErrorTasks()
	 */
	/*
	public int getNumErrorTasks(){
		return this.errorMap.size();
	}*/

	/*
	public int incrementNumErrorTask(String message){
		this.errorMap.put(this.externalizedTaskIndex, message);
		this.externalizedTaskIndex++;
		return this.errorMap.size();
	}
	*/

	/* (non-Javadoc)
	 * @see net.sf.sqs_xml.omr.session.TaskHolder#getNumExternalizedTasks()
	 */
	public int getNumExternalizingTasks(){
		return this.numExternalizingTasks;
	}

	public int incrementNumExternalizingTasks(){		
		this.externalizedTaskIndex++;
		return this.numExternalizingTasks++;		
	}

	void setNumExternalizingTasks(int numExternalizingTasks){
		this.numExternalizingTasks = numExternalizingTasks;
		//update();		
	}

	/* (non-Javadoc)
	 * @see net.sf.sqs_xml.omr.session.TaskHolder#start()
	 */
	public void reset(){
		this.numReusedTasks = 0;
		this.numTargetTasks = 0;
		this.numExternalizingTasks = 0;
		this.externalizedTaskIndex = 0;
		this.preparedTaskQueue.clear();
		this.preparedTaskSet.clear();
		this.localLeasedTaskQueue.clear();
		this.remoteLeasedTaskQueue.clear();
		this.submittedTaskQueue.clear();
		//this.errorMap.clear();
	}

	public void addPreparedTask(PageTask task){
		this.preparedTaskSet.add(task);
		this.preparedTaskQueue.offer(task);
	}

	public PageTask addLeaseTask()throws InterruptedException{
		PageTask task = this.preparedTaskQueue.take();
		this.preparedTaskSet.remove(task);
		return task;
	}

	public void addLeaseLocalTask(PageTask task){
		this.localLeasedTaskQueue.add(task);
	}

	public void addLeaseRemoteTask(PageTask task){
		this.remoteLeasedTaskQueue.add(task);
	}

	public boolean isPreparedTask(AbstractTask task){
		return this.preparedTaskSet.contains(task);
	}

	public boolean isEmpty(){
		return this.preparedTaskQueue.isEmpty() &&
		this.localLeasedTaskQueue.isEmpty() &&
		this.remoteLeasedTaskQueue.isEmpty() && 
		this.submittedTaskQueue.isEmpty();
	}

	public PageTask takeLocalLeasedTask()throws InterruptedException{
		return this.localLeasedTaskQueue.take();
	}

	public PageTask takeRemoteLeasedTask()throws InterruptedException{
		return this.remoteLeasedTaskQueue.take();
	}	

	public boolean isLeasedTask(AbstractTask task){
		return this.localLeasedTaskQueue.contains(task) || this.remoteLeasedTaskQueue.contains(task);
	}

	public AbstractTask submitTask(PageTask task){
		if(this.localLeasedTaskQueue.remove(task)){
			this.submittedTaskQueue.add(task);
			return task;
		}else if(this.remoteLeasedTaskQueue.remove(task)){
			this.submittedTaskQueue.add(task);
			return task;
		}else{
			Logger.getAnonymousLogger().warning("submitted unknown task: "+task);
			return null;
		}		
	}

	public PageTask pollSubmittedTask(){
		try{
			PageTask task = this.submittedTaskQueue.poll(5, TimeUnit.MILLISECONDS);
			return task;
		}catch(InterruptedException ignore){
			return null;
		}
	}

	/*
	public Set<Map.Entry<Integer,String>> getErrorMapEntrySet(){
		return this.errorMap.entrySet();
	}*/

}
