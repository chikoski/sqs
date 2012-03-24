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

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>${folderPrefix}${path}:${textAreaColumnMetadata.getLabel()}:${listOfFreeAnswersLabel}</title>
</head>
<body>
<div id="wrap">
<div id="header">
 <h1 id="pagetitle">${listOfFreeAnswersLabel}</h1>
 <#if 0 < (title?length + path?length)>
 <h2 id="title">${folderPrefix}${path}</h2>
 </#if>
  
 <h3 class="question">${textAreaColumnMetadata.getLabel()}
 <#list textAreaColumnMetadata.getHints() as hint>
${hint?html}<br/>
</#list>
  </h3>
</div>

<div id="content">

<#if 1 == textAreaColumn.getTextAreaRowGroupSize() && 1 == textAreaColumn.getLastTextAreaRowGroup().getTextAreaRowRangeSize()>

<!-- single row group -->

<#assign textAreaRowRange = textAreaColumn.getLastTextAreaRowGroup().getLastTextAreaRowRange()>
<#assign textAreaImageItemList = textAreaRowRange.getTextAreaImageItemList()>

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

<#else>
     
<!-- multiple row groups -->
<ul>
<#list textAreaColumn.getTextAreaRowGroupList() as textAreaRowGroup>

  <#assign subFolderPath = textAreaRowGroup.getSourceDirectory().getRelativePath()>
  <#if subFolderPath == "">
    <#assign subFolderPrefixLabel = "">
  <#else>
    <#assign subFolderPrefixLabel = folderPrefixLabel>
  </#if>
 
 <#if subFolderPath != path>
  <#assign targetPath = "${returnPath}/${subFolderPath}/${resultFolder}/TEXTAREA/${textAreaColumn.getTextAreaColumnMetadata().getQuestionIndex()}">
  <li><a href="${targetPath}/index.html">${subFolderPrefixLabel}${subFolderPath}</a>
  <ul>
  <#list textAreaRowGroup.getTextAreaRowRangeList() as textAreaRowRange>  
  <li>  
    <a href="${targetPath}/${textAreaRowRange.getTextAreaRowRangeMetadata().getRowRangeIndex()}.html">
    <#if textAreaRowRange.getTextAreaRowRangeMetadata().getStartRowIndex() == textAreaRowRange.getTextAreaRowRangeMetadata().getEndRowIndex()>
${(1+textAreaRowRange.getTextAreaRowRangeMetadata().getStartRowIndex())}${rowNumberSuffixLabel}
    <#else>
${rowNumberPrefixLabel}${(1+textAreaRowRange.getTextAreaRowRangeMetadata().getStartRowIndex())}${rowNumberSuffixLabel} - 
${rowNumberPrefixLabel}${(1+textAreaRowRange.getTextAreaRowRangeMetadata().getEndRowIndex())}${rowNumberSuffixLabel}
    </#if>
  </a>
  </li>
  </#list>
  </ul>
  </li>
  
 <#else>

  <#list textAreaRowGroup.getTextAreaRowRangeList() as textAreaRowRange>  
  <li>  
    <a href="${returnPath}/${subFolderPath}/${resultFolder}/TEXTAREA/${textAreaColumn.getTextAreaColumnMetadata().getQuestionIndex()}/${textAreaRowRange.getTextAreaRowRangeMetadata().getRowRangeIndex()}.html">
    <#if textAreaRowRange.getTextAreaRowRangeMetadata().getStartRowIndex() == textAreaRowRange.getTextAreaRowRangeMetadata().getEndRowIndex()>
${rowNumberPrefixLabel}${(1+textAreaRowRange.getTextAreaRowRangeMetadata().getStartRowIndex())}${rowNumberSuffixLabel}
    <#else>
${rowNumberPrefixLabel}${(1+textAreaRowRange.getTextAreaRowRangeMetadata().getStartRowIndex())}${rowNumberSuffixLabel} - 
${rowNumberPrefixLabel}${(1+textAreaRowRange.getTextAreaRowRangeMetadata().getEndRowIndex())}${rowNumberSuffixLabel}
    </#if>
  </a>
  </li>
  </#list>
 </#if>

</#list>
</ul>

</#if>
</div>
</div>
</body>
</html>
