/*

 NodeEditorModuleFactory.java
 
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
 
 Created on 2004/08/05

 */
package net.sqs2.editor.base.swing;

import java.lang.reflect.Constructor;

import net.sqs2.editor.base.modules.AbstractNodeEditor;
import net.sqs2.editor.base.modules.resource.EditorResource;
import net.sqs2.exsed.source.DOMTreeSource;

import org.w3c.dom.Node;

/**
 * @author hiroya
 * 
 */
public abstract class EditorResourceFactory {

	final static Class<?>[] CONST_PARAM_LIST = new Class[] { SourceEditorMediator.class, DOMTreeSource.class,
			Node.class, EditorResource.class };

	public EditorResourceFactory() {
	}

	public boolean isEditable(String localName, String uri) {
		EditorResource resource = ((EditorResource) (getEditorResource(localName, uri)));
		return (resource == null) ? false : resource.isSelectable;
	}

	public AbstractNodeEditor create(SourceEditorMediator mediator, DOMTreeSource source, Node node) {
		String uri = node.getNamespaceURI();
		String localName = node.getLocalName();
		EditorResource editorResource = getEditorResource(localName, uri);
		if (editorResource == null) {
			return null;
		}
		try {
			Class<? extends AbstractNodeEditor> clazz = editorResource.nodeEditorClass;
			Constructor<? extends AbstractNodeEditor> constructor = clazz.getConstructor(CONST_PARAM_LIST);
			return (AbstractNodeEditor) constructor.newInstance(new Object[] { mediator, source, node,
					editorResource });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getKey(String localName, String uri) {
		return "" + localName + "@" + uri;
	}

	public EditorResource getEditorResource(String localName, String uri) {
		return getEditorResource(getKey(localName, uri));
	}

	public EditorResource getEditorResource(Node node) {
		return getEditorResource(getKey(node.getLocalName(), node.getNamespaceURI()));
	}

	public abstract EditorResource getEditorResource(String key);
}
