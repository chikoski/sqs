/*

 FormMasterFactory.java
 
 Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
 Created on 2005/02/26

 */
package net.sqs2.omr.session.init;

import java.io.File;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.FormMasterException;
import net.sqs2.omr.master.FormMasterFactory;
import net.sqs2.omr.master.pdfbookmark.PDFBookmarkFormMasterFactory;
import net.sqs2.omr.master.sqm.PDFAttachmentFormMasterFactory;
import net.sqs2.omr.master.sqm.XMLFormMasterFactory;

public class MergedFormMasterFactory implements FormMasterFactory {

	public static final String SUFFIX_PDF = ".pdf";
	public static final String SUFFIX_SQM = ".sqm";

	PDFAttachmentFormMasterFactory pdfAttachmentFormMasterFactory = new PDFAttachmentFormMasterFactory();
	PDFBookmarkFormMasterFactory pdfBookmarkFormMasterFactory = new PDFBookmarkFormMasterFactory();
	XMLFormMasterFactory xmlFormMasterFactory = new XMLFormMasterFactory();

	/* (non-Javadoc)
	 * @see net.sqs2.omr.app.IFormMasterFactory#create(java.io.File, java.lang.String)
	 */
	public FormMaster create(File sourceDirectoryRoot, String masterPath) throws FormMasterException {
		if (masterPath.endsWith(SUFFIX_PDF)) {
			return createMasterByPDFFile(sourceDirectoryRoot, masterPath);
		} else if (masterPath.endsWith(SUFFIX_SQM)) {
			return createMasterBySQMFile(sourceDirectoryRoot, masterPath);
		}
		return null;
	}

	private FormMaster createMasterByPDFFile(File sourceDirectoryRoot, String masterPath) throws FormMasterException {
		FormMasterException ex = null;
		FormMaster master = null;
		try {
			master = this.pdfAttachmentFormMasterFactory.create(sourceDirectoryRoot, masterPath);
			if (master != null) {
				return master;
			}
		} catch (FormMasterException expt) {
			ex = expt;
		}
		master = this.pdfBookmarkFormMasterFactory.create(sourceDirectoryRoot, masterPath);
		if (master != null) {
			return master;
		}
		throw ex;
	}

	private FormMaster createMasterBySQMFile(File sourceDirectoryRoot, String masterPath) throws FormMasterException {
		return this.xmlFormMasterFactory.create(sourceDirectoryRoot, masterPath);
	}
}
