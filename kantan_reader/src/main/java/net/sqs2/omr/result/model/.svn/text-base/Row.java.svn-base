/**
 *  Row.java

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

 Created on 2007/03/13
 Author hiroya
 */
package net.sqs2.omr.result.model;

import java.io.Serializable;

import net.sqs2.omr.source.PageID;
import net.sqs2.omr.task.TaskError;

import org.apache.commons.collections15.multimap.MultiHashMap;

public class Row extends AbstractRow implements Serializable {
	private static final long serialVersionUID = 4;

	Answer[] answer;
	MultiHashMap<PageID,TaskError> taskErrorMap;

	public Row() {
	}

	public Row(String masterPath, String sourceDirectoryPath, int rowIndex, int numColumns) {
		super(masterPath, sourceDirectoryPath, rowIndex);
		this.answer = new Answer[numColumns];
	}

	public void setAnswer(int columnIndex, Answer answer) {
		this.answer[columnIndex] = answer;
	}

	public Answer getAnswer(int columnIndex) {
		return this.answer[columnIndex];
	}
	
	public void addError(TaskError taskError){
		if(this.taskErrorMap == null){
			this.taskErrorMap = new MultiHashMap<PageID,TaskError>();
		}
		if(! this.taskErrorMap.containsKey(taskError)){
			this.taskErrorMap.put(taskError.getSource(), taskError);
		}
	}
	
	public MultiHashMap<PageID,TaskError> getTaskErrorMultiHashMap(){
		return this.taskErrorMap;
	}
	
	public void clearTaskError(){
		this.taskErrorMap = null;
	}
}