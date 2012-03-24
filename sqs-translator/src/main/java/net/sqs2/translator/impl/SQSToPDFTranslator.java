/*

 SQSToPDFTranslator.java

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

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;

import javax.xml.transform.URIResolver;

import net.sqs2.translator.TranslatorException;
import net.sqs2.util.FileUtil;
import net.sqs2.util.VersionTag;
import net.sqs2.xml.XMLUtil;
import net.sqs2.xmlns.SQSNamespaces;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.render.pdf.PageRectangle;
import org.apache.fop.render.pdf.SVGElementIDToPageRectangleMap;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class SQSToPDFTranslator extends FOPTranslator {
	
	private static final float SCALE = 1.0f;

	String basename;

	public SQSToPDFTranslator(String groupID, String appID, String fopURL, String xsltURL, String language, String filename, 
			URIResolver uriResolver, PageSetting pageSetting)
	throws TranslatorException {
		super(groupID, appID, fopURL, xsltURL, language, uriResolver, pageSetting);
		this.basename = FileUtil.getBasename(filename);
	}

	public SQSToPDFTranslator(String groupID, String appID, String fopURL, String xsltURL, String filename, 
			URIResolver uriResolver, PageSetting pageSetting)
	throws TranslatorException {
		super(groupID, appID, fopURL, xsltURL, uriResolver, pageSetting);
		this.basename = FileUtil.getBasename(filename);
	}

	private String getBasename() {
		return basename;
	}

	private byte[] createSVGPrint(FOUserAgent userAgent, int numPages) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			writer.println("<svg:svg ");
			writer.println(" xmlns=\"" + SQSNamespaces.SVG_URI + "\" ");
			writer.println(" xmlns:svg=\"" + SQSNamespaces.SVG_URI + "\" ");
			writer.println(" xmlns:sqs=\"" + SQSNamespaces.SQS2004_URI + "\" ");
			writer.println(" xmlns:xforms=\"" + SQSNamespaces.XFORMS_URI + "\" ");
			writer.println(" xmlns:master=\"" + SQSNamespaces.SQS2007MASTER_URI + "\" ");
			writer.print("width=\"");
			PageSetting pageSetting = (PageSetting)userAgent.getRendererOptions().get("pageSetting");
			writer.print(pageSetting.getWidth());
			writer.print("\" height=\"");
			writer.print(pageSetting.getHeight());
			writer.println("\">");
			
			pageSetting.init(SVGElementIDToPageRectangleMap.getInstance(), userAgent);
			
			printPageSet(pageSetting, userAgent, numPages, writer);
			writer.println("</svg:svg>");
			writer.close();
			out.close();
			
			byte[] svgBytes = out.toByteArray();

			if (false) {
				ByteArrayInputStream svgInputStream = new ByteArrayInputStream(svgBytes);
				OutputStream sqmOutputStream = new BufferedOutputStream(new FileOutputStream("/tmp/sqs.sqm"));
				FileUtil.connect(svgInputStream, sqmOutputStream);
				svgInputStream.close();
				sqmOutputStream.close();
			}

			return svgBytes;
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (SQMSchemeException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void printPageSet(PageSetting pageSetting, FOUserAgent userAgent, int numPages, PrintWriter writer) {
		writer.println("<svg:pageSet>");

		printMasterPage(pageSetting, numPages, writer);

		SVGElementIDToPageRectangleMap svgElementIDToPageRectangleMap = SVGElementIDToPageRectangleMap
		.getInstance();

		if(new VersionTag(pageSetting.getVersion()).isSameOrOlderThan(new VersionTag("1.3.3"))){
			for (int pageIndex = 0; pageIndex < numPages; pageIndex++) {
				writer.println("  <svg:page>");
				Map<String, PageRectangle> map = svgElementIDToPageRectangleMap.remove(userAgent, pageIndex);
				if (map != null) {
					for (Map.Entry<String, PageRectangle> entry : map.entrySet()) {
						String id = entry.getKey();
						PageRectangle pageRectangle = entry.getValue();
						pageIndex = pageRectangle.getPageIndex();
						printGElement(pageSetting, id, pageRectangle, writer);
					}
				}
				writer.println("  </svg:page>");
			}
		}else{
			for (int pageIndex = 0; pageIndex < numPages; pageIndex++) {
				writer.println("  <svg:page>");
				Map<String, PageRectangle> map = svgElementIDToPageRectangleMap.remove(userAgent, pageIndex);
				if (map != null) {
					for (Map.Entry<String, PageRectangle> entry : map.entrySet()) {
						String id = entry.getKey();
						if(id.startsWith("mark") || id.startsWith("textarea") ){
							PageRectangle pageRectangle = entry.getValue();
							pageIndex = pageRectangle.getPageIndex();
							printGElement(pageSetting, id, pageRectangle, writer);
						}
					}
				}
				writer.println("  </svg:page>");
			}
		}		
		writer.println("</svg:pageSet>");
	}

	private void printGElement(PageSetting pageSetting, String id, PageRectangle area, PrintWriter writer) {
		writer.print("<svg:g id=\"");
		writer.print(id);
		writer.println("\">");
		writer.print("<svg:rect x=\"");
		writer.print(area.getX() / SCALE);
		writer.print("\" y=\"");
		writer.print(pageSetting.getHeight() - area.getY() / SCALE); //
		writer.print("\" width=\"");
		writer.print(area.getWidth() / SCALE);
		writer.print("\" height=\"");
		writer.print(area.getHeight() / SCALE);
		writer.println("\">");
		writer.println(XMLUtil.createString((Element) area.getSVGMetadataNode()));
		writer.println("</svg:rect>");
		writer.println("</svg:g>");
	}

	private void printMasterPage(PageSetting pageSetting, int numPages, PrintWriter writer) {
		writer.println(" <svg:masterPage>");
		writer.println("  <svg:metadata>");
		writer.print("      <master:master master:version=\"" + pageSetting.getVersion()+ "\" master:numPages=\"");
		writer.print(numPages);
		writer.println("\" />");
		printDeskewGuideElement(pageSetting, writer);
		writer.println("  </svg:metadata>");
		writer.println(" </svg:masterPage>");
	}

	private void printDeskewGuideElement(PageSetting pageSetting, PrintWriter writer) {
		if(pageSetting.getDeskewGuideCenterPointArray()[0] == null){
			Logger.getLogger(getClass().getName()).fatal("deskewGuides are null.");
			return;
		}
		
		writer.print("      <master:deskewGuide master:x1=\"");
		writer.print(pageSetting.getDeskewGuideCenterPointArray()[0].getX());
		writer.print("\" master:y1=\"");
		writer.print(pageSetting.getHeight() - pageSetting.getDeskewGuideCenterPointArray()[0].getY());
		writer.print("\" master:x2=\"");
		writer.print(pageSetting.getDeskewGuideCenterPointArray()[1].getX());
		writer.print("\" master:y2=\"");
		writer.print(pageSetting.getHeight() - pageSetting.getDeskewGuideCenterPointArray()[1].getY());
		writer.print("\" master:x3=\"");
		writer.print(pageSetting.getDeskewGuideCenterPointArray()[2].getX());
		writer.print("\" master:y3=\"");
		writer.print(pageSetting.getHeight() - pageSetting.getDeskewGuideCenterPointArray()[2].getY());
		writer.print("\" master:x4=\"");
		writer.print(pageSetting.getDeskewGuideCenterPointArray()[3].getX());
		writer.print("\" master:y4=\"");
		writer.print(pageSetting.getHeight() - pageSetting.getDeskewGuideCenterPointArray()[3].getY());
		writer.println("\" />");
	}
	
	synchronized public void translate(byte[] sqsSourceBytes, ByteArrayInputStream foInputStream, String systemId, OutputStream pdfOutputStream) throws TranslatorException {
		try {
			ByteArrayOutputStream pdfRawDataOutputStream = new ByteArrayOutputStream(65536);

			FOUserAgent userAgent = getTranslatorCore().getUserAgent();
			userAgent.setBaseURL(systemId);

			synchronized (SQSToPDFTranslator.class) {
				Fop fop = getTranslatorCore().createFop(pdfRawDataOutputStream);
				render(fop.getDefaultHandler(), foInputStream, systemId);
			}

			pdfRawDataOutputStream.flush();
			byte[] pdfRawDataBytes = pdfRawDataOutputStream.toByteArray();

			foInputStream.close();
			foInputStream = null;
			combinePDFData(userAgent, sqsSourceBytes, pdfRawDataBytes, pdfOutputStream, getBasename());

			pdfRawDataOutputStream.close();
			pdfRawDataOutputStream = null;
			pdfOutputStream.flush();

		} catch (FOPException ex) {
			ex.printStackTrace();
			throw new TranslatorException(ex);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new TranslatorException(ex);
		}
	}

	private void combinePDFData(FOUserAgent userAgent, byte[] sqsSourceBytes, byte[] pdfRawDataBytes, OutputStream pdfOutputStream, String basename) throws IOException {
		try {
			PdfReader reader = new PdfReader(pdfRawDataBytes);
			int numPages = reader.getNumberOfPages();
			userAgent.getRendererOptions().get("pageWidth");
			byte[] svgBytes = createSVGPrint(userAgent, numPages);
			PdfStamper stamp = new PdfStamper(reader, pdfOutputStream);
			stamp.addFileAttachment("SQS Source", sqsSourceBytes, null, basename + ".sqs");
			stamp.addFileAttachment("SQS Master", svgBytes, null, basename + ".sqm");
			stamp.close();
			reader.close();
		} catch (DocumentException ex) {
			ex.printStackTrace();
		}
	}
}
