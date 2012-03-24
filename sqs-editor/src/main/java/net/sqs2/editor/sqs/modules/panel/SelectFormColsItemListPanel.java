/*

 SelectFormColsItemListPanel.java
 
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
 
 Created on 2004/12/27

 */
package net.sqs2.editor.sqs.modules.panel;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.sqs2.editor.base.modules.AbstractNodeEditor;
import net.sqs2.editor.base.modules.EditorUtil;
import net.sqs2.editor.base.modules.panel.AbstractNodeEditorPanel;
import net.sqs2.editor.base.swing.Messages;

/**
 * @author hiroya
 * 
 */
public class SelectFormColsItemListPanel extends SelectFormItemListPanel {
	public static final long serialVersionUID = 0;

	public SelectFormColsItemListPanel(AbstractNodeEditor editor) {
		super(editor);
	}

	public Item createItem(int i) {
		return new ItemWithCols(this, i);
	}

	public class ItemWithCols extends Item {
		public static final long serialVersionUID = 0;
		JSpinner colspanSpinner;
		JComponent itemFormEast;

		ItemWithCols(AbstractNodeEditorPanel itemListForm, int index) {
			super(itemListForm, index);
			itemFormEast = createFormItemEast();
			add(itemFormEast, BorderLayout.EAST);
		}

		private JComponent createFormItemEast() {
			colspanSpinner = new JSpinner(new SpinnerNumberModel(model.getColspan(index), 1, 9, 1));
			colspanSpinner.addChangeListener(updateListener);
			JComponent colspanForm = EditorUtil.createSpinnerForm("/"
					+ Messages.MARK_ITEM_DISPLAY_WIDTH_LABEL + ":", colspanSpinner);
			colspanSpinner.setSize(1, 1);
			final Box itemFormEast = Box.createHorizontalBox();
			itemFormEast.add(colspanForm);
			if (getEditor().getSource().isReadOnly()) {
				colspanSpinner.setEnabled(false);
			}
			return itemFormEast;
		}

		public boolean updateNodeValue() {
			return model.updateNodeValue(index, labelTextField.getText(), valueTextField.getText(),
					((Integer) colspanSpinner.getValue()).intValue());
		}
	}

}
