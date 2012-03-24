<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<link rel="stylesheet" href="css/${skin}.css" type="text/css" media="screen,print" />
<#setting url_escaping_charset='UTF-8'>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<#if 0 < path?length>
 <title>${folderPrefixLabel} ${path?html}:${contentsOfResultLabel}</title>
 <#else>
 <title>${contentsOfResultLabel}</title>
</#if>
 
</head>
<body>

<div id="wrap">

	<div id="header">

	<h1 id="title">${folderPrefixLabel} ${path}</h1>
	<h2 id="pagetitle">${contentsOfResultLabel}</h2>

	</div>
	
	<div id="content">

	<div id="containter">

<div class="block">
<h3 class="category">${listOfSpreadSheetsLabel}</h3>
<ul class="link">
<li><a href="${xlsFilePath}">${xlsSpreadSheetLabel}</a></li>
<li><a href="${csvFilePath}">${csvSpreadSheetLabel}</a></li>
</ul>
</div>

<div class="block">
<h3 class="category">${listOfResultsLabel}</h3>
<ul class="link">
<li><a href="TEXTAREA/index.html">${listOfFreeAnswersLabel}</a></li>
<li><a href="CHART/index.html">${listOfStatisticsLabel}</a></li>
</ul>
</div>
</div>
</div>

<div id="subFolders">

<#if 0 < numChildSourceDirectories >
<h3 class="category">${showSubFolders}</h3>
<ul>
<#list sourceDirectory.getChildSourceDirectoryList() as subSourceDirectory>
 <li><a href="../${subSourceDirectory.getRelativePath()}/${resultFolderName}/index.html">${subSourceDirectory.getRelativePath()}</a></li>
</#list>
</ul>
</#if>

</div>


</div><!-- id="wrap" -->
	
</body>
</html>
