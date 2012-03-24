package net.sqs2.omr.session.service;

import java.io.IOException;


import java.rmi.RemoteException;
import java.util.logging.Logger;

import net.sqs2.omr.app.deskew.DeskewedImageSource;
import net.sqs2.omr.app.deskew.ExtractedDeskewGuides;
import net.sqs2.omr.app.deskew.PageFrameException;
import net.sqs2.omr.app.deskew.PageImageSourceException;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.ConfigSchemeException;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTask;
import net.sqs2.omr.model.PageTaskException;
import net.sqs2.omr.model.PageTaskExceptionModel;
import net.sqs2.omr.model.PageTaskResult;
import net.sqs2.omr.model.SourceDirectoryConfiguration;
import net.sqs2.omr.session.logic.OMRProcessor;
import net.sqs2.omr.session.logic.DeskewProcessor;
import net.sqs2.omr.session.logic.PageProcessorSource;
import net.sqs2.sound.SoundManager;
import net.sqs2.util.FileResourceID;


public class PageTaskExecutable extends AbstractExecutable<OMRPageTask, SessionSourceServerDispatcher> {

	public PageTaskExecutable(OMRPageTask task, SessionSourceServerDispatcher dispacher) {
		super(task, dispacher);
	}
	
	private void logSucceed(long baseTimeStamp, PageTask pageTask) {
		long submitLap = System.currentTimeMillis() - baseTimeStamp;
		Logger.getLogger("executor").info("[[Process OK in " + submitLap + " msec]]\t" + pageTask);
		SoundManager.getInstance().play(AppConstants.TASK_EXECUTION_SOUND_FILENAME);
	}
	
	private void logFailure(long baseTimeStamp, PageTask pageTask) {
		long submitLap = System.currentTimeMillis() - baseTimeStamp;
		Logger.getLogger("executor").info("[[Process NG in " + submitLap + " msec]]\t" + pageTask+"\tresult="+pageTask.getPageTaskResult()+"\texception="+pageTask.getPageTaskException());
		SoundManager.getInstance().play(AppConstants.TASK_EXECUTION_SOUND_FILENAME);
	}

	private PageTaskException createPageTaskException(PageTask pageTask, Exception ex) {
		PageTaskException exception = new PageTaskException(new PageTaskExceptionModel(pageTask.getPageID(), ex.getLocalizedMessage()));
		return exception;
	}

	public void execute() throws RemoteException {
		long baseTimeStamp = System.currentTimeMillis();
		try{
			PageProcessorSource pageProcessorSource = createPageProcessorSource();
			
			ExtractedDeskewGuides extractedDeskewGuides = new DeskewProcessor(pageProcessorSource).call();
			DeskewedImageSource deskewedImageSource = new DeskewedImageSource(pageProcessorSource.getPageImage(),
					pageProcessorSource.getFormMaster().getDeskewGuideCenterPoints(), extractedDeskewGuides.getDeskewGuideCenterPoints());
			
			new PageUpsideDownChecker(pageProcessorSource, extractedDeskewGuides, deskewedImageSource).check();
			new PageSequenceChecker(pageProcessorSource, extractedDeskewGuides, deskewedImageSource).check();
			
			PageTaskResult pageTaskResult = OMRProcessor.updateFormAreaResults(pageProcessorSource, deskewedImageSource);
			
			task.setTaskResult(pageTaskResult);
			
			logSucceed(baseTimeStamp, task);
			return;
			
		} catch (PageFrameException ex) {
			logFailure(baseTimeStamp, task);
			task.setPageTaskException(ex);
		} catch (PageImageSourceException ex) {
			logFailure(baseTimeStamp, task);
			task.setPageTaskException(ex);
		} catch (ConfigSchemeException ex){
			task.setPageTaskException(createPageTaskException(task, ex));
		} catch (IOException ex) {
			task.setPageTaskException(createPageTaskException(task, ex));
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}

	protected PageProcessorSource createPageProcessorSource()
			throws RemoteException, IOException {
		long sessionID = task.getSessionID();
		PageID pageID = task.getPageID();
		int processingPageNumber = task.getProcessingPageNumber();
		FileResourceID pageMasterFileResourceID = task.getDefaultPageMasterFileResourceID();
		FormMaster formMaster = dispatcher.getFormMaster(sessionID, pageMasterFileResourceID);
		FileResourceID configFileResourceID = task.getConfigHandlerFileResourceID();
		SourceDirectoryConfiguration sourceDirectoryConfguration = dispatcher.getConfiguration(sessionID, configFileResourceID);
		byte[] imageByteArray = dispatcher.getFileContentByteArray(sessionID, pageID.getFileResourceID());
		
		PageProcessorSource pageProcessorSource = new PageProcessorSource(this.getTask(), formMaster, processingPageNumber, sourceDirectoryConfguration, imageByteArray);
		return pageProcessorSource;
	}

	/*
	private void check(PageProcessorSource pageProcessorSource, PageProcessorResult pageProcessorResult) throws PageFrameException,
			PageImageSourceException, IOException, ConfigSchemeException {
	}*/

	public void emit() throws RemoteException{
		long key = getDispatcher().getKey();
		long sessionID = this.getTask().getSessionID();
		dispatcher.getServer().submitPageTask(key, sessionID, this.getTask());
	}

}