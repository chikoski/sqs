/*

 PageTaskResult.java

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

 Created on 2007/01/11

 */
package net.sqs2.omr.task;

import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class TaskResult implements Serializable{
	public static final long serialVersionUID = 1L;

	protected Point[] corners;
	protected List<PageAreaCommand> pageAreaCommandList = new LinkedList<PageAreaCommand>();

	public TaskResult(){
	}

	public TaskResult(Point[] corners){
		this.corners = corners;
	}

	public String toString(){
		return "corners=" + this.corners[0]+"," + this.corners[3]+" cmd=" + this.pageAreaCommandList;
	}

	public Point[] getCorners(){
		return this.corners;
	}

	public List<PageAreaCommand> getPageAreaCommandList(){
		return this.pageAreaCommandList;
	}

	public void addFormAreaCommand(PageAreaCommand pageAreaCommand){
		this.pageAreaCommandList.add(pageAreaCommand);
	}

}
