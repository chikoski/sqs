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

SelectHandler = Class.create();
SelectHandler.prototype = {

	initialize: function(id, attr, optionSourceArray, imagebase) {
		this.id = id;
		this.attr = attr;
		this.optionSourceArray = optionSourceArray;
		this.flattenOptionSourceArray = [];
		this.imagebase = imagebase;
	},

	setup: function() {
		this.elem = document.getElementById(this.id);
		this.updateOptions(this.optionSourceArray);
		return this;
	},
	
	getOptionSource: function(index){
		return this.flattenOptionSourceArray[index];
	},
	
	getSelectedOptionText: function(){
		var ret = [];
		var separator = ', ';
		if(this.elem == null){
			return;
		}
		var length = this.elem.options.length;
		for(var i = 0; i < length; i++){
			var option = this.elem.options[i];
			if(option.selected){
				if(ret.length != 0){
					ret.push(separator);			
				}
				ret.push(option.text);
			}
		}
		return ret.join('');
	},
	
	getOptionText: function(index){
		if(index < this.elem.options.length){
			return this.elem.options[index].text;
		}else{
			return '* len='+this.elem.options.length+", idx="+index;
		}
	},

	getSelectedIndexArray: function(){
		var ret = [];
		if(this.elem == null){
			return [];
		}
		var length = this.elem.options.length;
		for(var i = 0; i < length; i++){
			var option = this.elem.options[i];
			if(option.selected){
				ret.push(i);
			}
		}
		return ret;
	},

	regulateSelection: function() {
		// constraintUI
		if (!this.elem.value) {
			var length = this.elem.options.length;
			for (var i = 0;i < length; i++) {
				var opt = select.options[i];
				if (opt.disabled != true) {
					opt.selected = true;
					break;
				}
			}
		}
	},
	
	onChange: function(){		
		if(this.elem == null || this.optionSourceArray == null){
			return;
		}
		if(this.onChangeHandler != null){
			this.onChangeHandler.onChange(this.id);
		}
	},
	
	hasChanged: function(){
	},
	
	updateOptions: function(optionSourceArray) {

		this.optionSourceArray = optionSourceArray;
		
		if(this.elem == null || this.optionSourceArray == null){
			return;
		}
		while(this.elem.hasChildNodes()){
			this.elem.removeChild(this.elem.firstChild);
		}
		this.flattenOptionSourceArray = [];

		var length = optionSourceArray.length;
		for(var i = 0; i < length; i++){
			var optionSource = optionSourceArray[i];
			option = this.createOptionElement(optionSource);
			this.elem.appendChild(option);
		}
	},
	
	createOptionGroup: function(optionSource){
		var optionGroup = document.createElement('optgroup');
		optionGroup.setAttribute('label', optionSource.optgroup);
		if(optionSource.icon){
			optionGroup.setAttribute('style', 'background-image:url('+this.imagebase+'/'+optionSource.icon+')');
		}
		var length = optionSource.items.length;
		for(var i = 0; optionSource.items && i < length; i++){
			var optionSourceOption = optionSource.items[i];
			optionGroup.appendChild(this.createOptionElement(optionSourceOption));
		};
		return optionGroup;
	},
	
	createOption: function(optionSource){
		var option = document.createElement('option');
		option.appendChild(document.createTextNode(optionSource.text));
		
		if(optionSource.value != null){
			option.setAttribute('value', optionSource.value);
		}
			
		if(optionSource.selected){
			option.setAttribute('selected', 'selected');
		}
		if(optionSource.disabled){
			option.setAttribute('disabled', 'disabled');
		}
		if(optionSource.icon){
			option.setAttribute('style', 'background-image:url('+this.imagebase+'/'+optionSource.icon+')');
		}
		/*
		if(! Prototype.Browser.IE){
			option.onmouseover = new function(){alert(optionSource.text);};
		}
		*/
		this.flattenOptionSourceArray.push(optionSource);
		return option;
	},	

	createOptionElement: function(optionSource){
		if(optionSource.optgroup){
			return this.createOptionGroup(optionSource);
		}else{			
			return this.createOption(optionSource);
		}
	},
	
	
	toString: function() {
		return this.id;
	}
};

ListBoxHandler = Class.create();
ListBoxHandler.prototype = Object.extend(new SelectHandler(), {

	initialize : function(id, attr, options, imagebase) {
		SelectHandler.prototype.initialize.apply(this, [id, attr, options, imagebase]);
		this.reservedSelection = [];

		this.selectedIndexLinkedList = new LinkedList();
		this.disabledIndexLinkedList = new LinkedList();
	},
	
	setup : function(){
		SelectHandler.prototype.setup.apply(this);
		this.prevButton = document.getElementById(this.id+'PrevButton');
		this.nextButton = document.getElementById(this.id+'NextButton');
	},
	
    updateOptions: function(optionSourceArray) {
		SelectHandler.prototype.updateOptions.apply(this, [optionSourceArray]);
		this.updateState();
	},
	
	toHTML: function(){
		var tilte = '';
		var multiple = '';
		if(this.attr['title']){
			title = '<h4>'+this.attr['title']+'</h4>';
		}
		if(this.attr['multiple']){
			multiple = 'multiple="multiple"';
		}
		
		return '<div>'+
		title+
		'<select '+multiple+' size="'+this.attr['size']+
		'" style="width:'+this.attr['width']+
		'em" id="'+this.id+'" onchange="'+this.id+'Handler.onChange()"></select>'+
		'<br/>'+
		'<input type="button" value="▼" style="width:'+(this.attr['width']/2-1)+
		'em" class="prevNextButton" id="'+this.id+'NextButton" onclick="'+
		this.id+'Handler.clickNextButton()"/>'+
		'<input type="button" value="▲" style="width:'+(this.attr['width']/2-1)+
		'em" class="prevNextButton" id="'+this.id+'PrevButton" onclick="'+
		this.id+'Handler.clickPrevButton()"/>'+
		'</div>'; 
	},
	
	onChange: function(){
		SelectHandler.prototype.onChange.apply(this);

		if(this.attr['toggleAllId']  !=  null){
			$(this.attr['toggleAllId']).checked = false;
		}
		
		this.updateSelected();

		this.updatePrevNextButtonState();
	},
	
	/*
	onMouseOver: function(option){
		if(this.onMouseOverHandler != null){
			this.onMouseOverHandler.onMouseOver(this, option);
		}
	},*/
	
	hasChanged: function(){		
		SelectHandler.prototype.hasChanged.apply(this);	
	},
	
	updateSelected: function(){
		this.upperDisabled = 0;
		this.numInRangeDisabled = 0;
		this.lowerDisabled = 0;
		
		this.selectedIndexLinkedList.clear();
		var length = this.elem.options.length;
		for(var i = 0; i < length; i++){
			var option = this.elem.options[i];
			if(option.disabled){
				if(this.selectedIndexLinkedList.size == 0){
					this.upperDisabled += 1;
				}else{
					this.numInRangeDisabled += 1;
					this.lowerDisabled += 1;
				}
			}
			if(option.selected){
				this.selectedIndexLinkedList.add(i);
				this.lowerDisabled = 0;
			}
		}
		this.numInRangeDisabled -= this.lowerDisabled;
	},

	getUpperSelectedIndex: function(){
		if(this.selectedIndexLinkedList.firstEntry != null){
			return this.selectedIndexLinkedList.firstEntry.value;
		}
		return -1;
	},

	getLowerSelectedIndex: function(){
		if(this.selectedIndexLinkedList.lastEntry != null){
			return this.selectedIndexLinkedList.lastEntry.value;
		}
		return -1;
	},
	
	countUpperSelectableOptions: function(){
		var i = this.getUpperSelectedIndex();
		if(i != -1){
			return i - this.upperDisabled;
		}else{
			return 0;
		}
	},

	countLowerSelectableOptions: function(){
		var i = this.getLowerSelectedIndex();
		if(i != -1){
			return this.elem.options.length - 1 - i - this.lowerDisabled;
		}else{
			return 0;
		}
	},
	
	getSelectedIndexRange: function(){
		if(this.selectedIndexLinkedList.lastEntry == null){
			return 0;
		}
		return this.selectedIndexLinkedList.lastEntry.value - this.selectedIndexLinkedList.firstEntry.value + 1 - this.numInRangeDisabled;
	},
	
	isPrevButtonEnabled: function(range){
		return range <= this.countUpperSelectableOptions();
	},
	
	isNextButtonEnabled: function(range){
		return range <= this.countLowerSelectableOptions();
	},

	clickPrevButton: function() {
		
		var upperSelectedIndex = this.getUpperSelectedIndex();
		var lowerSelectedIndex = this.getLowerSelectedIndex();

		if (this.elem == null || 0 == this.elem.options.length || upperSelectedIndex == -1 ) {
			return;
		}
		
		var range = this.getSelectedIndexRange();
		
		if(! this.isPrevButtonEnabled(range)){
			return;
		}
		
		var numMoved = 0;
		var length = this.elem.options.length;
		for(var i = upperSelectedIndex; i < length && numMoved != range; i++){
			if(! this.elem.options[i].disabled){
				this.elem.options[i].selected = false;
				numMoved++;
			}
		}
		numMoved = 0;
		for(var i = upperSelectedIndex - 1 ; 0 <= i && numMoved != range; i--){
			if(! this.elem.options[i].disabled){
				this.elem.options[i].selected = true;
				numMoved++;
			}
		}
		
		this.updatePrevNextButtonState();
		this.onChange();
	},

	clickNextButton: function() {
		var upperSelectedIndex = this.getUpperSelectedIndex();
		var lowerSelectedIndex = this.getLowerSelectedIndex();

		if (this.elem == null || 0 == this.elem.options.length || lowerSelectedIndex == -1 ) {
			return;
		}

		var range = this.getSelectedIndexRange();

		if(! this.isNextButtonEnabled(range)){
			return;
		}

		var numMoved = 0;
		var length = this.elem.options.length;
		for(var i = lowerSelectedIndex + 1; i < length  && numMoved != range; i++){
			if(! this.elem.options[i].disabled){
				this.elem.options[i].selected = true;
				numMoved++;
			}
		}
		numMoved = 0;
		for(var i = lowerSelectedIndex ; 0 <= i && numMoved != range; i--){
			if(! this.elem.options[i].disabled){
				this.elem.options[i].selected = false;
				numMoved++;
			}
		}
		
		this.updatePrevNextButtonState();
		this.onChange();
	},

	updateState : function() {
		this.updatePrevNextButtonState();
	},
	
	updatePrevNextButtonState: function() {
		
		if(this.elem == null || this.prevButton == null || this.nextButton == null){
			return;
		}

		var length = this.elem.options.length;
		if (this.elem.options.length == 0 || this.elem.disabled) {
			this.prevButton.disabled = true;
			this.nextButton.disabled = true;
			return;
		}

		var range = this.getSelectedIndexRange();

		if (this.elem.value) {
			if (! this.isPrevButtonEnabled(range)) {
				this.prevButton.disabled = true;
			} else {
				this.prevButton.disabled = false;
			}

			if (! this.isNextButtonEnabled(range)) {
				this.nextButton.disabled = true;
			} else {
				this.nextButton.disabled = false;
			}
		} else {
			this.prevButton.disabled = true;
			this.nextButton.disabled = true;
		}
	},
	
	/*
	setStyle: function(isFocused){
		this.setEvenOddStyle(isFocused);
	},
	
	setEvenOddStyle: function(isFocused){
		// set even/odd background color
		if (isFocused) {
			for (var i = 0; i < this.elem.options.length; i++){
				var opt = this.elem.options[i];
				if (i % 2 == 0) {
					opt.className = 'focusedEven';
				} else {
					opt.className = 'focusedOdd';
				}
			}
		} else {
			for (var i = 0; i < this.elem.options.length; i++){
				var opt = this.elem.options[i];
				if (i % 2 == 0) {
					opt.className = 'even';
				} else {
					opt.className = 'odd';
				}
			}	
		}
	},

	updateFocusState : function(isFocused) {
		// ActiveSelectedMark
		var length = this.elem.options.length;
		for (var i = 0; i < length; i++) {
			var opt = this.elem.options[i];
			if (opt.selected == false) {
				if (isFocused) {
					if (i % 2 == 0) {
						opt.className = 'focusedEven';
					} else {
						opt.className = 'focusedOdd';
					}
				} else {
					if (i % 2 == 0) {
						opt.className = 'even';
					} else {
						opt.className = 'odd';
					}
				}
			}
		}
	},
	*/

	toggleAll: function(isSelectAllCheckbox, func){
		var elem = document.getElementById(this.id);
		if(elem != null){
			if(0 == this.elem.options.length){
				isSelectAllCheckbox.checked = false;
			}
			if(isSelectAllCheckbox.checked){
				var length = elem.options.length;
				for(var i = 0; i < length; i++){
					var option = elem.options[i];
					this.reservedSelection[i] = option.selected;
					if(func != null){
						func(option, this.flattenOptionSourceArray[i]);
					}else{
						if(! option.disabled){
							option.selected = true;
						}
					}
				}
			}else{
				for(var i = 0; i < elem.options.length; i++){
					elem.options[i].selected = this.reservedSelection[i];
				}
			}
			SelectHandler.prototype.onChange.apply(this);
			this.updatePrevNextButtonState();
		}
	}

});
