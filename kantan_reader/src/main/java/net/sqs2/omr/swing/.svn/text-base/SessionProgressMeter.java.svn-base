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
package net.sqs2.omr.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class SessionProgressMeter extends SessionProgressPanel{
	private static final long serialVersionUID = 0L;

	int numRemoteClients;

	private static Stroke DASHED_STROKE1 = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_MITER, 10.0f, new float[]{2.0f, 1.0f}, 0.0f);
	private static Stroke DASHED_STROKE2 = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_MITER, 10.0f, new float[]{1.0f, 1.0f}, 0.0f);

	//static Resource resource = new Resource("messages");
	
	public SessionProgressMeter(SessionProgressModel model){
		super(model);		
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(this.sessionProgressModel == null){
			return; 
		}
		setPreferredSize(new Dimension(200,120));
		Dimension size = getSize();
		int width = (int)size.getWidth();
		int height = (int)size.getHeight();
		int fieldHeight = height / this.sessionProgressModel.COLORS.length;
		int fontSize = 12;
		int fieldWidth = 55;

		g.setColor(getBackground());
		g.fillRect(0, 0, width, height);

		for(int i = 0; i < this.sessionProgressModel.COLORS.length; i++){
			if((i == 1 && this.sessionProgressModel.getNumReusedPages() == 0)||
					(i == 4 && this.sessionProgressModel.getNumRemoteLeasedPages() == 0)||
					(i == 6 && 0 == this.sessionProgressModel.getNumErrorPages())){
				continue;
			}
			Color color = this.sessionProgressModel.COLORS[i];
			g.setColor(color);
			g.fillRect(fieldWidth - 2 , fieldHeight*(i+1)-fontSize, fontSize-1, fontSize-1);
			g.setColor(Color.BLACK);
			g.drawRect(fieldWidth - 2, fieldHeight*(i+1)-fontSize, fontSize-1, fontSize-1);
		}

		g.setColor(Color.BLACK);

		g.drawString(Messages.SESSION_PROCESS_NUMFILES_TOTAL,
				fieldWidth + fontSize, fieldHeight*1);

		g.drawLine(getCategorySeparatorLineXStart(fontSize) , getCategorySeparatorLineY(fieldHeight, fontSize, 3),
				getCategorySeparatorLineXEnd(fontSize, fieldWidth), getCategorySeparatorLineY(fieldHeight, fontSize, 3));

		if(0 < this.sessionProgressModel.getNumReusedPages()){
			g.drawString(Messages.SESSION_PROCESS_NUMFILES_REUSED,
					fieldWidth + fontSize, fieldHeight*2);
		}else{
			g.drawImage(ImageManager.TRIANGLE_DOWN_IMAGE,
					fieldWidth + fontSize * 2, fieldHeight*3/2, this);
		}

		g.drawString(Messages.SESSION_PROCESS_NUMFILES_PREPARED,
				fieldWidth + fontSize, fieldHeight*3);

		g.drawString(Messages.SESSION_PROCESS_NUMFILES_LEASEDLOCAL,
				fieldWidth + fontSize, fieldHeight*4);
		
		if(this.sessionProgressModel.isGridAvailable()){
			g.drawString(Messages.SESSION_PROCESS_NUMFILES_LEASEDREMOTE,
					fieldWidth + fontSize, fieldHeight*5);
		}else{
			g.drawImage(ImageManager.TRIANGLE_DOWN_IMAGE,
					fieldWidth + fontSize * 2, fieldHeight*9/2, this);
		}
		
		g.drawString(Messages.SESSION_PROCESS_NUMFILES_SUBMITTED,
				fieldWidth + fontSize, fieldHeight*6);
		
		if(0 < this.sessionProgressModel.getNumErrorPages()){
			g.drawString(Messages.SESSION_PROCESS_NUMFILES_ERROR,
					fieldWidth + fontSize, fieldHeight*7);
		}else{
			g.drawImage(ImageManager.TRIANGLE_DOWN_IMAGE,
					fieldWidth + fontSize * 2, fieldHeight*13/2, this);
		}
		
		g.drawString(Messages.SESSION_PROCESS_NUMFILES_EXTERNALIZED,
				fieldWidth + fontSize, fieldHeight*8);

		if(this.sessionProgressModel.getNumTargetPages() != 0){
			g.drawString(Integer.toString(this.sessionProgressModel.getNumTargetPages()),
					fontSize, fieldHeight*1);
		}else{
			g.drawString("-", fontSize, fieldHeight*1);
		}

		if(this.sessionProgressModel.getNumReusedPages() != 0){
			g.drawString(Integer.toString(this.sessionProgressModel.getNumReusedPages()),
					fontSize, fieldHeight*2);
		}else{

		}
		g.drawString(Integer.toString(this.sessionProgressModel.getNumPreparedPages()), fontSize, fieldHeight*3);		
		g.drawString(Integer.toString(this.sessionProgressModel.getNumLocalLeasedPages()), fontSize, fieldHeight*4);
		
		if(0 < this.sessionProgressModel.getNumRemoteLeasedPages()){
			g.drawString(Integer.toString(this.sessionProgressModel.getNumRemoteLeasedPages()), fontSize, fieldHeight*5);
		}else{
		}
		
		g.drawString(Integer.toString(this.sessionProgressModel.getNumSubmittedPages()), fontSize, fieldHeight*6);
		if(0 < this.sessionProgressModel.getNumErrorPages()){
			g.drawString(Integer.toString(this.sessionProgressModel.getNumErrorPages()), fontSize, fieldHeight*7);
		}else{
		}
		g.drawString(Integer.toString(this.sessionProgressModel.getNumExternalizingPages()), fontSize, fieldHeight*8);

		if(this.sessionProgressModel.getTimeElapsedString() != null){
			g.drawString(Messages.SESSION_PROCESS_TIMEELAPSED+": " + this.sessionProgressModel.getTimeElapsedString()+
					" "+Messages.SESSION_PROCESS_SEC, fieldWidth*4 - fontSize, fieldHeight*1);
		}

		if(this.sessionProgressModel.getPageParSecString() != null){
			g.drawString(Messages.SESSION_PROCESS_RATE+
					": "+ this.sessionProgressModel.getPageParSecString()+" "+Messages.SESSION_PROCESS_RATE_UNIT, fieldWidth*4 - fontSize, fieldHeight*2);
		}

		if(this.sessionProgressModel.getTimeRemainsString() != null){
			g.drawString(Messages.SESSION_PROCESS_TIMEREMAINS+
					": "+ this.sessionProgressModel.getTimeRemainsString()+" "+Messages.SESSION_PROCESS_SEC , fieldWidth*4 - fontSize, fieldHeight*3);
		}

		((Graphics2D)g).setStroke(SessionProgressMeter.DASHED_STROKE1);
		g.drawLine(getCategorySeparatorLineXStart(fontSize) , getCategorySeparatorLineY(fieldHeight, fontSize, 5),
				getCategorySeparatorLineXEnd(fontSize, fieldWidth), getCategorySeparatorLineY(fieldHeight, fontSize, 5));
		((Graphics2D)g).setStroke(SessionProgressMeter.DASHED_STROKE2);
		g.drawLine(getCategorySeparatorLineXStart(fontSize) , getCategorySeparatorLineY(fieldHeight, fontSize, 7),
				getCategorySeparatorLineXEnd(fontSize, fieldWidth), getCategorySeparatorLineY(fieldHeight, fontSize, 7));
		g.drawLine(getCategorySeparatorLineXStart(fontSize) , getCategorySeparatorLineY(fieldHeight, fontSize, 13),
				getCategorySeparatorLineXEnd(fontSize, fieldWidth), getCategorySeparatorLineY(fieldHeight, fontSize, 13));
		
	}

	private int getCategorySeparatorLineY(int fieldHeight, int fontSize, int step) {
		return fieldHeight*step/2-fontSize/2;
	}

	private int getCategorySeparatorLineXStart(int fontSize) {
		return fontSize - 3;
	}

	private int getCategorySeparatorLineXEnd(int fontSize, int fieldWidth) {
		return fieldWidth * 4 - fontSize * 2;
	}

	public void setNumRemoteClient(int numRemoteClients){
		this.numRemoteClients = numRemoteClients;
	}
}
