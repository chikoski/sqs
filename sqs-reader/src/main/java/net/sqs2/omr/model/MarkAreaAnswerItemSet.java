/**
 * MarkAreaAnswerItemSet.java

 Copyright 2009 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Author hiroya
 */

package net.sqs2.omr.model;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

public class MarkAreaAnswerItemSet extends TreeSet<MarkAreaAnswerItem> {
	private static final long serialVersionUID = 0L;
	
	boolean isMultiple;
	
	public MarkAreaAnswerItemSet(MarkAreaAnswer answer) {
		super(new Comparator<MarkAreaAnswerItem>() {
			public int compare(MarkAreaAnswerItem b, MarkAreaAnswerItem a) {
				double value = Math.ceil(a.getDensity() - b.getDensity());
				if (0 == value) {
					return a.getItemIndex() - b.getItemIndex();
				} else if (0 < value) {
					return (int) Math.ceil(value);
				} else {
					return (int) Math.floor(value);
				}
			}

			@Override
			public boolean equals(Object obj) {
				return false;
			}
		});

		for (MarkAreaAnswerItem markAreaAnswerItem : answer.getMarkAreaAnswerItemArray()) {
			add(markAreaAnswerItem);
		}
		
		this.isMultiple = answer.isMultiple;
	}
	
	public boolean isSelected(float densityThreshold, float doubleMarkErrorSuppressThreshold, float noMarkErrorSuppressThreshold, MarkAreaAnswerItem targetItem){
		if(this.isMultiple){
			return targetItem.getDensity() < densityThreshold;
		}else{
			return isSelectedSelect1Item(densityThreshold, doubleMarkErrorSuppressThreshold, noMarkErrorSuppressThreshold, targetItem);
		}
	}
	
	public boolean isSelectedSelect1Item(float densityThreshold, float doubleMarkIgnoranceThreshold, float noMarkRecoveryThreshold, MarkAreaAnswerItem targetItem) {
		if(targetItem.isManualMode()){
			return (targetItem.isManualSelected()); // manual Selected
		}
		if(targetItem.getDensity() < densityThreshold){
			// may be marked marked
			if( higher(targetItem) == null){
				// the most dense item
				return true; // recognize normally
			}else{
				// the second or later dense item
				if(doubleMarkIgnoranceThreshold < targetItem.getDensity()){
					return false;
				}else{
					return true;
				}
			}
		}else{
			// may not be marked
			if(higher(targetItem) == null){
				// difference of density between the 1st item and 2nd item is larger than its threshold, recover the 1st as marked.
				if(noMarkRecoveryThreshold <= lower(targetItem).getDensity() - targetItem.getDensity()){
					return true;
				}else{
					return false;
				}
			}
			return false;
		}
	}
	
	public static boolean isSelectedSelectItem(float densityThreshold, MarkAreaAnswerItem targetItem){
		if(targetItem.isManualMode()){
			return (targetItem.isManualSelected()); // manual Selected
		}
		return (targetItem.getDensity() < densityThreshold); // recognition normally 
	}
	
	public LinkedList<MarkAreaAnswerItem> getMarkedAnswerItems(float densityThreshold, float doubleMarkSuppressThreshold,
			float noMarkSuppressThreshold) {
		LinkedList<MarkAreaAnswerItem> list = new LinkedList<MarkAreaAnswerItem>();
		for (Iterator<MarkAreaAnswerItem> it = iterator(); it.hasNext();) {
			MarkAreaAnswerItem item = it.next();
			if (isSelected(densityThreshold, doubleMarkSuppressThreshold,  noMarkSuppressThreshold, item)) {
				list.add(item);
			}
		}
		return list;
	}

	public boolean[] getIsSelectedBooleanArray(float densityThreshold, float doubleMarkIgnoranceThreshold, float noMarkRecoveryThreshold) {
		boolean[] ret = new boolean[this.size()];
		for (Iterator<MarkAreaAnswerItem> it = iterator(); it.hasNext();) {
			MarkAreaAnswerItem item = it.next();
			if (isSelected(densityThreshold, doubleMarkIgnoranceThreshold, noMarkRecoveryThreshold, item)) {
				ret[item.getItemIndex()] = true;
			}
		}
		return ret;
	}

	public int getNumSelected(float densityThreshold, float doubleMarkIgnoranceThreshold, float  noMarkRecoveryThreshold) {
		int ret = 0;
		for (Iterator<MarkAreaAnswerItem> it = iterator(); it.hasNext();) {
			MarkAreaAnswerItem item = it.next();
			if (isSelected(densityThreshold, doubleMarkIgnoranceThreshold, noMarkRecoveryThreshold, item)) {
				ret++;
			}
		}
		return ret;
	}
}
