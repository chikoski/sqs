package net.sqs2.omr.session.logic;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import net.sqs2.omr.app.deskew.ExtractedDeskewGuides;
import net.sqs2.omr.app.deskew.PageFrameException;
import net.sqs2.omr.app.deskew.PageFrameHandler;
import net.sqs2.omr.app.deskew.PageImageSourceException;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.ConfigImpl;
import net.sqs2.omr.model.ConfigSchemeException;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTask;
import net.sqs2.omr.model.PageTaskException;
import net.sqs2.omr.model.SourceConfig;

public class DeskewProcessor implements Callable<ExtractedDeskewGuides> {

	PageProcessorSource pageTaskProcessorSource;

	public DeskewProcessor(PageProcessorSource pageTaskProcessorSource){
		this.pageTaskProcessorSource = pageTaskProcessorSource;
	}

	private void logError(PageTaskException pageTaskException, PageID pageID) {
		Logger.getLogger("executor").warning(
				"[[Process NG]]\t" + pageID.getFileResourceID().getRelativePath() + " "
						+ pageTaskException.getExceptionModel().toString());
	}

	private SourceConfig getSourceConfig(PageTask pageTask)throws ConfigSchemeException {
		if (this.pageTaskProcessorSource.getConfiguration() == null) {
			throw new ConfigSchemeException("configration is null");
		}
		ConfigImpl config = (ConfigImpl) this.pageTaskProcessorSource.getConfiguration().getConfig();
		SourceConfig sourceConfig = null;
		sourceConfig = config.getSourceConfig(pageTask.getPageID().getFileResourceID().getRelativePath());
		return sourceConfig;
	}

	protected void setPageTaskException(PageTaskException pageTaskException) {
		PageID pageID = this.pageTaskProcessorSource.getPageTask().getPageID();
		logError(pageTaskException, pageID);
		this.pageTaskProcessorSource.getPageTask().setPageTaskException(pageTaskException);
	}

	public ExtractedDeskewGuides call()throws PageFrameException, PageImageSourceException, ConfigSchemeException, IOException{
		SourceConfig sourceConfig = getSourceConfig(this.pageTaskProcessorSource.getPageTask());
		BufferedImage pageImage = this.pageTaskProcessorSource.getPageImage();
		FormMaster defaultPageMaster = this.pageTaskProcessorSource.getFormMaster();
		PageFrameHandler pageFrameHandler = new PageFrameHandler(sourceConfig, pageImage, this.pageTaskProcessorSource.getPageTask().getPageID(), defaultPageMaster);
		return pageFrameHandler.extractDeskewGuidesAndValidate();
	}


}