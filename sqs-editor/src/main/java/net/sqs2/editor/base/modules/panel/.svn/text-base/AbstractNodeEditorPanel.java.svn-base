/*

 AbstractNodeEditorPanel.java
 
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
 
 Created on 2004/08/16

 */
package net.sqs2.editor.base.modules.panel;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;

import net.sqs2.editor.base.modules.AbstractNodeEditor;

/**
 * @author hiroya
 * 
 */
public abstract class AbstractNodeEditorPanel extends Box {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AbstractNodeEditor editor;

	public AbstractNodeEditorPanel(AbstractNodeEditor editor) {
		super(BoxLayout.Y_AXIS);
		this.editor = editor;
	}

	public void initSize() {
		// setSize(new Dimension(200, getPreferredHeight()));
		setPreferredSize(new Dimension(200, getPreferredHeight()));
	}

	public AbstractNodeEditor getEditor() {
		return editor;
	}

	public int getPreferredHeight() {
		return 14 * 4;
	}

	public abstract boolean updateNodeValue();

}
