/*

 DensityHistgramAnswerItemEntryMap.java

 Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).

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
package net.sqs2.omr.model;

import java.util.Collection;

import org.apache.commons.collections15.multimap.MultiHashMap;

public class DensityHistgramAnswerItemEntryMap {
	int resolution;

	private MultiHashMap<Integer, MarkAreaAnswerItem> markAreaMap = new MultiHashMap<Integer, MarkAreaAnswerItem>();

	public DensityHistgramAnswerItemEntryMap(int resolution) {
		this.resolution = resolution;
	}

	public void clear() {
		this.markAreaMap.clear();
	}

	int size() {
		return this.markAreaMap.size();
	}

	public Collection<MarkAreaAnswerItem> get(int densityIndex) {
		return this.markAreaMap.getCollection(densityIndex);
	}

	public void add(MarkAreaAnswerItem item) {
		int densityIndex = (int) Math.ceil(item.getDensity() * this.resolution) - 1;
		this.markAreaMap.put(densityIndex, item);
	}

}
