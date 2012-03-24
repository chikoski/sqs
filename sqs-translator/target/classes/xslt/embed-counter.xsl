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
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xhtml2="http://www.w3.org/2002/06/xhtml2" xmlns:xforms="http://www.w3.org/2002/xforms" xmlns:sqs="http://sqs.cmr.sfc.keio.ac.jp/2004/sqs">

	<xsl:param name="internal-destination-prefix-section">Section-</xsl:param>
	<xsl:param name="internal-destination-prefix-question">Question-</xsl:param>
	<xsl:param name="internal-destination-prefix-matrixforms">QuestionMatrix-</xsl:param>
	<xsl:param name="internal-destination-prefix-xform">XForm-</xsl:param>
	<xsl:param name="internal-destination-prefix-svg">SVG-</xsl:param>

	<xsl:param name="xhtml.h-attribute..sqs.prefix">Q</xsl:param>
	<xsl:param name="xhtml.h-attribute..sqs.suffix">. </xsl:param>
	<xsl:param name="xhtml.h-attribute..sqs.format">1</xsl:param>
	<xsl:param name="sqs.counter-attribute..sqs.prefix">(</xsl:param>
	<xsl:param name="sqs.counter-attribute..sqs.suffix">)</xsl:param>
	<xsl:param name="sqs.counter-attribute..sqs.format">1</xsl:param>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xhtml2:section">
	<xsl:element name="xhtml2:section">
			<xsl:attribute name="id"><xsl:value-of select="$internal-destination-prefix-section"/><xsl:number level="any" count="xhtml2:section" from="xhtml2:body" format="1"/></xsl:attribute>
			<xsl:apply-templates select="@*|*"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="xhtml2:section/xhtml2:h">
		<xsl:element name="xhtml2:h">
			<xsl:if test="0 &lt; string-length(@sqs:id)">
				<xsl:attribute name="sqs:id"><xsl:value-of select="@sqs:id"/></xsl:attribute>
			</xsl:if>
			<!--
ad-hoc comment out because this code is not work now.
			<xsl:copy>
				<xsl:apply-templates select="@*"/>
			</xsl:copy>
-->
			<xsl:choose>
				<xsl:when test="0 &lt; string-length(@sqs:prefix)">
					<xsl:value-of select="@sqs:prefix"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$xhtml.h-attribute..sqs.prefix"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="0 = string-length(@sqs:format)">
					<xsl:number level="any" count="xhtml2:section" from="xhtml2:body" format="{$xhtml.h-attribute..sqs.format}"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:number level="any" count="xhtml2:section" from="xhtml2:body" format="{@sqs:format}"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="0 &lt; string-length(@sqs:suffix)">
					<xsl:value-of select="@sqs:suffix"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$xhtml.h-attribute..sqs.suffix"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="sqs:counter[string-length(@sqs:level) = 0 or number(@sqs:level) &lt;= 1]">
		<xsl:choose>
			<xsl:when test="0 &lt; string-length(@sqs:prefix)">
				<xsl:value-of select="@sqs:prefix"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$sqs.counter-attribute..sqs.prefix"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="0 = string-length(@sqs:format)">
				<xsl:number level="any" count="sqs:counter[string-length(@sqs:level) = 0 or number(@sqs:level) &lt;= 1]" from="xhtml2:body" format="{@sqs:format}"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:number level="any" count="sqs:counter[string-length(@sqs:level) = 0 or number(@sqs:level) &lt;= 1]" from="xhtml2:body" format="{$sqs.counter-attribute..sqs.format}"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="0 &lt; string-length(@sqs:suffix)">
				<xsl:value-of select="@sqs:suffix"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$sqs.counter-attribute..sqs.suffix"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="sqs:counter[@sqs:level='2']">
<!--
		<xsl:choose>
			<xsl:when test="0 &lt; string-length(@sqs:prefix)">
				<xsl:value-of select="@sqs:prefix"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$sqs.counter-attribute..sqs.prefix"/>
			</xsl:otherwise>
		</xsl:choose>
-->
		<xsl:choose>
			<xsl:when test="0 = string-length(@sqs:format)">
				<xsl:number level="any" count="sqs:counter[@sqs:level='2']" from="sqs:row-array" format="{@sqs:format}"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:number level="any" count="sqs:counter[@sqs:level='2']" from="sqs:row-array" format="{$sqs.counter-attribute..sqs.format}"/>
			</xsl:otherwise>
		</xsl:choose>
<!--
		<xsl:choose>
			<xsl:when test="0 &lt; string-length(@sqs:suffix)">
				<xsl:value-of select="@sqs:suffix"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$sqs.counter-attribute..sqs.suffix"/>
			</xsl:otherwise>
		</xsl:choose>
-->
	</xsl:template>
</xsl:stylesheet>
