/*

 IconFactory.java

 Copyright 2004 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2004/08/08

 */
package net.sqs2.swing;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import net.sqs2.editor.SourceEditorJarURIContext;

/**
 * @author hiroya
 * 
 */
public class IconFactory {

	public static ImageIcon create(String filename) {
		return create(filename, "");
	}

	public static ImageIcon create(String filename, String description) {
		try {
			URL url = new URL(SourceEditorJarURIContext.getImageBaseURI() + filename);
			return new ImageIcon(url, description);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static ImageIcon create(String filename, String overlayText, int overlayTextSize, int overlayTextXOffset) {
		try {
			URL url = new URL(SourceEditorJarURIContext.getImageBaseURI() + filename);
			return new TextOverlayImageIcon(url, overlayText, overlayTextSize, overlayTextXOffset);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
