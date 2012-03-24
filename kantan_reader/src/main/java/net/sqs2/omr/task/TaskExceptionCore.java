/*

 TaskExceptionCore.java

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

 Created on Apr 7, 2007

 */
package net.sqs2.omr.task;

import java.io.Serializable;


public abstract class TaskExceptionCore implements Serializable,Comparable<TaskExceptionCore>{

	protected String pageID;
	protected int width;
	protected int height;

	public TaskExceptionCore(String pageID, int width, int height){
		this.pageID = pageID;
		this.width = width;
		this.height = height;
	}

	public int getWidth(){
		return this.width;
	}

	public int getHeight(){
		return this.height;
	}

	public abstract String getLocalizedMessage(); 
	public abstract String getDescription(); 

	@Override
	public int hashCode(){
		return pageID.hashCode(); 
	}

	@Override
	public boolean equals(Object o){
		try{
			return pageID.equals(((TaskExceptionCore)o).pageID);
		}catch(Exception ex){
			return false;
		}
	}
	
	public int compareTo(TaskExceptionCore c){
		try{
			return pageID.compareTo(((TaskExceptionCore)c).pageID);
		}catch(Exception ex){
			return -1;
		}
	}
}
