/*

 SelectFormColsPanel.java
 
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
 
 Created on 2004/12/26

 */
package net.sqs2.editor.sqs.modules.panel;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import net.sqs2.editor.base.modules.AbstractNodeEditor;
import net.sqs2.editor.base.modules.EditorUtil;
import net.sqs2.editor.base.swing.Messages;
import net.sqs2.xmlns.SQSNamespaces;

/**
 * @author hiroya
 * 
 */
public class SelectFormColsPanel extends SelectFormPanel {
	public static final long serialVersionUID = 0;
	SpinnerNumberModel colsModel;
	int cols;

	public SelectFormColsPanel(AbstractNodeEditor editor) {
		super(editor);
		Box colsForm = new Box(BoxLayout.X_AXIS);
		colsForm.setBackground(getBackground());
		colsForm.setBorder(new TitledBorder(EditorUtil.LOWERED_BORDER, Messages.FORM_COLS_LAYOUT_LABEL));
		colsForm.add(createColsForm());
		add(colsForm);
		initSize();
	}

	private SpinnerNumberModel createColsModel() {
		this.cols = getEditor().intValueOf("@sqs:cols", 4);
		return new SpinnerNumberModel(cols, 1, 10, 1);
	}

	private JComponent createColsForm() {
		this.colsModel = createColsModel();
		JSpinner colsSpinner = new JSpinner(colsModel);
		JComponent colsForm = EditorUtil.createSpinnerForm(Messages.FORM_COLS_LAYOUT_NUM_LABEL + ":",
				colsSpinner);
		colsForm.setPreferredSize(new Dimension(100, 16));
		colsSpinner.addChangeListener(getEditor().getUpdateListener());
		colsSpinner.addFocusListener(getEditor().getUpdateListener());
		if (getEditor().getSource().isReadOnly()) {
			colsSpinner.setEnabled(false);
		}
		return colsForm;
	}

	public boolean updateNodeValue() {
		if (cols != ((Integer) colsModel.getValue()).intValue()) {
			getEditor().updateAttributeValue(".", SQSNamespaces.SQS2004_URI, "sqs", "cols",
					colsModel.getValue());
			return true;
		} else {
			return false;
		}
	}
}
