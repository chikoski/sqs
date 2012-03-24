/*

 SQSTreeCellRenderer.java
 
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
 
 Created on 2004/08/08

 */
package net.sqs2.editor.base.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import net.sqs2.editor.base.modules.resource.EditorResource;

import org.w3c.dom.Node;

/**
 * @author hiroya
 * 
 */
public class SourceEditorTreeCellRenderer extends DefaultTreeCellRenderer {
	public static final long serialVersionUID = 0;
	static final Color SELECTED_BGCOLOR = new Color(40, 40, 200);
	static final Color HIARACHICAL_SELECTED_BGCOLOR = new Color(230, 230, 255);
	static final Color SELECTED_COLOR = new Color(180, 180, 180);
	static final Color HIARACHICAL_SELECTED_COLOR = new Color(0, 0, 0);
	static final Color NODENAME_COLOR = new Color(130, 130, 255);
	boolean hiarachicalSelected = false;
	transient EditorResourceFactory factory;

	public SourceEditorTreeCellRenderer(EditorResourceFactory factory) {
		this.factory = factory;
		this.setBorderSelectionColor(Color.green);
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		Node node = (Node) value;
		String localName = node.getLocalName();
		String uri = node.getNamespaceURI();
		String prefix = node.getPrefix();
		EditorResource resource = this.factory.getEditorResource(localName, uri);
		String name = null;

		this.setTextSelectionColor(NODENAME_COLOR);
		this.setTextNonSelectionColor(NODENAME_COLOR);

		if (resource == null) {
			if (prefix != null) {
				name = prefix + ":" + localName;
			} else {
				name = localName;
			}
		} else {
			if (expanded || leaf) {
				name = resource.getName(node, expanded, leaf);
			} else {
				name = resource.getDigestText(node);
				this.setTextSelectionColor(Color.GRAY);
				this.setTextNonSelectionColor(Color.GRAY);
			}
		}

		boolean isDecendant = ((NodeTreeSelectionModel) tree.getSelectionModel()).isHierachicalSelected(tree
				.getPathForRow(row));

		if (Node.TEXT_NODE == node.getNodeType()) {
			this.setTextSelectionColor(Color.black);
			this.setTextNonSelectionColor(Color.black);
			if (isDecendant) {
				this.hiarachicalSelected = true;
				this.setBackgroundSelectionColor(HIARACHICAL_SELECTED_BGCOLOR);
				this.setTextSelectionColor(HIARACHICAL_SELECTED_COLOR);
				super.getTreeCellRendererComponent(tree, name, true, expanded, leaf, row, hasFocus);
			} else {
				this.hiarachicalSelected = false;
				this.setBackgroundSelectionColor(SELECTED_BGCOLOR);
				this.setTextSelectionColor(SELECTED_COLOR);
				super.getTreeCellRendererComponent(tree, name, sel, expanded, leaf, row, hasFocus);
			}
			if (resource != null) {
				setIcon(resource.icon);
			}
		} else {
			if (isDecendant) {
				this.hiarachicalSelected = true;
				this.setBackgroundSelectionColor(HIARACHICAL_SELECTED_BGCOLOR);
				super.getTreeCellRendererComponent(tree, name, true, expanded, leaf, row, hasFocus);
			} else {
				this.hiarachicalSelected = false;
				this.setBackgroundSelectionColor(SELECTED_BGCOLOR);
				this.setTextSelectionColor(SELECTED_COLOR);
				super.getTreeCellRendererComponent(tree, name, sel, expanded, leaf, row, hasFocus);
			}
			if (resource != null) {
				setIcon(resource.icon);
			}
		}
		return this;
	}

	public Icon getIcon(String localName, String uri) {
		EditorResource resource = factory.getEditorResource(localName, uri);
		if (resource != null) {
			return resource.icon;
		}
		return null;
	}

	public void paint(Graphics g) {
		if (this.selected) {
			g.setColor(SELECTED_BGCOLOR);
			g.fillRect(0, 0, getIcon().getIconWidth() + 4, getIcon().getIconHeight());
		}
		if (this.hiarachicalSelected) {
			g.setColor(HIARACHICAL_SELECTED_BGCOLOR);
			g.fillRect(0, 0, getIcon().getIconWidth() + 4, getIcon().getIconHeight());
		}
		super.paint(g);
	}
}
