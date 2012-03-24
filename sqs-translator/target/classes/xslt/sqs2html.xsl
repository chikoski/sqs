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
  xmlns="http://www.w3.org/1999/xhtml"
  xmlns:xhtml="http://www.w3.org/1999/xhtml"
  xmlns:xhtml2="http://www.w3.org/2002/06/xhtml2"
  xmlns:xforms="http://www.w3.org/2002/xforms"
  xmlns:sqs="http://sqs.cmr.sfc.keio.ac.jp/2004/sqs"
  exclude-result-prefixes="xhtml"
  version="1.0">

  
<!-- <xsl:namespace-alias stylesheet-prefix="xhtml" result-prefix="#default"/> -->
<xsl:output 
		method="xml" 
		encoding="UTF-8" 
		omit-xml-declaration="yes" 
	    indent="yes" 
 		doctype-public="-//W3C//DTD XHTML 1.1//EN" 
		doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" 
		media-type="text/html; charset=UTF-8"
/>

	<xsl:param name="form-submit-label">Submit</xsl:param>

<xsl:template match="/">
 <xsl:apply-templates/>
</xsl:template>
 
<xsl:template match="sqs:mark">
  <img src="" alt="[mark]"/>
</xsl:template>

<xsl:template match="sqs:marking-example">
  <img src="" alt="[mark1]"/>
  <img src="" alt="[mark2]"/>
  <img src="" alt="[mark3]"/>
</xsl:template>

<xsl:template match="sqs:warning">
  <div class="warning">
    <xsl:apply-templates/>
  </div>
</xsl:template>

<!-- form-render -->

<xsl:template name="qid">
		<xsl:choose>
			<xsl:when test="0 = string-length(@sqs:qid)">a<xsl:number level="any" from="/" count="xforms:select | xforms:select1 | xforms:input | xforms:textarea | xforms:range" format="1"/></xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="@sqs:qid"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="xforms:select1">
		<xsl:variable name="qid"><xsl:call-template name="qid"/></xsl:variable>
		<xsl:for-each select="xforms:item">
			<input type="radio" name="{$qid}" value="{xforms:value}"/>
			<xsl:value-of select="xforms:value"/>: <xsl:for-each select="xforms:label"><xsl:apply-templates/></xsl:for-each>
			<br/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="xforms:select">
		<xsl:variable name="qid"><xsl:call-template name="qid"/></xsl:variable>
		<xsl:choose>
			<xsl:when test="0 &lt; string-length(@sqs:pxform-type)">
				<xsl:for-each select="xforms:item">
					<input type="checkbox" name="{$qid}" value="true"/>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="xforms:item">
					<input type="checkbox" name="{$qid}/v{xforms:value}" value="true"/>
					<xsl:value-of select="xforms:value"/>: <xsl:for-each select="xforms:label"><xsl:apply-templates/></xsl:for-each>
					<br/>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="xforms:input">
		<xsl:variable name="qid"><xsl:call-template name="qid"/></xsl:variable>
		<xsl:choose>
			<xsl:when test="0 &lt; string-length(@sqs:width)">
				<input type="text" name="{$qid}" value="{xforms:value}" size="{ceiling(number(@sqs:width) div 14)}"/>
			</xsl:when>
			<xsl:otherwise>
				<input type="text" name="{$qid}" value="{xforms:value}"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="xforms:textarea">
		<xsl:variable name="qid"><xsl:call-template name="qid"/></xsl:variable>
		<xsl:choose>
			<xsl:when test="0 &lt; string-length(@sqs:width)">
				<textarea name="{$qid}" cols="{ceiling(number(@sqs:width) div 14)}" rows="{ceiling(number(@sqs:height) div 14)}"><xsl:text> </xsl:text></textarea>
			</xsl:when>
			<xsl:otherwise>
				<textarea name="{$qid}"><xsl:text> </xsl:text></textarea>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!-- XHMLT2 to XHTML1 -->
	<xsl:template match="xhtml2:*">
		<xsl:element name="{local-name()}">
			<xsl:for-each select="@xhtml2:*|@*">
				<xsl:attribute name="{local-name()}"><xsl:value-of select="."/></xsl:attribute>
			</xsl:for-each>
			<xsl:apply-templates select="child::node()"/>
		</xsl:element>
	</xsl:template>
	<xsl:template match="xforms:*">
		<xsl:element name="xforms:{local-name()}">
			<xsl:for-each select="@*">
				<xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>
			</xsl:for-each>
			<xsl:apply-templates select="child::node()"/>
		</xsl:element>
	</xsl:template>

	<xsl:template match="xhtml2:html">	    
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>

	<xsl:template match="xhtml2:title">
		<title>
			<xsl:apply-templates/>
		</title>
	</xsl:template>

	<xsl:template match="xhtml2:p">
		<p>
			<xsl:apply-templates/>
		</p>
	</xsl:template>

	<xsl:template match="xhtml2:table">
		<table>
			<xsl:apply-templates/>
		</table>
	</xsl:template>

	<xsl:template match="xhtml2:thead">
		<thead>
			<xsl:apply-templates/>
		</thead>
	</xsl:template>

	<xsl:template match="xhtml2:tbody">
		<tbody>
			<xsl:apply-templates/>
		</tbody>
	</xsl:template>

	<xsl:template match="xhtml2:tfoot">
		<tfoot>
			<xsl:apply-templates/>
		</tfoot>
	</xsl:template>

	<xsl:template match="xhtml2:tr">
		<tr>
			<xsl:apply-templates/>
		</tr>
	</xsl:template>

	<xsl:template match="xhtml2:th">
		<th>
			<xsl:for-each select="@xhtml2:*|@*">
				<xsl:attribute name="{local-name()}"><xsl:value-of select="."/></xsl:attribute>
			</xsl:for-each>
			<xsl:apply-templates/>
		</th>
	</xsl:template>

	<xsl:template match="xhtml2:td">
		<td>
			<xsl:for-each select="@xhtml2:*|@*">
				<xsl:attribute name="{local-name()}"><xsl:value-of select="."/></xsl:attribute>
			</xsl:for-each>
			<xsl:apply-templates/>
		</td>
	</xsl:template>

	<xsl:template match="xhtml2:body//xhtml2:h">
		<h1>
			<xsl:apply-templates/>
		</h1>
	</xsl:template>
	<xsl:template match="xhtml2:body//xhtml2:section/xhtml2:h">
		<h2>
			<xsl:apply-templates/>
		</h2>
	</xsl:template>
	<xsl:template match="xhtml2:body//xhtml2:section//xhtml2:section/xhtml2:h">
		<h3>
			<xsl:apply-templates/>
		</h3>
	</xsl:template>
	<xsl:template match="xhtml2:body//xhtml2:section//xhtml2:section/xhtml2:section/xhtml2:h">
		<h4>
			<xsl:apply-templates/>
		</h4>
	</xsl:template>
	<xsl:template match="xhtml2:section">
		<xsl:apply-templates/>
		<hr />
	</xsl:template>
	<xsl:template match="xhtml2:head">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
			<style type="text/css">
table{
 border-style: solid;
 border-width: 1px;
 border-color: black;
}
th,td{
 border-style: solid;
 border-width: 1px;
 border-color: black;
 vertical-align: top;
}
div.warning{
border-style: solid;
border-color: black;
border-width: 1px;
}
strong{
  text-decoration: underline;
}
        </style>
			<xsl:apply-templates/>
		</head>
	</xsl:template>
	
	<xsl:template match="xhtml2:body">
		<body>
			<form action="" method="post">
				<xsl:apply-templates/>
<xsl:if test="0 &lt; string-length($form-submit-label)">
  <input type="submit" value="{$form-submit-label}"/>
</xsl:if>
			</form>
		</body>
	</xsl:template>

</xsl:stylesheet>
