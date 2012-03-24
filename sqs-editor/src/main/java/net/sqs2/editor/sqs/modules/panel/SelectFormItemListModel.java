/*

 SelectFormItemListModel.java
 
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
 
 Created on 2004/08/01

 */
package net.sqs2.editor.sqs.modules.panel;

import net.sqs2.editor.base.swing.SourceEditorMediator;
import net.sqs2.exsed.source.DOMTreeSource;
import net.sqs2.xml.XPathUtil;
import net.sqs2.xmlns.SQSNamespaces;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author hiroya
 * 
 */
public class SelectFormItemListModel {
	SourceEditorMediator mediator;
	DOMTreeSource source;
	Element elem;

	// SelectFormItemListModel model;
	// JScrollPane itemListFormScrollPane;

	public SelectFormItemListModel(SourceEditorMediator mediator, DOMTreeSource source, Element elem) {
		// this.mediator = mediator;
		this.source = source;
		this.elem = elem;
	}

	public SelectFormItemListModel(SourceEditorMediator mediator, DOMTreeSource source, Element elem, int n) {
		this(mediator, source, elem);
		for (int i = 0; i < n; i++) {
			// Element itemElem = createItemElement("", "", 1);
		}
	}

	public void renumber() {
		NodeList list = getItemNodeList();
		for (int i = 0; i < list.getLength(); i++) {
			Element item = (Element) list.item(i);
			XPathUtil.setValue(item, "xforms:value/text()", Integer.valueOf(i + 1).toString());
		}
	}

	public int size() {
		return getItemNodeList().getLength();
	}

	private Element createItemElement(String label, String value, int colspan) {
		Element labelElem = createLabelElement(label);
		Element valueElem = createValueElement(value);
		Element itemElem = createItemElement(colspan, labelElem, valueElem);
		return itemElem;
	}

	/**
	 * @param colspan
	 * @param labelElem
	 * @param valueElem
	 * @return
	 */
	private Element createItemElement(int colspan, Element labelElem, Element valueElem) {
		Element itemElem = source.getDocument().createElementNS(SQSNamespaces.XFORMS_URI, "item");
		itemElem.setPrefix(SQSNamespaces.XFORMS_PREFIX);
		itemElem.setAttributeNS(SQSNamespaces.SQS2004_URI, "colspan", Integer.valueOf(colspan).toString());
		Attr attr = itemElem.getAttributeNodeNS(SQSNamespaces.SQS2004_URI, "colspan");
		attr.setPrefix("sqs");
		itemElem.appendChild(labelElem);
		itemElem.appendChild(valueElem);
		return itemElem;
	}

	/**
	 * @param value
	 * @return
	 */
	private Element createValueElement(String value) {
		Element valueElem = source.getDocument().createElementNS(SQSNamespaces.XFORMS_URI, "value");
		valueElem.setPrefix(SQSNamespaces.XFORMS_PREFIX);
		valueElem.appendChild(source.getDocument().createTextNode(value));
		return valueElem;
	}

	/**
	 * @param label
	 * @return
	 */
	private Element createLabelElement(String label) {
		Element labelElem = source.getDocument().createElementNS(SQSNamespaces.XFORMS_URI, "label");
		labelElem.setPrefix(SQSNamespaces.XFORMS_PREFIX);
		labelElem.appendChild(source.getDocument().createTextNode(label));
		return labelElem;
	}

	public void remove(int index) {
		if (source.isReadOnly()) {
			return;
		}
		elem.removeChild(getItemNode(index));
		renumber();
	}

	public void add(String label, String value, int colspan) {
		if (source.isReadOnly()) {
			return;
		}
		Element itemElem = createItemElement(label, value, colspan);
		elem.appendChild(itemElem);
		renumber();
	}

	public void insert(int index, String label, String value, int colspan) {
		if (source.isReadOnly()) {
			return;
		}
		Element itemElem = createItemElement(label, value, colspan);
		elem.insertBefore(itemElem, getItemNode(index));
		renumber();
	}

	public boolean updateNodeValue(int index, String label, String value) {
		return updateNodeValue(index, label, value, 1);
	}

	public boolean updateNodeValue(int index, String label, String value, int colspan) {
		if (source.isReadOnly()) {
			return false;
		}
		// Document document = source.getDocument();
		Element item = (Element) getItemNode(index);
		String prevLabel = XPathUtil.getStringValue(item, "xforms:label/text()");
		String prevValue = XPathUtil.getStringValue(item, "xforms:value/text()");
		int prevColspan = 1;
		try {
			prevColspan = XPathUtil.getIntegerValue(item, "@sqs:colspan");
		} catch (NumberFormatException ignore) {
		}

		if (!label.equals(prevLabel) || !value.equals(prevValue) || colspan != prevColspan) {
			XPathUtil.setValue(item, "xforms:label/text()", label);
			XPathUtil.setValue(item, "xforms:value/text()", value);
			XPathUtil.setAttributeValue(item, ".", SQSNamespaces.SQS2004_URI, "sqs", "colspan", Integer
					.valueOf(colspan).toString());
			return true;
		} else {
			return false;
		}
	}

	public String getLabel(int index) {
		Element item = (Element) getItemNode(index);
		return XPathUtil.getStringValue(item, "xforms:label/text()");
	}

	public String getValue(int index) {
		Element item = (Element) getItemNode(index);
		return XPathUtil.getStringValue(item, "xforms:value/text()");
	}

	public int getColspan(int index) {
		Element item = (Element) getItemNode(index);
		try {
			return XPathUtil.getIntegerValue(item, "@sqs:colspan");
		} catch (Exception ignore) {
			return 1;
		}
	}

	public NodeList getItemNodeList() {
		return (NodeList) elem.getElementsByTagNameNS(SQSNamespaces.XFORMS_URI, "item");
	}

	public Element getItemNode(int index) {
		return (Element) getItemNodeList().item(index);
	}
}
