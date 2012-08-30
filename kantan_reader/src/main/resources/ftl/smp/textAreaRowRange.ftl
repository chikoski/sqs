<!DOCTYPE html 
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<!--
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
 -->
<head>
<link rel="stylesheet" href="../../css/base.css" type="text/css" media="screen,print" />
<#setting url_escaping_charset='UTF-8'>

<#if path == "">
<#assign folderPrefix = "">
<#else>
<#assign folderPrefix = "フォルダ ">
</#if>

<#if textAreaRowRangeMetadata.getStartRow() == textAreaRowRangeMetadata.getEndRow()>
<#assign rowRange = textAreaRowRangeMetadata.getStartRow() + "行目">
<#else>
<#assign rowRange = textAreaRowRangeMetadata.getStartRow() + "行目-"+ textAreaRowRangeMetadata.getEndRow() + "行目">
</#if>
<title>
${title?html}:${folderPrefix}${path}:${textAreaColumnMetadata.getLabel()}:[${rowRange}] (${textAreaRowRangeMetadata.getRowRangeIndex()+1}/${textAreaColumnMetadata.getNumPages()}):自由記述欄一覧</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<div id="wrap">

<div id="header">
 <#if 0 < (title?length + path?length)>
 <h1 id="title">${title}</h1>
 </#if>
 <h2 id="pagetitle">処理結果:自由記述欄一覧</h2>
 
 <br/>
 
  <h3 class="question">${textAreaColumnMetadata.getLabel()}
 <#list textAreaColumnMetadata.getHints() as hint>
${hint?html}<br/>
</#list>
  </h3>
  <!--
<span>(調査票原稿内の ${textAreaColumnMetadata.getPage()}ページ目)</span>
 -->

<h3>${folderPrefix}${path}</h3>
<h4>[${rowRange}](${textAreaRowRangeMetadata.getRowRangeIndex() + 1}/${textAreaColumnMetadata.getNumPages()})</h4>

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
