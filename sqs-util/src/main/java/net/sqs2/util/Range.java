/**
 *  Range.java

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

 Created on 2007/02/21
 Author hiroya
 */
package net.sqs2.util;

import java.io.Serializable;

public class Range implements Serializable {
	private static final long serialVersionUID = 0L;
	int min = 0;
	int max = 0;

	public Range(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public String toString() {
		return "min=" + this.min + ",max=" + this.max;
	}

	public int getMin() {
		return this.min;
	}

	public int getMax() {
		return this.max;
	}

	public int updateMax(int candidate) {
		return (this.max = (candidate > this.max) ? candidate : this.max);
	}

	public int updateMin(int candidate) {
		return (this.min = (candidate < this.min) ? candidate : this.min);
	}

	public void update(int candidate) {
		updateMax(candidate);
		updateMin(candidate);
	}
}
