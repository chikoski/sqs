package net.sqs2.omr.util;

import java.util.ArrayList;
import java.util.TreeSet;

public class URLSafeRLEBase64 {

	/**
	 *  usable characters for base64value
	 */
	private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-";
	
	/**
	 *  special character for padding
	 */
	private static final char PADDING_CHAR = '~';
	
	/**
	 *  selection super class
	 */
	public abstract static class Selection{
		
	}
	
	/**
	 * Single selection implement class
	 */
	public static class SingleSelection extends Selection{
		long value;
		
		public SingleSelection(long value){
			this.value = value;
		}
		
		public long getValue(){
			return value;
		}
	}

	/**
	 * Multi selection implement class
	 */
	public static class MultiSelection extends Selection{
		ArrayList<Boolean> selectionBooleanArray;
		TreeSet<Integer> selectedIndexTreeSet;
		
		public MultiSelection(ArrayList<Boolean> selectionBooleanArray, 
				TreeSet<Integer> selectedIndexTreeSet){
			this.selectionBooleanArray = selectionBooleanArray;
			this.selectedIndexTreeSet = selectedIndexTreeSet;
		}
		
		public ArrayList<Boolean> getSelectionBooleanArray() {
			return selectionBooleanArray;
		}
		
		public TreeSet<Integer> getSelectedIndexTreeSet() {
			return selectedIndexTreeSet;
		}

	}

	public static String decodeToString(String srcString){
		StringBuilder ret = new StringBuilder();
		String prevWord = null;
		int runLength = 0;
	
		for(int index = 0; index < srcString.length(); index++){
			char c = srcString.charAt(index);
			int value = CHARS.indexOf(c);
			if((value & 1 << 5) != 0){
				runLength = (value - 32) + (runLength << 5);
				if(index == srcString.length() - 1){ // end of char
					for(int i = 1; i < runLength; i++){
						ret.append(prevWord);
					}
				}
			}else{
				for(int i = 1; i < runLength; i++){
					ret.append(prevWord);
				}
				int paddingLength;
				if(index + 1 < srcString.length() && srcString.charAt(index+1) == PADDING_CHAR){
					paddingLength = srcString.length() - index - 1;
				}else{
					paddingLength = 0;
				}
				StringBuilder word = new StringBuilder(5);
				for(int i = 0; i < 5 - paddingLength; i++){
					if((value & 1<<i) != 0){
						word.append('1');
					}else{
						word.append('0');
					}
				}
				String w = word.toString();
				ret.append(w);
				prevWord = w;
				runLength = 0;
			}
		}
		return ret.toString();
	}		

	/**
	 * decode src
	 * @param srcString
	 * @return
	 */
	public static Selection decodeToSelection(String srcString){
		ArrayList<Boolean> selectionBooleanArray = new ArrayList<Boolean>();
		TreeSet<Integer> selectedIntegerTreeSet = new TreeSet<Integer>(); 
		
		ArrayList<Boolean> prevWord = new ArrayList<Boolean>(5);
		
		int wordIndex = 0;
		int runLength = 0;
		
		if(srcString != null){
			for(int index = 0; index < srcString.length(); index++){
				int value = CHARS.indexOf(srcString.charAt(index));
				if((value & 1 << 5) != 0){
					runLength = (value - 32) + (runLength << 5);
					if(index == srcString.length() - 1){
						if(prevWord.size() == 0){
							int indexValue = runLength;
							return new SingleSelection(indexValue); 
						}else{
							for(int i = 1; i < runLength; i++){
								selectionBooleanArray.addAll(prevWord);
							}
						}
					}
					wordIndex += runLength;
				}else{
					for(int i = 1; i < runLength; i++){
						selectionBooleanArray.addAll(prevWord);
					}
					int paddingLength;
					if(index + 1 < srcString.length() && srcString.charAt(index+1) == PADDING_CHAR){
						paddingLength = srcString.length() - index - 1;
					}else{
						paddingLength = 0;
					}
					ArrayList<Boolean> word = new ArrayList<Boolean>(5);
					for(int i = 0; i < 5 - paddingLength; i++){
						if((value & 1<<i) != 0){
							word.add(Boolean.TRUE);
							selectedIntegerTreeSet.add(i+wordIndex*5);
						}else{
							word.add(Boolean.FALSE);
						}
					}
					selectionBooleanArray.addAll(word);
					prevWord = word;
					runLength = 0;
				}
			}
		}
	
		for(int i=0; i<selectionBooleanArray.size();i++){
			boolean a = selectionBooleanArray.get(i);
			boolean b = selectedIntegerTreeSet.contains(i);
			if((a && b)||(!a && !b)){
				continue;
			}else{
				throw new IllegalArgumentException(srcString);
			}
		}
		return new MultiSelection(selectionBooleanArray, selectedIntegerTreeSet);
	}

	public static boolean[] decode(String srcString){
		ArrayList<Boolean> ret = new ArrayList<Boolean>();
		ArrayList<Boolean> prevWord = new ArrayList<Boolean>();
		int runLength = 0;

		if(srcString != null){
			for(int index = 0; index < srcString.length(); index++){
				int value = CHARS.indexOf(srcString.charAt(index));
				if((value & 1 << 5) != 0){
					runLength = (value - 32) + (runLength << 5);
					if(index == srcString.length() - 1){
						for(int i = 1; i < runLength; i++){
							ret.addAll(prevWord);
						}
					}
				}else{
					for(int i = 1; i < runLength; i++){
						ret.addAll(prevWord);
					}
					ArrayList<Boolean> word = new ArrayList<Boolean>(5);
					for(int i = 0; i < 5; i++){
						if((value & 1<<i) != 0){
							word.add(Boolean.TRUE);
						}else{
							word.add(Boolean.FALSE);
						}
					}
					ret.addAll(word);
					prevWord = word;
					runLength = 0;
				}
			}
		}
		boolean[] returnValue = new boolean[ret.size()];
		for(int i=0; i<ret.size();i++){
			returnValue[i] = ret.get(i);
		}
	
		return returnValue;
	}

	/**
	 * @param srcString source string
	 * @return decoded value holder object
	 */
	public static MultiSelection decodeToMultiSelection(String srcString){
		return (MultiSelection) decodeToSelection(srcString);
	}

	/**
	 * @param srcString source string
	 * @return decoded value
	 */
	public static long decodeToLong(String srcString){
		long total = 0L;
		if(srcString != null){
			for(int index = 0; index < srcString.length(); index++){
				int value = CHARS.indexOf(srcString.charAt(index));
				if((value & 1 << 5) != 0){
					total = (value - 32) + (total << 5);
				}else{
					throw new IllegalArgumentException(srcString);
				}
			}
			return total; 
		}
		throw new IllegalArgumentException(srcString);
	}
	
	/**
	 * @param srcString source string
	 * @return decoded value
	 */
	public static int decodeToInt(String srcString){
		return (int)URLSafeRLEBase64.decodeToLong(srcString);
	}
	
	public static String encode(String bitData){
		int length = bitData.length();
		StringBuilder ret = new StringBuilder();
		int prevValue = -1;
		
		int runLength = 1;
		
		for(int index = 0; index < length; index += 5){
			int value = (bitData.charAt(index)=='1'?1:0);
			//value += (bits[index]==1?1:0);
			int padding = 0;
			if(index + 1 <length){
				value += (bitData.charAt(index+1)=='1'?2:0);
				//value += (bits[index+1]==1?2:0);
				if(index + 2 < length){
					value += (bitData.charAt(index+2)=='1'?4:0); 
					//value += (bits[index+2]==1?4:0);
					if(index + 3 < length){
						value += (bitData.charAt(index+3)=='1'?8:0);
						//value += (bits[index+3]==1?8:0);
						if(index + 4 < length){
							value += (bitData.charAt(index+4)=='1'?16:0);
							//value += (bits[index+4]==1?16:0);
						}else{
							padding = 1;
						}
					}else{
						padding = 2;
					}
				}else{
					padding = 3;	
				}
			}else{
				padding = 4;
			}
			
			if(prevValue == value){
				if(0 < padding || index + 5 == length){
					ret.append(encode(runLength+1));
					for(int i = 0; i < padding; i++){
						ret.append(PADDING_CHAR);
					}
					break;
				}else{
					runLength++;
					continue;
				}
			}else{
				if(1 < runLength){
					ret.append(encode(runLength));
					runLength = 1;
				}
			}
			ret.append(CHARS.charAt(value));
			if(0 < padding){
				for(int i = 0; i < padding; i++){
					ret.append(PADDING_CHAR);
				}
				break;
			}
			prevValue = value;
		}
		return ret.toString();
	}
	
	public static String encode(boolean[] bits){
		StringBuilder ret = new StringBuilder();
		int prevValue = -1;
		
		int runLength = 1;
		
		for(int index = 0; index < bits.length; index += 5){
			int value = (bits[index]?1:0);
			int padding = 0;
			if(index + 1 < bits.length){
				value += (bits[index+1]?2:0);
				if(index + 2 < bits.length){
					value += (bits[index+2]?4:0);
					if(index + 3 < bits.length){
						value += (bits[index+3]?8:0);
						if(index + 4 < bits.length){
							value += (bits[index+4]?16:0);
						}else{
							padding = 1;
						}
					}else{
						padding = 2;
					}
				}else{
					padding = 3;	
				}
			}else{
				padding = 4;
			}
			
			if(prevValue == value){
				if(0 < padding || index + 5 == bits.length){
					ret.append(encode(runLength+1));
					for(int i = 0; i < padding; i++){
						ret.append(PADDING_CHAR);
					}
					break;
				}else{
					runLength++;
					continue;
				}
			}else{
				if(1 < runLength){
					ret.append(encode(runLength));
					runLength = 1;
				}
			}
			ret.append(CHARS.charAt(value));
			if(0 < padding){
				for(int i = 0; i < padding; i++){
					ret.append(PADDING_CHAR);
				}
				break;
			}
			prevValue = value;
		}
		return ret.toString();
	}
		
	public static String encode(byte[] bits){
		boolean[] args = new boolean[bits.length];
		for(int i=0;i<bits.length; i++){
			args[i] = (bits[i] == 1);
		}
		return encode(args);
	}

	public static String encode(MultiSelection entry){
		StringBuilder ret = new StringBuilder();
		int prevValue = -1;
		
		int runLength = 1;

		ArrayList<Boolean> bits = entry.getSelectionBooleanArray();
		
		for(int index = 0; index < bits.size(); index+=5){
			int value = 0;
			try{
				value = (bits.get(index)?1:0);
				value += (bits.get(index+1)?2:0);
				value += (bits.get(index+2)?4:0); 
				value += (bits.get(index+3)?8:0);
				value += (bits.get(index+4)?16:0);
			}catch(ArrayIndexOutOfBoundsException ignore){
			}
			
			if(prevValue == value){
				runLength++;
				if(bits.size() <= index+5){
					ret.append(encode(runLength));
					break;
				}else{
					continue;
				}
			}else{
				if(1 < runLength){
					ret.append(encode(runLength));
					runLength = 1;	
				}
			}
			ret.append(CHARS.charAt(value));
			prevValue = value;
		}
		return ret.toString();
	}

	public static String encode(long srcValue){
		StringBuilder ret = new StringBuilder();
		String src = Long.toBinaryString(srcValue);

		int value = 0;
		int length = src.length() - 1;
		for(int index = length; 0 <= index; index--){
			switch(index % 5){
			case 0:
				value += (src.charAt(length - index)=='1')?1:0;
				break;
			case 1:
				value += (src.charAt(length - index)=='1')?2:0;
				break;
			case 2:
				value += (src.charAt(length - index)=='1')?4:0;
				break;
			case 3:
				value += (src.charAt(length - index)=='1')?8:0;
				break;
			case 4:
				value = (src.charAt(length - index)=='1')?16:0;
				break;
			}
			if( ((index % 5 == 0) && (0 < value || 0 < ret.length())) || index == 0){
				ret.append(CHARS.charAt(32+value));
			}
		}
		return ret.toString();
	}

	public static String encode(int src){
		return encode((long)src);
	}

}
