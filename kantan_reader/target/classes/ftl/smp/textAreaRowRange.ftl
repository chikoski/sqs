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

<#setting url_escaping_charset='UTF-8'>
<#assign questionIDNum = textAreaColumnMetadata.getColumnIndex() + 1>

<#if path == "">
<#assign folderPrefix = "">
<#else>
<#assign folderPrefix = "フォルダ">

</#if>
<#if textAreaRowRangeMetadata.getStartRow() == textAreaRowRangeMetadata.getEndRow()>
<#assign rowRange = textAreaRowRangeMetadata.getStartRow() + "行目">
<#else>
<#assign rowRange = textAreaRowRangeMetadata.getStartRow() + "行目-"+ textAreaRowRangeMetadata.getEndRow() + "行目">
</#if>
<title>
自由記述欄一覧:${folderPrefix} ${path}: 設問${questionIDNum}: [${rowRange}] (${textAreaRowRangeMetadata.getRowRangeIndex()+1}/${textAreaColumnMetadata.getNumPages()})</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>

<h1>
自由記述欄一覧:
${folderPrefix} ${path}:
設問${questionIDNum}:
[${rowRange}]
(${textAreaRowRangeMetadata.getRowRangeIndex() + 1}/${textAreaColumnMetadata.getNumPages()})
</h1>

<p> (調査票原稿内の ${textAreaColumnMetadata.getPage()}ページ目)</p>
<p>
<#list textAreaColumnMetadata.getHints() as hint>
${hint?html}<br/>
</#list>
</p>


<ul>
<#list textAreaImageItemList as textAreaImageItem>
<#assign rowID = textAreaImageItem.getRowIndex() + 1>
<li> ${rowID} 行目:
<img src="${textAreaImageItem.getRowIndex()}.png" alt="${textAreaImageItem.getRowIndex()}"/>  :
 ${textAreaImageItem.getValue()?html} </li>
</#list>
</ul>

</body>
</html>
