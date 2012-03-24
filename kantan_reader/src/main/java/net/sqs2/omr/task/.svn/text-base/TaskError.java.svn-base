/*

 PageTaskError.java

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

 Created on 2007/04/07

 */
package net.sqs2.omr.task;

import java.io.Serializable;

import net.sqs2.omr.source.PageID;


public class TaskError implements Serializable, Comparable<TaskError>{
	private static final long serialVersionUID = 3L;
	String message;
	PageID source;
	TaskExceptionCore exceptionCore;

	public TaskError(PageID source, String message){
		this.source = source;
		this.message = message;
	}

	public TaskError(PageID source, TaskExceptionCore exceptionCore){
		this.source = source;
		this.exceptionCore = exceptionCore;
	}

	public PageID getSource(){
		return this.source;
	}

	public String getLocalizedMessage(){
		return this.exceptionCore.getLocalizedMessage();
	}

	public TaskExceptionCore getExceptionCore(){
		return this.exceptionCore;
	}

	@Override
	public String toString(){
		return getClass().getName()+"@"+hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		try{
			return getSource().equals(((TaskError)o).getSource());
		}catch(Exception ex){
			return false;
		}
	}

	@Override
	public int hashCode(){
		return source.hashCode();
	}

	public int compareTo(TaskError o) {
		return getSource().getFileResourceID().getRelativePath().compareTo(o.getSource().getFileResourceID().getRelativePath());
	}
	
}
