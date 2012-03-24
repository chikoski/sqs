/*

 FormAreaFactory.java
 
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
 
 Created on 2007/01/11

 */
package net.sqs2.omr.master.pdfbookmark;

import java.awt.Rectangle;

import javax.xml.transform.TransformerException;

import net.sqs2.omr.master.FormArea;
import net.sqs2.xml.DOMUtil;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PDFBookMarkFormAreaFactory {

	public static FormArea create(Element gElem) throws TransformerException {
		Element pageNode = ((Element) XPathAPI.selectSingleNode(gElem,
				FormMasterConstants.METADATA_PAGE_ELEMENT_REPRESENTATION));
		// Element pageNode = ((Element)XPathAPI.selectSingleNode(gElem,
		// "reader:page"));
		if (pageNode == null) {
			throw new RuntimeException(gElem.toString());
		}
		String pageNumber = pageNode.getAttribute(FormMasterConstants.NUMBER_ATTRIBUTE_REPRESENTATION);
		// pageNode.getAttributes().getNamedItemNS(SQSNamespaces.SQS2004READER_URI,
		// "number").getNodeValue();

		Element xformUCElem = ((Element) XPathAPI.selectSingleNode(gElem,
				FormMasterConstants.RECT_METADATA_STAR_ELEMENT_REPRESENTATION));

		FormArea area = new FormArea();

		area.setPage(Integer.valueOf(Integer.parseInt(pageNumber)));
		area.setType(xformUCElem.getLocalName().intern());
		area.setTypeCode(getTypeCode(area.getType()));
		area.setQID(xformUCElem.getAttribute("ref").intern());
		area.setID(gElem.getAttribute("id").intern());
		area.setRect(getRectangle(gElem));
		area
				.setLabel(XPathAPI.selectSingleNode(xformUCElem, "xforms:label").getTextContent().trim()
						.intern());
		area.setHint(XPathAPI.selectSingleNode(xformUCElem, "xforms:hint").getTextContent().trim().intern());
		area.setHints(getHints(gElem));
		if (area.getTypeCode() == FormArea.SELECT1 || area.getTypeCode() == FormArea.SELECT) {
			area.setItemLabel(getItemLabel(xformUCElem).intern());
			area.setItemValue(getItemValue(xformUCElem).intern());
		}
		return area;
	}

	private static int getTypeCode(String type) {
		if ("select1".equals(type)) {
			return FormArea.SELECT1;
		} else if ("select".equals(type)) {
			return FormArea.SELECT;
		} else if ("textarea".equals(type)) {
			return FormArea.TEXTAREA;
		} else if ("input".equals(type)) {
			return FormArea.INPUT;
		} else {
			return -1;
			// throw new RuntimeException("type:"+type);
		}
	}

	private static Rectangle getRectangle(Element gElem) throws TransformerException {
		Element rnode = (Element) XPathAPI.selectSingleNode(gElem,
				FormMasterConstants.RECT_ELEMENT_REPRESENTATION);
		int x = Integer.parseInt(rnode.getAttribute("x"));
		int y = Integer.parseInt(rnode.getAttribute("y"));
		int width = Integer.parseInt(rnode.getAttribute("width"));
		int height = Integer.parseInt(rnode.getAttribute("height"));
		return new Rectangle(x, y, width, height);
	}

	private static String[] getHints(Element gElem) throws TransformerException {
		NodeList list = XPathAPI.selectNodeList(gElem,
				FormMasterConstants.RECT_METADATA_STAR_HINT_ELEMENT_REPRESENTATION);
		String[] hints = new String[list.getLength()];
		for (int i = 0; i < hints.length; i++) {
			Element hElem = (Element) list.item(i);
			hints[i] = hElem.getTextContent().trim().intern();
		}
		return hints;
	}

	private static String getItemLabel(Element xformUCElem) {
		try {
			return DOMUtil.getDescendantElement(xformUCElem, FormMasterConstants.LABEL_PATH).getTextContent()
					.trim();
		} catch (AbstractMethodError ex) {
			return "";
		}
	}

	private static String getItemValue(Element xformUCElem) {
		try {
			return DOMUtil.getDescendantElement(xformUCElem, FormMasterConstants.VALUE_PATH).getTextContent()
					.trim();
		} catch (AbstractMethodError ex) {
			return "";
		}
	}
}
