/**
 *  PDFAttachmentFormMasterFactoryTest.java
 
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
 
 Created on 2007/07/31
 Author hiroya
 */

package net.sqs2.omr.master.sqm;

import java.io.File;

import junit.framework.TestCase;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.PageMasterFactory;

public class PDFAttachmentFormMasterFactoryTest extends TestCase {
	public void testCreate(){
		try{
			PageMasterFactory factory = PDFAttachmentFormMasterFactory.createInstance();
			//FormMaster master = (FormMaster)factory.create(new File("sqs-editor/src/main/template"),"simple.pdf");
			FormMaster master = (FormMaster)factory.create(new File("/tmp"), "sqs.pdf");
			assertEquals(24, master.getFormAreaList().size());
		}catch(Exception ex){
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}
}
