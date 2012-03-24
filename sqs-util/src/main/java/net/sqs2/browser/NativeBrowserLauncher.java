/*
 * 

 NativeBrowserLauncher.java

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
package net.sqs2.browser;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.sqs2.lang.JVMUtil;

import org.mortbay.util.URIUtil;

public class NativeBrowserLauncher implements BrowserLauncher {
	public NativeBrowserLauncher() {
	}

	public void showDocument(URL url) throws IOException {
		showDocument(url.toString());
	}

	public void showDocument(String url) throws IOException {
		openURL(url);
	}

	public void showDocument(File file) throws IOException {
		if (File.separatorChar == '\\') {
			showDocument(new URL("file:///"
					+ URIUtil.encodeString(null, file.getAbsolutePath().replace("\\", "/"), "MS932")));
		} else {
			showDocument(file.toURI().toURL());
		}
	}

	private static int jvmTypeCode = 0;

	/**
	 * The first parameter that needs to be passed into Runtime.exec() to open
	 * the default web browser on Windows.
	 */
	private final static String FIRST_WINDOWS_PARAMETER = "/c";

	/**
	 * The second parameter for Runtime.exec() on Windows.
	 */
	private final static String SECOND_WINDOWS_PARAMETER = "start";

	/**
	 * The third parameter for Runtime.exec() on Windows. This is a "title"
	 * parameter that the command line expects. Setting this parameter allows
	 * URLs containing spaces to work.
	 */
	private final static String THIRD_WINDOWS_PARAMETER = "\"\"";

	private static int getJVMTypeCode() {
		if (jvmTypeCode == 0) {
			jvmTypeCode = JVMUtil.getJVMTypeCode();
		}
		return jvmTypeCode;
	}

	/**
	 * Attempts to open the default web browser to the given URL.
	 * 
	 *@param url
	 *            The URL to open
	 *@throws IOException
	 *             If the web browser could not be located or does not run
	 */
	public static void openURL(String url) throws IOException {
		int jvmTypeCode = getJVMTypeCode();

		String browser = null;

		switch (jvmTypeCode) {
		case JVMUtil.WINDOWS_9x:
			browser = "command.exe";
			break;
		case JVMUtil.WINDOWS_NT:
			browser = "cmd.exe";
			break;
		default:
			browser = "firefox";
		}

		switch (jvmTypeCode) {
		case JVMUtil.WINDOWS_9x:
		case JVMUtil.WINDOWS_NT:
			// Add quotes around the URL to allow ampersands and other special
			// characters to work.
			Process process = Runtime.getRuntime().exec(
					new String[] { (String) browser, FIRST_WINDOWS_PARAMETER, SECOND_WINDOWS_PARAMETER,
							THIRD_WINDOWS_PARAMETER, '"' + url + '"' });
			// This avoids a memory leak on some versions of Java on Windows.
			// That's hinted at in
			// <http://developer.java.sun.com/developer/qow/archive/68/>.
			try {
				process.waitFor();
				process.exitValue();
			} catch (InterruptedException ie) {
				throw new IOException("InterruptedException while launching browser: " + ie.getMessage());
			}
			break;
		case JVMUtil.OTHER:
			// Assume that we're on Unix and that Firefox is installed
			try {
				Runtime.getRuntime().exec(new String[] { browser, url }).waitFor();
			} catch (InterruptedException ie) {
				throw new IOException("InterruptedException while launching browser: " + ie.getMessage());
			}
			break;
		default:
			// This should never occur, but if it does, we'll try the simplest
			// thing possible
			Runtime.getRuntime().exec(new String[] { (String) browser, url });
			break;
		}
	}

}
