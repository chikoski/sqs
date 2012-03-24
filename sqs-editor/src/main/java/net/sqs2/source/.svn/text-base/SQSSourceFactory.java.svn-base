/*

 SourceFactory.java
 
 Copyright 2004 KUBO Hiroya (hiroya@sfc.keio.ac.jp).
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
 Created on 2004/08/03

 */
package net.sqs2.source;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.sqs2.exsed.source.Source;
import net.sqs2.exsed.source.SourceException;
import net.sqs2.exsed.source.SourceFactory;
import net.sqs2.translator.impl.PDFAttachmentReader;
import net.sqs2.util.FileUtil;

/**
 * @author hiroya
 * 
 */
public class SQSSourceFactory implements SourceFactory {

	public SQSSourceFactory() {
		super();
	}

	public Source createSource() throws SourceException {
		return new SQSSource();
	}

	private ByteArrayInputStream parsePDFInputStream(InputStream pdfInputStream, String suffix) throws IOException {
		PDFAttachmentReader reader = new PDFAttachmentReader(pdfInputStream);
		byte[] bytes = reader.extractAttachmentFiles(suffix);
		return new ByteArrayInputStream(bytes);
	}

	public Source createSource(File file) throws SourceException {
		try {
			InputStream sqsInputStream = null;
			String name = file.getName();
			if (name.endsWith(".pdf")) {
				InputStream pdfInputStream = new BufferedInputStream(new FileInputStream(file));
				sqsInputStream = parsePDFInputStream(pdfInputStream, ".sqs");
				pdfInputStream.close();
				File sqsFile = new File(FileUtil.getSuffixReplacedFilePath(file, "sqs"));
				return new SQSSource(sqsFile, sqsInputStream, file);
			} else if (name.endsWith(".sqs")) {
				sqsInputStream = new BufferedInputStream(new FileInputStream(file));
				return new SQSSource(file, sqsInputStream, null);
			}
			throw new SourceException("invalid suffix:"+name);
		} catch (IOException ex) {
			throw new SourceException(ex);
		}
	}

	public Source createSource(URL url, boolean readonly, String title) throws SourceException {

		// FIXME: support PDF format

		return new SQSSource(url, readonly, title);
	}
}
