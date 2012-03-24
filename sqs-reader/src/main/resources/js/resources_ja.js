
PAGE_ERROR_REASON_LABEL = '原因';
EXPORT_EXCEL_FILE_LABEL = 'Excelファイルの書き出し';
EXPORT_CSV_FILE_LABEL = 'タブ区切りテキストファイルの書き出し';
LINK_TO_THIS_PAGE = 'このページへのリンク';
LINK_TO_THIS_PAGE_MESSAGE = 'で現在の表示内容を再表示・共有できます';
ALL_ROWS_LABEL = '全ての行';
ALL_QUESTIONS_LABEL = '全ての設問';
DISPLAY_CONTENTS_LABEL = '表示';
POPUP_CONSOLE_ON_FOCUS_LABEL = 'ここにマウスカーソルを合わせるとコンソールが開きます';
MASTER_LABEL = '調査票';
ROW_GROUP_LABEL = '集計グループ';
ROW_LABEL = '行';
QUESTION_LABEL = '設問';
SELECT_SINGLE_LABEL = '択一選択';
SELECT_MULTI_LABEL = '複数選択';
FREE_ANSWER_LABEL = '自由記述';
BROWSE_EDIT_ANSWER_LABEL = '回答の閲覧/修正';
EXPORT_SPREADSHEET_LABEL = '表データ書き出し';
SIMPLE_STATISTICS_LABEL = '単純集計';
GROUPED_STATISTICS_LABEL = 'グループ単純集計';
GROUPED_STATISTICS_DETAIL_LABEL = 'グループごとの単純集計の一覧';
CROSS_TABULAR_LABEL = 'クロス集計';
CROSS_TABULAR_BY_GROUP_LABEL = 'グループクロス集計';
CROSS_TABULAR_BY_GROUP_DETAIL_LABEL = 'グループを軸に加えたクロス集計';
CROSS_TABULAR_LIST_DETAIL_LABEL = 'すべての設問に対するクロス集計の一覧';
CROSS_TABULAR_LIST_LABEL = 'クロス集計一覧';
PAGE_ERROR_LABEL = '読み取り失敗';
PAGE_ERROR_DETAIL_LABEL = '読み取り失敗画像';
LOW_CONFIDENCE_LABEL = '信頼度低';
LOW_CONFIDENCE_DETAIL_LABEL = '読み取り信頼度の低い回答';
NO_ANSWER_ERROR_LABEL = '無回答エラー';
NO_ANSWER_ERROR_DETAIL_LABEL = '無回答エラーを含む回答';
MULTIPLE_ANSWER_ERROR_LABEL = '重複回答エラー';
MULTIPLE_ANSWER_ERROR_DETAIL_LABEL = '重複回答エラーを含む回答';
TOTAL_LABEL = '合計';
TABLE_LABEL = '集計グループ';
NO_ANSWER_LABEL = '無回答';
UNDER_CONSTRUCTING_LABEL = '※この機能は現在開発中です。';

OVERVIEW_HOWTO_MESSAGE = '<h1><img src="'+IMAGE_BASEPATH+'/omr.gif" width="64" height="64">SQS MarkReader: Resultブラウザ</h1>'+
				'<h2>操作方法の概要</h2>'+
				'<h3>1.　コンソールの表示</h3>'+
				'<ul><li>画面上部の紺色のバーの部分にマウスを重ねると、「コンソール」が表示されます。</li>'+
			 	'<li>マウスを「コンソール」から外に出すと、「コンソール」は自動的に隠れます。</li>'+
				'<li>「コンソール」のタイトル表示部分をマウスで左クリックすると、「コンソールを自動的に隠す」「コンソールを常時表示」を切り替えできます。</li></ul>'+
				'<h3>2.　表示メニューからの選択</h3>'+
				'<ul><li>「■表示」のメニューの、以下の項目から、利用したい機能を選択してください。'+
				'<dl>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/pencil.png">回答の閲覧/修正</dt>'+
				'<dd>マーク欄・自由記述欄の回答内容を表示します。<br/>マーク欄の内容は、ラジオボタン/チェックボックスによって修正できます。<br/>自由記述欄の内容は、テキストエリアを用いて文字に書き起こすことができます。</dd>'+
				'<hr/>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/page_excel.png">表データ書き出し</dt>'+
				'<dd>表データの書き出しをします。形式は「Excel」「タブ区切りテキスト」の2種類から選ぶことができます。</dd>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/chart_bar.png">単純集計</dt>'+
				'<dd>単純集計結果とそれに対応する「棒グラフ」を表示します。</dd>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/chart_bar_add.png">グループ単純集計</dt>'+
				'<dd>「■集計グループ」として複数個の項目が選択されている場合に、それぞれの集計グループごとで「単純集計」を表示します。</dd>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/table.png">クロス集計</dt>'+
				'<dd>「■設問」が2個または3個選択されている場合に、それらをクロス集計した内容を表示します。</dd>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/table_add.png">グループクロス集計</dt>'+
				'<dd>「■集計グループ」が複数項目と、「■設問」が1個または2個選択されている場合に、それらをクロス集計した内容を表示します。</dd>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/table_multiple.png">クロス集計一覧</dt>'+
				'<dd>選択された「■設問」と、それ以外の(選択されていない)設問を、総当たり方式でクロス集計した内容を表示します。</dd>'+
				'<hr/>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/exclamation.png">読み取り失敗</dt>'+
				'<dd>セッション中に読み取りに失敗した画像ファイルを抽出して表示します</dd>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/icon_alert_cyan.gif">信頼度低</dt>'+
			 	'<dd>マーク式回答のうち、塗りつぶしの認識の閾値設定に関連して「認識結果の信頼度が低いもの」を抽出して表示します。</dd>'+
			 	'<dt><img src="'+IMAGE_BASEPATH+'/icon_alert.gif">無回答</dt>'+
				'<dd>択一選択式設問のうち、「無回答エラーのもの」を抽出して表示します。</dd>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/icon_alert_orange.gif">重複回答</dt>'+
				'<dd>択一選択式設問のうち、「重複回答エラーのもの」を抽出して表示します。</dd>'+
				'</dl>'+
				'</li></ul>'+
				'<h3>3.　表示内容の選択</h3>'+
				'<p>コンソール内の4つリストボックスから、表示したい内容を選択してください。</p>'+
				'<p>※複数選択をするときには、選択範囲のドラッグ、選択範囲の始点・終点でのShift+クリック、選択トグル項目のCtrl+クリックをしてください。</p>'+
				'<ul>'+
				'<li>「■調査票」から、Sourceフォルダ内から抽出処理された「調査票」をひとつ選んでください。</li>'+
				'<li>「■集計グループ」から、Sourceフォルダ内で回答用紙のスキャン画像を保存したフォルダ構造に対応した「集計グループ」を選んでください(複数選択可)。</li>'+
				'<li>「■行」から、選択されている「集計グループ」内の、回答者(表の行)を選んでください(複数選択可)。</li>'+
				'<li>「■設問」から、選択されている「調査票」内の、設問(表の列)を選んでください(複数選択可)。</li>'+
				'</ul>'
				;
				
FORM_EDIT_MODE_HOWTO_MESSAGE = '<h2>'+BROWSE_EDIT_ANSWER_LABEL+'</h2>'+
								'<p>マーク欄・自由記述欄の回答内容を表示します。</p>'+
								'<p>表示する項目・範囲を、次から選んでください。</p>'+
								'<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(複数選択可)</li><li>「■設問」(複数選択可)</li></ul>'+
								'<p>マーク欄の内容は、ラジオボタン/チェックボックスによって修正できます。</p><p>自由記述欄の内容は、テキストエリアを用いて文字に書き起こすことができます。</p>';

EXPORT_SPREADSHEET_HOWTO_LABEL = '<p>表データの書き出しをします。</p>'+
				'<p>出力する項目・範囲を、次から選んでください。</p>'+
				'<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(省略可・複数選択可)</li></ul>'+
				'<p>※「■行」は、基本的には「全ての行」を選んでください。一部の行だけを選ぶと、選んだ行だけが表データに書き出しされます。</p>'+
				'<p>表データの書き出し形式は、次の中から選ぶことができます。</p>'+
				'<ul><li>「Excel」</li><li>「タブ区切りテキスト」</li></ul>';

SIMPLE_STATISTICS_HOWTO_LABEL = '<p>単純集計結果を表示します。</p>'+
				'<p>集計する項目・範囲を、次から選んでください。</p>'+
				'<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(複数選択可)</li><li>「■設問」(複数選択可)</li></ul>'+
				'<p>※「■行」は、基本的には「全ての行」を選んでください。一部の行だけを選ぶと、選んだ行だけが集計されます。</p>'+
				'<p>※「■設問」は、基本的には1つ選んでください。複数の設問を選ぶと、選んだ設問の集計結果が列挙されます。</p>'+
				'<p>集計結果は、設問の型に応じてグラフ化されます。</p>'+
				'<ul><li>択一選択/複数選択の設問:「棒グラフ」</li></ul>';

GROUP_STATISTICS_HOWTO_LABEL = '<p>「■集計グループ」として<strong>複数個の項目を選択</strong>している場合に、それぞれの集計グループごとで「単純集計」を表示します。</p>'+
				'<p>集計する項目・範囲を、次から選んでください。</p>'+
				'<ul><li>「■調査票」</li><li>「■集計グループ」(<strong>複数個の項目を選択</strong>)</li><li>「■行」(複数選択可)</li><li>「■設問」(複数選択可)</li></ul>'+
				'<p>※「■集計グループ」は、<strong>複数個の項目を選択</strong>してください。</p>'+
				'<p>※「■行」は、基本的には「全ての行」を選んでください。一部の行だけを選ぶと、選んだ行だけが集計されます。</p>'+
				'<p>※「■設問」は、基本的には1つ選んでください。複数の設問を選ぶと、選んだ設問の集計結果が列挙されます。</p>'+
				'<p>集計結果は、設問の型に応じてグラフ化されます。</p>'+
				'<ul><li>択一選択の設問:「円グラフ」</li><li>複数選択の設問:「棒グラフ」</li></ul>';
				
CROSS_TABULAR_HOWTO_LABEL = '<p>「■設問」として<strong>2個または3個の項目を選択</strong>している場合に、それらをクロス集計した内容を表示します。</p>'+
				'<p>集計する項目・範囲を、次から選んでください。</p>'+
				'<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(複数選択可)</li><li>「■設問」(<strong>2個または3個選択</strong>)</li></ul>'+
				'<p>※「■行」は、基本的には「全ての行」を選んでください。一部の行だけを選ぶと、選んだ行だけが集計されます。</p>'+
				'<p>※「■設問」は、「択一選択」ないし「複数選択」から<strong>2個または3個選択</strong>してください。</p>'+
				'<p>クロス集計表は、表示する軸を入れ替えて再表示させることができます。</p>';

CROSS_TABULAR_BY_GROUP_HOWTO_LABEL = '<p>「■集計グループ」として<strong>複数個の項目を選択</strong>し、'+
				'「■設問」として<strong>1個または2個の項目を選択</strong>している場合に、それらをクロス集計した内容を表示します。</p>'+
				'<p>集計する項目・範囲を、次から選んでください。</p>'+
				'<ul><li>「■調査票」</li><li>「■集計グループ」(<strong>複数個の項目を選択</strong>)</li><li>「■行」(複数選択可)</li><li>「■設問」(<strong>1個または2個選択</strong>)</li></ul>'+
				'<p>※「■集計グループ」は、<strong>複数個の項目を選択</strong>してください。</p>'+
				'<p>※「■行」は、基本的には「全ての行」を選んでください。一部の行だけを選ぶと、選んだ行だけが集計されます。</p>'+
				'<p>※「■設問」は、「択一選択」ないし「複数選択」から<strong>1個または2個選択</strong>してください。</p>'+
				'<p>クロス集計表は、表示する軸を入れ替えて再表示させることができます。</p>';

CROSS_TABULAR_LIST_HOWTO_LABEL = '<p>選択された「■設問」と、それ以外の(選択されていない)設問を、総当たり方式でクロス集計した内容を表示します。</p>'+
				'<p>集計する項目・範囲を、次から選んでください。</p>'+
				'<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(複数選択可)</li><li>「■設問」(複数選択可)</li></ul>'+
				'<p>※「■行」は、基本的には「全ての行」を選んでください。一部の行だけを選ぶと、選んだ行だけが集計されます。</p>'+
				'<p>※「■設問」は、「択一選択」ないし「複数選択」から<strong>1個、または、必要ならばそれ以上の個数(最大4個)を選択</strong>してください。</p>';
				
PAGE_ERROR_HOWTO_LABEL_1 = '<p>このセッションでは、以下の画像ファイルの読み取りに失敗しています。</p>'+
				'<p>失敗の原因を参考に、スキャンをやり直すか、ペイントツール等を使って画像を修正してください。</p>'+
				'<p>修正を終えたら、MarkReader画面の「<img src="'+IMAGE_BASEPATH+'/Refresh24.gif"/>再処理」ボタンを押してください。</p>';

PAGE_ERROR_HOWTO_LABEL_2 = '<p>このセッションで、<strong>読み取りに失敗した画像ファイル</strong>を抽出して表示します。</p>'+
				'<p><strong>読み取りに失敗した画像ファイル</strong>を検索する範囲を、次から選んでください。</p>'+
				'<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(複数選択可)</li></ul>';

NO_ANSWER_ERROR_HOWTO_LABEL = '<p>択一選択式設問のうち、<strong>無回答エラー</strong>のものを抽出して表示します。</p>'+
				'<p><strong>無回答エラー</strong>を検索する範囲を、次から選んでください。</p>'+
				'<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(複数選択可)</li><li>「■設問」(複数選択可)</li></ul>';

MULTIPLE_ANSWER_ERROR_HOWTO_LABEL = '<p>択一選択式設問のうち、<strong>重複回答エラー</strong>のものを抽出して表示します。</p>'+
				'<p><strong>重複回答エラー</strong>を検索する範囲を、次から選んでください。</p>'+
				'<ul><li>「■調査票」</li><li>「■集計グループ」(複数選択可)</li><li>「■行」(複数選択可)</li><li>「■設問」(複数選択可)</li></ul>';

NO_MATCHED_ITEM='この範囲にはエラーを含む回答項目は存在しません。';