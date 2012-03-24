/*

 MatrixFormsWidthRatioPanel.java
 
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
package net.sqs2.editor.sqs.modules.panel;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;

import net.sqs2.editor.base.modules.AbstractNodeEditor;
import net.sqs2.editor.base.modules.EditorUtil;
import net.sqs2.editor.base.modules.panel.AbstractNodeEditorPanel;
import net.sqs2.editor.base.swing.Messages;
import net.sqs2.xmlns.SQSNamespaces;

/**
 * @author hiroya
 * 
 */
public class MatrixFormsWidthRatioPanel extends AbstractNodeEditorPanel {
	public static final long serialVersionUID = 0;
	static double DEFAULT_RATIO = 0.3;
	SpinnerModel ratioSpinnerModel;
	transient ChangeListener listener;
	Double ratio;

	public MatrixFormsWidthRatioPanel(AbstractNodeEditor editor) {
		super(editor);
		ratio = getEditor().doubleValueOf("@sqs:form-width-ratio");
		JSpinner ratioSpinner = createRatioSpinner(ratio);
		JComponent body = Box.createHorizontalBox();
		body.setBorder(new TitledBorder(EditorUtil.LOWERED_BORDER, Messages.FORM_LAYOUT_LABEL));
		JComponent ratioForm = EditorUtil.createSpinnerForm(Messages.FORM_LAYOUT_RATIO_LABEL + ":",
				ratioSpinner);
		if (editor.getSource().isReadOnly()) {
			ratioSpinner.setEnabled(false);
		}
		body.add(ratioForm);
		// body.add(new );
		add(body);
		initSize();
	}

	public int getPreferredHeight() {
		return 40;
	}

	private JSpinner createRatioSpinner(Double ratio) {
		ratioSpinnerModel = createRatioModel(ratio);
		JSpinner ratioSpinner = new JSpinner(ratioSpinnerModel);
		ratioSpinner.addFocusListener(getEditor().getUpdateListener());
		ratioSpinner.addChangeListener(getEditor().getUpdateListener());
		return ratioSpinner;
	}

	private SpinnerNumberModel createRatioModel(Double value) {
		return new SpinnerNumberModel(value.doubleValue(), 0.10, 1.0, 0.05);
	}

	public boolean updateNodeValue() {
		if (ratio == null || !ratio.equals(this.ratioSpinnerModel.getValue())) {
			getEditor().updateAttributeValue(".", SQSNamespaces.SQS2004_URI, "sqs", "form-width-ratio",
					this.ratioSpinnerModel.getValue());
			return true;
		} else {
			return false;
		}
	}
}
