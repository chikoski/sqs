/**
 * 
 */
package net.sqs2.omr.util;

import java.util.HashMap;
import java.util.Map;

import net.sqs2.util.FileResourceID;

import org.apache.commons.collections15.map.LRUMap;

public class FileResourceCache<T> {
	protected Map<FileResourceID, T> map;

	public FileResourceCache() {
		this.map = new HashMap<FileResourceID, T>();
	}

	public FileResourceCache(int cacheSize) {
		this.map = new LRUMap<FileResourceID, T>(cacheSize);
	}

	public void regist(FileResourceID fileResourceID, T element) {
		this.map.put(fileResourceID, element);
	}

	public T lookup(FileResourceID fileResourceID) {
		return this.map.get(fileResourceID);
	}
	
	public void clear(){
		map.clear();
	}
}
