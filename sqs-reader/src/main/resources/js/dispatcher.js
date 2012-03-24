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

ContentsDispatcher = Class.create();
ContentsDispatcher.prototype = {
	initialize: function(sessionID, consoleFormId, contentsPanelId, imagebase){
		this.sessionID = sessionID;
		this.consoleFormId = consoleFormId; 
		this.contentsPanelId = contentsPanelId;
		this.answerItemSource = null;
		this.navigationIndex = 0;
		this.numMaxAnswerItems = 20;
		this.statValues = {};
		this.pageStartIndexArray = [];
		this.numPages = this.pageStartIndexArray.length;
		this.imagebase = imagebase;
	},
	
	getImageBase: function(){
		return this.imagebase;
	},
	
	getNavigationIndex: function(){
		return this.navigationIndex;
	},
	
	setNavigationIndex: function(index){
		this.navigationIndex = index;
	},
	
	getNumPages: function(){
		return this.pageStartIndexArray.length;
	},
	
	getPageTitle: function(offset){
		return (offset + 1+this.navigationIndex)+' / '+(this.getNumPages());
	},
	
	getAnswerItemStartIndex: function(){
		if(this.navigationIndex < this.pageStartIndexArray.length){
			return this.pageStartIndexArray[this.navigationIndex];
		}else{
			//throw "index out of range:"+ this.navigationIndex +'<'+ this.pageStartIndexArray.length;
			return 0;
		}
	},
	
	getNumMaxAnswerItems: function(){
		return this.numMaxAnswerItems;
	},
	
	isMultiPageContents: function(){
		return 1 < this.getNumPages();
	},
		
	setPagerVisible: function(isVisible, viewMode){
		if( isVisible && this.isFormAreaMode(viewMode)){
			var prevTitle = '';
			var nextTitle = '';
			if(0 < this.navigationIndex){
				prevTitle = this.getPageTitle(-1) +"　&lt;-　";
				$('prevPageButton').style.display = 'block';
			}else{
				$('prevPageButton').style.display = 'none';
			}
			
			if(this.navigationIndex + 1 < this.getNumPages()){
				nextTitle =  "　-&gt;　"+this.getPageTitle(+1);
				$('nextPageButton').style.display = 'block';
			}else{
				$('nextPageButton').style.display = 'none';
			}

			$('pageTitle').innerHTML =  "&lt;&lt;"+prevTitle + this.getPageTitle(0) + nextTitle+ "&gt;&gt;";
		}else{
			$('prevPageButton').style.display = 'none';
			$('nextPageButton').style.display = 'none';
		}

	},

	/*
	hasChanged: function(){
		return $(this.contentsPanelId).innerHTML = ["<h1>hoge</h1>"];
	},
	*/
	
	hasChanged: function(){
 	
 	//$('logger').innerHTML=''; 	
	var contents = [];
	try{
		var viewMode = this.getValue(this.consoleFormId, 'v');

		this.setPagerVisible(this.isMultiPageContents(), viewMode);
		if(typeof(viewMode) == 'undefined'){
			$(this.contentsPanelId).innerHTML = OVERVIEW_HOWTO_MESSAGE.join('')+"<hr/>";
			return;
		}
		//export
		if(this.isFormEditMode(viewMode)){
			
			var body = '';
			
			if(this.isSelected(['t','r','q'])){
				body = new EditableFormAreaFormatter(this).toHTML();
				if(body != ''){
					contents.push(body);
					contents.push('<hr/>');
				}
			}else{
				contents.push(FORM_EDIT_MODE_HOWTO_MESSAGE);
			}
			
		}else if(this.isExportMode(viewMode)){
			contents.push('<h2>',EXPORT_SPREADSHEET_LABEL,'</h2>');
			var body = '';
			if(this.isSelected(['t'])){
				body = new ExportAnchorFormatter().toHTML();
			}
			if(body != ''){
				contents.push(body); 
			}else{
				contents.push(EXPORT_SPREADSHEET_HOWTO_LABEL)
			}
			contents.push("<hr/>");
			
		}else if(this.isSimpleChartViewMode(viewMode)){
			contents.push('<h2>'+SIMPLE_STATISTICS_LABEL+'</h2>');

			var body = '';			
			if(this.isSelected(['t','r','q'])){
				body = new SimpleChartFormatter(this).toHTML();
			}			
			if(body != ''){
				contents.push(body);
			}else{
				contents.push(SIMPLE_STATISTICS_HOWTO_LABEL);				
			}
			contents.push("<hr/>");
			
		}else if(this.isGroupSimpleChartViewMode(viewMode)){
			contents.push('<h2>'+GROUPED_STATISTICS_DETAIL_LABEL+'</h2>');
			
			var body = '';
			if(this.isSelected(['t','r','q'])){
				body = new GroupSimpleChartFormatter(this).toHTML();
			}
			
			if(body != ''){
				contents.push(body);
			}else{
				contents.push(GROUP_STATISTICS_HOWTO_LABEL);
			}
			contents.push("<hr/>");
			
		}else if(this.isCrossChartViewMode(viewMode)){
			contents.push('<h2>',CROSS_TABULAR_LABEL,'</h2>');

			var body = '';
			if(this.isSelected(['t','r','q'])){
				body = new CrossChartFormatter(this).toHTML();
			}
			
			if(body != ''){
				contents.push(body);
			}else{
				contents.push(CROSS_TABULAR_HOWTO_LABEL);
			}
			contents.push("<hr/>");
			
		}else if(this.isGroupCrossChartViewMode(viewMode)){
			contents.push('<h2>'+CROSS_TABULAR_BY_GROUP_DETAIL_LABEL+'</h2>');

			var body = '';
			if(this.isSelected(['t','r','q'])){
				body = new GroupCrossChartFormatter(this).toHTML();
			}
			
			if(body != ''){
				contents.push(body);
			}else{
				contents.push(CROSS_TABULAR_BY_GROUP_HOWTO_LABEL);
			}
			contents.push("<hr/>");
			
		}else if(this.isCrossChartListViewMode(viewMode)){
			contents.push('<h2>',CROSS_TABULAR_LIST_DETAIL_LABEL,'</h2>');

			var body = '';
			if(this.isSelected(['t','r','q'])){
				body = new CrossChartListFormatter(this).toHTML();
			}
			
			if(body != ''){
				contents.push(body);
			}else{
				contents.push(CROSS_TABULAR_LIST_HOWTO_LABEL);
			}
			contents.push("<hr/>");
			
		}else if(this.isErrorPageViewMode(viewMode)){
			
			contents.push('<h2>'+PAGE_ERROR_DETAIL_LABEL+'</h2>');
			
			var body = '';
			if(this.isSelected(['t','r'])){
				body = new ErrorPageFormatter(this).toHTML();
				if(body != ''){
					contents.push(PAGE_ERROR_HOWTO_LABEL_1);
					contents.push(body); 
				}
			}else{
				contents.push(PAGE_ERROR_HOWTO_LABEL_2);
			}
			contents.push("<hr/>");
			
		}else if(this.isLowReliabilityViewMode(viewMode)){
			contents.push('<h2>'+ LOW_CONFIDENCE_DETAIL_LABEL+ '</h2>');
			contents.push('<p>'+UNDER_CONSTRUCTING_LABEL+'</p>'); 
			contents.push("<hr/>");
		}else if(this.isNoAnswerErrorViewMode(viewMode)){
			contents.push('<h2>'+NO_ANSWER_ERROR_DETAIL_LABEL+'</h2>');
			var body = '';

			if(this.isSelected(['t','r','q'])){
				body = new EditableErrorFormAreaFormatter(this, true, false).toHTML();
				if(body != ''){
					contents.push(body);
				}else{
					contents.push(NO_MATCHED_ITEM);
				}
			}else{
				contents.push(NO_ANSWER_ERROR_HOWTO_LABEL);
			}
			contents.push("<hr/>");
			
		}else if(this.isMultiAnswerErrorViewMode(viewMode)){			
			contents.push('<h2>'+MULTIPLE_ANSWER_ERROR_DETAIL_LABEL+'</h2>');
			var body = '';
			if(this.isSelected(['t','r'])){
				body = new EditableErrorFormAreaFormatter(this, false, true).toHTML();
				if(body != ''){
					contents.push(body);
				}else{
					contents.push(NO_MATCHED_ITEM);
				}
			}else{
				contents.push(MULTIPLE_ANSWER_ERROR_HOWTO_LABEL);
			}
			contents.push("<hr/>");
		}
		
		$(this.contentsPanelId).innerHTML = contents.join(''); 
		}catch(ignore){
			console.trace();
			alert("ERROR:"+ignore);
		}

	},
	
	getStatValue: function(key){
		var value = this.statValues[key];
		if(value != null){
			return value;
		}else{
			return 0;
		}
	},
	
	getValue: function(formId, paramName){
		var serializeTarget = Form.serialize($(formId));
		return serializeTarget.toQueryParams()[paramName];
	},
	
	getMarkRecogThreshold: function(tableIndex){
		return consoleHandler.getDensityThreshold(tableIndex);
	},
	
	setCrossTableAxis: function(axis){
		this.axis = axis;
		this.hasChanged();
		requestHandler.updateCurrentURI('axis'); 
	},
	
	isSelected: function(arr){
		for(var i = 0; i < arr.length; i++){
			var f = $F(arr[i]);
			if( f == null || f == ''){
				return false;
			}
		}
		return true;
	},
	
	isFormEditMode: function(viewMode){
		return viewMode == '0';
	},
	
	isStatMode: function(viewMode){
		return (
			viewMode == '2' || 
			viewMode == '3' || 
			viewMode == '4' || 
			viewMode == '5' || 
			viewMode == '6' || 
			viewMode == '7' );
	},
	
	isFormAreaMode: function(viewMode){
		return (
			viewMode == '0' || 
			viewMode == '10' || 
			viewMode == '11' || 
			viewMode == '12' );
	},
	
	isExportMode: function(viewMode){
		return viewMode == '2';
	},
	
	isSimpleChartViewMode: function(viewMode){
		return viewMode == '3';
	},
	
	isGroupSimpleChartViewMode: function(viewMode){
		return viewMode == '4';
	},
	
	isCrossChartViewMode: function(viewMode){
		return viewMode == '5';
	},
	
	isGroupCrossChartViewMode: function(viewMode){
		return viewMode == '6';
	},
	
	isCrossChartListViewMode: function(viewMode){
		return viewMode == '7';
	},
	
	isErrorPageViewMode: function(viewMode){
		return viewMode == '9';
	},
	
	isLowReliabilityViewMode: function(viewMode){
		return viewMode == '10';
	},
	
	isNoAnswerErrorViewMode: function(viewMode){
		return viewMode == '11';
	},
	
	isMultiAnswerErrorViewMode: function(viewMode){
		return viewMode == '12';
	}

};
