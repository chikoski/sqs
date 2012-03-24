/*

 PageFrameHandler.java

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

import java.awt.image.BufferedImage;

import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.model.DeskewGuideAreaConfig;
import net.sqs2.omr.model.FrameConfig;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.SourceConfig;
import net.sqs2.omr.model.ValidationConfig;

public class PageFrameHandler {

	protected final SourceConfig sourceConfig;
	protected final BufferedImage image;
	protected final PageID pageID;
	protected final PageMaster pageMaster;
	
	ExtractedDeskewGuides extractedDeskewGuides;
	
	public PageFrameHandler(SourceConfig sourceConfig, BufferedImage image, PageID pageID) {
		this(sourceConfig, image, pageID, null);
	}
	
	public PageFrameHandler(SourceConfig sourceConfig, BufferedImage image, PageID pageID,
			PageMaster pageMaster) {
		this.sourceConfig = sourceConfig;
		this.image = image;
		this.pageID = pageID;
		this.pageMaster = pageMaster;
	}

	public ExtractedDeskewGuides extractDeskewGuidesAndValidate() throws PageFrameException {
		FrameConfig frameConfig = this.sourceConfig.getFrameConfig();
		DeskewGuideAreaConfig guideAreaConfig = frameConfig.getDeskewGuideAreaConfig();
		DeskewGuideExtractor deskewGuideArea = new DeskewGuideExtractor(this.image, pageID, guideAreaConfig);
		this.extractedDeskewGuides = deskewGuideArea.extractDeskewGuides();
		Point2D[] deskewGuideCenterPoints = this.extractedDeskewGuides.getDeskewGuideCenterPoints(); 
		validate(deskewGuideCenterPoints);
		return this.extractedDeskewGuides;
	}
	
	private void validate(Point2D[] deskewGuideCenterPoints)throws PageFrameException{
		if (this.pageMaster != null) {
			validate(this.sourceConfig.getFrameConfig().getValidationConfig(), 
					this.pageMaster.getDeskewGuideCenterPoints(), deskewGuideCenterPoints);
		}
	}

	private Point2D[] validate(ValidationConfig validationConfig, Point2D[] masterCorners, Point2D[] corners) throws PageFrameException {
		double h1 = corners[0].distance(corners[1]);
		double h2 = corners[2].distance(corners[3]);
		double h12max = Math.max(h1, h2);

		double v1 = corners[0].distance(corners[2]);
		double v2 = corners[1].distance(corners[3]);
		double v12max = Math.max(v1, v2);
		
		if(h12max == 0 || v12max == 0){
			return corners;
		}
		
		double dh = Math.min(h1, h2) / h12max;
		double dv = Math.min(v1, v2) / v12max;

		if (dh < 1 - validationConfig.getHorizontalDistortion()) {
			throw new PageFrameException(new PageFrameDistortionExceptionModel(this.pageID, 
					this.image.getWidth(), 
					this.image.getHeight(), masterCorners, corners,
					PageFrameDistortionExceptionModel.HORIZONTAL_ERROR_TYPE, (float) dh));
		}
		if (dv < 1 - validationConfig.getVerticalDistortion()) {
			throw new PageFrameException(new PageFrameDistortionExceptionModel(this.pageID,
					this.image.getWidth(), 
					this.image.getHeight(), masterCorners, corners,
					PageFrameDistortionExceptionModel.VERTICAL_ERRORR_TYPE, (float) dv));
		}
		return corners;
	}

}
