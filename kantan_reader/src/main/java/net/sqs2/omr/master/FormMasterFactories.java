/*  FormMasterFactory.java

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

package net.sqs2.omr.master;

import java.io.File;

import net.sqs2.omr.master.pdfbookmark.PDFBookmarkFormMasterFactory;
import net.sqs2.omr.master.sqm.PDFAttachmentFormMasterFactory;
import net.sqs2.omr.master.sqm.XMLFormMasterFactory;

public class FormMasterFactories{

	PDFAttachmentFormMasterFactory pdfAttachmentFormMasterFactory = new PDFAttachmentFormMasterFactory();  
	PDFBookmarkFormMasterFactory pdfBookmarkFormMasterFactory = new PDFBookmarkFormMasterFactory();
	XMLFormMasterFactory xmlFormMasterFactory = new XMLFormMasterFactory();
	
	public PageMaster createMasterByPDFFile(File sourceDirectoryRoot, String masterPath) throws PageMasterException{
		PageMasterException ex = null;
		PageMaster master = null;
		try{
			master = pdfAttachmentFormMasterFactory.create(sourceDirectoryRoot, masterPath);
			if(master != null){
				return master;
			}
		}catch(PageMasterException expt){
			ex = expt;
		}
		master = pdfBookmarkFormMasterFactory.create(sourceDirectoryRoot, masterPath);
		if(master != null){
			return master;
		}
		throw ex;
	}

	public PageMaster createMasterBySQMFile(File sourceDirectoryRoot, String masterPath) throws PageMasterException{
		return xmlFormMasterFactory.create(sourceDirectoryRoot, masterPath);
	}
	
}
