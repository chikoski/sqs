/*

 TextOverrayImageIcon.java

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

 Created on 2004/08/15

 */
package net.sqs2.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class TextOverlayImageIcon extends ImageIcon {
	public static final long serialVersionUID = 0;
	String label;
	Icon icon;
	Font font;
	int xoffset;

	public TextOverlayImageIcon(URL url, String label, int size, int xoffset) {
		super(url);
		this.label = label;
		if (label != null) {
			this.font = new Font("Serif", Font.PLAIN, size);
			this.xoffset = xoffset;
		}
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		super.paintIcon(c, g, x, y);
		if (this.label != null) {
			FontMetrics fm = g.getFontMetrics();
			int offsetX = (getIconWidth() - fm.stringWidth(this.label)) / 2 + this.xoffset;
			int offsetY = (getIconHeight() - fm.getHeight()) / 2 - 2;
			g.setColor(Color.black);
			g.setFont(this.font);
			g.drawString(this.label, x + offsetX, y + offsetY + fm.getHeight());
		}
	}
}
