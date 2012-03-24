/*
 * 

 JNLPBrowserLauncher.java

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

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import org.mortbay.util.URIUtil;

/**
 * @author hiroya
 * 
 */
public class JNLPBrowserLauncher implements BrowserLauncher {
	BasicService basicService = null;

	public JNLPBrowserLauncher() throws UnavailableServiceException {
		this.basicService = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
	}

	public void showDocument(URL url) throws IOException {
		this.basicService.showDocument(url);
	}

	public void showDocument(String url) throws IOException {
		this.basicService.showDocument(new URL(url));
	}

	public void showDocument(File file) throws IOException {
		if (File.separatorChar == '\\') {
			this.basicService.showDocument(new URL("file:///"
					+ URIUtil.encodeString(null, file.getAbsolutePath().replace("\\", "/"), "MS932")));
		} else {
			this.basicService.showDocument(file.toURI().toURL());
		}
	}
}
