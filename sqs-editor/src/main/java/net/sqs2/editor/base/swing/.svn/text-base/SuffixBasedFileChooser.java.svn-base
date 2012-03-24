/*

 SuffixBasedFileChooser.java
 
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
 
 Created on 2004/08/21

 */
package net.sqs2.editor.base.swing;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileView;

import net.sqs2.swing.SuffixBasedFileFilter;

/**
 * @author hiroya
 * 
 */
public abstract class SuffixBasedFileChooser extends JFileChooser {
	private static final long serialVersionUID = 1L;

	public SuffixBasedFileChooser(SuffixBasedFileFilter filter) {
		this();
		this.setFileFilter(filter);
	}

	public SuffixBasedFileChooser() {
		this.setFileView(createFileView());
	}

	// public abstract FileFilter createFileFilter();
	public abstract FileView createFileView();

	public abstract String getSuffix();
}
