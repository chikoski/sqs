/*

 SuffixBasedFileFilter.java

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

 Created on 2004/08/21

 */
package net.sqs2.swing;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class SuffixBasedFileFilter extends FileFilter {

	String[][] suffixDescriptionArray;

	public SuffixBasedFileFilter(String suffix, String description) {
		this.suffixDescriptionArray = new String[][] { { suffix, description } };
	}

	public SuffixBasedFileFilter(String[][] suffixDescriptionArray) {
		this.suffixDescriptionArray = suffixDescriptionArray;
	}

	public boolean accept(File file) {
		String name = file.getName();
		for (String[] suffixDescription : suffixDescriptionArray) {
			String suffix = suffixDescription[0];
			if (file.isDirectory() || (suffix != null && name.endsWith(suffix))) {
				return true;
			}
		}
		return false;
	}

	public String getDescription() {
		return suffixDescriptionArray[0][1];
	}

	public String getSuffix() {
		return suffixDescriptionArray[0][0];
	}
}
