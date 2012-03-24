/**
 * 
 */
package net.sqs2.omr.swing;

import net.sqs2.omr.task.TaskError;

public class PageTaskErrorEntry{
	int index;
	TaskError taskError;
	PageTaskErrorEntry(int index, TaskError taskError){
		this.index = index; 
		this.taskError = taskError;
	}
	public int getIndex() {
		return index;
	}
	public TaskError getTaskError() {
		return taskError;
	}
}