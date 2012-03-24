<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

  <script src='/jar/js/jquery/jquery.js' type='text/javascript'></script>
  <script src='/jar/js/jquery/ui.core.js' type='text/javascript'></script>
  <script src='/jar/js/jquery/jquery.cookie.js' type='text/javascript'></script>
  <link href='/jar/js/dynatree/skin/ui.dynatree.css' rel='stylesheet' type='text/css'>
  <script src='/jar/js/dynatree/jquery.dynatree.js' type='text/javascript'></script>
  <script type="text/javascript" src="/jar/js/shadedborder.js"></script>
		
<script>

function getMasterID(path){
  return path.split('/')[3]; 
}
function getPath(path){
  return path.substring(path.indexOf('/', path.indexOf('/', path.indexOf('/', path.indexOf('/')+1)+1)+1)+1); 
}

var selectQuestionEventHandler = function(path){
       alert("show contents:"+path);
};

var pathEventHandler = function(path){
	var p = getPath(path);
	alert(path+" - "+p);
	$('#row').fileTree( {script : "/r/${sessionID}/"+masterID+"/"+p,
        				 root: "/r/${sessionID}/"+masterID+"/"+p+"/"},
           	function(path){
				  alert("show contents:"+p);
			} );
};
var selectMasterEventHandler = function(path){      
	var masterID = getMasterID(path);
    $('#path').fileTree( {script : "/p/${sessionID}/"+masterID,
        				 root : "/p/${sessionID}/"+masterID+"/"},
        				 pathEventHandler
	       				  );
	   /*
	   //   $('#question').fileTree( {script : "/q/${sessionID}/"+masterID, root: "/q/${sessionID}/"+masterID},
	   //  selectQuestionEventHandler );
	   */
    };
    
$(function() {
    var border = RUZEE.ShadedBorder.create({ corner:8, shadow:16, border:2 });
    /*
    $('#master').fileTree( {script : "/m/${sessionID}", 
                            root: "/m/${sessionID}/"}, 
    						selectMasterEventHandler );
     */  
     
    $('#path').dynatree({
      onActivate: function(dtnode){
      },
	  persist: true,
      title: "Path",
      key: "/",
      rootVisible: false,
      minExpandLevel: 1,
      autoFocus:true,
      activeVisible: true,
      checkbox: true,
      selectMode: 3,
      fx: {height:"toggle", duration:100},
      // Low level event handlers: onEvent(dtnode, event): return false, to stop default processing
    onClick: null, // null: generate focus, expand, activate, select events.
    onDblClick: null, // (No default actions.)
    onKeydown: null, // null: generate keyboard navigation (focus, expand, activate).
    onKeypress: null, // (No default actions.)
    onFocus: null, // null: handle focus.
    onBlur: null, // null: handle unfocus.

    // Pre-event handlers onQueryEvent(flag, dtnode): return false, to stop processing
    onQueryActivate: null, // Callback(flag, dtnode) before a node is (de)activated.
    onQuerySelect: null, // Callback(flag, dtnode) before a node is (de)selected.
    onQueryExpand: null, // Callback(flag, dtnode) before a node is expanded/collpsed.
    
    // High level event handlers
    onPostInit: null, // Callback(isReloading, isError) when tree was (re)loaded.
    onActivate: null, // Callback(dtnode) when a node is activated.
    onDeactivate: null, // Callback(dtnode) when a node is deactivated.
    onSelect: null, // Callback(flag, dtnode) when a node is (de)selected.
    onExpand: null, // Callback(flag, dtnode) when a node is expanded.
    onLazyRead: null, // Callback(dtnode) when a lazy node is expanded for the first time.
    
    ajaxDefaults: { // Used by initAjax option
        cache: false, // false: Append random '_' argument to the request url to prevent caching.
        dataType: "json" // Expect json format and pass json object to callbacks.
    }
    
    }
    );
    border.render('consolePanelFrame');
});
</script>
</head>
<body>

  <div id="pageTitle" class="pageTitle">mouse over here!</div>

  <form id='consoleForm' action='' method='post'>

   <div id="consolePanelFrame" class="console fixedAtTop">
		<div id="frame" class="box">
		<div id="panel">
		
		<h2 id="consoleTitle" style="float:right;">[title]
		  <img id="consoleCloseButton" alt="close button" src="/jar/image/close.gif" style="cursor: default;">
		</h2>
		
		<div>
			<div>
			  <div><h4>Master</h4>
			    <div style="border-style:solid; border-width: 1px; background:#fff; width:25%; height:20%;" id="master"></div>
			  </div>
			  <div style="float:left;"><h4>Path</h4>
			    <div style="border-style:solid; border-width: 1px; background:#fff; width:25%; height:20%;" id="path">..</div>
			  </div>
			  <div style="float:left;"><h4>Row</h4>
			    <div style="border-style:solid; border-width: 1px; background:#fff; width:25%; height:20%;" id="row"></div>
			  </div>
			  <div style="clear:both;"></div>
			</div>
			<div style="clear:both;"></div>
			<div><h4>Question</h4>
			  <div style="border-style:solid; border-width: 1px; background:#fff;width:60%; height:17%;" id="question"></div>
			  <div style="clear:both;"></div>
			</div>
		</div>

<!--		
		<div id="showLinkDiv" style="clear:both; display:none;">
		<span style="margin-left: 2em; font-size: 70%"><a id="linkURIAnchor2" href="">LINK_TO_THIS_PAGE</a>:LINK_TO_THIS_PAGE_MESSAGE:</span>
		<input type="text" id="linkURI" size="50" style="width: 28em"/></div>
-->
		</div>
		</div>
   </div>

 </form>

  <div id="padding"></div>
  <form id="contentsForm" action="" method='post'>
  <div id="contentsPanel">
     <hr/>
  </div>
  </form>
  
  <div id="logger">
  </div>
  
  <div id="prevPageButton" class="fixedAtSouthWestCorner" style="display:none" >
   <img src="/jar/image/leftArrow.png" alt="&lt;&lt; prev"
   onclick=""
   onmouseover="$('pageTitle').style.display='block'; this.setAttribute('src', '../image/leftArrow.gif')" onmouseout="$('pageTitle').style.display='none'; this.setAttribute('src', '../image/leftArrow.png')"/>
  </div>
   
  <div id="pageTitle" class="fixedAtBottom" style="display:none; text-align:center" >pageTitle</div>

  <div id="nextPageButton" class="fixedAtSouthEastCorner" style="display:none" >
   <img src="/jar/image/rightArrow.png" alt="next　&gt;&gt;"
   onclick=""
   onmouseover="$('pageTitle').style.display='block'; this.setAttribute('src', '../image/rightArrow.gif')" onmouseout="$('pageTitle').style.display='none'; this.setAttribute('src', '../image/rightArrow.png')"/>
  </div>

  <div id="messageDialog">
  </div>

  
</body>
</html>
