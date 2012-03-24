/*

 GenericBrowserParam.java

 Copyright 2008 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2008/01/13

 */
package net.sqs2.omr.result.contents;

import java.util.TreeSet;

public class ResultContentSelectorParam{

	protected int masterIndex = -1;
	protected TreeSet<Integer> tableIndexSet = null;
	protected TreeSet<Integer> rowIndexSet = null;
	protected TreeSet<Integer> questionIndexSet = null;
	
	public ResultContentSelectorParam(){
	}
	
	public ResultContentSelectorParam(int masterIndex, int tableIndex){
		this.masterIndex = masterIndex;
		this.tableIndexSet = new TreeSet<Integer>();
		this.tableIndexSet.add(tableIndex);
	}
	
	public int getSelectedMasterIndex(){
		return this.masterIndex;
	}
	
	public TreeSet<Integer>getSelectedTableIndexSet(){
		return this.tableIndexSet;
	}
	
	public TreeSet<Integer> getSelectedRowIndexSet(){
		return this.rowIndexSet;
	}
		
	public TreeSet<Integer> getSelectedQuestionIndexSet(){
		return this.questionIndexSet;
	}
	
	public boolean isSelectedRowIndex(String rowIndex){
		return this.rowIndexSet.contains(rowIndex);
	}

	public boolean isSelectedQuestionIndex(int questionIndex){
		return this.questionIndexSet.contains(questionIndex);
	}
	
}