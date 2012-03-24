/*

 JarContentServlet.java

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

 Created on 2004/10/17

 */
package net.sqs2.httpd;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.util.FileUtil;

import org.mortbay.resource.Resource;

/**
 * @author hiroya
 * 
 */
public class JarContentServlet extends HttpServlet {
	private static final long serialVersionUID = 0;

	public static String getContextString() {
		return "jar";
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInContext = request.getPathInfo();
		URL url = getResourceURL(pathInContext);
		Resource resource = Resource.newResource(url);
		response.setStatus(HttpServletResponse.SC_OK);
		String filePath = url.getFile();
		String suffix = FileUtil.getSuffix(filePath);
		String mimeType = GenericHttpd.MIME_MAP.get(suffix);
		if (mimeType != null) {
			response.setContentType(mimeType);
		}
		// Logger.getLogger("").info("GET "+url.toString());
		FileUtil.pipe(resource.getInputStream(), response.getOutputStream());
		response.getOutputStream().close();
	}

	private static URL getResourceURL(String name) {
		if (name == null || name.length() == 0) {
			return null;
		}
		return JarContentServlet.class.getResource(name);
	}
}
