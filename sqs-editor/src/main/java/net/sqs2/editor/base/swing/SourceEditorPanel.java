/*

 SourceEditorFrame.java
 
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
 
 Created on 2004/07/31

 */
package net.sqs2.editor.base.swing;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * @author hiroya
 * 
 */
public class SourceEditorPanel extends JPanel {
	public static final long serialVersionUID = 0;

	public SourceEditorPanel(SourceEditorToolBar toolBar, Component mainPanel) {
		super();
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(0, 7, 7, 5));
		add(toolBar, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
	}
}
