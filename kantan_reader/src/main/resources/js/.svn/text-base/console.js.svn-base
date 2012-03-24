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

DIGIT_ARRAY_CHARS =  '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_~';
DIGIT_ARRAY_UNIT_LEN = 6;

DigitArrayValueHandler = {
   	encode: function(options){
		var encodedValue = '';
		for(var i = 0; i < options.length; i+= DIGIT_ARRAY_UNIT_LEN){
			var k = 0;
			for(var j = 0; j < DIGIT_ARRAY_UNIT_LEN; j++){
				if(options[i + j] && options[i + j].selected){
					k += Math.pow(2, j);
				}
			}
			encodedValue += DIGIT_ARRAY_CHARS.charAt(k); 
		}
		return encodedValue;
	},
 
	isSelected: function(encodedValue, index){
		if(encodedValue == null){
			return false;
		}
		return 0 != (Math.pow(2, index % DIGIT_ARRAY_UNIT_LEN) & DIGIT_ARRAY_CHARS.indexOf(encodedValue.charAt(index / DIGIT_ARRAY_UNIT_LEN))); 
	}
};


TooltipHandler = Class.create();
TooltipHandler.prototype = {	
	initialize: function(id, defaultMessage){
		this.id = id;
		this.defaultMessage = defaultMessage;
	},
	
	setup: function(){
		this.elem = document.getElementById(this.id);
		this.elem.innerHTML = this.defaultMessage;
		return this.elem;
	},
	
 	update: function(newMessage){
 		if(this.elem == null){
 			this.setup();
 		}
		if(newMessage == null || newMessage.length == 0){
			this.elem.innerHTML = this.defaultMessage;
		}else{
			this.elem.innerHTML = newMessage.escapeHTML();
		}
  	},
  	
  	toHTML: function(){
 		return '<div class="fixedAtTop" id="'+this.id+'">'+this.defaultMessage+'</div>';
  	}
};

// ---------------------------------------------------------------------------------------------
ConsoleListBoxHandler = Class.create();
ConsoleListBoxHandler.prototype = Object.extend(new ListBoxHandler(), {
	initialize : function(id, attr, options) {
		this.index = 0;
		ListBoxHandler.prototype.initialize.apply(this, [id, attr, options]);		
	},
	
	updateOptions: function(optionSourceArray, func){
		this.index = 0;
		ListBoxHandler.prototype.updateOptions.apply(this, [optionSourceArray]);
		if(func != null){
			func();
		}
	},

	createOption: function(optionSource){
		if(optionSource.value == null){
			optionSource.value = this.index;
		}
		var ret = ListBoxHandler.prototype.createOption.apply(this, [optionSource]);
		this.index++;
		return ret; 
	},
	
	setEventHandler: function(requestHandler){
		this.onChangeHandler = requestHandler;
	},
	
	onChange: function(){
		ListBoxHandler.prototype.onChange.apply(this);
		this.attr['tooltipHandler'].update(this.getSelectedOptionText());
	},
	
	setDefaultSelection: function(params){
		var value = params[this.id];
		for(var i = 0; i < this.elem.options.length; i++){
			var isSelected = DigitArrayValueHandler.isSelected(value, i);
			//alert(value+" "+i+" "+isSelected);
			this.elem.options[i].selected = isSelected;
		}
	},
		
	type: 'ConsoleListBoxHandler'
});

MasterHandler = Class.create();
MasterHandler.prototype = Object.extend(new ConsoleListBoxHandler(), {
	initialize : function(id, attr, options) {
		ConsoleListBoxHandler.prototype.initialize.apply(this, [id, attr, options]);
	}
});

TableHandler = Class.create();
TableHandler.prototype = Object.extend(new ConsoleListBoxHandler(), {
	initialize : function(id, attr, options) {
		ConsoleListBoxHandler.prototype.initialize.apply(this, [id, attr, options]);
	}
});

RowHandler = Class.create();
RowHandler.prototype = Object.extend(new ConsoleListBoxHandler(), {
	initialize : function(id, attr, options) {
		ConsoleListBoxHandler.prototype.initialize.apply(this, [id, attr, options]);
	},
	
	updateState: function(){
		if($F('ar') == '1'){
			this.toggleAll(true);
		}
		ConsoleListBoxHandler.prototype.updateState.apply(this);
	},	
	
	createOption: function(optionSource){
		optionSource.text = '';
		optionSource.text += '[';
		optionSource.text += (this.index + 1);
		optionSource.text += ']';
		optionSource.text += optionSource.items[0];
		if(1 < optionSource.items.length){
			optionSource.text += '-';
			optionSource.text += optionSource.items[optionSource.items.length - 1];
		}
		return ConsoleListBoxHandler.prototype.createOption.apply(this, [optionSource]);
	},
	
	onChange: function(){
		ConsoleListBoxHandler.prototype.onChange.apply(this);
		if($F('ar') == '1'){
			this.attr['tooltipHandler'].update('全ての行');
		}else{
			this.attr['tooltipHandler'].update(this.getSelectedOptionText());
		}
	}
	
});

QuestionHandler = Class.create();
QuestionHandler.prototype = Object.extend(new ConsoleListBoxHandler(), {
	initialize : function(id, attr, options) {
		ConsoleListBoxHandler.prototype.initialize.apply(this, [id, attr, options]);
	},

	updateState: function(){
		if($F('aq') == '1'){
			this.toggleAll(true);
		}
		ConsoleListBoxHandler.prototype.updateState.apply(this);
	},

	onChange: function(){
		ConsoleListBoxHandler.prototype.onChange.apply(this);
		if($F('aq') == '1'){
			this.attr['tooltipHandler'].update('全ての設問');
		}else{
			this.attr['tooltipHandler'].update(this.getSelectedOptionText());
		}
	},

	createOption: function(optionSource){
		
		if(optionSource.text == null){
			var text = '';
			if(optionSource.label != null){
				text += optionSource.label;
			}
			if(optionSource.hints != null){
				text += optionSource.hints;
			}			
			optionSource.text = text;
		}
		
		if(optionSource.type != null){
			var icon = null;
			if(optionSource.type == 'select1'){
				icon = 'ball.red.gif';
			}else if(optionSource.type == 'select'){
				icon = 'ball.magenta.gif';
			}else if(optionSource.type == 'textarea'){
				icon = 'image.gif';
			}
			if(icon != null){
				optionSource.icon = icon;
			}
		}
		return ConsoleListBoxHandler.prototype.createOption.apply(this, [optionSource]);
	}

});
	 
fHandler =new CheckboxGroup('f', [
				    {'text':'択一選択', 'icon':'ball.red.gif', 'checked': true},
					{'text':'複数選択', 'icon':'ball.magenta.gif', 'checked': true},
				    {'text':'自由記述', 'icon':'image.gif', 'checked': true}
				 ]
				 );
				 
ViewModeHandler = Class.create();
ViewModeHandler.prototype = Object.extend(new RadioButtonGroup(),{
	initialize: function(id){
		var items = [
					{text:'回答の閲覧/修正', icon:'pencil.png'},
					{value: '<img src="'+imagebase+'/hr.gif" alt="----">'},
				    {text:'表データ書き出し', icon:'page_excel.png'},
					{text:'単純集計', icon:'chart_bar.png'},
				    {text:'グループ単純集計', icon:'chart_bar_add.png'},
				    {text:'クロス集計', icon:'table.png'},
				    {text:'グループクロス集計', icon:'table_add.png'},
				    {text:'クロス集計一覧', icon:'table_multiple.png'},
				    {value: '<img src="'+imagebase+'/hr.gif" alt="----">'},
				    {text:'読み取り失敗', icon:'exclamation.png'},
					{text:'信頼度低', icon:'icon_alert_cyan.gif'},				    
				    {text:'無回答エラー', icon:'icon_alert.gif'},
				    {text:'重複回答エラー', icon:'icon_alert_orange.gif'}
		];
		RadioButtonGroup.prototype.initialize.apply(this, [id, items]);
	},
	
	setup: function(){
	},
	
	setEventHandler: function(requestHandler){
		this.onChangeHandler = requestHandler;
		fHandler.setEventHandler(requestHandler);
	},
	
	toHTML: function(){
		var ret = [];
		ret.push('<h4>表示</h4>') 
		ret.push('<div style="float:left">');
		ret.push('<div style="font-size: 75%;">');
		ret.push(RadioButtonGroup.prototype.toHTML.apply(this, []));
		ret.push('</div>');
		ret.push('<div style="font-size: 75%; vertical-align: bottom; padding: 8px 4px 0 4px;">');
		ret.push('<img src="'+imagebase+'/icon_link.gif" style="border:none; margin: 1px" alt="[link]"/><a href="" id="linkURIAnchor" onclick="vHandler.showLink(); return false;">このページのリンク</a>');
		ret.push('</div>');
		ret.push('</div>');
		return ret.join('');
	},
	
	type: 'vHandler',
	
	updateState: function(){
		RadioButtonGroup.prototype.updateState.apply(this, []);
	},
	
	showLink: function(){
		if($('showLinkDiv').style.display == 'none'){
			$('showLinkDiv').style.display = 'block';
		}else{
			$('showLinkDiv').style.display = 'none';
		}
	}
	
});

tooltipHandler = new TooltipHandler('popupBar', '<p style="text-align: right">--- ここにマウスカーソルを合わせるとコンソールが開きます ---</p>');

mHandler = new MasterHandler('m',  
{'title': '調査票',
 'size':7,
 'width':14,
  'multiple': false,
  'tooltipHandler': tooltipHandler}
, 
[]);

tHandler = new TableHandler('t',  
{'title':'集計グループ',
 'size':7, 
 'width':14,
  'multiple': true,
  'tooltipHandler': tooltipHandler}
,
[]
);

rHandler = new RowHandler('r',  
{'title':'行 (<input type="checkbox" id="ar" value="1" onclick="rHandler.toggleAll(this); "/><label for="ar">:全ての行</label>)',
 'size':7, 
 'width':14,
  'multiple': true,
  'tooltipHandler': tooltipHandler,
  'toggleAllId': 'ar'}
 , 
[]);

vHandler = new ViewModeHandler('v');

qHandler = new QuestionHandler('q',  
{'title':'設問 (<input type="checkbox" id="aq" value="1" onclick="qHandler.toggleAll(this); " /><label for="aq">:全ての設問</label>) '+
'<span style="font-size: 80%">'+
'<input type="checkbox" id="f-0" value="1" checked="checked" onclick="fHandler.onClick(0);" /><label for="f-0"><img alt="select1" src="'+imagebase+'/ball.red.gif">:択一選択</label> '+
'<input type="checkbox" id="f-1" value="1" checked="checked" onclick="fHandler.onClick(1);" /><label for="f-1"><img alt="select" src="'+imagebase+'/ball.magenta.gif">:複数選択</label> '+
'<input type="checkbox" id="f-2" value="1" checked="checked" onclick="fHandler.onClick(2);" /><label for="f-2"><img alt="textarea" src="'+imagebase+'/image.gif">:自由記述</label>'+
'</span>',
 'size':7, 
 'width':42,
  'multiple': true,
  'tooltipHandler': tooltipHandler,
  'toggleAllId': 'aq'}
 , 
[]
);

/**********************************************************************************************************************/

ConsoleHandler = Class.create();
ConsoleHandler.prototype = {
	
	initialize: function(id, idArray, title, handlerArray){
		this.id = id;
		this.frameId = idArray['frame'];
		this.panelId = idArray['panel'];
		this.titleId = idArray['title'];
		this.closeButtonId = idArray['closeButton'];
		this.densityThreshold = -1;
		this.title = title;
		this.handlerArray = handlerArray;
		
		this.sticky = false;
		this.prevSelectedId = null;
		
		this.handlers = {};
		for(var z = 0; z < this.handlerArray.length; z++){
			for(var y = 0; y < this.handlerArray[z].length; y++){
	 			for(var x = 0; x < this.handlerArray[z][y].length; x++){
	 				var handler = this.handlerArray[z][y][x];
					this.handlers[handler.id] = handler;
	 			}
			}
		}
	},
	
	getDensityThreshold: function(tableIndex){
		var table = tHandler.flattenOptionSourceArray[tableIndex];
		return table.densityThreshold;
		/*
		if(this.densityThreshold != -1){
			return this.densityThreshold;
		}else{
		}*/
	},
	
	
	foreach: function(func){
		for(var z = 0; z < this.handlerArray.length; z++){
			for(var y = 0; y < this.handlerArray[z].length; y++){
	 			for(var x = 0; x < this.handlerArray[z][y].length; x++){
	 				var handler = this.handlerArray[z][y][x];
					func(handler);
	 			}
			}
		}
	},
	
	setup: function(){
		this.elem = $(this.id);
		this.titleElem = $(this.titleId);
		this.foreach(function(handler){handler.setup();});
		this.updateState();
	},
	
	setRequestHandler: function(requestHandler){
		this.foreach(function(handler){handler.setEventHandler(requestHandler);});
	},
	
	getHandler: function(handlerId){
		return this.handlers[handlerId];
	},

	toHTML: function(){
		ret = [];
		ret.push('<div id="'+this.id+'" class="console fixedAtTop">');
		ret.push('<div id="'+this.frameId+'" class="box">'); // frame
		ret.push('<div id="'+this.panelId+'">'); // panel
		ret.push('<h2 id="'+this.titleId+'">'); // title
		
		ret.push(this.title);
		ret.push('<img id="'+this.closeButtonId+'" alt="close button" src="'+imagebase+'/close.gif" style="cursor: default;">');
		ret.push('</h2>');
		
		ret.push('<div>');
		for(var z = 0; z < this.handlerArray.length; z++){
			ret.push('<div style="float:left;">');
			for(var y = 0; y < this.handlerArray[z].length; y++){
		  		ret.push('<div>');
				for(var x = 0; x < this.handlerArray[z][y].length; x++){
					ret.push('<div class="selector" style="float:left">');
					ret.push(this.handlerArray[z][y][x].toHTML());
					ret.push('</div>');
				}
				ret.push('<div style="clear:both"></div>');
				ret.push('</div>');
			}
			ret.push('</div>');
		}
		ret.push('<div style="clear:both"></div>');
    	ret.push('</div>');
    	
        ret.push('<div id="showLinkDiv" style="clear:both; display:none">');
        ret.push('<span style="margin-left: 2em; font-size: 70%"><a id="linkURIAnchor2" href="">このリンク</a>で現在の表示内容を再表示・共有できます:</span>');
		ret.push('<input type="text" id="linkURI" size="50" style="width: 28em"/></div>');

        ret.push('</div>'); // panel

        ret.push('</div>'); // frame
        ret.push('</div>'); // console
        return ret.join('');
	},
	
	updateState: function(){
		this.foreach(function(handler){handler.updateState();});
	},	
	
	show: function(){
		this.elem.style.display = 'block';
	},
	
	forcedHide: function(){
		this.hide(true);
	},

	hide: function(force){
		if(force || this.sticky == false){
			this.elem.style.display = 'none';
		}
	},

	toggle: function(){
		this.sticky = ! this.sticky;
		if(this.sticky){
			this.titleElem.style.cursor = 'crosshair';
		}else{
			this.titleElem.style.cursor = 'move';
		}
	},
	
	updateBackgroundColor: function(targetHandlerId){
		if(this.prevSelectedId != null){
			$(this.prevSelectedId).style.backgroundColor = '#eee';
			$A($(this.prevSelectedId).options).each(function(option){
				option.style.backgroundColor = '#eee';
			});
		}

		$(targetHandlerId).style.backgroundColor = '#ddf';
		$A($(targetHandlerId).options).each(function(option){
				option.style.backgroundColor = '#ddf';
		});
		this.prevSelectedId = targetHandlerId;
	},
	
	/*
	storeSelectedIndexKey: function(eventProducedTargetHandlerId){
		var key = '';
		var value = null;
		this.foreach(function(handler){
			if(handler.type == 'ConsoleListBoxHandler'){
	 			if(value == null){
	 				if(key != ''){
		 				key += ' '
	 				}
					key += $A(handler.getSelectedIndexArray()).join(',');
	 				if(handler.id == eventProducedTargetHandlerId){
		 				value = '';
	 				}
	 			}else{
	 				if(value != ''){
		 				value += ' '
	 				}
					value += $A(handler.getSelectedIndexArray()).join(',');
	 			}
	 		}			
		});
		alert(key+" = "+value);
	 },
	*/
	
	onChange: function(eventProducedTargetHandlerId){
		if(eventProducedTargetHandlerId == 'm' ||
		eventProducedTargetHandlerId == 't' ||
		eventProducedTargetHandlerId == 'r' ||
		eventProducedTargetHandlerId == 'q' ||
		eventProducedTargetHandlerId == 'ar' ||
		eventProducedTargetHandlerId == 'aq' ){
			this.updateBackgroundColor(eventProducedTargetHandlerId);
		}
	},
	
	hasChanged: function(eventProducedTargetHandlerId){
		this.foreach(function(handler){
			if(handler.id == eventProducedTargetHandlerId){
				handler.hasChanged();
			}
		});	
	}
};

UIHandlers = Class.create();
UIHandlers.prototype = {
	initialize: function(consoleHandler, tooltipHandler) {
		this.consoleHandler = consoleHandler;
		this.tooltipHandler = tooltipHandler;
	},
	
	setup: function(){
	},
	
	isEnabled : function(){
		return this._isEnabled;
	},
	
	getValue: function(formId, paramName){
		return Form.serialize($(formId)).toQueryParams()[paramName];
	},
	
	setEnabled: function(isEnabled) {
		this._isEnabled = isEnabled;
		if (this._isEnabled) {
			this.consoleHandler.foreach(function(handler){if(handler.elem != null){handler.elem.style.cursor = 'default';}});
			document.body.style.cursor = 'default';
		} else {
			this.consoleHandler.foreach(function(handler){if(handler.elem != null){handler.elem.style.cursor = 'wait';}});
			document.body.style.cursor = 'wait';
		}
	},

	isExportMode: function(viewMode){
		return viewMode == 2;
	},
	
	isPageErrorMode: function(viewMode){
		return viewMode == 9;
	},
	
	isLowReliabilityMode: function(viewMode){
		return viewMode == 10;
	},
	isNoAnswerMode: function(viewMode){
		return viewMode == 11;
	},
	isMultiAnswerMode: function(viewMode){
		return viewMode == 12;
	},
		
	onChange: function(eventProducedTargetHandlerId){
		this.setEnabled(false);
		this.consoleHandler.onChange(eventProducedTargetHandlerId);		
		var viewMode = this.getValue('consoleForm', 'v');				
		this.updateQuestionOptionItemState(eventProducedTargetHandlerId, viewMode);
		if(eventProducedTargetHandlerId != 'pagerButton'){
			contentsHandler.setNavigationIndex(0);
		}
	},

	storeSelectedQuestionOptionIndex:function(){
		if(this.storedSelectedQuestionOptionIndex != null){
			return;
		}
		var storedSelectedQuestionOptionIndex = [];
		$A(qHandler.elem.options).each(function(option, i){
			storedSelectedQuestionOptionIndex[i] = option.selected;
			option.selected = false;
			option.disabled = true;
		});
		this.storedSelectedQuestionOptionIndex = storedSelectedQuestionOptionIndex;
	},
	
	restoreSelectedQuestionOptionIndex:function(){
		if(this.storedSelectedQuestionOptionIndex == null){
			return;
		}
		this.storedSelectedQuestionOptionIndex.each(function(value, index){
			var option = qHandler.elem.options[index];
			option.selected = value;
		});
		this.storedSelectedQuestionOptionIndex = null;
	},

	disable: function(options){
		for(var i = 0; i < options.length; i++){
			options[i].disabled = true;
		}
	},
	
	deselect: function(options){
		for(var i = 0; i < options.length; i++){
			options[i].selected = false;
		}
	},
	
	setCheckboxState: function(id, isChecked, isDisabled){
		var checkbox = $(id);
		checkbox.checked = isChecked;
		checkbox.disabled = isDisabled;
	},
	
	updateQuestionOptionItemState: function(eventProducedTargetHandlerId, viewMode){
		if(eventProducedTargetHandlerId == 'f'){
			this.updateQuestionOptionItemStateByType();
			qHandler.updatePrevNextButtonState();
			return;
		}
		if(eventProducedTargetHandlerId != 'v' && eventProducedTargetHandlerId != 'aq'){
			return;  
		}
		if(this.isExportMode(viewMode)){
			this.storeSelectedQuestionOptionIndex();
			$('aq').disabled = true;
			$('aq').checked = false;
			this.setCheckboxState('f-0', true, true);
			this.setCheckboxState('f-1', true, true);
			this.setCheckboxState('f-2', true, true);
			this.deselect(qHandler.elem.options);
			this.disable(qHandler.elem.options);
		}else if(this.isPageErrorMode(viewMode)){
			this.storeSelectedQuestionOptionIndex();
			$('aq').disabled = true;
			$('aq').checked = false;
			this.setCheckboxState('f-0', true, true);
			this.setCheckboxState('f-1', true, true);
			this.setCheckboxState('f-2', true, true);
			this.deselect(qHandler.elem.options);
			this.disable(qHandler.elem.options);
		}else if(this.isNoAnswerMode(viewMode) || this.isMultiAnswerMode(viewMode)){
			this.storeSelectedQuestionOptionIndex();
			$('aq').disabled = false;
			this.setCheckboxState('f-0', true, true);
			this.setCheckboxState('f-1', false, true);
			this.setCheckboxState('f-2', false, true);
			this.updateQuestionOptionItemStateByType();
		}else if(this.isLowReliabilityMode(viewMode)){
			this.storeSelectedQuestionOptionIndex();
			$('aq').disabled = false;
			this.setCheckboxState('f-0', true, true);
			this.setCheckboxState('f-1', true, true);
			this.setCheckboxState('f-2', false, true);
			this.updateQuestionOptionItemStateByType();
		}else{
			this.restoreSelectedQuestionOptionIndex();
			$('aq').disabled = false;
			this.setCheckboxState('f-0', true, false);
			this.setCheckboxState('f-1', true, false);
			this.setCheckboxState('f-2', true, false);
			this.updateQuestionOptionItemStateByType();
		}
		qHandler.updatePrevNextButtonState();
	},
	
	updateQuestionOptionItemStateByType: function(){
		var checked = {};
		checked['select1'] = $F('f-0') != null;
		checked['select'] = $F('f-1') != null;
		checked['textarea'] = $F('f-2') != null;
		for(var i = 0; i < qHandler.optionSourceArray.length; i++){
			var optionSource = qHandler.optionSourceArray[i];
			var option = qHandler.elem.options[i];
			var disabled =  (checked[optionSource.type] == false);
			option.disabled = disabled;
			if(disabled){
				option.selected = false;
			}
		}
	},
	
	hasChanged: function(eventProducedTargetHandlerId){
		this.consoleHandler.hasChanged(eventProducedTargetHandlerId);		
		this.setEnabled(true);
	}	
};

/************************************************************************************/
RequestHandler = Class.create();
RequestHandler.prototype = {
	initialize : function(sid, uiHandler, contentsHandler, formId, checkboxIdArray, selectIdArray, digitSelectIdArray) {
		this.sid = sid;
		this.uiHandler = uiHandler;
		this.contentsHandler = contentsHandler;
		this.formId = formId;
		this.checkboxIdArray = checkboxIdArray;
		this.selectIdArray = selectIdArray;
		this.digitSelectIdArray = digitSelectIdArray;
		this.queryParams = '';
	},
	
	createQueryString : function(formId, targetHandlerId) {

		this.queryParams = Form.serialize(formId);
		
		for(var i = 0; i < this.checkboxIdArray.length; i++){
			var key = this.checkboxIdArray[i];
			var elem = $(key); 
			if(! elem){
				continue;
			}
			var value = elem.value;
			if(value == null || (! elem.checked && ! elem.selected)){
				continue;
			}
			
			if(value instanceof Array){
				for(var j = 0; j < value.length; j++){
					var arrayValue = value[j];
					this.queryParams += '&' + key + '=' + arrayValue;
				}
			}else{
				this.queryParams += '&' + key + '=' + value;
			}
		}
		
		for(var i = 0; i < this.selectIdArray.length; i++){
			var key = this.selectIdArray[i];
			var elem = $(key); 
			if(! elem){
				continue;
			}
			var value = elem.value;
			this.queryParams += '&' + key + '=' + value;
		}
		
		for(var i = 0; i < this.digitSelectIdArray.length; i++){
			var key = this.digitSelectIdArray[i];
			var elem = $(key); 
			this.queryParams += '&' + key + '=' + DigitArrayValueHandler.encode(elem.options);
		}

/*		
		if(consoleHandler.getDensityThreshold() != -1){
			this.queryParams += '&threshold='+consoleHandler.getDensityThreshold();
		}
*/	
		this.queryParams += '&sid='+this.sid;
		
		this.queryParams += '&n='+	contentsHandler.getAnswerItemStartIndex();//change n as navigationIndex?
		this.queryParams += '&N='+	contentsHandler.getNumMaxAnswerItems();

		return this.queryParams;
	},
	
	setCurrentURI: function(url, param){
		var uri = url+'?'+param;
		$('linkURI').value = uri; 
		$('linkURIAnchor').href = uri; 
		$('linkURIAnchor2').href = uri; 
	},
	
	createURI: function(){
		 return 'http://' + location.host + '/e';
	},

	updateCurrentURI: function(targetHandlerId){
		var url = this.createURI();
		var param = this.createQueryString(this.formId, targetHandlerId);
		this.setCurrentURI(url, param);
	},
	
	sendRequest : function(targetHandlerId, func) {
		var url = this.createURI();
		var param = this.createQueryString(this.formId, targetHandlerId);
		this.setCurrentURI(url, param);
		var ajax = new Ajax.Request(url, {
			method : 'get',
			parameters : 'u='+targetHandlerId+'&'+param,
			onComplete : func,
			/*
			onException : function(req, ex) {
				alert("Exception:"+ex);
				this.uiHandler.setEnabled(id, true);
				throw ex;
			},*/
			onFailure : function(ex) {
				alert("Exception:"+ex);
				//alert("指定されたセッションは無効です。最新のセッションに接続します。");
				this.uiHandler.setEnabled(id, true);
				location.href=  'http://' + location.host + '/e';
			}
		});
	},
		
	onChange: function(targetHandlerId){
		var CLIENT_SIDE_DEBUG = false;
		
		if (! this.uiHandler.isEnabled) {
			return;
		}
		
		this.uiHandler.onChange(targetHandlerId);

		
		if(CLIENT_SIDE_DEBUG){
			this.hasChanged(targetHandlerId);
		}else{
			this.sendRequest(targetHandlerId, this.hasChanged.bind(this, [targetHandlerId]));
		}
	},

	hasChanged: function(targetHandlerId){
		window.scroll(0,0);
		this.uiHandler.hasChanged(targetHandlerId);
		this.contentsHandler.hasChanged(this.uiHandler);
	},
	
	saveTextArea: function(srcElem){
		if(! srcElem.value){
			return;
		}
		document.body.style.cursor = 'wait';
		var params = 'key=' + srcElem.id + '&value=' + srcElem.value+'&sid='+this.sid;
		var url = 'http://'+location.host+'/u';
		var ajax = new Ajax.Request(
			url, 
			{
				method: 'post', 
				parameters: params, 
				onComplete: function func(){
					document.body.style.cursor = 'default';
				},
				onFailure: function(){
					alert("connection refused.");
					document.body.style.cursor = 'default';
				}
			});
	},

	saveMarkArea: function(srcElem, tableIndex){
		document.body.style.cursor = 'wait';
		var densityThreshold = consoleHandler.getDensityThreshold(tableIndex);
		var params = 'key=' + srcElem.id + '&value=' + ((srcElem.checked)?'1':'0') + '&threshold=' + densityThreshold + '&sid='+this.sid;
		var url = 'http://'+location.host+'/u';
		var ajax = new Ajax.Request(
			url, 
			{
				method: 'post', 
				parameters: params, 
				onComplete: function func(){
					document.body.style.cursor = 'default';
				},
				onFailure: function(message){
					alert("connection refused:"+message);
					document.body.style.cursor = 'default';
				}
			});
	},
	
	requestPrevPage: function(){
		document.body.style.cursor = 'wait';	
		//contentsHandler.setNavigationIndex(contentsHandler.navigationIndex - 1);
		this.sendRequest('pagerButton', this.hasChanged.bind(this, ['pagerButton']));
	},
	
	requestNextPage: function(){
		document.body.style.cursor = 'wait';	
		//contentsHandler.setNavigationIndex(contentsHandler.navigationIndex + 1);
		this.sendRequest('pagerButton', this.hasChanged.bind(this, ['pagerButton']));
	}

};
