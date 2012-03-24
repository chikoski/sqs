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
package net.sqs2.omr.logic;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.source.config.FrameConfig;
import net.sqs2.omr.source.config.PageGuideAreaConfig;
import net.sqs2.omr.source.config.SourceConfig;
import net.sqs2.omr.source.config.ValidationConfig;
import net.sqs2.omr.task.TaskResult;

public class PageFrameHandler{

	protected final SourceConfig sourceConfig;
	protected final BufferedImage image;
	protected final String pageID;
	
	FormMaster formMaster;
	PageGuideArea pageGuideArea;
	int guideAreaStartY = 0;
	int guideAreaEndY = 0;

	public PageFrameHandler(SourceConfig sourceConfig, BufferedImage image, String pageID){
		this(sourceConfig, image, pageID, null);
	}
	
	public PageFrameHandler(SourceConfig sourceConfig, BufferedImage image, String pageID, FormMaster formMaster){
		this.formMaster = formMaster;
		this.sourceConfig = sourceConfig;
		this.image = image;
		this.pageID = pageID;

		FrameConfig frameConfig = getFrameConfig();
		PageGuideAreaConfig guideAreaConfig = getGuideAreaConfig(frameConfig);
		this.pageGuideArea = new PageGuideArea(this.image, pageID, guideAreaConfig); 
	}

	public Point[] scanPageFrameCorners() throws PageFrameException {
		Point[] corners = this.pageGuideArea.scanCorners();
		if(formMaster != null){
			validate(getFrameConfig().getValidationConfig(), formMaster.getCorners(), corners);
		}
		return corners;
	}

	private Point[] validate(ValidationConfig validationConfig, Point[] masterCorners, Point[] corners)
	throws PageFrameException {
		double h1 = corners[0].distance(corners[1]);
		double h2 = corners[2].distance(corners[3]);
		double dh = Math.min(h1, h2) / Math.max(h1, h2);

		double v1 = corners[0].distance(corners[2]);
		double v2 = corners[1].distance(corners[3]);
		double dv = Math.min(v1, v2) / Math.max(v1, v2);

		if (dh < 1 - validationConfig.getHorizontalDistortion()) {
			throw new PageFrameException(new PageFrameDistortionExceptionCore(this.pageID, this.image.getWidth(), this.image.getHeight(), 
					masterCorners, corners, PageFrameDistortionExceptionCore.HORIZONTAL, (float)dh));
		}
		if (dv < 1 - validationConfig.getVerticalDistortion()) {
			throw new PageFrameException(new PageFrameDistortionExceptionCore(this.pageID, this.image.getWidth(), this.image.getHeight(), 
					masterCorners, corners, PageFrameDistortionExceptionCore.VERTICAL, (float)dv));
		}
		return corners;
	}
	
	public void checkUpsideDown(FormMaster omrMaster, PageSource pageSource) throws PageFrameException, PageSourceException {
		int headerDensity = pageSource.getGrayscaleDensity(omrMaster.getHeaderCheckArea());
		int footerDensity = pageSource.getGrayscaleDensity(omrMaster.getFooterCheckArea());
		if(headerDensity < footerDensity){
			return;
		}
		
		throw new PageFrameException(new PageUpsideDownExceptionCore(this.pageID, this.image.getWidth(), this.image.getHeight(), pageSource.getMasterCorners(), 
				pageSource.getBlockCenters(), 
				omrMaster.getHeaderCheckArea(),
				omrMaster.getFooterCheckArea(),
				headerDensity, footerDensity));
	}

	public void checkPageOrder(TaskResult result, FormMaster omrMaster, int pageNumber, PageSource pageSource) throws PageFrameException, PageSourceException {
		Rectangle footerAreaLeft = omrMaster.getFooterLeftRectangle();
		Rectangle footerAreaRight = omrMaster.getFooterRightRectangle();
		int left = pageSource.getGrayscaleDensity(footerAreaLeft);
		int right = pageSource.getGrayscaleDensity(footerAreaRight);

		if(pageNumber % 2 == 1){
			if(left > right){
				return;
			}
		}else{
			if(left < right){
				return;
			}
		}
		throw new PageFrameException(new PageSequenceInvalidExceptionCore(this.pageID, this.image.getWidth(), this.image.getHeight(), 
				pageSource.getMasterCorners(), pageSource.getBlockCenters(), footerAreaLeft, left, footerAreaRight, right));
	}

	protected PageGuideAreaConfig getGuideAreaConfig(FrameConfig frameConfig) {
		return frameConfig.getPageGuideAreaConfig();
	}

	protected FrameConfig getFrameConfig() {
		return this.sourceConfig.getFrameConfig();
	}
}

