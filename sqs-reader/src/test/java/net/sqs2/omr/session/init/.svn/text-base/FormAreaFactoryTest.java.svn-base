package net.sqs2.omr.session.init;

import static org.testng.Assert.assertEquals;


import java.io.File;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.FormMasterFactory;
import net.sqs2.omr.session.init.MergedFormMasterFactory;

import org.testng.annotations.Test;


public class FormAreaFactoryTest {
	
	FormMasterFactory formMasterFactory = new MergedFormMasterFactory();

	@Test
	public void createTest()throws Exception{
		File sourceDirectoryRoot = new File("src/test/resources/test0");
		String formMasterFilePath = "form.pdf";

		FormMaster formMaster = formMasterFactory.create(sourceDirectoryRoot, formMasterFilePath);
		assertEquals(78, formMaster.getFormAreaList().size());
	}

}
