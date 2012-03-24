package net.sqs2.omr.model;


import static org.testng.Assert.assertEquals;


import java.io.File;

import net.sqs2.omr.app.command.RemoveResultFoldersCommand;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.sqm.PDFAttachmentFormMasterFactory;
import net.sqs2.omr.model.FormMasterAccessor;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class FormMasterAccessorTest {
	
	static File sourceDirectoryRoot = new File("src/test/resources/test3");
	static String masterFilePath = "form.pdf"; 
	
	@Test
	public void testPutAndGet()throws Exception{
		FormMasterAccessor formMasterAccessor;
		FormMaster formMaster, formMasterFromStorage;
		
		formMasterAccessor = FormMasterAccessor.createInstance(sourceDirectoryRoot);
		formMaster = new PDFAttachmentFormMasterFactory().create(sourceDirectoryRoot, masterFilePath);
		formMasterAccessor.put(formMaster);
		formMasterFromStorage = formMasterAccessor.get(FormMaster.createKey(formMaster.getFileResourceID()));
		
		assertEquals(formMaster, formMasterFromStorage);
		assertEquals(formMaster.getFormAreaList().size(), formMasterFromStorage.getFormAreaList().size());
		formMasterAccessor.dispose();

		formMasterAccessor = FormMasterAccessor.createInstance(sourceDirectoryRoot);
		formMaster = new PDFAttachmentFormMasterFactory().create(sourceDirectoryRoot, masterFilePath);
		formMasterAccessor.put(formMaster);
		
		formMasterFromStorage = formMasterAccessor.get(formMaster.getFileResourceID());
		
		assertEquals(formMaster, formMasterFromStorage);
		assertEquals(formMaster.getFormAreaList().size(), formMasterFromStorage.getFormAreaList().size());
		formMasterAccessor.dispose();
}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		new RemoveResultFoldersCommand(sourceDirectoryRoot).call();
	}

}
