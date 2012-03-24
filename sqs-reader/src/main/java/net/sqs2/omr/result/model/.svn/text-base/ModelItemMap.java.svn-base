package net.sqs2.omr.result.model;

import java.util.HashMap;

import net.sqs2.omr.util.JSONUtil;

public class ModelItemMap extends HashMap<String,ModelItem> implements ModelItem{

	public static class Value implements ModelItem {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private String value;
		public Value(String value){
			this.value = value;
		}
		
		public static Value getValue(float value){
			return new Value(Float.toString(value));
		}
		
		public static Value getValue(int value){
			return new Value(Integer.toString(value));
		}
		
		public static Value getValue(boolean value){
			return new Value(Boolean.toString(value));
		}
		
		public String getValue(){
			return this.value;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void put(String key, String value){
		super.put(key, new Value(value));
	}

	public void put(String key, int value){
		super.put(key, Value.getValue(value));
	}

	public void put(String key, float value){
		super.put(key, Value.getValue(value));
	}

	public void put(String key, boolean value){
		super.put(key, Value.getValue(value));
	}
	
	public void putNull(String key){
		super.put(key, null);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		JSONUtil.printAsJSON(sb, this);
		return sb.toString();
	}
}
