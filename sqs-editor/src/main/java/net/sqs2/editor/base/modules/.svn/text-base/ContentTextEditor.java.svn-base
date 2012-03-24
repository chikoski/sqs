/*

 ContentTextEditor.java
 
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
package net.sqs2.editor.base.modules;

import java.awt.Dimension;

import javax.swing.BoxLayout;

import net.sqs2.editor.base.modules.panel.ContentTextPanel;
import net.sqs2.editor.base.modules.resource.EditorResource;
import net.sqs2.editor.base.swing.SourceEditorMediator;
import net.sqs2.exsed.source.DOMTreeSource;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author hiroya
 * 
 */
public class ContentTextEditor extends AbstractNodeEditor {
	public static final long serialVersionUID = 0;
	ContentTextEditor[] contentTextEditor;
	ContentTextPanel panel;

	public ContentTextEditor(SourceEditorMediator mediator, DOMTreeSource source, Node node,
			EditorResource resource) {
		super(mediator, source, node, resource);
		if (node.getNodeType() == Node.TEXT_NODE) {
			add((panel = new ContentTextPanel(this, node)));
			prefHeight += panel.getPreferredHeight();
		} else {
			NodeList list = node.getChildNodes();
			contentTextEditor = new ContentTextEditor[list.getLength()];
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			for (int i = 0; i < list.getLength(); i++) {
				Node item = list.item(i);
				EditorResource subResource = mediator.getEditorResourceFactory().getEditorResource(item);
				contentTextEditor[i] = new ContentTextEditor(mediator, source, item, subResource);
				contentTextEditor[i].setPreferredSize(new Dimension(200, contentTextEditor[i].getHeight()));
				contentTextEditor[i].setSize(new Dimension(200, contentTextEditor[i].getHeight()));
				prefHeight += contentTextEditor[i].getPreferredHeight();
				add(contentTextEditor[i]);
			}
		}
		initSize();
	}

	public boolean updateNodeValue() {
		if (node.getNodeType() == Node.TEXT_NODE) {
			if (panel.updateNodeValue()) {
				super.updateNodeValue();
				return true;
			} else {
				return false;
			}
		} else {
			boolean isUpdated = false;
			for (int i = 0; i < contentTextEditor.length; i++) {
				if (contentTextEditor[i].updateNodeValue()) {
					isUpdated = true;
				}
			}
			if (isUpdated) {
				super.updateNodeValue();
				return true;
			} else {
				return false;
			}
		}
	}
}
