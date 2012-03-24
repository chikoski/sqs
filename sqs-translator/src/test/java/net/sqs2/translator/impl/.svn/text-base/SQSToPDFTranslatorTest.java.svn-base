/*

 SQSToPDFTranslatorTest.java
 
 Copyright 2004 KUBO Hiroya (hiroya@sfc.keio.ac.jp).
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
 Created on 2007/09/04

 */
package net.sqs2.translator.impl;

import java.io.BufferedInputStream;


import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.transform.URIResolver;

import org.w3c.dom.Document;

import net.sqs2.net.ClassURIResolver;
import net.sqs2.translator.TranslatorException;
import net.sqs2.xml.XMLUtil;
import net.sqs2.xml.XPathUtil;

import junit.framework.TestCase;

public class SQSToPDFTranslatorTest extends TestCase {
	
	Document document;
	protected URIResolver uriResolver;
	
	public SQSToPDFTranslatorTest()throws Exception{
		
		uriResolver = new ClassURIResolver();
		
		PageSetting pageSetting = new PageSettingImpl(595, 842);
		SQSToPDFTranslator translator = createSourceEditorTranslator(uriResolver, pageSetting);
		File srcFile = new File("src/test/resources/simple_en.sqs");
		BufferedInputStream sqsSourceInputStream = new BufferedInputStream(new FileInputStream(srcFile));
		File pdfFile = File.createTempFile("sqs", "pdf");
		pdfFile.deleteOnExit();
		
		BufferedOutputStream pdfOutputStream = new BufferedOutputStream(new FileOutputStream(pdfFile));
		translator.execute(sqsSourceInputStream, srcFile.toURI().toString(), pdfOutputStream, uriResolver);
		PDFAttachmentReader attachmentReader = new PDFAttachmentReader(new BufferedInputStream(new FileInputStream(pdfFile)));
		byte[] sqmBytes = attachmentReader.extractAttachmentFiles(".sqm");
		this.document = XMLUtil.createDocumentBuilder().parse(new ByteArrayInputStream(sqmBytes));
		XMLUtil.marshal(this.document, System.err);
	}
	
	private SQSToPDFTranslator createSourceEditorTranslator(URIResolver uriResolver, PageSetting pageSetting)throws TranslatorException{
		return createSQSToPDFTranslator("sqs","SourceEditor","en", uriResolver, pageSetting);
	}
		
	private SQSToPDFTranslator createSQSToPDFTranslator(String groupID, String appID, String language, URIResolver uriResolver, PageSetting pageSetting)throws TranslatorException{
		return new SQSToPDFTranslator(groupID, appID, 
				TranslatorJarURIContext.getFOPBaseURI(), 
				TranslatorJarURIContext.getXSLTBaseURI(), 
				language,
				"test",
				uriResolver,
				pageSetting);
	}
	
	public void setUp() throws Exception {
	}
	
	public void testVersion()throws Exception{
		assertEquals("2.1.0", XPathUtil.getStringValue(this.document.getDocumentElement(), 
				"/svg:svg/svg:pageSet/svg:masterPage/svg:metadata/master:master/@master:version"));
	}
	
	public void testRectWidthHeight()throws Exception{
		assertEquals("5.0", XPathUtil.getStringValue(this.document.getDocumentElement(),
		"/svg:svg/svg:pageSet/svg:page[1]/svg:g/svg:rect/@width"));
		assertEquals("16.0", XPathUtil.getStringValue(this.document.getDocumentElement(),
		"/svg:svg/svg:pageSet/svg:page[1]/svg:g/svg:rect/@height"));
	}
}
