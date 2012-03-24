/*

 PageMasterAccessor.java

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

 Created on 2007/01/11

 */
package net.sqs2.omr.model;

import java.io.File;
import java.io.IOException;

import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import net.sqs2.cache.PersistentCacheAccessor;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.util.FileResourceID;

public class FormMasterAccessor extends PersistentCacheAccessor {

	public static FormMasterAccessor createInstance(File sourceDirectoryRoot)throws IOException{
		return new FormMasterAccessor(sourceDirectoryRoot);
	}

	protected FormMasterAccessor(File sourceDirectoryRoot) throws IOException {
		super(sourceDirectoryRoot, "master", CacheConstants.getCacheDirname());
	}

	public FormMaster get(String key) {
		return (FormMaster) super.get(key);
	}

	public FormMaster get(FileResourceID fileResourceID) {
		return (FormMaster) super.get(FormMaster.createKey(fileResourceID));
	}

	public void put(FormMaster master) {
		if(getEhcache().getStatus() != Status.STATUS_ALIVE){
			throw new RuntimeException("The master Cache is not alive: PageMaster:"+master+":"+getEhcache().getStatus());
		}
		getEhcache().put(new Element(master.getKey(), master));
	}
}
