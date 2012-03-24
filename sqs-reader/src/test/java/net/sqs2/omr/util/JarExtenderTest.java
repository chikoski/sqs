package net.sqs2.omr.util;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;

public class JarExtenderTest {
	
	@Test(groups ={"JarExtender"})
	public void testExtend()throws IOException{
		File basePath = new File(System.getProperty("java.io.tmpdir"), "sqsJarExtenderTest");
		String targetFilePath = "ftl/sqs/index.ftl";
		File targetFile = new File(basePath, targetFilePath);
		targetFile.deleteOnExit();
		basePath.deleteOnExit();
		new JarExtender().extend(new String[]{targetFilePath}, basePath);
		assertTrue(targetFile.exists());
	}
}
