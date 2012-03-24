/*

 SQSSource.java
 
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import net.sqs2.exsed.source.DOMTreeSource;
import net.sqs2.exsed.source.SourceException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author hiroya
 * 
 */
public class SQSSource extends DOMTreeSource {
	public SQSSource() throws SourceException {
	}

	public SQSSource(File file, InputStream in, File originalFile) throws SourceException {
		super(file, in, originalFile);
	}

	public SQSSource(URL url, boolean readonly, String title) throws SourceException {
		super(url, readonly, title);
	}

	public Document createDocument(File file) throws ParserConfigurationException, SAXException, IOException {
		// FileUtil.keywordSubstitution(file, SQSNamespaces.XHTML2_URI_WITH_BUG,
		// SQSNamespaces.XHTML2_URI);
		return super.createDocument(file);
	}

	public Document createDocument(InputStream in) throws ParserConfigurationException, SAXException, IOException {
		// in = adhocFixXHTML2Namespace(in);
		return super.createDocument(in);
	}

	/*
	 * private InputStream adhocFixXHTML2Namespace(InputStream in) throws
	 * IOException, FileNotFoundException { //TODO: someday, remove .sqs schema
	 * fix adhoc File tmpFile = File.createTempFile("sqs",".sqs");
	 * tmpFile.deleteOnExit(); OutputStream out = new BufferedOutputStream(new
	 * FileOutputStream(tmpFile)); FileUtil.keywordSubstitution(in, out,
	 * SQSNamespaces.XHTML2_URI_WITH_BUG, SQSNamespaces.XHTML2_URI, "UTF-8");
	 * out.close(); in.close(); in = new BufferedInputStream(new
	 * FileInputStream(tmpFile)); return in; }
	 */

}
