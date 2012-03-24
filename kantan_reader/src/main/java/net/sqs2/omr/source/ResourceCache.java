/**
 * 
 */
package net.sqs2.omr.source;

import java.util.HashMap;
import java.util.Map;

import net.sqs2.util.FileResourceID;

import org.apache.commons.collections15.map.LRUMap;

public class ResourceCache<T>{
	protected Map<FileResourceID,T> map;
	
	public ResourceCache(){
		 this.map = new HashMap<FileResourceID,T>();
	}
	
	public ResourceCache(int cacheSize){
		 this.map = new LRUMap<FileResourceID,T>(cacheSize);
	}
	
	public void regist(FileResourceID fileResourceID, T element){
		this.map.put(fileResourceID, element);
	}
	
	public T lookup(FileResourceID fileResourceID){
		return this.map.get(fileResourceID);
	}
}