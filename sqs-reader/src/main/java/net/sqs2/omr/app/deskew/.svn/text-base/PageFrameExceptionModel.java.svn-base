/*

 PageFrameExceptionCore.java

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

 Created on 2009/01/11

 */
package net.sqs2.omr.app.deskew;

import java.awt.geom.Point2D;
import java.io.Serializable;

import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageImageExceptionModel;

public abstract class PageFrameExceptionModel extends PageImageExceptionModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	protected Point2D[] masterCorners;
	protected Point2D[] corners;

	public PageFrameExceptionModel(PageID pageID, int width, int height,
			Point2D[] masterCorners,
			Point2D[] corners) {
		super(pageID, width, height);
		this.masterCorners = masterCorners;
		this.corners = corners;
	}

	public Point2D[] getMasterCorners() {
		return this.masterCorners;
	}

	public Point2D[] getCorners() {
		return this.corners;
	}

}
