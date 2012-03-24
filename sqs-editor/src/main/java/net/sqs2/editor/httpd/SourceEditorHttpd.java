/*

 SourceEditorHttpd.java

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

 Created on 2007/08/03

 */
package net.sqs2.editor.httpd;

import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import net.sqs2.httpd.GenericHttpd;
import net.sqs2.httpd.JarContentServlet;

import org.mortbay.jetty.MimeTypes;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class SourceEditorHttpd extends GenericHttpd {
	private static final long serialVersionUID = 0L;
	int port;

	public SourceEditorHttpd(int port) {
		super(port);
		this.port = port;
		try {
			this.setStopAtShutdown(true);
			Context context = new Context(this, "/", Context.ALL);
			context.addServlet(new ServletHolder(new JarContentServlet()), '/'
					+ JarContentServlet.getContextString() + "/*");
			context.addServlet(new ServletHolder(new TempFileContentServlet()), '/'
					+ TempFileContentServlet.getContextString() + "/*");

			MimeTypes mimeTypes = new MimeTypes();
			for (String[] entry : GenericHttpd.MIME_TYPE_ARRAY) {
				mimeTypes.addMimeMapping(entry[0], entry[1]);
			}
			context.setMimeTypes(mimeTypes);

		} catch (Exception e) {
			e.printStackTrace();
			Logger.getLogger(getClass().getName()).severe("Another Process may be running.");
			throw new RuntimeException("Another Process may be running.");
		}
	}

	public String getBaseURI() {
		return "http://" + LOOPBACK_ADDRESS + ":" + this.port;
	}

	public URL createTempFileURL(File file) throws IOException {
		return TempFileContentServlet.createTempFile(getBaseURI(), file);
	}
}
