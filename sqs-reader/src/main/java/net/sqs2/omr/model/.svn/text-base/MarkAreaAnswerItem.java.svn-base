/**
 * MarkAreaAnswerItem.java

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

public class MarkAreaAnswerItem implements Serializable {// Comparable<MarkAreaAnswerItem>,
	private static final long serialVersionUID = 5L;
	private int itemIndex;
	private float density;
	private Boolean isManualSelected;

	public MarkAreaAnswerItem() {
	}

	public MarkAreaAnswerItem(int itemIndex, float density) {
		this.itemIndex = itemIndex;
		this.density = density;
		this.isManualSelected = null;
	}

	public boolean isSelectMultiSelected(MarkAreaAnswer answer, float densityThreshold) {
		return (isManualMode() && isManualSelected()) || (!isManualMode() && getDensity() < densityThreshold);
	}

	public boolean isSelectOneSelected(MarkAreaAnswer answer, float densityThreshold, int numMarkedAnswerItems) {
		return (isManualMode() && isManualSelected())
				|| (!isManualMode() && numMarkedAnswerItems == 1 && getDensity() < densityThreshold);
	}

	/*
	 * @Override public int hashCode(){ return itemIndex; }
	 * 
	 * @Override public boolean equals(Object o){ return this.itemIndex ==
	 * ((MarkAreaAnswerItem)o).itemIndex; }
	 */

	/**
	 * Set value by user's decision filled mark: Boolean.TRUE not filled mark:
	 * Boolean.FALSE recognize by density: null
	 * 
	 * @param isManualSelected
	 */
	public void setManualSelected(Boolean isManualSelected) {
		this.isManualSelected = isManualSelected;
	}

	public boolean isManualMode() {
		return this.isManualSelected != null;
	}

	public boolean isManualSelected() {
		return this.isManualSelected != null && this.isManualSelected.booleanValue();
	}

	/*
	 * public int compareTo(MarkAreaAnswerItem item) { float c = this.density -
	 * item.density; if (c < 0.0f) { return -1; } else if (c > 0.0f) { return 1;
	 * } else { return 0; } }
	 */

	public int getItemIndex() {
		return this.itemIndex;
	}

	public float getDensity() {
		return this.density;
	}

}
