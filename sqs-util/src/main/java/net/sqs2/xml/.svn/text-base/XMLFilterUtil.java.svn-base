/*

 XMLFilterUtil.java

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

 Created on 2004/10/18

 */
package net.sqs2.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

public class XMLFilterUtil {

	/*
	 * class SAXFilter extends DefaultHandler { public void startDocument() { }
	 * 
	 * public void endDocument() { }
	 * 
	 * public void startElement(String namespaceURI, String localName, String
	 * qName, Attributes atts) { StringBuffer attstr = new StringBuffer(); for
	 * (int i = 0; i < atts.getLength(); i++) { String aQName =
	 * atts.getQName(i); String aValue = atts.getValue(i);
	 * attstr.append(" ").append
	 * (aQName).append("=\"").append(escape(aValue)).append("\""); }
	 * writer.print("<" + qName + attstr + ">"); }
	 * 
	 * private String escape(String src) { return
	 * StringUtil.replaceAll(StringUtil.replaceAll(StringUtil
	 * .replaceAll(StringUtil.replaceAll(src, "&", "&amp;"), "\"", "&quot;"),
	 * ">", "&gt;"), "<", "&lt;"); }
	 * 
	 * public void endElement(String namespaceURI, String localName, String
	 * qName) { writer.print("</" + qName + ">"); }
	 * 
	 * public void characters(char[] ch, int start, int length) { for (int i =
	 * 0; i < length; i++) { char c = ch[start + i]; switch (c) { case '"':
	 * writer.print("&quot;"); break; case '<': writer.print("&lt;"); break;
	 * case '>': writer.print("&gt;"); break; case '&': writer.print("&amp;");
	 * break; default: writer.print(c); } } }
	 * 
	 * public void error(SAXParseException ex) { //
	 * System.err.println(ex.getPublicId
	 * ()+":"+ex.getSystemId()+":"+ex.getLocalizedMessage());
	 * ex.printStackTrace(); }
	 * 
	 * public void fatalError(SAXParseException ex) {
	 * System.err.println(ex.getPublicId() + ":" + ex.getSystemId() + ":" +
	 * ex.getLocalizedMessage()); ex.printStackTrace(); }
	 * 
	 * public void warning(SAXParseException ex) { ex.printStackTrace(); } }
	 */

	public static void overwrite(File file, String[][] args) {
		try {
			// build DOM
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);

			// modify DOM
			/*
			 * Node root = doc.getElementsByTagName(args[1]).item(0); Node child
			 * = ((Element) root).getElementsByTagName(args[2]).item(0);
			 * child.replaceChild(doc.createTextNode(args[3]),
			 * child.getFirstChild());
			 */

			for (String[] keyValue : args) {
				String xpath = keyValue[0];
				String value = keyValue[1];
				NodeIterator nl = XPathAPI.selectNodeIterator(doc, xpath);
				Node node = null;

				while ((node = nl.nextNode()) != null) {
					switch (node.getNodeType()) {
					case Node.ELEMENT_NODE:
						node.replaceChild(doc.createTextNode(value), node.getFirstChild());
						break;
					case Node.ATTRIBUTE_NODE:
						// node.setTextContent(value);
						node.setNodeValue(value);
						break;
					default:
						break;
					}
				}

				// output DOM
				BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
				StreamResult streamResult = new StreamResult(fileWriter);
				DOMSource domSource = new DOMSource(doc);
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.transform(domSource, streamResult);
				fileWriter.close();
				// System.out.println(stringWriter.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void overwrite(File file, String xpath, String value) {
		overwrite(file, new String[][] { { xpath, value } });
	}

	public static String[] selectValues(File file, String[] xpathArray) throws IOException {
		String ret[] = new String[xpathArray.length];
		try {

			// build DOM
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);

			// modify DOM
			/*
			 * Node root = doc.getElementsByTagName(args[1]).item(0); Node child
			 * = ((Element) root).getElementsByTagName(args[2]).item(0);
			 * child.replaceChild(doc.createTextNode(args[3]),
			 * child.getFirstChild());
			 */

			for (int index = 0; index < xpathArray.length; index++) {
				String xpath = xpathArray[index];
				NodeIterator nl = XPathAPI.selectNodeIterator(doc, xpath);
				Node node = null;

				while ((node = nl.nextNode()) != null) {
					switch (node.getNodeType()) {
					case Node.ELEMENT_NODE:
						// ret[index] = ((Element)node).getTextContent();
						ret[index] = ((Element) node).getNodeValue();
						break;
					case Node.ATTRIBUTE_NODE:
						ret[index] = node.getNodeValue();
						// node.setNodeValue(value);
						break;
					default:
						break;
					}
				}

				// output DOM
				BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
				StreamResult streamResult = new StreamResult(fileWriter);
				DOMSource domSource = new DOMSource(doc);
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.transform(domSource, streamResult);
				fileWriter.close();
				// System.out.println(stringWriter.toString());
			}

		} catch (IOException e) {
			throw e;
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static String select(File file, String xpath) {
		String ret = null;
		try {

			// build DOM
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);

			// modify DOM
			/*
			 * Node root = doc.getElementsByTagName(args[1]).item(0); Node child
			 * = ((Element) root).getElementsByTagName(args[2]).item(0);
			 * child.replaceChild(doc.createTextNode(args[3]),
			 * child.getFirstChild());
			 */

			NodeIterator nl = XPathAPI.selectNodeIterator(doc, xpath);
			Node node = null;

			while ((node = nl.nextNode()) != null) {
				switch (node.getNodeType()) {
				case Node.ELEMENT_NODE:
					// ret = node.getTextContent();
					ret = node.getNodeValue();
					break;
				case Node.ATTRIBUTE_NODE:
					ret = node.getNodeValue();
					// node.setNodeValue(value);
					break;
				default:
					break;
				}
			}

			// output DOM
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
			StreamResult streamResult = new StreamResult(fileWriter);
			DOMSource domSource = new DOMSource(doc);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(domSource, streamResult);
			fileWriter.close();
			// System.out.println(stringWriter.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
}
