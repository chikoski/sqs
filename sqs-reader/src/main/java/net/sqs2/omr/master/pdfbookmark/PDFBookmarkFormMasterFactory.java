/*

 PDFBookmarkFormMasterFactory.java

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

 Created on 2006/07/10

 */
package net.sqs2.omr.master.pdfbookmark;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.FormMasterException;
import net.sqs2.omr.master.FormMasterFactory;
import net.sqs2.omr.master.InvalidPageMasterException;
import net.sqs2.omr.master.PageMasterMetadata;
import net.sqs2.translator.TranslatorException;
import net.sqs2.util.FileResourceID;
import net.sqs2.xml.XMLUtil;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PDFBookmarkFormMasterFactory implements FormMasterFactory {

	private static FormMasterFactory singleton = new PDFBookmarkFormMasterFactory();
	
	public static FormMasterFactory getSingleton() {
		return singleton;
	}

	public FormMaster create(File sourceDirectoryRoot, String masterPath) throws FormMasterException {
		File masterFile = new File(sourceDirectoryRoot, masterPath);
		try {
			long lastModified;
			lastModified = masterFile.lastModified();
			FormMaster master = new FormMaster(new FileResourceID(masterPath, lastModified),
					new PageMasterMetadata(sourceDirectoryRoot, masterPath, "PDFBookmarkFormMaster", lastModified,
							null));
			Document document = createDocument(master, masterFile);
			if (document == null) {
				return null;
			}
			NodeList areaMasterElementList = document.getDocumentElement().getElementsByTagName(
					FormMasterConstants.SVG_G_ELEMENT_REPRESENTATION);

			int prevPage = -1;
			String prevQID = null;

			for (int pageIndex = 0; pageIndex < master.getNumPages(); pageIndex++) {
				master.addFormAreaList(new ArrayList<FormArea>());
			}

			int itemIndex = 0;
			int areaLength = areaMasterElementList.getLength();
			int areaIndexInPage = 0;
			int columnIndex = -1;

			for (int areaIndex = 0; areaIndex < areaLength; areaIndex++) {

				Element gElem = (Element) areaMasterElementList.item(areaIndex);
				FormArea area = PDFBookMarkFormAreaFactory.create(gElem);
				if (area == null) {
					throw new InvalidPageMasterException(masterFile);
				}

				if (prevPage != area.getPage()) {
					areaIndexInPage = 0;
				}
				if (! area.getQID().equals(prevQID)) {
					master.addQID(area.getQID(), area.getType());
					itemIndex = 0;
					columnIndex++;
				}

				List<FormArea> areaListByPageIndex = getAreaListByPageIndex(master, area.getPageIndex());
				List<FormArea> areaListByQID = getAreaListByQID(master, area.getQID());

				areaListByPageIndex.add(area);
				areaListByQID.add(area);

				master.getFormAreaList().add(area);
				master.putFormArea(area.getID(), area);

				master.setAreaIndexInPage(area.getQID(), areaIndexInPage);
				area.setIndex(columnIndex, itemIndex, areaIndexInPage);

				itemIndex++;
				prevQID = area.getQID();
				prevPage = area.getPage();
				areaIndexInPage++;
			}


			master.setDeskewGuideCenterPoints(getDeskewGuideCenterPoints(document, masterPath));
			master.setFooterLeftRectangle(new Rectangle(31, 788, 24, 11));
			master.setFooterRightRectangle(new Rectangle(554, 788, 24, 11));
			// master.setHeaderCheckArea(new Rectangle(308, 19, 76, 19));
			// master.setFooterCheckArea(new Rectangle(308, 801, 76, 19));
			master.setHeaderCheckArea(new Rectangle(89, 19, 20, 20));
			master.setFooterCheckArea(new Rectangle(84, 800, 20, 20));
			Logger.getLogger("master").log(Level.INFO, master.getRelativePath());
			return master;
		} catch (InvalidPageMasterException e) {
			throw new InvalidPageMasterException(masterFile);
		} catch (TransformerException e) {
			throw new InvalidPageMasterException(masterFile);
		}
	}

	private Document createDocument(FormMaster master, File pdfFile) throws FormMasterException {

		if (!pdfFile.getName().endsWith(".pdf")) {
			return null;
		}

		FileInputStream inputStream = null;
		try {
			PDFBookmarkToSQMTranslator translator = new PDFBookmarkToSQMTranslator();
			inputStream = new FileInputStream(pdfFile);
			Document doc = XMLUtil.createDocumentBuilder().parse(
					translator.translate(inputStream, pdfFile.toURI().toString(), null));
			master.setNumPages(translator.getNumPages());
			return doc;
		} catch (SAXException ex) {
			return null;
			// throw new PageMasterException(ex,
			// master.getFileResourceID().getRelativePath());
		} catch (IOException ex) {
			throw new InvalidPageMasterException(pdfFile);
		} catch (TranslatorException ex) {
			throw new InvalidPageMasterException(pdfFile);
		} catch (FactoryConfigurationError ex) {
			throw new RuntimeException(ex);
		} catch (ParserConfigurationException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				inputStream.close();
			} catch (Exception ignore) {
			}
		}
	}

	private ArrayList<FormArea> getAreaListByPageIndex(FormMaster master, int pageIndex) {
		return master.getFormAreaListByPageIndex(pageIndex);
	}

	private ArrayList<FormArea> getAreaListByQID(FormMaster master, String qid) {
		ArrayList<FormArea> areaList = master.getFormAreaList(qid);
		if (areaList == null) {
			areaList = new ArrayList<FormArea>();
			master.putFormAreaList(qid, areaList);
		}
		return areaList;
	}

	private Point[] getDeskewGuideCenterPoints(Document document, String path) throws InvalidPageMasterException {
		try {
			Element pageElem = (Element) XPathAPI.selectSingleNode(document.getDocumentElement(),
					FormMasterConstants.METADATA_PAGE_ELEMENT_REPRESENTATION);
			Point[] ret = new Point[4];
			for (int i = 0; i < 4; i++) {
				ret[i] = createPoint(pageElem, i + 1);
			}
			return ret;
		} catch (TransformerException ex) {
			ex.printStackTrace();
			throw new InvalidPageMasterException(null);
		}
	}

	private static Point createPoint(Element elem, int n) {
		return new Point(Integer.parseInt(elem.getAttribute(FormMasterConstants.X_ATTRIBUTE + n)), Integer
				.parseInt(elem.getAttribute(FormMasterConstants.Y_ATTRIBUTE + n)));
	}

}
