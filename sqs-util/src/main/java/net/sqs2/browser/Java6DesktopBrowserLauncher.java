/*
 * 

 Java6DesktopBrowserLauncher.java

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

//import java.awt.Desktop;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Java6DesktopBrowserLauncher implements BrowserLauncher {
	public void showDocument(URL url) throws IOException {
		try {
			show(url.toURI());
		} catch (URISyntaxException ex) {
			throw new IOException(ex.getMessage());
		}
	}

	public void showDocument(String url) throws IOException {
		try {
			show(new URL(url).toURI());
		} catch (URISyntaxException ex) {
			throw new IOException(ex.getMessage());
		}
	}

	public void showDocument(File file) throws IOException {
		show(file.toURI());
	}

	private void show(URI uri) throws IOException {
		try {
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				if (uri.getScheme().startsWith("file")) {
					desktop.open(new File(uri.getPath()));
				} else {
					desktop.browse(uri);
				}
			} else {
				throw new IOException("Desktop is unsupported.");
			}
		} catch (NoClassDefFoundError ex) {
			throw new IOException("Desktop is unsupported.");
		}
	}
}
