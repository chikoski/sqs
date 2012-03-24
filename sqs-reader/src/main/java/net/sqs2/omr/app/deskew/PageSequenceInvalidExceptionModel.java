/*

 PageSequenceInvalidExceptionCore.java

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
package net.sqs2.omr.app.deskew;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.model.PageID;


public class PageSequenceInvalidExceptionModel extends PageFrameExceptionModel implements Serializable {
	final static private long serialVersionUID = 0L;

	Rectangle2D leftFooterArea, rightFooterArea;
	int leftValue, rightValue;

	public PageSequenceInvalidExceptionModel(PageID pageID, int width, int height, Point2D[] masterGuide,
			Point2D[] corners, Rectangle2D footerAreaLeft, int leftValue, Rectangle2D footerAreaRight, int rightValue) {
		super(pageID, width, height, masterGuide, corners);
		this.leftFooterArea = footerAreaLeft;
		this.rightFooterArea = footerAreaRight;
		this.leftValue = leftValue;
		this.rightValue = rightValue;
	}

	public PageSequenceInvalidExceptionModel(PageID pageID, int width, int height, Point2D[] masterGuide,
			Point2D[] corners, int leftValue, int rightValue) {
		super(pageID, width, height, masterGuide, corners);
		this.leftValue = leftValue;
		this.rightValue = rightValue;
	}

	@Override
	public String getLocalizedMessage() {
		return Messages.SESSION_ERROR_PAGESEQUENCEINVALID;
	}

	public Rectangle2D getLeftFooterArea() {
		return this.leftFooterArea;
	}

	public Rectangle2D getRightFooterArea() {
		return this.rightFooterArea;
	}

	public int getLeftValue() {
		return this.leftValue;
	}

	public int getRightValue() {
		return this.rightValue;
	}

	@Override
	public String toString() {
		if(this.leftFooterArea != null && this.rightFooterArea != null){
			return '{' + "left:{value:" + this.leftValue + ',' + "x:" + this.leftFooterArea.getX() + ',' + "y:"
			+ this.leftFooterArea.getY() + ',' + "w:" + this.leftFooterArea.getWidth() + ',' + "h:"
			+ this.leftFooterArea.getHeight() + "}," + "right:{value:" + this.rightValue + ',' + "x:"
			+ this.rightFooterArea.getX() + ',' + "y:" + this.rightFooterArea.getY() + ',' + "w:"
			+ this.rightFooterArea.getWidth() + ',' + "h:" + this.rightFooterArea.getHeight() + "}}";
		}else{
			return '{' + "left:" + this.leftValue + ',' + "right:" + this.rightValue+'}';
		}
	}
}
