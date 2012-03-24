/*

 SQSToHTMLTranslator.java
 
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
 
 Created on 2007/09/04

 */
package net.sqs2.translator.impl;

import net.sqs2.translator.TranslatorException;
import net.sqs2.translator.XSLTranslator;

public class SQSToHTMLTranslator extends XSLTranslator {
	private static final String[] SQS2HTML_FILES = { "cmpl-label.xsl", "cmpl-ref.xsl", "embed-counter.xsl",
			"embed-link.xsl", "convert1.xsl", "sqs2html.xsl" };
	public SQSToHTMLTranslator(String xsltURL) throws TranslatorException {
		super();
		try {
			initialize(xsltURL, SQS2HTML_FILES, null);
		} catch (Exception ex) {
			throw new TranslatorException(ex);
		}
	}
}
