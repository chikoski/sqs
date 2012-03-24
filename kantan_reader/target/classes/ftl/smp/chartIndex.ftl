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
<#if path == "">
<#assign folderPrefix = "">
<#else>
<#assign folderPrefix = "フォルダ">
</#if>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>集計結果一覧: ${folderPrefix} ${path}</title>
</head>
<body>

<h1>集計結果一覧: ${folderPrefix} ${path}</h1>

<#macro itemValue value total unitLabel><#assign percent = value/total>${value}${unitLabel}(${percent?string.percent})</#macro>
<#macro chd chart><#list chart.getChartItemList() as chartItem><#if chartItem_index != 0>,</#if>${chartItem.getValue()?url}</#list></#macro>
<#macro chl chart total unitLabel><#list chart.getChartItemList() as chartItem><#if chartItem_index != 0>|</#if>${chartItem.getFormArea().getItemValue()?url}+${chartItem.getFormArea().getItemLabel()?url}+%3d+<@itemValue chartItem.getValue() total unitLabel/></#list></#macro>
<#macro chdl chart><#list chart.getChartItemList() as chartItem><#if chartItem_index != 0>|</#if>${chartItem.getFormArea().getItemValue()?url}+${chartItem.getFormArea().getItemLabel()?url}+%3d+<@itemValue chartItem.getValue() numRows unitLabel/></#list></#macro>

<#assign noAnswerLabel = "無回答">
<#assign errorLabel = "エラー">
<#assign totalLabel = "計">
<#assign unitLabel = "人">
<#assign piechart_url = "http://chart.apis.google.com/chart?chs=640x200&cht=p">
<#assign barchart_url = "http://chart.apis.google.com/chart?chs=640x200&cht=bhg">

<#list charts as chart>
<#assign formArea = chart.getDefaultFormArea()>
<#assign questionIDNum = chart.getDefaultFormArea().getColumnIndex() + 1 >

 <h2>設問 ${questionIDNum}</h2>
 
 <p>(
<#if formArea.getType() == "select1">
択一選択設問
<#elseif formArea.getType() == "select">
複数選択設問
</#if>
)</p>

<p>
<#list formArea.getHints() as hint>
${hint}<br/>
</#list>
</p>

<table border="1">
<tbody>
<#list chart.getChartItemList() as chartItem>
<tr>
<th>${chartItem.getFormArea().getItemValue()?html}</th>
<th>${chartItem.getFormArea().getItemLabel()?html}</th>
<td>${chartItem.getValue()?html}</td> 
</tr>
</#list>
<#if formArea.isSelect1()>
<tr><th></th><th>${noAnswerLabel}</th><td>${chart.getNumNoAnswers()}</td></tr>
<tr><th></th><th>${errorLabel}</th><td>${chart.getNumMultipleAnswers()}</td></tr>
<tr><th colspan="2">${totalLabel}</th><td>${numRows}</td></tr>
</#if>
</tbody>
</table>

<#if exportChartImageMode>

<#if formArea.isSelect1()>
<img alt="chart" src="${formArea.getColumnIndex()}/pie.png"/>
<#else>
<img alt="chart" src="${formArea.getColumnIndex()}/bar.png"/>
</#if>

<#else>

<#if formArea.isSelect1()>
<#assign total = numRows - chart.getNumMultipleAnswers()>
<#if 0 < total>
<img alt="chart" src="${piechart_url}&chd=t:<@chd chart/>,${chart.getNumNoAnswers()}&chl=<@chl chart total unitLabel/>|${noAnswerLabel}+%3d+<@itemValue chart.getNumNoAnswers() total unitLabel/>"/>
</#if>
<#else>
<#if 0 < numRows>
<img alt="chart" src="${barchart_url}&chd=t:<@chd chart/>&chdl=<@chdl chart/>"/>
</#if>
</#if>

</#if>


</#list>

</body>
</html>
