/*

 EditMenuFeature.java
 
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
 
 Created on 2005/02/03

 */
package net.sqs2.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import net.sqs2.editor.SourceEditorJarURIContext;
import net.sqs2.editor.base.swing.Messages;

/**
 * @author hiroya
 * 
 */
class EditMenuFeature {
	public static final long serialVersionUID = 0L;

	JPopupMenu popup;

	JMenuItem undoMenuItem;

	JMenuItem redoMenuItem;

	UndoManager undo = null;

	JTextComponent textcomp = null;

	static Icon undoIcon;

	protected static ImageIcon createImageIcon(String file) {
		try {
			URL url = new URL(SourceEditorJarURIContext.getImageBaseURI() + file);
			return new ImageIcon(url);
		} catch (Exception ignore) {// MalformedURL
			throw new RuntimeException("cannot resolve:" + file); //$NON-NLS-1$
		}
	}

	public EditMenuFeature(JTextComponent textcomp) {
		this.textcomp = textcomp;
		initPopupMenu();
		initUndoFeature();
		textcomp.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3 && !popup.isVisible()) {
					showPopup(e.getX(), e.getY());
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3 && !popup.isVisible()) {
					showPopup(e.getX(), e.getY());
				}
			}
		});

	}

	void showPopup(int x, int y) {
		popup.show(textcomp, x, y);
	}

	public JPopupMenu getPopup() {
		return popup;
	}

	public void setUndoManager(UndoManager undoManager) {
		undo = undoManager;
	}

	private void initPopupMenu() {
		this.popup = new JPopupMenu();
		undoMenuItem = new JMenuItem(Messages.UNDO_LABEL);
		undoMenuItem.setIcon(createImageIcon("Undo16.gif"));
		redoMenuItem = new JMenuItem(Messages.REDO_LABEL);
		redoMenuItem.setIcon(createImageIcon("Redo16.gif"));
		JMenuItem cutMenuItem = new JMenuItem(Messages.CUT_LABEL);
		cutMenuItem.setIcon(createImageIcon("Cut16.gif"));
		JMenuItem copyMenuItem = new JMenuItem(Messages.COPY_LABEL);
		copyMenuItem.setIcon(createImageIcon("Copy16.gif"));
		JMenuItem pasteMenuItem = new JMenuItem(Messages.PASTE_LABEL);
		pasteMenuItem.setIcon(createImageIcon("Paste16.gif"));
		JMenuItem selectAllMenuItem = new JMenuItem(Messages.SELECT_ALL_LABEL);

		undoMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				undo();
			}
		});
		redoMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				redo();
			}
		});
		cutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				textcomp.cut();
			}
		});
		copyMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				textcomp.copy();
			}
		});
		pasteMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				textcomp.paste();
			}
		});
		selectAllMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				textcomp.selectAll();
			}
		});
		popup.add(undoMenuItem);
		popup.add(redoMenuItem);
		popup.addSeparator();
		popup.add(cutMenuItem);
		popup.add(copyMenuItem);
		popup.add(pasteMenuItem);
		popup.addSeparator();
		popup.add(selectAllMenuItem);

		undoMenuItem.setEnabled(false);
		redoMenuItem.setEnabled(false);
	}

	public void addEdit(UndoableEdit edit) {
		if (undo != null) {
			undo.addEdit(edit);
		}
	}

	public void undo() {
		try {
			if (undo != null && undo.canUndo()) {
				undo.undo();
				refreshEnabledStatus();
			}
		} catch (CannotUndoException ignore) {
		}
	}

	public void redo() {
		try {
			if (undo != null && undo.canRedo()) {
				undo.redo();
				refreshEnabledStatus();
			}
		} catch (CannotRedoException ignore) {
		}
	}

	void refreshEnabledStatus() {
		if (undo != null) {
			if (undo.canRedo()) {
				redoMenuItem.setEnabled(true);
			}
			if (undo.canUndo()) {
				undoMenuItem.setEnabled(true);
			}
		}
	}

	private void initUndoFeature() {
		Document doc = textcomp.getDocument();

		// Listen for undo and redo events
		doc.addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent ev) {
				undoMenuItem.setEnabled(true);
				addEdit(ev.getEdit());
				refreshEnabledStatus();
			}
		});

		// Create an undo action and add it to the text component
		textcomp.getActionMap().put("Undo", new AbstractAction("Undo") { //$NON-NLS-1$ //$NON-NLS-2$
					public final static long serialVersionUID = 0L;

					public void actionPerformed(ActionEvent ev) {
						undo();
					}
				});

		// Bind the undo action to ctl-Z
		textcomp.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo"); //$NON-NLS-1$ //$NON-NLS-2$

		// Create a redo action and add it to the text component
		textcomp.getActionMap().put("Redo", new AbstractAction("Redo") { //$NON-NLS-1$ //$NON-NLS-2$
					public final static long serialVersionUID = 0L;

					public void actionPerformed(ActionEvent ev) {
						redo();
					}
				});

		// Bind the redo action to ctl-Y
		textcomp.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo"); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
