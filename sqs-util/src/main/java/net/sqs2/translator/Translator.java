/*
 * 
   Translator.java

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
 */
package net.sqs2.translator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.URIResolver;

public interface Translator {

	/**
	 * translate sourceFile to targetFile.
	 * 
	 * @param sourceFile
	 * @param targetFile
	 * @throws TranslatorException
	 */
	public void translate(String sourceFile, String targetFile, URIResolver uriResolver) throws TranslatorException, IOException;

	/**
	 * translate sourceFile to targetFile.
	 * 
	 * @param sourceFile
	 * @return outputStream
	 * @throws TranslatorException
	 */
	public InputStream translate(String sourceFile, URIResolver uriResolver) throws TranslatorException, IOException;

	/**
	 * translate sourceInputStream to targetOutputStream.
	 * 
	 * @param sourceInputStream
	 * @param targetOutputStream
	 * @throws TranslatorException
	 */
	public void translate(InputStream sourceInputStream, String systemId, OutputStream targetOutputStream, URIResolver uriResolver) throws TranslatorException, IOException;

	/**
	 * translate sourceInputStream to targetOutputStream.
	 * 
	 * @param sourceInputStream
	 * @return targetOutputStream
	 * @throws TranslatorException
	 */
	public InputStream translate(InputStream sourceInputStream, String systemId, URIResolver uriResolver) throws TranslatorException, IOException;

}
