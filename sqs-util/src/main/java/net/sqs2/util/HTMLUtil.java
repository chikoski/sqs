/*
 * 

 HTMLUtil.java

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
package net.sqs2.util;

import java.io.File;
import java.io.PrintWriter;

public class HTMLUtil {
	public static void printHTMLHead(PrintWriter writer, String encoding, String title) {
		writer.print("<?xml version=\"1.0\" encoding=\"");
		writer.print(encoding);
		writer.println("\"?>");
		writer
				.println("<html lang=\"ja\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:svg=\"http://www.w3.org/2000/svg\"");
		writer
				.println(" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xml:space=\"preserve\" xml:lang=\"ja\">");
		writer.println("<head>");
		writer.print("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=");
		writer.print(encoding);
		writer.print("\" />");
		writer.println("<title>");
		writer.println(HTMLUtil.escapeHTML(title));
		writer.println("</title>");
		writer.println("<style type=\"text/css\">");
		writer.println(".m{color: #fff; background-color: gray}");
		writer.println("img.textAreaImage{border:solid; border-color: gray; border-width: 1px;}");
		writer.println("</style>");
		writer.println("</head>");
	}

	public static void printInlineImage(PrintWriter writer, String uri, String alt) {
		writer.print("<a href=\"");
		writer.print(HTMLUtil.encodePath(uri));
		writer.print("\">");
		writer.print("<img src=\"");
		writer.print(HTMLUtil.encodePath(uri));
		writer.print("\" alt=\"");
		writer.print(HTMLUtil.escapeHTML(alt));
		writer.print("\" />");
		writer.print("</a>");
		writer.println();
	}

	public static String encodeURL(String src) {
		return org.mortbay.util.URIUtil.encodePath(src);
	}

	public static String encodePath(String src) {
		String ret = org.mortbay.util.URIUtil.encodePath(src);
		if (File.separatorChar == '\\') {
			return ret.replace('\\', '/');
		} else {
			return ret;
		}
	}

	/**
	 * escape &amp;, &lt; &gt; and &quot;
	 * 
	 * @param src
	 * @return escaped string
	 */
	public static String escapeHTML(String src) {
		return src.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"",
				"&quot;");
	}
}
