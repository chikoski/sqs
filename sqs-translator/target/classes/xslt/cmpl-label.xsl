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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xforms="http://www.w3.org/2002/xforms" xmlns:sqs="http://sqs.cmr.sfc.keio.ac.jp/2004/sqs">

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>


	<xsl:template match="xforms:select | xforms:select1 | xforms:input | xforms:textarea | xforms:range | sqs:matrix-forms">
		<xsl:element name="{name()}">
			<xsl:apply-templates select="@*"/>
			<xsl:if test="0 = count(xforms:label)">
				<xforms:label>
					<sqs:counter sqs:level="1" sqs:format="1"/>
				</xforms:label>
			</xsl:if>
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="xforms:group">
		<xsl:element name="{name()}">
			<xsl:apply-templates select="@*"/>
			<xsl:if test="0 = count(xforms:label)">
				<xforms:label>
					<sqs:counter sqs:level="2" sqs:format="1"/>
				</xforms:label>
			</xsl:if>
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
