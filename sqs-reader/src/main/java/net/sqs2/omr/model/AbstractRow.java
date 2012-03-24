/**
 *  AbstractRow.java

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

 Created on 2007/01/31
 Author hiroya
 */

package net.sqs2.omr.model;

import java.io.Serializable;

public class AbstractRow implements Serializable {
	private static final long serialVersionUID = 2;

	protected String id;

	public AbstractRow() {
	}

	public AbstractRow(String masterPath, String sourceDirectoryPath, int rowIndex) {
		this.id = createID(masterPath, sourceDirectoryPath, rowIndex);
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Row) {
			Row row = (Row) o;
			if (this.hashCode() != row.hashCode()) {
				return false;
			}
			return this.id.equals(row.id);
		} else {
			return false;
		}
	}

	public String getID() {
		return this.id;
	}

	public static String createID(String masterPath, String sourceDirectoryPath, int rowIndex) {
		return masterPath + "\t" + sourceDirectoryPath + "\t" + rowIndex;
	}

	@Override
	public String toString() {
		return getID();
	}

}
