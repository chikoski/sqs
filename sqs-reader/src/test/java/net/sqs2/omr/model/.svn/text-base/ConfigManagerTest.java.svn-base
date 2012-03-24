package net.sqs2.omr.model;


import static org.testng.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import net.sqs2.net.ClassURLStreamHandlerFactory;

import org.testng.annotations.Test;

public class ConfigManagerTest {
	
	static{
		try{
			URL.setURLStreamHandlerFactory(ClassURLStreamHandlerFactory.getSingleton());
		}catch(Error ignore){}
	}
	
	@Test
	public void testCreateConfigInstance()throws Exception{
		Config config = ConfigManager.createConfigInstance(new File("src/main/resources/config.xml").toURI().toURL(), "configRule.xml", "config.xml");
		assertEquals("2.1.1", config.getVersion());
	}
}
