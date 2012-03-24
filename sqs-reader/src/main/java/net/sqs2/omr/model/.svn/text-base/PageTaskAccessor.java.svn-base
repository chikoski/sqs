/*

 PageTaskAccessor.java

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
package net.sqs2.omr.model;

import java.io.File;
import java.io.IOException;

import net.sf.ehcache.Element;
import net.sqs2.cache.PersistentCacheAccessor;


public class PageTaskAccessor extends PersistentCacheAccessor {

	public static PageTaskAccessor createInstance(File sourceDirectoryRoot) throws IOException {
		return new PageTaskAccessor(sourceDirectoryRoot); 
	}
	
	protected PageTaskAccessor(File sourceDirectoryRoot) throws IOException {
		super(sourceDirectoryRoot, "pageTask", CacheConstants.getCacheDirname());
	}

	public OMRPageTask get(int pageNumber, PageID pageID) {
		return get(OMRPageTask.createID(pageID, pageNumber));
	}

	public OMRPageTask get(String key) {
		return (OMRPageTask) super.get(key);
	}

	public void put(OMRPageTask pageTask) {
		try{
			getEhcache().put(new Element(pageTask.toString(), pageTask));
		}catch(ClassCastException ex){
			getEhcache().remove(pageTask.getID());
			getEhcache().put(new Element(pageTask.toString(), pageTask));
		}
	}

}
