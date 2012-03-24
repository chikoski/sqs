/*

 GenericHttpd.java

 Copyright 2004-2007 KUBO Hiroya (hiroya@sfc.keio.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2004/08/06

 */
package net.sqs2.httpd;

import java.util.HashMap;
import java.util.Map;

import org.mortbay.jetty.Server;

public class GenericHttpd extends Server {
	private static final long serialVersionUID = 0L;
	public static final String LOOPBACK_ADDRESS = "127.0.0.1";
	public final static String[][] MIME_TYPE_ARRAY = new String[][] {
			{ "html", "text/html; charset=UTF-8" },
			// {"xhtml", "text/html; charset=UTF-8"},
			{ "xhtml", "applicatin/xhtml+xml" }, { "js", "text/javascript; charset=UTF-8" },
			{ "css", "text/css; charset=UTF-8" }, { "txt", "text/plain; charset=MS932" },
			{ "xsl", "text/xml; charset=UTF-8" }, { "xslt", "text/xml; charset=UTF-8" },
			{ "xls", "application/vnd.ms-excel" }, { "svg", "image/svg" }, { "tif", "image/tiff" },
			{ "gif", "image/gif" }, { "jpeg", "image/jpeg" }, { "jpg", "image/jpeg" }, { "png", "image/png" } };

	public static Map<String, String> MIME_MAP = new HashMap<String, String>();
	static {
		for (String[] entry : MIME_TYPE_ARRAY) {
			MIME_MAP.put(entry[0], entry[1]);
		}
	}

	public GenericHttpd() {
	}

	public GenericHttpd(int port) {
		super(port);
	}
}
