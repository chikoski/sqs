/*
 * 

 XMLUtil.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

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

package net.sqs2.xml;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * XML Utilitiessqs-omr/src/main/java/net/sqs2
 * 
 * @author hiroya
 * 
 */
public class XMLUtil {

	/**
	 * create DocumentBuilder (wrapper method).
	 * 
	 * @return DocumentBuilder
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static DocumentBuilder createDocumentBuilder() throws ParserConfigurationException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setExpandEntityReferences(false);
		factory.setNamespaceAware(true);
		factory.setValidating(false);
		// factory.setValidating(false);
		// factory.setIgnoringComments(true);
		DocumentBuilder parser = factory.newDocumentBuilder();
		return parser;
	}

	/**
	 * create XMLSerializer (wrapper method).
	 * 
	 * @param targetOutputStream
	 * @return XMLSerializer
	 */
	/*
	 * public static XMLSerializer createXMLSerializer(OutputStream
	 * targetOutputStream) { OutputFormat formatter = new OutputFormat();
	 * formatter.setPreserveSpace(true); formatter.setEncoding("UTF-8");
	 * XMLSerializer serializer = new XMLSerializer(targetOutputStream,
	 * formatter); return serializer; }
	 */

	/**
	 * create XMLSerializer (wrapper method).
	 * 
	 * @param targetWriter
	 * @return XMLSerializer
	 */
	/*
	 * public static XMLSerializer createXMLSerializer(Writer targetWriter) {
	 * OutputFormat formatter = new OutputFormat();
	 * formatter.setPreserveSpace(true); formatter.setEncoding("UTF-8");
	 * formatter.setOmitXMLDeclaration(false); XMLSerializer serializer = new
	 * XMLSerializer(targetWriter, formatter); return serializer; }
	 */

	public static SAXResult createSAXResult(OutputStream outputStream) throws IOException {
		Serializer serializer = SerializerFactory.getSerializer(OutputPropertiesFactory
				.getDefaultMethodProperties("xml"));
		serializer.setOutputStream(outputStream);
		SAXResult result = new SAXResult(serializer.asContentHandler());
		return result;
	}

	/**
	 * create InputStream from a Document object.
	 * 
	 * @param sourceDocument
	 * @return InputStream
	 * @throws IOException
	 */
	/*
	 * public static InputStream createInputStream(Document
	 * sourceDocument)throws IOException{ return
	 * createInputStream(sourceDocument.getDocumentElement()); }
	 */

	/**
	 * create InputStream from an Element object. sourceElement will be
	 * serialized in memory, then, returns created ByteArrayInputStream.
	 * 
	 * @param sourceElement
	 * @return InputStream
	 * @throws IOException
	 */
	/*
	 * public static InputStream createInputStream(Element sourceElement)throws
	 * IOException{ ByteArrayOutputStream outputStream = new
	 * ByteArrayOutputStream(4096); XMLSerializer serializer =
	 * createXMLSerializer(outputStream); serializer.serialize(sourceElement);
	 * return new ByteArrayInputStream(outputStream.toByteArray()); }
	 */

	/**
	 * create String from an Element object.
	 * 
	 * @param sourceElement
	 * @return created String
	 */
	/*
	 * public static String createString(Element sourceElement){ try{
	 * StringWriter writer = new StringWriter(64); XMLSerializer serializer =
	 * createXMLSerializer(writer); serializer.serialize(sourceElement); return
	 * writer.toString(); }catch(IOException ignore){ return null; } }
	 */

	/**
	 * marshal document object into outputStream.
	 * 
	 * @param sourceDocument
	 * @param targetFile
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 * @throws IOException
	 */
	public static void marshal(Document sourceDocument, File targetFile) throws ParserConfigurationException, TransformerConfigurationException, TransformerException, IOException {
		BufferedOutputStream outputStream = null;
		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(targetFile));
			marshal(sourceDocument, outputStream);
		} finally {
			outputStream.close();
		}
	}

	/**
	 * marshal document object into outputStream.
	 * 
	 * @param sourceDocument
	 * @param targetOutputStream
	 * @throws ParserConfigurationException
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	public static void marshal(Document sourceDocument, OutputStream targetOutputStream) throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		DOMSource source = new DOMSource(sourceDocument);
		StreamResult result = new StreamResult(targetOutputStream);
		transformer.transform(source, result);
	}

	public static String createString(Element element) {
		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			DOMSource source = new DOMSource(element);
			Writer writer = new StringWriter();
			Result result = new StreamResult(writer);
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			return writer.toString();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static InputStream createInputStream(Element element) throws IOException {
		byte[] ret = null;
		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			DOMSource source = new DOMSource(element);
			Writer writer = new StringWriter();
			Result result = new StreamResult(writer);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			ret = writer.toString().getBytes("UTF-8");
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(ret);
	}

}
