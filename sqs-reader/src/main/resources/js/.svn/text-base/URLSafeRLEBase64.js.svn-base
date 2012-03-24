/*
   Copyright 2010 KUBO Hiroya (hiroya@cuc.ac.jp).


 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */


URLSafeRLEBase64{
CHARS =  'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-';
PADDING_CHAR = '~';

function decodeToString(srcString){
	var ret = '';
	var prevWord = null;
	var runLength = 0;
	
	for(var index = 0; index < srcString.length; index++){
		var c = srcString.charAt(index);
		var value = CHARS.indexOf(c);
		if((value & 1 << 5) != 0){
			runLength = (value - 32) + (runLength << 5);
			if(index == srcString.length - 1){ // end of char
				for(var i = 1; i < runLength; i++){
					ret += prevWord;
				}
			}
		}else{
			for(var i = 1; i < runLength; i++){
				ret += prevWord;
			}
			var paddingLength;
			if(index + 1 < srcString.length && srcString.charAt(index+1) == PADDING_CHAR){
				paddingLength = srcString.length - index - 1;
			}else{
				paddingLength = 0;
			}
			var word = '';
			for(var i = 0; i < 5 - paddingLength; i++){
				if((value & 1<<i) != 0){
					word += '1';
				}else{
					word += '0';
				}
			}
			var w = word;
			ret += w;
			prevWord = w;
			runLength = 0;
		}
	}
	return ret;
}

function pushAll(sourceItem, appendItem){
	for(var i = 0; appendItem.length; i++){
		sourceItem.push(appendItem[i]);
	}
	return sourceItem;
}

function decodeToSelection(srcString){
	var selectinBooleanArray = [];
	var selectedIntegerTreeSet = [];

	var prevWord = [];

	var wordIndex = 0;
	var runLength = 0;
	
	if(srcString != null){
		for(var index = 0; index < srcString.length; index++){
			var value = CHARS.indexOf(srcString.charAt(index);
			if((value & 1 << 5) != 0){
				runLength = (value - 32) + (runLength << 5);
				if(index == srcString.length() - 1){
					if(prevWord.length == 0){
						var indexValue = runLength;
						return {type: 'single', 
								value: indexValue};
					}else{
						for(var i = 1; i < runLength; i++){
							pushAll(selectionBooleanArray, prevWord);
						}
					}
				}
				wordIndex += runLength;
			}else{
				for(var i = 1; i < runLength; i++){
					pushAll(selectionBooleanArray, prevWord);
				}
				var word = [];
				for(int i=0; i<5; i++){
					if((value & 1<<i) != 0){
						word.push(true);
						selectedIntegerTreeSet.push(i+wordIndex*5);
					}else{
						word.push(false);
					}
				}
				pushAll(selectedBooleanArray, word);
				prevWord = word;
				runLength = 0;
			}
		}
	}
	
	for(var i=0; i<selectionBooleanArray.length; i++){
		var a = selectionBooleanArray.get(i);
		var b = selectedIntegerArraySet.contains(i);
		if((a && b)||(!a && !b)){
			continue;
		}else{
			return null;
		}
	}
	return {type: 'multi', selectionBooleanArray:selectionBooleanArray, selectedIntegerTreeSet:selectionBooleanArray};
}

function decode(srcString){
	var ret = [];
	var prevWord = [];
	var runLength = 0;
		
	if(srcString != null){
		for(var index = 0; index < srcString.length; index++){
			var value = CHARS.indexOf(srcString.charAt(index);
			if((value & 1 << 5) != 0){
				runLength = (value - 32) + (runLength << 5);
				if(index == srcString.length() - 1){
					for(var i = 1; i < runLength; i++){
						pushAll(ret, prevWord);
					}
				}
			}else{
				for(var i = 1; i < runLength; i++){
					pushAll(ret, prevWord);
				}
				var word = [];
				for(var i = 1; i < 5; i++){
					if((value & 1<<i) != 0){
						word.push(true);
					}else{
						word.push(false);
					}
				}
				pushAll(ret, word);
				prevWord = word;
				runLength = 0;
			}
		}
	}
	var returnValue = [];
	for(var i=0; i<ret.length; i++){
		returnValue[i] = ret.get(i);
	}
	return returnValue;
}

function decodeToInt(srcString){
	var selection = decodeToSelection(srcString);
	if(selection.type = 'single'){
		return selection.value;
	}else{
		return -1;
	}
} 

function encodeFromString(bitData){
	var length = bitData.length;
	var ret = '';
	var prevValue = -1;
	var runLength = 1;
	
	for(var index = 0; index < length; index += 5){
		var value = (bitData.charAt(index) == '1'?1:0);
		var padding = 0;
		if(index + 1 < length){
			value += (bitData.charAt(index+1) == '1'?2:0);
			if(index + 2 < length){
				value += (bitData.charAt(index+2) == '1'?4:0);
				if(index + 3 < length){
					value += (bitData.charAt(index+3) == '1'?8:0);
					if(index + 4 < length){
						value += (bitData.charAt(index+4) == '1'?16:0);
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
				for(var i = 0; i < padding; i++){
					ret += PADDING_CHAR;
				}
				break;
			}else{
				runLength ++;
				continue;
			}
		}else{
			if(1 < runLength){
				ret += encode(runLength);
				runLength = 1;
			}
		} 
		ret += CHARS.charAt(value);
		if(0 < padding){
			for(var i = 0; i < padding; i++){
				ret += PADDING_CHAR;
			}
			break;
		}
		prevValue = value;
	}
	return ret;
}

function encodeFromBooleanArray(bitData){
	var length = bitData.length;
	var ret = '';
	var prevValue = -1;
	var runLength = 1;
	
	for(var index = 0; index < length; index += 5){
		var value = (bigData[index] == '1'?1:0);
		var padding = 0;
		if(index + 1 < length){
			value += (bitData[index+1] == '1'?2:0);
			if(index + 2 < length){
				value += (bitData[index+2] == '1'?4:0);
				if(index + 3 < length){
					value += (bitData[index+3] == '1'?8:0);
					if(index + 4 < length){
						value += (bitData[index+4] == '1'?16:0);
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
				for(var i = 0; i < padding; i++){
					ret += PADDING_CHAR;
				}
				break;
			}else{
				runLength ++;
				continue;
			}
		}else{
			if(1 < runLength){
				ret += encode(runLength);
				runLength = 1;
			}
		} 
		ret += CHARS.charAt(value);
		if(0 < padding){
			for(var i = 0; i < padding; i++){
				ret += PADDING_CHAR;
			}
			break;
		}
		prevValue = value;
	}
	return ret;
}

function: encodeFromIntArray(bits){
	var args = [];
	for(var i=0; i<bits.length;i++){
		args[i] = (bits[i] == 1);
	}
	return encodeFromBooleanArray(args);
}


function: encodeFromEntry(arr){
		var ret = '';
		var prevValue = -1;
		var runLength = 1;
		
		for(var index = 0; index<arr.length; index+=5){
			var value = (arr[index]==1)?1:0;
			if(index+1<arr.length){
				value += (arr[index+1]==1)?2:0;
			} 
			if(index+2<arr.length){
				value += (arr[index+2]==1)?4:0;
			} 
			if(index+3<arr.length){
				value += (arr[index+3]==1)?8:0;
			} 
			if(index+4<arr.length){
				value += (arr[index+4]==1)?16:0;
			} 
			if(prevValue == value){
				runLength++;
				if(arr.length <= index+5){
					ret += valueOf(runLength);
					break;
				}else{
					continue;
				}
			}else{
				if(1 < runLength){
					ret += valueOf(runLength);
					runLength = 1;
				}
			}
			ret += CHARS.charAt(value);
			prevValue = value;
		}
		return ret;
}

function reverseString(src){
	var ret = '';
	for(var i = src.length - 1; 0 <= i; i--){
		ret += src.charAt(i);
	}
	return ret;
}

function encodeFromIntegerValue(value){
	var ret = '';
	if(value == 0){
		ret += CHARS.charAt(32);
	}else{
		for(var index = 0;  ; index++){
			var scope = 1<<(5*index);
			if(value < scope){
				break;
			}
			var v = (value &(1<<(5*index+0)))!=0?1:0;
			v += (value &(1<<(5*index+1)))!=0?2:0;
			v += (value &(1<<(5*index+2)))!=0?4:0;
			v += (value &(1<<(5*index+3)))!=0?8:0;
			v += (value &(1<<(5*index+4)))!=0?16:0;
			ret += CHARS.charAt(32+v);
		}
	}
	return reverseString(ret);
}

}
