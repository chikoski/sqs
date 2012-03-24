package net.sqs2.omr.app.deskew;

import static org.testng.Assert.assertEquals;

import static org.testng.Assert.fail;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import net.sqs2.net.ClassURLStreamHandlerFactory;
import net.sqs2.omr.app.deskew.PageFrameException;
import net.sqs2.omr.app.deskew.PageFrameHandler;
import net.sqs2.omr.master.FormMasterException;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.master.sqm.PDFAttachmentFormMasterFactory;
import net.sqs2.omr.model.ConfigSchemeException;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.SourceConfig;
import net.sqs2.omr.model.SourceDirectoryConfiguration;
import net.sqs2.omr.model.SourceDirectoryConfigurationFactoryImpl;
import net.sqs2.util.FileResourceID;
import net.sqs2.util.FileUtil;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;



public class PageFrameHandlerTest {
	File sourceDirectoryConfigOriginalFile = new File("src/main/resources/config.xml");
	
	String sourceDirectoryRootPath = "src/test/resources/test3";
	String configPath = "config.xml";
	String masterFilePath = "form.pdf";
	
	File sourceDirectoryRoot = new File(sourceDirectoryRootPath);
	File sourceDirectoryConfigFile = new File(sourceDirectoryRoot, configPath);
		
	@BeforeClass
	public static void setURLStreamHandlerFactory(){	
		try{
			URL.setURLStreamHandlerFactory(ClassURLStreamHandlerFactory.getSingleton());
		}catch(Error ignore){}
	}
	
	private SourceConfig createSourceConfig(File sourceDirectoryRoot, FileResourceID configFileResourceID)throws ConfigSchemeException, MalformedURLException{		
		SourceDirectoryConfiguration config = new SourceDirectoryConfigurationFactoryImpl().create(sourceDirectoryRoot, configFileResourceID);
		SourceConfig sourceConfig = config.getConfig().getPrimarySourceConfig();
		sourceConfig.getFrameConfig().getDeskewGuideAreaConfig().setDebug(true);
		return sourceConfig;
	}
		
	private PageFrameHandler createPageFrameHandler(String imageFilePath, Point2D[] deskewGuideCenterPointsAssumed)throws MalformedURLException, ConfigSchemeException, IOException, FormMasterException, PageFrameException{
		FileResourceID configFileResourceID = new FileResourceID(configPath, sourceDirectoryConfigFile.lastModified());
		SourceConfig sourceConfig = createSourceConfig(sourceDirectoryRoot, configFileResourceID);

		File imageFile = new File(sourceDirectoryRoot, imageFilePath);
		FileResourceID imageFileResourceID = new FileResourceID(imageFilePath, imageFile.lastModified());
		int indexInFile = 0;
		int numPagesInFile = 1;
		BufferedImage image = ImageIO.read(imageFile);
		PageID pageID = new PageID(imageFileResourceID, indexInFile, numPagesInFile);
		PageMaster pageMaster = new PDFAttachmentFormMasterFactory().create(sourceDirectoryRoot, masterFilePath);
		PageFrameHandler pageFrameHandler = new PageFrameHandler(sourceConfig, image, pageID, pageMaster);
		return pageFrameHandler;
	}
	
	private void assertDeskewGuideCenterPoints(String imageFilePath, Point2D[] deskewGuideCenterPointsAssumed)throws MalformedURLException, ConfigSchemeException, IOException, FormMasterException, PageFrameException{
		PageFrameHandler pageFrameHandler = createPageFrameHandler(imageFilePath, deskewGuideCenterPointsAssumed);
		Point2D[] deskewGuideCenterPoints = pageFrameHandler.extractDeskewGuidesAndValidate().getDeskewGuideCenterPoints();
		for(int i=0; i<4;i++){
			assertEquals(deskewGuideCenterPointsAssumed[i], deskewGuideCenterPoints[i]);
		}
	}
	
	@BeforeMethod
	public void before() throws Exception{
		FileUtil.copy(sourceDirectoryConfigOriginalFile, sourceDirectoryConfigFile);
	}
	
	@Test
	public void testOnImage001(){
		try{
			assertDeskewGuideCenterPoints("001.png", new Point2D[]{new Point2D.Float(109, 53),new Point(499, 62), new Point(92, 792), new Point(474, 802)});
		}catch(Exception ex){
			ex.printStackTrace();
			fail();
		}
	}

	@AfterMethod
	public void after()throws Exception{
		sourceDirectoryConfigFile.delete();
	}
}
