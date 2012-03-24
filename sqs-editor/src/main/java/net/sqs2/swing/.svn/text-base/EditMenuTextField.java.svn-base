/*

 EditableTextarea.java

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

 Created on 2005/02/03

 */
package net.sqs2.swing;

import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.undo.UndoManager;

/**
 * @author hiroya
 * 
 */

public class EditMenuTextField extends JTextField {
	public static final long serialVersionUID = 0L;
	EditMenuFeature feature = null;

	public EditMenuTextField() {
		super();
		this.feature = new EditMenuFeature(this);
	}

	public JPopupMenu getPopup() {
		return this.feature.getPopup();
	}

	public void setUndoManager(UndoManager undo) {
		this.feature.setUndoManager(undo);
	}
}
