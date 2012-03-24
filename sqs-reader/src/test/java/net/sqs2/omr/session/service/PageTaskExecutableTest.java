package net.sqs2.omr.session.service;

import static org.testng.Assert.assertEquals;


import static org.testng.Assert.assertNull;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import net.sqs2.net.ClassURLStreamHandlerFactory;
import net.sqs2.omr.app.command.RemoveResultFoldersCommand;
import net.sqs2.omr.master.FormMasterException;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.ConfigSchemeException;
import net.sqs2.omr.model.MarkReaderConstants;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.model.PageAreaResult;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTaskException;
import net.sqs2.omr.model.PageTaskResult;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SessionSources;
import net.sqs2.omr.session.init.SessionSourceException;
import net.sqs2.omr.session.init.SessionSourceInitializationStopException;
import net.sqs2.omr.session.model.SessionStopException;
import net.sqs2.omr.session.service.PageTaskExecutable;
import net.sqs2.omr.session.service.SessionSourceServerDispatcher;
import net.sqs2.omr.session.service.SessionSourceServerDispatcherImpl;
import net.sqs2.omr.session.service.SessionSourceServerImpl;
import net.sqs2.util.FileResourceID;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class PageTaskExecutableTest {

	static{
		try {
			URL.setURLStreamHandlerFactory(ClassURLStreamHandlerFactory.getSingleton());
		} catch (Error ex) {
		}
	}

	static long KEY = 0L;
	
	static SessionSourceServerImpl sessionSourceServer;
	
	@BeforeClass
	public static void before()throws Exception{
		sessionSourceServer = SessionSourceServerImpl.createInstance(KEY, MarkReaderConstants.CLIENT_TIMEOUT_IN_SEC);
	}

	private OMRPageTask executePageTask(
			File sourceDirectoryRootFile,
			long key, 
			long sessionID, 
			String formMassterFilePath, 
			String configFilePath,
			String imageFilePath, 
			int indexofPageInImageFile, 
			int numPagesInImageFile,
			int processingPageNumber) throws RemoteException, IOException, PageTaskException, SessionSourceException, FormMasterException, SessionStopException, ConfigSchemeException, SessionSourceInitializationStopException {
		File imageFile = new File(sourceDirectoryRootFile, imageFilePath);
		File masterFile = new File(sourceDirectoryRootFile, formMassterFilePath);
		File configFile = new File(sourceDirectoryRootFile, configFilePath);

		FileResourceID formMasterFileResourceID = new FileResourceID(formMassterFilePath,masterFile.lastModified());
		FileResourceID configFileResourceID = new FileResourceID(configFilePath, configFile.lastModified());
		FileResourceID imageFileResourceID = new FileResourceID(imageFilePath, imageFile.lastModified());

		PageID pageID = new PageID(imageFileResourceID, indexofPageInImageFile, numPagesInImageFile);
		OMRPageTask pageTask = new OMRPageTask(sourceDirectoryRootFile.getAbsolutePath(), 
				configFileResourceID, 
				formMasterFileResourceID, 
				processingPageNumber, 
				pageID, 
				sessionID);

		SessionSourceServerDispatcher dispatcher = new SessionSourceServerDispatcherImpl(sessionSourceServer, null, key);

		PageTaskExecutable exeutable = new PageTaskExecutable(pageTask, dispatcher);

		SessionSource sessionSource = SessionSources.create(sessionID, sourceDirectoryRootFile);

		exeutable.execute();
		
		sessionSource.close();
		
		dispatcher.close();
		
		return pageTask;
	}

	
	@Test
	public synchronized void testExecute()throws Exception{

		File sourceDirectoryRootFile = new File("src/test/resources/test1");
		String formMasterFilePath = "form.pdf";
		String configFilePath = AppConstants.RESULT_DIRNAME+File.separatorChar+"config.xml";

		String imagePath = "a001.tif";
		int processingPageNumber = 1;
		int indexInFile = 0;
		int numPagesInFile = 1;
		long sessionID = 12345L;
		
		OMRPageTask pageTask = executePageTask(
				sourceDirectoryRootFile, KEY,
				sessionID, formMasterFilePath, configFilePath, imagePath,
				indexInFile, numPagesInFile, processingPageNumber);
		
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
		
		new RemoveResultFoldersCommand(sourceDirectoryRootFile).call();

	}

	@Test
	public synchronized void testExecute2()throws Exception{

		File sourceDirectoryRootFile = new File("src/test/resources/test3");

		String masterPath = "form.pdf";
		String configPath = AppConstants.RESULT_DIRNAME+File.separatorChar+"config.xml";

		String imagePath = "001.png";
		int processingPageNumber = 1;
		int indexInFile = 0;
		int numPagesInFile = 1;
		long sessionID = 12346L;
		
		OMRPageTask pageTask = executePageTask(
				sourceDirectoryRootFile, KEY,
				sessionID, masterPath, configPath, imagePath,
				indexInFile, numPagesInFile, processingPageNumber);
		
		PageTaskResult result = pageTask.getPageTaskResult();
		PageTaskException exception = pageTask.getPageTaskException();
		assertNull(exception);

		Point2D[] deskewGuideCenterPoints = result.getDeskewGuideCenterPoints();
		
		assertEquals(new Point(114,55), deskewGuideCenterPoints[0]);
		assertEquals(new Point(505,63), deskewGuideCenterPoints[1]);
		assertEquals(new Point(94,790), deskewGuideCenterPoints[2]);
		assertEquals(new Point(483,800), deskewGuideCenterPoints[3]);
		
		List<PageAreaResult> pageAreaResultList = result.getPageAreaResultList();
		assertEquals(18, pageAreaResultList.size());
		
		new RemoveResultFoldersCommand(sourceDirectoryRootFile).call();
	}
}
