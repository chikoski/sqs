/*
 * 

 XSLTranslator.java

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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;

import net.sqs2.util.FileUtil;
import net.sqs2.xml.XMLUtil;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XSLTranslator extends AbstractTranslator implements Translator {

	TransformerHandler[] tHandlers = null;
	String[] baseURIArray = null;
	String[] xsltFilenames = null;
	Map<String, ParamEntry[]> xsltParamEntryArrayMap = null;

	public XSLTranslator() throws TranslatorException {
		System.setProperty("javax.xml.transform.TransformerFactory",
				"org.apache.xalan.processor.TransformerFactoryImpl");
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
				"org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		System.setProperty("javax.xml.parsers.SAXParserFactory",
				"org.apache.xerces.jaxp.SAXParserFactoryImpl");
	}

	public synchronized void initialize(String[] baseURIArray, String[] xsltFilenames, Map<String, ParamEntry[]> xsltParamEntryArrayMap) throws TranslatorException {
		this.baseURIArray = baseURIArray;
		if (xsltFilenames == null) {
			throw new RuntimeException("XSLT filenames cannot be null!");
			// this.xsltFilenames = new String[]{};
		} else {
			this.xsltFilenames = xsltFilenames;
		}
		if (xsltParamEntryArrayMap != null) {
			this.xsltParamEntryArrayMap = xsltParamEntryArrayMap;
		}
	}

	public synchronized void initialize(String baseURI, String[] xsltFilenames, Map<String, ParamEntry[]> xsltParamEntryArrayMap) throws TranslatorException {
		initialize(new String[] { baseURI }, xsltFilenames, xsltParamEntryArrayMap);
	}

	private TransformerHandler[] createTransformerHandlers(URIResolver uriResolver) throws TransformerFactoryConfigurationError, TransformerConfigurationException, IOException {
		SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();
		if(uriResolver != null){
			factory.setURIResolver(uriResolver);
		}
		TransformerHandler[] tHandlers = new TransformerHandler[this.xsltFilenames.length];
		for (int i = 0; i < this.xsltFilenames.length; i++) {
			String xsltFilename = this.xsltFilenames[i];
			String enabledBaseURI = null;
			for (String baseURI : baseURIArray) {
				try {
					StreamSource streamSource = createStreamSource(xsltFilename, baseURI);
					tHandlers[i] = factory.newTransformerHandler(streamSource);
					enabledBaseURI = baseURI;
					break;
				} catch (IOException ignore) {
				}
			}
			if (enabledBaseURI == null) {
				// JarURLConnection c = new JarURLConnection(URL url){};
				throw new RuntimeException("baseURI is invalid, resource loading failed: "+xsltFilename);
			}
			if (tHandlers[i] == null) {
				Logger.getLogger(getClass().getName()).warning("XSLT BaseURI: " + enabledBaseURI + xsltFilename);
				throw new RuntimeException("thandler[" + i + "] == null");
			}
			tHandlers[i].setSystemId(enabledBaseURI + xsltFilename);
			ParamEntry[] xsltParamEntryArray = null;
			if (this.xsltParamEntryArrayMap != null
					&& (xsltParamEntryArray = this.xsltParamEntryArrayMap.get(xsltFilename)) != null) {
				setTransformerParameters(tHandlers[i], xsltParamEntryArray);
			}
			if (0 < i) {
				tHandlers[i - 1].setResult(new SAXResult(tHandlers[i]));
			}
		}
		return tHandlers;
	}

	private void setTransformerParameters(TransformerHandler tHandler, ParamEntry[] xsltParams) {
		Transformer transformer = tHandler.getTransformer();
		if (xsltParams == null) {
			return;
		}
		for (ParamEntry entry : xsltParams) {
			transformer.setParameter(entry.getKey(), entry.getValue());
		}
	}

	public StreamSource createStreamSource(String href, String base) throws IOException {
		URL u = null; 
		if(base.endsWith("/")){
			u = new URL(base +href);
		}else{
			u = new URL(FileUtil.getBasepath(base) +'/'+ href);
		}
		// Logger.getLogger(getClass().getName()).info(u.toString());
		InputStream inputStream = u.openStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
		return new StreamSource(inputStreamReader, u.toString());
	}

	/**
	 * get XSLT base URI.
	 * 
	 * @return base URI in string. public String getBaseURI(){ return
	 *         this.baseURI; }
	 */

	/**
	 * get XSLT script filenames as "XSLT pipe line".
	 * 
	 * @return XSLT script filenames.
	 */
	public String[] getXsltFilenames() {
		return this.xsltFilenames;
	}

	public void execute(InputStream inputStream, String systemId, OutputStream outputStream, URIResolver uriResolver) throws TranslatorException {
		try {
			SAXResult result = XMLUtil.createSAXResult(outputStream);
			try {
				// Logger.getLogger(getClass().getName()).info(StringUtil.join(this.xsltFilenames,", "));
				this.tHandlers = createTransformerHandlers(uriResolver);
			} catch (TransformerException ex) {
				ex.printStackTrace();
				throw new TranslatorException(ex);
			}

			if (this.tHandlers == null || this.tHandlers.length == 0) {
				throw new TranslatorException("XSLTranslator is not initilized.");
			}
			synchronized (this) {
				this.tHandlers[this.tHandlers.length - 1].setResult(result);
				XMLReader reader = XMLReaderFactory.createXMLReader();
				reader.setContentHandler(this.tHandlers[0]);
				InputStreamReader in = new InputStreamReader(inputStream, "UTF-8");
				InputSource is = new InputSource(in);
				// InputSource is = new InputSource(inputStream);
				is.setSystemId(systemId);
				reader.parse(is);
			}
		} catch (SAXException ex) {
			ex.printStackTrace();
			throw new TranslatorException(ex);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new TranslatorException(ex);
		}
	}

}
