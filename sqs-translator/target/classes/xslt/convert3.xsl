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
	xmlns:svg="http://www.w3.org/2000/svg" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:sqs="http://sqs.cmr.sfc.keio.ac.jp/2004/sqs"
	xmlns:xlink="http://www.w3.org/1999/xlink" version="1.0">

	<xsl:include href="param.xsl"/>
	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="xhtml2:body">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="/xhtml2:html">
		<fo:root language="{$language}" xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<!-- FOP default = 2400 dpi -->
			<!-- hagaki portrait width : 9456 px = 148mm = 3.94inch -->
			<!-- hagaki portrait height: 13992 px = 100mm = 5.83inch -->
			<xsl:call-template name="layout-master-set"/>
			<!--	<xsl:call-template name="bookmark-tree"/>-->
			<xsl:call-template name="page-sequence"/>
		</fo:root>
	</xsl:template>


	<!-- ########   1.  fo:layout-master-set 			 ######### -->
	<!-- ########   1.1 fo:layout-master-set/fo:simple-page-master   ######### -->
	<!-- ########   1.2 fo:layout-master-set/fo:region-body 	 ######### -->

	<xsl:template name="layout-master-set">
		<fo:layout-master-set>
			<xsl:choose>
				<xsl:when test="$sides='duplex'">

					<xsl:call-template name="simple-page-master">
						<xsl:with-param name="pageSide">left</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="simple-page-master">
						<xsl:with-param name="pageSide">right</xsl:with-param>
					</xsl:call-template>

					<fo:page-sequence-master master-name="page">
						<fo:repeatable-page-master-alternatives>
							<fo:conditional-page-master-reference odd-or-even="odd"
								master-reference="page-left"/>
							<fo:conditional-page-master-reference odd-or-even="even"
								master-reference="page-right"/>
						</fo:repeatable-page-master-alternatives>
					</fo:page-sequence-master>
				</xsl:when>
				<xsl:otherwise>

					<xsl:if test="$pageSideStartingFrom='left'">
						<xsl:call-template name="simple-page-master">
							<xsl:with-param name="pageSide">left</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="$pageSideStartingFrom='right'">
						<xsl:call-template name="simple-page-master">
							<xsl:with-param name="pageSide">right</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
		</fo:layout-master-set>
	</xsl:template>

	<xsl:template name="simple-page-master">
		<xsl:param name="pageSide"/>
		<fo:simple-page-master 
			page-width="{$pageMasterPageWidth}px" 
			page-height="{$pageMasterPageHeight}px" 
			master-name="page-{$pageSide}">

			<fo:region-body
				margin-top="{$regionBodyMarginTop}px"
				margin-bottom="{$regionBodyMarginTop}px"
				margin-left="{$regionBodyMarginLeft}px"
				margin-right="{$regionBodyMarginRight}px"
			/>

 			<xsl:choose>
				<xsl:when test="$pageSide='left' or $pageSide='right'">
					<fo:region-before extent="{$regionBeforeExtent}px" region-name="header-{$pageSide}"/>
					<fo:region-after extent="{$regionAfterExtent}px" region-name="footer-{$pageSide}"/>
				</xsl:when>
				<xsl:otherwise> </xsl:otherwise>
			</xsl:choose>

			<fo:region-start extent="{$regionStartExtent}px"/>
			<fo:region-end extent="{$regionEndExtent}px"/>

		</fo:simple-page-master>
	</xsl:template>

	<!-- ########  bookmark ######### -->


	<!-- ########   2.    fo:page-sequence                                			######### -->

	<!-- ########   2.1   fo:page-sequence/fo:static-cotntent             			######### -->
	<!-- ########   2.1.1 fo:page-sequence/fo:static-cotntent/fo:static-cotntent            ######### -->
	<!-- ########   2.2   fo:page-sequence/fo:flow                        			######### -->

	<xsl:template name="page-sequence">

		<xsl:variable name="master-reference">page<xsl:choose>
				<xsl:when test="$sides!='duplex'">-<xsl:value-of select="$pageSideStartingFrom"/>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
		</xsl:variable>

		<fo:page-sequence master-reference="{$master-reference}">

			<xsl:if test="$sides='duplex' or $pageSideStartingFrom='right'">
				<fo:static-content flow-name="header-right">
						<xsl:call-template name="xsl-region-before">
							<xsl:with-param name="pageSide">right</xsl:with-param>
						</xsl:call-template>
				</fo:static-content>
				<fo:static-content flow-name="footer-left">
						<xsl:call-template name="xsl-region-after">
							<xsl:with-param name="pageSide">left</xsl:with-param>
						</xsl:call-template>
				</fo:static-content>
			</xsl:if>
			<xsl:if test="$sides='duplex' or $pageSideStartingFrom='left'">
				<fo:static-content flow-name="header-left">
					<xsl:call-template name="xsl-region-before">
						<xsl:with-param name="pageSide">left</xsl:with-param>
					</xsl:call-template>
				</fo:static-content>
				<fo:static-content flow-name="footer-right">
					<xsl:call-template name="xsl-region-after">
						<xsl:with-param name="pageSide">right</xsl:with-param>
					</xsl:call-template>
				</fo:static-content>
			</xsl:if>

			<fo:static-content flow-name="xsl-region-end">
				<xsl:call-template name="xsl-region-end"/>
			</fo:static-content>

			<fo:flow flow-name="xsl-region-body">
				<fo:block id="originator"/>
				<xsl:apply-templates select="xhtml2:body"/>
				<fo:block id="terminator"/>
			</fo:flow>

		</fo:page-sequence>
	</xsl:template>

<!-- //////////////////////////////////////////////////////////////////// -->

	<xsl:template name="xsl-region-start"><fo:block> </fo:block></xsl:template>

	<xsl:template name="xsl-region-end"><fo:block> </fo:block></xsl:template>

	<xsl:template name="xsl-region-before">
		<xsl:param name="pageSide"/>

		<xsl:choose>
			<xsl:when test="$pageSide='left'">

				<fo:block-container
					height="{$stapleAreaHeight}px" width="{$stapleAreaWidth}px"
					top="{$stapleAreaTop}px"
					left="{$stapleAreaPadding}px - {$regionBeforeExtent}px"
					position="absolute">
					<fo:block>
					<xsl:if test="$enableMarkSheetMode='true' and $showStapleMark='true'">
						<fo:instream-foreign-object>
							<xsl:call-template name="staple">
								<xsl:with-param name="pageSide">
									<xsl:value-of select="$pageSide"/>
								</xsl:with-param>
							</xsl:call-template>
						</fo:instream-foreign-object>
					</xsl:if>
					</fo:block>
				</fo:block-container>

			</xsl:when>

			<xsl:when test="$pageSide='right'">
				<fo:block-container
					height="{$stapleAreaHeight}px" width="{$stapleAreaWidth}px" 
					top="{$stapleAreaTop}px"
					left="{$pageMasterPageWidth}px - {$stapleAreaPadding}px - {$regionBeforeExtent}px - {$regionAfterExtent}px"
					position="absolute">
					<fo:block>
						<xsl:if test="$enableMarkSheetMode='true' and $showStapleMark='true'">
						<fo:instream-foreign-object>
							<xsl:call-template name="staple">
								<xsl:with-param name="pageSide">
									<xsl:value-of select="$pageSide"/>
								</xsl:with-param>
							</xsl:call-template>
						</fo:instream-foreign-object>
						</xsl:if>
					</fo:block>
				</fo:block-container>
			</xsl:when>

		</xsl:choose>
		<xsl:if test="$enableMarkSheetMode = 'true'">
			<xsl:call-template name="deskewGuideTop"/>
		</xsl:if>
	</xsl:template>

	<xsl:template name="xsl-region-after">
		<xsl:param name="pageSide"/>

		<xsl:choose>
			<xsl:when test="$pageSide='left'">
				<fo:block-container border-color="white" border-style="solid" border-width="0mm"
					height="40px" width="47px" top="5px" left="{$pageMasterPageWidth - 80}px" padding="0px"
					position="absolute" display-align="center">

					<xsl:if test="$showPageNumber">
						<xsl:call-template name="pageNumber"/>
					</xsl:if>
					<xsl:if test="$showEnqtitleBelowPagenum">
						<xsl:call-template name="pageTitle"/>
					</xsl:if>

				</fo:block-container>
			</xsl:when>

			<xsl:when test="$pageSide='right'">
				<fo:block-container border-color="white" border-style="solid" border-width="0mm"
					height="40px" width="47px" top="5px" left="15px" padding="0px"
					position="absolute" display-align="center">

					<xsl:if test="$showPageNumber">
						<xsl:call-template name="pageNumber"/>
					</xsl:if>
					<xsl:if test="$showEnqtitleBelowPagenum">
						<xsl:call-template name="pageTitle"/>
					</xsl:if>

				</fo:block-container>
			</xsl:when>
		</xsl:choose>
		
		<xsl:if test="$enableMarkSheetMode = 'true'">
				<xsl:call-template name="deskewGuideBottom">
					<xsl:with-param name="pageSide">
						<xsl:value-of select="$pageSide"/>
					</xsl:with-param>
				</xsl:call-template>
		</xsl:if>
		
	</xsl:template>

	<xsl:template name="staple">
		<xsl:param name="pageSide"/>
		<xsl:choose>
			<xsl:when test="$pageSide='left'">
				<xsl:element name="svg:svg">
					<xsl:attribute name="xml:space">default</xsl:attribute>
					<xsl:attribute name="width"><xsl:value-of select="$stapleAreaWidth"/>px</xsl:attribute>
					<xsl:attribute name="height"><xsl:value-of select="$stapleAreaHeight"/>px</xsl:attribute>
					<svg:g style="stroke:black; fill:white; stroke-width: 0.1">
						<svg:path d="M 60 0 L 0 60" stroke-dasharray="3,3"/>
						<svg:path d="M 26 10 L 19 17 Z"/>
						<svg:path d="M 17 19 L 10 26 Z"/>
					</svg:g>
				</xsl:element>
			</xsl:when>
			<xsl:when test="$pageSide='right'">
				<xsl:element name="svg:svg">
					<xsl:attribute name="xml:space">default</xsl:attribute>
					<xsl:attribute name="width"><xsl:value-of select="$stapleAreaWidth"/>px</xsl:attribute>
					<xsl:attribute name="height"><xsl:value-of select="$stapleAreaHeight"/>px</xsl:attribute>

					<svg:g style="stroke:black; fill:white; stroke-width: 0.1">
						<svg:path d="M 0 0 L 60 60" stroke-dasharray="3,3"/>
						<svg:path d="M 34 10 L 41 17 Z"/>
						<svg:path d="M 43 19 L 50 26 Z"/>
					</svg:g>
				</xsl:element>
			</xsl:when>
			<xsl:otherwise>
				<!--  
				<xsl:element name="svg:svg">
					<xsl:attribute name="xml:space">default</xsl:attribute>
					<xsl:attribute name="width"><xsl:value-of select="$stapleAreaWidth"/>px</xsl:attribute>
					<xsl:attribute name="height"><xsl:value-of select="$stapleAreaHeight"/>px</xsl:attribute>					
				</xsl:element>
				-->
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="pageNumber">
		<fo:block display-align="center" xsl:use-attribute-sets="page-number">
			<fo:page-number/> / <fo:page-number-citation ref-id="terminator"/>
		</fo:block>
	</xsl:template>

	<xsl:template name="pageTitle">
		<fo:block display-align="center" xsl:use-attribute-sets="qtitle-below-page-number">
			<xsl:variable name="title">
				<xsl:value-of
					select="/xhtml2:html/xhtml2:head/xhtml2:meta[@name='DC.Title.Short']/@content"/>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="0 &lt; string-length($title)">
					<xsl:value-of select="$title"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="/xhtml2:html/xhtml2:head/xhtml2:title"/>
				</xsl:otherwise>
			</xsl:choose>
		</fo:block>
	</xsl:template>
	
<!-- //////////////////////////////////////////////////////////////////// -->

	<xsl:template name="deskewGuideTop">	

	<fo:block-container 
width="{$deskewGuideBlockWidth}px"
height="{$deskewGuideBlockHeight}px"
top="30px"
left="80px"
absolute-position="absolute"
xsl:use-attribute-sets="zero">	
		<fo:block xsl:use-attribute-sets="zero">
		<xsl:if test="$enableMarkSheetMode='true'">
		<fo:instream-foreign-object xsl:use-attribute-sets="zero">
			<xsl:element name="svg:svg">
			<xsl:attribute name="xml:space">default</xsl:attribute>
			<xsl:attribute name="width"><xsl:value-of select="$deskewGuideBlockWidth"/></xsl:attribute>
			<xsl:attribute name="height"><xsl:value-of select="$deskewGuideBlockHeight"/></xsl:attribute>
			<xsl:attribute name="id">SQSDeskewGuideNorthWest</xsl:attribute>
				<svg:g style="stroke:black; fill:black;">
					<xsl:element name="svg:rect">
						<xsl:attribute name="x"><xsl:value-of select="0"/></xsl:attribute>
						<xsl:attribute name="y"><xsl:value-of select="0"/></xsl:attribute>
						<xsl:attribute name="width"><xsl:value-of select="$deskewGuideBlockWidth"/></xsl:attribute>
						<xsl:attribute name="height"><xsl:value-of select="$deskewGuideBlockHeight"/></xsl:attribute>
					</xsl:element>
				</svg:g>
			</xsl:element>
		</fo:instream-foreign-object>
		</xsl:if>
	</fo:block>
	</fo:block-container>

	<fo:block-container 
width="{$deskewGuideBlockWidth}px"
height="{$deskewGuideBlockHeight}px"
top="18px"
right="{80 + $markingExampleWidth + $deskewGuideBlockWidth}px"
absolute-position="absolute"
xsl:use-attribute-sets="zero">
	<fo:block>
	<xsl:if test="$enableMarkSheetMode='true'">
		<fo:instream-foreign-object>
			<xsl:element name="svg:svg">
			<xsl:attribute name="xml:space">default</xsl:attribute>
			<xsl:attribute name="width"><xsl:value-of select="$markingExampleWidth"/></xsl:attribute>
			<xsl:attribute name="height"><xsl:value-of select="$markingExampleHeight"/></xsl:attribute>
				<svg:g style="stroke:black; fill:black;">
					<xsl:if test="$showMarkingExample = 'true'">
						<xsl:element name="svg:image">
							<xsl:attribute name="xlink:href">class://net.sqs2.impl.TranslatorJarURIContext/xslt/marking-examples_<xsl:value-of select="$language"/>.svg</xsl:attribute>
							<xsl:attribute name="x"><xsl:value-of select="0"/></xsl:attribute>
							<xsl:attribute name="y"><xsl:value-of select="0"/></xsl:attribute>
							<xsl:attribute name="width"><xsl:value-of select="$markingExampleWidth"/></xsl:attribute>
							<xsl:attribute name="height"><xsl:value-of select="$markingExampleHeight"/></xsl:attribute>
					</xsl:element>
					</xsl:if>
				</svg:g>
			</xsl:element>	
		</fo:instream-foreign-object>
		</xsl:if>
	</fo:block>
	</fo:block-container>
	
	<fo:block-container 
width="{$deskewGuideBlockWidth}px"
height="{$deskewGuideBlockHeight}px"
top="30px"
right="40px"
absolute-position="absolute"
xsl:use-attribute-sets="zero">
	<fo:block xsl:use-attribute-sets="zero">
		<xsl:if test="$enableMarkSheetMode='true'">
		<fo:instream-foreign-object xsl:use-attribute-sets="zero" background-color="green">
			<xsl:element name="svg:svg">
			<xsl:attribute name="xml:space">default</xsl:attribute>
			<xsl:attribute name="width"><xsl:value-of select="$deskewGuideBlockWidth"/></xsl:attribute>
			<xsl:attribute name="height"><xsl:value-of select="$deskewGuideBlockHeight"/></xsl:attribute>
			<xsl:attribute name="id">SQSDeskewGuideNorthEast</xsl:attribute>
				<svg:g style="stroke:black; fill:black;">
					<xsl:element name="svg:rect">
						<xsl:attribute name="x"><xsl:value-of select="0"/></xsl:attribute>
						<xsl:attribute name="y"><xsl:value-of select="0"/></xsl:attribute>
						<xsl:attribute name="width"><xsl:value-of select="$deskewGuideBlockWidth"/></xsl:attribute>
						<xsl:attribute name="height"><xsl:value-of select="$deskewGuideBlockHeight"/></xsl:attribute>
					</xsl:element>
				</svg:g>
			</xsl:element>
		</fo:instream-foreign-object>
		</xsl:if> 
	</fo:block>
	</fo:block-container>
	</xsl:template>
	

	<xsl:template name="deskewGuideBottom">
		<xsl:param name="pageSide"/>
	
		<xsl:variable name="leftDeskewGuideBlockWidth"><xsl:choose>
			<xsl:when test="$pageSide='left'"><xsl:value-of select="$deskewGuideBlockWidth div 2"/></xsl:when>
			<xsl:when test="$pageSide='right'"><xsl:value-of select="$deskewGuideBlockWidth"/></xsl:when>
			</xsl:choose></xsl:variable>
		<xsl:variable name="rightDeskewGuideBlockWidth"><xsl:choose>
			<xsl:when test="$pageSide='left'"><xsl:value-of select="$deskewGuideBlockWidth"/></xsl:when>
			<xsl:when test="$pageSide='right'"><xsl:value-of select="$deskewGuideBlockWidth div 2"/></xsl:when>
			</xsl:choose></xsl:variable>
		
		  <fo:block-container
width="{$deskewGuideBlockWidth}px"
height="{$deskewGuideBlockHeight}px"
bottom="30px"
left="80px"
absolute-position="absolute"
xsl:use-attribute-sets="zero">
			<fo:block xsl:use-attribute-sets="zero">
		<xsl:if test="$enableMarkSheetMode='true'">
		<fo:instream-foreign-object xsl:use-attribute-sets="zero">
			<xsl:element name="svg:svg">
			<xsl:attribute name="xml:space">default</xsl:attribute>
			<xsl:attribute name="width"><xsl:value-of select="$deskewGuideBlockWidth"/></xsl:attribute>
			<xsl:attribute name="height"><xsl:value-of select="$deskewGuideBlockHeight"/></xsl:attribute>
			<xsl:attribute name="id">SQSDeskewGuideSouthWest</xsl:attribute>
				<svg:g style="stroke:black; fill:black;">
					<xsl:element name="svg:rect"><!--  ($deskewGuideBlockWidth - $leftDeskewGuideBlockWidth) div 2" -->
						<xsl:attribute name="x"><xsl:value-of select="($deskewGuideBlockWidth - $leftDeskewGuideBlockWidth) div 2"/></xsl:attribute>
						<xsl:attribute name="y"><xsl:value-of select="0"/></xsl:attribute>
						<xsl:attribute name="width"><xsl:value-of select="$leftDeskewGuideBlockWidth"/></xsl:attribute>
						<xsl:attribute name="height"><xsl:value-of select="$deskewGuideBlockHeight"/></xsl:attribute>
					</xsl:element>
				</svg:g>
			</xsl:element>
		</fo:instream-foreign-object>
		</xsl:if>
			</fo:block>
		  </fo:block-container>

		  <fo:block-container
width="{$deskewGuideBlockWidth}px"
height="{$deskewGuideBlockHeight}px"
bottom="30px"
right="40px"
absolute-position="absolute"
xsl:use-attribute-sets="zero">
			<fo:block xsl:use-attribute-sets="zero">
			<xsl:if test="$enableMarkSheetMode='true'">
		<fo:instream-foreign-object xsl:use-attribute-sets="zero">
			<xsl:element name="svg:svg">
			<xsl:attribute name="xml:space">default</xsl:attribute>
			<xsl:attribute name="width"><xsl:value-of select="$deskewGuideBlockWidth"/></xsl:attribute>
			<xsl:attribute name="height"><xsl:value-of select="$deskewGuideBlockHeight"/></xsl:attribute>
			<xsl:attribute name="id">SQSDeskewGuideSouthEast</xsl:attribute>
				<svg:g style="stroke:black; fill:black;">
					<xsl:element name="svg:rect">
						<xsl:attribute name="x"><xsl:value-of select="($deskewGuideBlockWidth - $rightDeskewGuideBlockWidth) div 2"/></xsl:attribute>
						<xsl:attribute name="y"><xsl:value-of select="0"/></xsl:attribute>
						<xsl:attribute name="width"><xsl:value-of select="$rightDeskewGuideBlockWidth"/></xsl:attribute>
						<xsl:attribute name="height"><xsl:value-of select="$deskewGuideBlockHeight"/></xsl:attribute>
					</xsl:element>
				</svg:g>
			</xsl:element>
		</fo:instream-foreign-object>
		</xsl:if>
			</fo:block>
		  </fo:block-container>
		
	</xsl:template>

	
<!-- //////////////////////////////////////////////////////////////////// -->

	<xsl:template match="sqs:pageset">
		<fo:block page-break-after="always">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<xsl:template match="sqs:warning">
		<fo:block xsl:use-attribute-sets="textbox">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>
	<xsl:template match="xhtml2:p">
		<fo:block xsl:use-attribute-sets="p">
			<xsl:for-each select="@fo:*">
				<xsl:attribute name="{local-name()}">
					<xsl:value-of select="."/>
				</xsl:attribute>
			</xsl:for-each>

			<fo:inline color="white"> </fo:inline>
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>

	<xsl:template match="xhtml2:section">
		<fo:block id="{@id}">
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>

	<xsl:template match="xhtml2:h">
		<fo:block xsl:use-attribute-sets="h" keep-with-next="always">
			<!--     xsl:keep-with-next="always"-->
			<xsl:apply-templates/>
		</fo:block>
	</xsl:template>

	<xsl:template match="xhtml2:ul|xhtml2:ol">
		<fo:list-block>
			<xsl:apply-templates/>
		</fo:list-block>
	</xsl:template>
	<xsl:template match="xhtml2:li">
		<fo:list-item>
			<fo:list-item-label start-indent="inherit+5mm" end-indent="label-end()">
				<xsl:choose>
					<xsl:when test="local-name(parent::node()) = 'ul'">
						<fo:block>&#x02022;</fo:block>
					</xsl:when>
					<xsl:when test="local-name(parent::node()) = 'ol'">
						<fo:block>
							<xsl:number level="multiple" count="xhtml2:li" from="xhtml2:ul"
								format="1."/>
						</fo:block>
					</xsl:when>
				</xsl:choose>
			</fo:list-item-label>
			<fo:list-item-body start-indent="body-start()">
				<fo:block>
					<xsl:apply-templates/>
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>

	<xsl:template match="xhtml2:table">
		<fo:table table-layout="fixed" width="100%">
			<xsl:apply-templates/>
		</fo:table>
	</xsl:template>

	<xsl:template match="xhtml2:table[@xhtml2:class='form']">
		<fo:table table-layout="fixed" width="100%">
			<fo:table-column column-width="{$regionBodyWidth * 0.04}px"/>
			<fo:table-column column-width="{$regionBodyWidth * 0.96}px"/>
			<xsl:apply-templates/>
		</fo:table>
	</xsl:template>

	<xsl:template match="xhtml2:table[@xhtml2:class='form-compact']">
		<fo:table table-layout="fixed" width="100%">

			<fo:table-column column-width="{$qid-label-width}px"/>
			 
			<fo:table-column column-width="{($regionBodyWidth - $qid-label-width) * (1.0 - number(@sqs:form-width-ratio))}px"/>
			<fo:table-column column-width="{($regionBodyWidth - $qid-label-width) * (number(@sqs:form-width-ratio))}px"/>
			<xsl:apply-templates/>
		</fo:table>
	</xsl:template>

	<xsl:template match="xhtml2:table[@xhtml2:class='matrix-forms']">
		<fo:table table-layout="fixed" width="100%" xsl:use-attribute-sets="matrix-forms">
			<xsl:variable name="label-width"
				select="floor(($regionBodyWidth - $qid-label-width) * (1.0 - @sqs:form-width-ratio))"/>
			<xsl:variable name="form-width"
				select="floor(($regionBodyWidth - $qid-label-width) * (@sqs:form-width-ratio))"/>

			<fo:table-column column-width="{$qid-label-width}px"/>
			<fo:table-column column-width="{$label-width}px"/>

			<xsl:variable name="textarea-width-total"
				select="sum(xhtml2:thead/xhtml2:tr[1]/xhtml2:th/@sqs:width)"/>
			<xsl:variable name="num-items" select="count(xhtml2:thead/xhtml2:tr[2]/xhtml2:th)"/>

			<xsl:for-each select="xhtml2:thead/xhtml2:tr[1]/xhtml2:th[1 &lt; position()]">
				<xsl:variable name="column-group-index" select="@sqs:column-group-index"/>
				<xsl:choose>
					<xsl:when test="@sqs:xforms-type='select1' or @sqs:xforms-type='select'">
						<xsl:for-each
							select="ancestor::xhtml2:thead/xhtml2:tr[2]/xhtml2:th[@sqs:column-group-index = $column-group-index]">
							<fo:table-column
								column-width="{floor(($form-width - $textarea-width-total) div $num-items )}px"	/>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<fo:table-column column-width="{number(@sqs:width)}px"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
			<xsl:apply-templates/>
		</fo:table>
	</xsl:template>


	<xsl:template match="xhtml2:table[@xhtml2:class='itemset']">
		<fo:table table-layout="fixed" width="100%" start-indent="6pt">
			<!-- 
		<xsl:comment><xsl:value-of select="number(@sqs:cols)"/></xsl:comment>
			<xsl:call-template name="xsl-function-loop">
				<xsl:with-param name="times"><xsl:value-of select="number(@sqs:cols)"/></xsl:with-param>
				<xsl:with-param name="value">
					<fo:table-column column-width="{floor(($regionBodyWidth - $qid-label-width) div number(@sqs:cols))}"/>
				</xsl:with-param>
			</xsl:call-template>
		 -->
			<xsl:apply-templates/>
		</fo:table>
	</xsl:template>

	<xsl:template match="xhtml2:table[@xhtml2:class='item']">
		<fo:table table-layout="fixed" width="5%" start-indent="6pt">
			<fo:table-column column-width="{$item-form-width}px"/>
		<!--
			<fo:table-column column-width="{$item-label-width}px"/>
		-->
			<fo:table-column
				column-width="{floor(number(parent::xhtml2:td/@xhtml2:colspan) * ($regionBodyWidth - $qid-label-width) div number(ancestor::xhtml2:table/@sqs:cols)) - $item-form-width - $item-label-width}px"/>
			<xsl:apply-templates/>
		</fo:table>
	</xsl:template>

	<xsl:template name="table-itemset"> </xsl:template>

	<xsl:template match="xhtml2:thead">
		<fo:table-header>
			<xsl:apply-templates/>
		</fo:table-header>
	</xsl:template>

	<xsl:template match="xhtml2:tbody">
		<fo:table-body>
			<xsl:apply-templates/>
		</fo:table-body>
	</xsl:template>

	<xsl:template match="xhtml2:tr">
		<xsl:choose>
			<xsl:when test="0 &lt; string-length(@id)">
				<xsl:element name="fo:table-row">
					<!--
					<xsl:attribute name="id">
						<xsl:value-of select="@xhtml:id"/>
					</xsl:attribute>
					-->
					<xsl:apply-templates/>
				</xsl:element>
			</xsl:when>
			<xsl:when
				test="ancestor::xhtml2:table/@xhtml2:class = 'itemset' or ancestor::xhtml2:table/@xhtml2:class = 'question'">
				<xsl:element name="fo:table-row">
					<xsl:apply-templates select="@*|@xhtml2:*"/>
					<xsl:apply-templates/>
				</xsl:element>
			</xsl:when>
			<xsl:otherwise>
				<xsl:element name="fo:table-row">
					<xsl:apply-templates select="@*|@xhtml2:*"/>
					<xsl:apply-templates/>
				</xsl:element>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="xhtml2:th">
		<xsl:variable name="number-columns-spanned">
			<xsl:choose>
				<xsl:when test="0 = string-length(@xhtml2:colspan)">1</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@xhtml2:colspan"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="number-rows-spanned">
			<xsl:choose>
				<xsl:when test="0 = string-length(@xhtml2:rowspan)">1</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="@xhtml2:rowspan"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="@xhtml2:class='matrix-forms-label'">
			  <!-- group label -->
				<fo:table-cell xsl:use-attribute-sets="td-block.matrix-forms-label">
					<fo:block padding="2.5px" text-align="center">
						<xsl:apply-templates/>
					</fo:block>
				</fo:table-cell>
			</xsl:when>
			<xsl:when test="ancestor::xhtml2:table/@xhtml2:class = 'matrix-forms'">
				<fo:table-cell number-columns-spanned="{$number-columns-spanned}"
					number-rows-spanned="{$number-rows-spanned}"
					xsl:use-attribute-sets="matrix-forms-th">
					<fo:block xsl:use-attribute-sets="matrix-forms-th-block">
						<xsl:value-of select="."/>
					</fo:block>
				</fo:table-cell>
			</xsl:when>
			<xsl:otherwise>
				<fo:table-cell number-columns-spanned="{$number-columns-spanned}"
					number-rows-spanned="{$number-rows-spanned}">
					<fo:block xsl:use-attribute-sets="p">
						<xsl:apply-templates/>
					</fo:block>
				</fo:table-cell>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="xhtml2:td">
		<xsl:choose>
			<xsl:when test="@xhtml2:class='matrix-forms-column-set'">
				<xsl:choose>
					<xsl:when test="@sqs:column-start = 'true'">
						<fo:table-cell
							xsl:use-attribute-sets="td-block.matrix-forms-column-set-start">
							<fo:block>
								<xsl:apply-templates/>
							</fo:block>
						</fo:table-cell>
					</xsl:when>
					<xsl:otherwise>
						<fo:table-cell xsl:use-attribute-sets="td-block.matrix-forms-column-set">
							<fo:block>
								<xsl:apply-templates/>
							</fo:block>
						</fo:table-cell>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>

			<xsl:when test="@xhtml2:class='matrix-forms-label'">
				<fo:table-cell xsl:use-attribute-sets="td-block.matrix-forms-label">
					<fo:block padding="2.5px">
						<xsl:apply-templates/>
					</fo:block>
				</fo:table-cell>
			</xsl:when>
			<xsl:when test="@xhtml2:class='matrix-forms-hint'">
				<fo:table-cell xsl:use-attribute-sets="td-block.matrix-forms-label">
					<fo:block padding="2.5px">
						<xsl:apply-templates/>
					</fo:block>
				</fo:table-cell>
			</xsl:when>

			<xsl:when test="@xhtml2:class='matrix-forms-td'">
				<fo:table-cell xsl:use-attribute-sets="matrix-forms-td">
					<fo:block>
						<xsl:apply-templates/>
					</fo:block>
				</fo:table-cell>
			</xsl:when>

			<xsl:when test="@xhtml2:class='item-form'">
				<fo:table-cell>
					<fo:block xsl:use-attribute-sets="td-block.item-form">
						<xsl:apply-templates/>
					</fo:block>
				</fo:table-cell>
			</xsl:when>
			<xsl:when test="@xhtml2:class='item-value'">
				<fo:table-cell>
					<fo:block xsl:use-attribute-sets="td-block.item-value">
					<!--
						<xsl:apply-templates/>
					-->
					</fo:block>
				</fo:table-cell>
			</xsl:when>
			<xsl:when test="@xhtml2:class='item-label'">
				<fo:table-cell>
					<fo:block xsl:use-attribute-sets="td-block.item-label">
						<xsl:apply-templates/>
					</fo:block>
				</fo:table-cell>
			</xsl:when>
			<xsl:when test="1 &lt; @xhtml2:colspan">
				<fo:table-cell number-columns-spanned="{@xhtml2:colspan}">
					<fo:block xsl:use-attribute-sets="td-block">
						<xsl:apply-templates/>
					</fo:block>
				</fo:table-cell>
			</xsl:when>
			<xsl:otherwise>
				<fo:table-cell>
					<fo:block xsl:use-attribute-sets="td-block">
						<xsl:apply-templates/>
					</fo:block>
				</fo:table-cell>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="xhtml2:span[@xhtml2:class='bold']">
		<fo:inline font-weight="bold">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>
	<xsl:template match="xhtml2:span[@class='small']">
		<fo:inline xsl:use-attribute-sets="small">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>
	<xsl:template match="xhtml2:strong">
		<fo:inline text-decoration="underline">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>
	<xsl:template match="xhtml2:br">
		<fo:block>
			<fo:inline color="white">&#10;</fo:inline>
		</fo:block>
	</xsl:template>
	<xsl:template match="xhtml2:em">
		<fo:inline font-style="italic" font-weight="bold">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>
	<xsl:template match="xhtml2:img">
		<fo:block>
			<fo:external-graphic src="{@src}" content-width="{@width}" content-height="{@height}"/>
		</fo:block>
	</xsl:template>
	<xsl:template match="xhtml2:sup">
		<fo:inline baseline-shift="super">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>
	<xsl:template match="xhtml2:sub">
		<fo:inline baseline-shift="sub">
			<xsl:apply-templates/>
		</fo:inline>
	</xsl:template>
	<xsl:template match="xhtml2:div">
		<xsl:choose>
			<xsl:when test="@class = 'warning'">
				<fo:block xsl:use-attribute-sets="textbox">
					<xsl:apply-templates/>
				</fo:block>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if
			test="substring-before(substring-after(attribute::style, 'page-brake-after:'), ';') = 'always'">
			<fo:block fo:break-after="page">
				<fo:inline color="white">.</fo:inline>
			</fo:block>
		</xsl:if>
	</xsl:template>
	<xsl:template match="xhtml2:ruby">
		<xsl:for-each select="xhtml2:rb">
			<xsl:apply-templates/>
		</xsl:for-each>
		<xsl:for-each select="xhtml2:rt">
			<fo:inline font-size="50%">(<xsl:apply-templates/>)</fo:inline>
		</xsl:for-each>
	</xsl:template>
	<!--
  <fo:inline-container baseline-indentifier="text-after-edge"
    text-indent="0mm" last-line-end-indent="0mm" start-indent="0mm" end-indent="0mm"
   >
   <fo:block font-size="0.5em" text-align="center" line-height="1.3" space-before.conditionality="retain" wrap-option="no-wrap">
     <xsl:apply-templates select="rt"/>
   </fo:block>
   <fo:block text-align="center" line-height="1" wrap-option="no-wrap">
     <xsl:apply-templates select="rb"/>
   </fo:block>
  </fo:inline-container>
-->

	<xsl:template match="xforms:textarea">
		<fo:block>
			<fo:table table-layout="fixed" width="100%">
				<fo:table-column column-width="{number(@sqs:width)+10}px"/>

				<fo:table-body>
					<fo:table-row>
						<fo:table-cell>
							<fo:block>
							<xsl:if test="$enableMarkSheetMode='true'">
								<fo:instream-foreign-object>
					<xsl:element name="svg:svg">
						<xsl:attribute name="width"><xsl:value-of select="@sqs:width"/>px</xsl:attribute>
						<xsl:attribute name="height"><xsl:value-of select="@sqs:height"/>px</xsl:attribute>
						<xsl:attribute name="id">textarea<xsl:value-of select="@sqs:qid"/></xsl:attribute>

						<svg:metadata>
							<xsl:copy-of select="."/>
						</svg:metadata>

					<svg:g style="stroke:black; stroke-width: 1pt; fill:white;">
					<xsl:element name="svg:rect">
						<xsl:attribute name="x">0</xsl:attribute>
						<xsl:attribute name="y">0</xsl:attribute>
						<xsl:attribute name="rx">5</xsl:attribute>
						<xsl:attribute name="ry">5</xsl:attribute>
						<xsl:attribute name="width"><xsl:value-of select="@sqs:width"/>px</xsl:attribute>
						<xsl:attribute name="height"><xsl:value-of select="@sqs:height"/>px</xsl:attribute>
					</xsl:element>

					</svg:g>
										<!--
				<xsl:if test="ancestor::xhtml2:table[@xhtml2:class != 'matrix-forms']">
-->
					</xsl:element>
								</fo:instream-foreign-object>
								</xsl:if>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</fo:table-body>
			</fo:table>
		</fo:block>
	</xsl:template>

	<xsl:template match="xforms:select|xforms:select1"> </xsl:template>

	<xsl:template match="xforms:select[@sqs:pxform-type='select1' or @sqs:pxform-type='select']">
		<fo:block>
		<xsl:if test="$enableMarkSheetMode='true'">
			<xsl:call-template name="mark"/>
		</xsl:if>
		</fo:block>
	</xsl:template>

	
<!-- //////////////////////////////////////////////////////////////////// -->


	<xsl:param name="mark-type">ellipse</xsl:param>

	<xsl:template name="mark-filled">
		<xsl:param name="width"/>
		<xsl:param name="height"/>
		<fo:instream-foreign-object>
		<svg:svg width="{$width}" height="{$height}">
			<svg:g style="stroke:black; fill:black;">
				<xsl:choose>
					<xsl:when test="$mark-type='rect'">
						<svg:rect x="0" y="0" width="{$width}" height="{$height}"/>
					</xsl:when>
					<xsl:when test="$mark-type='ellipse'">
						<svg:g style="stroke:black; fill:black;">
							<svg:ellipse style="stroke-width:0.5;stroke-dasharray:0.5,1.0"
							cx="{number($width) div 2}" cy="{number($height) div 2}"
							rx="{number($width) div 2.1}" ry="{number($height) div 2.4}"/>
						</svg:g>
					</xsl:when>
				</xsl:choose>
			</svg:g>
		</svg:svg>
		</fo:instream-foreign-object>
	</xsl:template>

	<xsl:template name="mark">
		<xsl:call-template name="generic-mark">
			<xsl:with-param name="width">5</xsl:with-param>
			<xsl:with-param name="height">16</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="generic-mark">
		<xsl:param name="width"/>
		<xsl:param name="height"/>
		<xsl:if test="$enableMarkSheetMode='true'">
		<fo:instream-foreign-object>
		<xsl:element name="svg:svg">
			<xsl:if test="0 &lt; string-length(@sqs:qid)">
				<xsl:attribute name="id">mark<xsl:value-of select="@sqs:qid"/>/<xsl:value-of
						select="@sqs:itemIndex"/></xsl:attribute>
			</xsl:if>
			<xsl:attribute name="width">
				<xsl:value-of select="$width"/>
			</xsl:attribute>
			<xsl:attribute name="height">
				<xsl:value-of select="$height"/>
			</xsl:attribute>

				<xsl:call-template name="generic-mark-core">
					<xsl:with-param name="width">
						<xsl:value-of select="$width"/>
					</xsl:with-param>
					<xsl:with-param name="height">
						<xsl:value-of select="$height"/>
					</xsl:with-param>
				</xsl:call-template>
		</xsl:element>
		</fo:instream-foreign-object>
		</xsl:if>
	</xsl:template>

	<xsl:template name="generic-mark-core">
		<xsl:param name="width"/>
		<xsl:param name="height"/>

		<svg:metadata>
			<xsl:copy-of select="."/>
		</svg:metadata>

		<xsl:choose>
			<xsl:when test="$mark-type='rect'">
				<svg:g svg:style="stroke:black; fill:black;">
					<svg:rect x="0" y="0" width="{$width}" height="5"/>
					<svg:rect x="0" y="{$height - 5}" width="{$width}" height="5"/>
				</svg:g>
				<svg:g style="stroke:white; fill:white;">
					<svg:rect x="1.5" y="3" width="{$width - 3}" height="{$height - 6}"/>
				</svg:g>
			</xsl:when>
			<xsl:when test="$mark-type='ellipse'">
				<svg:g style="stroke:black; fill:white;">
					<svg:ellipse style="stroke-width:0.5;stroke-dasharray:0.5,1.0"
						cx="{number($width) div 2}" cy="{number($height) div 2}"
						rx="{number($width) div 2.1}" ry="{number($height) div 2.4}"/>
				</svg:g>
				<!--
					<svg:g style="stroke:white; fill:white;">
						<svg:ellipse style="stroke-width:0.1"
							 cx="{number($width) div 2}"
							 cy="{number($height) div 2}"
							 rx="{number($width) div 2.4}"
							 ry="{number($height) div 2.8}"/>
					</svg:g>
					-->
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:param name="example-blank-mark-label"/>
	<xsl:param name="example-filled-mark-label"/>
	<xsl:param name="example-incomplete-mark-label"/>

	<xsl:template match="sqs:marking-example">
		<fo:table table-layout="fixed" width="0%" margin-left="-18px">

			<fo:table-column column-width="60px"/>
			<fo:table-column column-width="6px"/>
			<fo:table-column column-width="70px"/>
			<fo:table-column column-width="6px"/>   
			<fo:table-column column-width="95px"/>
			<fo:table-column column-width="6px"/>
			<fo:table-column column-width="95px"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<fo:block/>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-left="0px" border-left="0px" margin-left="0px">
							<!-- blank mark -->
							<xsl:call-template name="mark"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="{$fontFamily}" font-size="10pt" margin-left="0px"
							margin-right="0px">
							<xsl:value-of select="$example-blank-mark-label"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-left="0px" border-left="0px" margin-left="0px">
								<xsl:call-template name="mark-filled">
								 <xsl:with-param name="width"><xsl:value-of select="$markAreaWidth"/></xsl:with-param>
								 <xsl:with-param name="height"><xsl:value-of select="$markAreaHeight"/></xsl:with-param>
								</xsl:call-template>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="{$fontFamily}" font-size="10pt" margin-left="0px"
							margin-right="0px">
							<xsl:value-of select="$example-filled-mark-label"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block padding-left="0px" border-left="0px" margin-left="0px">
								<xsl:call-template name="bad-mark"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-family="{$fontFamily}" font-size="10pt" margin-left="0px"
							margin-right="0px">
							<xsl:value-of select="$example-incomplete-mark-label"/>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<xsl:template name="bad-mark">
		<xsl:variable name="width">
			<xsl:value-of select="number(5)"/>
		</xsl:variable>
		<xsl:variable name="height">
			<xsl:value-of select="number(16)"/>
		</xsl:variable>
		<xsl:if test="$enableMarkSheetMode='true'">
		<fo:instream-foreign-object>
		<xsl:element name="svg:svg">
			<xsl:attribute name="width">
				<xsl:value-of select="5"/>
			</xsl:attribute>
			<xsl:attribute name="height">
				<xsl:value-of select="16"/>
			</xsl:attribute>
			<xsl:call-template name="generic-mark-core">
				<xsl:with-param name="width">
					<xsl:value-of select="5"/>
				</xsl:with-param>
				<xsl:with-param name="height">
					<xsl:value-of select="16"/>
				</xsl:with-param>
			</xsl:call-template>
			<svg:g style="stroke:black; fill:black; stroke-width=0.3pt">
				<xsl:element name="svg:path">
				<xsl:attribute name="d">M <xsl:value-of select="$width"/> 0 L 0 <xsl:value-of select="$height"/> Z</xsl:attribute>
				</xsl:element>
			</svg:g>
		</xsl:element>
		</fo:instream-foreign-object>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="bad-marks">
		<xsl:variable name="width">
				<xsl:value-of select="number(5)"/>
		</xsl:variable>
		<xsl:variable name="height">
				<xsl:value-of select="number(16)"/>
		</xsl:variable>
		<fo:instream-foreign-object>
		<xsl:element name="svg:svg">
			<xsl:attribute name="width">
				<xsl:value-of select="$width"/>
			</xsl:attribute>
			<xsl:attribute name="height">
				<xsl:value-of select="$height"/>
			</xsl:attribute>
			<xsl:call-template name="generic-mark-core">
				<xsl:with-param name="width">
					<xsl:value-of select="$width"/>
				</xsl:with-param>
				<xsl:with-param name="height">
					<xsl:value-of select="$height"/>
				</xsl:with-param>
			</xsl:call-template>
			<svg:g style="stroke:black; fill:black; stroke-width=0.3pt">
				<svg:path d="M 2.5 10.5 L 8 1 Z"/>
				<svg:path d="M 2.5 10.5 L 1 5 Z"/>
			</svg:g>
		</xsl:element>
		</fo:instream-foreign-object>
	</xsl:template>
	
	<xsl:template match="sqs:mark">
		<fo:inline padding-left="0px" border-left="0px" margin-left="0px">
			<xsl:call-template name="mark"/>
		</fo:inline>
	</xsl:template>

</xsl:stylesheet>
