/*

 XMLFormMasterFactory.java
 
 Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
 Created on 2005/02/26

 */

package net.sqs2.omr.master.sqm;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.FormMasterException;
import net.sqs2.omr.master.FormMasterFactory;
import net.sqs2.omr.master.InvalidPageMasterException;
import net.sqs2.omr.master.NoConfigFilePageMasterException;
import net.sqs2.omr.master.PageMasterMetadata;
import net.sqs2.util.FileResourceID;
import net.sqs2.util.VersionTag;
import net.sqs2.xml.XMLUtil;
import net.sqs2.xml.XPathSelector;
import net.sqs2.xmlns.SQSNamespaces;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLFormMasterFactory implements FormMasterFactory {

	private static final boolean DEBUG = false;

	XPathSelector xpathSelector;

	static FormMasterFactory createInstance() {
		return new XMLFormMasterFactory();
	}

	public XMLFormMasterFactory() {
	}

	public FormMaster create(File sourceDirectoryRoot, String masterPath) throws FormMasterException {
		String masterPathLower = masterPath.toLowerCase();
		if (masterPathLower.endsWith(".sqm")) {
			File file = new File(sourceDirectoryRoot, masterPath);
			PageMasterMetadata pageMasterMetadata = new PageMasterMetadata(sourceDirectoryRoot, masterPath,
					"SQS Master File", file.lastModified(), null);
			return createFormMaster(pageMasterMetadata, createDocumentBySQM(file, masterPath), null);
		}
		return null;
	}

	protected Rectangle2D createRectangle(Element rect) {
		return new Rectangle2D.Float(Float.parseFloat(this.xpathSelector.selectAttribute(rect, "x", SQSNamespaces.SVG_URI)),
				Float.parseFloat(this.xpathSelector.selectAttribute(rect, "y", SQSNamespaces.SVG_URI)), Float
						.parseFloat(this.xpathSelector.selectAttribute(rect, "width", SQSNamespaces.SVG_URI)), Float
						.parseFloat(this.xpathSelector.selectAttribute(rect, "height", SQSNamespaces.SVG_URI)));
	}

	private void setMasterMetadata(Element svgNode, FormMaster master) throws TransformerException, XPathExpressionException {
		Element metadataNode = this.xpathSelector.selectSingleNode(svgNode,
				"/svg:svg/svg:pageSet/svg:masterPage/svg:metadata");
		setMasterMaster(metadataNode, master);
		setDeskewGuideCenterPoints(metadataNode, master);
		if (new VersionTag(master.getVersion()).isOlderThan(new VersionTag("2.0"))) {
			setMasterCheckers(metadataNode, master);
		}
	}

	private void setMasterCheckers(Element metadataNode, FormMaster master) throws TransformerException, XPathExpressionException {

		Element upsideDownCheckerHeaderRectangle = null;
		Element upsideDownCheckerFooterRectangle = null;
		Element evenOddCheckerLeftRectangle = null;
		Element evenOddCheckerRightRectangle = null;

		if ("1.1".equals(master.getVersion())) {
			upsideDownCheckerHeaderRectangle = this.xpathSelector.selectSingleNode(metadataNode,
					"master:upsideDownChecker/master:checkerArea[@side='header']/svg:rect");
			upsideDownCheckerFooterRectangle = this.xpathSelector.selectSingleNode(metadataNode,
					"master:upsideDownChecker/master:checkerArea[@side='footer']/svg:rect");
			evenOddCheckerLeftRectangle = this.xpathSelector.selectSingleNode(metadataNode,
					"master:evenOddChecker/master:checkerArea[@side='left']/svg:rect");
			evenOddCheckerRightRectangle = this.xpathSelector.selectSingleNode(metadataNode,
					"master:evenOddChecker/master:checkerArea[@side='right']/svg:rect");
		} else {
			upsideDownCheckerHeaderRectangle = this.xpathSelector.selectSingleNode(metadataNode,
					"master:upsideDownChecker/master:checkerArea[@master:side='header']/svg:rect");
			upsideDownCheckerFooterRectangle = this.xpathSelector.selectSingleNode(metadataNode,
					"master:upsideDownChecker/master:checkerArea[@master:side='footer']/svg:rect");
			evenOddCheckerLeftRectangle = this.xpathSelector.selectSingleNode(metadataNode,
					"master:evenOddChecker/master:checkerArea[@master:side='left']/svg:rect");
			evenOddCheckerRightRectangle = this.xpathSelector.selectSingleNode(metadataNode,
					"master:evenOddChecker/master:checkerArea[@master:side='right']/svg:rect");
		}

		try{
			master.setHeaderCheckArea(createRectangle(upsideDownCheckerHeaderRectangle));
		}catch(NullPointerException ignore){
			Logger.getLogger(getClass().getName()).warning("upsideDownChecker:Header undefined");
		}
		try{
			master.setFooterCheckArea(createRectangle(upsideDownCheckerFooterRectangle));
		}catch(NullPointerException ignore){
			Logger.getLogger(getClass().getName()).warning("upsideDownChecker:Footer undefined");
		}
		try{
			master.setFooterLeftRectangle(createRectangle(evenOddCheckerLeftRectangle));
		}catch(NullPointerException ignore){
			Logger.getLogger(getClass().getName()).warning("evenOddChecker:Left undefined");
		}
		try{
			master.setFooterRightRectangle(createRectangle(evenOddCheckerRightRectangle));
		}catch(NullPointerException ignore){
			Logger.getLogger(getClass().getName()).warning("evenOddChecker:Right undefined");
		}
	}

	private void setMasterMaster(Element metadataNode, FormMaster master) throws TransformerException, XPathExpressionException {
		Element masterNode = this.xpathSelector.selectSingleNode(metadataNode, "master:master");

		String version = this.xpathSelector.selectAttribute(masterNode, "version", SQSNamespaces.SQS2007MASTER_URI);
		master.setVersion(version);

		master.setNumPages(Integer.parseInt(this.xpathSelector.selectAttribute(masterNode, "numPages",
				SQSNamespaces.SQS2007MASTER_URI)));

		String horizontalOffset = this.xpathSelector.selectAttribute(masterNode, "horizontalOffset",
				SQSNamespaces.SQS2007MASTER_URI);
		if (horizontalOffset != null) {
			try {
				master.setHorizontalOffset(Integer.parseInt(this.xpathSelector.selectAttribute(masterNode,
						"horizontalOffset", SQSNamespaces.SQS2007MASTER_URI)));
			} catch (Exception ignore) {
			}
		}
		String verticalOffset = this.xpathSelector.selectAttribute(masterNode, "verticalOffset",
				SQSNamespaces.SQS2007MASTER_URI);
		if (verticalOffset != null) {
			try {
				master.setVerticalOffset(Integer.parseInt(this.xpathSelector.selectAttribute(masterNode,
						"verticalOffset", SQSNamespaces.SQS2007MASTER_URI)));
			} catch (Exception ignore) {
			}
		}
	}

	private void setDeskewGuideCenterPoints(Element metadataNode, FormMaster master) throws TransformerException, XPathExpressionException {
		Element deskewGuideNode = null;
		for(String nodeName:new String[]{"master:deskewGuide", "master:corner"}){
			deskewGuideNode = this.xpathSelector.selectSingleNode(metadataNode, nodeName);
			if(deskewGuideNode != null){
				break;
			}
		}
		if(deskewGuideNode == null){
			throw new TransformerException("master:deskewGuide node not found.");
		}
		Point2D.Double[] deskewGuideCornerPoints = new Point.Double[4];
		for (int i = 1; i <= 4; i++) {
			double x = Double.parseDouble(this.xpathSelector.selectAttribute(deskewGuideNode, 
					"x"+ i, 
					SQSNamespaces.SQS2007MASTER_URI));
			double y = Double.parseDouble(this.xpathSelector.selectAttribute(deskewGuideNode, 
					"y" + i, 
					SQSNamespaces.SQS2007MASTER_URI));
			deskewGuideCornerPoints[i - 1] = new Point2D.Double(x, y);
		}
		master.setDeskewGuideCenterPoints(deskewGuideCornerPoints);
	}

	private ArrayList<FormArea> getAreaListByQID(FormMaster master, String qid) {
		ArrayList<FormArea> areaList = master.getFormAreaList(qid);
		if (areaList == null) {
			areaList = new ArrayList<FormArea>();
			master.putFormAreaList(qid, areaList);
		}
		return areaList;
	}

	protected Document createDocumentBySQM(File sqmFile, String path) throws FormMasterException {
		InputStream sqmInputStream = null;
		ByteArrayInputStream fixedSQMInputStream = null;
		try {
			sqmInputStream = new FileInputStream(sqmFile);
			fixedSQMInputStream = XMLFormMasterFactory.hotfixXFormsNSURItoSVGNSURI(sqmInputStream);
			return createDocumentByStream(fixedSQMInputStream, path);
		} catch (InvalidPageMasterException ex) {
			throw new FormMasterException(sqmFile);
		} catch (IOException ex) {
			throw new FormMasterException(sqmFile);
		} finally {
			try {
				sqmInputStream.close();
				fixedSQMInputStream.close();
			} catch (Exception ignore) {
			}
		}
	}

	protected Document createDocumentByStream(InputStream inputStream, String path) throws InvalidPageMasterException {
		try {
			return XMLUtil.createDocumentBuilder().parse(
					new InputSource(new InputStreamReader(inputStream, "UTF-8")));
		} catch (IOException ex) {
			throw new InvalidPageMasterException(null);
		} catch (SAXException ex) {
			throw new InvalidPageMasterException(null);
		} catch (FactoryConfigurationError ex) {
			throw new RuntimeException(ex);
		} catch (ParserConfigurationException ex) {
			throw new RuntimeException(ex);
		}
	}

	public FormMaster createFormMaster(PageMasterMetadata metadata, Document sqmDocument, Document sqsDocument) throws FormMasterException {
		try {

			FormMaster master = new FormMaster(new FileResourceID(metadata.getMasterPath(), metadata
					.getLastModified()), metadata);
			Element svgNode = sqmDocument.getDocumentElement();

			this.xpathSelector = new XPathSelector(sqmDocument, "svg");

			setMasterMetadata(svgNode, master);

			int prevPage = -1;
			int columnIndex = -1;
			String prevQID = null;
			int areaIndexInPage = 0;
			int itemIndex = 0;

			// NodeList pageElementList = XPathAPI.selectNodeList(svgNode,
			// "svg:pageSet/svg:page");
			NodeList pageElementList = this.xpathSelector.selectNodeList(svgNode, "svg:pageSet/svg:page");

			for (int pageIndex = 0; pageIndex < pageElementList.getLength(); pageIndex++) {

				// NodeList gElementList =
				// XPathAPI.selectNodeList(pageElementList.item(pageIndex),
				// "svg:g");
				NodeList gElementList = this.xpathSelector.selectNodeList((Element) pageElementList.item(pageIndex),
						"svg:g");

				ArrayList<FormArea> areaListByPageIndex = new ArrayList<FormArea>();
				master.addFormAreaList(areaListByPageIndex);

				for (int gIndex = 0; gIndex < gElementList.getLength(); gIndex++) {

					Element gElement = (Element) gElementList.item(gIndex);

					FormArea area = SQMFormAreaFactory.create(this.xpathSelector, master, gElement, pageIndex);
					if (area == null) {
						throw new FormMasterException(metadata.getMasterFile());
					}
					if (prevPage != area.getPage()) {
						areaIndexInPage = 0;
					}
					if (! area.getQID().equals(prevQID)) {
						master.addQID(area.getQID(), area.getType());
						itemIndex = 0;
						columnIndex++;
					}
					List<FormArea> areaListByQID = getAreaListByQID(master, area.getQID());

					areaListByPageIndex.add(area);
					areaListByQID.add(area);

					master.getFormAreaList().add(area);
					master.putFormArea(area.getID(), area);

					master.setAreaIndexInPage(area.getQID(), areaIndexInPage);
					area.setIndex(columnIndex, itemIndex, areaIndexInPage);

					itemIndex++;
					areaIndexInPage++;
					prevQID = area.getQID();
					prevPage = area.getPage();

				}
			}
			Logger.getLogger("master").info(master.getRelativePath());
			return master;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			throw new InvalidPageMasterException(metadata.getMasterFile());
		} catch (TransformerException e) {
			throw new InvalidPageMasterException(metadata.getMasterFile());
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new NoConfigFilePageMasterException(metadata.getMasterFile());
		}
	}

	static ByteArrayInputStream hotfixXFormsNSURItoSVGNSURI(InputStream sqmInputStream) throws UnsupportedEncodingException, IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream(4096);

		PrintWriter writer = new PrintWriter(new OutputStreamWriter(b, "UTF-8"));
		LineNumberReader reader = new LineNumberReader(new InputStreamReader(sqmInputStream, "UTF-8"));
		String line = null;

		while ((line = reader.readLine()) != null) {
			String pattern = "xmlns:svg=\"" + SQSNamespaces.XFORMS_URI + "\"";
			int c = line.indexOf(pattern);

			if (0 <= c) {
				String replace = "xmlns:svg=\"" + SQSNamespaces.SVG_URI + "\"";
				writer.print(line.substring(0, c));
				writer.print(replace);
				writer.println(line.substring(c + pattern.length()));

				if (DEBUG) {
					System.out.print(line.substring(0, c));
					System.out.print(replace);
					System.out.println(line.substring(c + pattern.length()));
				}

			} else {
				if (DEBUG) {
					System.out.println(line);
				}
				writer.println(line);
			}
		}
		writer.close();
		reader.close();

		ByteArrayInputStream in = new ByteArrayInputStream(b.toByteArray());
		return in;
	}

}
