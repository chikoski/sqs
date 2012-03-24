<!DOCTYPE html 
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<link rel="stylesheet" href="../../css/smp.css" type="text/css" media="screen,print" />
<#setting url_escaping_charset='UTF-8'>

<#if path == "">
<#assign folderPrefix = "">
<#else>
<#assign folderPrefix = "フォルダ ">
</#if>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>${title?html}:${folderPrefix}${path}:${textAreaColumnMetadata.getLabel()}:自由記述欄一覧</title>
</head>
<body>
<div id="wrap">
<div id="header">
 <#if 0 < (title?length + path?length)>
 <h1 id="title">${title}:${folderPrefix}${path}</h1>
 </#if>
 <h2 id="pagetitle">処理結果:自由記述欄一覧</h2>
 
 <br/>
 
 <h3 class="question">${textAreaColumnMetadata.getLabel()}
 <#list textAreaColumnMetadata.getHints() as hint>
${hint?html}<br/>
</#list>
<!--<span>(調査票原稿内の ${textAreaColumnMetadata.getPage()}ページ目)</span>-->
  </h3>
</div>

<div id="content">

<#if 1 != textAreaColumn.getTextAreaRowRangeListSize()>

<ul>
<#list textAreaColumn.getTextAreaRowRangeList() as textAreaRowRange>

<#assign subFolderPath = textAreaRowRange.getTextAreaRowRangeMetadata().getSourceDirectory().getPath()>
<#if subFolderPath == "">
<#assign subFolderPrefix = "">
<#else>
<#assign subFolderPrefix = "フォルダ ">
</#if>

<li>${subFolderPrefix}${subFolderPath}<#if 0 < subFolderPath?length>:</#if>  
[<a href="${textAreaRowRange.getTextAreaRowRangeMetadata().getPath()}/${textAreaRowRange.getTextAreaRowRangeMetadata().getRowRangeIndex()}.html">
<#if textAreaRowRange.getTextAreaRowRangeMetadata().getStartRow() == textAreaRowRange.getTextAreaRowRangeMetadata().getEndRow()>
${textAreaRowRange.getTextAreaRowRangeMetadata().getStartRow()}行目
<#else>
${textAreaRowRange.getTextAreaRowRangeMetadata().getStartRow()}行目 - ${textAreaRowRange.getTextAreaRowRangeMetadata().getEndRow()}行目
</#if>
</a>]
</li>
</#list>
</ul>

<#else>

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

</#if>
</div>
</div>
</body>
</html>
