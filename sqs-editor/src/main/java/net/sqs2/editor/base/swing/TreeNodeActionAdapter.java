/*

 TreeNodePopupAdapter.java
 
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
 
 Created on 2004/08/10

 */
package net.sqs2.editor.base.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreePath;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class TreeNodeActionAdapter implements ActionListener {
	SourceEditorMediator mediator;
	boolean isClickedNode;
	
	TreeNodeActionAdapter(SourceEditorMediator mediator, boolean isClickedNode) {
		this.mediator = mediator;
		this.isClickedNode = isClickedNode;
	}

	public void actionPerformed(ActionEvent ev) {
		String command = ev.getActionCommand();
		TreePath target = getCurrentNodeTreePane().currentPopUpMenuNodePath;
		if (command.equals("d")) {
			cut();
		} else if (command.equals("c")) {
			copy();
		} else if (command.equals("b")) {
			insert(target, true);
		} else if (command.equals("i")) {
			insertWithin(target);
		} else if (command.equals("a")) {
			insert(target, false);
		} else if (command.equals("S")) {
			showAll(target);
		} else if (command.equals("H")) {
			hideAll(target);
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getCurrentNodeTreePane().updateUI();
			}
		});
	}
	
	private NodeTreePane getCurrentNodeTreePane(){ 
		return this.mediator.getCurrentTreePane();
	}

	private List<Node> getRecentCutNode() {
		return getCurrentNodeTreePane().mediator.getMenuBarMediator().getRecentCutNode();
	}

	private void setRecentCutNode(List<Node> list) {
		getCurrentNodeTreePane().mediator.getMenuBarMediator().setRecentCutNode(list);
	}

	private void insert(TreePath path, boolean isInsertBeforeMode) {
		if (getCurrentNodeTreePane().isReadOnly()) {
			return;
		}
		List<Node> recentCuttedNodeList = getRecentCutNode();
		if (recentCuttedNodeList == null || 0 == recentCuttedNodeList.size()) {
			return;
		}
		NodeTreePane tree = getCurrentNodeTreePane();
		TreePath parentPath = path.getParentPath();
		Node currentNode = (Node) path.getLastPathComponent();
		Node parentNode = (Node) parentPath.getLastPathComponent();
		boolean appendMode = false;
		if (!isInsertBeforeMode) {
			Node nextNode = currentNode.getNextSibling();
			if (nextNode == null) {
				appendMode = true;
			} else {
				currentNode = nextNode;
			}
		}
		insertNodeCore(recentCuttedNodeList, tree, parentPath, currentNode, parentNode, appendMode);
		updateCurrentTitle(tree);
		tree.setupEditorPane(null);
	}

	private void updateCurrentTitle(NodeTreePane tree) {
		tree.mediator.getSourceEditorTabbedPane().getCurrentEditingSource().setDirty(true);
		tree.mediator.getSourceEditorTabbedPane().updateCurrentTitle();
	}

	private void insertNodeCore(List<Node> recentCuttedNodeList, NodeTreePane tree, TreePath parentPath, Node currentNode, Node parentNode, boolean appendMode) {
		Document document = tree.mediator.getSourceEditorTabbedPane().getCurrentEditingSource().getDocument();
		// tree.getSelectionModel().clearSelection();
		TreePath[] paths = new TreePath[recentCuttedNodeList.size()];
		for (int i = 0; i < recentCuttedNodeList.size(); i++) {
			Node insertedNode = document.importNode((Node) recentCuttedNodeList.get(i), true);
			TreePath insertedPath = parentPath.pathByAddingChild(insertedNode);
			paths[i] = insertedPath;
			if (appendMode) {
				getCurrentNodeTreePane().appendChildNode(parentNode, insertedNode);
			} else {
				getCurrentNodeTreePane().insertBefore(currentNode, parentNode, insertedNode);
			}
			tree.getModel().valueForPathChanged(
					parentPath,
					new TreeModelEvent(this, parentPath, new int[] { getIndex(parentNode, insertedNode) },
							new Object[] { insertedNode }));
		}
		for (int i = 0; i < paths.length; i++) {
			tree.getSelectionModel().addSelectionPath(paths[i]);
		}
	}

	private int getIndex(Node parent, Node children) {
		NodeList list = parent.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (children == list.item(i)) {
				return i;
			}
		}
		return -1;
	}

	private void insertWithin(TreePath path) {
		if (getCurrentNodeTreePane().isReadOnly()) {
			return;
		}
		List<Node> recentCuttedNodeList = getRecentCutNode();
		if (recentCuttedNodeList == null || 0 == recentCuttedNodeList.size()) {
			return;
		}
		TreePath[] paths = new TreePath[recentCuttedNodeList.size()];
		NodeTreePane tree = getCurrentNodeTreePane();
		Node currentNode = (Node) path.getLastPathComponent();

		// tree.getSelectionModel().clearSelection();
		for (int i = 0; i < recentCuttedNodeList.size(); i++) {
			Node insertedNode = ((Node) recentCuttedNodeList.get(i)).cloneNode(true);
			TreePath insertedPath = path.pathByAddingChild(insertedNode);
			paths[i] = insertedPath;
			getCurrentNodeTreePane().appendChildNode(currentNode, insertedNode);
			tree.getSelectionModel().addSelectionPath(insertedPath);
		}
		tree.getModel().valueForPathChanged(path, currentNode);
		updateCurrentTitle(tree);
		tree.setupEditorPane(paths);
	}
	
	public void cut() {
		cutcopy(true);
	}

	public void copy() {
		cutcopy(false);
	}

	
	private void cutcopy(boolean isDeleteMode) {
		NodeTreePane tree = getCurrentNodeTreePane();
		TreePath[] paths = null;
		if (isClickedNode) {
			paths = new TreePath[1];
			paths[0] = tree.currentPopUpMenuNodePath;
		} else {
			paths = tree.getSelectionModel().getSelectionPaths();
		}
		cutcopy(paths, tree, isDeleteMode);
	}

	private void cutcopy(TreePath[] paths, NodeTreePane tree, boolean isDeleteMode) {
		if (isNodeDeleteProihibited(isDeleteMode, tree, paths)) {
			return;
		}
		List<Node> recentCutNode = resetRecentCutNode();
		cutcopy(isDeleteMode, tree, paths, recentCutNode);
		if (! getCurrentNodeTreePane().isReadOnly() && isDeleteMode) {
			updateCurrentTitle(tree);
			tree.setupEditorPane(null);
		}
		mediator.getMenuBarMediator().updateEditMenu();
	}

	private boolean isNodeDeleteProihibited(boolean isDeleteMode, NodeTreePane tree, TreePath[] paths) {
		return paths.length == 0
				|| (isDeleteMode && 0 < paths.length && tree.mediator.getMenuBarMediator().avoidNodeDelete(
						paths.length));
	}

	private List<Node> resetRecentCutNode() {
		List<Node> recentCutNode = getRecentCutNode();
		if (recentCutNode == null) {
			recentCutNode = new LinkedList<Node>();
			setRecentCutNode(recentCutNode);
		} else {
			recentCutNode.clear();
		}
		return recentCutNode;
	}

	private void cutcopy(boolean isDeleteMode, NodeTreePane tree, TreePath[] paths, List<Node> recentCutNode) {
		for (int i = 0; i < paths.length; i++) {
			TreePath path = paths[i];
			if(path == null){
				continue;
			}
			Node leaf = (Node) path.getLastPathComponent();
			Node parent = (Node) path.getParentPath().getLastPathComponent();
			recentCutNode.add(leaf);
			if (! getCurrentNodeTreePane().isReadOnly() && isDeleteMode) {
				parent.removeChild(leaf);
				tree.getSelectionModel().removeSelectionPath(path);
			}
		}
	}

	private void showAll(TreePath path) {
		NodeTreePane tree = getCurrentNodeTreePane();
		/*
		 * TreePath[] paths = tree.getSelectionPaths(); for(int i = 0;
		 * i<paths.length; i++){
		 * ((NodeTreePane)tree).expandDecendant((TreePath)paths[i]); }
		 */
		((NodeTreePane) tree).expandDecendant(path);
		tree.setupEditorPane(null);
	}

	private void hideAll(TreePath path) {
		boolean doCollapsed = false;
		NodeTreePane tree = getCurrentNodeTreePane();
		if (tree.isCollapsed(path)) {
			return;
		}
		// TreePath[] paths = tree.getSelectionPaths();
		// for(int i = 0; i<paths.length; i++){
		// TreePath branchPath = paths[i];
		/*
		 * if(tree.isCollapsed(path)){ continue; }
		 */
		NodeList branchObjectList = ((Node) path.getLastPathComponent()).getChildNodes();
		for (int j = 0; j < branchObjectList.getLength(); j++) {
			TreePath subBranchPath = path.pathByAddingChild(branchObjectList.item(j));
			if (!tree.isCollapsed(subBranchPath)) {
				tree.collapsePath(subBranchPath);
				doCollapsed = true;
			}
		}

		if (!doCollapsed) {
			tree.collapsePath(path);
		}
		tree.setupEditorPane(null);
	}

}
