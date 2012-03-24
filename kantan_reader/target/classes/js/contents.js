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

NO_ANSWER = ' (無回答) ';
PAGEERROR_REASON = '原因';
EXPORT_EXCEL_FILE = 'Excelファイルの書き出し';
 EXPORT_TSV_FILE = 'タブ区切りテキストファイルの書き出し';
 
ContentsHandler = Class.create();
ContentsHandler.prototype = {
	initialize: function(consoleFormId, contentsPanelId){
	
		this.consoleFormId = consoleFormId; 
		this.contentsPanelId = contentsPanelId;
		this.answerItemSource = null;
		this.navigationIndex = 0;
		this.numMaxAnswerItems = 20;
		this.statValues = {};
		this.pageStartIndexArray = [];
		this.numPages = this.pageStartIndexArray.length;
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
				prevTitle = this.getPageTitle(-1) +"　←　";
				$('prevPageButton').style.display = 'block';
			}else{
				$('prevPageButton').style.display = 'none';
			}
			
			if(this.navigationIndex + 1 < this.getNumPages()){
				nextTitle =  "　→　"+this.getPageTitle(+1);
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
	
	
 	hasChanged: function(){
 	
 	//$('logger').innerHTML='';
 	
	var contents = [];
	try{
		var imagePath = '/jar/image/';
		var viewMode = this.getValue(this.consoleFormId, 'v');
		this.setPagerVisible(this.isMultiPageContents(), viewMode);

		if(typeof(viewMode) == 'undefined'){
			$(this.contentsPanelId).innerHTML = 
			 [	'<h1><img src="'+imagePath+'omr.gif" width="64" height="64">SQS MarkReader: Resultブラウザ</h1>',
				'<h2>操作方法の概要</h2>',
				'<h3>1.　コンソールの表示</h3>',
				'<ul><li>画面上部の紺色のバーの部分にマウスを重ねると、「コンソール」が表示されます。</li>',
			 	'<li>マウスを「コンソール」から外に出すと、「コンソール」は自動的に隠れます。</li>',
				'<li>「コンソール」のタイトル表示部分をマウスで左クリックすると、「コンソールを自動的に隠す」「コンソールを常時表示」を切り替えできます。</li></ul>',
				'<h3>2.　表示メニューからの選択</h3>',
				'<ul><li>「■表示」のメニューの、以下の項目から、利用したい機能を選択してください。',
				'<dl>',
				'<dt><img src="'+imagePath+'pencil.png">回答の閲覧/修正</dt>',
				'<dd>マーク欄・自由記述欄の回答内容を表示します。<br/>マーク欄の内容は、ラジオボタン/チェックボックスによって修正できます。<br/>自由記述欄の内容は、テキストエリアを用いて文字に書き起こすことができます。</dd>',
				'<hr/>',
				'<dt><img src="'+imagePath+'page_excel.png">表データ書き出し</dt>',
				'<dd>表データの書き出しをします。形式は「Excel」「タブ区切りテキスト」の2種類から選ぶことができます。</dd>',
				'<dt><img src="'+imagePath+'chart_bar.png">単純集計</dt>',
				'<dd>単純集計結果とそれに対応する「棒グラフ」を表示します。</dd>',
				'<dt><img src="'+imagePath+'chart_bar_add.png">グループ単純集計</dt>',
				'<dd>「■集計グループ」として複数個の項目が選択されている場合に、それぞれの集計グループごとで「単純集計」を表示します。</dd>',
				'<dt><img src="'+imagePath+'table.png">クロス集計</dt>',
				'<dd>「■設問」が2個または3個選択されている場合に、それらをクロス集計した内容を表示します。</dd>',
				'<dt><img src="'+imagePath+'table_add.png">グループクロス集計</dt>',
				'<dd>「■集計グループ」が複数項目と、「■設問」が1個または2個選択されている場合に、それらをクロス集計した内容を表示します。</dd>',
				'<dt><img src="'+imagePath+'table_multiple.png">クロス集計一覧</dt>',
				'<dd>選択された「■設問」と、それ以外の(選択されていない)設問を、総当たり方式でクロス集計した内容を表示します。</dd>',
				'<hr/>',
				'<dt><img src="'+imagePath+'exclamation.png">読み取り失敗</dt>',
				'<dd>セッション中に読み取りに失敗した画像ファイルを抽出して表示します</dd>',
				'<dt><img src="'+imagePath+'icon_alert_cyan.gif">信頼度低</dt>',
			 	'<dd>マーク式回答のうち、塗りつぶしの認識の閾値設定に関連して「認識結果の信頼度が低いもの」を抽出して表示します。</dd>',
			 	'<dt><img src="'+imagePath+'icon_alert.gif">無回答</dt>',
				'<dd>択一選択式設問のうち、「無回答エラーのもの」を抽出して表示します。</dd>',
				'<dt><img src="'+imagePath+'icon_alert_orange.gif">重複回答</dt>',
				'<dd>択一選択式設問のうち、「重複回答エラーのもの」を抽出して表示します。</dd>',
				'</dl>',
				'</li></ul>',
				'<h3>3.　表示内容の選択</h3>',
				'<p>コンソール内の4つリストボックスから、表示したい内容を選択してください。</p>',
				'<p>※複数選択をするときには、選択範囲のドラッグ、選択範囲の始点・終点でのShift+クリック、選択トグル項目のCtrl+クリックをしてください。</p>',
				'<ul>',
				'<li>「■調査票」から、Sourceフォルダ内から抽出処理された「調査票」をひとつ選んでください。</li>',
				'<li>「■集計グループ」から、Sourceフォルダ内で回答用紙のスキャン画像を保存したフォルダ構造に対応した「集計グループ」を選んでください(複数選択可)。</li>',
				'<li>「■行」から、選択されている「集計グループ」内の、回答者(表の行)を選んでください(複数選択可)。</li>',
				'<li>「■設問」から、選択されている「調査票」内の、設問(表の列)を選んでください(複数選択可)。</li>',
				'</ul>'
				].join('');
			return;
		}

		//export
		if(this.isFormEditMode(viewMode)){
			if(this.isSelected(['t','r','q'])){
				contents.push(new EditableFormAreaFormatter(this).toHTML());	
			}else{
				contents = ['<h2>回答の閲覧/修正</h2>',
								'<p>マーク欄・自由記述欄の回答内容を表示します。</p>',
								'<p>表示する項目・範囲を、次から選んでください。</p>',
								'<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(複数選択可)</li><li>「■設問」(複数選択可)</li></ul>',
								'<p>マーク欄の内容は、ラジオボタン/チェックボックスによって修正できます。</p><p>自由記述欄の内容は、テキストエリアを用いて文字に書き起こすことができます。</p>'];
			}
		}else if(this.isExportMode(viewMode)){
			contents.push('<h2>表データの書き出し</h2>');

			var body = '';
			if(this.isSelected(['t'])){
				body = new ExportAnchorFormatter().toHTML();
			}
			if(body != ''){
				contents.push(body); 
			}else{
				contents.push('<p>表データの書き出しをします。</p>');
				contents.push('<p>出力する項目・範囲を、次から選んでください。</p>');
				contents.push('<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(省略可・複数選択可)</li></ul>');
				contents.push('<p>※「■行」は、基本的には「全ての行」を選んでください。一部の行だけを選ぶと、選んだ行だけが表データに書き出しされます。</p>');
				contents.push('<p>表データの書き出し形式は、次の中から選ぶことができます。</p>');
				contents.push('<ul><li>「Excel」</li><li>「タブ区切りテキスト」</li></ul>');
			}
		}else if(this.isSimpleChartViewMode(viewMode)){
			contents.push('<h2>単純集計</h2>');

			var body = '';			
			if(this.isSelected(['t','r','q'])){
				body = new SimpleChartFormatter(this).toHTML();
			}			
			if(body != ''){
				contents.push(body);
			}else{
				contents.push('<p>単純集計結果を表示します。</p>');
				contents.push('<p>集計する項目・範囲を、次から選んでください。</p>');
				contents.push('<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(複数選択可)</li><li>「■設問」(複数選択可)</li></ul>');
				contents.push('<p>※「■行」は、基本的には「全ての行」を選んでください。一部の行だけを選ぶと、選んだ行だけが集計されます。</p>');
				contents.push('<p>※「■設問」は、基本的には1つ選んでください。複数の設問を選ぶと、選んだ設問の集計結果が列挙されます。</p>');
				contents.push('<p>集計結果は、設問の型に応じてグラフ化されます。</p>');
				contents.push('<ul><li>択一選択/複数選択の設問:「棒グラフ」</li></ul>');
			}
		}else if(this.isGroupSimpleChartViewMode(viewMode)){
			contents.push('<h2>グループごとの単純集計の一覧</h2>');
			
			var body = '';
			if(this.isSelected(['t','r','q'])){
				body = new GroupSimpleChartFormatter(this).toHTML();
			}
			
			if(body != ''){
				contents.push(body);
			}else{
				contents.push('<p>「■集計グループ」として<strong>複数個の項目を選択</strong>している場合に、それぞれの集計グループごとで「単純集計」を表示します。</p>');
				contents.push('<p>集計する項目・範囲を、次から選んでください。</p>');
				contents.push('<ul><li>「■調査票」</li><li>「■集計グループ」(<strong>複数個の項目を選択</strong>)</li><li>「■行」(複数選択可)</li><li>「■設問」(複数選択可)</li></ul>');
				contents.push('<p>※「■集計グループ」は、<strong>複数個の項目を選択</strong>してください。</p>');
				contents.push('<p>※「■行」は、基本的には「全ての行」を選んでください。一部の行だけを選ぶと、選んだ行だけが集計されます。</p>');
				contents.push('<p>※「■設問」は、基本的には1つ選んでください。複数の設問を選ぶと、選んだ設問の集計結果が列挙されます。</p>');
				contents.push('<p>集計結果は、設問の型に応じてグラフ化されます。</p>');
				contents.push('<ul><li>択一選択の設問:「円グラフ」</li><li>複数選択の設問:「棒グラフ」</li></ul>');
			}	
		}else if(this.isCrossChartViewMode(viewMode)){
			contents.push('<h2>クロス集計</h2>');

			var body = '';
			if(this.isSelected(['t','r','q'])){
				body = new CrossChartFormatter(this).toHTML();
			}
			
			if(body != ''){
				contents.push(body);
			}else{
				contents.push('<p>「■設問」として<strong>2個または3個の項目を選択</strong>している場合に、それらをクロス集計した内容を表示します。</p>');
				contents.push('<p>集計する項目・範囲を、次から選んでください。</p>');
				contents.push('<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(複数選択可)</li><li>「■設問」(<strong>2個または3個選択</strong>)</li></ul>');
				contents.push('<p>※「■行」は、基本的には「全ての行」を選んでください。一部の行だけを選ぶと、選んだ行だけが集計されます。</p>');
				contents.push('<p>※「■設問」は、「択一選択」ないし「複数選択」から<strong>2個または3個選択</strong>してください。</p>');
				contents.push('<p>クロス集計表は、表示する軸を入れ替えて再表示させることができます。</p>');
			}	
		}else if(this.isGroupCrossChartViewMode(viewMode)){
			contents.push('<h2>グループを軸に加えたクロス集計</h2>');

			var body = '';
			if(this.isSelected(['t','r','q'])){
				body = new GroupCrossChartFormatter(this).toHTML();
			}
			
			if(body != ''){
				contents.push(body);
			}else{	
				contents.push('<p>「■集計グループ」として<strong>複数個の項目を選択</strong>し、');
				contents.push('「■設問」として<strong>1個または2個の項目を選択</strong>している場合に、それらをクロス集計した内容を表示します。</p>');
				contents.push('<p>集計する項目・範囲を、次から選んでください。</p>');
				contents.push('<ul><li>「■調査票」</li><li>「■集計グループ」(<strong>複数個の項目を選択</strong>)</li><li>「■行」(複数選択可)</li><li>「■設問」(<strong>1個または2個選択</strong>)</li></ul>');
				contents.push('<p>※「■集計グループ」は、<strong>複数個の項目を選択</strong>してください。</p>');
				contents.push('<p>※「■行」は、基本的には「全ての行」を選んでください。一部の行だけを選ぶと、選んだ行だけが集計されます。</p>');
				contents.push('<p>※「■設問」は、「択一選択」ないし「複数選択」から<strong>1個または2個選択</strong>してください。</p>');
				contents.push('<p>クロス集計表は、表示する軸を入れ替えて再表示させることができます。</p>');
			}	
		}else if(this.isCrossChartListViewMode(viewMode)){
			contents.push('<h2>すべての設問に対するクロス集計の一覧</h2>');

			var body = '';
			if(this.isSelected(['t','r','q'])){
				body = new CrossChartListFormatter(this).toHTML();
			}
			
			if(body != ''){
				contents.push(body);
			}else{	
				contents.push('<p>選択された「■設問」と、それ以外の(選択されていない)設問を、総当たり方式でクロス集計した内容を表示します。</p>');
				contents.push('<p>集計する項目・範囲を、次から選んでください。</p>');
				contents.push('<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(複数選択可)</li><li>「■設問」(複数選択可)</li></ul>');
				contents.push('<p>※「■行」は、基本的には「全ての行」を選んでください。一部の行だけを選ぶと、選んだ行だけが集計されます。</p>');
				contents.push('<p>※「■設問」は、「択一選択」ないし「複数選択」から<strong>1個、または、必要ならばそれ以上の個数(最大4個)を選択</strong>してください。</p>');
				//contents += '<p>クロス集計表は、表示する軸を入れ替えて再表示させることができます。</p>';
			}
		}else if(this.isErrorPageViewMode(viewMode)){
			
			contents.push('<h2>読み取り失敗画像</h2>');
			
			var body = '';
			if(this.isSelected(['t','r'])){
				body = new ErrorPageFormatter().toHTML();
			}
			
			if(body != ''){
				contents.push('<p>前回のセッションでは、以下の画像ファイルの読み取りに失敗しました。</p>');
				contents.push('<p>失敗の原因を参考に、スキャンをやり直すか、ペイントツール等を使って画像を修正してください。</p>');
				contents.push('<p>修正を終えたら、MarkReader画面の「<img src="'+imagePath+'Refresh24.gif"/>再処理」ボタンを押してください。</p>');
				contents.push('<hr/>');
				contents.push(body); 
			}else{
				contents.push('<p>前回のセッションで、<strong>読み取りに失敗した画像ファイル</strong>を抽出して表示します。</p>');
				contents.push('<p><strong>読み取りに失敗した画像ファイル</strong>を検索する範囲を、次から選んでください。</p>');
				contents.push('<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(複数選択可)</li></ul>');
			}	
		}else if(this.isLowReliabilityViewMode(viewMode)){
			contents.push('<h2>読み取り信頼度の低い設問</h2>');
			contents.push('<p>※この機能は現在開発中です。</p>'); 
			//contents += '<p>「■行」と「■設問」を選んでください。</p>';
				
		}else if(this.isNoAnswerErrorViewMode(viewMode)){
			
			var body = '';

			if(this.isSelected(['t','r'])){
				body = new EditableErrorFormAreaFormatter(this, true, false).toHTML();
			}
			
			if(body != ''){
				contents.push(body);
			}else{
				contents.push('<h2>無回答エラーの択一式設問</h2>');
				contents.push('<p>択一選択式設問のうち、<strong>無回答エラー</strong>のものを抽出して表示します。</p>');
				contents.push('<p><strong>無回答エラー</strong>を検索する範囲を、次から選んでください。</p>');
				contents.push('<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(複数選択可)</li><li>「■設問」(複数選択可)</li></ul>');
			}				
		}else if(this.isMultiAnswerErrorViewMode(viewMode)){
			
			var body = '';
			if(this.isSelected(['t','r'])){
				body = new EditableErrorFormAreaFormatter(this, false, true).toHTML();
			}
			if(body != ''){
				contents.push(body);
			}else{
				contents.push('<h2>重複回答エラーの択一式設問</h2>');
				contents.push('<p>択一選択式設問のうち、<strong>重複回答エラー</strong>のものを抽出して表示します。</p>');
				contents.push('<p><strong>重複回答エラー</strong>を検索する範囲を、次から選んでください。</p>');
				contents.push('<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(複数選択可)</li><li>「■設問」(複数選択可)</li></ul>');
			}
		}
		
		contents.push('<hr/>');
		$(this.contentsPanelId).innerHTML = contents.join(''); 

		}catch(ignore){
			console.trace();
			//alert("contents:"+ignore);
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
		return Form.serialize($(formId)).toQueryParams()[paramName];
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
