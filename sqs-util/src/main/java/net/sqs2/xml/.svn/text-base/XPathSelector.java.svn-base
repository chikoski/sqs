/*

 XPathExecutor.java
 
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

package net.sqs2.xml;

import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.sqs2.xmlns.SQSNamespaces;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathSelector {
	XPath xpath;

	public XPathSelector(Document document, String defaultPrefix) {
		XPathFactory factory = XPathFactory.newInstance();
		this.xpath = factory.newXPath();
		this.xpath.setNamespaceContext(getNamespaceContext(document, defaultPrefix));
	}

	private NamespaceContext getNamespaceContext(Document doc, String defaultPrefix) {
		Map<String, String> nsMap = new HashMap<String, String>();
		Element root = doc.getDocumentElement();
		NamedNodeMap attrs = root.getAttributes();
		String xmlns = "xmlns";
		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			String[] name = attr.getNodeName().split(":");
			if (xmlns.equals(name[0]))
				nsMap.put(name.length == 1 ? defaultPrefix : name[1], attr.getNodeValue());
		}
		return new HotFixedNamespaceContextImpl(nsMap);
	}

	class NamespaceContextImpl implements NamespaceContext {
		protected Map<String, String> nsMap;

		public NamespaceContextImpl(Map<String, String> nsMap) {
			this.nsMap = nsMap;
		}

		public String getNamespaceURI(String prefix) {
			if (prefix == null) {
				throw new NullPointerException("Null prefix");
			}
			if (this.nsMap.containsKey(prefix)) {
				String uri = this.nsMap.get(prefix);
				return uri;
			}
			return XMLConstants.NULL_NS_URI;
		}

		public String getPrefix(String uri) {
			throw new UnsupportedOperationException();
		}

		public Iterator<?> getPrefixes(String namespaceURI) {
			throw new UnsupportedOperationException();
		}
	}

	class HotFixedNamespaceContextImpl extends NamespaceContextImpl {
		public HotFixedNamespaceContextImpl(Map<String, String> nsMap) {
			super(nsMap);
			nsMap.put("svg", SQSNamespaces.SVG_URI);
		}
	}

	public NodeList selectNodeList(Element elem, String path) throws XPathExpressionException {
		NodeList elementList = (NodeList) this.xpath.evaluate(path, elem, XPathConstants.NODESET);
		return elementList;
	}

	public Element selectSingleNode(Element elem, String path) throws XPathExpressionException {
		try {
			Element element = (Element) this.xpath.evaluate(path, elem, XPathConstants.NODE);
			return element;
		} catch (XPathExpressionException ex) {
			Logger.getLogger(getClass().getName()).severe("path:" + path+" elem= <" + elem.getPrefix() + ":" + elem.getLocalName() + "...> "
					+ elem.getNamespaceURI());
			throw ex;
		}
	}

	private String chooseNotNullValue(String a, String b) {
		if (a != null && !"".equals(a)) {
			return a;
		} else if (b != null && !"".equals(b)) {
			return b;
		} else {
			return null;
		}
	}

	public String selectAttribute(Element elem, String localName, String nsURI) {
		return chooseNotNullValue(elem.getAttributeNS(nsURI, localName), elem.getAttribute(localName));
	}

}
