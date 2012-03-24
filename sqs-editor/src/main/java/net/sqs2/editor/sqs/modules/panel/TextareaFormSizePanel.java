/*

 TextareaSizePanel.java
 
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

import net.sqs2.editor.base.modules.AbstractNodeEditor;
import net.sqs2.editor.base.modules.EditorUtil;
import net.sqs2.editor.base.modules.panel.AbstractNodeEditorPanel;
import net.sqs2.editor.base.swing.Messages;
import net.sqs2.xml.XPathUtil;
import net.sqs2.xmlns.SQSNamespaces;

import org.w3c.dom.Element;

/**
 * @author hiroya
 * 
 */
public class TextareaFormSizePanel extends AbstractNodeEditorPanel {
	public static final long serialVersionUID = 0;
	static int DEFAULT_WIDTH = 400;
	static int DEFAULT_HEIGHT = 30;

	int width;
	int height;

	SpinnerModel widthSpinnerModel;
	SpinnerModel heightSpinnerModel;

	public TextareaFormSizePanel(AbstractNodeEditor editor) {
		super(editor);
		Element elem = (Element) editor.getNode();
		JComponent widthForm = createWidthForm(elem);
		JComponent heightForm = createHeightForm(elem);
		JComponent sizePanel = Box.createHorizontalBox();
		sizePanel.setBorder(new TitledBorder(EditorUtil.LOWERED_BORDER, Messages.FORM_TABLE_SIZE_LABEL));
		sizePanel.add(Box.createHorizontalGlue());
		sizePanel.add(Box.createHorizontalStrut(24));
		sizePanel.add(widthForm);
		sizePanel.add(Box.createHorizontalGlue());
		sizePanel.add(heightForm);
		sizePanel.add(Box.createHorizontalStrut(24));
		add(sizePanel);
		initSize();
	}

	private JComponent createHeightForm(Element elem) {
		this.height = XPathUtil.getIntValue(elem, "@sqs:height");
		JSpinner heightSpinner = createHeightSpinner(height);
		heightSpinner.addChangeListener(getEditor().getUpdateListener());
		heightSpinner.addFocusListener(getEditor().getUpdateListener());
		JComponent heightForm = EditorUtil.createSpinnerForm(Messages.FORM_TABLE_SIZE_HEIGHT + ":",
				heightSpinner);
		if (getEditor().getSource().isReadOnly()) {
			heightSpinner.setEnabled(false);
		}
		return heightForm;
	}

	private JComponent createWidthForm(Element elem) {
		this.width = XPathUtil.getIntValue(elem, "@sqs:width");
		JSpinner widthSpinner = createWidthSpinner(width);
		widthSpinner.addFocusListener(getEditor().getUpdateListener());
		widthSpinner.addChangeListener(getEditor().getUpdateListener());
		JComponent widthForm = EditorUtil.createSpinnerForm(Messages.FORM_TABLE_SIZE_WIDTH + ":",
				widthSpinner);
		if (getEditor().getSource().isReadOnly()) {
			widthSpinner.setEnabled(false);
		}
		return widthForm;
	}

	private JSpinner createWidthSpinner(int width) {
		JSpinner widthSpinner = new JSpinner(createWidthModel(width));
		widthSpinner.setValue(Integer.valueOf(width));
		this.widthSpinnerModel = widthSpinner.getModel();
		return widthSpinner;
	}

	private JSpinner createHeightSpinner(int height) {
		JSpinner heightSpinner = new JSpinner(createHeightModel(height));
		heightSpinner.setValue(Integer.valueOf(height));
		this.heightSpinnerModel = heightSpinner.getModel();
		return heightSpinner;
	}

	private SpinnerNumberModel createWidthModel(int value) {
		return new SpinnerNumberModel(value, 0, 505, 1);
	}

	private SpinnerNumberModel createHeightModel(int value) {
		return new SpinnerNumberModel(value, 0, 841, 1);
	}

	public int getPreferredHeight() {
		return 30;
	}

	public boolean updateNodeValue() {
		if (width != ((Integer) widthSpinnerModel.getValue()).intValue()
				|| height != ((Integer) heightSpinnerModel.getValue()).intValue()) {
			getEditor().updateAttributeValue(".", SQSNamespaces.SQS2004_URI, "sqs", "width",
					widthSpinnerModel.getValue());
			getEditor().updateAttributeValue(".", SQSNamespaces.SQS2004_URI, "sqs", "height",
					heightSpinnerModel.getValue());
			return true;
		} else {
			return false;
		}
	}
}
