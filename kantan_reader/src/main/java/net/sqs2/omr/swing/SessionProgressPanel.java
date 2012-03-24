/*

 SessionProgressPanel.java

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

 Created on 2007/2/11

 */
package net.sqs2.omr.swing;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.sqs2.omr.session.event.MarkReaderSessionMonitorAdapter;
import net.sqs2.omr.task.PageTask;

public class SessionProgressPanel extends JPanel{
	private static final long serialVersionUID = 0L;

	protected SessionProgressModel sessionProgressModel;
	//boolean isNowUpdating = false;
	
	public SessionProgressPanel(){
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	public SessionProgressPanel(SessionProgressModel model) {
		this.sessionProgressModel = model;
		this.sessionProgressModel.addSessionMonitor(new MarkReaderSessionMonitorAdapter(){
			public void notifyTaskProduced(PageTask pageTask) {
				update();
			}

			public void notifyStoreTask(PageTask pageTask) {
				update();
			}
		});
		this.setBackground(Color.WHITE);
	}
	
	public SessionProgressModel getSessionProgressModel(){
		return this.sessionProgressModel;
	}

	protected synchronized void update(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				repaint();
			}
		});
	}

}