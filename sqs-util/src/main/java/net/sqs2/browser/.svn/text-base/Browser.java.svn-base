/**

 Browser.java

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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.jnlp.UnavailableServiceException;

public class Browser {

	public static void showDocument(File file, URL url) {

		if (file != null) {
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				if ("file".equals(url.getProtocol()) && File.separatorChar == '\\') {
					new NativeBrowserLauncher().showDocument(url);
					return;
				}

				try {
					new Java6DesktopBrowserLauncher().showDocument(url);
					return;
				} catch (IOException ex1) {
				} catch (ClassCastException ex) {
				}

				try {
					new JNLPBrowserLauncher().showDocument(url);
					return;
				} catch (UnavailableServiceException ex2) {
				} catch (NullPointerException ex2) {
				}

				new NativeBrowserLauncher().showDocument(url);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
