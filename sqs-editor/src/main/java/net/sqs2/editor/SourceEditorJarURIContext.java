/*

 SourceEditorJarURIContext.java
 
 Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
 Created on 2005/08/09

 */
package net.sqs2.editor;

import java.net.URL;

import net.sqs2.net.ClassURLStreamHandlerFactory;

public class SourceEditorJarURIContext {

	private static final String JAR_URI_CONTEXT = net.sqs2.editor.SourceEditorJarURIContext.class
			.getCanonicalName();

	static {
		URL.setURLStreamHandlerFactory(ClassURLStreamHandlerFactory.getSingleton());
	}

	public static String geBaseURI() {
		return "class://" + JAR_URI_CONTEXT + "/";
	}

	public static String getImageBaseURI() {
		return geBaseURI() + "image/";
	}

	public static String getTemplateBaseURI() {
		return geBaseURI() + "template/";
	}

}
