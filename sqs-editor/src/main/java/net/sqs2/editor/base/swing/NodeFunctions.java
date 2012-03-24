/*

 NodeFunctions.java
 
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
 
 Created on 2004/10/19

 */
package net.sqs2.editor.base.swing;

import net.sqs2.exsed.source.DOMTreeSource;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * @author hiroya
 * 
 */
class NodeFunctions {
	static Node createNode(DOMTreeSource source, Node node) {
		Document document = source.getDocument();
		if (node instanceof Element) {
			Element ret = document.createElementNS(node.getNamespaceURI(), node.getLocalName());
			setAttributeNode(source, node, ret);
			appendChild(source, node, ret);
			return ret;
		} else if (node instanceof Attr) {
			Attr ret = document.createAttributeNS(node.getNamespaceURI(), node.getLocalName());
			ret.setNodeValue(node.getNodeValue());
			return ret;
		} else if (node instanceof Text) {
			return document.createTextNode(node.getNodeValue());
		} else {
			return null;
		}
	}

	static void appendChild(DOMTreeSource source, Node node, Element ret) {
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			ret.appendChild(createNode(source, list.item(i)));
		}
	}

	static void setAttributeNode(DOMTreeSource source, Node node, Element ret) {
		NamedNodeMap attList = node.getAttributes();
		for (int i = 0; i < attList.getLength(); i++) {
			ret.setAttributeNodeNS((Attr) createNode(source, attList.item(i)));
		}
	}

}
