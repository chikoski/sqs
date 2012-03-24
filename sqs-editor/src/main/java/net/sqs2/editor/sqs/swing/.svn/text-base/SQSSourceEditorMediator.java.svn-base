/*

 SQSSourceEditorMediator.java
 
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
 
 Created on Jul 20, 2004

 */
package net.sqs2.editor.sqs.swing;

import java.awt.BorderLayout;
import java.io.File;
import java.net.URL;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.transform.URIResolver;

import net.sqs2.editor.base.swing.EditorResourceFactory;
import net.sqs2.editor.base.swing.NodeTreePane;
import net.sqs2.editor.base.swing.SourceEditorMediator;
import net.sqs2.editor.base.swing.SourceEditorMenuBarMediator;
import net.sqs2.editor.base.swing.SourceEditorSplitPane;
import net.sqs2.editor.base.swing.SourceEditorTabbedPane;
import net.sqs2.editor.base.swing.SourceEditorToolBar;
import net.sqs2.exsed.source.DOMTreeSource;
import net.sqs2.exsed.source.SourceManager;
import net.sqs2.net.ClassURIResolver;
import net.sqs2.source.SQSSourceManager;
import net.sqs2.swing.IconFactory;
import net.sqs2.translator.impl.PageSetting;

/**
 * @author hiroya
 * 
 */
public class SQSSourceEditorMediator extends SourceEditorMediator {

	JPanel backgroundPanel = null;
	static URIResolver uriResolver = new ClassURIResolver();

	public SQSSourceEditorMediator(URL url) throws Exception {
		super(url);
	}

	public SQSSourceEditorMediator(File file) throws Exception {
		super(file);
	}

	public SQSSourceEditorMediator() throws Exception {
		super();
	}

	public String getTitle() {
		return SourceEditorLauncher.TITLE;
	}

	public EditorResourceFactory createEditorResourceFactory() {
		return new SQSEditorResourceFactory();
	}

	public JPanel getBackgroundPanel() {
		if (backgroundPanel == null) {
			backgroundPanel = new JPanel();
			backgroundPanel.setLayout(new BorderLayout());
			JLabel titleLabel = new JLabel("Shared Questionnaire System");
			titleLabel.setEnabled(false);
			Box titlePanel = Box.createHorizontalBox();
			titlePanel.add(titleLabel);
			titlePanel.add(new JLabel(IconFactory.create("wizard.gif")));
			backgroundPanel.add(titlePanel, BorderLayout.EAST);
		}
		return backgroundPanel;
	}

	public SourceManager createSourceManager() {
		return new SQSSourceManager();
	}

	public SourceEditorMenuBarMediator createSourceEditorMenuBarMediator() {
		return new SQSSourceEditorMenuBarMediator(this);
	}

	public SourceEditorToolBar createSourceEditorToolBar() {
		return new SQSSourceEditorToolBar(this);
	}

	@Override
	public SourceEditorTabbedPane createSourceEditorTabbedPane(final SourceEditorMediator mediator) {
		return new SourceEditorTabbedPane(mediator) {
			public static final long serialVersionUID = 0;

			public SourceEditorSplitPane createSourceEditorSplitPane(final SourceEditorMediator mediator, DOMTreeSource source, int width) {
				return new SourceEditorSplitPane(mediator, source, width) {
					public static final long serialVersionUID = 0;

					public NodeTreePane createSourceEditorTree(final SourceEditorMediator mediator) {
						return new SQSNodeTreePane(mediator, getSource(), getEditorPane());
					}
				};
			}

			public Icon createTabIcon(DOMTreeSource source) {
				return IconFactory.create("sqs-tiny.gif", "SQS Source");
			}
		};
	}
	
}
