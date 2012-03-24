/*

 PDFAttachmentFormMasterFactory.java

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

 Created on 2006/07/10

 */
package net.sqs2.omr.master.sqm;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.FactoryConfigurationError;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.PageMasterException;
import net.sqs2.omr.master.PageMasterFactory;
import net.sqs2.omr.master.PageMasterMetadata;
import net.sqs2.translator.TranslatorException;
import net.sqs2.xmlns.SQSNamespaces;

import org.w3c.dom.Document;


public class PDFAttachmentFormMasterFactory extends XMLFormMasterFactory implements PageMasterFactory {

	static PageMasterFactory createInstance(){
		return new PDFAttachmentFormMasterFactory();
	}
	
	public FormMaster create(File sourceDirectoryRoot, String pdfPath) throws PageMasterException {
		String masterPathLower = pdfPath.toLowerCase();
		if(masterPathLower.endsWith(".pdf")){
			File pdfFile = new File (sourceDirectoryRoot, pdfPath);
			InputStream pdfInputStream = null;
			InputStream sqmInputStream = null;
			try {	
				PageMasterMetadata pageMasterMetadata = new PageMasterMetadata(sourceDirectoryRoot, pdfPath, "PDFAttachment", 3, pdfFile.lastModified());
				pdfInputStream = new BufferedInputStream(new FileInputStream(pdfFile));
				PDFAttachmentToSQMTranslator translator = new PDFAttachmentToSQMTranslator();
				sqmInputStream = translator.translate(pdfInputStream, pdfFile.toURI().toString());
				
				ByteArrayInputStream fixedSQMInputStream = hotfixXFormsNSURItoSVGNSURI(sqmInputStream);  

				Document document = createDocumentByStream(fixedSQMInputStream, pdfPath);
				return super.createFormMaster(pageMasterMetadata, document);
			} catch (IOException ex) {
				throw new PageMasterException(ex, pdfPath);
			} catch (TranslatorException ex) {
				throw new PageMasterException(ex, pdfPath);
			} catch (FactoryConfigurationError ex) {
				throw new RuntimeException(ex);
			}finally{
				try{
					sqmInputStream.close();
					pdfInputStream.close();
				}catch(Exception ignore){
				}
			}
		}else{
			return null;
		}
	}

	private ByteArrayInputStream hotfixXFormsNSURItoSVGNSURI(InputStream sqmInputStream) throws UnsupportedEncodingException,
			IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream(4096);
		
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(b, "UTF-8"));
		LineNumberReader reader = new LineNumberReader(new InputStreamReader(sqmInputStream, "UTF-8"));
		String line = null;
		
		while((line = reader.readLine()) != null){
			String pattern = "xmlns:svg=\""+SQSNamespaces.XFORMS_URI+"\"";
			int c = line.indexOf(pattern);
			
			if(0 <= c){
				String replace = "xmlns:svg=\""+SQSNamespaces.SVG_URI+"\"";
				writer.print(line.substring(0, c));
				writer.print(replace);
				writer.println(line.substring(c + pattern.length()));
				
				if(true){
					System.out.print(line.substring(0, c));
					System.out.print(replace);
					System.out.println(line.substring(c + pattern.length()));
				}
				
			}else{
				if(true){
					System.out.println(line);
				}
				writer.println(line);		
			}
		}
		writer.close();
		reader.close();
		
		ByteArrayInputStream in = new ByteArrayInputStream(b.toByteArray());
		return in;
	}
}
