/*

 SQSNodeTree.java
 
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
 
 Created on 2004/08/08

 */
package net.sqs2.editor.sqs.swing;

//import java.util.List;

import javax.swing.JComponent;
import javax.swing.tree.TreePath;

import net.sqs2.editor.base.swing.NodeTreePane;
import net.sqs2.editor.base.swing.SourceEditorMediator;
import net.sqs2.editor.base.swing.SourceEditorTreeCellRenderer;
import net.sqs2.exsed.source.DOMTreeSource;

import org.w3c.dom.Node;

/**
 * @author hiroya
 * 
 */
public class SQSNodeTreePane extends NodeTreePane {
	public static final long serialVersionUID = 0;

	public SQSNodeTreePane(SourceEditorMediator mediator, DOMTreeSource source, final JComponent editorPane) {
		super(mediator, source, editorPane);
		if (getModel() != null) {
			expandRow(0);// expand root
			expandRow(2);// expand body
			expandRow(1);// expand head
			for (int i = this.getRowCount() - 1; 0 <= i; i--) {
				TreePath path = this.getPathForRow(i);
				if ("section".equals(((Node) path.getLastPathComponent()).getLocalName())) {
					this.expandRow(i);
				}

			}
		}
	}

	public void setupRenderer(SourceEditorTreeCellRenderer renderer) {
	}

	public void initialize() {
	}

}
