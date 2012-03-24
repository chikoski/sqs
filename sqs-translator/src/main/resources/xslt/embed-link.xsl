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
<xsl:stylesheet
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:fo="http://www.w3.org/1999/XSL/Format"
 xmlns:sqs="http://sqs.cmr.sfc.keio.ac.jp/2004/sqs"
 xmlns:xalan="http://xml.apache.org/xalan"
 exclude-result-prefixes="xalan"
 version="1.0">
	<!--
	<xsl:template match="@sqs:id">
		<fo:marker marker-class-name="{parent::node()/attribute::sqs:id}">
			<xsl:apply-templates/>
		</fo:marker>
	</xsl:template>
	<xsl:template match="sqs:ref">
		<fo:retrieve-marker fo:retrieve-class-name="{@sqs:idref}"/>
	</xsl:template>
-->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<xsl:variable name="idstore">
		<xsl:for-each select="//node()[0 &lt; string-length(@sqs:id)]">
			<sqs:marker sqs:name="{@sqs:id}">
				<xsl:apply-templates/>
			</sqs:marker>
		</xsl:for-each>
	</xsl:variable>
	<xsl:template match="sqs:ref">
		<xsl:variable name="idref">
			<xsl:value-of select="@sqs:idref"/>
		</xsl:variable>
		<xsl:for-each select="xalan:nodeset($idstore)/sqs:marker[@sqs:name = $idref][1]">
			<xsl:apply-templates/>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
