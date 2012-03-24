/**
 * 
 */
package net.sqs2.translator.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNameTree;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfString;

public class PDFAttachmentReader {

	private InputStream pdfInputStream;

	public PDFAttachmentReader(InputStream pdfInputStream) throws IOException {
		this.pdfInputStream = pdfInputStream;
	}

	public byte[] extractAttachmentFiles(String suffix) throws IOException {
		PdfReader reader = new PdfReader(this.pdfInputStream);
		byte[] bytes = extractAttachmentFiles(reader, suffix);
		reader.close();
		if (bytes == null) {
			return null;
		}
		return bytes;
	}

	@SuppressWarnings("unchecked")
	public static byte[] extractAttachmentFiles(PdfReader reader, String suffix) throws IOException {
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
