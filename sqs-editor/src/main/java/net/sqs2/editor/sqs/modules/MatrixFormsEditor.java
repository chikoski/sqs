/*

 MatrixFormsEditor.java
 
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
 
 Created on 2004/07/30

 */
package net.sqs2.editor.sqs.modules;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import net.sqs2.editor.base.modules.AbstractNodeEditor;
import net.sqs2.editor.base.modules.panel.AbstractNodeEditorPanel;
import net.sqs2.editor.base.modules.resource.EditorResource;
import net.sqs2.editor.base.swing.Messages;
import net.sqs2.editor.base.swing.SourceEditorMediator;
import net.sqs2.editor.sqs.modules.panel.MatrixFormsWidthRatioPanel;
import net.sqs2.exsed.source.DOMTreeSource;
import net.sqs2.xmlns.SQSNamespaces;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author hiroya
 * 
 */
public class MatrixFormsEditor extends AbstractFormEditor {
	public static final long serialVersionUID = 0;
	AbstractNodeEditorPanel widthRatioPanel;

	public MatrixFormsEditor(SourceEditorMediator mediator, DOMTreeSource source, Node node,
			EditorResource resource) {
		super(mediator, source, node, resource);
		widthRatioPanel = new MatrixFormsWidthRatioPanel(this);
		add(widthRatioPanel);

		JTabbedPane tabbedPane = new JTabbedPane();
		Box r = Box.createVerticalBox();
		Box c = Box.createVerticalBox();
		JScrollPane rowArrayScrollPane = new JScrollPane(r);
		JScrollPane columnArrayScrollPane = new JScrollPane(c);

		Element rowArray = ((Element) ((Element) node).getElementsByTagNameNS(SQSNamespaces.SQS2004_URI,
				"row-array").item(0));
		NodeList rowArrayList = rowArray.getElementsByTagNameNS(SQSNamespaces.XFORMS_URI, "group");
		/*
		 * EditorResource groupEditorResource =
		 * mediator.getEditorResourceFactory().getEditorResource("group",
		 * SQSNamespaces.XFORMS_URI);
		 */
		Element columnArray = ((Element) ((Element) node).getElementsByTagNameNS(SQSNamespaces.SQS2004_URI,
				"column-array").item(0));
		NodeList columnArrayList = columnArray.getChildNodes();

		for (int i = 0; i < rowArrayList.getLength(); i++) {
			AbstractNodeEditor editor = mediator.getEditorResourceFactory().create(mediator, source,
					rowArrayList.item(i));
			r.add(editor);
		}

		for (int i = 0; i < columnArrayList.getLength(); i++) {
			Node elem = columnArrayList.item(i);
			if (elem instanceof Element) {
				c.add(mediator.getEditorResourceFactory().create(mediator, source, elem));
			}
		}
		columnArrayScrollPane.getVerticalScrollBar().setUnitIncrement(8);
		rowArrayScrollPane.getVerticalScrollBar().setUnitIncrement(8);
		tabbedPane.add(Messages.ROWS_DEFINITION_LABEL, rowArrayScrollPane);
		tabbedPane.add(Messages.COLS_DEFINITION_LABEL, columnArrayScrollPane);
		tabbedPane.setPreferredSize(new Dimension(tabbedPane.getSize().width, 280));
		addPreferredHeight(200);
		add(tabbedPane);
		initSize();
	}

	public boolean updateNodeValue() {
		if (super.updateNodeValue() || widthRatioPanel.updateNodeValue()) {
			super.updateNodeValue(true);
			return true;
		} else {
			return false;
		}
	}
}
