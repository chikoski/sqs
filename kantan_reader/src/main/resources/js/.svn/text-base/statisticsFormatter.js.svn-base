function trim(src){
		if(src == null){
			return '';
		}
		return src.replace(/　+/,' ');
}

function format02d(value, total){
	var rate10000 = Math.floor(10000*value/total);
	var dot2 = rate10000%100;
	if(dot2 < 10){
		dot2 = '0'+dot2;
	}
	return (Math.floor(rate10000/100))+'.'+(dot2);
}

function formatPercent(value, total){
	return ' <span class="per">('+format02d(value, total)+'%)</span>';
}

function brFormatPercent(value, total){
	return '<br/>'+formatPercent(value, total);
}


AbstractChartFormatter = Class.create();
AbstractChartFormatter.prototype = Object.extend(new ContentsFormatter(), {

	initialize: function(contentsHandler){
		this.contentsHandler = contentsHandler;
	},
	
	createSelectedIndexArray: function(){
		return qHandler.getSelectedIndexArray();
	},

	getQuestionSource: function(questionIndex){
		if(questionIndex == -1){
			var selectedIndexArray = tHandler.getSelectedIndexArray();;
			var items = [];
			for(var i = 0; i < selectedIndexArray.length; i++){
				var index = selectedIndexArray[i];
				 var source = tHandler.flattenOptionSourceArray[index];
				items[i] = {icon:'dir.gif',  l: source.text,  v: index};
			}
			return {label:'集計グループ', hints:'', items: items, icon:'dir0.gif', type:'table'};
		}else{
			return qHandler.optionSourceArray[questionIndex];
		}
	}
	

});


SimpleChartFormatter = Class.create();
SimpleChartFormatter.prototype = Object.extend(new AbstractChartFormatter(), {

	initialize: function(contentsHandler){
		AbstractChartFormatter.prototype.initialize.apply(this, [contentsHandler]);
	},
	
	toHTML: function(){
		return this.formatSimpleChart(this.createSelectedIndexArray());
	},
	
	formatSimpleChart: function(selectedIndexArray){
		var body = [];
		
		var length = selectedIndexArray.length;
		for(var i = 0; i < length; i++){
			var selectedQuestionIndex = selectedIndexArray[i];
			var question = this.getQuestionSource(selectedQuestionIndex);
			//var question = qHandler.optionSourceArray[selectedQuestionIndex]; // FIXME: selectedQuestionIndex == -1
			if(question.type != 'textarea'){
				body.push(this.formatSimpleChartTable(selectedQuestionIndex, question));
			}
			if(question.type != 'textarea' || questionIndex == -1){
				body.push(this.formatSimpleChartImage(selectedQuestionIndex, question));
			}
			body.push('<div style="clear:both"></div>');
		}
		return body.join('');
	},
		
	formatSimpleChartImage: function(questionIndex, question){
		var m = mHandler.selectedIndexLinkedList.firstEntry.value;
		var t = DigitArrayValueHandler.encode(tHandler.elem.options);
		var r = DigitArrayValueHandler.encode(rHandler.elem.options);
		var q = DigitArrayValueHandler.encode(qHandler.elem.options);

		var sid = requestHandler.sid;
		var h = 120 + 17 * (question.items.length + 1);
		var viewMode = 3;
		var type = 'bar';
		if(question.type == 'select1'){
			type = 'pie'
		}

		var uri = '/c?type='+type+'&Q='+questionIndex+'&w=350&h='+h+'&v='+viewMode+'&m='+m+'&t='+t+'&r='+r+'&q='+q+'&sid='+sid;
		return '<img class="simpleChartImage" title="'+question.hints+'" src="'+uri+'" alt="chart['+question.hints+']"/>';
	},

	formatSimpleChartTable: function(questionIndex, question){
		var ret = [];
		var total = 0;
		
		if(question.type == 'select1' || question.type == 'select'){
			ret.push(this.formatQuestionTitle(question));
			ret.push('<table border="1" class="simple">');
			ret.push('<tbody>');
			
			for(var itemIndex = 0; itemIndex < question.items.length; itemIndex++){
				var value = this.getStatValue(questionIndex+','+itemIndex);
				total += value;
			}
			
			var na = 0;
			if(question.type == 'select1'){
				var value = this.getStatValue(questionIndex+',-1');
				total += value;
				na = 1;
			}

			var thbgcolor = this.get_255(1,  (question.items.length + na));
			
			for(var itemIndex = 0; itemIndex < question.items.length; itemIndex++){
				
				var formArea = question.items[itemIndex];
				var key = questionIndex+','+itemIndex;
				var value = this.getStatValue(key);

				ret.push('<tr>');
				ret.push('<th style="background-color:rgb('+thbgcolor+',255,'+thbgcolor+')">');
				
				ret.push(trim(formArea.l));
				ret.push('</th>');
				
				var bgcolor = this.get_255(value, total);
				
				ret.push('<td style="background-color:rgb('+bgcolor+',255,'+bgcolor+')">');
				
				//ret.push(key);
				//ret.push('*');
				ret.push(value);
				ret.push(formatPercent(value, total));
				
				ret.push('</td>');
				ret.push('</tr>');
			}
			
			if(question.type == 'select1'){
				var value = this.getStatValue(questionIndex+',-1');
				var bgcolor = this.get_255(value, total);
				
				ret.push('<tr>');
				ret.push('<th style="background-color:rgb('+thbgcolor+',255,'+thbgcolor+')">');
				ret.push('無回答');
				ret.push('</th>');			
				ret.push('<td style="background-color:rgb('+bgcolor+',255,'+bgcolor+')">');
				ret.push(value);
				ret.push(formatPercent(value, total));
				ret.push('</td>');
				ret.push('</tr>');
			}
			
			ret.push('<tr>');
			ret.push('<th class="sum">');
			ret.push('合計');
			ret.push('</th>');
			
			var bgcolor = (total == 0)? 255:this.get_255(total, total);
			ret.push('<td  class="sum" style="background-color:rgb('+bgcolor+',255,'+bgcolor+')">');
			
			ret.push(total);
			ret.push(' <span class="per">(100%)</spam></td>');
			
			ret.push('</tr>');
			ret.push('</tbody>');
			ret.push('</table>');
		}
		
		return ret.join('');
	}
		
});



CrossChartFormatter = Class.create();
CrossChartFormatter.prototype = Object.extend(new AbstractChartFormatter(), {
	initialize: function(contentsHandler){
		AbstractChartFormatter.prototype.initialize.apply(this);
		if(contentsHandler != null){
			this.axis = contentsHandler.axis;
		}
	},
	
	createKeyFunction: function(keyArray){
		return function(values){
			var ret = [];
			if(keyArray.length == 2){
				ret.push(values[keyArray[0]]);
				ret.push(values[keyArray[1]]);
			}else if(keyArray.length == 3){
				ret.push(values[keyArray[0]]);
				ret.push(values[keyArray[1]]);
				ret.push(values[keyArray[2]]);
			}else if(keyArray.length == 4){
				ret.push(values[keyArray[0]]);
				ret.push(values[keyArray[1]]);
				ret.push(values[keyArray[2]]);
				ret.push(values[keyArray[3]]);
			}else{
				throw("ERROR: createKeyFunction param.length == 2 or 3 : "+keyArray.length);
			}
			return ret.join(',');
		}
	},
	
	toHTML: function(){
		var selectedIndexArray = this.createSelectedIndexArray()
		if(2 == selectedIndexArray.length){
			return this.doubleCrossChart(selectedIndexArray);
		}else if(3 == selectedIndexArray.length){
			return this.tripleCrossChart(selectedIndexArray);
		}else{
			return '';
		}
	},
	
	doubleCrossChart: function(selectedIndexArray){
		var body = '';
 		body += '<div class="doubleChartTable" style="z-index: 2;">'; 
		body += '<select style="width:12em;" id="axis" onChange="contentsHandler.setCrossTableAxis(this.value);">';
		var keyArray = new Array(0, 1, 2);
		var orderedSelectedIndexArray = [selectedIndexArray[0], selectedIndexArray[1], selectedIndexArray[2]];
 		var questionArray = []; //question0, question1];

 		for(var i = 0; i < 2; i++){
			for(var j = 0; j < 2; j++){
				if(i != j){
					var value = i+','+j;
					var selected = '';
					if(value == this.axis){
						selected=' selected="selected"';
						orderedSelectedIndexArray[0] = selectedIndexArray[i];
						orderedSelectedIndexArray[1] = selectedIndexArray[j];
						keyArray[0] = 0;
						keyArray[i+1] = 1;
						keyArray[j+1] = 2;
						
						questionArray[0] = this.getQuestionSource(orderedSelectedIndexArray[0]);
						questionArray[1] = this.getQuestionSource(orderedSelectedIndexArray[1]);
						
					}
					body += '<option value="'+value+'"'+selected+'>';
					body += this.getQuestionSource(selectedIndexArray[i]).label;
					body += ' \\ ';
					body += this.getQuestionSource(selectedIndexArray[j]).label;
					body += ' </option>';
				}
			}
		}
		body += '</select>';
		var question0 = this.getQuestionSource(orderedSelectedIndexArray[0]);
		var question1 = this.getQuestionSource(orderedSelectedIndexArray[1]);
 		var questionArray = [question0, question1];
 		
		body += this.formatDoubleChartTable('', '',  questionArray, this.createKeyFunction(keyArray));
		body += '</div>';
		body += this.formatDoubleChartImage(questionArray);
		body += '<div style="clear:both"></div>';
		return body;
	},
	
	formatDoubleChartImage: function(questionArray){
		var m = mHandler.selectedIndexLinkedList.firstEntry.value;
		var t = DigitArrayValueHandler.encode(tHandler.elem.options);
		var r = DigitArrayValueHandler.encode(rHandler.elem.options);
		var q = DigitArrayValueHandler.encode(qHandler.elem.options);
		var sid = requestHandler.sid;
		var w;
		var h;
		var viewMode = 5;
		
	 	h = 120 + 40 * (questionArray[0].items.length + 1);
	 	w = 220 + 40 * (questionArray[1].items.length + 1);
		
		var title = questionArray[0].hints+' x '+questionArray[1].hints;
		var uri = '/c?type=bar&w='+w+'&h='+h+'&v='+viewMode+'&axis='+this.axis+'&m='+m+'&t='+t+'&r='+r+'&q='+q+'&sid='+sid;
		return '<img class="crossChartImage" title="'+title+'" src="'+uri+'" alt="chart['+title+']"/>';
	},
	

	tripleCrossChart: function(selectedIndexArray){
		var body = '';
		body += '<select style="width:18em;" id="axis" onChange="contentsHandler.setCrossTableAxis(this.value);">';
		var keyArray = new Array(0, 1, 2);
		var orderedSelectedIndexArray = [selectedIndexArray[0], selectedIndexArray[1], selectedIndexArray[2]];
		for(var i = 0; i < 3; i++){
			for(var j = 0; j < 3; j++){
				for(var k = 0; k < 3; k++){
					if(i != j && i != k && j != k){
						var value = i+','+j+','+k;
						var selected = '';
						if(value == this.axis){
					 	 	selected=' selected="selected"';
							orderedSelectedIndexArray[0] = selectedIndexArray[i];
							orderedSelectedIndexArray[1] = selectedIndexArray[j];
							orderedSelectedIndexArray[2] = selectedIndexArray[k];
							keyArray[i] = 0;
							keyArray[j] = 1;
							keyArray[k] = 2;
						}
						body += '<option value="'+value+'"'+selected+'>';
						body += this.getQuestionSource(selectedIndexArray[i]).label;
						body += ' : ';
						body += this.getQuestionSource(selectedIndexArray[j]).label;
						body += ' \\ ';
						body += this.getQuestionSource(selectedIndexArray[k]).label;
						body += ' </option>';
					}
				}
			}
		}
		body += '</select>';

		var question0 = this.getQuestionSource(orderedSelectedIndexArray[0]);
		var question1 = this.getQuestionSource(orderedSelectedIndexArray[1]);
		var question2 = this.getQuestionSource(orderedSelectedIndexArray[2]);
		
		body += this.formatQuestionTitle(question0);
		
		var len = question0.items.length;
		for(var i = 0; i < len; i++){
			var label0 = trim(question0.items[i].l);
			var key = i;
			body += this.formatDoubleChartTable(label0, key,  [question0, question1, question2], 
				this.createKeyFunction(keyArray));
			body += '<hr style="margin: 0.4em;"/>';
		}
		if(this.hasNoAnswerItem(orderedSelectedIndexArray[0])){
			var func;
			var key = -1;
			body += this.formatDoubleChartTable('無回答',  key,  [question0, question1, question2], 
				this.createKeyFunction(keyArray));
		}
		
		return body;
	},
		
	isGroupModeSelected: function(index){
		return index == -1;
	},

	isGroupModeQuestion: function(question){
		return question.text == null;
	},
	
	hasNoAnswerItem: function(question){
		return (! this.isGroupModeQuestion(question)) && question.type == 'select1' ;
	},

	formatDoubleChartTable: function(title, key, questions, func){
		var ret = [];
		var question0;
		var question1;

		
		if(questions.length == 2){
			question0 = questions[0];
			question1 = questions[1];
		}else{
			question0 = questions[1];
			question1 = questions[2];
		}
		
		try{
			if(question0.type == 'textarea' || question1.type == 'textarea'){
				return '';
			}
		}catch(ignore){
		}

		var na0 = this.hasNoAnswerItem(question0)?1:0;
		var na1 = this.hasNoAnswerItem(question1)?1:0;
		
		var total = 0;
		var colTotal = [];
		var rowTotal = [];
		var naColTotal = 0;
		
		ret.push('<table border="1" class="cross">');
		ret.push('<thead>');
			
		ret.push('<tr>');
		ret.push('<th class="e" colspan="2" rowspan="2">'+title+'</th>');
		ret.push('<th  class="l" colspan="'+(question1.items.length + na1 + 1)+'">');
		ret.push(trim(question1.label.escapeHTML()));
		ret.push(trim(question1.hints.escapeHTML()));
		ret.push('</th>');
		ret.push('</tr>');
		ret.push('<tr>');
		
		var thbgcolor0 = this.get_255(1,  (question0.items.length + na0));
		var thbgcolor1 = this.get_255(1,  (question1.items.length + na1));

		for(var itemIndex1 = 0; itemIndex1 < question1.items.length; itemIndex1++){
			var formArea = question1.items[itemIndex1];
			ret.push('<th class="i" style="background-color:rgb('+thbgcolor1+',255,'+thbgcolor1+')">');
			ret.push(trim(formArea.l));
			ret.push('</th>');
			colTotal[itemIndex1] = 0;
		}
		
		if(na1 == 1){
			ret.push('<th class="na" style="background-color:rgb('+thbgcolor1+',255,'+thbgcolor1+')">');
			ret.push('無回答');
			ret.push('</th>');
		}
		
		ret.push('<th class="sum">');
		ret.push('合計');
		ret.push('</th>');
		ret.push('</tr>');
		
		ret.push('</thead>');
		ret.push('<tbody>');
		
		
		for(var itemIndex0 = 0; itemIndex0 < question0.items.length; itemIndex0++){
			rowTotal[itemIndex0] = 0;
			for(var itemIndex1 = 0; itemIndex1 < question1.items.length; itemIndex1++){
				var value = this.getStatValue(func([key, itemIndex0, itemIndex1]));
				rowTotal[itemIndex0] += value;
			}
			
			if(na1 == 1){
				var value = this.getStatValue(func([key, itemIndex0, '-1']));
				rowTotal[itemIndex0] += value;
			}
			total += rowTotal[itemIndex0];
		}
		if(na0 == 1){
			for(var itemIndex1 = 0; itemIndex1 < question1.items.length; itemIndex1++){
				var value = this.getStatValue(func([key, '-1', itemIndex1]));
				total += value;
			}
			if(na1 == 1){
				var value = this.getStatValue(func([key, '-1', '-1']));
				total += value;
			}
		}
		
		
		for(var itemIndex0 = 0; itemIndex0 < question0.items.length; itemIndex0++){
			var formArea = question0.items[itemIndex0];

			ret.push('<tr>');

			if(itemIndex0 == 0){
				ret.push('<th  class="l" rowspan="'+(question0.items.length + na0 + 1)+'">');
				ret.push(trim(question0.label.escapeHTML()));
				ret.push(trim(question0.hints.escapeHTML()));
				ret.push('</th>');
			}
			ret.push('<th class="i" style="background-color:rgb('+thbgcolor0+',255,'+thbgcolor0+')">');
			ret.push(trim(formArea.l));
			ret.push('</th>');
					
			
			for(var itemIndex1 = 0; itemIndex1 < question1.items.length; itemIndex1++){
				var formArea = question1.items[itemIndex1];
				var statKey = func([key, itemIndex0, itemIndex1]);
				var value = this.getStatValue(statKey);

				var bgcolor = this.get_255(value, total);
				
				ret.push('<td style="background-color:rgb(255,'+bgcolor+','+bgcolor+')">');
				ret.push(value);
				ret.push(brFormatPercent(value, total));
				
				colTotal[itemIndex1] += value;
				ret.push('</td>');
			}
			
			if(na1 == 1){
				var value = this.getStatValue(func([key, itemIndex0, '-1']));
				var bgcolor = this.get_255(value, total);
				ret.push('<td class="na" style="background-color:rgb(255,'+bgcolor+','+bgcolor+')">');
				ret.push(value);
				ret.push(brFormatPercent(value, total));
				naColTotal += value;
				ret.push('</td>');
			}
			
			var value = rowTotal[itemIndex0];
			var bgcolor = this.get_255(value, total);
			ret.push('<td class="sum" style="background-color:rgb('+bgcolor+',255,'+bgcolor+')">');
			ret.push(value);
			ret.push(brFormatPercent(value, total));
			ret.push('</td>');
			ret.push('</tr>');
		}

		var rowTotalNA = 0;
		
		if(na0 == 1){
			ret.push('<tr>');
			ret.push('<th  class="na" style="background-color:rgb('+thbgcolor0+',255,'+thbgcolor0+')">');
			ret.push('無回答');
			ret.push('</th>');
			for(var itemIndex1 = 0; itemIndex1 < question1.items.length; itemIndex1++){
				var formArea = question1.items[itemIndex1];
				var value = this.getStatValue(func([key, '-1', itemIndex1]));
				var bgcolor = this.get_255(value, total);
				ret.push('<td class="na" style="background-color:rgb(255,'+bgcolor+','+bgcolor+')">');
				ret.push(value);
				ret.push(brFormatPercent(value, total));
				rowTotalNA += value;
				colTotal[itemIndex1] += value;
				ret.push('</td>');
			}
			
			if(na1 == 1){
				var value = this.getStatValue(func([key, '-1', '-1']));
				var bgcolor = this.get_255(value, total);
				ret.push('<td class="na" style="background-color:rgb(255,'+bgcolor+','+bgcolor+')">');
				ret.push(value);
				ret.push(brFormatPercent(value, total));
				rowTotalNA += value;
				naColTotal += value;
				ret.push('</td>');
			}
			
			var value = rowTotalNA;
			var bgcolor = this.get_255(value, total);
			ret.push('<td class="na" style="background-color:rgb(255,'+bgcolor+','+bgcolor+')">');
			ret.push(value);
			ret.push(brFormatPercent(value, total));
			ret.push('</td>');
			ret.push('</tr>');
		}
		
		var rowTotalTotal = 0;
		ret.push('<tr>');
		ret.push('<th class="sum">');
		ret.push('合計');
		ret.push('</th>');
		for(var itemIndex1 = 0; itemIndex1 < question1.items.length; itemIndex1++){
			var formArea = question1.items[itemIndex1];
			var value = colTotal[itemIndex1];
			var bgcolor = this.get_255(value, total);
			ret.push('<td class="sum" style="background-color:rgb('+bgcolor+',255,'+bgcolor+')">');
			ret.push(value);
			ret.push(brFormatPercent(value, total));
			rowTotalTotal += value;
			ret.push('</td>');
		}
		
		if(na1 == 1){
			var value = naColTotal;
			var bgcolor = this.get_255(value, total);
			ret.push('<td class="sum" style="background-color:rgb('+bgcolor+',255,'+bgcolor+')">');
			ret.push(naColTotal);
			ret.push(brFormatPercent(value, total));
			ret.push('</td>');
		}
		
		var bgcolor = (total == 0)? 255:this.get_255(total, total);
		ret.push('<td  class="sum" style="background-color:rgb('+bgcolor+',255,'+bgcolor+')">');
		ret.push(rowTotalTotal + naColTotal);
		ret.push('<br/><span class="per">(100%)</span>'); 
		ret.push('</td>');
		ret.push('</tr>');
		
		ret.push('</tbody>');
		ret.push('</table>');
		
					
	return ret.join('');
}

});


GroupSimpleChartFormatter = Class.create();
GroupSimpleChartFormatter.prototype = Object.extend(new SimpleChartFormatter(), {
	initialize: function(contentsHandler){
		SimpleChartFormatter.prototype.initialize.apply(this, [contentsHandler]);
	},
	createSelectedIndexArray: function(){
		return [-1, qHandler.getSelectedIndexArray()].flatten();
	}			
});

GroupCrossChartFormatter = Class.create();
GroupCrossChartFormatter.prototype = Object.extend(new CrossChartFormatter(), {
	
	initialize: function(contentsHandler){
		CrossChartFormatter.prototype.initialize.apply(this, [contentsHandler]);
	},
	
	createSelectedIndexArray: function(){
		return [-1, qHandler.getSelectedIndexArray()].flatten();
	},			
	
	formatChart: function(selectedIndexArray){
		if(2 == selectedIndexArray.length){
			return this.doubleCrossChart(selectedIndexArray);
		}else if(3 == selectedIndexArray.length){
			return this.tripleCrossChart(selectedIndexArray);
		}else{
			return '';
		}
	}			
	
});

CrossChartListFormatter = Class.create();
CrossChartListFormatter.prototype = Object.extend(new CrossChartFormatter(), {
	
	initialize: function(contentsHandler){
		CrossChartFormatter.prototype.initialize.apply(this, [contentsHandler]);
	},

	toHTML: function(){
		var MAX_SELECTABLE_QUESTIONS = 4;
		var selectedIndexArray = qHandler.getSelectedIndexArray();
		var body = [];
		for(var i = 0; i < qHandler.optionSourceArray.length; i++){
			var question0 = qHandler.optionSourceArray[i];
			for(var j = 0; j < selectedIndexArray.length && j <= MAX_SELECTABLE_QUESTIONS; j++){
				if(i == selectedIndexArray[j]){
					continue;
				}
				var question1 = qHandler.optionSourceArray[selectedIndexArray[j]];
				var key =  i+','+j;
				body.push(this.formatDoubleChartTable('', key,  [key, question0, question1], this.createKeyFunction([0, 1, 2])));
			}
			body.push('<hr style="margin: 0.4em;"/>');
		}
		return body.join('');
	}
	
});

GroupCrossChartListFormatter = Class.create();
GroupCrossChartListFormatter.prototype = Object.extend(new CrossChartListFormatter(), {
	initialize: function(contentsHandler){
		CrossChartListFormatter.prototype.initialize.apply(this, [contentsHandler]);
	},
	
	createSelectedIndexArray: function(){
		return [-1, qHandler.getSelectedIndexArray()].flatten();
	},

	toHTML: function(){

		var selectedIndexArray = qHandler.getSelectedIndexArray();
		
		var body = [];
		var tableGroup = this.getQuestionSource(-1);
		body.push(this.formatQuestionTitle(tableGroup));
		
		for(var i = 0; i < tableGroup.items.length; i++){
			var table = tableGroup.items[i];
			var key = i;
			body.push(this.formatQuestionItemTitle(table));			
			for(var j = 0; j < selectedIndexArray.length; j++){
				var index = selectedIndexArray[j];
				var question1 = this.getQuestionSource(index);
				body.push(this.formatSimpleChartTable(key+','+index, question1));
			}
			
			body.push('<hr style="margin: 0.4em;"/>');
		}
		return body.join('');
	}
});

