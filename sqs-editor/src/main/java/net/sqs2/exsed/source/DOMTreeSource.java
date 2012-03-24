/*

 JDOMSource.java
 
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
 
 Created on 2004/08/03

 */
package net.sqs2.exsed.source;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.swing.tree.TreeModel;
import javax.swing.undo.UndoManager;
import javax.xml.parsers.ParserConfigurationException;

import net.sqs2.xml.XMLUtil;
import net.sqs2.xmlns.SQSNamespaces;

import org.apache.xerces.dom.DOMImplementationImpl;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;
import org.xml.sax.SAXException;

/**
 * @author hiroya
 * 
 */
public class DOMTreeSource extends Source {
	Document document;
	DOMTreeWalkerTreeModel model;

	public DOMTreeSource() throws SourceException {
	}

	public DOMTreeSource(File file, InputStream in, File originalFile) throws SourceException {
		super(file, in, originalFile);
	}

	public DOMTreeSource(URL url, boolean readonly, String title) throws SourceException {
		super(url, readonly, title);
	}

	public void initialize() throws SourceException {
		DOMImplementation domImpl = new DOMImplementationImpl();
		DocumentType docType = domImpl.createDocumentType("html", "public ID", "system ID");
		this.document = domImpl.createDocument(SQSNamespaces.XHTML2_URI, "html", docType);
	}

	public void initialize(File file) throws SourceException {
		try {
			this.document = createDocument(file);
			// this.document.normalize();
			this.model = createTreeModel(document);
		} catch (IOException e) {
			throw new SourceException(e);
		} catch (ParserConfigurationException e) {
			throw new SourceException(e);
		} catch (SAXException e) {
			throw new SourceException(e);
		}
	}

	public void initialize(InputStream stream) throws SourceException {
		try {
			this.document = createDocument(stream);
			// this.document.normalize();
			this.model = createTreeModel(document);
			stream.close();
		} catch (IOException e) {
			throw new SourceException(e);
		} catch (ParserConfigurationException e) {
			throw new SourceException(e);
		} catch (SAXException e) {
			throw new SourceException(e);
		}
	}

	HashMap<Node, UndoManager> undoManagerMap = new HashMap<Node, UndoManager>();
	HashMap<Node, javax.swing.text.Document> documentMap = new HashMap<Node, javax.swing.text.Document>();

	public UndoManager getUndoManager(Node node) {
		UndoManager undo = (UndoManager) undoManagerMap.get(node);
		return undo;
	}

	public javax.swing.text.Document getDocument(Node node) {
		return (javax.swing.text.Document) documentMap.get(node);
	}

	public void putUndoManager(Node node, UndoManager undoManager) {
		undoManagerMap.put(node, undoManager);
	}

	public void putDocument(Node node, javax.swing.text.Document document) {
		documentMap.put(node, document);
	}

	private DOMTreeWalkerTreeModel createTreeModel(Document document) {

		DOMImplementation domimpl = document.getImplementation();
		if (!domimpl.hasFeature("Traversal", "2.0")) {
			throw new RuntimeException("Traversal is not supported!");
		}

		DocumentTraversal traversal = (DocumentTraversal) document;
		NodeFilter filter = createNodeFilter();
		// This set of flags says to "show" all node types except comments
		int whatToShow = NodeFilter.SHOW_ALL & ~NodeFilter.SHOW_COMMENT;
		// Create a TreeWalker using the filter and the flags
		TreeWalker walker = traversal.createTreeWalker(document, whatToShow, filter, false);
		// Instantiate a TreeModel and a JTree to display it
		return new DOMTreeWalkerTreeModel(walker);
	}

	/**
	 * @return
	 */
	private NodeFilter createNodeFilter() {
		// For this demonstration, we create a NodeFilter that filters out
		// Text nodes containing only space; these just clutter up the tree
		NodeFilter filter = new NodeFilter() {
			public short acceptNode(Node n) {
				if (n.getNodeType() == Node.TEXT_NODE) {
					// Use trim() to strip off leading and trailing space.
					// If nothing is left, then reject the node
					if (((Text) n).getData().trim().length() == 0)
						return NodeFilter.FILTER_REJECT;
				}
				return NodeFilter.FILTER_ACCEPT;
			}
		};
		return filter;
	}

	public Document createDocument(File file) throws ParserConfigurationException, SAXException, IOException {
		return XMLUtil.createDocumentBuilder().parse(file);
	}

	public Document createDocument(InputStream stream) throws ParserConfigurationException, SAXException, IOException {
		return XMLUtil.createDocumentBuilder().parse(stream);
	}

	public Document getDocument() {
		return document;
	}

	public TreeModel getTreeModel() {
		return model;
	}

	public InputStream createInputStream() throws IOException {
		return XMLUtil.createInputStream(document.getDocumentElement());
	}
}
