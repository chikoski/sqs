/*

 SessionProgressBar.java

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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import net.sqs2.lang.GroupThreadFactory;
import net.sqs2.omr.model.PageTaskException;
import net.sqs2.omr.model.PageTaskNumberCounter;
import net.sqs2.omr.session.model.PageTaskExceptionEntry;
import net.sqs2.omr.session.service.PageTaskExecutionProgressModel;


public class PageTaskExecutionProgressBar extends JPanel{
	
	private static final long serialVersionUID = 0L;

	private float indeterminate = -1f;
	private int mousePositionX = -1;

	private transient ExecutorService executor;
	private transient Future<?> indeterminateAnimationFuture = null;

	PageTaskExecutionProgressModel model;
	
	public PageTaskExecutionProgressBar(PageTaskExecutionProgressModel model) {
		this.model = model;
		setPreferredSize(new Dimension(300, 20));
		setSize(new Dimension(300, 20));
		setBorder(new MatteBorder(1,1,1,1,Color.gray));	
	}

	class ProgressBarMouseMotionAdapter implements MouseMotionListener {
		
		public void mouseDragged(MouseEvent e) {
		}

		public void mouseMoved(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			Dimension size = getSize();
			int height = (int) size.getHeight();
			setToolTipText(null);
			try {
				if (PageTaskExecutionProgressBar.this.model.getNumTargetPages() == 0) {
					return;
				}
				// int unit = width /
				// OMRSessionProgressBar.this.model.getNumTargetPages();
				// unit = (unit == 0)? 1:unit;
				if (0 <= y && y < height) {
					PageTaskExecutionProgressBar.this.mousePositionX = x;
					model.hasChanged();
					model.notifyObservers();
					return;
				} else {
					// TODO: reset progress bar
					model.hasChanged();
					model.notifyObservers();
				}
			} catch (ConcurrentModificationException ignore) {
			}
		}
	}
	
	public void setMousePositionX(int x){
		this.mousePositionX = x;
	}

	public boolean isIndeterminate() {
		return (0 <= this.indeterminate);
	}

	public void setIndeterminate(boolean isIndeterminate) {
		if (isIndeterminate) {
			this.indeterminate = 0.0f;
			if (this.executor == null) {
				this.executor = Executors.newSingleThreadExecutor(new GroupThreadFactory(
						"SessionProgressBar", Thread.MIN_PRIORITY, true));
			}
			this.indeterminateAnimationFuture = this.executor.submit(new Runnable() {
				public void run() {
					while (0 <= PageTaskExecutionProgressBar.this.indeterminate) {
						PageTaskExecutionProgressBar.this.indeterminate += 0.033f;
						// TODO: update progress bar
						model.hasChanged();
						model.notifyObservers();
						try {
							Thread.sleep(33);
						} catch (InterruptedException ignore) {
						}
					}
				}
			});

		} else {
			if (this.indeterminateAnimationFuture != null) {
				this.indeterminateAnimationFuture.cancel(true);
				this.indeterminate = -1.0f;
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Dimension size = getSize();
		int width = (int) size.getWidth();
		int height = (int) size.getHeight();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) size.getWidth(), (int) size.getHeight());

		// int FONT_WIDTH = 14;
		int FONT_HEIGHT = 14;
		// int statusMesageWidth = FONT_WIDTH *
		// this.model.getStatusMessage().length();

		if (this.model == null || 0 <= this.indeterminate
				|| this.model.getNumTargetPages() == 0) {
			paintIndeterminate(g, size, width);
			g.setColor(Color.WHITE);
			g.drawString(this.model.getStatusMessage(), 
					4, height / 2 + FONT_HEIGHT / 2);
			return;
		}

		paintProgressBarCore(g, width, height);
		
		paintErrorItems(g, width, height, FONT_HEIGHT);
	}

	private void paintProgressBarCore(Graphics g, int width, int height) {
		g.setColor(PageTaskGUIConstants.COLOR_TOTAL);
		g.fillRect(0, 0, width, height);

		PageTaskNumberCounter taskNumberCounter = this.model.createTaskNumberCounter();
		int total = taskNumberCounter.getNumTotal();
		
		float unit = (float) width / total;
		
		g.setColor(PageTaskGUIConstants.COLOR_PREPARED);
		g.fillRect((int)( getWidth() - unit * taskNumberCounter.getNumPrepared()),
				0, 
				(int)( unit * taskNumberCounter.getNumPrepared()), 
				height);

		g.setColor(PageTaskGUIConstants.COLOR_REFUSED);
		g.fillRect(0, 0,
				(int)(unit * taskNumberCounter.getNumReused()),
				height);

		g.setColor(PageTaskGUIConstants.COLOR_EXTERNALIZED);
		g.fillRect((int)(unit * taskNumberCounter.getNumReused()), 0,
				(int)(unit * taskNumberCounter.getNumExternalized()),
				height);
		
		g.setColor(PageTaskGUIConstants.COLOR_SUBMITTED);
		g.fillRect((int)(unit * (taskNumberCounter.getNumReused() + taskNumberCounter.getNumExternalized())),
				0,
				(int)(unit * taskNumberCounter.getNumSubmitted()),
				height);
		
		g.setColor(PageTaskGUIConstants.COLOR_LEASED_LOCAL);
		g.fillRect((int)(unit * (taskNumberCounter.getNumReused() + taskNumberCounter.getNumExternalized() + taskNumberCounter.getNumSubmitted())),
				0, 
				(int)(unit * taskNumberCounter.getNumLocalLeased()), 
				height);
		
		g.setColor(PageTaskGUIConstants.COLOR_LEASED_REMOTE);
		g.fillRect((int)(unit * (taskNumberCounter.getNumReused() + taskNumberCounter.getNumExternalized() + taskNumberCounter.getNumSubmitted() + taskNumberCounter.getNumLocalLeased())),
				0,
				(int)(unit * taskNumberCounter.getNumRemoteLeased()),
				height);
	}

	private void paintErrorItems(Graphics g, int width, int height,
			int FONT_HEIGHT) {
		g.setColor(PageTaskGUIConstants.COLOR_ERROR);

		int unit = width / this.model.getNumTargetPages() + 1;

		try {
			for (Map.Entry<String, PageTaskExceptionEntry> taskErrorEntry : this.model.getErrorPathToTaskErrorEntryMap().entrySet()) {
				PageTaskExceptionEntry e = taskErrorEntry.getValue();
				int errorIndex = e.getIndex();
				PageTaskException taskException = e.getTaskError();
				int x1 = errorIndex * width / this.model.getNumTargetPages();
				if (x1 <= this.mousePositionX && this.mousePositionX <= x1 + unit) {
					g.setColor(Color.RED);
					g.fillRect(x1, 0, unit, height);
					g.setColor(PageTaskGUIConstants.COLOR_ERROR);
					String message = taskException.getExceptionModel().getPageID().getFileResourceID().getRelativePath() + "="
							+ taskException.getLocalizedMessage();
					PageTaskExecutionProgressBar.this.model.setStatusMessage(message);
				} else {
					g.fillRect(x1, 0, unit, height);
				}
			}
			g.setColor(Color.WHITE);
			g.drawString(this.model.getStatusMessage(), 0, height / 2 + FONT_HEIGHT / 2);
		} catch (ConcurrentModificationException ignore) {
		}
	}

	private void paintIndeterminate(Graphics g, Dimension size, int width) {
		g.setColor(Color.GRAY);
		g.fillRect(1, 1, (int) size.getWidth() - 1, (int) size.getHeight() - 1);
		g.setColor(Color.LIGHT_GRAY);
		if (((int) this.indeterminate) % 2 == 0) {
			g.fillRect((int) (width * (this.indeterminate % 1)), 0, width / 10, (int) size.getHeight());
		} else {
			g.fillRect(width - (int) (width * (this.indeterminate % 1)), 0, width / 10, (int) size
					.getHeight());
		}
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, (int) size.getWidth() - 1, (int) size.getHeight() - 1);
	}

}
