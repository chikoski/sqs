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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xforms="http://www.w3.org/2002/xforms"
	xmlns:xhtml2="http://www.w3.org/2002/06/xhtml2"
	xmlns:sqs="http://sqs.cmr.sfc.keio.ac.jp/2004/sqs" version="1.0">
	<xsl:param name="xforms.hint-attribute..sqs.prefix"/>
	<xsl:param name="xforms.hint-attribute..sqs.suffix"/>
	<xsl:param name="xforms.hint-attribute..sqs.display">inline</xsl:param>
	<xsl:param name="xforms.help-attribute..sqs.prefix">(</xsl:param>
	<xsl:param name="xforms.help-attribute..sqs.suffix">)</xsl:param>
	<xsl:param name="xforms.help-attribute..sqs.display">inline</xsl:param>
	<xsl:param name="xforms.alart-attribute..sqs.prefix">*</xsl:param>
	<xsl:param name="xforms.alart-attribute..sqs.suffix"/>
	<xsl:param name="xforms.alart-attribute..sqs.display">inline</xsl:param>
	<xsl:param name="internal-destination-prefix-question">Question-</xsl:param>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template
		match="xforms:textarea|xforms:input|xforms:select1|xforms:select|xforms:range|sqs:matrix-forms">
	  <xsl:choose>
<xsl:when test="@sqs:style='form-compact'">

<xhtml2:table xhtml2:class="form-compact" sqs:form-width-ratio="{@sqs:form-width-ratio}">
			<xhtml2:tbody>
				<xhtml2:tr>
				<xhtml2:td>
					<xsl:call-template name="xforms:label"/>
				</xhtml2:td>
				<xhtml2:td>
					<xsl:call-template name="xforms:hint"/>
					<xsl:call-template name="xforms:help"/>
					<xsl:call-template name="xforms:alart"/> : 
				</xhtml2:td>
				<xhtml2:td>
		<xsl:copy-of select="."/>
				</xhtml2:td>
				</xhtml2:tr>
			</xhtml2:tbody>
		</xhtml2:table>
</xsl:when>
<xsl:otherwise>
		<xhtml2:table xhtml2:class="form">
			<xhtml2:tbody>
				<xhtml2:tr>
				<xhtml2:td>
					<xsl:call-template name="xforms:label"/>
				</xhtml2:td>
				<xhtml2:td>
					<xsl:call-template name="xforms:hint"/>
					<xsl:call-template name="xforms:help"/>
					<xsl:call-template name="xforms:alart"/>
				</xhtml2:td>
				</xhtml2:tr>
			</xhtml2:tbody>
		</xhtml2:table>
<!--
		<xhtml2:p fo:keep-with-next.within-page="always">
			<xsl:call-template name="xforms:label"/>
			<xsl:call-template name="xforms:hint"/>
		</xhtml2:p>
		<xhtml2:p fo:keep-with-next.within-page="always">
			<xsl:call-template name="xforms:help"/>
		</xhtml2:p>
		<xhtml2:p fo:keep-with-next.within-page="always">
			<xsl:call-template name="xforms:alart"/>
		</xhtml2:p>
-->
		<xsl:choose>
			<xsl:when test="local-name()='matrix-forms'">
				<xsl:call-template name="sqs:matrix-forms"/>
			</xsl:when>
			<xsl:when test="local-name()='range'"> </xsl:when>
			<xsl:otherwise>
				<xsl:copy-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
</xsl:otherwise>
</xsl:choose>
	</xsl:template>

	<xsl:template name="xforms:label">
		<xsl:for-each select="xforms:label">
			<xsl:apply-templates/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="xforms:hint">
		<xsl:variable name="diplay">
			<xsl:choose>
				<xsl:when test="0 &lt; string-length(@sqs:display)">
					<xsl:value-of select="@sqs:display"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$xforms.hint-attribute..sqs.display"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="$diplay = 'inline' and 0 &lt; string-length(xforms:hint)">
			<xsl:choose>
				<xsl:when test="0 &lt; string-length(@sqs:prefix)">
					<xsl:value-of select="@sqs:prefix"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$xforms.hint-attribute..sqs.prefix"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:for-each select="xforms:hint">
				<xsl:apply-templates/>
			</xsl:for-each>
			<xsl:choose>
				<xsl:when test="0 &lt; string-length(@sqs:suffix)">
					<xsl:value-of select="@sqs:suffix"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$xforms.hint-attribute..sqs.suffix"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="xforms:help">
		<xsl:variable name="diplay">
			<xsl:choose>
				<xsl:when test="0 &lt; string-length(@sqs:display)">
					<xsl:value-of select="@sqs:display"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$xforms.help-attribute..sqs.display"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="$diplay = 'inline' and 0 &lt; string-length(xforms:help)">
			<xsl:choose>
				<xsl:when test="0 &lt; string-length(@sqs:prefix)">
					<xsl:value-of select="@sqs:prefix"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$xforms.help-attribute..sqs.prefix"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:for-each select="xforms:help">
				<xsl:apply-templates/>
			</xsl:for-each>
			<xsl:choose>
				<xsl:when test="0 &lt; string-length(@sqs:suffix)">
					<xsl:value-of select="@sqs:suffix"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$xforms.help-attribute..sqs.suffix"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="xforms:alart">
		<xsl:variable name="diplay">
			<xsl:choose>
				<xsl:when test="0 &lt; string-length(@sqs:display)">
					<xsl:value-of select="@sqs:display"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$xforms.alart-attribute..sqs.display"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="$diplay = 'inline' and 0 &lt; string-length(xforms:alart)">
			<xsl:choose>
				<xsl:when test="0 &lt; string-length(@sqs:prefix)">
					<xsl:value-of select="@sqs:prefix"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$xforms.alart-attribute..sqs.prefix"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:for-each select="xforms:alart">
				<xsl:apply-templates/>
			</xsl:for-each>
			<xsl:choose>
				<xsl:when test="0 &lt; string-length(@sqs:suffix)">
					<xsl:value-of select="@sqs:suffix"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$xforms.alart-attribute..sqs.suffix"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template name="sqs:matrix-forms">
		<xsl:variable name="qid" select="@sqs:qid"/>

		<xhtml2:table xhtml2:class="matrix-forms" sqs:form-width-ratio="{@sqs:form-width-ratio}">
			<!-- id="{@xforms:ref}" -->

			<xhtml2:thead>
				<xhtml2:tr>
					<xhtml2:th xhtml2:colspan="2" xhtml2:rowspan="2"/>
					<xsl:for-each select="sqs:column-array/sqs:*">
						<xsl:choose>
							<xsl:when test="local-name()='select1' or local-name()='select'">

								<xhtml2:th xhtml2:class="label"
									xhtml2:colspan="{count(xforms:item)}" xhtml2:rowspan="1"
									sqs:column-group-index="{position()}"
									sqs:xforms-type="{local-name()}">
									<xsl:value-of select="xforms:hint"/>
								</xhtml2:th>

							</xsl:when>
							<xsl:otherwise>
								<xhtml2:th xhtml2:class="label" xhtml2:colspan="1"
									xhtml2:rowspan="2" sqs:xforms-type="{local-name()}"
									sqs:width="{@sqs:width}">

									<xsl:call-template name="xforms:hint"/>

								</xhtml2:th>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</xhtml2:tr>
				<xhtml2:tr>

					<xsl:for-each select="sqs:column-array/sqs:*">
						<xsl:variable name="column-group-index" select="position()"/>

						<xsl:for-each select="xforms:item">
							<xhtml2:th sqs:column-group-index="{$column-group-index}"
								sqs:value="{xforms:value}">
								<xsl:call-template name="xforms:label"/>
							</xhtml2:th>
						</xsl:for-each>
					</xsl:for-each>

				</xhtml2:tr>
			</xhtml2:thead>

			<xhtml2:tbody>
				<xsl:for-each select="sqs:row-array/xforms:group">
					<xsl:variable name="row-id">
						<xsl:number level="any" from="sqs:row-array" count="xforms:group"/>
					</xsl:variable>
					<xsl:element name="xhtml2:tr">
						<xsl:attribute name="id"><xsl:value-of
								select="$internal-destination-prefix-question"/><xsl:value-of
								select="$qid"/>_<xsl:value-of select="$row-id"/></xsl:attribute>
						<xhtml2:th xhtml2:class="matrix-forms-label">
							<xsl:call-template name="xforms:label"/>
						</xhtml2:th>
						<xhtml2:td xhtml2:class="matrix-forms-hint">
							<xsl:call-template name="xforms:hint"/>
							<xsl:call-template name="xforms:help"/>
							<xsl:call-template name="xforms:alart"/>
						</xhtml2:td>

						<xsl:for-each select="ancestor::sqs:matrix-forms/sqs:column-array/sqs:*">
							<xsl:call-template name="matrix-forms-row-column">
								<xsl:with-param name="qid" select="$qid"/>
								<xsl:with-param name="row-id" select="$row-id"/>
								<xsl:with-param name="col-id">
									<xsl:number level="any"
										count="sqs:select|sqs:select1|sqs:input|sqs:textarea"
										from="sqs:column-array"/>
								</xsl:with-param>

							</xsl:call-template>
						</xsl:for-each>

					</xsl:element>
				</xsl:for-each>
			</xhtml2:tbody>
		</xhtml2:table>
	</xsl:template>

	<xsl:template name="matrix-forms-row-column">
		<xsl:param name="qid"/>
		<xsl:param name="row-id"/>
		<xsl:param name="col-id"/>
		<xsl:choose>

			<xsl:when test="local-name()='select' or local-name()='select1'">
				<xsl:call-template name="matrix-forms-row-column-set">
					<xsl:with-param name="qid" select="$qid"/>
					<xsl:with-param name="row-id" select="$row-id"/>
					<xsl:with-param name="col-id" select="$col-id"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="local-name()='input' or local-name()='textarea'">
				<xsl:call-template name="matrix-forms-row-textarea">
					<xsl:with-param name="qid" select="$qid"/>
					<xsl:with-param name="row-id" select="$row-id"/>
					<xsl:with-param name="col-id" select="$col-id"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xhtml2:td xhtml2:colspan="1">
					<xsl:copy-of select="."/>
				</xhtml2:td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="matrix-forms-row-column-set">
		<xsl:param name="qid"/>
		<xsl:param name="row-id"/>
		<xsl:param name="col-id"/>
		<xsl:variable name="colspan">
			<xsl:value-of select="count(xforms:item)"/>
		</xsl:variable>

		<xhtml2:td xhtml2:class="matrix-forms-column-set" xhtml2:colspan="{$colspan}">
			<xsl:element name="xforms:{local-name()}">
				<xsl:attribute name="sqs:qid"><xsl:value-of select="$qid"/>/<xsl:value-of
						select="$row-id"/>/<xsl:value-of select="$col-id"/></xsl:attribute>
				<xsl:attribute name="sqs:pxform-type">matrix-forms</xsl:attribute>

				<xsl:choose>
				<xsl:when test="0 &lt; string-length($col-id)">
								<xforms:label>(<xsl:value-of select="$qid"/>)/<xsl:value-of
						select="$row-id"/>/<xsl:value-of select="$col-id"/></xforms:label>			
				</xsl:when>
				<xsl:otherwise>
								<xforms:label>(<xsl:value-of select="$qid"/>)/<xsl:value-of
						select="$row-id"/></xforms:label>				
				</xsl:otherwise>
				</xsl:choose>
						
				<xforms:hint>
					<xsl:copy-of select="ancestor::sqs:matrix-forms/xforms:hint/text()"/>
				</xforms:hint>
				<xforms:hint>
					<xsl:copy-of
						select="ancestor::sqs:matrix-forms/sqs:row-array/xforms:group[position()=$row-id]/xforms:hint/text()"
					/>
				</xforms:hint>
				<xforms:hint>
					<xsl:copy-of select="xforms:hint/text()"/>
				</xforms:hint>

				<xsl:for-each select="*">
					<xsl:choose>
						<xsl:when test="local-name()='cols'">
							<xsl:attribute name="xhtml2:cols">
								<xsl:value-of select="$colspan"/>
							</xsl:attribute>
						</xsl:when>
						<xsl:when test="local-name()='hint'"> </xsl:when>
						<xsl:otherwise>
							<xsl:copy-of select="."/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</xsl:element>
		</xhtml2:td>
	</xsl:template>

	<xsl:template name="matrix-forms-row-textarea">
		<xsl:param name="qid"/>
		<xsl:param name="row-id"/>
		<xsl:param name="col-id"/>

		<xhtml2:td xhtml2:colspan="1" xhtml2:class="matrix-forms-td">
			<xsl:element name="xforms:{local-name()}">
				<xsl:apply-templates select="@sqs:*"/>

				<xsl:attribute name="sqs:qid"><xsl:value-of select="$qid"/>/<xsl:value-of
						select="$row-id"/>/<xsl:value-of select="$col-id"/></xsl:attribute>

				<xforms:hint>
					<xsl:copy-of select="ancestor::sqs:matrix-forms/xforms:hint/*"/>
					<xsl:copy-of
						select="ancestor::sqs:matrix-forms/sqs:column-array/*/xforms:hint/*"/>
					<xsl:copy-of
						select="ancestor::sqs:matrix-forms/sqs:row-array/xforms:group[position()=$row-id]/xforms:hint/*"/>
					<xsl:copy-of select="xforms:hint/*"/>
				</xforms:hint>

			</xsl:element>
		</xhtml2:td>
	</xsl:template>


	<xsl:template match="xforms:range">
		<xsl:variable name="times">
			<xsl:value-of
				select="1 + round((number(@xforms:end)-number(@xforms:start)) div number(@xforms:step))"
			/>
		</xsl:variable>
		<xsl:for-each select="xforms:indicator">
			<xhtml2:div>
				<xsl:apply-templates select="xforms:label"/>:<xsl:value-of select="@xforms:value"/>
			</xhtml2:div>
		</xsl:for-each>
		<xsl:call-template name="range-select">
			<xsl:with-param name="times">
				<xsl:value-of select="$times"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="range-item">
		<xsl:param name="total"/>
		<xsl:param name="times"/>
		<xsl:variable name="value">
			<xsl:value-of select="( number($total) -  number($times)) * number(@xforms:step)"/>
		</xsl:variable>
		<xforms:item>
			<xforms:label>
				<xsl:value-of select="$value"/>
			</xforms:label>
			<xforms:value>
				<xsl:value-of select="$value"/>
			</xforms:value>
		</xforms:item>
	</xsl:template>

	<xsl:template name="range-loop">
		<xsl:param name="label"/>
		<xsl:param name="total"/>
		<xsl:param name="times"/>
		<xsl:param name="value"/>
		<xsl:if test="$times &gt;= 0">
			<xsl:copy-of select="$value"/>
			<xsl:call-template name="range-loop">
				<xsl:with-param name="total">
					<xsl:value-of select="$total"/>
				</xsl:with-param>
				<xsl:with-param name="times">
					<xsl:value-of select="$times - 1"/>
				</xsl:with-param>
				<xsl:with-param name="value">
					<xsl:call-template name="range-item">
						<xsl:with-param name="total">
							<xsl:value-of select="$total"/>
						</xsl:with-param>
						<xsl:with-param name="times">
							<xsl:value-of select="$times"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<xsl:template name="range-select">
		<xsl:param name="times"/>
		<xforms:select1 sqs:pxform-type="range" sqs:qid="@sqs:qid">
			<xsl:call-template name="xforms:label"/>
			<xsl:call-template name="xforms:hint"/>
			<xsl:call-template name="xforms:help"/>
			<xsl:call-template name="xforms:alart"/>
			<xsl:call-template name="range-loop">
				<xsl:with-param name="total">
					<xsl:value-of select="$times"/>
				</xsl:with-param>
				<xsl:with-param name="times">
					<xsl:value-of select="$times"/>
				</xsl:with-param>
			</xsl:call-template>
		</xforms:select1>
	</xsl:template>
</xsl:stylesheet>
