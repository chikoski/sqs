/*

 JTabbedPaneWithBackgroundImage.java

 Copyright 2009 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */
package net.sqs2.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

public class JTabbedPaneWithBackgroundImage extends JTabbedPane {
	private static final long serialVersionUID = 0L;
	private Color bgColor;
	private Image image;

	public JTabbedPaneWithBackgroundImage(Color bgColor, Image image) {
		super(SwingConstants.LEFT);
		this.bgColor = bgColor;
		this.image = image;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int y = getSize().height - this.image.getHeight(null) - 10;
		Rectangle lastTab = getUI().getTabBounds(this, getTabCount() - 1);
		int tabEnd = lastTab.y + lastTab.height;
		if (y < tabEnd) {
			y = tabEnd;
		}
		g.setColor(bgColor);
		g.fillRect(0, 0, lastTab.x - 1, lastTab.y + lastTab.width - 1);
		g.fillRect(0, tabEnd, lastTab.x + lastTab.width, getHeight() - tabEnd);
		g.drawImage(this.image, 10, y, null);
	}
}