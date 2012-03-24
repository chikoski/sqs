<!DOCTYPE html 
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<head>
<link rel="stylesheet" href="../css/smp.css" type="text/css" media="screen,print" />
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
