/**
 * 
 */
package net.sqs2.omr.session.service;

import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;

import net.sqs2.omr.app.deskew.DeskewedImageSource;
import net.sqs2.omr.app.deskew.ExtractedDeskewGuides;
import net.sqs2.omr.app.deskew.PageFrameException;
import net.sqs2.omr.app.deskew.PageImageSourceException;
import net.sqs2.omr.app.deskew.PageSequenceInvalidExceptionModel;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.SourceConfig;
import net.sqs2.omr.model.ValidationConfig;
import net.sqs2.omr.session.logic.PageProcessorSource;

public class PageSequenceChecker extends Checker{
	
	public PageSequenceChecker(PageProcessorSource source,
			ExtractedDeskewGuides extractedDeskewGuides,
			DeskewedImageSource deskewedImageSource){
		super(source, extractedDeskewGuides, deskewedImageSource);
		
	}
	
	public void check()throws PageFrameException, PageImageSourceException{
		SourceConfig sourceConfig = this.source.getConfiguration().getConfig().getPrimarySourceConfig();
		FormMaster formMaster = this.source.getFormMaster(); 
		ValidationConfig validationConfig = sourceConfig.getFrameConfig().getValidationConfig();
		if (validationConfig != null && validationConfig.isCheckEvenOdd() &&
			formMaster.getFooterLeftRectangle() != null && formMaster.getFooterRightRectangle() != null){
			checkPageSequence();
		}
	}
	
	private void checkPageSequence() throws PageFrameException, PageImageSourceException {
		PageID pageID = source.getPageTask().getPageID();
		BufferedImage image = source.getPageImage();
		FormMaster formMaster = source.getFormMaster();
		int pageNumber = source.getProcessingPageNumber();
		Rectangle2D footerAreaLeft = formMaster.getFooterLeftRectangle();
		Rectangle2D footerAreaRight = formMaster.getFooterRightRectangle();
		int left = deskewedImageSource.calcMarkAreaDensity(footerAreaLeft);
		int right = deskewedImageSource.calcMarkAreaDensity(footerAreaRight);

		if (pageNumber == 1) {
			return;
		}
		if (pageNumber % 2 == 1) {
			if (left > right) {
				return;
			}
		} else {
			if (left < right) {
				return;
			}
		}
		throw new PageFrameException(new PageSequenceInvalidExceptionModel(pageID,
				image.getWidth(), image.getHeight(),
				deskewedImageSource.getDeskewMasterPoints(), 
				deskewedImageSource.getDeskewImagePoints(), footerAreaLeft, left, footerAreaRight, right));
	}

}