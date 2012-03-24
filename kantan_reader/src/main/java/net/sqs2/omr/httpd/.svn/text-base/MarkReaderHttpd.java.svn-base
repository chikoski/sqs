/*

 MarkReaderHttpd.java

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

 Created on 2004/10/17

 */
package net.sqs2.omr.httpd;

import net.sqs2.httpd.GenericHttpd;
import net.sqs2.httpd.JarContentServlet;
import net.sqs2.omr.result.servlet.AnswerUpdateServlet;
import net.sqs2.omr.result.servlet.ChartServlet;
import net.sqs2.omr.result.servlet.ExportCSVServlet;
import net.sqs2.omr.result.servlet.ExportXLSServlet;
import net.sqs2.omr.result.servlet.FormAreaImageServlet;
import net.sqs2.omr.result.servlet.PageImageServlet;
import net.sqs2.omr.result.servlet.ResultBrowserServlet;

import org.mortbay.jetty.MimeTypes;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class MarkReaderHttpd extends GenericHttpd {
	private static final long serialVersionUID = 0L; 

	public MarkReaderHttpd(int port) {
		super(port);
		try {
			this.setStopAtShutdown(true);
			Context context = new Context(this, "/", Context.ALL);
			context.addServlet(new ServletHolder(new JarContentServlet()), '/'+JarContentServlet.getContextString()+"/*"); // jar
			context.addServlet(new ServletHolder(new ResultBrowserServlet()), '/'+ResultBrowserServlet.getContextString()+"/*"); // e
			context.addServlet(new ServletHolder(new PageImageServlet()), '/'+PageImageServlet.getContextString()+"/*"); // p
			context.addServlet(new ServletHolder(new FormAreaImageServlet()), '/'+FormAreaImageServlet.getContextString()+"/*"); // i
			context.addServlet(new ServletHolder(new AnswerUpdateServlet()), '/'+AnswerUpdateServlet.getContextString()+"/*"); // u
			
			context.addServlet(new ServletHolder(new ExportCSVServlet()), '/'+ExportCSVServlet.getContextString()+"/*"); // exportCSV
			context.addServlet(new ServletHolder(new ExportXLSServlet()), '/'+ExportXLSServlet.getContextString()+"/*"); // exportXLS
			context.addServlet(new ServletHolder(new ChartServlet()), '/'+ChartServlet.getContextString()+"/*"); // c
			
            MimeTypes mimeTypes = new MimeTypes();
			for(String[] entry : GenericHttpd.MIME_TYPE_ARRAY){
				mimeTypes.addMimeMapping(entry[0], entry[1]);
			}
			context.setMimeTypes(mimeTypes);   

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Another Process may be running.");
			throw new RuntimeException("Another Process may be running.");
		}
	}
}
