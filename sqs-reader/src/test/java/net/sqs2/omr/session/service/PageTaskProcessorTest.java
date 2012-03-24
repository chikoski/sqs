package net.sqs2.omr.session.service;

import static org.testng.Assert.assertEquals;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import net.sqs2.image.ImageFactory;
import net.sqs2.net.ClassURLStreamHandlerFactory;
import net.sqs2.omr.app.deskew.ExtractedDeskewGuides;
import net.sqs2.omr.app.deskew.PageFrameException;
import net.sqs2.omr.app.deskew.PageFrameHandler;
import net.sqs2.omr.app.deskew.DeskewedImageSource;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.FormMasterException;
import net.sqs2.omr.master.FormMasterFactory;
import net.sqs2.omr.model.ConfigSchemeException;
import net.sqs2.omr.model.FormAreaResult;
import net.sqs2.omr.model.MarkRecognitionConfig;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.model.PageAreaResult;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTask;
import net.sqs2.omr.model.PageTaskException;
import net.sqs2.omr.model.PageTaskResult;
import net.sqs2.omr.model.SourceConfig;
import net.sqs2.omr.model.SourceDirectoryConfiguration;
import net.sqs2.omr.model.SourceDirectoryConfigurationFactory;
import net.sqs2.omr.model.SourceDirectoryConfigurationFactoryImpl;
import net.sqs2.omr.session.init.MergedFormMasterFactory;
import net.sqs2.omr.session.logic.PageProcessorSource;
import net.sqs2.omr.session.service.LocalSessionSourceServer;
import net.sqs2.omr.session.service.PageTaskExecutable;
import net.sqs2.omr.session.service.SessionSourceServerDispatcher;
import net.sqs2.omr.util.JarExtender;
import net.sqs2.util.FileResourceID;
import net.sqs2.util.FileUtil;
import net.sqs2.util.PathUtil;

import org.apache.commons.io.FilenameUtils;
import org.testng.annotations.Test;


public class PageTaskProcessorTest {
	
	static{
		try{
			URL.setURLStreamHandlerFactory(ClassURLStreamHandlerFactory.getSingleton());
		}catch(Error ignore){}
	}

	static final long KEY = 0L;
	
	private void assertMarkValues(String id, int width, int height, float density, FormAreaResult formAreaResult)throws IOException{
		assertEquals(id, formAreaResult.getID());
		BufferedImage markarea1 = ImageFactory.createImage(formAreaResult.getImageType(), formAreaResult.getImageByteArray(), 0);
		assertEquals(width, markarea1.getWidth());
		assertEquals(height, markarea1.getHeight());
		assertEquals(density, formAreaResult.getDensity(), 0);
	}
	
	private FileResourceID createFileResourceID(File sourceDirectoryRoot, String relativePath)throws IOException{
		File target = new File(sourceDirectoryRoot, relativePath);
		return new FileResourceID(PathUtil.getRelativePath(target, sourceDirectoryRoot, File.separatorChar), target.lastModified());
	}

	private PageTask executePageTask(
			File sourceDirectoryRootFile,
			long key,
			long sessionID, 
			String formMasterFilePath, 
			String configFilePath, 
			String imageFilePath, 
			int indexOfPageInImageFile, 
			int numPagesInImageFile, 
			int processingPageNumber)throws IOException, FormMasterException, ConfigSchemeException, PageFrameException{

		new JarExtender().extend(new String[]{configFilePath}, sourceDirectoryRootFile);
		new File(sourceDirectoryRootFile, configFilePath).deleteOnExit();

		FileResourceID formMasterFileResourceID = createFileResourceID(sourceDirectoryRootFile, formMasterFilePath); 
		FileResourceID configFileResourceID = createFileResourceID(sourceDirectoryRootFile, configFilePath);
		FileResourceID imageFileResourceID = createFileResourceID(sourceDirectoryRootFile, imageFilePath);
		
		PageID pageID = new PageID(imageFileResourceID, indexOfPageInImageFile, numPagesInImageFile);
		OMRPageTask pageTask = new OMRPageTask(sourceDirectoryRootFile.getAbsolutePath(), 
				formMasterFileResourceID, 
				imageFileResourceID,
				processingPageNumber,
				pageID, 
				sessionID);
		
		FormMasterFactory formMasterFactory = new MergedFormMasterFactory();
		FormMaster formMaster = formMasterFactory.create(sourceDirectoryRootFile, formMasterFilePath);

		SourceDirectoryConfigurationFactory sourceDirectoryConfigurationFactory = new SourceDirectoryConfigurationFactoryImpl();
		SourceDirectoryConfiguration config = sourceDirectoryConfigurationFactory.create(sourceDirectoryRootFile, configFileResourceID);
		
		config.getConfig().getSourceConfig(imageFilePath, 1).getMarkRecognitionConfig().setAlgorithm(MarkRecognitionConfig.CONVOLUTION5x3_AVERAGE_DENSITY_WITH_DEBUGOUT);
		byte[] imageBytes = FileUtil.getBytes(new File(sourceDirectoryRootFile, imageFilePath));
		BufferedImage pageImage = ImageFactory.createImage(FilenameUtils.getExtension(imageFilePath), imageBytes, 0);
		SourceConfig sourceConfig = config.getConfig().getSourceConfig(imageFilePath, processingPageNumber);
		PageFrameHandler pageFrameHandler = new PageFrameHandler(sourceConfig, pageImage, pageID);
		
		Point2D[] deskewGuideCenterPoints = formMaster.getDeskewGuideCenterPoints();
		ExtractedDeskewGuides extractedDeskewGuides = pageFrameHandler.extractDeskewGuidesAndValidate();
		Point2D[] extractedDeskewGuideCenterPoints = extractedDeskewGuides.getDeskewGuideCenterPoints();

		//PageProcessorSource sources = new PageProcessorSource(pageTask, formMaster, processingPageNumber, config, imageBytes);
		//DeskewedImageSource deskewedImageSource = new DeskewedImageSource(pageImage, deskewGuideCenterPoints, extractedDeskewGuideCenterPoints);
		
		PageTaskExecutable executable = new PageTaskExecutable(pageTask, new SessionSourceServerDispatcher(){

			@Override
			public byte[] getFileContentByteArray(long sessionID,
					FileResourceID fileResourceID) throws RemoteException,
					IOException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public FormMaster getFormMaster(long sessionID,
					FileResourceID fileResourceID) throws RemoteException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public SourceDirectoryConfiguration getConfiguration(
					long sessionID, FileResourceID fileResourceID)
					throws RemoteException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setInitialized() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean hasInitialized() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public long getKey() {
				return KEY;
			}

			@Override
			public boolean isRemote() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public LocalSessionSourceServer getLocalServer() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public LocalSessionSourceServer getServer() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void close() {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		executable.execute();
		
		return pageTask;
	}
	

	@Test
	public void testOMRLogicOnA001()throws Exception{
		
		File sourceDirectoryRootFile = new File("src/test/resources/test0");
		String formMasterFilePath = "form.pdf";
		String configFilePath = "config.xml"; 
		String imageFilePath = "a001.tif";
		int indexOfPageInImageFile = 0;
		int numPagesInImageFile = 1;
		
		int processingPageNumber = 1;
		long sessionID = 0;
		
		PageTask pageTask = executePageTask(
				sourceDirectoryRootFile, KEY, 
				sessionID, formMasterFilePath, configFilePath, imageFilePath, 
				indexOfPageInImageFile, numPagesInImageFile,
				processingPageNumber);
		
		PageTaskResult result = pageTask.getPageTaskResult();
		PageTaskException exception = pageTask.getPageTaskException();
		
		assertNull(exception);
		
		Point2D[] deskewGuideCenterPoints = result.getDeskewGuideCenterPoints();
		assertEquals(new Point(226,87), deskewGuideCenterPoints[0]);
		assertEquals(new Point(1018,95), deskewGuideCenterPoints[1]);
		assertEquals(new Point(199,1642), deskewGuideCenterPoints[2]);
		assertEquals(new Point(991,1650), deskewGuideCenterPoints[3]);
		
		List<PageAreaResult> pageAreaResultList = result.getPageAreaResultList();
		assertEquals(2, pageAreaResultList.size());
		
		FormAreaResult formAreaResult0 = (FormAreaResult)pageAreaResultList.get(0);
		assertEquals("textarea1", formAreaResult0.getID());
		BufferedImage textarea1 = ImageFactory.createImage(formAreaResult0.getImageType(), formAreaResult0.getImageByteArray(), 0);
		assertEquals(454, textarea1.getWidth());
		assertEquals(84, textarea1.getHeight());
		
		FormAreaResult formAreaResult1 = (FormAreaResult)pageAreaResultList.get(1);
		assertEquals("textarea2", formAreaResult1.getID());
		BufferedImage textarea2 = ImageFactory.createImage(formAreaResult1.getImageType(), formAreaResult1.getImageByteArray(), 0);
		assertEquals(454, textarea2.getWidth());
		assertEquals(84, textarea2.getHeight());

	}

	@Test
	public void testOMRLogicOnA002()throws Exception{
		
		File sourceDirectoryRoot = new File("src/test/resources/test0");
		String formMasterFilePath = "form.pdf";
		String configFilePath = "config.xml"; 
		String imageFilePath = "a002.tif";
		int indexOfPageInImageFile = 0;
		int numPagesInImageFile = 1;
		int processingPageNumber = 2;
		long sessionID = 0;
		
		PageTask pageTask = executePageTask(sourceDirectoryRoot, KEY, sessionID, formMasterFilePath, configFilePath, imageFilePath, indexOfPageInImageFile, numPagesInImageFile,
				processingPageNumber);
		
		assertNull(pageTask.getPageTaskException());
		
		PageTaskResult pageTaskResult = pageTask.getPageTaskResult();
		assertNotNull(pageTaskResult);
		
		Point2D[] deskewGuideCenterPoints = pageTaskResult.getDeskewGuideCenterPoints();
		assertEquals(new Point(229,93), deskewGuideCenterPoints[0]);
		assertEquals(new Point(1020,90), deskewGuideCenterPoints[1]);
		assertEquals(new Point(224,1646), deskewGuideCenterPoints[2]);
		assertEquals(new Point(1016,1645), deskewGuideCenterPoints[3]);
		
		List<PageAreaResult> pageAreaResultsList = pageTaskResult.getPageAreaResultList();
		assertEquals(76, pageAreaResultsList.size()); // this image contains 76 answer fields
		assertMarkValues("mark3/0", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(0)); // not marked
		assertMarkValues("mark3/1", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(1)); // not marked
		assertMarkValues("mark3/2", 13, 24, 0.3803921639919281f, (FormAreaResult)pageAreaResultsList.get(2)); // marked
		assertMarkValues("mark3/3", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(3)); // not marked
	}

	@Test
	public void testOMRLogicOnA003()throws Exception{
		
		File sourceDirectoryRoot = new File("src/test/resources/test4");
		String formMasterFilePath = "form.pdf";
		String configFilePath = "config.xml"; 
		String imageFilePath = "04.tif";
		int indexOfPageInImageFile = 0;
		int numPagesInImageFile = 1;
		int processingPageNumber = 2;
		long sessionID = 0;
		
		PageTask pageTask = executePageTask(sourceDirectoryRoot, KEY, sessionID, formMasterFilePath, configFilePath, imageFilePath, indexOfPageInImageFile, numPagesInImageFile,
				processingPageNumber);
		PageTaskResult result = pageTask.getPageTaskResult();
		
		assertNull(pageTask.getPageTaskException());
		
		PageTaskResult pageTaskResult = pageTask.getPageTaskResult();
		assertNotNull(pageTaskResult);
		
		Point2D[] deskewGuideCenterPoints = pageTaskResult.getDeskewGuideCenterPoints();
		assertEquals(new Point(207,84), deskewGuideCenterPoints[0]);
		assertEquals(new Point(1013,84), deskewGuideCenterPoints[1]);
		assertEquals(new Point(207,1678), deskewGuideCenterPoints[2]);
		assertEquals(new Point(1013,1678), deskewGuideCenterPoints[3]);
		
		List<PageAreaResult> pageAreaResultsList = pageTaskResult.getPageAreaResultList();
		assertEquals(18, pageAreaResultsList.size()); // this image contains 18 answer fields
		assertMarkValues("mark2/0", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(0)); // not marked
		assertMarkValues("mark2/1", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(1)); // not marked
		assertMarkValues("mark2/2", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(2)); // marked
		assertMarkValues("mark2/3", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(3)); // not marked
		assertMarkValues("mark2/4", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(4)); // not marked
		assertMarkValues("mark2/5", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(5)); // not marked

		assertMarkValues("mark3/0", 13, 24, 0.6901960968971252f, (FormAreaResult)pageAreaResultsList.get(6)); // not marked
		assertMarkValues("mark3/1", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(7)); // not marked
		assertMarkValues("mark3/2", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(8)); // marked
		assertMarkValues("mark3/3", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(9)); // not marked
		assertMarkValues("mark3/4", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(10)); // not marked
		assertMarkValues("mark3/5", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(11)); // not marked

		assertMarkValues("mark4/0", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(12)); // not marked
		assertMarkValues("mark4/1", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(13)); // not marked
		assertMarkValues("mark4/2", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(14)); // marked
		assertMarkValues("mark4/3", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(15)); // not marked
		assertMarkValues("mark4/4", 13, 24, 1.0f, (FormAreaResult)pageAreaResultsList.get(16)); // not marked

		assertEquals("textarea5", pageAreaResultsList.get(17).getID());
	}

}
