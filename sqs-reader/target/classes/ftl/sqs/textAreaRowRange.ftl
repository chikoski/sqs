<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<link rel="stylesheet" href="../../css/${skin}.css" type="text/css" media="screen,print" />
<#setting url_escaping_charset='UTF-8'>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

<#if path == "">
<#assign folderPrefix = "">
<#else>
<#assign folderPrefix = folderPrefixLabel>
</#if>

<#if textAreaRowRangeMetadata.getStartRowIndex() == textAreaRowRangeMetadata.getEndRowIndex()>
<#assign rowRange = rowNumberPrefixLabel + (1+textAreaRowRangeMetadata.getStartRowIndex()) + rowNumberSuffixLabel>
<#else>
<#assign rowRange = rowNumberPrefixLabel + (1+textAreaRowRangeMetadata.getStartRowIndex()) + rowNumberSuffixLabel + "-"+ rowNumberPrefixLabel + (1+textAreaRowRangeMetadata.getEndRowIndex()) + rowNumberSuffixLabel>
</#if>
<title>${folderPrefix}${path}:${textAreaColumnMetadata.getLabel()}:[${rowRange}](Page ${textAreaRowRangeMetadata.getRowRangeIndex()+1} of ${numRowRangePages}):${listOfFreeAnswersLabel}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<div id="wrap">

<div id="header">
 
 <h1 id="pagetitle">${listOfFreeAnswersLabel}</h1>
 
 <h2>${folderPrefix}${path}</h2>

 <h3 class="question">${textAreaColumnMetadata.getLabel()}
 <#list textAreaColumnMetadata.getHints() as hint>
${hint?html}<br/>
</#list>
 </h3>
  
 <h4>[${rowRange}](Page ${1+textAreaRowRangeMetadata.getRowRangeIndex()} of ${numRowRangePages})</h4>
</div>

<div id="content">

<ul class="textarea">
<#list textAreaImageItemList as textAreaImageItem>
<#assign rowID = textAreaImageItem.getRowIndex() + 1>
<li><span class="rowIndex"> ${rowID}</span>
<img src="${textAreaImageItem.getRowIndex()}.png" alt="${textAreaImageItem.getRowIndex()}"/>
<#if 0 < textAreaImageItem.getValue()?length>  :
 ${textAreaImageItem.getValue()?html} 
</#if>
</li>
</#list>
</ul>

</div>

</div>
</body>
</html>
