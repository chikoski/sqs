package net.sqs2.omr.util;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections15.map.LinkedMap;

public class RelationSet <A,B> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private LinkedMap<A,Set<B>> relationAtoB = new LinkedMap<A,Set<B>>();
	private LinkedMap<B,Set<A>> relationBtoA = new LinkedMap<B,Set<A>>();

	public RelationSet(){
	}
	
	public Set<A> getValueSetA(B b){
		Set<A> aSet = relationBtoA.get(b);
		if(aSet == null){
			aSet = new TreeSet<A>();
		}
		return aSet;
	}

	public Set<B> getValueSetB(A a){
		Set<B> bSet = relationAtoB.get(a);
		if(bSet == null){
			bSet = new TreeSet<B>();
		}
		return bSet;
	}

	public void put(A a, B b){
		Set<B> bSet = getValueSetB(a);
		bSet.add(b);
		Set<A> aSet = getValueSetA(b);
		aSet.add(a);
	}

	public boolean remove(A a, B b){
		Set<B> bList = getValueSetB(a);
		boolean ret1 = bList.remove(b);
		Set<A> aList = getValueSetA(b);
		boolean ret2 = aList.remove(a);
		return ret1 & ret2;
	}

	public Set<A> getKeySetA(){
		return relationAtoB.keySet();
	}

	public Set<B> getKeySetB(){
		return relationBtoA.keySet();
	}

	public Set<Map.Entry<A, Set<B>>> getEntrySetA(){
		return relationAtoB.entrySet();
	}

	public Set<Map.Entry<B, Set<A>>> getEntrySetB(){
		return relationBtoA.entrySet();
	}
	
}
