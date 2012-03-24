/*

 DOMTreeWalkerTreeModel.java
 
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
package net.sqs2.exsed.source;

/**
 * @author hiroya
 *
 */

import java.util.LinkedList;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

/**
 * This class implements the Swing TreeModel interface so that the DOM tree
 * returned by a TreeWalker can be displayed in a JTree component.
 */
public class DOMTreeWalkerTreeModel implements TreeModel {
	TreeWalker walker; // The TreeWalker we're modeling for JTree

	/** Create a TreeModel for the specified TreeWalker */
	public DOMTreeWalkerTreeModel(TreeWalker walker) {
		this.walker = walker;
	}

	/**
	 * Create a TreeModel for a TreeWalker that returns all nodes in the
	 * specified document
	 */
	public DOMTreeWalkerTreeModel(Document document) {
		DocumentTraversal dt = (DocumentTraversal) document;
		walker = dt.createTreeWalker(document, NodeFilter.SHOW_ALL, null, false);
	}

	/**
	 * Create a TreeModel for a TreeWalker that returns the specified element
	 * and all of its descendant nodes.
	 */
	public DOMTreeWalkerTreeModel(Element element) {
		DocumentTraversal dt = (DocumentTraversal) element.getOwnerDocument();
		walker = dt.createTreeWalker(element, NodeFilter.SHOW_ALL, null, false);
	}

	// Return the root of the tree
	public Object getRoot() {
		return walker.getRoot();
	}

	// Is this node a leaf? (Leaf nodes are displayed differently by JTree)
	public boolean isLeaf(Object node) {
		walker.setCurrentNode((Node) node); // Set current node
		Node child = walker.firstChild(); // Ask for a child
		return (child == null); // Does it have any?
	}

	// How many children does this node have?
	public int getChildCount(Object node) {
		walker.setCurrentNode((Node) node); // Set the current node
		// TreeWalker doesn't count children for us, so we count ourselves
		int numkids = 0;
		Node child = walker.firstChild(); // Start with the first child
		while (child != null) { // Loop 'till there are no more
			numkids++; // Update the count
			child = walker.nextSibling(); // Get next child
		}
		return numkids; // This is the number of children
	}

	// Return the specified child of a parent node.
	public Object getChild(Object parent, int index) {
		walker.setCurrentNode((Node) parent); // Set the current node
		// TreeWalker provides sequential access to children, not random
		// access, so we've got to loop through the kids one by one
		Node child = walker.firstChild();
		while (index-- > 0)
			child = walker.nextSibling();
		return child;
	}

	// Return the index of the child node in the parent node
	public int getIndexOfChild(Object parent, Object child) {
		walker.setCurrentNode((Node) parent); // Set current node
		int index = 0;
		Node c = walker.firstChild(); // Start with first child
		while ((c != child) && (c != null)) { // Loop 'till we find a match
			index++;
			c = walker.nextSibling(); // Get the next child
		}
		return index; // Return matching position
	}

	// Return the index of the child node in the parent node
	public int _getIndexOfChild(Object parent, Object child) {
		walker.setCurrentNode((Node) parent); // Set current node
		int index = 0;
		Node c = walker.firstChild(); // Start with first child
		while ((c != child) && (c != null)) { // Loop 'till we find a match
			index++;
			c = walker.nextSibling(); // Get the next child
		}
		if (c == null) {
			return -1 * index;
		}
		return index; // Return matching position
	}

	public void valueForPathChanged(TreePath path, Object value) {
		for (int i = 0; i < listenerList.size(); i++) {
			((TreeModelListener) listenerList.get(i)).treeStructureChanged(new TreeModelEvent(this, path));
		}
	}

	LinkedList<TreeModelListener> listenerList = new LinkedList<TreeModelListener>();

	// This TreeModel never fires any events (since it is not editable)
	// so event listener registration methods are left unimplemented
	public void addTreeModelListener(TreeModelListener l) {
		listenerList.add(l);
	}

	public void removeTreeModelListener(TreeModelListener l) {
		listenerList.remove(l);
	}

}
