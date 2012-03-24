package net.sqs2.omr.swing.pagetask;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import net.sqs2.omr.model.PageTask;
import net.sqs2.omr.model.PageTaskHolder;

public class TaskQueueModel extends Observable{
	
	PageTaskHolder holder;
	Map<Integer,PageTask> pageTaskErrorMap;
	String statusMessage;
	
	TaskQueueModel(PageTaskHolder holder){
		this.holder = holder;
		this.pageTaskErrorMap = new HashMap<Integer,PageTask>();
	}
	
	public int getNumTotalTasks(){
		return this.holder.getNumTotalTasks();
	}
	
	void clear() {
		this.statusMessage = "";
		this.setChanged();
		this.notifyObservers();
	}
}
