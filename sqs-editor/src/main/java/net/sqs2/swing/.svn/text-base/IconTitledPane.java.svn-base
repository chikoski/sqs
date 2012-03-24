/*

 IconTitledPane.java

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

import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

/**
 * @author hiroya
 * 
 */
public class IconTitledPane extends JPanel {
	public static final long serialVersionUID = 0;
	protected CompTitledBorder iconBorder;
	protected JComponent component;
	protected Box panel;
	protected boolean transmittingAllowed;
	protected StateTransmitter transmitter;
	String title;

	public IconTitledPane() {
	}

	public void initialize(Icon icon, String title, Border border) {
		initialize(icon, title, border, null);
	}

	public void initialize(Icon icon, String title, Border border, JPopupMenu menu) {
		this.title = title;
		this.iconBorder = new IconTitledBorder(icon, title, border, menu);
		this.component = this.iconBorder.getTitleComponent();
		this.panel = Box.createVerticalBox();
		setLayout(null);
		setBorder(this.iconBorder);
		super.add(this.component);
		super.add(this.panel);
		this.transmittingAllowed = false;
		this.transmitter = null;
	}

	public JComponent getTitleComponent() {
		return this.component;
	}

	public void setTitleComponent(JComponent newComponent) {
		remove(this.component);
		add(newComponent);
		this.iconBorder.setTitleComponent(newComponent);
		this.component = newComponent;
	}

	public Box getContentPane() {
		return this.panel;
	}

	public void doLayout() {
		Insets insets = getInsets();
		Rectangle rect = getBounds();
		rect.x = 0;
		rect.y = 0;

		Rectangle compR = iconBorder.getComponentRect(rect, insets);
		this.component.setBounds(compR);
		rect.x += insets.left;
		rect.y += insets.top;
		rect.width -= insets.left + insets.right;
		rect.height -= insets.top + insets.bottom;
		this.panel.setBounds(rect);
	}

	public void setTransmittingAllowed(boolean enable) {
		this.transmittingAllowed = enable;
	}

	public boolean getTransmittingAllowed() {
		return this.transmittingAllowed;
	}

	public void setTransmitter(StateTransmitter transmitter) {
		this.transmitter = transmitter;
	}

	public StateTransmitter getTransmitter() {
		return this.transmitter;
	}

	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		if (this.transmittingAllowed && this.transmitter != null) {
			this.transmitter.setChildrenEnabled(enable);
		}
	}

	public void add(JComponent comp) {
		getContentPane().add(comp);
	}
}
