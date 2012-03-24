/*

 BookmarkToSQMTranslator.java

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

import java.util.HashMap;
import java.util.Map;

import net.sqs2.translator.ParamEntry;
import net.sqs2.translator.TranslatorException;
import net.sqs2.translator.XSLTranslator;

public class BookmarkToSQMTranslator extends XSLTranslator {
	int numPages = -1;

	public BookmarkToSQMTranslator() throws TranslatorException {
		super();
	}

	public void setNumPages(int numPages) {
		this.numPages = numPages;
		Map<String, ParamEntry[]> xsltParams = new HashMap<String, ParamEntry[]>();
		xsltParams.put(null, new ParamEntry[] { new ParamEntry("pages", Integer.valueOf(this.numPages).toString()) });
	}

}
