<!DOCTYPE html 
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<link rel="stylesheet" href="css/smp.css" type="text/css" media="screen,print" />
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
<li><a href="${xlsFilePath}">XLSファイル形式</a></li>
<li><a href="${csvFilePath}">CSVファイル形式</a></li>
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
