/*

 AbstractNodeTreePane.java
 
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

import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import net.sqs2.exsed.source.DOMTreeSource;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author hiroya
 * 
 */
public abstract class AbstractNodeTreePane extends JTree {
	private static final long serialVersionUID = 1L;
	DOMTreeSource source;
	JTree self;
	TreeModel model;
	NodeTreeSelectionModel selectionModel;
	JPopupMenu popup;
	EditorResourceFactory editorResourceFactory = null;

	public AbstractNodeTreePane(SourceEditorMediator mediator, DOMTreeSource source) {
		this.source = source;
		this.self = this;
		this.editorResourceFactory = mediator.getEditorResourceFactory();
		this.model = ((DOMTreeSource) source).getTreeModel();

		this.setModel(model);
		this.setEditable(false);
		this.setDragEnabled(true);
		this.setRootVisible(false);
		this.setShowsRootHandles(false);
		this.setExpandsSelectedPaths(true);
		this.setToggleClickCount(2);
		this.setDragEnabled(false);
		this.selectionModel = new NodeTreeSelectionModel(this);
		this.setSelectionModel(selectionModel);
		this.setCellRenderer(new SourceEditorTreeCellRenderer(editorResourceFactory));
	}

	TreePath getEditablePath(TreePath path) {
		if (path == null) {
			return null;
		}
		TreePath currentPath = path;
		while (true) {
			Node node = (Node) currentPath.getLastPathComponent();
			String uri = node.getNamespaceURI();
			String localName = node.getLocalName();

			if (editorResourceFactory.isEditable(localName, uri)) {
				if (node instanceof Document) {
					return null;
				}
				return currentPath;
			}
			if (currentPath.getParentPath() == null) {
				return null;
			}
			currentPath = currentPath.getParentPath();
		}
	}

	public synchronized void appendChildNode(Node parentNode, Node insertedNode) {
		try {
			parentNode.appendChild(insertedNode);
		} catch (DOMException ex) {
			if (ex.code == DOMException.WRONG_DOCUMENT_ERR) {
				parentNode.appendChild(NodeFunctions.createNode(source, insertedNode));
			}
		}
	}

	public synchronized void insertBefore(Node currentNode, Node parentNode, Node insertedNode) {
		try {
			parentNode.insertBefore(insertedNode, currentNode);
		} catch (DOMException ex) {
			if (ex.code == DOMException.WRONG_DOCUMENT_ERR) {
				parentNode.insertBefore(NodeFunctions.createNode(source, insertedNode), currentNode);
			}
		}
	}

	public Icon getIcon(String localName, String uri) {
		return getTreeCellRenderer().getIcon(localName, uri);
	}

	public SourceEditorTreeCellRenderer getTreeCellRenderer() {
		return (SourceEditorTreeCellRenderer) getCellRenderer();
	}

	public abstract void setupRenderer(SourceEditorTreeCellRenderer renderer);

	public DOMTreeSource getSource() {
		return source;
	}

	public void expandDecendant(TreePath path) {
		expandDecendant(path.getLastPathComponent(), path);
	}

	public void expandDecendant(Object nodeObject) {
		expandDecendant(nodeObject, new TreePath(nodeObject));
	}

	void expandDecendant(Object nodeObject, TreePath path) {
		if (model.isLeaf(nodeObject)) {
			return;
		} else {
			this.expandPath(path);
		}
		for (int i = 0; i < model.getChildCount(nodeObject); i++) {
			Object child = model.getChild(nodeObject, i);
			expandDecendant(child, path.pathByAddingChild(child));
		}
	}
}
