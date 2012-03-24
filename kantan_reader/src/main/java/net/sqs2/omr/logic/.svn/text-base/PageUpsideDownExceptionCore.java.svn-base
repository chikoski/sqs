/*

 PageUpsideDownExceptionCore.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on Apr 7, 2007

 */

package net.sqs2.omr.logic;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import net.sqs2.omr.swing.Messages;

public class PageUpsideDownExceptionCore extends PageFrameExceptionCore implements Serializable{
	final static private long serialVersionUID = 0L;

	int headerDensity;
	int footerDensity;
	Rectangle headerCheckArea, footerCheckArea;
	
	public PageUpsideDownExceptionCore(String pageID, int width, int height, Point[] masterGuide, Point[] corners, 
			Rectangle headerCheckArea, Rectangle footerCheckArea, int headerDensity, int footerDensity){
		super(pageID, width, height, masterGuide, corners);
		this.headerCheckArea = headerCheckArea;
		this.footerCheckArea = footerCheckArea;
		this.headerDensity = headerDensity;
		this.footerDensity = footerDensity;
	}
	
	public String getLocalizedMessage(){
		return Messages.SESSION_ERROR_PAGEUPSIDEDOWN;
	}

	@Override
	public String getDescription(){
		return "density:(h="+headerDensity+", f="+footerDensity+")";
	}

	public Rectangle getHeaderCheckArea() {
    	return headerCheckArea;
    }

	public Rectangle getFooterCheckArea() {
    	return footerCheckArea;
    }

}
