/*
   Copyright 2008 KUBO Hiroya (hiroya@cuc.ac.jp).

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
 LinkedList = Class.create();
 LinkedList.prototype = {
 	initialize: function(){
 		this.firstEntry = null;
 		this.lastEntry = null;
 		this.size = 0;
 	},
 	
 	add: function(obj){
 		if(this.lastEntry == null){
 			this.firstEntry =  new LinkedListEntry(obj); 
 			this.lastEntry = this.firstEntry;
 		}else{
 			var nextEntry = new LinkedListEntry(obj);
			nextEntry.setPrevEntry(this.lastEntry);
			this.lastEntry.setNextEntry(nextEntry);
			this.lastEntry = nextEntry;
 		}
 		this.size += 1;
 	},
 	
 	clear: function(){
 		this.firstEntry = null;
 		this.lastEntry = null;
 		this.size = 0;
 	},
 	
 	forwardEach: function(func){
 		var cursor = this.firstEntry;
 		while(cursor != null){
 			func(cursor.value);
 			cursor = cursor.nextEntry;
 		}
 	},

 	backwardEach: function(func){
 		var cursor = this.lastEntry;
 		while(cursor != null){
 			func(cursor.value);
 			cursor = cursor.prevEntry;
 		}
 	},
 	
 	toString: function(){
 		var ret = []; 
 		var cursor = this.firstEntry;
 		while(cursor != null){
 			if(ret.length != 0){
 				ret.push(',');
 			}
 			ret.push(cursor.value);
 			cursor = cursor.nextEntry;
 		}
 		return ret.join('');
 	}
};
 
 LinkedListEntry = Class.create();
 LinkedListEntry.prototype = {
 	initialize: function(obj){
 		this.value = obj;
 	},
 	setPrevEntry: function(prevEntry){
 		this.prevEntry = prevEntry;
 	},
 	setNextEntry: function(nextEntry){
 		this.nextEntry = nextEntry;
 	}
 };
 
 /*
  list = new LinkedList();
 list.add(1);
 list.add(3);
 list.add(5);
 list.add(7);
 alert(list.firstEntry.value);
 alert(list.firstEntry.nextEntry.value);
 alert(list.firstEntry.nextEntry.nextEntry.value);
 alert(list.firstEntry.nextEntry.nextEntry.nextEntry.value);
 alert(list.firstEntry.nextEntry.nextEntry.nextEntry.nextEntry.value);
 list.forwardEach(function(value){alert(value)});
 list.backwardEach(function(value){alert(value)});
 */
 
 