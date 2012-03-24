/**
 * 
 */
package net.sqs2.omr.result.builder;

import org.apache.commons.collections15.Bag;
import org.apache.commons.collections15.bag.HashBag;

public class StatisticsResult<K>{
	protected Bag<K> values;

	public StatisticsResult() {
		this.values = new HashBag<K>();
	}

	public int getCount(K key) {
		return this.values.getCount(key);
	}

	public void add(K key) {
		this.values.add(key);
	}
}