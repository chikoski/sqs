/*

 NodeTreeSelectionModel.java
 
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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

/**
 * @author hiroya
 * 
 */
public class NodeTreeSelectionModel extends DefaultTreeSelectionModel {
	public static final long serialVersionUID = 0;
	public static final int NORMAL_MODE = 0;
	public static final int XOR_MODE = 1;

	AbstractNodeTreePane tree;

	public NodeTreeSelectionModel(AbstractNodeTreePane tree) {
		setSelectionMode(DefaultTreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		this.tree = tree;
	}

	Set<TreePath> getEditablePathSet(int row1, int row2) {
		LinkedHashSet<TreePath> set = new LinkedHashSet<TreePath>();
		int min = Math.min(row1, row2);
		int max = Math.max(row1, row2);
		TreePath prevEditablePath = null;
		for (int i = min; i <= max; i++) {
			TreePath path = tree.getEditablePath(tree.getPathForRow(i));
			TreePath editablePath = tree.getEditablePath(path);
			if (prevEditablePath == null || !prevEditablePath.isDescendant(editablePath)) {
				set.add(editablePath);
			}
			prevEditablePath = editablePath;
		}
		return set;
	}

	void xORSelection(int row1, int row2) {
		Set<TreePath> set = getEditablePathSet(row1, row2);
		for (Iterator<TreePath> it = set.iterator(); it.hasNext();) {
			TreePath path = (TreePath) it.next();
			if (this.isPathSelected(path)) {
				removeSelectionPath(path);
			} else {
				addSelectionPath(path);
			}
		}
		set.clear();
	}

	public void selectNodeGroup(int row1, int row2) {
		int min = Math.min(row1, row2);
		int max = Math.max(row1, row2);
		for (int i = min; i <= max; i++) {
			selectNodeGroup(i);
		}
	}

	public void removeHierachicalSelected() {
		for (int i = 0; i < this.getSelectionCount(); i++) {
			TreePath path = getSelectionPaths()[i];
			if (isHierachicalSelected(path)) {
				this.removeSelectionPath(path);
			}
		}
	}

	public boolean isHierachicalSelected(TreePath path) {
		TreePath p = path;
		if (p == null) {
			return false;
		}
		while ((p = p.getParentPath()) != null) {
			if (this.isPathSelected(p)) {
				return true;
			}
		}
		return false;
	}

	public void selectNodeGroup(int rowClicked) {
		TreePath clickedPath = tree.getPathForRow(rowClicked);
		TreePath clickedTargetPath = tree.getEditablePath(clickedPath);
		if (clickedTargetPath == null) {
			return;
		}

		boolean isSelectedByAncestor = removeAncestorSelectedPath(clickedTargetPath);

		if (!isSelectedByAncestor) {
			tree.addSelectionPath(clickedTargetPath);
		}
	}

	private boolean removeAncestorSelectedPath(TreePath clickedTargetPath) {
		int[] rows = getSelectionRows();
		boolean isSelectedByAncestor = false;

		if (rows != null) {
			for (int i = 0; i < rows.length; i++) {
				TreePath aSelectedPath = tree.getPathForRow(rows[i]);
				if (aSelectedPath.isDescendant(clickedTargetPath)) {
					isSelectedByAncestor = true;
				} else if (clickedTargetPath.isDescendant(aSelectedPath)) {
					tree.removeSelectionPath(aSelectedPath);
				}
			}
		}
		return isSelectedByAncestor;
	}
}
