<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ja" lang="ja">
<head>
<link rel="stylesheet" href="../css/${skin}.css" type="text/css" media="screen,print" />
<#setting url_escaping_charset='UTF-8'>

<#assign pageBreakPolicy = "sqs">

<#if path == "">
<#assign folderPrefix = "">
<#else>
<#assign folderPrefix = folderPrefixLabel>
</#if>
<#macro itemValue value total unitLabel><#assign percent = value/total>${value}${unitLabel}(${percent?string.percent})</#macro>
<#macro chd chart><#list chart.getChartItemList() as chartItem><#if chartItem_index != 0>,</#if>${chartItem.getValue()?url}</#list></#macro>
<#macro chl chart total unitLabel><#list chart.getChartItemList() as chartItem><#if chartItem_index != 0>|</#if>${chartItem.getFormArea().getItemValue()?url}+${chartItem.getFormArea().getItemLabel()?url}+%3d+<@itemValue chartItem.getValue() total unitLabel/></#list></#macro>
<#macro chdl chart><#list chart.getChartItemList() as chartItem><#if chartItem_index != 0>|</#if>${chartItem.getFormArea().getItemValue()?url}+${chartItem.getFormArea().getItemLabel()?url}+%3d+<@itemValue chartItem.getValue() numRows unitLabel/></#list></#macro>

<#assign piechart_url = "http://chart.apis.google.com/chart?chs=640x200&cht=p">
<#assign barchart_url = "http://chart.apis.google.com/chart?chs=640x200&cht=bhg">

<#assign labelLevel0 = "">
<#assign labelLevel1 = "">
<#assign labelLevel2 = "">
<#assign prevLabelLevel0 = "-">
<#assign prevLabelLevel1 = "-">
<#assign prevLabelLevel2 = "-">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>${title?html} ${folderPrefix}${path}: ${listOfStatisticsLabel}</title>
<link rel="stylesheet" type="text/css" media="screen,print" />
<style type="text/css">
th.itemValue{
}
th.itemLabel{
}
th.itemLabel.rightAnswer{
  color: #faa;
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
 <#if 0 < (title?length)>
 <h1 id="title">${title?html}</h1>
 </#if>
 <h1 id="pagetitle">${listOfStatisticsLabel}</h1>
 <h2>${folderPrefix}${path}</h2>
 </div>

	<div id="content">

<#list charts as chart>

<#if pageBreakPolicy == 'smp' && 3 <= chart_index && chart_index % 2 == 1>
 <#assign pageBreakAfter = 'always'>
<#else>
 <#assign pageBreakAfter = 'inherit'>
</#if>

	<div class="container" style="page-break-inside:avoid; page-break-after: ${pageBreakAfter}">

<#assign formArea = chart.getPrimaryFormArea()>
<#assign questionIDNum = chart.getPrimaryFormArea().getQuestionIndex() + 1 >
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
${selectSingleLabel}
<#elseif formArea.getType() == "select">
${selectMultipleLabel}
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

<#if formArea.isSelectMultiple() || 0 < numRows - chart.getNumMultipleAnswers() >
 <#if false>

  <table border="0" cellspacing="0" cellpadding="0" class="chart" style="page-break-before:avoid;">
  <tbody>
  
  <#list chart.getChartItemList() as chartItem>
    <tr>
    <th class="itemValue">${chartItem.getFormArea().getItemValue()?html}</th>
    <th class="itemLabel">${chartItem.getFormArea().getItemLabel()?html}</th>
    <td class="value">${chartItem.getValue()?html}</td> 
<#if formArea.isSelectSingle()>
    <td class="percent"><#if 0 < (numRows - chart.getNumMultipleAnswers())>${( 100 * chartItem.getValue() / (numRows - chart.getNumMultipleAnswers()))?int}%</#if></td>
<#else>
    <td class="percent"><#if 0 < numRows>${( 100 * chartItem.getValue() / numRows)?int}%</#if></td>
</#if>
    </tr>
  </#list>
  
  <#if formArea.isSelectSingle()>
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
  <#if formArea.isSelectSingle()>
    <th rowspan="2" class="itemValue noAnswerError">${noAnswerLabel}</th>
    <th rowspan="2" class="itemValue total">${totalLabel}</th>
  </#if>
  </tr>

  <tr>
  <#list chart.getChartItemList() as chartItem>
    <th class="itemLabel">${chartItem.getFormArea().getItemLabel()?html}</th>
  </#list>
  <#if formArea.isSelectSingle()>
  </#if>
  </tr>
  
<#else>

  <tr>
  <#if formArea.isSelectSingle()>
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
  <#if formArea.isSelectSingle()>
    <td class="value">${chart.getNumNoAnswers()}</td>
    <td class="value">${numRows - chart.getNumMultipleAnswers()}</td>
  </#if>
  </tr>

  <tr>
  <#list chart.getChartItemList() as chartItem>
   <#if formArea.isSelectSingle()>
    <td class="percent">${( 100 * chartItem.getValue() / (numRows-chart.getNumMultipleAnswers()) )?int}%</td>
   <#else>
    <td class="percent">${( 100 * chartItem.getValue() / numRows )?int}%</td>
   </#if>
  </#list>

  <#if formArea.isSelectSingle()>
    <td class="percent">${(100 * chart.getNumNoAnswers() / (numRows-chart.getNumMultipleAnswers()))?int}%</td>
    <td class="percent">100%</td>
  </#if>
  </tr>

  </tbody>
  </table>
 </#if>
</#if>


<#if formArea.isSelectSingle() && 0 < chart.getNumMultipleAnswers() && 0 < numRows-chart.getNumMultipleAnswers()>
<p style="page-break-before:avoid;">※ ${errorLabel}:${chart.getNumMultipleAnswers()} 
(${(100 * chart.getNumMultipleAnswers() / numRows)?int}%)${exceptErrorDescriptionLabel}</p>
</#if>

<#if exportChartImageMode>

<#if formArea.isSelectSingle()>
<img class="chart" alt="chart" src="${formArea.getQuestionIndex()}/pie.png" style="page-break-before:avoid;"/>
<#else>
<img class="chart"  alt="chart" src="${formArea.getQuestionIndex()}/bar.png" style="page-break-before:avoid;"/>
</#if>

<#else>

<#if formArea.isSelectSingle()>
<#assign total = numRows - chart.getNumMultipleAnswers()>
<#if 0 < total>
<img class="chart"  alt="chart" src="${piechart_url}&chd=t:<@chd chart/>,${chart.getNumNoAnswers()}&chl=<@chl chart total unitLabel/>|${noAnswerLabel}+%3d+<@itemValue chart.getNumNoAnswers() total unitLabel/>"
 style="page-break-before:avoid;"/>
</#if>
<#else>
<#if 0 < numRows>
<img class="chart"  alt="chart" src="${barchart_url}&chd=t:<@chd chart/>&chdl=<@chdl chart/>"
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
