/*

 PageFrameDistortionExceptionCore.java

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

 Created on 2006/01/02

 */
package net.sqs2.omr.app.deskew;

import java.awt.geom.Point2D;
import java.io.Serializable;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.model.PageID;

public class PageFrameDistortionExceptionModel extends PageFrameExceptionModel implements Serializable {
	public static final long serialVersionUID = 1L;

	public static final int UNDEFINED_ERROR_TYPE = 0;

	public static final int HORIZONTAL_ERROR_TYPE = 1;

	public static final int VERTICAL_ERRORR_TYPE = 2;

	int errorType;
	float distortion;

	public PageFrameDistortionExceptionModel(PageID pageID, int width, int height, Point2D[] masterCorners,
			Point2D[] corners, int errorType, float distortion) {
		super(pageID, width, height, masterCorners, corners);
		this.errorType = errorType;
		this.distortion = distortion;
	}

	@Override
	public String getLocalizedMessage() {
		return Messages.SESSION_ERROR_PAGEFRAMEDISTORTION;
	}

	public int getErrorType() {
		return this.errorType;
	}

}
