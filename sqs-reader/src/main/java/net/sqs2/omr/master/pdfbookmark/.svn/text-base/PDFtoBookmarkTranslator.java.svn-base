/*

 PDFtoBookmarkTranslator.java

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

 Created on 2007/01/11

 */
package net.sqs2.omr.master.pdfbookmark;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.URIResolver;

import net.sqs2.omr.master.InvalidPageMasterException;
import net.sqs2.translator.AbstractTranslator;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;

public class PDFtoBookmarkTranslator extends AbstractTranslator {
	private int numPages = -1;

	public PDFtoBookmarkTranslator() {
	}

	@Override
	public void execute(InputStream sourceInputStream, String systemId, OutputStream bookmarkOutputStream, URIResolver uriResolver) throws InvalidPageMasterException {
		try {
			PdfReader reader = new PdfReader(sourceInputStream);
			this.numPages = reader.getNumberOfPages();
			SimpleBookmark.exportToXML(SimpleBookmark.getBookmark(reader), bookmarkOutputStream, "UTF-8",
					false);
			reader.close();
		} catch (IOException ex) {
			throw new InvalidPageMasterException();
		} finally {
		}
	}

	public int getNumPages() {
		return this.numPages;
	}
}
