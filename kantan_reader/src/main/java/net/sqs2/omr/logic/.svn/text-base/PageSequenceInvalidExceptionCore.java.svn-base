/*

 PageOrderExceptionCore.java

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

public class PageSequenceInvalidExceptionCore extends PageFrameExceptionCore implements Serializable {
	final static private long serialVersionUID = 0L;

	Rectangle leftFooterArea, rightFooterArea;
	int leftValue, rightValue;

	public PageSequenceInvalidExceptionCore(String pageID, int width, int height, Point[] masterGuide, Point[] corners,
	        Rectangle footerAreaLeft, int left, Rectangle footerAreaRight,
	        int right) {
		super(pageID, width, height, masterGuide, corners);
		this.leftFooterArea = footerAreaLeft;
		this.rightFooterArea = footerAreaRight;
		this.leftValue = left;
		this.rightValue = right;
	}

	public String getLocalizedMessage(){
		return Messages.SESSION_ERROR_PAGESEQUENCEINVALID;
	}

	public Rectangle getLeftFooterArea() {
    	return leftFooterArea;
    }

	public Rectangle getRightFooterArea() {
    	return rightFooterArea;
    }

	public int getLeftValue() {
		return leftValue;
	}

	public int getRightValue() {
		return rightValue;
	}

	@Override
	public String getDescription() {
		return '{'+
		"left:{value:" + leftValue +','+
		"x:" + leftFooterArea.x +','+
		"y:" + leftFooterArea.y +','+
		"w:" + leftFooterArea.width +','+
		"h:" + leftFooterArea.height +"},"+
		"right:{value:" + rightValue+','+
		"x:"+rightFooterArea.x +','+
		"y:"+rightFooterArea.y +','+
		"w:"+rightFooterArea.width +','+
		"h:"+rightFooterArea.height +
		"}}";
	}
}
