package net.sqs2.omr.result.model;

import java.util.ArrayList;

import net.sqs2.omr.util.JSONUtil;

public class ModelItemList<I extends ModelItem> extends ArrayList<ModelItem> implements ModelItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ModelItemList(){
		super();
	}

	public ModelItemList(int size){
		super(size);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		JSONUtil.printAsJSON(sb, this);
		return sb.toString();
	}
}
