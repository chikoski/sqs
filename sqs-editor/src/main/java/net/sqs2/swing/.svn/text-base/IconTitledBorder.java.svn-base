/*

 IconTitledBorder.java

 Copyright 2004 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2004/08/09

 */
package net.sqs2.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

/**
 * @author hiroya
 * 
 */
public class IconTitledBorder extends CompTitledBorder {
	public static final long serialVersionUID = 0;

	// transient PopupFactory popupFactory;
	public IconTitledBorder(Icon icon, String title) {
		super(createComponent(icon, title));
	}

	public IconTitledBorder(Icon icon, String title, JPopupMenu menu) {
		super(createComponent(icon, title, menu));
	}

	public IconTitledBorder(Icon icon, String title, Border border) {
		super(border, createComponent(icon, title));
	}

	public IconTitledBorder(Icon icon, String title, Border border, JPopupMenu menu) {
		super(border, createComponent(icon, title, menu));
	}

	static JComponent createComponent(Icon icon, String title) {
		return createComponent(icon, title, null);
	}

	static JComponent createComponent(Icon icon, String title, final JPopupMenu menu) {
		Box comp = Box.createHorizontalBox();
		comp.add(new JLabel(icon));
		comp.add(Box.createHorizontalStrut(4));
		comp.add(new JLabel(title));
		if (menu != null) {
			comp.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent event) {
					popupMenu(event);
				}

				public void mouseReleased(MouseEvent event) {
					popupMenu(event);
				}

				private void popupMenu(MouseEvent event) {
					if (event.isPopupTrigger()) {
						menu.show(event.getComponent(), event.getX(), event.getY());
					}
				}
			});
		}
		return comp;
	}

}
