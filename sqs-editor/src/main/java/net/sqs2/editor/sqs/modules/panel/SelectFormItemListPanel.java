/*

 SelectFormItemList.java
 
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
package net.sqs2.editor.sqs.modules.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.undo.UndoManager;

import net.sqs2.editor.base.modules.AbstractNodeEditor;
import net.sqs2.editor.base.modules.EditorUtil;
import net.sqs2.editor.base.modules.UpdateListener;
import net.sqs2.editor.base.modules.UpdateTarget;
import net.sqs2.editor.base.modules.panel.AbstractNodeEditorPanel;
import net.sqs2.editor.base.swing.Messages;
import net.sqs2.swing.EditMenuTextField;
import net.sqs2.xmlns.SQSNamespaces;

import org.w3c.dom.Element;

/**
 * @author hiroya
 * 
 */
public class SelectFormItemListPanel extends AbstractNodeEditorPanel implements UpdateTarget {
	public static final long serialVersionUID = 0;
	transient SelectFormItemListModel model;
	transient Icon itemIcon = null;
	Item[] itemList = null;
	Box itemListFormInnerPane;
	SelectFormItemListPanel self;

	transient Thread invalidValuesAlartThread = null;

	public SelectFormItemListPanel(AbstractNodeEditor editor) {
		super(editor);
		this.self = this;
		this.model = new SelectFormItemListModel(editor.getMediator(), editor.getSource(), (Element) editor
				.getNode());
		this.itemIcon = editor.getMediator().getCurrentTreePane().getIcon("item", SQSNamespaces.XFORMS_URI);
		this.itemListFormInnerPane = Box.createVerticalBox();
		JPanel itemListForm = new JPanel();
		itemListForm.setBorder(new TitledBorder(EditorUtil.LOWERED_BORDER, Messages.FORM_QUESTION_ITEMS));
		itemListForm.setBackground(editor.getResource().bgcolor);
		itemListForm.setLayout(new BorderLayout());
		itemListForm.add(createItemListScrollPanel(), BorderLayout.CENTER);
		setBackground(editor.getResource().bgcolor);
		add(itemListForm);
		initialize();
		initSize();
	}

	public int getPreferredHeight() {
		return 40;
	}

	public boolean validateValues() {
		HashSet<String> set = new HashSet<String>();
		for (int i = 0; i < this.itemList.length; i++) {
			Item item = this.itemList[i];
			String value = item.getValueText();
			if (set.contains(value)) {
				item.setValueText("?" + value);
				return false;
			}
			set.add(value);
		}
		return true;
	}

	/**
	 * @param model
	 * @return
	 */
	private JScrollPane createItemListScrollPanel() {
		final JScrollPane itemListFormScrollPane = new JScrollPane(itemListFormInnerPane);
		itemListFormScrollPane.setWheelScrollingEnabled(true);
		itemListFormScrollPane.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent ev) {
				if (itemListFormScrollPane != null && model != null) {
					int increment = 10;
					itemListFormScrollPane.getVerticalScrollBar().setUnitIncrement(increment);
				}
			}
		});
		return itemListFormScrollPane;
	}

	public synchronized void initialize() {
		this.itemListFormInnerPane.removeAll();
		this.itemList = new Item[this.model.size()];
		for (int i = 0; i < this.model.size(); i++) {
			Item item = createItem(i);
			this.itemList[i] = item;
			this.itemListFormInnerPane.add(item);
		}
	}

	public Item createItem(int i) {
		return new Item(this, i);
	}

	public synchronized Item get(int index) {
		return this.itemList[index];
	}

	public boolean updateNodeValue() {
		boolean isUpdated = false;
		if (!validateValues()) {
			this.invalidValuesAlartThread = new Thread() {
				public void run() {
					synchronized (self) {
						JOptionPane.showMessageDialog(self, Messages.ERROR_FORM_ITEM_VALUE_OVERLOAD, "alert",
								JOptionPane.ERROR_MESSAGE);
						invalidValuesAlartThread = null;
					}
				}
			};
			if (invalidValuesAlartThread != null) {
				invalidValuesAlartThread.start();
			}
			return true;
		}
		for (int i = 0; i < this.itemList.length; i++) {
			if (this.itemList[i].updateNodeValue()) {
				isUpdated = true;
			} else {
			}
		}
		if (isUpdated) {
			getEditor().updateNodeValue(true);
		}
		return isUpdated;
	}

	public class Item extends JPanel {
		public static final long serialVersionUID = 0;
		PopupMenu popupMenu;
		int index;
		EditMenuTextField valueTextField;
		EditMenuTextField labelTextField;
		UpdateListener updateListener;
		JComponent itemFormWest;

		Item(AbstractNodeEditorPanel itemListForm, int index) {
			this.index = index;
			this.updateListener = new UpdateListener(self);
			this.popupMenu = new PopupMenu(index);
			setLayout(new BorderLayout());
			setBackground(getEditor().getResource().bgcolor);

			this.itemFormWest = createFormItemWest();
			add(this.itemFormWest, BorderLayout.WEST);

			this.labelTextField = createFormItemCenter();
			add(this.labelTextField, BorderLayout.CENTER);

			/*
			 * labelTextField.setTransferHandler(new TransferHandler("text"));
			 * labelTextField.addMouseListener(new MouseAdapter() { public void
			 * mousePressed(MouseEvent e) { JComponent c =
			 * (JComponent)e.getSource(); TransferHandler th =
			 * c.getTransferHandler(); th.exportAsDrag(c, e,
			 * TransferHandler.COPY); } });
			 */

			setPreferredSize(new Dimension(340, 20));
			setSize(new Dimension(340, 20));
			addMouseListener(popupMenu.getMouseListener());
		}

		public void setSelectedBackGroundColor(boolean isSelected) {
			if (isSelected) {
				setBackground(getEditor().getResource().bgcolor.brighter());
			} else {
				setBackground(getEditor().getResource().bgcolor);
			}
		}

		JTextField getLabelTextField() {
			return this.labelTextField;
		}

		public boolean updateNodeValue() {
			return model.updateNodeValue(this.index, this.labelTextField.getText(), this.valueTextField
					.getText());
		}

		public String getValueText() {
			return this.valueTextField.getText();
		}

		public void setValueText(String text) {
			this.valueTextField.setText(text);
		}

		private JComponent createFormItemWest() {
			this.valueTextField = new EditMenuTextField();
			this.valueTextField.setSize(2, 1);
			this.valueTextField.setText(model.getValue(this.index));
			this.valueTextField.setBackground(getBackground());
			this.valueTextField.setEditable(true);
			this.valueTextField.addFocusListener(this.updateListener);
			this.valueTextField.setPreferredSize(new Dimension(16, 14));
			this.valueTextField.setUndoManager(new UndoManager());
			JComponent itemFormWest = Box.createHorizontalBox();
			itemFormWest.add(new JLabel(itemIcon));
			itemFormWest.add(this.valueTextField);
			itemFormWest.add(new JLabel(":"));
			if (getEditor().getSource().isReadOnly()) {
				this.valueTextField.setEnabled(false);
			}
			return itemFormWest;
		}

		private EditMenuTextField createFormItemCenter() {
			this.labelTextField = new EditMenuTextField();
			this.labelTextField.setSize(10, 1);
			this.labelTextField.setText(model.getLabel(this.index));
			this.labelTextField.addFocusListener(this.updateListener);
			this.labelTextField.setUndoManager(new UndoManager());
			if (getEditor().getSource().isReadOnly()) {
				this.labelTextField.setEnabled(false);
			}
			return this.labelTextField;
		}

		class PopupMenu extends JPopupMenu {
			public static final long serialVersionUID = 0;
			transient private PopupAdapter listener;
			private JMenuItem deleteButton;
			transient private MouseAdapter mouseAdapter = new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					setSelectedBackGroundColor(true);
				}

				public void mouseExited(MouseEvent e) {
					setSelectedBackGroundColor(false);
				}
			};

			PopupMenu(int index) {
				setPopupSize(300, 70);
				addMouseListener(this.mouseAdapter);
				this.listener = new PopupAdapter(this);

				JMenuItem insertBeforeButton = createPopupMenuItem(
						Messages.FORM_QUESTION_PASTE_ITEM_AS_PRECEDING_SIBLING, "b " + index);
				JMenuItem insertAfterButton = createPopupMenuItem(
						Messages.FORM_QUESTION_PASTE_ITEM_AS_FOLLOWING_SIBLING, "a " + index);
				this.deleteButton = createPopupMenuItem(Messages.FORM_QUESTION_DELETE_ITEM, "d " + index);

				add(insertBeforeButton);
				add(insertAfterButton);
				addSeparator();
				add(this.deleteButton);
			}

			private JMenuItem createPopupMenuItem(String label, String command) {
				JMenuItem item = new JMenuItem(label);
				item.setActionCommand(command);
				item.addActionListener(this.listener);
				item.addMouseListener(this.mouseAdapter);
				return item;
			}

			MouseAdapter getMouseListener() {
				return this.listener;
			}

			public JMenuItem getDeleteButton() {
				return this.deleteButton;
			}

			class PopupAdapter extends MouseAdapter implements ActionListener {
				private JPopupMenu popupMenu;

				PopupAdapter(JPopupMenu commandPopup) {
					this.popupMenu = commandPopup;
				}

				public void actionPerformed(ActionEvent ev) {
					String[] commandArgs = ev.getActionCommand().split("\\s");
					String command = commandArgs[0];
					int target = Integer.parseInt(commandArgs[1]);
					if (command.equals("d")) {
						delete(target);
					} else if (command.equals("b")) {
						insertBefore(target);
					} else if (command.equals("a")) {
						insertAfter(target);
					} else {
						return;
					}
					setSelectedBackGroundColor(false);
					initialize();
					if (command.equals("b")) {
						get(target).getLabelTextField().requestFocus();
					} else if (command.equals("a")) {
						get(target + 1).getLabelTextField().requestFocus();
					}
					getEditor().updateNodeValue(true);
					getEditor().updateUI();
					getEditor().getMediator().getCurrentTreePane().updateUI();
				}

				private void insertAfter(int target) {
					if (target == model.size() - 1) {
						model.add("", "", 1);
					} else {
						model.insert(target + 1, "", "", 1);
					}
					getDeleteButton().setEnabled(true);
				}

				private void insertBefore(int target) {
					model.insert(target, "", "", 1);
					getDeleteButton().setEnabled(true);
				}

				private void delete(int target) {
					if (1 < model.size()) {
						model.remove(target);
						if (model.size() == 1) {
							getDeleteButton().setEnabled(false);
						}
					}
				}

				public void mousePressed(MouseEvent e) {
					// if (e.isPopupTrigger()) {
					setSelectedBackGroundColor(true);
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
					// }
				}

				public void mouseReleased(MouseEvent e) {
				}

				public void mouseEntered(MouseEvent e) {
					setSelectedBackGroundColor(true);
				}

				public void mouseExited(MouseEvent e) {
					if (!popupMenu.isVisible()) {
						setSelectedBackGroundColor(false);
					}
				}
			}
		}
	}
}
