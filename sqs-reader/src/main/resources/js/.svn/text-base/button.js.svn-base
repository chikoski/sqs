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

ButtonGroup = Class.create();
ButtonGroup.prototype = {
	initialize: function(id, items, imagebase){
		this.id = id;
		this.items = items;
		this.imagebase = imagebase;
	},
	
	setEventHandler: function(requestHandler){
		this.onChangeHandler = requestHandler;
	},

	toHTML: function(){
		 return this.ulHTML(this.items);
	},
	
	ulHTML: function(items){
		var ret = [];
		ret.push('<ul>');
		var length = items.length;
		for(var i=0; i < length; i++){
		 	var item = items[i];
		 	ret.push('<li>');
		 	if(item.value){
		 		ret.push(item.value);
		 	}else{
		 		var icon = '';
		 		if(item.icon){
			 		icon = '<img class="buttonItemIcon" src="'+this.imagebase+'/'+item.icon+'" alt="item"/>';
		 		}
		 		ret.push('<label for="'+this.id+'-'+i+'">'+this.itemHTML(i, item)+icon+
		 				'<span id="'+this.id+'-'+i+'-itemText">'+item.text+'<span>'+
		 				'<span id="'+this.id+'-'+i+'-itemSuffix"><span>'+
		 				'</label>');
		 		if(item.items != null){
			 		ret.push(item.items.toHTML());
		 		}
		 	}		 	
	 		ret.push('</li>');
		}
		ret.push('</ul>');
		return ret.join('');
	},

	itemHTML: function(i, item){
	},
	
	setEnabled: function(isEnabled){
		var length = this.items.length;
		var id_ = this.id+'-';
		for(var i=0; i < length; i++){
			//var item = this.items[i];
			$(id_+i).disabled = ! isEnabled;
		}
	},
	
	onClick: function(changedIndex){
		this.changedIndex = changedIndex;
		this.onChange();
	},
	
	onChange: function(){
		this.onChangeHandler.onChange(this.id);
	},
	
	hasChanged: function(){
		this.updateState();
	},
	
	updateState: function(){
		var isEnabled = true;
		var length = this.items.length;
		for(var i=0; i < length; i++){
			var item = this.items[i];
			var itemElem = $(this.id+'-'+i);
			if(item.items != null && itemElem != null){
				item.items.setEnabled(itemElem.checked);
			}
		}
	}

}; 

CheckboxGroup = Class.create();
CheckboxGroup.prototype = Object.extend(new ButtonGroup(),{

	itemHTML: function(i, item){
		var checked = '';		
		if(item.checked){
			checked = 'checked="checked"';
		}
		return '<input type="checkbox" id="'+this.id+'-'+i+'" value="1" '+checked+' onclick="'+this.id+'Handler.onClick('+i+')"/>';
	},
	
	setDefaultSelection: function(params){
	  	var length = this.items.length;
		for(var i=0; i < length; i++){
		 	var key = this.id+'-'+i;
		 	var button = $(key);
		 	if(button){		 	
				button.checked = (params[key] == '1');
			}
		} 
	}

}); 

RadioButtonGroup = Class.create();
RadioButtonGroup.prototype = Object.extend(new ButtonGroup(),{
	
	itemHTML: function(i, item){
		var checked = '';
		if(item.checked){
			checked = 'checked="checked"';
		}
		return '<input type="radio" name="'+this.id+'" id="'+this.id+'-'+i+'" value="'+i+'" '+checked+' onclick="'+this.id+'Handler.onClick('+i+')"/>';
	},
	
	setDefaultSelection: function(params){
		var value = params[this.id];
		var length = this.items.length;
		for(var i=0; i < length; i++){
		 	var key = this.id+'-'+i;
		 	var button = $(key);
		 	if(button){		 	
				button.checked = (value == $(key).value);
			}
		} 
	}
	
}); 
