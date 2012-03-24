/*

 MetaEditor.java
 
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
 
 Created on 2004/08/14

 */
package net.sqs2.editor.sqs.modules;

import net.sqs2.editor.base.modules.AbstractNodeEditor;
import net.sqs2.editor.base.modules.panel.AbstractNodeEditorPanel;
import net.sqs2.editor.base.modules.resource.EditorResource;
import net.sqs2.editor.base.swing.SourceEditorMediator;
import net.sqs2.editor.sqs.modules.panel.MetaDescriptionPanel;
import net.sqs2.exsed.source.DOMTreeSource;

import org.w3c.dom.Node;

/**
 * @author hiroya
 * 
 */
public class MetaEditor extends AbstractNodeEditor {
	public static final long serialVersionUID = 0;
	AbstractNodeEditorPanel metaPanel;

	public MetaEditor(SourceEditorMediator mediator, DOMTreeSource source, Node node, EditorResource resource) {
		super(mediator, source, node, resource);
		metaPanel = new MetaDescriptionPanel(this);
		add(metaPanel);
		initSize();
	}

	public String getName() {
		return getNode().getAttributes().getNamedItem("name").getNodeValue();
	}

	public boolean updateNodeValue() {
		if (metaPanel.updateNodeValue()) {
			super.updateNodeValue();
			return true;
		} else {
			return false;
		}
	}
}
