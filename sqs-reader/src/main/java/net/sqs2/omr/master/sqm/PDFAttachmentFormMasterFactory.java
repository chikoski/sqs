/*

 PDFAttachmentFormMasterFactory.java

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

 Created on 2006/07/10

 */
package net.sqs2.omr.master.sqm;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.FactoryConfigurationError;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.FormMasterException;
import net.sqs2.omr.master.FormMasterFactory;
import net.sqs2.omr.master.InvalidPageMasterException;
import net.sqs2.omr.master.NoConfigFilePageMasterException;
import net.sqs2.omr.master.PageMasterMetadata;

import org.w3c.dom.Document;

public class PDFAttachmentFormMasterFactory extends XMLFormMasterFactory implements FormMasterFactory {

	
	private static FormMasterFactory singleton = new PDFAttachmentFormMasterFactory();
	
	public static FormMasterFactory getSingleton() {
		return singleton;
	}

	@Override
	public FormMaster create(File sourceDirectoryRoot, String pdfPath) throws FormMasterException {
		String masterPathLower = pdfPath.toLowerCase();
		if (masterPathLower.endsWith(".pdf")) {
			File pdfFile = new File(sourceDirectoryRoot, pdfPath);
			InputStream pdfInputStream = null;
			InputStream sqmInputStream = null;
			InputStream sqsInputStream = null;
			try {
				pdfInputStream = new BufferedInputStream(new FileInputStream(pdfFile));
				PDFAttachmentExtractor pdfAttachmentExtractor = new PDFAttachmentExtractor(pdfInputStream);
				sqmInputStream = pdfAttachmentExtractor.extract(".sqm");
				sqsInputStream = pdfAttachmentExtractor.extract(".sqs");

				ByteArrayInputStream fixedSQMInputStream = XMLFormMasterFactory
						.hotfixXFormsNSURItoSVGNSURI(sqmInputStream);

				Document sqmDocument = createDocumentByStream(fixedSQMInputStream, pdfPath);
				Document sqsDocument = createDocumentByStream(sqsInputStream, pdfPath);

				PageMasterMetadata pageMasterMetadata = new PageMasterMetadata(sourceDirectoryRoot, pdfPath,
						"PDFAttachment", pdfFile.lastModified(), sqsDocument);
				return super.createFormMaster(pageMasterMetadata, sqmDocument, sqsDocument);
			} catch (NoConfigFilePageMasterException ex) {
				throw new NoConfigFilePageMasterException(pdfFile);
			} catch (InvalidPageMasterException ex) {
				throw new InvalidPageMasterException(pdfFile);
			} catch (IOException ex) {
				throw new FormMasterException(pdfFile);
			} catch (FactoryConfigurationError ex) {
				throw new RuntimeException(ex);
			} finally {
				try {
					pdfInputStream.close();
					sqmInputStream.close();
					sqsInputStream.close();
				} catch (Exception ignore) {
				}
			}
		} else {
			return null;
		}
	}

}
