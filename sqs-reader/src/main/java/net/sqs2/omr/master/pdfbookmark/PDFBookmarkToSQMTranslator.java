/**

  PDFBookmarkToSQMTranslator.java

  Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).

  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations
  under the License.
 */

package net.sqs2.omr.master.pdfbookmark;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.URIResolver;

import net.sqs2.omr.base.MarkReaderJarURIContext;
import net.sqs2.omr.master.InvalidPageMasterException;
import net.sqs2.translator.TranslatorException;
import net.sqs2.translator.XSLTranslator;

import org.apache.commons.io.output.ByteArrayOutputStream;

public class PDFBookmarkToSQMTranslator extends XSLTranslator {
	private int numPages = -1;
	private PDFtoBookmarkTranslator pdfToBookmarkTranslator;
	private BookmarkToSQMTranslator bookmarkToSQMTranslator;
	private static final String bookmark2sqm_xsl_file = "bookmark2svg/bookmark.xsl";

	public PDFBookmarkToSQMTranslator() throws TranslatorException {
		this.pdfToBookmarkTranslator = new PDFtoBookmarkTranslator();
		this.bookmarkToSQMTranslator = new BookmarkToSQMTranslator();
		String uri = MarkReaderJarURIContext.getXSLTBaseURI();
		this.bookmarkToSQMTranslator.initialize(uri, bookmark2sqm_xsl_file.split(" "), null);
	}

	@Override
	public void execute(InputStream sourceInputStream, String systemId, OutputStream sqmOutputStream, URIResolver uriResolver) throws TranslatorException{
		try {
			ByteArrayOutputStream bookmarkOutputStream = new ByteArrayOutputStream(65536);
			this.pdfToBookmarkTranslator.translate(sourceInputStream, systemId, bookmarkOutputStream, uriResolver);
			this.numPages = this.pdfToBookmarkTranslator.getNumPages();
			this.bookmarkToSQMTranslator.setNumPages(this.numPages);
			byte[] bytes = bookmarkOutputStream.toByteArray();
			if (bytes.length == 0) {
				throw new InvalidPageMasterException();
			}
			ByteArrayInputStream bookmarkInputStream = new ByteArrayInputStream(bytes);
			this.bookmarkToSQMTranslator.translate(bookmarkInputStream, systemId, sqmOutputStream, uriResolver);
			sqmOutputStream.flush();
			bookmarkInputStream.close();
		} catch (TranslatorException ex) {
			throw new InvalidPageMasterException();
		} catch (IOException ex) {
			throw new InvalidPageMasterException();
		}
	}

	public int getNumPages() {
		return this.numPages;
	}

}
