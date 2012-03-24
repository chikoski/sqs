
/**********************************************************************************/
ContentsFormatter = Class.create();
ContentsFormatter.prototype ={
	initialize: function(){
	},
	
	getStatValue: function(key){
		return contentsHandler.getStatValue(key);
	},
	
	formatTableTitle: function(table){
		return [
		 	'<h2>',
	    	'<img src="'+imagebase+'/'+table.icon+'" alt="'+table.icon+'"/>',
			table.text.escapeHTML(),
			'</h2>'].join('');
	},
	
	formatPageTitle: function(masterIndex, tableIndex, rowIndex, rowIndexByTable, pageIndex, file){
		return [
		'<a onclick="window.open(this.getAttribute(\'href\')); return false;" href="/p/',
		file,
		'?m=',
		masterIndex,
		'&t=',
		tableIndex,
		'&r=',
		rowIndexByTable,
		'&p=',
		pageIndex,
		'"> ',
		 file,
		 '</a>'].join('');
	},
	
	formatRowTitle: function(masterIndex, tableIndex, rowIndex, rowIndexByTable, row){
		var body = ['<hr />', 
			'<h3>', 
			'<img src="'+imagebase+'/'+row.icon+'" alt="'+row.icon+'"/>', 
			'[', rowIndex+1, '] '];

		for(var pageIndex = 0; pageIndex < row.items.length; pageIndex++){
			if(0 < pageIndex){
				body.push(' - ');
			}
			body.push(this.formatPageTitle(masterIndex, tableIndex, rowIndex, rowIndexByTable, pageIndex, row.items[pageIndex]));	
		}
		
		body.push('</h3>');
		return body.join('');
	},

	formatQuestionTitle: function(question){
		var body = [];
		body.push('<h4>');
		body.push('<img src="'+imagebase+'/'+question.icon+'" alt="'+question.icon+'"/>');
		if(question.text){
			body.push(question.text);
		}else{
			body.push(question.label);
		}
		body.push('</h4>');
		return body.join('');
	},
	
	formatQuestionItemTitle: function(question){
		var body = [];
		body.push('<h4>');
		body.push('<img src="'+imagebase+'/'+question.icon+'" alt="'+question.icon+'"/>');
		if(question.text){
			body.push(question.text);
		}else if(question.label){
			body.push(question.label);
		}else if(question.l){
			body.push(question.l);
		}
		body.push('</h4>');
		return body.join('');
	},
	
	get255: function(value, total){
		if(total  !=  0){
			return Math.floor(255*value/total);
		}else{
			return 0;
		}
	},
	
	get_255: function(value, total){
		if(total  !=  0){
			return 255 - Math.floor(255*value/total);
		}else{
			return 255;
		}
	}
	
};

RowContentsFormatter = Class.create();
RowContentsFormatter.prototype = Object.extend(new ContentsFormatter(), {
	initialize: function(){
		ContentsFormatter.prototype.initialize.apply(this);
	},
	
	toHTML: function(){ 
		var body = [];

		var masterIndex = mHandler.selectedIndexLinkedList.firstEntry.value;
		
		var tableIndex = -1;
		var tableTitle = null;
		var selectedTableIndex = 0;
		var selectedRowIndexByTable = 0;
		var rowIndexByTable = 0;
		for(var rowIndex = 0; rowIndex < rHandler.flattenOptionSourceArray.length; rowIndex++){
			
			if(! rHandler.elem.options[rowIndex].selected){
				rowIndexByTable++;
				continue;
			}

			var row = rHandler.flattenOptionSourceArray[rowIndex];
			
			if(tableIndex != row.t){
				if(tableIndex != -1){
					rowIndexByTable = 0;
					selectedRowIndexByTable = 0;
					selectedTableIndex++;
				}
				tableIndex = row.t;
				var table = tHandler.flattenOptionSourceArray[tableIndex];
				tableTitle = this.formatTableTitle(table);
			}

			var rowBody = this.formatRow(masterIndex, tableIndex, selectedTableIndex,
													   rowIndex,  rowIndexByTable, selectedRowIndexByTable, row);
			if(rowBody != '' && tableTitle != null){
				body.push(tableTitle);	
				tableTitle = null;
			}
			body.push(rowBody);
			
			selectedRowIndexByTable++;
			rowIndexByTable++;
		}
		return body.join('');
	}

});



FormAreaFormatter = Class.create();
FormAreaFormatter.prototype = Object.extend(new RowContentsFormatter(), {
	
	initialize: function(consoleHandler){
		RowContentsFormatter.prototype.initialize.apply(this);
		this.consoleHandler = consoleHandler;
	},
	
	isPrintable: function(tableIndex, question, answer){
		return true;
	},
	
	
	getAnswers: function(selectedTableIndex, rowIndexByTable){
		var tableSource = contentsHandler.answerItemSource[selectedTableIndex];
		if(tableSource == null){
			return null;
		}
		return tableSource[rowIndexByTable];
	},
	
	getAnswer: function(answers, selectedQuestionIndex){
		if(answers != null && selectedQuestionIndex < answers.length){
			return answers[selectedQuestionIndex];
		}
		return null;
	},
	
	formatRow: function(masterIndex, tableIndex, selectedTableIndex, rowIndex, rowIndexByTable, selectedRowIndexByTable, row){

		var body = [];
		
		var answers = this.getAnswers(selectedTableIndex, selectedRowIndexByTable);
		if(answers == null){
			//alert("selectedTableIndex:"+selectedTableIndex+", selectedRowIndexByTable:"+selectedRowIndexByTable);
			return '';
		}
		
		var rowTitle = this.formatRowTitle(masterIndex, tableIndex, rowIndex, rowIndexByTable, row);
		
		var answerItemStartIndex = this.consoleHandler.getAnswerItemStartIndex();

		var selectedQuestionIndex = 0;
		
		for(var questionIndex = 0; questionIndex < qHandler.optionSourceArray.length; questionIndex++){
			if(! qHandler.elem.options[questionIndex].selected){
				continue;
			}
			
			var question = qHandler.flattenOptionSourceArray[questionIndex];
			var answer = this.getAnswer(answers, selectedQuestionIndex);
			selectedQuestionIndex++;
					
			if(answer == null){
				continue;
			}

			var isPrintable = this.isPrintable(tableIndex, question, answer);
			
			if(isPrintable){
				var questionBody = this.formatQuestion(answer, masterIndex, tableIndex, rowIndex, questionIndex);
				if( rowTitle != null && questionBody != ''){
					body.push(rowTitle);
					rowTitle = null;
				}
				body.push(questionBody);
			}else{
			}
		}
		
//		alert(selectedRowIndexByTable+":"+body.length);
		
		return body.join('');
	},
	
	isMarked: function(tableIndex, value){
		return (value < contentsHandler.getMarkRecogThreshold(tableIndex));
	},
	
	getNumSelectedItems: function(tableIndex, answer){
		var numSelectedItems = 0;
		if(answer == null || answer.i == null){
			return 0;
		}
		if(answer.M){
			for(var itemIndex = 0; itemIndex < answer.i.length; itemIndex++){
				if(answer.i[itemIndex].M){
					numSelectedItems++;
				}
			}
		}else{
			for(var itemIndex = 0; itemIndex < answer.i.length; itemIndex++){
				if(this.isMarked(tableIndex, answer.i[itemIndex].d)){
			 		numSelectedItems++;
				}
			}
		}
		return numSelectedItems;
	},
	
		
	createResource: function(tableIndex, item, isManualMode, numSelectedItems){
		var isMarked = this.isMarked(tableIndex, item.d);
		var isManualSelected = (item.M == 1);
		var isManualDeSelected = (item.M == 0);
		var isChecked = (isManualMode && isManualSelected) || (! isManualMode && isMarked);   
		var clazzValue = null;
		if(isChecked){
			if(numSelectedItems == null || numSelectedItems == 1){
				clazzValue = 's';
			}else if(! isManualMode && 1 < numSelectedItems){
				clazzValue = 'm';
				isChecked = false;
			}
		}
		if(isManualMode && isManualDeSelected && isMarked){
			clazzValue = '';
		}
		var clazz = null;
		if(clazzValue != null){
			if(isManualMode){
				clazz = 'M'+clazzValue;
			}else{
				clazz = clazzValue;
			}
		}
		return [isChecked, clazz];
	},
	
	formatQuestion: function(answer, masterIndex, tableIndex, rowIndex, questionIndex){
		var body = [];
		

		var question = qHandler.flattenOptionSourceArray[questionIndex];
		
		body.push('<div>');
		body.push(this.formatQuestionTitle(question));
		
		var name = masterIndex+','+tableIndex+','+rowIndex + ',' + questionIndex;

		var isManualMode = answer && (answer.M == 1);
		
		if(question.type == 'select1'){
			body.push('<ul>');
	
			var numSelectedItems = this.getNumSelectedItems(tableIndex, answer);
						
			for(var itemIndex = 0; itemIndex < question.items.length; itemIndex++){
				if(answer.i == null){
					continue;
				}
				var item = answer.i[itemIndex];
				if(item == null){
					continue;
				}
				var resource = this.createResource(tableIndex, item, isManualMode, numSelectedItems);
				var isChecked = resource[0];
				var clazz = resource[1];

				var formArea = question.items[itemIndex];
				var imgsrc = item.s;
				
				body.push(this.formatSelect1(tableIndex, formArea, name, name + ',' + itemIndex, isChecked, clazz, imgsrc, item.d));
			}

			var isNoAnswer = (numSelectedItems == 0); 
			var clazz = null;
			if(isNoAnswer){
				if(isManualMode){
					clazz = 'Mn';
				}else{
					clazz = 'n';
				}
			}
			body.push(this.formatSelect1(tableIndex, {l:NO_ANSWER, v:'-'}, name, name + ',-1',  isNoAnswer, clazz, null, 0)); 
			
			body.push('</ul>');
		}else if(question.type == 'select'){
			body.push('<ul>');
			for(var itemIndex = 0; itemIndex < question.items.length; itemIndex++){
				var formArea = question.items[itemIndex];
				var item = answer.i[itemIndex];
				
				var resource = this.createResource(tableIndex, item, isManualMode, null);
				var isChecked = resource[0];
				var clazz = resource[1];
				
				body.push(this.formatSelect(tableIndex, formArea, name + ',' + itemIndex, isChecked, clazz, item.s, item.d));
			}
			body.push('</ul>');
		}else if(question.type == 'textarea'){
			var formArea = question.items[0];
			var imgsrc = answer.s;
			var value = answer.v;
			body.push(this.formatTextarea(formArea, name, value, imgsrc));
		}
		body.push('</div>');
		return body.join('');
	}
});

ReadOnlyFormAreaFormatter = Class.create();
ReadOnlyFormAreaFormatter.prototype = Object.extend(new FormAreaFormatter(), {
	
	initialize: function(consoleHandler){
		FormAreaFormatter.prototype.initialize.apply(this, [consoleHandler]);
	},

	formatSelect1: function(tableIndex, formArea, name, id, isChecked, clazz, src, density){
		return ['<li', clazz, '>',  formArea.l.escapeHTML(),  '</li>'].join('');
	},
	
	formatSelect: function(tableIndex, formArea, id, isChecked, clazz, src, density){
		return ['<li', clazz, '>',  formArea.l.escapeHTML(),  '</li>'].join('');
	},
	
	formatTextarea: function(formArea, id, value, imgsrc){
		if(value != null){
			return value.escapeHTML();
		}
		return ''; 
	}
});

EditableFormAreaFormatter = Class.create();
EditableFormAreaFormatter.prototype = Object.extend(new FormAreaFormatter(), {
	
	initialize: function(consoleHandler){
		FormAreaFormatter.prototype.initialize.apply(this, [consoleHandler]);
		this.tabIndex = 1;
	},

	formatSelect1: function(tableIndex, formArea, name, id, isChecked, clazz, imgsrc, density){
		var master = mHandler.optionSourceArray[mHandler.selectedIndexLinkedList.firstEntry.value];
		var marginHorizontal = master.markAreaMarginHorizontal * 2;
		var marginVertical = master.markAreaMarginVertical * 2;
		var clazz = (clazz != null)? ' class="'+clazz+'"':'';
		var checked = (isChecked)?' checked="checked"' : '';
		
		var body = ['<li', clazz, '><label for="', id, '">'];
		if(imgsrc != null){
			body.push(['<img src="', imgsrc, '" title="', density, '" alt="{', density, '}" width="', 
			(formArea.w+marginHorizontal), '" height="', (formArea.h+marginVertical), '"/>'].join(''));
		}else{
			body.push(' - ');
		}
		body.push(['<input type="radio" name="', name, '" id="', id, '" onChange="requestHandler.saveMarkArea(this, ', tableIndex, ')" value="',
			formArea.v, '"', checked, ' tabindex="0"/> '].join(''));
		body.push(formArea.v.escapeHTML());
		body.push(' : ');
		body.push(formArea.l.escapeHTML());
		body.push('</label></li>');
		return body.join('');
	},
	
	formatSelect: function(tableIndex, formArea, id, isChecked, clazz, imgsrc, density){
		var master = mHandler.optionSourceArray[mHandler.selectedIndexLinkedList.firstEntry.value];
		var marginHorizontal = master.markAreaMarginHorizontal * 2;
		var marginVertical = master.markAreaMarginVertical * 2;
		var body = [];
		var clazz = (clazz != null)? ' class="'+clazz+'"':'';
		var checked = (isChecked)?' checked="checked"' : '';
		body.push('<li'+clazz+'><label for="'+id+'">');
		body.push('<img src="'+imgsrc+'" title="'+density+'" alt="{'+density+'}" width="'+(formArea.w+marginHorizontal)+'" height="'+(formArea.h+marginVertical)+'"/>');
		body.push('<input type="checkbox" id="'+id+'" value="1" onChange="requestHandler.saveMarkArea(this, '+tableIndex+')"'+checked+' tabindex="0"/> ');
		body.push(formArea.v.escapeHTML());
		body.push(' : ');
		body.push(formArea.l.escapeHTML());		
		body.push('</label></li>');
		return body.join(''); 
	},
	
	
	formatTextarea: function(formArea, id, value, imgsrc){
		var body = [];
		var defaultValue = (value == null)?"":value;
		body.push('<img src="'+imgsrc+'" alt="'+defaultValue+'" width="'+formArea.w+'" height="'+formArea.h+'"/>');
		body.push('<textarea id="'+id+'" onChange="requestHandler.saveTextArea(this)" tabindex="'+(this.tabIndex++)+'">');
		body.push(defaultValue.escapeHTML());
		body.push('</textarea>');
		return body.join(''); 
	}
});



EditableErrorFormAreaFormatter = Class.create();
EditableErrorFormAreaFormatter.prototype = Object.extend(new EditableFormAreaFormatter(), {

	initialize: function(consoleHandler, isNoAnswerErrorMode, isMultiAnswerErrorMode){

		EditableFormAreaFormatter.prototype.initialize.apply(this, [consoleHandler]);

		this.isNoAnswerErrorMode = isNoAnswerErrorMode; 
		this.isMultiAnswerErrorMode = isMultiAnswerErrorMode;
	},
	
	isPrintable: function(tableIndex, question, answer){
		if(question.type != 'select1'){
			return false;
		}
		var numSelectedItems = this.getNumSelectedItems(tableIndex, answer);
		 return (( this.isNoAnswerErrorMode == true && numSelectedItems == 0) || 
		  ( this.isMultiAnswerErrorMode == true && 1 < numSelectedItems)) ;
	}

});
/**********************************************************************************/

PageFormatter = Class.create();
PageFormatter.prototype = Object.extend(new RowContentsFormatter(), {
	
	formatRow: function(masterIndex, tableIndex, selectedTableIndex, rowIndex, rowIndexByTable, selectedRowIndexByTable, row){
		
		var body = [];

		var pages = contentsHandler.errorPageSource[rowIndex];
		if(pages == null){
			return '';
		}

		var pageIDList = row.items;	
		var master = mHandler.optionSourceArray[masterIndex];
		
		for(var pageIDIndex = 0; pageIDIndex < pageIDList.length; pageIDIndex++){
			var pageID = pageIDList[pageIDIndex];
			var pageIndex = pageIDIndex % master.numPages;
			if(pages[pageIndex] == null){
				continue;
			}
			body.push(this.formatPageIDTitle(masterIndex, tableIndex, rowIndex, rowIndexByTable, pageIndex, pageIDIndex, pageID));
			body.push(PAGEERROR_REASON+': '+pages[pageIndex]+'<br/>');
		}
		return body.join('');
	},
	
	
	formatPageIDTitle: function(masterIndex, tableIndex, rowIndex, rowIndexByTable, pageIndex, pageIDIndex, pageID){
		return [ '<h3>[',
		 (rowIndex+1),
		  '] p.',
		   (pageIDIndex+1), 
		   ' : ', 
			this.formatPageTitle(masterIndex, tableIndex, rowIndex, rowIndexByTable, pageIndex, pageID), 
			'</h3>'].join('');
	},
	
	
	formatPage: function(){
		return ''; // TODO: format page image
	}

});

ErrorPageFormatter = Class.create();
ErrorPageFormatter.prototype = Object.extend(new PageFormatter(), {

	formatPage: function(){
		return ''; // TODO: format page image with error
	}

});


/**********************************************************************************/
ExportAnchorFormatter = Class.create();
ExportAnchorFormatter.prototype = Object.extend(new ContentsFormatter(), {
	toHTML: function(){
		var param = requestHandler.createQueryString('consoleForm');
		return  [
			'<a href="/exportXLS?', 
			param, 
			'">',
			'<img style="border:none" src="',
			 imagebase, 
			 '/xls.jpg" alt="[xls]" width="47" height="46"/> ',
			EXPORT_EXCEL_FILE,
			'</a>',
			'<br/>',
			'<a href="/exportCSV?', 
			param, 
			'">',
			'<img style="border:none" src="', 
			imagebase, 
			'/csv.jpg" alt="[csv]" width="45" height="44"/> ',
			EXPORT_TSV_FILE,
			'</a>'].join('');
	}
	
});
