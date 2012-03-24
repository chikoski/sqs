
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
	initialize : function(id, attr, options, imagebase) {
		this.index = 0;
		ListBoxHandler.prototype.initialize.apply(this, [id, attr, options, imagebase]);		
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
		var arr = RLEBinaryArrayUtil.decode(value);
		for(var i = 0; i < this.elem.options.length; i++){
			this.elem.options[i].selected = (arr[i] == 1);
		}
	},
		
	type: 'ConsoleListBoxHandler'
});

MasterHandler = Class.create();
MasterHandler.prototype = Object.extend(new ConsoleListBoxHandler(), {
	initialize : function(id, attr, options, imagebase) {
		ConsoleListBoxHandler.prototype.initialize.apply(this, [id, attr, options, imagebase]);
	}
});

TableHandler = Class.create();
TableHandler.prototype = Object.extend(new ConsoleListBoxHandler(), {
	initialize : function(id, attr, options, imagebase) {
		ConsoleListBoxHandler.prototype.initialize.apply(this, [id, attr, options, imagebase]);
	}
});

RowHandler = Class.create();
RowHandler.prototype = Object.extend(new ConsoleListBoxHandler(), {
	initialize : function(id, attr, options, imagebase) {
		ConsoleListBoxHandler.prototype.initialize.apply(this, [id, attr, options, imagebase]);
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
			this.attr['tooltipHandler'].update(ALL_ROWS_LABEL);
		}else{
			this.attr['tooltipHandler'].update(this.getSelectedOptionText());
		}
	}
	
});

QuestionHandler = Class.create();
QuestionHandler.prototype = Object.extend(new ConsoleListBoxHandler(), {
	initialize : function(id, attr, options, imagebase) {
		ConsoleListBoxHandler.prototype.initialize.apply(this, [id, attr, options, imagebase]);
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
			this.attr['tooltipHandler'].update(ALL_QUESTIONS_LABEL);
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
				    {'text':SELECT_SINGLE_LABEL, 'icon':'ball.red.gif', 'checked': true},
					{'text':SELECT_MULTI_LABEL, 'icon':'ball.magenta.gif', 'checked': true},
				    {'text':FREE_ANSWER_LABEL, 'icon':'image.gif', 'checked': true}
				 ]
				 );


ViewModeHandler = Class.create();
ViewModeHandler.prototype = Object.extend(new RadioButtonGroup(),{
	initialize: function(id, imagebase){
		var items = [
					{text:BROWSE_EDIT_ANSWER_LABEL, icon:'pencil.png', checked:true},
					{value: '<img src="'+IMAGE_BASEPATH+'/hr.gif" alt="----">'},
				    {text:EXPORT_SPREADSHEET_LABEL, icon:'page_excel.png'},
					{text:SIMPLE_STATISTICS_LABEL, icon:'chart_bar.png'},
				    {text:GROUPED_STATISTICS_LABEL, icon:'chart_bar_add.png'},
				    {text:CROSS_TABULAR_LABEL, icon:'table.png'},
				    {text:CROSS_TABULAR_BY_GROUP_LABEL, icon:'table_add.png'},
				    {text:CROSS_TABULAR_LIST_LABEL, icon:'table_multiple.png'},
				    {value: '<img src="'+IMAGE_BASEPATH+'/hr.gif" alt="----">'},
				    {text:PAGE_ERROR_LABEL, icon:'exclamation.png'},
					{text:LOW_CONFIDENCE_LABEL, icon:'icon_alert_cyan.gif'},
				    {text:NO_ANSWER_ERROR_LABEL, icon:'icon_alert.gif'},
				    {text:MULTIPLE_ANSWER_ERROR_LABEL, icon:'icon_alert_orange.gif'}
		];
		RadioButtonGroup.prototype.initialize.apply(this, [id, items, imagebase]);
	},
	
	setup: function(){
	},
	
	setEventHandler: function(requestHandler){
		this.onChangeHandler = requestHandler;
		fHandler.setEventHandler(requestHandler);
	},
	
	toHTML: function(){
		var ret = [];
		ret.push('<h4>',DISPLAY_CONTENTS_LABEL,'</h4>') 
		ret.push('<div style="float:left">');
		ret.push('<div style="font-size: 75%;">');
		ret.push(RadioButtonGroup.prototype.toHTML.apply(this, []));
		ret.push('</div>');
		ret.push('<div style="font-size: 75%; vertical-align: bottom; padding: 8px 4px 0 4px;">');
		ret.push('<img src="'+IMAGE_BASEPATH+'/icon_link.gif" style="border:none; margin: 1px" alt="[link]"/><a href="" id="linkURIAnchor" onclick="vHandler.showLink(); return false;">'+LINK_TO_THIS_PAGE+'</a>');
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

tooltipHandler = new TooltipHandler('popupBar', '<p style="text-align: right">--- '+POPUP_CONSOLE_ON_FOCUS_LABEL+' ---</p>');

mHandler = new MasterHandler('m',  
{'title': MASTER_LABEL,
 'size':7,
 'width':14,
  'multiple': false,
  'tooltipHandler': tooltipHandler}
, 
[],
IMAGE_BASEPATH);

tHandler = new TableHandler('t',  
{'title':ROW_GROUP_LABEL,
 'size':7, 
 'width':14,
  'multiple': true,
  'tooltipHandler': tooltipHandler}
,
[],
IMAGE_BASEPATH
);


rHandler = new RowHandler('r',  
{'title':ROW_LABEL+' (<input type="checkbox" id="ar" value="1" onclick="rHandler.toggleAll(this); "/><label for="ar">:'+ALL_ROWS_LABEL+'</label>)',
 'size':7, 
 'width':14,
  'multiple': true,
  'tooltipHandler': tooltipHandler,
  'toggleAllId': 'ar'}
 , 
[],
IMAGE_BASEPATH);

vHandler = new ViewModeHandler('v', IMAGE_BASEPATH);

qHandler = new QuestionHandler('q',  
{'title':QUESTION_LABEL+' (<input type="checkbox" id="aq" value="1" onclick="qHandler.toggleAll(this); " /><label for="aq">:'+ALL_QUESTIONS_LABEL+'</label>) '+
'<span style="font-size: 80%">'+
'<input type="checkbox" id="f-0" value="1" checked="checked" onclick="fHandler.onClick(0);" /><label for="f-0"><img alt="select1" src="'+IMAGE_BASEPATH+'/ball.red.gif">:'+SELECT_SINGLE_LABEL+'</label> '+
'<input type="checkbox" id="f-1" value="1" checked="checked" onclick="fHandler.onClick(1);" /><label for="f-1"><img alt="select" src="'+IMAGE_BASEPATH+'/ball.magenta.gif">:'+SELECT_MULTI_LABEL+'</label> '+
'<input type="checkbox" id="f-2" value="1" checked="checked" onclick="fHandler.onClick(2);" /><label for="f-2"><img alt="textarea" src="'+IMAGE_BASEPATH+'/image.gif">:'+FREE_ANSWER_LABEL+'</label>'+
'</span>',
 'size':7, 
 'width':42,
  'multiple': true,
  'tooltipHandler': tooltipHandler,
  'toggleAllId': 'aq'}
 , 
[],
IMAGE_BASEPATH
);

/**********************************************************************************************************************/

ConsoleHandler = Class.create();
ConsoleHandler.prototype = {
	
	initialize: function(sessionID, id, idArray, title, handlerArray){
		this.sessionID = sessionID;
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
		ret.push('<img id="'+this.closeButtonId+'" alt="close button" src="'+IMAGE_BASEPATH+'/close.gif" style="cursor: default;">');
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
		ret.push('<span style="margin-left: 2em; font-size: 70%"><a id="linkURIAnchor2" href="">',LINK_TO_THIS_PAGE,'</a>:',LINK_TO_THIS_PAGE_MESSAGE,':</span>');
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
		return viewMode == 'a';
	},
	isNoAnswerMode: function(viewMode){
		return viewMode == 'b';
	},
	isMultiAnswerMode: function(viewMode){
		return viewMode == 'c';
	},
		
	onChange: function(eventProducedTargetHandlerId){
		this.setEnabled(false);
		this.consoleHandler.onChange(eventProducedTargetHandlerId);		
		var viewMode = this.getValue('consoleForm', 'v');				
		this.updateQuestionOptionItemState(eventProducedTargetHandlerId, viewMode);
		if(eventProducedTargetHandlerId != 'pagerButton'){
			contentsDispatcher.setNavigationIndex(0);
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
	initialize : function(sessionID, uiHandler, contentsDispatcher, formId, checkboxIdArray, selectIdArray, binarySelectIdArray) {
		this.sessionID = sessionID;
		this.uiHandler = uiHandler;
		this.contentsDispatcher = contentsDispatcher;
		this.formId = formId;
		this.checkboxIdArray = checkboxIdArray;
		this.selectIdArray = selectIdArray;
		this.binarySelectIdArray = binarySelectIdArray;
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
		
		for(var i = 0; i < this.binarySelectIdArray.length; i++){
			var key = this.binarySelectIdArray[i];
			var elem = $(key); 
			this.queryParams += '&' + key + '=' + RLEBinaryArrayUtil.encodeOptions(elem.options);
		}

/*		
		if(consoleHandler.getDensityThreshold() != -1){
			this.queryParams += '&threshold='+consoleHandler.getDensityThreshold();
		}
*/	
		this.queryParams += '&sid='+this.sessionID;
		
		this.queryParams += '&n='+	this.contentsDispatcher.getAnswerItemStartIndex();//change n as navigationIndex?
		this.queryParams += '&N='+	this.contentsDispatcher.getNumMaxAnswerItems();

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
		this.contentsDispatcher.hasChanged(this.uiHandler);
	},
	
	saveTextArea: function(srcElem){
		if(! srcElem.value){
			return;
		}
		document.body.style.cursor = 'wait';
		var params = 'key=' + srcElem.id + '&value=' + srcElem.value+'&sid='+this.sessionID;
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
		var params = 'key=' + srcElem.id + '&value=' + ((srcElem.checked)?'1':'0') + '&threshold=' + densityThreshold + '&sid='+this.sessionID;
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
		contentsDispatcher.setNavigationIndex(contentsDispatcher.navigationIndex - 1);
		this.sendRequest('pagerButton', this.hasChanged.bind(this, ['pagerButton']));
	},
	
	requestNextPage: function(){
		document.body.style.cursor = 'wait';	
		contentsDispatcher.setNavigationIndex(contentsDispatcher.navigationIndex + 1);
		this.sendRequest('pagerButton', this.hasChanged.bind(this, ['pagerButton']));
	}

};
