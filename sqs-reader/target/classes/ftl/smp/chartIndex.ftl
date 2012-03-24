<!DOCTYPE html 
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<link rel="stylesheet" href="../css/smp.css" type="text/css" media="screen,print" />
<#setting url_escaping_charset='UTF-8'>
<#if path == "">
<#assign folderPrefix = "">
<#else>
<#assign folderPrefix = "フォルダ ">
</#if>
<#macro itemValue value total unitLabel><#assign percent = value/total>${value}${unitLabel}(${percent?string.percent})</#macro>
<#macro chd chart><#list chart.getChartItemList() as chartItem><#if chartItem_index != 0>,</#if>${chartItem.getValue()?url}</#list></#macro>
<#macro chl chart total unitLabel><#list chart.getChartItemList() as chartItem><#if chartItem_index != 0>|</#if>${chartItem.getFormArea().getItemValue()?url}+${chartItem.getFormArea().getItemLabel()?url}+%3d+<@itemValue chartItem.getValue() total unitLabel/></#list></#macro>
<#macro chdl chart><#list chart.getChartItemList() as chartItem><#if chartItem_index != 0>|</#if>${chartItem.getFormArea().getItemValue()?url}+${chartItem.getFormArea().getItemLabel()?url}+%3d+<@itemValue chartItem.getValue() numRows unitLabel/></#list></#macro>

<#assign noAnswerLabel = "無回答">
<#assign errorLabel = "多重回答エラー">
<#assign totalLabel = "計">
<#assign unitLabel = "人">
<#assign piechart_url = "http://chart.apis.google.com/chart?chs=640x200&cht=p">
<#assign barchart_url = "http://chart.apis.google.com/chart?chs=640x200&cht=bhg">

<#assign labelLevel0 = "">
<#assign labelLevel1 = "">
<#assign labelLevel2 = "">
<#assign prevLabelLevel0 = "-">
<#assign prevLabelLevel1 = "-">
<#assign prevLabelLevel2 = "-">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>${title?html}:${folderPrefix}${path}: 集計結果一覧 </title>
<link rel="stylesheet" type="text/css" media="screen,print" />
<style type="text/css">
th.itemValue{
}
th.itemLabel{
}
table.vertical tbody tr td.value{
  padding-left: 1em;
  padding-right: 1em;
  text-align: right;
}
table.vertical tbody tr td.percent{
  padding-left: 1em;
  padding-right: 1em;
  text-align: right;
}

tr.noAnswerError {
}

tr.multipleAnswersError {
}

tr.total {
}

</style>
</head>
<body>
<div id="wrap">

<div id="header">
 <#if 0 < (title?length + folderPrefix?length + path?length)>
 <h1 id="title">${title?html}:${folderPrefix}${path}</h1>
 </#if>
 <h2 id="pagetitle">処理結果:チャート一覧</h2>
 </div>

	<div id="content">

<#list charts as chart>

<#if 3 <= chart_index && chart_index % 2 == 1>
<#assign pageBreakAfter = 'always'>
<#else>
<#assign pageBreakAfter = 'inherit'>
</#if>

		<div class="container" style="page-break-inside:avoid; page-break-after: ${pageBreakAfter}">

<#assign formArea = chart.getPrimaryFormArea()>
<#assign questionIDNum = chart.getPrimaryFormArea().getColumnIndex() + 1 >
<#assign labelLevel0 = chart.getPrimaryFormArea().getLabelArray()[0]>

<#if labelLevel0 != prevLabelLevel0>  
 <h3 class="question">${labelLevel0?html} ${formArea.getHints()[0]}</h3>
 <#assign prevLabelLevel1 = "-">
 <#assign prevLabelLevel2 = "-">
</#if>

<#if 1 < chart.getPrimaryFormArea().getLabelArray()?size>
 <#assign labelLevel1 = chart.getPrimaryFormArea().getLabelArray()[1]>
 <#if labelLevel1 != prevLabelLevel1>  
  <h4 class="s_question" style="page-break-before:avoid;">${labelLevel1?html}. ${formArea.getHints()[1]}</h4>
  <#assign prevLabelLevel2 = "-">
 </#if>

 <#if 2 < chart.getPrimaryFormArea().getLabelArray()?size>
  <#assign labelLevel2 = chart.getPrimaryFormArea().getLabelArray()[2]>
 </#if>
 <#if labelLevel2 != prevLabelLevel2>  
  <h4 style="page-break-before:avoid;">${formArea.getHints()[2]}</h4>
 </#if>
</#if>

<!-- 
 <p style="page-break-before:avoid;">(
<#if formArea.getType() == "select1">
択一選択設問
<#elseif formArea.getType() == "select">
複数選択設問
</#if>
)</p>
-->

<!--
<p style="page-break-before:avoid;">
<#list formArea.getHints() as hint>
${hint}<br/>
</#list>
</p>
-->

<#if formArea.isSelect() || 0 < numRows-chart.getNumMultipleAnswers() >
 <#if false>

  <table border="0" cellspacing="0" cellpadding="0" class="chart" style="page-break-before:avoid;">
  <tbody>
  
  <#list chart.getChartItemList() as chartItem>
    <tr>
    <th class="itemValue">${chartItem.getFormArea().getItemValue()?html}</th>
    <th class="itemLabel">${chartItem.getFormArea().getItemLabel()?html}</th>
    <td class="value">${chartItem.getValue()?html}</td> 
<#if formArea.isSelect1()>
    <td class="percent"><#if 0 < (numRows-chart.getNumMultipleAnswers())>${( 100 * chartItem.getValue() / (numRows-chart.getNumMultipleAnswers()))?int}%</#if></td>
<#else>
    <td class="percent"><#if 0 < numRows>${( 100 * chartItem.getValue() / numRows)?int}%</#if></td>
</#if>
    </tr>
  </#list>
  
  <#if formArea.isSelect1()>
    <tr class="noAnswerError">
    <th class="itemValue"></th>
    <th class="itemLabel">${noAnswerLabel}</th>
    <td class="value">${chart.getNumNoAnswers()}</td>
    <td class="percent">${(100 * chart.getNumNoAnswers() / (numRows-chart.getNumMultipleAnswers()))?int}%</td>
    </tr>

    <tr class="total">
    <th colspan="2" class="itemLabel">${totalLabel}</th>
    <td class="value">${numRows - chart.getNumMultipleAnswers()}</td>
    <td class="percent">100%</td>
    </tr>
  </#if>
  </tbody>
  </table>

 <#else>

  <table border="0" cellspacing="0" cellpadding="0" class="chart vertical">
  <tbody>
  
<#if false><!-- show itemValue-->
  <tr>
  <#list chart.getChartItemList() as chartItem>
    <th class="itemValue">${chartItem.getFormArea().getItemValue()?html}</th>
  </#list>
  <#if formArea.isSelect1()>
    <th rowspan="2" class="itemValue noAnswerError">${noAnswerLabel}</th>
    <th rowspan="2" class="itemValue total">${totalLabel}</th>
  </#if>
  </tr>

  <tr>
  <#list chart.getChartItemList() as chartItem>
    <th class="itemLabel">${chartItem.getFormArea().getItemLabel()?html}</th>
  </#list>
  <#if formArea.isSelect1()>
  </#if>
  </tr>
  
<#else>

  <tr>
  <#if formArea.isSelect1()>
    <#assign width = (100 / (chart.getChartItemList()?size+2)) >
    
    <#list chart.getChartItemList() as chartItem>
      <th width="${width}%" class="itemLabel">${chartItem.getFormArea().getItemLabel()?html}</th>
    </#list>
    <th width="${width}%" class="itemValue noAnswerError">${noAnswerLabel}</th>
    <th width="${width}%" class="itemValue total">${totalLabel}</th>
  <#else>
    <#assign width = 100 / ((chart.getChartItemList()?size)) >
    <#list chart.getChartItemList() as chartItem>
      <th width="${width}%" class="itemLabel">${chartItem.getFormArea().getItemLabel()?html}</th>
    </#list>
  </#if>
  </tr>
</#if>
  <tr>
  <#list chart.getChartItemList() as chartItem>
    <td class="value">${chartItem.getValue()?html}</td> 
  </#list>
  <#if formArea.isSelect1()>
    <td class="value">${chart.getNumNoAnswers()}</td>
    <td class="value">${numRows - chart.getNumMultipleAnswers()}</td>
  </#if>
  </tr>

  <tr>
  <#list chart.getChartItemList() as chartItem>
   <#if formArea.isSelect1()>
    <td class="percent">${( 100 * chartItem.getValue() / (numRows-chart.getNumMultipleAnswers()) )?int}%</td>
   <#else>
    <td class="percent">${( 100 * chartItem.getValue() / numRows )?int}%</td>
   </#if>
  </#list>

  <#if formArea.isSelect1()>
    <td class="percent">${(100 * chart.getNumNoAnswers() / (numRows-chart.getNumMultipleAnswers()))?int}%</td>
    <td class="percent">100%</td>
  </#if>
  </tr>

  </tbody>
  </table>
 </#if>
</#if>


<#if formArea.isSelect1() && 0 < chart.getNumMultipleAnswers() && 0 < numRows-chart.getNumMultipleAnswers()>
<p style="page-break-before:avoid;">※ ${errorLabel}:${chart.getNumMultipleAnswers()} 
(${(100 * chart.getNumMultipleAnswers() / numRows)?int}%)を除く</p>
</#if>

<#if exportChartImageMode>

<#if formArea.isSelect1()>
<img alt="chart" src="${formArea.getColumnIndex()}/pie.png" style="page-break-before:avoid;"/>
<#else>
<img alt="chart" src="${formArea.getColumnIndex()}/bar.png" style="page-break-before:avoid;"/>
</#if>

<#else>

<#if formArea.isSelect1()>
<#assign total = numRows - chart.getNumMultipleAnswers()>
<#if 0 < total>
<img alt="chart" src="${piechart_url}&chd=t:<@chd chart/>,${chart.getNumNoAnswers()}&chl=<@chl chart total unitLabel/>|${noAnswerLabel}+%3d+<@itemValue chart.getNumNoAnswers() total unitLabel/>"
 style="page-break-before:avoid;"/>
</#if>
<#else>
<#if 0 < numRows>
<img alt="chart" src="${barchart_url}&chd=t:<@chd chart/>&chdl=<@chdl chart/>"
 style="page-break-before:avoid;"/>
</#if>
</#if>

</#if>


<#assign prevLabelLevel0 = labelLevel0>
<#if 1 < chart.getPrimaryFormArea().getLabelArray()?size>
 <#assign prevLabelLevel1 = labelLevel1>
  <#if 2 < chart.getPrimaryFormArea().getLabelArray()?size>
   <#assign prevLabelLevel2 = labelLevel2>
 </#if>
</#if>

		</div><!--class:container end-->

</#list>

	</div><!--id:content end-->

</div><!--id:wrap end-->

</body>
</html>
