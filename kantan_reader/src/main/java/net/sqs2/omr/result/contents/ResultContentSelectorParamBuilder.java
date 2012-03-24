package net.sqs2.omr.result.contents;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections15.set.ListOrderedSet;

public class ResultContentSelectorParamBuilder {
	private static final String DIGIT_ARRAY_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_~";
	private static final int DIGIT_ARRAY_UNIT_LEN = 6;	
	protected static final int NUM_MAX_ANSWER_ITEMS = 3;
	protected static final int[] EMPTY_INT_ARRAY = new int[0]; 

	public static ResultContentSelectorParam build(ResultContentSelectorParam param, HttpServletRequest req){
			
		param.masterIndex = parseInt(req.getParameter("m"));
		param.tableIndexSet = digitArrayValueDecode(req.getParameter("t"));
		param.rowIndexSet = digitArrayValueDecode(req.getParameter("r"));
		param.questionIndexSet = digitArrayValueDecode(req.getParameter("q"));
			
		return param;
	} 

	public static int getIntegerParameter(HttpServletRequest req, String src){
		return parseInt(req.getParameter(src));
	}

	protected static int parseInt(String src){
		if(src != null){
			try{
				return Integer.parseInt(src);
			}catch(NumberFormatException ignore){
			}
		}
		return -1;
	}

	protected static float parseFloat(String src){
		if(src != null){
			try{
				return Float.parseFloat(src);
			}catch(NumberFormatException ignore){
			}
		}
		return 0.0f;
	}

	protected static long parseLong(String src){
		if(src != null){
			try{
				return Long.parseLong(src);
			}catch(NumberFormatException ignore){
			}
		}
		return -1L;
	}

	protected static int[] parseInt(String[] values){
		if(values == null){
			return EMPTY_INT_ARRAY;
		}
			
		int[] ret = new int[values.length];
		
		for(int i = 0; i < values.length; i++){
			ret[i] = parseInt(values[i]);
		}
		Arrays.sort(ret);
		return ret;
	}

	protected static Set<Integer> arrayToIntegerSet(String[] array) {
		Set<Integer> ret = new TreeSet<Integer>(); 
		if(array == null){
			return ret;
		}
		for(String value: array){
			ret.add(Integer.parseInt(value));
		}
		return ret;
	}

	protected ListOrderedSet<String> arrayToStringSet(String[] array) {
		ListOrderedSet<String> ret = new ListOrderedSet<String>(); 
		if(array == null){
			return ret;
		}
		for(String value: array){
			ret.add(value);
		}
		return ret;
	}


	private static boolean isSelected(String encodedValue, int pos){
		return 0 != ((int)Math.pow(2, pos % DIGIT_ARRAY_UNIT_LEN) & DIGIT_ARRAY_CHARS.indexOf(encodedValue.charAt(pos / DIGIT_ARRAY_UNIT_LEN))); 
	}

	static TreeSet<Integer> digitArrayValueDecode(String encodedValue){
		if(encodedValue != null){
			return digitArrayValueDecode(encodedValue, encodedValue.length()*DIGIT_ARRAY_UNIT_LEN);
		}else{
			return new TreeSet<Integer>();
		}
	}

	static TreeSet<Integer> digitArrayValueDecode(String encodedValue, int len){
		TreeSet<Integer> set = new TreeSet<Integer>();
		for(int i = 0; i < len; i++){
			if(isSelected(encodedValue, i)){
				set.add(i);
			}
		}
		return set;
	}

}
