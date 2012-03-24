
PAGE_ERROR_REASON_LABEL = 'Problem Caused by:';
EXPORT_EXCEL_FILE_LABEL = 'Export to Excel file';
EXPORT_CSV_FILE_LABEL = 'Export to CSV file';
LINK_TO_THIS_PAGE = 'Link';
LINK_TO_THIS_PAGE_MESSAGE = 'Paste link in email or IM';
ALL_ROWS_LABEL = 'all';
ALL_QUESTIONS_LABEL = 'all';
DISPLAY_CONTENTS_LABEL = 'View';
POPUP_CONSOLE_ON_FOCUS_LABEL = 'mouse over to popup console';
MASTER_LABEL = 'Forms';
ROW_GROUP_LABEL = 'Tables';
ROW_LABEL = 'Rows';
QUESTION_LABEL = 'Questions';
SELECT_SINGLE_LABEL = 'single answer';
SELECT_MULTI_LABEL = 'multiple answer';
FREE_ANSWER_LABEL = 'free answer';
BROWSE_EDIT_ANSWER_LABEL = 'Browse/Edit Answers';
EXPORT_SPREADSHEET_LABEL = 'Export to Spreadsheets';
SIMPLE_STATISTICS_LABEL = 'Simple Statistics';
GROUPED_STATISTICS_LABEL = 'Grouped Statistics';
GROUPED_STATISTICS_DETAIL_LABEL = 'List of Grouped Simple Statistics';
CROSS_TABULAR_LABEL = 'Cross Tabular Data';
CROSS_TABULAR_BY_GROUP_LABEL = 'Cross Tabular Data by Groups';
CROSS_TABULAR_BY_GROUP_DETAIL_LABEL = 'Cross Tabular Data by Groups';
CROSS_TABULAR_LIST_DETAIL_LABEL = 'List of Cross Tabular Data';
CROSS_TABULAR_LIST_LABEL = 'List of Cross Tabular Data';
PAGE_ERROR_LABEL = 'ERROR: Page Recognizaion';
PAGE_ERROR_DETAIL_LABEL = 'ERROR: Page Recognization';
LOW_CONFIDENCE_LABEL = 'WARN: Low Confidence Level';
LOW_CONFIDENCE_DETAIL_LABEL = 'Answers in Low Confidence Level';
NO_ANSWER_ERROR_LABEL = 'ERROR: No Answer';
NO_ANSWER_ERROR_DETAIL_LABEL = 'No Answer of Single Select Answers';
MULTIPLE_ANSWER_ERROR_LABEL = 'ERROR: Multiple Answers';
MULTIPLE_ANSWER_ERROR_DETAIL_LABEL = 'Multiple Answers of Single Select Answers';
TOTAL_LABEL = 'Total';
TABLE_LABEL = 'Group';
NO_ANSWER_LABEL = 'N/A';
UNDER_CONSTRUCTING_LABEL = '(Sorry! This feature is under constructing)';

OVERVIEW_HOWTO_MESSAGE = '<h1><img src="'+IMAGE_BASEPATH+'/omr.gif" width="64" height="64">SQS MarkReader: ResultBrowser/Editor</h1>'+
				'<h2>How to Use</h2>'+
				'<h3>1.　Show Console</h3>'+
				'<ul><li>To show the console, mouse over the blue area on the top of this window.</li>'+
			 	'<li>To hide the console, mouse out from the console, automatically.</li>'+
				'<li>To toggle automatic-hidden-console and show-by-default-console-mode, click the titlebar of the console.</li></ul>'+
				'<h3>2.　Use Drill-Down Menus</h3>'+
				'<ul><li>To select features, use "View" menu in the console.'+
				'<dl>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/pencil.png">Browse/Edit Answers</dt>'+
				'<dd>You can get answers in mark area and free answer area.<br/>'+
				'You can edit marking-answers with radio buttons or check boxes.<br/>'+
				'You can enter texts of free answer images with textarea form controls.</dd>'+
				'<hr/>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/page_excel.png">Export to Spreadsheets</dt>'+
				'<dd>Export to spreadsheet data. You can choose .xls or .csv format.</dd>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/chart_bar.png">Simple Statistics</dt>'+
				'<dd>You can get simple statistics, pie charts, and bar charts.</dd>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/chart_bar_add.png">Grouped Statistics</dt>'+
				'<dd>You can get simple list of simple statistics by select tables when two or more tables are selected.</dd>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/table.png">Cross Tabular</dt>'+
				'<dd>You can get cross tabular data when two or three questions are selected.</dd>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/table_add.png">Grouped Cross Tabular</dt>'+
				'<dd>You can get cross tabular data when multiple tables and one or two questions are selected.</dd>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/table_multiple.png">List of Cross Tabular Data.</dt>'+
				'<dd>You can get list of cross tabular data with selected question and the other (unselected) questions.</dd>'+
				'<hr/>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/exclamation.png">ERROR: Page Recognization</dt>'+
				'<dd>You can get image files with page recognization error.</dd>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/icon_alert_cyan.gif">WARN: Low Confidency Level</dt>'+
			 	'<dd>You can get answer areas in low confidency level. The results may be wrong.</dd>'+
			 	'<dt><img src="'+IMAGE_BASEPATH+'/icon_alert.gif">ERROR: No Answer</dt>'+
				'<dd>You can get answer areas with no answer error.</dd>'+
				'<dt><img src="'+IMAGE_BASEPATH+'/icon_alert_orange.gif">ERROR: Multiple answers</dt>'+
				'<dd>You can get answer areas with multiple answer error.</dd>'+
				'</dl>'+
				'</li></ul>'+
				'<h3>3.　Select items to browse</h3>'+
				'<p>Select items you from 4 boxes in console.</p>'+
				'<p>You can make multiple select with left-dragging, left-click on start point and shift+left-click on end, toggle with Ctrl+left-clicking.</p>'+
				'<ul>'+
				'<li>Select a form item.</li>'+
				'<li>Select one or more tables.</li>'+
				'<li>Select one or more rows.</li>'+
				'<li>Select one or more questions(columns).</li>'+
				'</ul>';
				
FORM_EDIT_MODE_HOWTO_MESSAGE = '<h2>'+BROWSE_EDIT_ANSWER_LABEL+'</h2>'+
								'<p>You can get images and values of answers, of which you have selected in the console.</p>'+
								'<p>Please select items shown in console:</p>'+
								'<ul><li>one of forms</li><li>one or more of tables</li><li>one or more of rows</li><li>one or more of questions(columns).</li></ul>'+
								'<p>You can modify marking values of answer areas by radio button or checkbox.</p><p>You can enter texts of free answers with textarea.</p>';

EXPORT_SPREADSHEET_HOWTO_LABEL = '<p>Export to Spreadsheets.</p>'+
				'<p>Please select items shown in console:</p>'+
				'<ul><li>one of forms</li><li>one or more of tables</li><li>one or more rows</li></ul>'+
				'<p>The selected rows are exported as a spreasheet. Basically, you may select all rows. </p>'+
				'<p>The exporting format of the spreadsheeet can be choosed:</p>'+
				'<ul><li>Excel</li><li>CSV</li></ul>';

SIMPLE_STATISTICS_HOWTO_LABEL = '<p>Simple statistical data.</p>'+
                                 '<p>Please select items shown in console:</p>'+
                                 '<ul><li>one of forms</li><li>one or more of tables</li><li>one or more rows</li><li>one or more questions(columns)</li></ul>'+
                                 '<p>Please select questions to be shown. Basically, you may select one questions.</p>'+
                                 '<p>The statistical result is visualized by its type of form control.</p>'+
                 				'<ul><li>Single select answers: pie-charts</li></li>Multiple select answers: bar-charts</li></ul>';

GROUP_STATISTICS_HOWTO_LABEL = '<p>When you are selecting <strong>multiple tables</strong>, you can get statistical data group by tables.</p>'+
                                '<p>Please select items shown in console:</p>'+
                                '<ul><li>one of forms</li><li>one or more of tables</li><li>one or more rows</li><li>one or more questions(columns)</li></ul>'+
                                '<p>Please select <strong>multiple tables</strong>.</p>'+
				'<p>Please select rows to be shown. Basically you should select all rows.</p>'+
				'<p>Please select questions to be shown. Basically, you may select one questions.</p>'+
				'<p>The statistical result is visualized by its type of form control.</p>'+
				'<ul><li>Single select answers: pie-charts</li></li>Multiple select answers: bar-charts</li></ul>';
				
CROSS_TABULAR_HOWTO_LABEL =  '<p>When you are selecting <strong>two ore three questions</strong>, you can get cross tabular data by the selected questions.</p>'+
                             //'<p>When you are selecting <strong>multiple tables</strong>, you can get statistical data group by tables.</p>'+
                             '<p>Please select items shown in console:</p>'+
                             '<ul><li>one of forms</li><li>one or more of tables</li><li>one or more rows</li><li>two or three questions(columns)</li></ul>'+
                             '<p>Please select rows to be shown. The selected rows are used in the statistics. Basically, you may select all rows.</p>'+
                             '<p>Please select questions of single answer or multiple answer to be shown. Basically, you may select two ore three questions.</p>'+
                             '<p>The cross tabular can change its axis.</p>';

CROSS_TABULAR_BY_GROUP_HOWTO_LABEL = 
'<p>When you are selecting <strong>multiple tables</strong> and <strong>one or two questions</strong>, you can get cross tabular data by the selected questions.</p>'+
				'<p>Please select items shown in console:</p>'+
                '<ul><li>one of forms</li><li>one or more of tables</li><li>one or more rows</li><li>one or two questions(columns)</li></ul>'+
                '<p>Please select <strong>multiple tables</strong>.</p>'+
				'<p>Please select rows to be shown. The selected rows are used in the statistics. Basically, you may select all rows.</p>'+
				'<p>Please select questions of single answer or multiple answer to be shown. Basically, you may select one ore two questions.</p>'+
				'<p>The cross tabular can change its axis.</p>';

CROSS_TABULAR_LIST_HOWTO_LABEL = '<p>You can get round robin cross tabular data of one selected question and the other questions.</p>'+
                                  '<p>When you are selecting <strong>multiple tables</strong> and <strong>one or two questions</strong>, you can get cross tabular data by the selected questions.</p>'+
                                  '<p>Please select items shown in console:</p>'+
                                  '<ul><li>one of forms</li><li>one or more of tables</li><li>one or more rows</li><li>one or more questions(columns)</li></ul>'+
                                  '<p>Please select rows to be shown. The selected rows are used in the statistics. Basically, you may select all rows.</p>'+
                                  '<p>Please select questions of single answer or multiple answer to be shown. Basically, you may select one ore more questions(upper 4 questions).</p>';
				
PAGE_ERROR_HOWTO_LABEL_1 = '<p>There are errors of page image processing in this session.</p>'+
				'<p>Please fix or rescan the images with error report.</p>'+
				'<p>When you have fixed and rescanned the images, push <img src="'+IMAGE_BASEPATH+'/Refresh24.gif"/>Restart button in MarkReader.</p>';

PAGE_ERROR_HOWTO_LABEL_2 = '<p>You can get <strong>scanned images with some errors</strong>.</p>'+
                            '<p>Please select items to find errors in them.'+
                '<ul><li>one of forms</li><li>one or more of tables</li><li>one or more rows</li></ul>';

NO_ANSWER_ERROR_HOWTO_LABEL = '<p>You can get <strong>answer areas with no answer errors</strong>.</p>'+
                               '<p>Please select items to find no answer errors in them.'+
				'<ul><li>one of forms</li><li>one or more of tables</li><li>one or more rows</li><li>one or more questions(columns)</li></ul>';

MULTIPLE_ANSWER_ERROR_HOWTO_LABEL = '<p>You can get <strong>answer areas with multiple answer errors</strong>.</p>'+
                                     '<p>Please select items to find multiple answer errors in them.'+
				'<ul><li>one of forms</li><li>one or more of tables</li><li>one or more rows</li><li>one or more questions(columns)</li></ul>';
NO_MATCHED_ITEM='No item mached in this range.';
