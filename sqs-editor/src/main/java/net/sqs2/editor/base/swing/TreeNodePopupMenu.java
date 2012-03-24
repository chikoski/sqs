/*

 TreeNodePopupMenu.java
 
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

import java.awt.Component;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.w3c.dom.Node;

public class TreeNodePopupMenu extends JPopupMenu {
	public static final long serialVersionUID = 0;
	private final NodeTreePane treePane;	

	EditMenuItems editMenuItems;

	TreeNodePopupMenu(SourceEditorMediator mediator, boolean isClickedNode) {
		this.treePane = mediator.getCurrentTreePane();
		createPopupMenu(mediator, isClickedNode);
		setPopupSize(320, 150);
		updateMenu();
	}
	
	public void show(Component invoker, int x, int y) {
		updateMenu();
		super.show(invoker, x, y);
	}

	public void updateMenu() {
		List<Node> recentCutteNodeList = treePane.mediator.getMenuBarMediator().getRecentCutNode();
		if (recentCutteNodeList != null && 0 < recentCutteNodeList.size()) {
			setInsertMenuEnabled(true);
		} else {
			setInsertMenuEnabled(false);
		}
		boolean isPasteMenuItemEnabled = treePane.mediator.getMenuBarMediator().getRecentCutNode() != null;
		this.editMenuItems.updateMenuItem(isReadOnly(), isPasteMenuItemEnabled, true);
	}

	public NodeTreePane getTreePane() {
		return treePane;
	}

	public JMenuItem getCutMenuItem() {
		return editMenuItems.cutMenuItem;
	}

	public JMenuItem getCopyMenuItem() {
		return editMenuItems.copyMenuItem;
	}

	private void createPopupMenu(SourceEditorMediator mediator, boolean isClickedNode) {
		boolean isReadOnly = mediator.getCurrentTreePane().isReadOnly();
		boolean isPasteMenuEnabled = mediator.getMenuBarMediator().getRecentCutNode() != null;
		this.editMenuItems = new EditMenuItems(mediator, isClickedNode);
		EditMenuDecorator.setupAsPopupMenu(this, editMenuItems, isReadOnly, isPasteMenuEnabled, true);
	}

	private void setInsertMenuEnabled(boolean isEnabled) {
		this.editMenuItems.setInsertMenuItemEnabled(isEnabled);
	}

	boolean isReadOnly() {
		return treePane.isReadOnly();
	}

}
