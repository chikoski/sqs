/**
 * 
 */
package net.sqs2.omr.session.logic;

import java.awt.Rectangle;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import net.sqs2.image.ImageFactory;
import net.sqs2.omr.app.deskew.ExtractedDeskewGuides;
import net.sqs2.omr.app.deskew.PageFrameException;
import net.sqs2.omr.app.deskew.DeskewedImageSource;
import net.sqs2.omr.app.deskew.PageImageSourceException;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.ConfigSchemeException;
import net.sqs2.omr.model.FormAreaResult;
import net.sqs2.omr.model.MarkRecognitionConfig;
import net.sqs2.omr.model.PageTask;
import net.sqs2.omr.model.PageTaskResult;
import net.sqs2.omr.model.SourceConfig;

public class OMRProcessor {
	public static PageTaskResult updateFormAreaResults(PageProcessorSource source,
			DeskewedImageSource deskewedImageSource)throws IOException, PageFrameException, PageImageSourceException, ConfigSchemeException {
		String formAreaImageFormat = AppConstants.FORMAREA_IMAGE_FORMAT;
		FormMaster formMaster = source.getFormMaster();
		int processingPageNumber = source.getProcessingPageNumber();
		List<FormArea> formAreaList = formMaster.getFormAreaListByPageIndex(processingPageNumber - 1);
		SourceConfig sourceConfig = source.getConfiguration().getConfig().getPrimarySourceConfig();
		
		PageTaskResult pageTaskResult = new PageTaskResult(deskewedImageSource.getDeskewImagePoints());

		for (FormArea formArea : formAreaList) {
			if (formArea.isMarkArea()) {
				updateMarkAreaResult(pageTaskResult, deskewedImageSource, formAreaImageFormat,
						sourceConfig.getMarkRecognitionConfig(), formArea);
			} else if (formArea.isTextArea()) {
				updateTextAreaResult(pageTaskResult, deskewedImageSource, formAreaImageFormat, 
						formArea);
			} else {
				// throw new FormMasterException("invalid formArea:" + formArea);
			}
		}
		return pageTaskResult;
	}
	
	private static void updateTextAreaResult(PageTaskResult result,
			DeskewedImageSource deskewedImageSource, String formAreaImageFormat,
			FormArea formArea) throws PageImageSourceException, IOException {
		Rectangle textAreaRectangle = createFormAraeRectangleWithMargin(formArea.getRect(), 0f, 0f,
				TextAreaPreviewImageConstants.HORIZONTAL_MARGIN,
				TextAreaPreviewImageConstants.HORIZONTAL_MARGIN, 1.0f);
		BufferedImage image = deskewedImageSource.cropImage(textAreaRectangle);
		result.addPageAreaResult(createFormAreaResult(formArea.getID(), formAreaImageFormat,
				ImageFactory.writeImage(formAreaImageFormat, image), 0));
	}

	private static void updateMarkAreaResult(PageTaskResult result,
			DeskewedImageSource deskewedImageSource, String formAreaImageFormat,
			MarkRecognitionConfig config, 
			FormArea formArea) throws PageImageSourceException, IOException, ConfigSchemeException {
		BufferedImage previewImage = deskewedImageSource.cropImage(createFormAreaPreviewRectangleWithMargin(formArea.getRect(), config.getResolutionScale()));
		int value = -1;
		
		Rectangle markAreaRectangle = createFormAraeRectangleWithMargin(formArea.getRect(),
				config.getHorizontalTrim(), config.getVerticalTrim(), 
				config.getHorizontalMargin(), config.getVerticalMargin(), config.getResolutionScale());
		if (MarkRecognitionConfig.VERTICAL_SLICES_AVERAGE_DENSITY.equals(config.getAlgorithm())) {
			value = deskewedImageSource.calcMarkAreaDensityWithVerticalSlices(markAreaRectangle, config.getMarkRecognitionDensityThreshold(), config.getResolutionScale());
		} else if (MarkRecognitionConfig.CONVOLUTION5x3_AVERAGE_DENSITY.equals(config.getAlgorithm())) {
			value = deskewedImageSource.calcConvolution5x3AverageMarkAreaDensity(markAreaRectangle, config.getNoMarkErrorSuppressionThreshold(), config.getResolutionScale());
		} else if (MarkRecognitionConfig.CONVOLUTION5x3_AVERAGE_DENSITY_WITH_DEBUGOUT.equals(config.getAlgorithm())) {
			value = deskewedImageSource.calcConvolution5x3AverageMarkAreaDensityWithDebugOut(markAreaRectangle, config.getNoMarkErrorSuppressionThreshold(), config.getResolutionScale());
		} else {
			throw new ConfigSchemeException("Not supported algorithm:"+config.getAlgorithm());
		}
		result.addPageAreaResult(createFormAreaResult(formArea.getID(), formAreaImageFormat,
				ImageFactory.writeImage(formAreaImageFormat, previewImage), value / 255.0f));
	}

	private static FormAreaResult createFormAreaResult(String id, String imageType, byte[] imageByteArray, float density) {
		return new FormAreaResult(id, imageType, imageByteArray, density);
	}

	private static Rectangle createFormAraeRectangleWithMargin(Rectangle rect, float horizontalTrim, float verticalTrim, float horizontalMargin, float verticalMargin, float marginScale) {
		return new Rectangle((int)(rect.x + (horizontalTrim - horizontalMargin) * marginScale), (int)(rect.y + (verticalTrim - verticalMargin) * marginScale), 
				(int)(rect.width + (horizontalTrim + horizontalMargin * 2) * marginScale), (int)(rect.height + (verticalTrim + verticalMargin * 2) * marginScale));
	}

	private static Rectangle createFormAreaPreviewRectangleWithMargin(Rectangle rect, float marginScale) {
		return createFormAraeRectangleWithMargin(rect, 0f, 0f,
				MarkAreaPreviewImageConstants.HORIZONTAL_MARGIN,
				MarkAreaPreviewImageConstants.VERTICAL_MARGIN, marginScale);
	}
}
