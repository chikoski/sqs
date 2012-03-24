/*

 TempFileContentServlet.java

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.util.FileUtil;

public class TempFileContentServlet extends HttpServlet {

	private static final long serialVersionUID = 0L;
	private static Map<File, File> fileMap = new HashMap<File, File>();
	private static Map<String, File> tmpFileMap = new HashMap<String, File>();

	public static String getContextString() {
		return "tmp";
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String pathInfo = req.getPathInfo().substring(1);
		OutputStream out = null;
		InputStream in = null;
		try {
			File pathTranslated = getPathTranslated(pathInfo);
			if (pathTranslated == null) {
				return;
			}
			if (pathTranslated.isDirectory()) {
				pathTranslated = new File(pathTranslated.getAbsolutePath(), "index.html");
			}
			out = new BufferedOutputStream(res.getOutputStream());
			in = new BufferedInputStream(new FileInputStream(pathTranslated));
			byte[] buf = new byte[4096];
			int len = 0;
			while (0 < (len = in.read(buf, 0, buf.length))) {
				out.write(buf, 0, len);
			}
		} finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		}
	}

	private File getPathTranslated(String pathInfo) throws ServletException, UnsupportedEncodingException {
		// File tmpFile = tmpFileMap.get(pathInfo);
		// String enc = (File.separatorChar == '/')? "UTF-8":"ms932";
		// String decodedValue = URLDecoder.decode(pathInfo, enc);
		// return new File(tmpFile.getAbsolutePath());
		return tmpFileMap.get(pathInfo);
	}

	public static URL createTempFile(String baseURI, File file) throws IOException {
		File tmpDirectory = new File(System.getProperty("java.io.tmpdir"), "sqs");
		tmpDirectory.mkdirs();
		File tmpFile = fileMap.get(file);
		if (tmpFile == null) {
			tmpFile = File.createTempFile("sqs", '.' + FileUtil.getSuffix(file), tmpDirectory);
			tmpFile.deleteOnExit();
			fileMap.put(file, tmpFile);
		}
		FileUtil.copy(file, tmpFile);
		String tmpFileName = tmpFile.getName();
		String pathInfo = "/tmp/" + tmpFileName;
		TempFileContentServlet.tmpFileMap.put(tmpFileName, tmpFile);
		return new URL(baseURI + pathInfo);
	}

	/*
	 * private static SessionSource getSessionSource(){ return
	 * ExigridEngine.getCurrentEngine
	 * ().getSessionManager().getSessionServiceHandler().getSessionSource(); }
	 * 
	 * public static String getPathInfo(File file){ SessionSource sessionSource
	 * = getSessionSource(); File targetDirectory = getTargetDirectory(file,
	 * sessionSource.getSourceDirectoryRoot(),
	 * sessionSource.getResultDirectoryRoot()); return
	 * "/"+getContextString()+"/"
	 * +targetDirectory.getName()+"/"+file.getAbsolutePath
	 * ().substring(targetDirectory.getAbsolutePath().length()); }
	 * 
	 * private static File getTargetDirectory(File file, File
	 * sourceDirectoryRoot, File resultDirectoryRoot) { boolean
	 * isSourcePathStart =
	 * file.getAbsolutePath().startsWith(sourceDirectoryRoot.getAbsolutePath());
	 * boolean isResultPathStart =
	 * file.getAbsolutePath().startsWith(resultDirectoryRoot.getAbsolutePath());
	 * 
	 * if(isSourcePathStart && isResultPathStart){
	 * if(sourceDirectoryRoot.getAbsolutePath().length() <=
	 * resultDirectoryRoot.getAbsolutePath().length()){ return
	 * resultDirectoryRoot; }else{ return sourceDirectoryRoot; } }else{
	 * if(isSourcePathStart){ return sourceDirectoryRoot; }
	 * if(isResultPathStart){ return resultDirectoryRoot; } } return null; }
	 */

}
