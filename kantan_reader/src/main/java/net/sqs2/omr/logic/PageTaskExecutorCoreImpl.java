/*

 PageTaskExecutorCoreImpl.java

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

 Created on 2007/01/11

 */
package net.sqs2.omr.logic;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Logger;

import net.sqs2.image.ImageFactory;
import net.sqs2.image.ImageUtil;
import net.sqs2.omr.app.MarkReaderConstants;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.SourceDirectoryConfiguration;
import net.sqs2.omr.source.config.ConfigImpl;
import net.sqs2.omr.source.config.MarkRecognitionConfig;
import net.sqs2.omr.source.config.SourceConfig;
import net.sqs2.omr.source.config.ValidationConfig;
import net.sqs2.omr.task.ExecutableTaskWrapper;
import net.sqs2.omr.task.FormAreaCommand;
import net.sqs2.omr.task.PageTask;
import net.sqs2.omr.task.TaskError;
import net.sqs2.omr.task.TaskException;
import net.sqs2.omr.task.TaskResult;
import net.sqs2.omr.task.broker.TaskExecutorLogic;

public class PageTaskExecutorCoreImpl implements TaskExecutorLogic{

	public PageTaskExecutorCoreImpl(){
	}

	public void execute(ExecutableTaskWrapper executable)throws RemoteException{
		long base = System.currentTimeMillis();
		PageTask pageTask = executable.getTask();

		try{
			FormMaster formMaster = (FormMaster)executable.getTaskExecutorEnv().getPageMaster(pageTask.getSessionID(), pageTask);

			BufferedImage pageImage = retrievePageBufferedImage(executable);
			SourceDirectoryConfiguration configuration = executable.getConfiguration();
			if(configuration == null){
				throw new RuntimeException("use default page task");
			}

			ConfigImpl config = (ConfigImpl)configuration.getConfig(); 
			SourceConfig sourceConfig = config.getSourceConfig(pageTask);
			String formAreaImageFormat = MarkReaderConstants.FORMAREA_IMAGE_FORMAT;

			PageFrameHandler pageFrameHandler = new PageFrameHandler(sourceConfig, pageImage, executable.getTask().getPageID().toString(), formMaster);
			Point[] pageCorners = pageFrameHandler.scanPageFrameCorners();
			PageSource pageSource = createPageSource(formMaster, pageImage, pageCorners);
			
			ValidationConfig validationConfig = sourceConfig.getFrameConfig().getValidationConfig();
			if(validationConfig != null && validationConfig.isCheckUpsideDown()){
				pageFrameHandler.checkUpsideDown(formMaster, pageSource);
			}
			
			TaskResult result = new TaskResult(pageCorners);
			
			if(validationConfig != null && validationConfig.isCheckEvenOdd()){
				pageFrameHandler.checkPageOrder(result, formMaster, pageTask.getPageNumber(), pageSource);
			}
			
			setupFormAreaCommandList(result, formMaster, pageTask.getPageNumber(), pageSource, formAreaImageFormat, 
					sourceConfig.getMarkRecognitionConfig());
			pageTask.setTaskResult(result);
			long submitLap = System.currentTimeMillis() - base;
			Logger.getLogger("executor").info("[[Process OK in "+submitLap+" msec]]\t"+pageTask);			
		}catch(PageFrameException ex){
			setPageTaskError(executable, ex);
		}catch(PageSourceException ex){
			setPageTaskError(executable, ex);
		}catch(RemoteException ex){
			executable.getTaskExecutorEnv().setConnected(false);
			throw ex;
		}catch(IOException ex){
			ex.printStackTrace();
			pageTask.setTaskError(new TaskError(pageTask.getPageID(), ex.getMessage()));
		}
	}

	private static void setPageTaskError(ExecutableTaskWrapper executable, TaskException pageTaskException) {
		PageTask pageTask = executable.getTask();
		PageID pageID = pageTask.getPageID();
		Logger.getLogger("executor").warning("[[Process NG]]\t"+pageID.getFileResourceID().getRelativePath()+" "+pageTaskException.getExceptionCore().toString());
		pageTask.setTaskError(new TaskError(pageTask.getPageID(), pageTaskException.getExceptionCore()));
	}

	private static BufferedImage retrievePageBufferedImage(ExecutableTaskWrapper taskWrapper) throws RemoteException,IOException{
		PageID pageID = taskWrapper.getTask().getPageID();
		long sessionID = taskWrapper.getTask().getSessionID();
		byte[] imageByteArray = taskWrapper.getTaskExecutorEnv().getImageByteArray(sessionID, pageID.getFileResourceID());
		String type = ImageUtil.getType(pageID.getExtension());
		if(imageByteArray == null){
			throw new RuntimeException(pageID.toString());
		}

		BufferedImage pageImage = ImageFactory.createImage(imageByteArray, pageID.getIndex(), type);
		return pageImage;
	}

	private static PageSource createPageSource(PageMaster pageMaster, BufferedImage pageImage, Point[] pageCorners) {
		return new PageSource(pageImage, pageMaster.getCorners(), pageCorners);
	}
	
	private static void setupFormAreaCommandList(TaskResult result, FormMaster pageMaster, int pageNumber, PageSource pageSource,
			String formAreaImageFormat, MarkRecognitionConfig config)throws IOException, PageSourceException{
		List<FormArea> formAreaList = pageMaster.getFormAreaListByPageIndex(pageNumber - 1);
		int hmargin = config.getHorizontalMargin();
		int vmargin = config.getVerticalMargin();
		for(FormArea formArea: formAreaList){
			if(formArea.isMarkArea()){
				BufferedImage image = pageSource.cropImage(getPreviewMarginRect(formArea.getRect()));
				int value = -1; 
				if(MarkRecognitionConfig.AVERAGE_DENSITY.equals(config.getAlgorithm())){
					value  = pageSource.getGrayscaleDensityWithVerticalFilter(getMarginRect(formArea.getRect(), hmargin, vmargin), config.getResolutionScale());
				}else if(MarkRecognitionConfig.CONVOLUTION3X3_AVERAGE_DENSITY.equals(config.getAlgorithm())){
					value = pageSource.getGrayscaleDensityWithFilter(getMarginRect(formArea.getRect(), hmargin, vmargin), config.getNoMarkThreshold(), config.getResolutionScale());
				}else{
				 
				}
				result.addFormAreaCommand(createFormAreaCommand(formArea.getID(), formAreaImageFormat,
						ImageFactory.writeImage(image, formAreaImageFormat), value/255.0f));
			}else if(formArea.isTextArea()){
				BufferedImage image = pageSource.cropImage(getMarginRect(formArea.getRect(), 
						TextAreaPreviewImageConstants.HORIZONTAL_MARGIN,
						TextAreaPreviewImageConstants.HORIZONTAL_MARGIN
						));
				result.addFormAreaCommand(createFormAreaCommand(formArea.getID(), formAreaImageFormat,
						ImageFactory.writeImage(image, formAreaImageFormat), 0));
			}else{
				throw new RuntimeException("invalid formArea:"+formArea);
			}
		}
	}

	private static FormAreaCommand createFormAreaCommand(String id, String imageType, byte[] imageByteArray, float density){
		return new FormAreaCommand(id, imageType, imageByteArray, density);
	}

	private static Rectangle getMarginRect(Rectangle rect, int hmargin, int vmargin){
		return new Rectangle(rect.x - hmargin, rect.y - vmargin, rect.width + hmargin * 2, rect.height + vmargin * 2);
	}
	
	public static Rectangle getPreviewMarginRect(Rectangle rect){
		return getMarginRect(rect,
				MarkAreaPreviewImageConstants.HORIZONTAL_MARGIN,
				MarkAreaPreviewImageConstants.VERTICAL_MARGIN);
	}

}
