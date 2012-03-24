package net.sqs2.template;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import net.sqs2.omr.util.JarExtender;
import net.sqs2.util.FileUtil;

import org.testng.annotations.Test;

import freemarker.template.Template;


public class TemplateLoaderTest {
	
	String path = "ftl";
	String skin = "sqs";
	String fileName = "index.ftl";
	
	File basePath = new File(System.getProperty("user.home"), File.separatorChar + ".sqs" + File.separatorChar + "Test");

	@Test(expectedExceptions=FileNotFoundException.class)
	//dependsOnGroups={"JarExtender.*"}
	public void getUserCustomizedTemplateTest()throws Exception{

		// prepare user customizable preference directory
		if(! basePath.isDirectory()){
			basePath.mkdirs();
		}
		String targetFilePath = path+"/"+skin+"/"+fileName; // "ftl/sqs/index.html"
		File targetFile = new File(basePath, targetFilePath);
		
		if(basePath.exists()){
			new JarExtender().extend(new String[]{targetFilePath}, basePath);
		}
		
		assertTrue(targetFile.exists());
		
		TemplateLoader templateLoader = new TemplateLoader(basePath, path, skin);

		// Now, there is a targetFile, so 'getUserCustomizedTemplate' method calling should be OK.
		Template userCustomizedTemplate = templateLoader.getUserCustomizedTemplate(fileName, "UTF-8");
		assertNotNull(userCustomizedTemplate);
		
		// delete targetFile
		targetFile.delete();

		try{
			// TemplateLoader instance caches templates, so create new instance.
			templateLoader = new TemplateLoader(basePath, path, skin);
			
			// Now, there is no targetFile, so 'getUserCustomizedTemplate' method calling should throw FileNotFoundException 
			templateLoader.getUserCustomizedTemplate(fileName, "UTF-8");
		}catch(FileNotFoundException ex){
			throw ex;
		}finally{
			FileUtil.deleteDirectory(basePath);
		}
	}

	@Test
	public void getDefaultTemplateTest()throws Exception{
		TemplateLoader templateLoader = new TemplateLoader(basePath, path, skin);
		Template defaultTemplate = templateLoader.getDefaultTemplate(fileName, "UTF-8");
		assertNotNull(defaultTemplate);
	}
}
