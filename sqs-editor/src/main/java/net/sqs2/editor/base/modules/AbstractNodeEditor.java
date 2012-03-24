/*

 AbstractNodeEditor.java
 
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
 
 Created on 2004/08/03

 */
package net.sqs2.editor.base.modules;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPopupMenu;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

import net.sqs2.editor.base.modules.panel.AbstractNodeEditorPanel;
import net.sqs2.editor.base.modules.resource.EditorResource;
import net.sqs2.editor.base.swing.NodeTreePane;
import net.sqs2.editor.base.swing.SourceEditorMediator;
import net.sqs2.exsed.source.DOMTreeSource;
import net.sqs2.swing.IconTitledPane;
import net.sqs2.xml.XPathUtil;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author hiroya
 * 
 */
public abstract class AbstractNodeEditor extends IconTitledPane implements UpdateTarget {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SourceEditorMediator mediator;
	DOMTreeSource source;
	Node node;
	EditorResource resource;

	UpdateListener updateListener;
	int prefHeight = 0;

	public AbstractNodeEditor(SourceEditorMediator mediator, DOMTreeSource source, Node node,
			EditorResource resource) {
		initialize(resource.icon, resource.title, resource.border, createPopupMenu(mediator, source, node,
				resource));
		this.mediator = mediator;
		this.source = source;
		this.node = node;
		this.resource = resource;
		this.setBackground(resource.bgcolor);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		prefHeight = resource.icon.getIconHeight() + 20;// resource.editorHeight;
	}

	public JPopupMenu createPopupMenu(SourceEditorMediator mediator, DOMTreeSource source, Node node, EditorResource resource) {
		NodeTreePane treePane = mediator.getCurrentTreePane();
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			return treePane.createPopupMenu(true);
		} else {
			return null;
		}
	}

	public UndoManager getUndoManager() {
		return source.getUndoManager(node);
	}

	public void setUndoManager(UndoManager undoManager) {
		source.putUndoManager(node, undoManager);
	}

	public Document getDocument() {
		return source.getDocument(node);
	}

	public void setDocument(Document document) {
		source.putDocument(node, document);
	}

	/*
	 * private JMenuItem createPopupMenuItem(String label, String command){
	 * JMenuItem item = new JMenuItem(label); item.setActionCommand(command);
	 * //item.addActionListener(listener); return item; }
	 */

	public void addPreferredHeight(int height) {
		prefHeight += height;
	}

	public void add(AbstractNodeEditorPanel p) {
		prefHeight += p.getPreferredHeight() + 20;
		super.add(p);
	}

	/**
	 * @return Returns the mediator.
	 */
	public SourceEditorMediator getMediator() {
		return mediator;
	}

	/**
	 * @param mediator
	 *            The mediator to set.
	 */
	public void setMediator(SourceEditorMediator mediator) {
		this.mediator = mediator;
	}

	public void initSize() {
		setPreferredSize(new Dimension(200, prefHeight));
		// setSize(new Dimension(200, prefHeight));
		// if(getHeight() <= prefHeight){
		// }
		doLayout();
	}

	/**
	 * @return Returns the resource.
	 */
	public EditorResource getResource() {
		return resource;
	}

	/**
	 * @param resource
	 *            The resource to set.
	 */
	public void setResource(EditorResource resource) {
		this.resource = resource;
	}

	/**
	 * @param node
	 *            The node to set.
	 */
	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * @param source
	 *            The source to set.
	 */
	public void setSource(DOMTreeSource source) {
		this.source = source;
	}

	public int getPreferredHeight() {
		return prefHeight;
	}

	public DOMTreeSource getSource() {
		return source;
	}

	public Node getNode() {
		return node;
	}

	public void setDirty(boolean dirty) {
		source.setDirty(dirty);
	}

	public boolean updateNodeValue(String xpathString, Object value) {
		if (source.isReadOnly()) {
			return false;
		}
		XPathUtil.setValue(getNode(), xpathString, value.toString());
		mediator.getCurrentTreePane().updateUI();
		return true;
	}

	public void updateAttributeValue(String xpathString, String uri, String prefix, String name, Object value) {
		if (source.isReadOnly()) {
			return;
		}
		XPathUtil.setAttributeValue((Element) getNode(), xpathString, uri, prefix, name, value.toString());
	}

	public int intValueOf(String xpathString, int defaultValue) {
		try {
			return XPathUtil.getIntegerValue((Element) getNode(), xpathString);
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	public Double doubleValueOf(String xpathString) {
		try {
			return XPathUtil.getDoubleValue((Element) getNode(), xpathString);
		} catch (NullPointerException ex) {
			return null;
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	public Double doubleValueOf(String xpathString, Double defaultValue) {
		try {
			return XPathUtil.getDoubleValue((Element) getNode(), xpathString);
		} catch (NullPointerException ex) {
			return defaultValue;
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

	public String stringValueOf(String xpathString, String defaultValue) {
		try {
			return XPathUtil.getStringValue((Element) getNode(), xpathString);
		} catch (NullPointerException ex) {
			return defaultValue;
		}
	}

	public String stringValueOf(String xpathString) {
		try {
			return XPathUtil.getStringValue((Element) getNode(), xpathString);
		} catch (NullPointerException ex) {
			return null;
		}
	}

	public UpdateListener getUpdateListener() {
		if (updateListener == null) {
			updateListener = new UpdateListener(this);
		}
		return updateListener;
	}

	public boolean updateNodeValue(boolean isUpdated) {
		if (isUpdated) {
			return updateNodeValue();
		}
		return false;
	}

	public boolean updateNodeValue() {
		source.setDirty(true);
		mediator.fireSourceChanged();
		return true;
	}

	public boolean isSelectableNode() {
		return resource.isSelectable;
	}
}
