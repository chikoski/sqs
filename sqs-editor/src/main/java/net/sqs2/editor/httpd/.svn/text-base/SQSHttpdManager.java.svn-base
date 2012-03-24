/*

 SQSHttpdManager.java

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

import java.util.ResourceBundle;

public class SQSHttpdManager {
	final static ResourceBundle prop = java.util.ResourceBundle.getBundle("editor");
	public final static Integer HTTP_PORT = new Integer(prop.getString("sourceeditor.http.port"));

	private static SourceEditorHttpd exsedHttpd = null;

	public static SourceEditorHttpd getHttpd() throws Exception {
		synchronized (SQSHttpdManager.class) {
			if (SQSHttpdManager.exsedHttpd == null) {
				int port = SQSHttpdManager.HTTP_PORT.intValue();
				SQSHttpdManager.exsedHttpd = new SourceEditorHttpd(port);
				try {
					SQSHttpdManager.exsedHttpd.start();
				} catch (java.net.BindException ignore) {
				}
				while (!SQSHttpdManager.exsedHttpd.isStarted()) {
					Thread.yield();
				}
			}
			//ProxySelector.setDefault(null);
		}
		return SQSHttpdManager.exsedHttpd;
	}
}
