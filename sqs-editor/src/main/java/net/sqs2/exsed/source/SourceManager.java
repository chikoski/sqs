/*

 EditingState.java
 
 Copyright 2004 KUBO Hiroya (hiroya@sfc.keio.ac.jp).
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
 Created on 2004/07/31

 */
package net.sqs2.exsed.source;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author hiroya
 * 
 */
public abstract class SourceManager {
	LinkedList<Source> sourceList = new LinkedList<Source>();
	SourceFactory factory;

	public SourceManager() {
		this.factory = createSourceFactory();
	}

	public String toString() {
		return sourceList.toString();
	}

	public abstract SourceFactory createSourceFactory();

	public Source createSource(File file) throws SourceException {
		Source source = factory.createSource(file);
		sourceList.add(source);
		return source;
	}

	public Source createSource(URL url, boolean readonly, String title) throws SourceException {
		Source source = factory.createSource(url, readonly, title);
		sourceList.add(source);
		return source;
	}

	public Source createSource() throws SourceException {
		Source source = factory.createSource();
		sourceList.add(source);
		return source;
	}

	public void close(int index) {
		sourceList.remove(index);
	}

	public Source get(int index) {
		if (index != -1 && index < sourceList.size()) {
			return (Source) sourceList.get(index);
		} else {
			return null;
		}
	}

	public int getIndexOfFile(File saveAsFile) {
		int i = 0;
		for (Iterator<Source> it = sourceList.iterator(); it.hasNext(); i++) {
			Source source = (Source) it.next();
			if (source.getFile() != null
					&& source.getFile().getAbsoluteFile().equals(saveAsFile.getAbsoluteFile())) {
				return i;
			}
		}
		return -1;
	}

	public int size() {
		return sourceList.size();
	}

	public int countDirtySources() {
		int count = 0;
		for (int i = 0; i < sourceList.size(); i++) {
			Source source = (Source) sourceList.get(i);
			if (source.isDirty()) {
				count++;
			}
		}
		return count;
	}

}
