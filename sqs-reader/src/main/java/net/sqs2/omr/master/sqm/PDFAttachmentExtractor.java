/*

 PDFAttachmentExtractor.java

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

 Created on 2007/08/22

 */
package net.sqs2.omr.master.sqm;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;

import net.sqs2.omr.master.NoConfigFilePageMasterException;

import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNameTree;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfString;

public class PDFAttachmentExtractor {

	PdfReader reader;

	public PDFAttachmentExtractor(InputStream pdfInputStream) throws IOException {
		this.reader = new PdfReader(pdfInputStream);
	}

	public InputStream extract(String extractFileSuffix) throws NoConfigFilePageMasterException {
		if (extractFileSuffix == null) {
			throw new RuntimeException("type is undefined. call PDFAttachmentExtractor#setType(String) !");
		}
		OutputStream outputStream = new org.apache.commons.io.output.ByteArrayOutputStream();
		try {
			byte[] sqmBytes = extractAttachmentFiles(this.reader, extractFileSuffix);
			if (sqmBytes == null || sqmBytes.length == 0) {
				throw new NoConfigFilePageMasterException(null);
			}
			outputStream.write(sqmBytes);
			outputStream.flush();
			outputStream.close();
			return new ByteArrayInputStream(sqmBytes);
		} catch (IOException ex) {
			throw new NoConfigFilePageMasterException(null);
		}
	}

	public void close() {
		this.reader.close();
	}

	@SuppressWarnings("unchecked")
	private static byte[] extractAttachmentFiles(PdfReader reader, String suffix) throws IOException {
		PdfDictionary catalog = reader.getCatalog();
		PdfDictionary names = (PdfDictionary) PdfReader.getPdfObject(catalog.get(PdfName.NAMES));
		if (names != null) {
			PdfDictionary embFiles = (PdfDictionary) PdfReader.getPdfObject(names.get(new PdfName(
					"EmbeddedFiles")));
			if (embFiles != null) {
				HashMap<?, PdfObject> embMap = PdfNameTree.readTree(embFiles);
				for (Iterator<PdfObject> i = embMap.values().iterator(); i.hasNext();) {
					PdfDictionary filespec = (PdfDictionary) PdfReader.getPdfObject(i.next());
					byte[] ret = unpackFile(reader, filespec, suffix);
					if (ret != null) {
						return ret;
					}
				}
			}
		}
		for (int k = 1; k <= reader.getNumberOfPages(); ++k) {
			PdfArray annots = (PdfArray) PdfReader.getPdfObject(reader.getPageN(k).get(PdfName.ANNOTS));
			if (annots == null) {
				continue;
			}
			for (Iterator<PdfObject> i = annots.listIterator(); i.hasNext();) {
				PdfDictionary annot = (PdfDictionary) PdfReader.getPdfObject(i.next());
				PdfName subType = (PdfName) PdfReader.getPdfObject(annot.get(PdfName.SUBTYPE));
				if (!PdfName.FILEATTACHMENT.equals(subType)) {
					continue;
				}
				PdfDictionary filespec = (PdfDictionary) PdfReader.getPdfObject(annot.get(PdfName.FS));
				byte[] ret = unpackFile(reader, filespec, suffix);
				if (ret != null) {
					return ret;
				}
			}
		}
		return null;
	}

	/**
	 * Unpacks a file attachment.
	 * 
	 * @param reader
	 *            The object that reads the PDF document
	 * @param filespec
	 *            The dictionary containing the file specifications
	 * @param outPath
	 *            The path where the attachment has to be written
	 * @throws IOException
	 */
	private static byte[] unpackFile(PdfReader reader, PdfDictionary filespec, String suffix) throws IOException {
		if (filespec == null) {
			return null;
		}
		PdfName type = (PdfName) PdfReader.getPdfObject(filespec.get(PdfName.TYPE));
		if (!PdfName.F.equals(type) && !PdfName.FILESPEC.equals(type)) {
			return null;
		}
		PdfDictionary ef = (PdfDictionary) PdfReader.getPdfObject(filespec.get(PdfName.EF));
		if (ef == null) {
			return null;
		}
		PdfString fn = (PdfString) PdfReader.getPdfObject(filespec.get(PdfName.F));
		if (fn == null) {
			return null;
		}
		File fLast = new File(fn.toUnicodeString());
		String filename = fLast.getName();

		if (!filename.endsWith(suffix)) {
			return null;
		}

		PRStream prs = (PRStream) PdfReader.getPdfObject(ef.get(PdfName.F));
		if (prs == null) {
			return null;
		}
		return PdfReader.getStreamBytes(prs);
	}

}
