/**
 * 
 */
package net.sqs2.editor.base.swing;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import net.sqs2.editor.SourceEditorJarURIContext;

class EditMenuItems{
	JMenuItem cutMenuItem;
	JMenuItem copyMenuItem;
	JMenuItem insertUpperMenuItem = null;
	JMenuItem insertWithinMenuItem = null;
	JMenuItem insertLowerMenuItem = null;
	JMenuItem showAllMenuItem;
	JMenuItem hideAllMenuItem;
	
	EditMenuItems(SourceEditorMediator mediator, boolean isClickedNode){
		TreeNodeActionAdapter listener = new TreeNodeActionAdapter(mediator, isClickedNode);
		String target = null;

		if (isClickedNode) {
			target = Messages.VOCABRUARY_CLICKED_NODE;
		} else {
			target = Messages.VOCABRUARY_SELECTED_NODE;
		}

		this.cutMenuItem = createPopupMenuItem(target + Messages.SUFFIX_CUT_LABEL, "d", listener);
		this.cutMenuItem.setIcon(createImageIcon("Cut16.gif"));
		this.copyMenuItem = createPopupMenuItem(target + Messages.SUFFIX_COPY_LABEL, "c", listener);
		this.copyMenuItem.setIcon(createImageIcon("Copy16.gif"));

		this.cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		this.copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));

		ImageIcon pasteIcon = createImageIcon("Paste16.gif");
		this.insertUpperMenuItem = createPopupMenuItem("↑  " + Messages.VOCABRUARY_CLICKED_NODE
				+ Messages.SUFFIX_PASTE_AS_PRECEDING_SIBLING_LABEL, "b", listener);
		this.insertUpperMenuItem.setIcon(pasteIcon);
		this.insertWithinMenuItem = createPopupMenuItem("→  " + Messages.VOCABRUARY_CLICKED_NODE
				+ Messages.SUFFIX_PASTE_AS_CHILD_LABEL, "i", listener);
		this.insertWithinMenuItem.setIcon(pasteIcon);
		this.insertLowerMenuItem = createPopupMenuItem("↓  " + Messages.VOCABRUARY_CLICKED_NODE
				+ Messages.SUFFIX_PASTE_AS_FOLLOWING_SIBLING_LABEL, "a", listener);
		this.insertLowerMenuItem.setIcon(pasteIcon);
		this.insertLowerMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		this.showAllMenuItem = createPopupMenuItem(Messages.VOCABRUARY_CLICKED_NODE
				+ Messages.SUFFIX_EXPAND_NODE_LABEL, "S", listener);
		this.hideAllMenuItem = createPopupMenuItem(Messages.VOCABRUARY_CLICKED_NODE
				+ Messages.SUFFIX_HIDE_NODE_LABEL, "H", listener);
		
	}

	private JMenuItem createPopupMenuItem(String label, String command, TreeNodeActionAdapter listener) {
		JMenuItem item = new JMenuItem(label);
		item.setActionCommand(command);
		item.addActionListener(listener);
		return item;
	}

	private static ImageIcon createImageIcon(String url) {
		try {
			return new ImageIcon(new URL(SourceEditorJarURIContext.getImageBaseURI() + url));
		} catch (MalformedURLException ignore) {
			throw new RuntimeException("cannot resolve:" + url);
		}
	}
	
	public void updateMenuItem(boolean isReadOnly, boolean isPasteMenuItemsEnabled, boolean hasSomeNodeFocused) {
		this.copyMenuItem.setEnabled(hasSomeNodeFocused);
		this.showAllMenuItem.setEnabled(hasSomeNodeFocused);
		this.hideAllMenuItem.setEnabled(hasSomeNodeFocused);
		this.cutMenuItem.setEnabled(! isReadOnly && hasSomeNodeFocused);
		this.insertUpperMenuItem.setEnabled(isPasteMenuItemsEnabled);
		this.insertWithinMenuItem.setEnabled(isPasteMenuItemsEnabled);
		this.insertLowerMenuItem.setEnabled(isPasteMenuItemsEnabled);
	}
	
	public void setInsertMenuItemEnabled(boolean isEnabled){
		this.insertUpperMenuItem.setEnabled(isEnabled);
		this.insertWithinMenuItem.setEnabled(isEnabled);
		this.insertLowerMenuItem.setEnabled(isEnabled);
	}

}