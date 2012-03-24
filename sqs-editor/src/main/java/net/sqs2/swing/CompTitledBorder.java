/*

 CompTitledBorder.java
 
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
 
 Created on 2004/08/09

 */
package net.sqs2.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * @author hiroya
 * 
 */

public class CompTitledBorder extends TitledBorder {
	protected JComponent component;
	public static final long serialVersionUID = 0;

	public CompTitledBorder(JComponent component) {
		this(null, component, LEFT, TOP);
	}

	public CompTitledBorder(Border border) {
		this(border, null, LEFT, TOP);
	}

	public CompTitledBorder(Border border, JComponent component) {
		this(border, component, LEFT, TOP);
	}

	public CompTitledBorder(Border border, JComponent component, int titleJustification, int titlePosition) {
		super(border, null, titleJustification, titlePosition, null, null);
		// super(null, null, titleJustification, titlePosition, null, null);
		this.component = component;

		if (border == null) {
			this.border = super.getBorder();
		}
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Insets borderInsets = createBorderInsets(c);
		Rectangle compR = paintBorderRect(c, g, x, y, width, height, borderInsets);
		Color col = g.getColor();
		g.setColor(c.getBackground());
		g.fillRect(compR.x, compR.y, compR.width, compR.height);
		g.setColor(col);
		// component.repaint();
	}

	private Rectangle paintBorderRect(Component c, Graphics g, int x, int y, int width, int height, Insets borderInsets) {
		Rectangle borderR = new Rectangle(x + EDGE_SPACING, y + EDGE_SPACING, width - (EDGE_SPACING * 2),
				height - (EDGE_SPACING * 2));
		Rectangle rect = new Rectangle(x, y, width, height);
		Insets insets = getBorderInsets(c);
		Rectangle compR = getComponentRect(rect, insets);
		int diff;
		switch (titlePosition) {
		case ABOVE_TOP:
			diff = compR.height + TEXT_SPACING;
			borderR.y += diff;
			borderR.height -= diff;
			break;
		case TOP:
		case DEFAULT_POSITION:
			diff = insets.top / 2 - borderInsets.top - EDGE_SPACING;
			borderR.y += diff;
			borderR.height -= diff;
			break;
		case BELOW_TOP:
		case ABOVE_BOTTOM:
			break;
		case BOTTOM:
			diff = insets.bottom / 2 - borderInsets.bottom - EDGE_SPACING;
			borderR.height -= diff;
			break;
		case BELOW_BOTTOM:
			diff = compR.height + TEXT_SPACING;
			borderR.height -= diff;
			break;
		}
		border.paintBorder(c, g, borderR.x, borderR.y, borderR.width, borderR.height);
		return compR;
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		Insets borderInsets = createBorderInsets(c);
		insets.top = EDGE_SPACING + TEXT_SPACING + borderInsets.top;
		insets.right = EDGE_SPACING + TEXT_SPACING + borderInsets.right;
		insets.bottom = EDGE_SPACING + TEXT_SPACING + borderInsets.bottom;
		insets.left = EDGE_SPACING + TEXT_SPACING + borderInsets.left;
		if (c == null) {
			return insets;
		}
		modifyInsets(insets, borderInsets, (component != null) ? component.getPreferredSize().height : 0);
		return insets;
	}

	private Insets createBorderInsets(Component c) {
		Insets borderInsets;
		if (border != null) {
			borderInsets = border.getBorderInsets(c);
		} else {
			borderInsets = new Insets(0, 0, 0, 0);
		}
		return borderInsets;
	}

	private void modifyInsets(Insets insets, Insets borderInsets, int compHeight) {
		switch (titlePosition) {
		case ABOVE_TOP:
			insets.top += compHeight + TEXT_SPACING;
			break;
		case TOP:
		case DEFAULT_POSITION:
			insets.top += Math.max(compHeight, borderInsets.top) - borderInsets.top;
			break;
		case BELOW_TOP:
			insets.top += compHeight + TEXT_SPACING;
			break;
		case ABOVE_BOTTOM:
			insets.bottom += compHeight + TEXT_SPACING;
			break;
		case BOTTOM:
			insets.bottom += Math.max(compHeight, borderInsets.bottom) - borderInsets.bottom;
			break;
		case BELOW_BOTTOM:
			insets.bottom += compHeight + TEXT_SPACING;
			break;
		}
	}

	public JComponent getTitleComponent() {
		return component;
	}

	public void setTitleComponent(JComponent component) {
		this.component = component;
	}

	public Rectangle getComponentRect(Rectangle rect, Insets borderInsets) {
		Dimension compD = component.getPreferredSize();
		Rectangle compR = new Rectangle(0, 0, compD.width, compD.height);
		switch (titlePosition) {
		case ABOVE_TOP:
			compR.y = EDGE_SPACING;
			break;
		case TOP:
		case DEFAULT_POSITION:
			compR.y = EDGE_SPACING + (borderInsets.top - EDGE_SPACING - TEXT_SPACING - compD.height) / 2;
			break;
		case BELOW_TOP:
			compR.y = borderInsets.top - compD.height - TEXT_SPACING;
			break;
		case ABOVE_BOTTOM:
			compR.y = rect.height - borderInsets.bottom + TEXT_SPACING;
			break;
		case BOTTOM:
			compR.y = rect.height - borderInsets.bottom + TEXT_SPACING
					+ (borderInsets.bottom - EDGE_SPACING - TEXT_SPACING - compD.height) / 2;
			break;
		case BELOW_BOTTOM:
			compR.y = rect.height - compD.height - EDGE_SPACING;
			break;
		}
		switch (titleJustification) {
		case LEFT:
		case DEFAULT_JUSTIFICATION:
			compR.x = TEXT_INSET_H + borderInsets.left;
			break;
		case RIGHT:
			compR.x = rect.width - borderInsets.right - TEXT_INSET_H - compR.width;
			break;
		case CENTER:
			compR.x = (rect.width - compR.width) / 2;
			break;
		}
		return compR;
	}

}
