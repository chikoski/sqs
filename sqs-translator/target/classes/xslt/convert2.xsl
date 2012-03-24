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
	xmlns:xhtml2="http://www.w3.org/2002/06/xhtml2" xmlns:xforms="http://www.w3.org/2002/xforms"
	xmlns:sqs="http://sqs.cmr.sfc.keio.ac.jp/2004/sqs" xmlns:xalan="http://xml.apache.org/xalan"
	version="1.0">

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>


	<xsl:template name="xsl-function-loop">
		<xsl:param name="value"/>
		<xsl:param name="i">0</xsl:param>
		<xsl:param name="count"/>

		<xsl:if test="$i &lt; $count">
			<xsl:copy-of select="$value"/>
		</xsl:if>

		<xsl:if test="$i &lt; $count">
			<xsl:call-template name="xsl-function-loop">
				<xsl:with-param name="i">
					<xsl:value-of select="$i + 1"/>
				</xsl:with-param>
				<xsl:with-param name="value">
					<xsl:copy-of select="$value"/>
				</xsl:with-param>
				<xsl:with-param name="count">
					<xsl:value-of select="$count"/>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>


	<xsl:template match="xhtml2:td[@xhtml2:class='matrix-forms-column-set']">
		<xsl:for-each select="xforms:select|xforms:select1">
			<xsl:call-template name="matrix-forms-column-set-select">
				<xsl:with-param name="local-name">
					<xsl:value-of select="local-name()"/>
				</xsl:with-param>
				<xsl:with-param name="qid">
					<xsl:value-of select="@sqs:qid"/>
				</xsl:with-param>
				<xsl:with-param name="label">
					<xsl:value-of select="xforms:label"/>
				</xsl:with-param>
				<xsl:with-param name="hint">
					<xsl:copy-of select="xforms:hint"/>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="xforms:input|xforms:textarea">
		<xsl:call-template name="copy-textarea"/>
	</xsl:template>

	<xsl:template match="xforms:select|xforms:select1">
		<xsl:choose>
			<xsl:when test="@sqs:pxform-type = 'range'">
				<xsl:call-template name="layout-range"/>
			</xsl:when>
			<xsl:when test="@sqs:pxform-type = 'matrix-forms'">
				<xsl:call-template name="matrix-forms-column-set-select">
					<xsl:with-param name="local-name">
						<xsl:value-of select="local-name()"/>
					</xsl:with-param>
					<xsl:with-param name="label">
						<xsl:value-of select="xforms:label"/>
					</xsl:with-param>
					<xsl:with-param name="hint">
						<xsl:copy-of select="xforms:hint"/>
					</xsl:with-param>
					<xsl:with-param name="qid">
						<xsl:value-of select="@sqs:qid"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="convert-select-semantics">
					<xsl:with-param name="local-name">
						<xsl:value-of select="local-name()"/>
					</xsl:with-param>
					<xsl:with-param name="qid">
						<xsl:value-of select="@sqs:qid"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>


	<!-- ###################################### -->

	<xsl:template name="xforms-add-colspan-itemIndex">
		<xsl:element name="xforms:select">
			<xsl:attribute name="sqs:cols">
				<xsl:choose>
					<xsl:when test="0 &lt; string-length(@sqs:cols)">
						<xsl:value-of select="@sqs:cols"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="count(xforms:item)"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>

			<xforms:label>
				<xsl:apply-templates select="xforms:label"/>
			</xforms:label>
			 
			<xsl:copy-of select="xforms:hint"/>

			<xsl:for-each select="xforms:item">

				<xsl:variable name="colspan"><xsl:choose>
						<xsl:when test="0 &lt; string-length(@sqs:colspan)"><xsl:value-of select="@sqs:colspan"/></xsl:when>
						<xsl:otherwise>1</xsl:otherwise>
				</xsl:choose></xsl:variable>
				
				<xsl:element name="xforms:item">
					<xsl:attribute name="sqs:colspan"><xsl:value-of select="$colspan"/></xsl:attribute>
					<xsl:attribute name="sqs:itemIndex">
						<xsl:value-of select="position()-1"/>
					</xsl:attribute>
					<xsl:for-each select="xforms:label|xforms:value">
						<xsl:copy-of select="."/>
					</xsl:for-each>
				</xsl:element>
				<xsl:if test="1 &lt; $colspan">
					<xsl:call-template name="xsl-function-loop">
						<xsl:with-param name="count"><xsl:value-of select="$colspan - 1"/></xsl:with-param>
						<xsl:with-param name="value">
							<xsl:element name="xforms:item">
								<xsl:attribute name="sqs:colspan"><xsl:value-of select="number(0)"/></xsl:attribute>
							</xsl:element>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:if>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>


	<!-- ###################################################################### -->
	<xsl:template name="convert-select-semantics">
		<xsl:param name="local-name"/>
		<xsl:param name="qid"/>

		<xsl:variable name="cols">
			<xsl:choose>
				<xsl:when test="0 &lt; number(@sqs:cols)">
					<xsl:value-of select="@sqs:cols"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="count(xforms:item)"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="contents">
			<xsl:call-template name="xforms-add-colspan-itemIndex"/>
		</xsl:variable>

		<xsl:for-each select="xalan:nodeset($contents)//xforms:select">

			<xsl:variable name="label">
				<xsl:value-of select="xforms:label"/>
			</xsl:variable>
			<xsl:variable name="hint">
				<xsl:copy-of select="xforms:hint"/>
			</xsl:variable>

			<xhtml2:table xhtml2:class="itemset" sqs:cols="{$cols}">
				<xhtml2:tbody>
					<xsl:for-each select="xforms:item">
					
						<xsl:if test="(position()-1) mod ($cols) = 0">
						
							<xhtml2:tr>
								<xsl:for-each
									select=".|following-sibling::xforms:item[position() &lt; $cols]">
									<xsl:if test="0 &lt; number(@sqs:colspan)">

										<xhtml2:td
											xhtml2:style="width:{100 * number(@sqs:colspan) div $cols}%"
											xhtml2:colspan="{@sqs:colspan}">
											<xsl:call-template name="convert-select-semantics-item">
												<xsl:with-param name="local-name" select="$local-name"/>
												<xsl:with-param name="label" select="$label"/>
												<xsl:with-param name="hint" select="$hint"/>
												<xsl:with-param name="qid" select="$qid"/>
												<xsl:with-param name="itemIndex"
												select="@sqs:itemIndex"/>
											</xsl:call-template>
										</xhtml2:td>

									</xsl:if>
								</xsl:for-each>
								
								<xsl:call-template name="xsl-function-loop">
									<xsl:with-param name="count">
										<xsl:value-of select="$cols - sum(./@sqs:colspan|following-sibling::xforms:item[position() &lt; $cols]/@sqs:colspan)"/>
									</xsl:with-param>
									<xsl:with-param name="value">
										<xhtml2:td
										 xhtml2:style="width:{100 div $cols}%">
										 <!-- 
										 <xsl:value-of select="$cols"/> - 
										 <xsl:value-of select="sum(./@sqs:colspan|following-sibling::xforms:item[position() &lt; $cols]/@sqs:colspan)"/>
										  -->
										</xhtml2:td>
									</xsl:with-param>
								</xsl:call-template>
								
								 <!--  <xhtml2:td>
										 <xsl:for-each select=".|following-sibling::xforms:item[position() &lt; $cols]">
										 	(<xsl:value-of select="@sqs:colspan" />)
										 </xsl:for-each>
										 <xsl:value-of select="$cols - sum(./@sqs:colspan|following-sibling::xforms:item[position() &lt; $cols]/@sqs:colspan)"/>
								 </xhtml2:td>-->
							</xhtml2:tr>
						</xsl:if>

					</xsl:for-each>
				</xhtml2:tbody>
			</xhtml2:table>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="convert-select-semantics-item">

		<xsl:param name="local-name"/>
		<xsl:param name="label"/>
		<xsl:param name="hint"/>
		<xsl:param name="qid"/>
		<xsl:param name="itemIndex"/>

		<xhtml2:table xhtml2:class="item">
			<xhtml2:tbody>
				<xhtml2:tr>
					<xhtml2:td xhtml2:class="item-form">
						<xsl:call-template name="select-item-to-single-select">
							<xsl:with-param name="local-name">
								<xsl:value-of select="$local-name"/>
							</xsl:with-param>
							<xsl:with-param name="label">
								<xsl:value-of select="$label"/>
							</xsl:with-param>
							<xsl:with-param name="hint">
								<xsl:copy-of select="$hint"/>
							</xsl:with-param>
							<xsl:with-param name="qid">
								<xsl:value-of select="$qid"/>
							</xsl:with-param>
							<xsl:with-param name="itemIndex">
								<xsl:value-of select="$itemIndex"/>
							</xsl:with-param>
						</xsl:call-template>
					</xhtml2:td>
					<!--
					<xhtml2:td xhtml2:class="item-value">
						<xsl:value-of select="xforms:value"/>: </xhtml2:td>
					-->
					<xhtml2:td xhtml2:class="item-label">
						<xsl:copy-of select="xforms:label/* | xforms:label/text()"/>
					</xhtml2:td>
				</xhtml2:tr>
			</xhtml2:tbody>
		</xhtml2:table>
	</xsl:template>

	<xsl:template name="select-item-to-single-select">
		<xsl:param name="local-name"/>
		<xsl:param name="label"/>
		<xsl:param name="hint"/>
		<xsl:param name="qid"/>
		<xsl:param name="itemIndex"/>

		<xforms:select sqs:pxform-type="{$local-name}" sqs:qid="{$qid}" sqs:itemIndex="{$itemIndex}">
			<!-- TODO: recursive retrieve @class from ancestor -->
			<xsl:if test="@class">
				<xsl:attribute name="class">
					<xsl:value-of select="@class"/>
				</xsl:attribute>
			</xsl:if>
			
			<xforms:label>
				<xsl:copy-of select="$label"/>
			</xforms:label>
			
			<xsl:copy-of select="$hint"/>

			<xforms:item>
				<xforms:label>
					<xsl:value-of select="xforms:label"/>
				</xforms:label>
				<xforms:value>
					<xsl:value-of select="xforms:value"/>
				</xforms:value>
			</xforms:item>
		</xforms:select>
	</xsl:template>


	<xsl:template name="copy-textarea">
		<xsl:element name="xforms:textarea">
			<xsl:apply-templates select="@xforms:*|@sqs:*"/>
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>

	<xsl:template name="matrix-forms-column-set-select">
		<xsl:param name="local-name"/>
		<xsl:param name="label"/>
		<xsl:param name="hint"/>
		<xsl:param name="qid"/>

		<xsl:for-each select="xforms:item">
			<xsl:variable name="column-start">
				<xsl:choose>
					<xsl:when test="position()=1">true</xsl:when>
					<xsl:otherwise>false</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>

			<xhtml2:td xhtml2:class="matrix-forms-column-set" sqs:column-start="{$column-start}">
				<xsl:call-template name="select-item-to-single-select">
					<xsl:with-param name="local-name">
						<xsl:value-of select="$local-name"/>
					</xsl:with-param>
					<xsl:with-param name="label">
						<xsl:copy-of select="$label"/>
					</xsl:with-param>
					<xsl:with-param name="hint">
						<xsl:copy-of select="$hint"/>
					</xsl:with-param>
					<xsl:with-param name="qid">
						<xsl:value-of select="$qid"/>
					</xsl:with-param>
					<xsl:with-param name="itemIndex">
						<xsl:value-of select="position()-1"/>
					</xsl:with-param>
				</xsl:call-template>
			</xhtml2:td>
		</xsl:for-each>

	</xsl:template>


	<xsl:template name="layout-range">
		<xsl:variable name="label">
			<xsl:value-of select="@xforms:label"/>
		</xsl:variable>
		<xhtml2:table border="1">
			<xhtml2:tbody>
				<xhtml2:tr>
					<xsl:for-each select="xforms:item">
						<xhtml2:td>
							<xforms:select sqs:qid="{@sqs:qid}"
								sqs:itemIndex="count(preceding-sibling::@xforms:item)"
								sqs:pxform-type="range">
								<xforms:label>
									<xsl:value-of select="$label"/>
								</xforms:label>
								<xforms:item>
									<xforms:label>
										<xsl:value-of select="@xforms:label"/>
									</xforms:label>
									<xforms:value>
										<xsl:value-of select="@xforms:value"/>
									</xforms:value>
								</xforms:item>
							</xforms:select>
						</xhtml2:td>
					</xsl:for-each>
				</xhtml2:tr>
			</xhtml2:tbody>
		</xhtml2:table>
	</xsl:template>
</xsl:stylesheet>
