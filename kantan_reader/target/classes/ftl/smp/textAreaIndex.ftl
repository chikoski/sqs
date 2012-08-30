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
<link rel="stylesheet" href="../css/base.css" type="text/css" media="screen,print" />
<#setting url_escaping_charset='UTF-8'>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<#if path == "">
<#assign folderPrefix = "">
<#else>
<#assign folderPrefix = "フォルダ ">
</#if>
<title>${title?html}:${folderPrefix}${path}:自由記述欄一覧 </title>
</head>
<body>
<div id="wrap">
<div id="header">
 <#if 0 < (title?length + folderPrefix?length + path?length)>
 <h1 id="title">${title?html}：${folderPrefix} ${path}</h1>
 </#if>
 <h2 id="pagetitle">処理結果:自由記述欄一覧</h2>
</div>

<div id="content">

<#list textareas as textarea>

<h3 class="question"><a href="${textarea.getColumnIndex()}/index.html">${textarea.getLabel()}
<#list textarea.getHints() as hint>
${hint?html}<br/>
</#list></a>
</h3>
<!--<p> (調査票原稿内の ${textarea.getPage()}ページ目)</p>-->
</#list>

</div>

</div>
</body>
</html>
