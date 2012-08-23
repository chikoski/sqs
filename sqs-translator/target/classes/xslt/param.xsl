<?xml version="1.0" encoding="UTF-8"?>
	<!--
		Copyright 2004 SQS Development Team / Community Management Research
		Project. Licensed under the Apache License, Version 2.0 (the
		"License"); you may not use this file except in compliance with the
		License. You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0 Unless required by
		applicable law or agreed to in writing, software distributed under the
		License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
		CONDITIONS OF ANY KIND, either express or implied. See the License for
		the specific language governing permissions and limitations under the
		License.
	-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xml:space="preserve"
	version="1.0">

<xsl:param name="language">en</xsl:param>
<xsl:param name="localeSuffix">_en</xsl:param>

	<!--  hagaki size
		<xsl:param name="pageMasterPageWidth">284</xsl:param>
		<xsl:param name="pageMasterPageHeight">420</xsl:param>
	-->
		<xsl:param name="pageMasterPageWidth">595</xsl:param>
		<xsl:param name="pageMasterPageHeight">842</xsl:param>

		<xsl:param name="pageMasterMarginTop">0</xsl:param>
		<xsl:param name="pageMasterMarginBottom">0</xsl:param>
		<xsl:param name="pageMasterMarginLeft">0</xsl:param>
		<xsl:param name="pageMasterMarginRight">0</xsl:param>
		<xsl:param name="regionBodyMarginTop">50</xsl:param>
		<xsl:param name="regionBodyMarginBottom">50</xsl:param>
		<xsl:param name="regionBodyMarginLeft">0</xsl:param><!-- 14 -->
		<xsl:param name="regionBodyMarginRight">0</xsl:param><!-- 14 -->
		<xsl:param name="regionBeforeExtent">0</xsl:param>
		<xsl:param name="regionAfterExtent">0</xsl:param>
		<xsl:param name="regionStartExtent">0</xsl:param>
		<xsl:param name="regionEndExtent">0</xsl:param>
<!-- 	<xsl:param name="regionBodyWidth"><xsl:value-of select="number($pageMasterPageWidth) - number($regionBodyMarginLeft) - number($regionBodyMarginRight) - number($regionBeforeExtent) - number($regionAfterExtent)"/></xsl:param>-->
		<!-- <xsl:param name="regionBodyWidth"><xsl:value-of select="number($pageMasterPageWidth) - number($regionBeforeExtent) - number($regionAfterExtent)"/></xsl:param>-->
     <xsl:param name="regionBodyWidth"><xsl:value-of select="number($pageMasterPageWidth) - 90 "/></xsl:param>		

	<xsl:param name="pageSideStartingFrom">left</xsl:param>
	<xsl:param name="showStapleMark">true</xsl:param>
	<xsl:param name="showPageNumber">true</xsl:param>
	<xsl:param name="showEnqtitleBelowPagenum">true</xsl:param>
	<xsl:param name="showMarkingExample">true</xsl:param>
	
	<xsl:param name="sides">duplex</xsl:param>
	<!-- duplex, tumble -->

	<xsl:param name="stapleAreaWidth">60</xsl:param><!-- 20 -->
	<xsl:param name="stapleAreaHeight">60</xsl:param><!-- 60 -->
	<xsl:param name="stapleAreaTop">0</xsl:param><!-- 5 -->
	<xsl:param name="stapleAreaPadding">0</xsl:param>

	<xsl:param name="regionBeforeHeight">50</xsl:param>
	<xsl:param name="regionAfterHeight">50</xsl:param>
	
	<xsl:param name="deskewGuideBlockWidth">18</xsl:param>
	<xsl:param name="deskewGuideBlockHeight">18</xsl:param>
	
	<xsl:param name="markingExampleX">300</xsl:param>
	<xsl:param name="markingExampleY">18</xsl:param>
	<xsl:param name="markingExampleWidth">95</xsl:param>
	<xsl:param name="markingExampleHeight">38</xsl:param>
	<xsl:param name="markAreaWidth">5</xsl:param>
	<xsl:param name="markAreaHeight">16</xsl:param>

	<xsl:param name="enableMarkSheetMode">true</xsl:param>

	<xsl:variable name="fontFamily">Gothic</xsl:variable>
	<xsl:variable name="baseFontSizePt">10</xsl:variable>

	<xsl:param name="qid-label-width">20</xsl:param>
	<xsl:param name="item-form-width">9</xsl:param>
	<xsl:param name="item-label-width">12</xsl:param>

    <xsl:param name="qr-code-text"></xsl:param>

	<xsl:attribute-set name="page-number">
		<xsl:attribute name="font-family"><xsl:value-of select="$fontFamily"/></xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="font-size">8pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="zero">
		<xsl:attribute name="border-width">0px</xsl:attribute>
		<xsl:attribute name="padding">0</xsl:attribute>
		<xsl:attribute name="padding-top">0</xsl:attribute>
		<xsl:attribute name="padding-bottom">0</xsl:attribute>
		<xsl:attribute name="margin-top">0px</xsl:attribute>
		<xsl:attribute name="margin-bottom">0px</xsl:attribute>
		<xsl:attribute name="space-before">0px</xsl:attribute>
		<xsl:attribute name="space-after">0px</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="qtitle-below-page-number">
		<xsl:attribute name="font-family"><xsl:value-of select="$fontFamily"/></xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="font-size"><xsl:value-of select="number($baseFontSizePt) * 0.3 + 2" />pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="white">
		<xsl:attribute name="font-color">white</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="small">
		<xsl:attribute name="font-size"><xsl:value-of
			select="number($baseFontSizePt) * 0.7" />pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="underline">
		<xsl:attribute name="text-decoration">underline</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="h">
		<xsl:attribute name="space-before.optimum">20pt</xsl:attribute>
		<xsl:attribute name="margin-left">0pt</xsl:attribute>
		<xsl:attribute name="padding-top">5pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">2pt</xsl:attribute>
		<xsl:attribute name="font-size"><xsl:value-of select="number($baseFontSizePt) * 1.4" />pt</xsl:attribute>
		<xsl:attribute name="font-family"><xsl:value-of select="$fontFamily" /></xsl:attribute>
		<xsl:attribute name="font-weight">bold</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="p">
		<xsl:attribute name="space-before">1pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">2pt</xsl:attribute>
		<xsl:attribute name="font-family"><xsl:value-of select="$fontFamily" /></xsl:attribute>
		<xsl:attribute name="font-size"><xsl:value-of select="number($baseFontSizePt) * 1.0" />pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="question">
		<xsl:attribute name="padding-top">4pt</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="textbox">
		<xsl:attribute name="margin-left">5pt</xsl:attribute>
		<xsl:attribute name="margin-right">5pt</xsl:attribute>
		<xsl:attribute name="margin-bottom">5pt</xsl:attribute>
		<xsl:attribute name="padding-top">1pt</xsl:attribute>
		<xsl:attribute name="padding-bottom">1pt</xsl:attribute>
		<xsl:attribute name="padding-left">1pt</xsl:attribute>
		<xsl:attribute name="padding-right">1pt</xsl:attribute>
		<xsl:attribute name="border-width">0.4pt</xsl:attribute>
		<xsl:attribute name="border-color">gray</xsl:attribute>
		<xsl:attribute name="border-style">solid</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="th-block">
		<xsl:attribute name="space-before">0pt</xsl:attribute>
		<xsl:attribute name="font-family"><xsl:value-of select="$fontFamily" /></xsl:attribute>
		<xsl:attribute name="font-size"><xsl:value-of
			select="number($baseFontSizePt) * 0.9" />pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="td-block">
		<xsl:attribute name="space-before">0pt</xsl:attribute>
		<xsl:attribute name="font-family"><xsl:value-of select="$fontFamily" /></xsl:attribute>
		<xsl:attribute name="font-size"><xsl:value-of select="number($baseFontSizePt) * 0.9" />pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="td-block.item-form">
		<xsl:attribute name="space-before">2pt</xsl:attribute>
		<xsl:attribute name="margin-bottom">2pt</xsl:attribute>
		<xsl:attribute name="padding-right">0pt</xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="td-block.item-value">
		<xsl:attribute name="space-before">2pt</xsl:attribute>
		<xsl:attribute name="font-family"><xsl:value-of select="$fontFamily" /></xsl:attribute>
		<xsl:attribute name="font-size"><xsl:value-of select="number($baseFontSizePt) * 0.9" />pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="td-block.item-label">
		<xsl:attribute name="space-before">1pt</xsl:attribute>
		<xsl:attribute name="margin-top">3pt</xsl:attribute>
		<xsl:attribute name="margin-bottom">0.5pt</xsl:attribute>
		<xsl:attribute name="margin-right">6pt</xsl:attribute>
		<xsl:attribute name="font-family"><xsl:value-of select="$fontFamily" /></xsl:attribute>
		<xsl:attribute name="font-size"><xsl:value-of select="number($baseFontSizePt) * 0.9" />pt</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="td.matrix-forms-label">
		<xsl:attribute name="border-width">0.5pt</xsl:attribute>
		<xsl:attribute name="border-color">gray</xsl:attribute>
		<xsl:attribute name="border-style">solid</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="td-block.matrix-forms-label">
		<xsl:attribute name="space-before">0pt</xsl:attribute>
		<xsl:attribute name="margin-top">3pt</xsl:attribute>
		<xsl:attribute name="padding-top">3pt</xsl:attribute>
		<xsl:attribute name="margin-left">3pt</xsl:attribute>
		<xsl:attribute name="font-family"><xsl:value-of select="$fontFamily" /></xsl:attribute>
		<xsl:attribute name="font-size"><xsl:value-of select="number($baseFontSizePt) * 0.9" />pt</xsl:attribute>
		<xsl:attribute name="border-width">0.5pt</xsl:attribute>
		<xsl:attribute name="border-color">gray</xsl:attribute>
		<xsl:attribute name="border-style">solid</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="td-block.matrix-forms-hint">
		<xsl:attribute name="space-before">0pt</xsl:attribute>
		<xsl:attribute name="padding-top">2.5pt</xsl:attribute>
		<xsl:attribute name="padding-left">2.5pt</xsl:attribute>
		<xsl:attribute name="font-family"><xsl:value-of select="$fontFamily" /></xsl:attribute>
		<xsl:attribute name="font-size"><xsl:value-of select="number($baseFontSizePt) * 0.9" />pt</xsl:attribute>
		<xsl:attribute name="border-width">0.5pt</xsl:attribute>
		<xsl:attribute name="border-color">gray</xsl:attribute>
		<xsl:attribute name="border-style">solid</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="td-block.matrix-forms-column-set">
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">0.5pt</xsl:attribute>
		<xsl:attribute name="border-bottom-color">gray</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="td-block.matrix-forms-column-set-start">
		<xsl:attribute name="text-align">center</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.5pt</xsl:attribute>
		<xsl:attribute name="border-start-color">gray</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">0.5pt</xsl:attribute>
		<xsl:attribute name="border-bottom-color">gray</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="matrix-forms">
		<xsl:attribute name="border-width">0.5pt</xsl:attribute>
		<xsl:attribute name="border-color">gray</xsl:attribute>
		<xsl:attribute name="border-style">solid</xsl:attribute>
		<!--<xsl:attribute name="margin-right">10px</xsl:attribute>-->
	</xsl:attribute-set>
	<xsl:attribute-set name="matrix-forms-th">
		<xsl:attribute name="border-width">0.5pt</xsl:attribute>
		<xsl:attribute name="border-color">gray</xsl:attribute>
		<xsl:attribute name="border-style">solid</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="matrix-forms-td">
		<xsl:attribute name="margin-top">0pt</xsl:attribute>
		<xsl:attribute name="margin-bottom">0pt</xsl:attribute>
		<xsl:attribute name="border-start-style">solid</xsl:attribute>
		<xsl:attribute name="border-start-width">0.5pt</xsl:attribute>
		<xsl:attribute name="border-start-color">gray</xsl:attribute>
		<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
		<xsl:attribute name="border-bottom-width">0.5pt</xsl:attribute>
		<xsl:attribute name="border-start-color">gray</xsl:attribute>
	</xsl:attribute-set>
	<xsl:attribute-set name="matrix-forms-th-block">
		<xsl:attribute name="font-family"><xsl:value-of select="$fontFamily" /></xsl:attribute>
		<xsl:attribute name="font-size"><xsl:value-of select="number($baseFontSizePt) * 0.66" />pt</xsl:attribute>
		<xsl:attribute name="text-align">center</xsl:attribute>
	</xsl:attribute-set>
</xsl:stylesheet>
