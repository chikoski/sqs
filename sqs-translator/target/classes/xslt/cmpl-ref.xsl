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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:sqs="http://sqs.cmr.sfc.keio.ac.jp/2004/sqs" xmlns:xforms="http://www.w3.org/2002/xforms" version="1.0">

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xforms:select | xforms:select1 | xforms:input | xforms:textarea | xforms:range | sqs:matrix-forms | sqs:qrcode">
		<xsl:element name="{name()}">
			<xsl:apply-templates select="@*"/>
			<xsl:if test="0 = string-length(@sqs:qid)">
	</xsl:if>
			<xsl:attribute name="sqs:qid"><xsl:number level="any" from="/" count="xforms:select | xforms:select1 | xforms:input | xforms:textarea | xforms:range | sqs:matrix-forms | sqs:qrcode" format="1"/></xsl:attribute>
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
