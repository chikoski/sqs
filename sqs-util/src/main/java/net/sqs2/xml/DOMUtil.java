/*
 * 

 DOMUtil.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package net.sqs2.xml;

import java.util.logging.Logger;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * sqs-omr/src/main/java/net/sqs2 DOM(Document Object Model) Utilities
 * 
 * @author hiroya
 */
public class DOMUtil {

	/**
	 * get a descendant element by array of {nsURI, localName}. ex. String
	 * XHTML2_NSURI = "http://www.w3.org/2002/06/xhtml2"; String[][]
	 * PATH_TO_TITLE = new String[][]{{XHTML2_NSURI, "html"},{XHTML2_NSURI,
	 * "head"},{XHTML2_NSURI, "title"}}}; Element targetElement =
	 * getElement(currentNode, PATH_TO_TITLE);
	 * System.err.println(targetElement.getTextContent());
	 * 
	 * @param currentNode
	 *            current node
	 * @param args
	 *            array of {nsURI, localName}.
	 * @return descendantElement
	 */
	public static Element getDescendantElement(Element currentNode, String[][] args) {
		Element current = currentNode;
		for (int i = 0; i < args.length; i++) {
			if (current == null) {
				Logger.getLogger("dom").severe("ERROR:" + currentNode);
				Logger.getLogger("dom").severe("i:" + i);
				return null;
			}
			String nsURI = args[i][0];
			String localName = args[i][1];
			NodeList elementList = getChildElementList(current, nsURI, localName);
			current = (Element) elementList.item(0);
		}
		return current;
	}

	/*
	 * get a child element by nsURI and localName. ex. String XHTML2_NSURI =
	 * "http://www.w3.org/2002/06/xhtml2"; Element titleElement =
	 * getChildElement(headElement, XHTML2_NSURI, "title");
	 * System.err.println(titleElement.getTextContent());
	 * 
	 * @param currentNode
	 * 
	 * @param nsURI
	 * 
	 * @param localName
	 * 
	 * @return childElement
	 */
	public static Element getChildElement(Element currentNode, String nsURI, String localName) {
		return (Element) getChildElementList(currentNode, nsURI, localName).item(0);
	}

	/*
	 * get list of elements(childlen nodes) by nsURI and localName. ex. String
	 * XHTML2_NSURI = "http://www.w3.org/2002/06/xhtml2"; NodeList metaNodeList
	 * = getChildElementList(headElement, XHTML2_NSURI, "meta");
	 * 
	 * @param currentNode
	 * 
	 * @param nsURI
	 * 
	 * @param localName
	 * 
	 * @return childElement
	 */
	public static NodeList getChildElementList(Element node, String nsURI, String localName) {
		return node.getElementsByTagNameNS(nsURI, localName);
	}

}
