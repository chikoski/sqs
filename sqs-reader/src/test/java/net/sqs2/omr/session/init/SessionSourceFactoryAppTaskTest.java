package net.sqs2.omr.session.init;

import static org.testng.Assert.assertEquals;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import net.sqs2.net.ClassURLStreamHandlerFactory;
import net.sqs2.omr.app.command.RemoveResultFoldersCommand;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.FormMasterException;
import net.sqs2.omr.model.ConfigSchemeException;
import net.sqs2.omr.model.ContentIndexer;
import net.sqs2.omr.model.PageTaskException;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SessionSourceImpl;
import net.sqs2.omr.model.SessionSources;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.session.init.SessionSourceException;
import net.sqs2.omr.session.init.SessionSourceInitializationStopException;
import net.sqs2.omr.session.init.SessionSourceInitializeCommand;
import net.sqs2.omr.session.model.SessionStopException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SessionSourceFactoryAppTaskTest {

	//static MarkReaderSessionMonitor monitor = new MarkReaderSessionMonitorAdapter(); 
	static File testDirectory1 = new File("src/test/resources/test1");
	static File testDirectory2 = new File("src/test/resources/test2");
	static File testDirectory3 = new File("src/test/resources/test3");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			URL.setURLStreamHandlerFactory(ClassURLStreamHandlerFactory.getSingleton());
		} catch (Error ex) {
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		new RemoveResultFoldersCommand(testDirectory1).call();
		new RemoveResultFoldersCommand(testDirectory2).call();
		new RemoveResultFoldersCommand(testDirectory3).call();
	}

	@Test
	public void testInitialize(){
		long sessionID = 0L;
		
		File rootDirectory = testDirectory1.getAbsoluteFile();
		SessionSourceInitializeCommand sessionSourceInitCommand = null;
		SessionSource sessionSource = null;
		try{
			sessionSource = SessionSources.create(sessionID, rootDirectory);
			sessionSourceInitCommand = new SessionSourceInitializeCommand(sessionSource, null);
			sessionSourceInitCommand.call();
			
			ContentIndexer indexer = sessionSource.getContentIndexer();
			List<FormMaster> pageMasterList = indexer.getFormMasterList();

			assertTrue(sessionSource.getRootDirectory().isDirectory());
			assertEquals(1, pageMasterList.size());
			
			FormMaster master = (FormMaster)pageMasterList.get(0);
			List<SourceDirectory> sourceDirectoryList = indexer.getFlattenSourceDirectoryList(master);
			assertEquals(1, sourceDirectoryList.size());
			
			SourceDirectory sourceDirectory = sourceDirectoryList.get(0);
			assertEquals(0, sourceDirectory.getNumChildSourceDirectories());
			assertEquals(0, sourceDirectory.getDescendentSourceDirectoryList().size());
			
			assertEquals(22, sourceDirectory.getNumPageIDs());
			assertEquals(22, sourceDirectory.getNumPageIDsTotal());
			
			assertEquals(0, sourceDirectory.getID());
			
			assertEquals("", sourceDirectory.getRelativePath());
			assertEquals(rootDirectory, sourceDirectory.getRoot());
			
			
			return;
		}catch(SessionSourceInitializationStopException ex){
			ex.printStackTrace();
		}catch(ConfigSchemeException ex){
			ex.printStackTrace();
		}catch(FormMasterException ex){
			ex.printStackTrace();
		}catch(SessionStopException ex){
			ex.printStackTrace();
		}catch(SessionSourceException ex){
			ex.printStackTrace();
		}catch(PageTaskException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			if(sessionSource != null){
				try{
					sessionSource.close();
				}catch(IOException ignore){}
			}
			new RemoveResultFoldersCommand(testDirectory1).call();
		}
		fail();
	}


	@Test
	public void testInitializeFolderOfNewFormat(){
		long sessionID = 1L;
		
		File rootDirectory = testDirectory3.getAbsoluteFile();
		SessionSourceInitializeCommand sessionSourceInitAppTask = null;
		SessionSource sessionSource = null;
		try{
			sessionSource = SessionSources.create(sessionID, rootDirectory);
			sessionSourceInitAppTask = new SessionSourceInitializeCommand(sessionSource, null);
			sessionSourceInitAppTask.call();
			ContentIndexer indexer = sessionSource.getContentIndexer();
			List<FormMaster> formMasterList = indexer.getFormMasterList();

			assertTrue(sessionSource.getRootDirectory().isDirectory());
			assertEquals(1, formMasterList.size());
			
			FormMaster master = formMasterList.get(0);
			List<SourceDirectory> sourceDirectoryList = indexer.getFlattenSourceDirectoryList(master);
			assertEquals(1, sourceDirectoryList.size());
			
			SourceDirectory rootSourceDirectory = sourceDirectoryList.get(0);
			
			assertEquals(rootDirectory, rootSourceDirectory.getRoot());
			assertEquals("", rootSourceDirectory.getRelativePath());
			assertEquals(0, rootSourceDirectory.getNumChildSourceDirectories());
			assertEquals(0, rootSourceDirectory.getDescendentSourceDirectoryList().size());
			assertEquals(1, rootSourceDirectory.getNumPageIDs());
			assertEquals(1, rootSourceDirectory.getNumPageIDsTotal());
			assertEquals(0, rootSourceDirectory.getID());
			
			return;
		}catch(FormMasterException ex){
			ex.printStackTrace();
		}catch(SessionStopException ex){
			ex.printStackTrace();
		}catch(SessionSourceInitializationStopException ex){
			ex.printStackTrace();
		}catch(SessionSourceException ex){
			ex.printStackTrace();
		}catch(ConfigSchemeException ex){
			ex.printStackTrace();
		}catch(PageTaskException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			try{
				if(sessionSource != null){
					sessionSource.close();
				}
				new RemoveResultFoldersCommand(testDirectory2).call();
			}catch(IOException ignore){
				ignore.printStackTrace();
			}
		}
		fail();
	}
	
}
