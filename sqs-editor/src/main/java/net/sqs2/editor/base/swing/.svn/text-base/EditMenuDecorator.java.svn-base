/**
 * 
 */
package net.sqs2.editor.base.swing;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

public class EditMenuDecorator{
	static void setupAsPopupMenu(JPopupMenu menu, EditMenuItems items, boolean isReadOnly, boolean isPasteMenuItemsEnabled, boolean hasSomeNodeFocused){
		menu.add(items.cutMenuItem);
		menu.add(items.copyMenuItem);
		menu.addSeparator();
		menu.add(items.insertUpperMenuItem);
		menu.add(items.insertWithinMenuItem);
		menu.add(items.insertLowerMenuItem);
		menu.addSeparator();
		menu.add(items.showAllMenuItem);
		menu.add(items.hideAllMenuItem);
		items.updateMenuItem(isReadOnly, isPasteMenuItemsEnabled, hasSomeNodeFocused);
	}

	static void setupAsJMenu(JMenu menu, EditMenuItems items, boolean isReadOnly, boolean isPasteMenuItemsEnabled, boolean hasSomeNodeFocused){
		menu.add(items.cutMenuItem);
		menu.add(items.copyMenuItem);
		menu.addSeparator();
		menu.add(items.insertUpperMenuItem);
		menu.add(items.insertWithinMenuItem);
		menu.add(items.insertLowerMenuItem);
		menu.addSeparator();
		menu.add(items.showAllMenuItem);
		menu.add(items.hideAllMenuItem);		
		items.updateMenuItem(isReadOnly, isPasteMenuItemsEnabled, hasSomeNodeFocused);
	}
}