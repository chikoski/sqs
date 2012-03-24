/*
 * 

 PersistentCacheAccessor.java

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
 */
package net.sqs2.cache;

import java.io.File;

import java.io.IOException;
import java.io.Serializable;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;

public class PersistentCacheAccessor {

	private Ehcache cache;
	PersistentCacheManager manager;
	
	protected PersistentCacheAccessor(File dir, String name, String dirname) throws IOException {
		this.manager = PersistentCacheManager.getInstance(dir, dirname);
		this.cache = this.manager.getECache(name);
		if(this.cache.getStatus() == Status.STATUS_SHUTDOWN){
			this.cache.bootstrap();
		}
	}

	public void flush() {
		if(this.cache.getStatus() == Status.STATUS_ALIVE){
			this.cache.flush();
		}
	}
	
	
	public void remove(String key) {
		this.cache.remove(key);
	}

	public void removeAll() {
		this.cache.removeAll();
	}

	protected Ehcache getEhcache() {
		return this.cache;
	}

	public Serializable get(Serializable key) {
		if(getEhcache().getStatus() == Status.STATUS_ALIVE){
			try{
				Element element = getEhcache().get(key);
				if (element == null) {
					return null;
				} else {
					return element.getValue();
				}
			}catch(Error ignore){
				return null;
			}
		}
		return null;
	}

	public void dispose() {
		if(getEhcache().getStatus() == Status.STATUS_ALIVE){
			this.cache.flush();
		}
		this.manager.close();
	}

}
