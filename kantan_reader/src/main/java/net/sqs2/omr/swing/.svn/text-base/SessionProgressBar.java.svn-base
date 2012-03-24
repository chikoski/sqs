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
package net.sqs2.omr.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.sqs2.omr.session.event.MarkReaderSessionMonitorAdapter;
import net.sqs2.omr.task.PageTask;
import net.sqs2.omr.task.TaskError;

public class SessionProgressBar extends SessionProgressPanel{
	private static final long serialVersionUID = 0L;

	private float indeterminate = -1f;
	private int margin = 1;
	private int mousePositionX = -1;

	private transient ExecutorService executor;
	private transient Future<?> indeterminateAnimationFuture = null;
	
	public SessionProgressBar(SessionProgressModel model){
		super(model);
		setPreferredSize(new Dimension(300,20));
		setSize(new Dimension(300,20));
		addMouseMotionListener(new ProgressBarMouseMotionAdapter());
		addMouseListener(new MouseAdapter(){
			public void mouseExited(MouseEvent ev){
				clear();
			}
		});
		addFocusListener(new FocusAdapter(){
			public void focusLost(FocusEvent ev) {
				clear();
			}
		});
		this.sessionProgressModel.addSessionMonitor(new MarkReaderSessionMonitorAdapter(){
			public void notifyTaskProduced(PageTask pageTask) {
				setIndeterminate(true);
			}

			public void notifyStoreTask(PageTask pageTask) {
				setIndeterminate(false);
			}
		});
	}

	void clear() {
		SessionProgressBar.this.sessionProgressModel.setStatusMessage("");
		SessionProgressBar.this.mousePositionX = -1;
		update();
	}

	class ProgressBarMouseMotionAdapter implements MouseMotionListener{
		public void mouseDragged(MouseEvent e) {

		}
		public void mouseMoved(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			Dimension size = getSize();
			// int width = (int)size.getWidth() - OMRSessionProgressBar.this.margin * 2;
			int height = (int)size.getHeight() - SessionProgressBar.this.margin * 2;
			setToolTipText(null);
			try{
				if(SessionProgressBar.this.sessionProgressModel.getNumTargetPages() == 0){
					return;
				}
				//int unit = width / OMRSessionProgressBar.this.model.getNumTargetPages();
				// unit = (unit == 0)? 1:unit;
				if(SessionProgressBar.this.margin <= y && y <= height){
					SessionProgressBar.this.mousePositionX = x;
					update();
					return;
				}else{
					clear();
				}
			}catch(ConcurrentModificationException ignore){}
		}
	}

	public boolean isIndeterminate(){
		return (0 <= this.indeterminate);
	}

	public void setIndeterminate(boolean isIndeterminate){
		if(isIndeterminate){
			this.indeterminate = 0.0f;
			if(this.executor == null){
				this.executor = Executors.newSingleThreadExecutor();
			}
			this.indeterminateAnimationFuture = this.executor.submit(
					new Runnable(){
						public void run(){
							while(0 <= SessionProgressBar.this.indeterminate){
								SessionProgressBar.this.indeterminate += 0.033f;
								update();
								try{
									Thread.sleep(33);
								}catch(InterruptedException ignore){}
							}
						}
					}
			);

		}else{
			if(this.indeterminateAnimationFuture != null){
				this.indeterminateAnimationFuture.cancel(true);
				this.indeterminate = -1.0f;
			}
		}
	}


	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Dimension size = getSize();
		int width = (int)size.getWidth() - this.margin * 2;
		int height = (int)size.getHeight() - this.margin * 2;

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int)size.getWidth(), (int)size.getHeight());

		//int FONT_WIDTH = 14;
		int FONT_HEIGHT = 14;
		//int statusMesageWidth = FONT_WIDTH * this.model.getStatusMessage().length();

		if(this.sessionProgressModel == null || 0 <= this.indeterminate || this.sessionProgressModel.getNumTargetPages() == 0){
			paintIndeterminate(g, size, width);
			g.setColor(Color.WHITE);
			g.drawString(this.sessionProgressModel.getStatusMessage(), this.margin, height/2+FONT_HEIGHT/2);
			return; 
		}
		int w = 0;
		int p = this.margin;
		g.setColor(this.sessionProgressModel.colTotal);
		g.fillRect(this.margin, this.margin, width, height);

		g.setColor(this.sessionProgressModel.colExternalized);
		g.fillRect(p += w, this.margin, (w = width * (this.sessionProgressModel.getNumExternalizingPages() + this.sessionProgressModel.getNumErrorPages()) / this.sessionProgressModel.getNumTargetPages()), height);
		g.setColor(this.sessionProgressModel.colSubmitted);
		g.fillRect(p += w, this.margin, (w = width * this.sessionProgressModel.getNumSubmittedPages() / this.sessionProgressModel.getNumTargetPages()), height);
		g.setColor(this.sessionProgressModel.colLeasedLocal);
		g.fillRect(p += w, this.margin, (w = width * this.sessionProgressModel.getNumLocalLeasedPages() / this.sessionProgressModel.getNumTargetPages()), height);
		g.setColor(this.sessionProgressModel.colLeasedRemote);
		g.fillRect(p += w, this.margin, (w = width * this.sessionProgressModel.getNumRemoteLeasedPages() / this.sessionProgressModel.getNumTargetPages()), height);
		g.setColor(this.sessionProgressModel.colPrepared);
		g.fillRect(p + w, this.margin, width - p - w + this.margin, height);

		g.setColor(this.sessionProgressModel.colError);		

		int unit = width / this.sessionProgressModel.getNumTargetPages() + 1;

		try{
			for(Map.Entry<String,PageTaskErrorEntry> taskErrorEntry : this.sessionProgressModel.getErrorPathToTaskErrorEntryMap().entrySet()){
				PageTaskErrorEntry e = taskErrorEntry.getValue();
				int errorIndex = e.getIndex();
				TaskError taskError = e.getTaskError();
				int x1 = this.margin + errorIndex * width / this.sessionProgressModel.getNumTargetPages();
				if(x1 <= this.mousePositionX && this.mousePositionX <= x1 + unit){
					g.setColor(Color.RED);
					g.fillRect(x1, this.margin, unit, height);
					g.setColor(this.sessionProgressModel.colError);
					String message = taskError.getSource().getFileResourceID().getRelativePath()+"="+taskError.getLocalizedMessage();
					SessionProgressBar.this.sessionProgressModel.setStatusMessage(message);
				}else{
					g.fillRect(x1, this.margin, unit, height);
				}
			}
			g.setColor(Color.WHITE);
			g.drawString(this.sessionProgressModel.getStatusMessage(), this.margin, height/2+FONT_HEIGHT/2);
		}catch(ConcurrentModificationException ignore){}
	}

	private void paintIndeterminate(Graphics g, Dimension size, int width) {
		g.setColor(Color.GRAY);
		g.fillRect(1, 1, (int)size.getWidth()-1, (int)size.getHeight()-1);
		g.setColor(Color.LIGHT_GRAY);
		if(((int)this.indeterminate) % 2 == 0){
			g.fillRect((int)(width*(this.indeterminate%1)), 0, width/10, (int)size.getHeight());
		}else{
			g.fillRect(width - (int)(width*(this.indeterminate%1)), 0, width/10, (int)size.getHeight());
		}
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, (int)size.getWidth()-1, (int)size.getHeight()-1);
	}

}
