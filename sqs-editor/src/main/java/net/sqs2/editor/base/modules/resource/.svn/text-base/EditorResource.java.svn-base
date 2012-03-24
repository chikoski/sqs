/*

 EditorResource.java
 
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
 
 Created on 2004/08/15

 */
package net.sqs2.editor.base.modules.resource;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.border.Border;

import net.sqs2.editor.base.modules.AbstractNodeEditor;
import net.sqs2.editor.sqs.modules.MetaEditor;
import net.sqs2.editor.sqs.modules.panel.MetaDescriptionPanel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author hiroya
 * 
 */
public class EditorResource {
	public Class<? extends AbstractNodeEditor> nodeEditorClass;

	public Icon icon;

	public String title;

	public Color bgcolor;

	public int height;

	public boolean isSelectable;

	public boolean isDigestTextMode;

	public Border border;

	public EditorResource(Class<? extends AbstractNodeEditor> clazz, Icon icon, String title, Color bgcolor,
			int height, boolean isSelectable, boolean isDigestTextMode, Border border) {
		this.nodeEditorClass = clazz;
		this.icon = icon;
		this.title = title;
		this.bgcolor = bgcolor;
		this.height = height;
		this.isSelectable = isSelectable;
		this.isDigestTextMode = isDigestTextMode;
		this.border = border;
	}

	public String getName(Node node) {
		return getName(node, false, false);
	}

	public String getName(Node node, boolean isExpanded, boolean isLeaf) {
		if (node.getNodeType() == Node.TEXT_NODE) {// this.clazz.equals(ContentTextEditor.class)
			return ((Node) node).getNodeValue();

		} else if (nodeEditorClass.equals(MetaEditor.class)) {
			/*
			 * return ((Node) node).getLocalName() + "[@name='" + ((Node)
			 * node).getAttributes().getNamedItem("name") .getNodeValue() +
			 * "']";
			 */
			return MetaDescriptionPanel.getDescriptionLabel(((Node) node).getAttributes()
					.getNamedItem("name").getNodeValue())
					+ " = " + ((Node) node).getAttributes().getNamedItem("content").getNodeValue();
		} else if (0 < title.length()) {
			if (isExpanded || isLeaf) {
				return title;
			} else {
				return null;
			}
		} else if (node.getPrefix() == null) {
			return ((Node) node).getLocalName();
		} else {
			return node.getPrefix() + ":" + ((Node) node).getLocalName();
		}
	}

	public String getDigestText(Node node) {
		int type = node.getNodeType();
		if (type == Node.ELEMENT_NODE) {
			NodeList list = node.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node child = list.item(i);
				String value = getDigestText(child);
				if (value != null) {
					return value;
				}
			}
		} else if (type == Node.TEXT_NODE) {
			String trim = node.getNodeValue().trim();
			if (0 < trim.length()) {
				return trim;
			}
		}
		return null;
	}
}
