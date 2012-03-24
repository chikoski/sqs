<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<link rel="stylesheet" href="../css/${skin}.css" type="text/css" media="screen,print" />
<#setting url_escaping_charset='UTF-8'>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

<#if path == "">
<#assign folderPrefix = "">
<#else>
<#assign folderPrefix = folderPrefixLabel>
</#if>
<title>${title?html}:${folderPrefix}${path}:${listOfFreeAnswersLabel}</title>
</head>
<body>
<div id="wrap">
<div id="header">

 <h1 id="pagetitle">${listOfFreeAnswersLabel}</h1>
 <#if 0 < (title?length + path?length)>
 <h2 id="title">${folderPrefix}${path}</h2>
 </#if>
</div>

<div id="content">

<ul>

<#list textareas as textarea>
<li class="question"><a href="${textarea.getQuestionIndex()}/index.html">${textarea.getLabel()}
<#list textarea.getHints() as hint>
${hint?html}<br/>
</#list></a>
</li>
</#list>

</ul>
</div>

</div>
</body>
</html>
