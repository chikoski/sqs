<?xml version="1.0" encoding="UTF-8"?>
<!--
   Copyright 2004 SQS Development Team / Community Management Research Project.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

@page{
 size:595p 842p;
}
svg:svg{
  page-preak-after: always;
}
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:svg="http://www.w3.org/2000/svg"
 xmlns:xforms="http://www.w3.org/2002/xforms"
 xmlns:reader="http://sqs.cmr.sfc.keio.ac.jp/2004/reader"
 version="1.0">
	<xsl:param name="pages">0</xsl:param>

	<xsl:template match="/">
		<svg:svg width="595" height="842">
		    <svg:metadata>
		    	<reader:page total="{$pages}" x1="99" y1="29" x2="497" y2="29" x3="94" y3="810" x4="492" y4="810"/>
		    </svg:metadata>
			<xsl:apply-templates/>
		</svg:svg>
	</xsl:template>

	<xsl:template match="text()">
	</xsl:template>
	
	<xsl:template match="Title/Title[contains(text(), 'type=')]">
		<xsl:variable name="type"><xsl:value-of select="substring-after(../Title[1]/text(), 'type=')"/></xsl:variable>
		<xsl:text>
</xsl:text>
		<xsl:choose>
		<xsl:when test="$type = 'input' or $type = 'textarea'">
			<xsl:variable name="dimension"><xsl:value-of select="substring-after(../Title[7],'=')"/></xsl:variable>

			<svg:g id="{substring-after(../Title[8],'=')}">
				<svg:metadata><reader:page number="{substring-after(../Title[6],'=')}"/></svg:metadata>
			<svg:rect
				x="{substring-before($dimension,',')}"
				y="{substring-before(substring-after($dimension,','), ',')}"
				width="{substring-before(substring-after(substring-after($dimension,','), ','), ',')}"
				height="{substring-after(substring-after(substring-after($dimension,','), ','), ',')}"			
			>
			<svg:metadata>
			<xsl:element name="xforms:{substring-after(text(),'=')}">
			 	<xsl:attribute name="ref"><xsl:value-of select="substring-after(../Title[8],'=')"/></xsl:attribute>
				<xforms:label><xsl:value-of select="substring-after(../Title[2],'=')"/></xforms:label>
				<xsl:if test="0 &lt; string-length(substring-after(../../../../Title[1]/Title[3]/text(),'='))">
					<xforms:hint><xsl:value-of select="substring-after(../../../../Title[1]/Title[3]/text(),'=')"/></xforms:hint>
				</xsl:if>
				<xsl:if test="0 &lt; string-length(substring-after(../../../Title[1]/Title[3]/text(),'='))">
					<xforms:hint><xsl:value-of select="substring-after(../../../Title[1]/Title[3]/text(),'=')"/></xforms:hint>
				</xsl:if>
				<xforms:hint><xsl:value-of select="substring-after(../Title[3],'=')"/></xforms:hint>
				<xforms:help><xsl:value-of select="substring-after(../Title[4],'=')"/></xforms:help>
				<xforms:alart><xsl:value-of select="substring-after(../Title[5],'=')"/></xforms:alart>
			</xsl:element>
			</svg:metadata>
			</svg:rect>
			</svg:g>
		</xsl:when>
		<xsl:when test="$type = 'select1' or $type = 'select'">
		<xsl:variable name="type"><xsl:value-of select="substring-after(../Title[1],'=')"/></xsl:variable>
		<xsl:variable name="label"><xsl:value-of select="substring-after(../Title[2],'=')"/></xsl:variable>
		<xsl:variable name="select1-elements">
			<xsl:if test="0 &lt; string-length(substring-after(../../../../Title[1]/Title[3]/text(),'='))">
				<xforms:hint><xsl:value-of select="substring-after(../../../../Title[1]/Title[3]/text(),'=')"/></xforms:hint>
			</xsl:if>
			<xsl:if test="0 &lt; string-length(substring-after(../../../Title[1]/Title[3]/text(),'='))">
				<xforms:hint><xsl:value-of select="substring-after(../../../Title[1]/Title[3]/text(),'=')"/></xforms:hint>
			</xsl:if>
			<xforms:hint><xsl:value-of select="substring-after(../Title[3],'=')"/></xforms:hint>
			<xforms:help><xsl:value-of select="substring-after(../Title[4],'=')"/></xforms:help>
			<xforms:alart><xsl:value-of select="substring-after(../Title[5],'=')"/></xforms:alart>
		</xsl:variable>
		<xsl:variable name="ref"><xsl:value-of select="substring-after(../Title[6],'=')"/></xsl:variable>
		<xsl:for-each select="../../Title[2]/Title">
			<xsl:variable name="dimension"><xsl:value-of select="substring-after(Title[3],'=')"/></xsl:variable>
			<svg:g id="{$ref}/v{substring-after(Title[1],'=')}">
				<svg:metadata><reader:page number="{substring-after(Title[2],'=')}"/></svg:metadata>
			<svg:rect
				x="{substring-before($dimension,',')}"
				y="{substring-before(substring-after($dimension,','), ',')}"
				width="{substring-before(substring-after(substring-after($dimension,','), ','), ',')}"
				height="{substring-after(substring-after(substring-after($dimension,','), ','), ',')}"			
			>
			<svg:metadata>
			<xsl:element name="xforms:{$type}">
		 	<xsl:attribute name="ref"><xsl:value-of select="$ref"/></xsl:attribute>			
			<xforms:label><xsl:value-of select="$label"/></xforms:label>
			<xsl:copy-of select="$select1-elements"/>
			<xforms:item>
				<xforms:label><xsl:value-of select="substring-after(text(),'=')"/></xforms:label>
				<xforms:value><xsl:value-of select="substring-after(Title[1],'=')"/></xforms:value>
			</xforms:item>
			</xsl:element>
			</svg:metadata>
			</svg:rect>
			</svg:g>
		</xsl:for-each>
	</xsl:when>	
		</xsl:choose>
		<xsl:text>
</xsl:text>
	</xsl:template>
</xsl:stylesheet>
