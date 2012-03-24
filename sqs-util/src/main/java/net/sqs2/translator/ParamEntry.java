/**
 * 
 */
package net.sqs2.translator;

public class ParamEntry {
	String key;
	String value;

	public ParamEntry(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return this.key;
	}

	public String getValue() {
		return this.value;
	}
}