/*

 SessionProgressMeter.java

 Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2006/01/10

 */
package net.sqs2.omr.swing.pagetask;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JPanel;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.session.service.PageTaskExecutionProgressModel;
import net.sqs2.omr.swing.Images;

public class PageTaskExecutionProgressMeter extends JPanel{
	private static final long serialVersionUID = 0L;

	private static Stroke DASHED_STROKE1 = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_MITER, 10.0f, new float[] { 2.0f, 1.0f }, 0.0f);
	private static Stroke DASHED_STROKE2 = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_MITER, 10.0f, new float[] { 1.0f, 1.0f }, 0.0f);

	PageTaskExecutionProgressModel model;
	
	public PageTaskExecutionProgressMeter(PageTaskExecutionProgressModel model) {
		this.model = model;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.model == null) {
			return;
		}
		setPreferredSize(new Dimension(200, 120));
		Dimension size = getSize();
		int width = (int) size.getWidth();
		int height = (int) size.getHeight();
		int fieldHeight = height / PageTaskGUIConstants.COLORS.length;
		int fontSize = 12;
		int fieldWidth = 55;

		g.setColor(getBackground());
		g.fillRect(0, 0, width, height);

		for (int i = 0; i < PageTaskGUIConstants.COLORS.length; i++) {
			if ((i == 1 && this.model.getNumReusedPages() == 0)
					|| (i == 4 && this.model.getNumRemoteLeasedPages() == 0)
					|| (i == 6 && 0 == this.model.getNumErrorPages())) {
				continue;
			}
			Color color = PageTaskGUIConstants.COLORS[i];
			g.setColor(color);
			g.fillRect(fieldWidth - 2, fieldHeight * (i + 1) - fontSize, fontSize - 1, fontSize - 1);
			g.setColor(Color.BLACK);
			g.drawRect(fieldWidth - 2, fieldHeight * (i + 1) - fontSize, fontSize - 1, fontSize - 1);
		}

		drawNumPageTasks1(g, fieldHeight, fontSize, fieldWidth);

		drawNumPageTasks2(g, fieldHeight, fontSize);
		drawTimeString(g, fieldHeight, fontSize, fieldWidth);

		drawSeparatorLine(g, fieldHeight, fontSize, fieldWidth);

	}

	private void drawNumPageTasks1(Graphics g, int fieldHeight, int fontSize,
			int fieldWidth) {
		g.setColor(Color.BLACK);

		g.drawString(Messages.SESSION_PROCESS_NUMFILES_TOTAL, fieldWidth + fontSize, fieldHeight * 1);

		g.drawLine(getCategorySeparatorLineXStart(fontSize), getCategorySeparatorLineY(fieldHeight, fontSize,
				3), getCategorySeparatorLineXEnd(fontSize, fieldWidth), getCategorySeparatorLineY(
				fieldHeight, fontSize, 3));

		if (0 < this.model.getNumReusedPages()) {
			g.drawString(Messages.SESSION_PROCESS_NUMFILES_REUSED, fieldWidth + fontSize, fieldHeight * 2);
		} else {
			g.drawImage(Images.TRIANGLE_DOWN_IMAGE, fieldWidth + fontSize * 2, fieldHeight * 3 / 2, this);
		}

		g.drawString(Messages.SESSION_PROCESS_NUMFILES_PREPARED, fieldWidth + fontSize, fieldHeight * 3);

		g.drawString(Messages.SESSION_PROCESS_NUMFILES_LEASEDLOCAL, fieldWidth + fontSize, fieldHeight * 4);

		if (this.model.isGridAvailable()) {
			g.drawString(Messages.SESSION_PROCESS_NUMFILES_LEASEDREMOTE, fieldWidth + fontSize,
					fieldHeight * 5);
		} else {
			g.drawImage(Images.TRIANGLE_DOWN_IMAGE, fieldWidth + fontSize * 2, fieldHeight * 9 / 2, this);
		}

		g.drawString(Messages.SESSION_PROCESS_NUMFILES_SUBMITTED, fieldWidth + fontSize, fieldHeight * 6);

		if (0 < this.model.getNumErrorPages()) {
			g.drawString(Messages.SESSION_PROCESS_NUMFILES_ERROR, fieldWidth + fontSize, fieldHeight * 7);
		} else {
			g.drawImage(Images.TRIANGLE_DOWN_IMAGE, fieldWidth + fontSize * 2, fieldHeight * 13 / 2, this);
		}
		g.drawString(Messages.SESSION_PROCESS_NUMFILES_EXTERNALIZED, fieldWidth + fontSize, fieldHeight * 8);
	}

	private void drawNumPageTasks2(Graphics g, int fieldHeight, int fontSize) {
		if (this.model.getNumTargetPages() != 0) {
			g.drawString(Integer.toString(this.model.getNumTargetPages()), fontSize,
					fieldHeight * 1);
		} else {
			g.drawString("-", fontSize, fieldHeight * 1);
		}

		if (this.model.getNumReusedPages() != 0) {
			g.drawString(Integer.toString(this.model.getNumReusedPages()), fontSize,
					fieldHeight * 2);
		} else {

		}
		g.drawString(Integer.toString(this.model.getNumPreparedPages()), fontSize,
				fieldHeight * 3);
		g.drawString(Integer.toString(this.model.getNumLocalLeasedPages()), fontSize,
				fieldHeight * 4);

		if (0 < this.model.getNumRemoteLeasedPages()) {
			g.drawString(Integer.toString(this.model.getNumRemoteLeasedPages()), fontSize,
					fieldHeight * 5);
		} else {
		}

		g.drawString(Integer.toString(this.model.getNumSubmittedPages()), fontSize,
				fieldHeight * 6);
		if (0 < this.model.getNumErrorPages()) {
			g.drawString(Integer.toString(this.model.getNumErrorPages()), fontSize,
					fieldHeight * 7);
		} else {
		}
	}

	private void drawTimeString(Graphics g, int fieldHeight, int fontSize,
			int fieldWidth) {
		g.drawString(Integer.toString(this.model.getNumExternalizingPages()), fontSize,
				fieldHeight * 8);

		if (this.model.getTimeElapsedString() != null) {
			g.drawString(Messages.SESSION_PROCESS_TIMEELAPSED + ": "
					+ this.model.getTimeElapsedString(),
					fieldWidth * 4 - fontSize, fieldHeight * 1);
		}

		if (this.model.getPageParSecString() != null) {
			g.drawString(Messages.SESSION_PROCESS_RATE + ": "
					+ this.model.getPageParSecString() + " "
					+ Messages.SESSION_PROCESS_RATE_UNIT, fieldWidth * 4 - fontSize, fieldHeight * 2);
		}

		if (this.model.getTimeRemainsString() != null) {
			g.drawString(Messages.SESSION_PROCESS_TIMEREMAINS + ": "
					+ this.model.getTimeRemainsString(),
					fieldWidth * 4 - fontSize, fieldHeight * 3);
		}
	}

	private void drawSeparatorLine(Graphics g, int fieldHeight, int fontSize,
			int fieldWidth) {
		((Graphics2D) g).setStroke(PageTaskExecutionProgressMeter.DASHED_STROKE1);
		g.drawLine(getCategorySeparatorLineXStart(fontSize), getCategorySeparatorLineY(fieldHeight, fontSize,
				5), getCategorySeparatorLineXEnd(fontSize, fieldWidth), getCategorySeparatorLineY(
				fieldHeight, fontSize, 5));
		((Graphics2D) g).setStroke(PageTaskExecutionProgressMeter.DASHED_STROKE2);
		g.drawLine(getCategorySeparatorLineXStart(fontSize), getCategorySeparatorLineY(fieldHeight, fontSize,
				7), getCategorySeparatorLineXEnd(fontSize, fieldWidth), getCategorySeparatorLineY(
				fieldHeight, fontSize, 7));
		g.drawLine(getCategorySeparatorLineXStart(fontSize), getCategorySeparatorLineY(fieldHeight, fontSize,
				13), getCategorySeparatorLineXEnd(fontSize, fieldWidth), getCategorySeparatorLineY(
				fieldHeight, fontSize, 13));
	}

	private int getCategorySeparatorLineY(int fieldHeight, int fontSize, int step) {
		return fieldHeight * step / 2 - fontSize / 2;
	}

	private int getCategorySeparatorLineXStart(int fontSize) {
		return fontSize - 3;
	}

	private int getCategorySeparatorLineXEnd(int fontSize, int fieldWidth) {
		return fieldWidth * 4 - fontSize * 2;
	}

}
