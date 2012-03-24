/**
 *  MarkAreaAnswer.java

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
package net.sqs2.omr.model;

import java.io.Serializable;

public class MarkAreaAnswer extends Answer implements Serializable {
	private static final long serialVersionUID = 6L;

	boolean isMultiple; 

	protected MarkAreaAnswerItem[] markAreaAnswerItemArray = null;
	
	boolean isManualMode = false;

	public MarkAreaAnswer() {
	}

	public MarkAreaAnswer(boolean isMultiple, int size) {
		this.isMultiple = isMultiple;
		this.markAreaAnswerItemArray = new MarkAreaAnswerItem[size];
	}

	/**
	 * @return true when value is override by user's decision, ulness return
	 *         false;
	 */
	public boolean isManualMode() {
		return this.isManualMode;
	}
	
	public boolean isMultiple(){
		return this.isMultiple;
	}

	public void setManualMode(boolean isManualMode) {
		this.isManualMode = isManualMode;
	}

	@Override
	public int size() {
		return this.markAreaAnswerItemArray.length;
	}

	public void setMarkAreaAnswerItem(MarkAreaAnswerItem answerItem) {
		this.markAreaAnswerItemArray[answerItem.getItemIndex()] = answerItem;
	}

	public MarkAreaAnswerItem getMarkAreaAnswerItem(int itemIndex) {
		return this.markAreaAnswerItemArray[itemIndex];
	}

	public MarkAreaAnswerItem[] getMarkAreaAnswerItemArray() {
		return this.markAreaAnswerItemArray;
	}

	public MarkAreaAnswerItemSet createMarkAreaAnswerItemSet() {
		return new MarkAreaAnswerItemSet(this);
	}

	@Override
	public String toString() {
		return "MarkAreaAnswerItems:" + this.markAreaAnswerItemArray.toString();
	}
}
