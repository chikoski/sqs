/**
 * 
 */
package net.sqs2.omr.session.service;

import net.sqs2.omr.app.deskew.DeskewedImageSource;

import net.sqs2.omr.app.deskew.ExtractedDeskewGuides;
import net.sqs2.omr.app.deskew.PageFrameException;
import net.sqs2.omr.app.deskew.PageImageSourceException;
import net.sqs2.omr.app.deskew.PageUpsideDownExceptionModel;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.model.SourceConfig;
import net.sqs2.omr.model.ValidationConfig;
import net.sqs2.omr.session.logic.PageProcessorSource;
import net.sqs2.util.VersionTag;

public class PageUpsideDownChecker extends Checker{
	
	public PageUpsideDownChecker(PageProcessorSource source, 
			ExtractedDeskewGuides extractedDeskewGuides,
			DeskewedImageSource deskewedImageSource){
		super(source, extractedDeskewGuides, deskewedImageSource);
	}

	public void check() throws PageFrameException, PageImageSourceException {
		ValidationConfig validationConfig = source.getConfiguration().getConfig().getPrimarySourceConfig().getFrameConfig().getValidationConfig();
		FormMaster formMaster = source.getFormMaster();
		if(new VersionTag(formMaster.getVersion()).isNewerThan(new VersionTag("2.1"))){
			checkUpsideDown();
		}else{
			if (validationConfig != null && validationConfig.isCheckUpsideDown() &&
					formMaster.getHeaderCheckArea() != null && formMaster.getFooterCheckArea() != null) {
				checkUpsideDown(formMaster, deskewedImageSource);
			}
		}
	}
	
	private void checkUpsideDown() throws PageFrameException, PageImageSourceException {
		int[] areaSizes = extractedDeskewGuides.getDeskewGuideAreaSizes();
		int headerDensity = areaSizes[0]+areaSizes[1];
		int footerDensity = areaSizes[2]+areaSizes[3];
		if (headerDensity > footerDensity) {
			return;
		}

		throw new PageFrameException(createPageUpsideDownExceptionModel(headerDensity, footerDensity));
	}

	private void checkUpsideDown(PageMaster pageMaster, DeskewedImageSource pageSource) throws PageFrameException, PageImageSourceException {
		int headerDensity = pageSource.calcMarkAreaDensity(pageMaster.getHeaderCheckArea());
		int footerDensity = pageSource.calcMarkAreaDensity(pageMaster.getFooterCheckArea());
		if (headerDensity < footerDensity) {
			return;
		}

		throw new PageFrameException(createPageUpsideDownExceptionModel(headerDensity, footerDensity));
	}

	private PageUpsideDownExceptionModel createPageUpsideDownExceptionModel(
			int headerDensity, int footerDensity) {
		return new PageUpsideDownExceptionModel(this.source.getPageTask().getPageID(),
				this.source.getPageImage().getWidth(),
				this.source.getPageImage().getHeight(), 
				this.source.getFormMaster().getDeskewGuideCenterPoints(),
				this.extractedDeskewGuides.getDeskewGuideCenterPoints(),
				this.source.getFormMaster().getHeaderCheckArea(),
				this.source.getFormMaster().getFooterCheckArea(), 
				headerDensity, footerDensity);
	}


}