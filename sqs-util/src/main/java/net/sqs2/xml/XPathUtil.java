/*
 * 

 XPathUtil.java

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

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * XPath Utilities
 * 
 * @author hiroya
 */
public class XPathUtil {

	/*
	 * get String value by xpath.
	 * 
	 * @param currentNode
	 * 
	 * @param xpathString
	 * 
	 * @return stringValue return null when xpath is invalid.
	 */
	public static String getStringValue(Node currentNode, String xpathString) {
		try {
			Node target = XPathAPI.selectSingleNode(currentNode, xpathString, currentNode.getOwnerDocument());
			if (target == null) {
				return null;
			}

			if (target.getNodeType() == Node.TEXT_NODE) {
				return target.getNodeValue();
			} else if (target.getNodeType() == Node.ATTRIBUTE_NODE) {
				return ((Attr) target).getValue();
			} else {
				Logger.getLogger("dom").severe("ERROR Type:unknown" + target.getNodeType());
				Logger.getLogger("dom").severe("ERROR Node:" + currentNode);
				Logger.getLogger("dom").severe("ERROR XPath:" + xpathString);
				return null;
			}
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * get Integer value by xpath.
	 * 
	 * @param currentNode
	 * 
	 * @param xpathString
	 * 
	 * @return Integer Value return null when xpath is invalid.
	 * 
	 * @throws NumberFormatException
	 */
	public static Integer getIntegerValue(Element currentNode, String xpathString) throws NumberFormatException {
		return Integer.valueOf(getIntValue(currentNode, xpathString));
	}

	/*
	 * get Double value by xpath.
	 * 
	 * @param currentNode
	 * 
	 * @param xpathString
	 * 
	 * @return Double value return null when xpath is invalid.
	 * 
	 * @throws NumberFormatException
	 */
	public static Double getDoubleValue(Element currentNode, String xpathString) throws NumberFormatException {
		String value = getStringValue(currentNode, xpathString);
		if (value == null) {
			throw new NumberFormatException();
		}
		return new Double(Double.parseDouble(value));
	}

	/*
	 * get int value by xpath.
	 * 
	 * @param currentNode
	 * 
	 * @param xpathString
	 * 
	 * @return Double value return null when xpath is invalid.
	 * 
	 * @throws NumberFormatException
	 */
	public static int getIntValue(Element currentNode, String xpathString) throws NumberFormatException {
		return Integer.parseInt(getStringValue(currentNode, xpathString));
	}

	/**
	 * set value by xpath
	 * 
	 * @param currentNode
	 * @param xpathString
	 * @param value
	 */
	public static void setValue(Node currentNode, String xpathString, String value) {
		try {
			Document document = currentNode.getOwnerDocument();
			Node target = XPathAPI.selectSingleNode(currentNode, xpathString, document);
			if (target == null) {
				setValueCore(currentNode, xpathString, value, document);
				return;
			}

			if (target.getNodeType() == Node.TEXT_NODE) {
				target.setNodeValue(value);
			} else if (target.getNodeType() == Node.ATTRIBUTE_NODE) {
				((Attr) target).setValue(value);
			} else {
				System.err.println("ERROR Type:unknown" + target.getNodeType());
				System.err.println("ERROR Node:" + currentNode);
				System.err.println("ERROR XPath:" + xpathString);
			}
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	private static void setValueCore(Node currentNode, String xpathString, String value, Document document) throws TransformerException {
		if (xpathString.endsWith("text()")) {
			if ("text()".length() < xpathString.length()) {
				String parentXpathString = xpathString
						.substring(0, xpathString.length() - "/text()".length());
				Node targetParent = XPathAPI.selectSingleNode(currentNode, parentXpathString, document);
				if (targetParent.getNodeType() == Node.ELEMENT_NODE) {
					targetParent.appendChild(document.createTextNode(value));
				}
			} else {
				currentNode.appendChild(document.createTextNode(value));
			}
		} else {
			System.err.println("ERROR Node:" + currentNode);
			System.err.println("ERROR XPath:" + xpathString);
		}
	}

	/**
	 * set attribute value by xpath. when specified attribute exists, overwrite
	 * the attribute value. no matching attributes, create the attribute and set
	 * the value.
	 * 
	 * @param currentNode
	 * @param xpathString
	 * @param attributeNamespaceURI
	 * @param attributeNamespacePrefix
	 * @param attributeName
	 * @param attributeValue
	 */
	public static void setAttributeValue(Element currentNode, String xpathString, String attributeNamespaceURI, String attributeNamespacePrefix, String attributeName, String attributeValue) {
		try {
			Document document = currentNode.getOwnerDocument();
			Node target = XPathAPI.selectSingleNode(currentNode, xpathString, document);
			if (target == null) {
				System.err.println("ERROR Node:" + currentNode);
				System.err.println("ERROR XPath:" + xpathString);
				return;
			}

			if (target.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = ((Element) target);
				Attr attr;
				if (elem.hasAttributeNS(attributeNamespaceURI, attributeName)) {
					attr = elem.getAttributeNodeNS(attributeNamespaceURI, attributeName);
					attr.setNodeValue(attributeValue);
					attr.setPrefix(attributeNamespacePrefix);
					// elem.setAttributeNS(uri, name, value);
				} else {
					attr = document.createAttributeNS(attributeNamespaceURI, attributeName);
					attr.setNodeValue(attributeValue);
					attr.setPrefix(attributeNamespacePrefix);
					elem.setAttributeNodeNS(attr);
					// elem.setAttributeNS(uri, name, value);
				}
			} else {
				System.err.println("ERROR Type:unknown" + target.getNodeType());
				System.err.println("ERROR Node:" + currentNode);
				System.err.println("ERROR XPath:" + xpathString);
				return;
			}
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
