package net.sqs2.omr.util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.list.TreeList;
import org.apache.commons.collections15.map.LinkedMap;

public abstract class RelationList <K,V> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static class SimpleRelationList<K,V> extends RelationList<K,V>{
		private static final long serialVersionUID = 1L;
		protected List<K> createKeyList(){
			return new LinkedList<K>();
		}
		protected List<V> createValueList(){
			return new LinkedList<V>();
		}
	}
	
	public static class SortedRelationList<K,V> extends RelationList<K,V>{
		private static final long serialVersionUID = 1L;
		protected List<K> createKeyList(){
			return new TreeList<K>();
		}
		protected List<V> createValueList(){
			return new TreeList<V>();
		}
	}
	
	
	private LinkedMap<K,List<V>> relationAtoB = new LinkedMap<K,List<V>>();
	private LinkedMap<V,List<K>> relationBtoA = new LinkedMap<V,List<K>>();

	public RelationList(){
	}
	
	protected abstract List<K> createKeyList();
	protected abstract List<V> createValueList();
	
	public boolean containsKey(K key){
		return relationAtoB.containsKey(key);
	}
	
	public List<V> getUniqueValueList(){
		return relationBtoA.asList();
	}
	
	public boolean containsValue(V v){
		return relationAtoB.containsValue(v);
	}
	
	public List<K> getValueListA(V b){
		List<K> aList = this.relationBtoA.get(b);
		if(aList == null){
			aList = new LinkedList<K>();
			this.relationBtoA.put(b, aList);
		}
		return aList;
	}

	public List<V> getValueListB(K a){
		List<V> bList = this.relationAtoB.get(a);
		if(bList == null){
			bList = new LinkedList<V>();
			this.relationAtoB.put(a, bList);
		}
		return bList;
	}

	public V getFirstValue(K a){
		List<V> list = getValueListB(a);
		if(0 < list.size()){
			return list.get(0);
		}else{
			return null;
		}
	}

	public void put(K a, V b){
		List<V> bList = getValueListB(a);
		bList.add(b);
		List<K> aList = getValueListA(b);
		aList.add(a);
	}

	public boolean remove(K a, V b){
		List<V> bList = getValueListB(a);
		boolean ret1 = bList.remove(b);
		List<K> aList = getValueListA(b);
		boolean ret2 = aList.remove(a);
		return ret1 & ret2;
	}

	public Set<K> getKeySetA(){
		return this.relationAtoB.keySet();
	}

	public Set<V> getKeySetB(){
		return this.relationBtoA.keySet();
	}

	public Set<Map.Entry<K, List<V>>> getEntrySetA(){
		return this.relationAtoB.entrySet();
	}

	public Set<Map.Entry<V, List<K>>> getEntrySetB(){
		return this.relationBtoA.entrySet();
	}
}
