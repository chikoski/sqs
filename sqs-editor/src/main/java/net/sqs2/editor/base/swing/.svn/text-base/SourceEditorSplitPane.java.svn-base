/*

 SourceEditorSplitPane.java
 
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
 
 Created on 2004/08/01

 */
package net.sqs2.editor.base.swing;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sqs2.exsed.source.DOMTreeSource;

/**
 * @author hiroya
 * 
 */
public abstract class SourceEditorSplitPane extends JSplitPane {
	private static final long serialVersionUID = 1L;
	SourceEditorMediator mediator;
	DOMTreeSource source;
	NodeTreePane treePane;
	JComponent editorPane;
	JScrollPane editorScrollPane;

	public SourceEditorSplitPane(SourceEditorMediator mediator, DOMTreeSource source, int width) {
		this.mediator = mediator;
		this.source = source;
		this.editorPane = createEditorPane();
		this.treePane = createSourceEditorTree(mediator);
		final JScrollPane treeScrollPane = new JScrollPane(treePane);
		this.editorScrollPane = new JScrollPane(editorPane);
		this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		this.setOneTouchExpandable(true);
		this.setDividerLocation((int) (width * 0.4));
		this.setLeftComponent(treeScrollPane);
		this.setRightComponent(editorScrollPane);

		this.editorScrollPane.setWheelScrollingEnabled(true);
		editorScrollPane.getVerticalScrollBar().setUnitIncrement(14);
		ChangeListener viewportChangeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent ev) {
				treeScrollPane.getViewport().updateUI();
			}
		};
		treeScrollPane.getViewport().addChangeListener(viewportChangeListener);
		editorScrollPane.getViewport().addChangeListener(viewportChangeListener);

		this.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ev) {
				getMediator().menuBarMediator.updateMenu();
			}
		});
	}

	private SourceEditorMediator getMediator() {
		return mediator;
	}

	private JComponent createEditorPane() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		return panel;
	}

	public DOMTreeSource getSource() {
		return source;
	}

	public JScrollPane getEditorScrollPane() {
		return editorScrollPane;
	}

	public JComponent getEditorPane() {
		return editorPane;
	}

	public NodeTreePane getTreePane() {
		return treePane;
	}

	public abstract NodeTreePane createSourceEditorTree(final SourceEditorMediator mediator);
}
