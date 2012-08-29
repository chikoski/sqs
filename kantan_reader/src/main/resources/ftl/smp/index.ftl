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
<link rel="stylesheet" href="css/base.css" type="text/css" media="screen,print" />
<#setting url_escaping_charset='UTF-8'>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>${title?html}: 処理結果一覧</title>
</head>
<body>

<div id="wrap">

	<div id="header">

	<h1 id="title">${title?html}</h1>
	<h2 id="pagetitle">処理結果一覧</h2>

	</div>
	
	<div id="content">

	<div id="containter">

<div class="block">
<h3 class="category">表データの表示</h3>
<ul class="link">
<li><a href="${xlsFileName}">XLSファイル形式</a></li>
<li><a href="${csvFileName}">CSVファイル形式</a></li>
</ul>
</div>

<div class="block">
<h3 class="category">処理結果表示</h3>
<ul class="link">
<li><a href="TEXTAREA/index.html">自由記述欄一覧</a></li>
<li><a href="CHART/index.html">集計結果一覧</a></li>
</ul>
</div>

    </div>
    </div>
</div>
	
</body>
</html>
