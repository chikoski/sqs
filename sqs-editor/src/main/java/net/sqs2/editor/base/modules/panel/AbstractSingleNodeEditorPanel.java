/*

 AbstractSubModulePanel.java
 
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
 
 Created on 2004/08/15

 */
package net.sqs2.editor.base.modules.panel;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

import net.sqs2.editor.base.modules.AbstractNodeEditor;
import net.sqs2.swing.EditMenuTextArea;
import net.sqs2.xml.XPathUtil;

import org.w3c.dom.Node;

/**
 * @author hiroya
 * 
 */
public abstract class AbstractSingleNodeEditorPanel extends AbstractNodeEditorPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	EditMenuTextArea nodeValueTextArea;
	JComponent nodeValueEditor;
	String previousValue = null;
	UndoManager undoManager = null;

	public AbstractSingleNodeEditorPanel(AbstractNodeEditor editor) {
		super(editor);
		this.nodeValueTextArea = new EditMenuTextArea(3, 30);
		this.nodeValueEditor = createNodeValueEditor(nodeValueTextArea);
		initNodeValueEditor(editor);
		this.add(nodeValueEditor);
		initSize();
	}

	private void initNodeValueEditor(AbstractNodeEditor editor) {
		if (editor.getNode() != null) {
			try {
				Node node = (Node) editor.getNode();
				this.previousValue = XPathUtil.getStringValue(node, getDescriptionXPath());
				Document document = editor.getDocument();
				undoManager = editor.getUndoManager();
				if (document == null) {
					undoManager = new UndoManager();
					nodeValueTextArea.setText(previousValue);
					document = nodeValueTextArea.getDocument();
					editor.setUndoManager(undoManager);
					editor.setDocument(document);
					nodeValueTextArea.setUndoManager(undoManager);

				} else {
					nodeValueTextArea.setDocument(document);
					nodeValueTextArea.setUndoManager(undoManager);
				}
				if (editor.getSource().isReadOnly()) {
					nodeValueTextArea.setEnabled(false);
				}
			} catch (ClassCastException ignore) {
				ignore.printStackTrace();
			}
		}
	}

	public JComponent createNodeValueEditor(JTextArea nodeValueTextArea) {
		JComponent nodeValueEditor = Box.createHorizontalBox();
		JScrollPane scrollPane = new JScrollPane(nodeValueTextArea);
		scrollPane.setWheelScrollingEnabled(true);
		nodeValueEditor.add(scrollPane);
		nodeValueTextArea.addFocusListener(editor.getUpdateListener());
		return nodeValueEditor;
	}

	public abstract String getDescriptionXPath();

	public String getDescriptionLabel() {
		return "";
	}

	public boolean updateNodeValue() {
		if (previousValue == null || !previousValue.equals(nodeValueTextArea.getText())) {
			editor.updateNodeValue(getDescriptionXPath(), nodeValueTextArea.getText());
			return true;
		}
		return false;
	}

}
