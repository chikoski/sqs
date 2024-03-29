<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<title>${title}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link rel="stylesheet" href="${cssPath}/console.css">
<link rel="shortcut icon" href="${imagePath}/favicon.ico">
<style type="text/css">
 * {
  margin: 0; padding: 0;
 }
 html {
  overflow: auto;
}
body {
  overflow: auto;
}
</style>
<!--[if gte IE 5 ]>
<style type="text/css">
    #console{
 		top: 10px;
	}
 </style>
<![endif]-->
<!--[if lt IE 7]> 
<style type="text/css">
  body{
    background: url(null) fixed;
  }
  fixed {
    position: absolute;
    top: expression(eval(document.documentElement.scrollTop));
    right: 0px;
 }
</style>
<![endif]-->
<script type="text/javascript">
// <![CDATA[
IMAGE_BASEPATH = "${imagePath}";
</script>
<script type="text/javascript" src="${javaScriptPath}/resources_${language}.js"></script>
<script type="text/javascript" src="${javaScriptPath}/logger.js"></script>
<script type="text/javascript" src="${javaScriptPath}/shadedborder.js"></script>
<script type="text/javascript" src="${javaScriptPath}/prototype.js"></script>
<script type="text/javascript" src="${javaScriptPath}/linkedlist.js"></script>
<script type="text/javascript" src="${javaScriptPath}/button.js"></script>
<script type="text/javascript" src="${javaScriptPath}/selector.js"></script>
<script type="text/javascript" src="${javaScriptPath}/binaryArrayUtil.js"></script>
<script type="text/javascript" src="${javaScriptPath}/console.js"></script>
<script type="text/javascript" src="${javaScriptPath}/contentsFormatter.js"></script>
<script type="text/javascript" src="${javaScriptPath}/statisticsFormatter.js"></script>
<script type="text/javascript" src="${javaScriptPath}/dispatcher.js"></script>
<script type="text/javascript">
// <![CDATA[
var sessionID = ${sid};
var masterOptions = ${masterOptions};
var tableOptionsArray = ${tableOptionsArray};
var questionOptionsArray = ${questionOptionsArray};

var rowOptionsArray = ${rowOptionsArray};

consoleHandler = new ConsoleHandler(sessionID, 'console', 
  {
  'frame': 'consolePanelFrame',
  'panel': 'consoleContentPanel',
  'title': 'consoleTitle', 
  'closeButton': 'consoleCloseButton'
  }, 

  "${sourceDirectoryName}",

   [
    [
     [vHandler]
    ],
    [
     [mHandler, tHandler, rHandler], 
     [qHandler]
    ]
  ]
 );

contentsDispatcher = new ContentsDispatcher(sessionID, 'consoleForm', 'contentsPanel', IMAGE_BASEPATH);

function init(){

	uiHandler = new UIHandlers(consoleHandler, tooltipHandler);

	requestHandler = new RequestHandler(
		sessionID,
		uiHandler, 
		contentsDispatcher,
		'consoleForm',
		['ar', 'aq', 'f-0',  'f-1',  'f-2'],
		['m', 'axis'],
		[ 't', 'r', 'q']
	);
	
	tooltipHandler.setup();
	consoleHandler.setup();

	document.getElementById('popupBar').onmouseover= function(){ consoleHandler.show();};
	document.getElementById('consoleCloseButton').onclick= function(){consoleHandler.forcedHide();};
	document.getElementById('consoleTitle').onclick = function (){ consoleHandler.toggle();};

	consoleHandler.setRequestHandler(requestHandler);
	mHandler.onChange = function(){
		ConsoleListBoxHandler.prototype.onChange.apply(this);
		var selectedMasterIndex = mHandler.getSelectedIndexArray()[0];

		tHandler.updateOptionsBy(selectedMasterIndex);
		qHandler.updateOptionsBy(selectedMasterIndex);
		rHandler.updateOptions([]);
	};
	
	tHandler.onChange = function(){
		ConsoleListBoxHandler.prototype.onChange.apply(this);
		$('ar').checked = false;
		for(var i = 0; i < rHandler.elem.options.length; i++){
			var option = rHandler.elem.options[i];
			option.selected = false;
		}
	};
	
	tHandler.hasChanged = function(id){
		TableHandler.prototype.hasChanged.apply(this, [id]);
		ConsoleListBoxHandler.prototype.hasChanged.apply(this);
		rHandler.setDefaultSelection(params);
		rHandler.onChange();
		tHandler.hasChanged = TableHandler.prototype.hasChanged;
	};
	
	qHandler.updateOptionsBy = function(index){ qHandler.updateOptions( questionOptionsArray[index] );};
	tHandler.updateOptionsBy = function(index){ tHandler.updateOptions( tableOptionsArray[index] );};

	var params = getRequestParameters();

	mHandler.updateOptions(masterOptions,
		function(){
			if(params['u'] == null){
				if(params['v'] != null){
					vHandler.setDefaultSelection(params);
					fHandler.setDefaultSelection(params);
					vHandler.updateState();
				}

				try{
					mHandler.elem.options[0].selected = true;
					mHandler.onChange();
				}catch(e){}
				
				qHandler.setDefaultSelection(params);
				tHandler.setDefaultSelection(params);
				tHandler.onChange();
			}	
	});
	
	contentsDispatcher.axis = params['axis'];
	if(params['threshold'] != null){
		consoleDispatcher.densityThreshold = params['threshold'];
	}
	if(params['n'] != null){
		contentsDispatcher.navigationIndex = params['n'];
	}
	if(params['N'] != null){
		contentsDispatcher.numMaxAnswerItems = params['N'];
	}	
}

function getRequestParameters(){
  	if(1 < location.search.length) {
    	var o = new Object();
    	var ret = location.search.substr(1).split("&");
    	for(var i = 0; i < ret.length; i++) {
	   		var r = ret[i].split("=");
    		o[r[0]] = r[1];
    	}
    	return o;
  	} else {
    	return {};
  	}
}

// ]]>
</script>
</head>
<body onload="init()">

  <form id='consoleForm' action='' method='post'>
   <script type="text/javascript">
     document.write(tooltipHandler.toHTML());
   </script>
  <script type="text/javascript">
    var border = RUZEE.ShadedBorder.create({ corner:8, shadow:16, border:2 });
     document.write(consoleHandler.toHTML());
     border.render('consolePanelFrame');
  </script>
 </form>

  <div id="padding" onmouseover="consoleHandler.hide(false)"></div>
  <form id="contentsForm" action="" method='post'>
  <div id="contentsPanel" onmouseover="consoleHandler.hide(false)">
     <hr/>
  </div>
  </form>
  
  <div id="logger">
  </div>
  
  <div id="prevPageButton" class="fixedAtSouthWestCorner" style="display:none" ><img src="${imagePath}/leftArrow.png" alt="&lt;&lt; 前へ"
   onclick="requestHandler.requestPrevPage()"
   onmouseover="$('pageTitle').style.display='block'; this.setAttribute('src', '${imagePath}/leftArrow.gif')" onmouseout="$('pageTitle').style.display='none'; this.setAttribute('src', '${imagePath}/leftArrow.png')"/></div>
   
  <div id="pageTitle" class="fixedAtBottom" style="display:none; text-align:center" >pageTitle</div>

  <div id="nextPageButton" class="fixedAtSouthEastCorner" style="display:none" ><img src="${imagePath}/rightArrow.png" alt="次へ　&gt;&gt;"
   onclick="requestHandler.requestNextPage()"
   onmouseover="$('pageTitle').style.display='block'; this.setAttribute('src', '${imagePath}/rightArrow.gif')" onmouseout="$('pageTitle').style.display='none'; this.setAttribute('src', '${imagePath}/rightArrow.png')"/></div>

  <div id="messageDialog"/>

  
</body>
</html>
