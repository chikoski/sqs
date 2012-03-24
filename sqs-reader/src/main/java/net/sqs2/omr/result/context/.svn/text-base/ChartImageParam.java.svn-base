package net.sqs2.omr.result.context;

import java.util.Map;

public class ChartImageParam{

	int width, height;
	String type;

	public ChartImageParam(Map<String,String> map) {
		this.width = getValue((String)map.get("w"), 400);
		this.height = getValue((String)map.get("h"), 350);
		this.type = getValue((String)map.get("type"), "pie");
		this.height = getValue((String)map.get("h"), 350);
	}
	
	public static int getValue(String src, int defaultValue){
		try{
			if(src != null && ! "".equals(src)){
				return Integer.parseInt(src);				
			}
		}catch(NumberFormatException ignore){
		}
		return defaultValue;
	}

	public static String getValue(String src, String defaultValue){
		if(src != null && ! "".equals(src)){
			return src;				
		}
		return defaultValue;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public String getType() {
		return this.type;
	}
}
