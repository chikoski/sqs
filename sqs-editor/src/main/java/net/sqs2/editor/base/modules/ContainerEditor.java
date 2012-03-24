/*

 NullEditor.java
 
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
 
 Created on 2004/08/13

 */
package net.sqs2.editor.base.modules;

import java.util.LinkedList;

import javax.swing.Box;

import net.sqs2.editor.base.modules.resource.EditorResource;
import net.sqs2.editor.base.swing.EditorResourceFactory;
import net.sqs2.editor.base.swing.SourceEditorMediator;
import net.sqs2.exsed.source.DOMTreeSource;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author hiroya
 * 
 */
public class ContainerEditor extends AbstractNodeEditor {
	public static final long serialVersionUID = 0;
	LinkedList<AbstractNodeEditor> nodeEditors;

	public ContainerEditor(SourceEditorMediator mediator, DOMTreeSource source, Node node,
			EditorResource resource) {
		super(mediator, source, node, resource);
		NodeList list = node.getChildNodes();
		nodeEditors = new LinkedList<AbstractNodeEditor>();
		Box comp = Box.createVerticalBox();
		for (int i = 0; i < list.getLength(); i++) {
			Node item = list.item(i);
			if (isEditable(item)) {
				EditorResourceFactory factory = mediator.getEditorResourceFactory();
				EditorResource subResource = factory.getEditorResource(item);
				if (subResource != null) {
					AbstractNodeEditor subcomp = factory.create(mediator, source, item);
					nodeEditors.add(subcomp);
					comp.add(subcomp);
					addPreferredHeight(subcomp.getPreferredHeight());
				}
			}
		}
		initSize();
		add(comp);
	}

	public int getHeight() {
		return prefHeight;
	}

	public boolean updateNodeValue() {
		boolean isUpdated = false;
		for (int i = 0; i < nodeEditors.size(); i++) {
			if (isEditable(node.getChildNodes().item(i))) {
				if (((AbstractNodeEditor) nodeEditors.get(i)).updateNodeValue()) {
					isUpdated = true;
				}
			}
		}
		if (isUpdated) {
			super.updateNodeValue();
			return true;
		} else {
			return false;
		}
	}

	public boolean isEditable(Node item) {
		return item.getNodeType() != Node.TEXT_NODE;
	}
}
