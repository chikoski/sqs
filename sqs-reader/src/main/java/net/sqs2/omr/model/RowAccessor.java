/**
 *  RowAccessor.java

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

import java.io.File;
import java.io.IOException;

import net.sf.ehcache.Element;
import net.sqs2.cache.PersistentCacheAccessor;

public class RowAccessor extends PersistentCacheAccessor {

	public static RowAccessor createInstance(File sourceDirectoryRoot) throws IOException {
		return new RowAccessor(sourceDirectoryRoot); 
	}
	
	private RowAccessor(File sourceDirectoryRoot) throws IOException {
		super(sourceDirectoryRoot, "row", CacheConstants.getCacheDirname());
	}

	public Row get(String masterPath, String sourceDirectoryPath, int rowIndex) {
		try {
			return (Row) super.get(Row.createID(masterPath, sourceDirectoryPath, rowIndex));
		} catch (Exception ex) {
			return null;
		}
	}

	public void put(Row row) {
		getEhcache().put(new Element(row.getID(), row));
	}

}
