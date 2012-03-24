/*

 SourceEditorMenuBarMediator.java
 
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
package net.sqs2.editor.base.swing;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import net.sqs2.editor.SourceEditorJarURIContext;
import net.sqs2.editor.sqs.swing.SQSSourceEditorMediator;
import net.sqs2.exsed.source.DOMTreeSource;
import net.sqs2.exsed.source.Source;
import net.sqs2.exsed.source.SourceException;
import net.sqs2.translator.impl.PageSetting;
import net.sqs2.translator.impl.PageSettingImpl;

import org.w3c.dom.Node;
import org.xml.sax.SAXParseException;

/**
 * @author hiroya
 * 
 */
public abstract class SourceEditorMenuBarMediator {
	FileMenu fileMenu;
	JMenu editMenu;
	
	JMenuBar menuBar;
	JMenu helpMenu;

	SourceEditorMediator mediator;
	private static final File USER_HOME = new File(System.getProperty("user.dir"));
	private List<Node> recentCutNode;
	EditMenuItems editMenuItems;
	
	public SourceEditorMenuBarMediator(SQSSourceEditorMediator mediator) {
		super();
		this.mediator = mediator;
		this.menuBar = new JMenuBar();
		this.fileMenu = new FileMenu();
		this.editMenu = new JMenu(Messages.MENU_EDIT);
		this.helpMenu = createHelpMenu();

		fileMenu.setMnemonic(KeyEvent.VK_F);
		editMenu.setMnemonic(KeyEvent.VK_E);
		editMenuItems = new EditMenuItems(mediator, false);
		EditMenuDecorator.setupAsJMenu(editMenu, editMenuItems, true, false, false);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
	}

	protected static ImageIcon createImageIcon(String name) {
		try {
			URL url = new URL(SourceEditorJarURIContext.getImageBaseURI() + name);
			return new ImageIcon(url);
		} catch (MalformedURLException ignore) {
			throw new RuntimeException("cannot resolve:" + name);
		}
	}

	public List<Node> getRecentCutNode() {
		return recentCutNode;
	}

	public void setRecentCutNode(List<Node> list) {
		this.recentCutNode = list;
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}

	public SourceEditorMediator getMediator() {
		return mediator;
	}
	
	public class FileMenu extends JMenu {
		public static final long serialVersionUID = 0;
		public JMenuItem openMenuItem;
		public JMenuItem saveMenuItem;
		public JMenuItem saveAsMenuItem;
		public JMenuItem closeMenuItem;
		public JMenuItem pageSettingMenuItem;
		public JMenuItem quitMenuItem;
		public JMenuItem exportMenuItem;

		/**
		 * @return Returns the closeMenuItem.
		 */
		public JMenuItem getCloseMenuItem() {
			return closeMenuItem;
		}

		/**
		 * @return Returns the openMenuItem.
		 */
		public JMenuItem getOpenMenuItem() {
			return openMenuItem;
		}

		/**
		 * @return Returns the quitMenuItem.
		 */
		public JMenuItem getQuitMenuItem() {
			return quitMenuItem;
		}

		/**
		 * @return Returns the saveAsMenuItem.
		 */
		public JMenuItem getSaveAsMenuItem() {
			return saveAsMenuItem;
		}

		/**
		 * @return Returns the saveMenuItem.
		 */
		public JMenuItem getSaveMenuItem() {
			return saveMenuItem;
		}

		public JMenuItem getExportMenuItem() {
			return exportMenuItem;
		}

		public FileMenu() {
			setText(Messages.MENU_FILE);
			this.openMenuItem = new OpenMenuItem();
			this.saveMenuItem = new SaveMenuItem();
			this.saveAsMenuItem = new SaveAsMenuItem();
			this.closeMenuItem = new CloseMenuItem();
			this.quitMenuItem = new QuitMenuItem();
			this.pageSettingMenuItem = new PageSettingMenuItem();
			this.exportMenuItem = createExportMenuItem();

			this.saveMenuItem.setEnabled(false);
			this.saveAsMenuItem.setEnabled(false);
			this.closeMenuItem.setEnabled(false);
			this.exportMenuItem.setEnabled(false);

			add(createNewMenuItem());
			add(this.openMenuItem);
			add(new JSeparator());
			add(this.saveMenuItem);
			add(this.saveAsMenuItem);
			add(this.exportMenuItem);
			add(new JSeparator());
			add(this.closeMenuItem);
			add(new JSeparator());
			add(this.pageSettingMenuItem);
			add(new JSeparator());
			add(this.quitMenuItem);
		}


		private void save() {
			Source source = getCurrentSource();
			if (source.isDirty()) {
				if (avoidOverrideUpdatedFile(source)) {
					return;
				}
				try {
					source.save();
				} catch (IOException ex) {
					showError(ex, Messages.ERROR_FILE_SAVE + ":" + source.getFile());
				}
			}
		}

		private void saveAs() {
			Source currentSource = getCurrentSource();
			JFileChooser saveAsFileChooser = createSaveAsFileChoser(currentSource);
			int result = saveAsFileChooser.showSaveDialog(menuBar);
			if (result == JFileChooser.APPROVE_OPTION) {
				File saveAsFile = saveAsFileChooser.getSelectedFile();
				if (saveAsFile.equals(currentSource.getFile())) {
					save();
					return;
				}

				if (avoidOverrideOpenedBuffer(saveAsFile)) {
					return;
				}
				if (avoidOverwriteFile(saveAsFile)) {
					return;
				}
				currentSource.setFile(saveAsFile);
				currentSource.setReadOnly(false);
				try {
					currentSource.save();
				} catch (IOException ex) {
					showError(ex, Messages.ERROR_FILE_SAVE + ":" + currentSource.getFile());
				}
			}
		}

		public class OpenMenuItem extends JMenuItem {
			public static final long serialVersionUID = 0;

			public OpenMenuItem() {
				setText(Messages.MENU_FILE_OPEN);
				setIcon(createImageIcon("Open16.gif"));
				setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
				addActionListener(createFileOpenActionListener());
			}

			private ActionListener createFileOpenActionListener() {
				return new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						File defaultFolder = null;
						try {
							defaultFolder = getCurrentSource().getFile().getParentFile();
						} catch (NullPointerException ex) {
							defaultFolder = USER_HOME;
						}
						JFileChooser openFileChooser = createOpenFileChooser(defaultFolder);
						int result = openFileChooser.showOpenDialog(menuBar);
						if (result == JFileChooser.APPROVE_OPTION) {
							open(openFileChooser.getSelectedFile());
						}

					}
				};
			}
		}

		public class CloseMenuItem extends JMenuItem {
			public static final long serialVersionUID = 0;

			public CloseMenuItem() {
				setText(Messages.MENU_FILE_CLOSE);
				setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
				addActionListener(createFileCloseActionListener());
			}
		}

		public class SaveMenuItem extends JMenuItem {
			public static final long serialVersionUID = 0;

			public SaveMenuItem() {
				setText(Messages.MENU_FILE_SAVE);
				setIcon(createImageIcon("Save16.gif"));
				setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
				addActionListener(createFileSaveActionListener());
			}

			private ActionListener createFileSaveActionListener() {
				return new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						if (getCurrentSource().getFile() == null) {
							saveAs();
						} else {
							save();
						}
						mediator.getSourceEditorTabbedPane().updateCurrentTitle();
						updateMenu();
					}
				};
			}
		}

		public class SaveAsMenuItem extends JMenuItem {
			public static final long serialVersionUID = 0;

			public SaveAsMenuItem() {
				setText(Messages.MENU_FILE_SAVE_AS);
				setIcon(createImageIcon("SaveAs16.gif"));
				addActionListener(createSaveAsActionListener());
			}

			private ActionListener createSaveAsActionListener() {
				return new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						saveAs();
						mediator.getSourceEditorTabbedPane().updateCurrentTitle();
						updateMenu();
					}
				};
			}
		}
		
		public class QuitMenuItem extends JMenuItem {
			public static final long serialVersionUID = 0;

			public QuitMenuItem() {
				super(Messages.MENU_FILE_QUIT, KeyEvent.VK_Q);
				// setText();
				addActionListener(createQuitActionListener());
				setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
			}

			private ActionListener createQuitActionListener() {
				return new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						if (avoidUnsavedExit(mediator.getSourceManager().countDirtySources())) {
							return;
						}
						System.exit(0);
					}
				};
			}
		}
	}

	public abstract File getExportingFile(File sourceFile, String suffix);

	public abstract JMenu createNewMenuItem();

	public abstract JMenuItem createExportMenuItem();

	public abstract JMenu createHelpMenu();

	public Source getCurrentSource() {
		int index = mediator.getSourceEditorTabbedPane().getSelectedIndex();
		return mediator.getSourceManager().get(index);
	}

	private ActionListener createFileCloseActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				close();
			}
		};
	}

	protected boolean avoidNodeDelete(int n) {
		return JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(mediator.getFrame(), n
				+ Messages.SUFFIX_DELETE_NODES_CONFIRMATION_MESSAGE, "Delete Confirmation",
				JOptionPane.OK_OPTION);
	}

	private boolean avoidDisposeBuffer(Source source) {
		return source.isDirty()
				&& JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(mediator.getFrame(),
						Messages.UNSAVED_BUFFER_CONFIRMATION_MESSAGE, "Dispose Confirmation",
						JOptionPane.OK_OPTION);
	}

	/*
	 * private boolean avoidOverwriteBuffer(Source source) { return
	 * avoidOverrideOpenedBuffer(source.getFile()); }
	 */

	protected boolean avoidOverwriteFile(File file) {
		return file.exists()
				&& JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(mediator.getFrame(), file
						+ Messages.SUFFIX_OVERWRITE_CONFIRMATION_MESSAGE, "Overwrite Confirmation",
						JOptionPane.OK_OPTION);
	}

	private boolean avoidOverrideUpdatedFile(Source source) {
		return source.isModified()
				&& JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(mediator.getFrame(),
						Messages.OVERRIDE_FILE_CONFIRMATION_MESSAGE, "Override Confirmation",
						JOptionPane.OK_OPTION);
	}

	private boolean avoidOverrideOpenedBuffer(File file) {
		int index = mediator.getSourceManager().getIndexOfFile(file);
		if (0 <= index) {
			if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(mediator.getFrame(),
					Messages.OVERRIDE_BUFFER_MESSAGE, "Override Confirmation", JOptionPane.OK_OPTION)) {
				mediator.getSourceManager().close(index);
				mediator.tabbedPane.removeTabAt(index);
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private boolean avoidUpdatedFileOpen(Source source) {
		return source.isModified()
				&& JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(mediator.getFrame(),
						Messages.REOPEN_FILE_MESSAGE, "Updated File Open Confirmation",
						JOptionPane.YES_OPTION);
	}

	private boolean avoidUnsavedExit(int count) {
		return 0 < count
				&& JOptionPane.showConfirmDialog(getMediator().getFrame(),
						Messages.PREFIX_UNSAVED_EXIT_CONFIRMATION_MESSAGE + count
								+ Messages.SUFFIX_UNSAVED_EXIT_CONFIRMATION_MESSAGE, "Exit Confirmation",
						JOptionPane.YES_OPTION) != JOptionPane.OK_OPTION;
	}

	public void showError(Exception ex, String message) {
		JOptionPane.showMessageDialog(mediator.getFrame(), new Object[] { message, ex }, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	private void showFileOpenErrorMessage(SourceException ex) {
		if (ex.getCause() instanceof SAXParseException) {
			SAXParseException saxParseException = ((SAXParseException) ex.getCause());
			JOptionPane.showMessageDialog(getMediator().getFrame(), new Object[] {
					Messages.FILE_PARSE_ERROR_MESSAGE + ":",
					"SystemID: " + saxParseException.getSystemId(),
					"PublicID: " + saxParseException.getPublicId(),
					Messages.ROWS_LABEL + ": " + saxParseException.getLineNumber() + "  "
							+ Messages.COLS_LABEL + ": " + saxParseException.getColumnNumber(),
					saxParseException.getLocalizedMessage() }, "File Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		} else {
			JOptionPane.showMessageDialog(getMediator().getFrame(), new Object[] {
					Messages.FILE_PARSE_ERROR_MESSAGE + ":", ex.getLocalizedMessage() }, "File Error",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	public DOMTreeSource createSource(File file) throws SourceException {
		if (file != null) {
			return (DOMTreeSource) mediator.getSourceManager().createSource(file);
		} else {
			return (DOMTreeSource) mediator.getSourceManager().createSource();
		}
	}

	public DOMTreeSource createSource(URL url, boolean readonly, String title) throws SourceException {
		return (DOMTreeSource) mediator.getSourceManager().createSource(url, readonly, title);
	}

	public void open(URL url, boolean readonly, String title) {
		try {
			mediator.getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			DOMTreeSource source = createSource(url, readonly, title);
			mediator.tabbedPane.addComponent(source);
			updateMenu();
		} catch (SourceException ex) {
			showFileOpenErrorMessage(ex);
		} finally {
			mediator.getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void open(File file) {
		try {
			mediator.getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			DOMTreeSource source = null;
			if (file != null) {
				int index = -1;
				index = mediator.getSourceManager().getIndexOfFile(file);
				if (index == -1) {
					source = createSource(file);
					mediator.tabbedPane.addComponent(source);
				} else {
					source = (DOMTreeSource) mediator.getSourceManager().get(index);
					if (avoidUpdatedFileOpen(source)) {
						return;
					} else {
						source.setFile(file);
						source.initialize(file);
						mediator.tabbedPane.setComponent(index, source);
						if(source.isReadOnly()){
							mediator.getMenuBarMediator().editMenu.setEnabled(false);
						}else{
							mediator.getMenuBarMediator().editMenu.setEnabled(true);
						}
					}
				}
			}
		} catch (SourceException ex) {
			showFileOpenErrorMessage(ex);
		} finally {
			updateMenu();
			mediator.getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void close() {
		Source source = getCurrentSource();
		int index = mediator.getSourceEditorTabbedPane().getSelectedIndex();
		if (avoidDisposeBuffer(source)) {
			return;
		}
		mediator.getSourceEditorTabbedPane().removeTabAt(index);
		mediator.getSourceManager().close(index);
		updateMenu();
		if (mediator.getSourceManager().size() == 0) {
			fileMenu.closeMenuItem.setEnabled(false);
		}
	}

	public void updateMenu() {
		int selectedIndex = getMediator().getSourceEditorTabbedPane().getSelectedIndex();
		if (mediator.getSourceEditorTabbedPane().getComponentCount() == 0) {
			fileMenu.getSaveMenuItem().setEnabled(false);
			fileMenu.getSaveAsMenuItem().setEnabled(false);
			fileMenu.getExportMenuItem().setEnabled(false);
			fileMenu.getCloseMenuItem().setEnabled(false);
			mediator.toolBar.setEnabled(false);
			for(int i = editMenu.getMenuComponentCount() - 1; i <= 0; i--){
				editMenu.getMenuComponent(i).setEnabled(false);
			}
			editMenu.setEnabled(false);
		} else {
			Source source = getMediator().getSourceManager().get(selectedIndex);
			if (source.isDirty()) {
				fileMenu.getSaveMenuItem().setEnabled(true);
				fileMenu.getSaveAsMenuItem().setEnabled(true);
				fileMenu.getExportMenuItem().setEnabled(true);
			} else {
				fileMenu.getSaveMenuItem().setEnabled(false);
				fileMenu.getSaveAsMenuItem().setEnabled(true);
				fileMenu.getExportMenuItem().setEnabled(true);
			}
			fileMenu.getCloseMenuItem().setEnabled(true);
			mediator.toolBar.setEnabled(true);
			updateEditMenu();
		}
	}

	public void updateEditMenu() {
		boolean isReadOnly = mediator.getCurrentTreePane().isReadOnly();
		boolean isPasteMenuItemEnabled = mediator.getMenuBarMediator().getRecentCutNode() != null;
		boolean hasSomeNodeFocused = mediator.getCurrentTreePane().getSelectionPath() != null;
		this.editMenuItems.updateMenuItem(isReadOnly, isPasteMenuItemEnabled, hasSomeNodeFocused);
	}
	
	public abstract JFileChooser createSaveAsFileChoser(Source currentSource);

	public abstract JFileChooser createOpenFileChooser(File defaultFolder);

	
	protected PageSetting pageSetting = new PageSettingImpl(595, 842);
	
	public void setCurrentPageSetting(PageSetting pageSetting){
		this.pageSetting = pageSetting;
	}
	
	public PageSetting getCurrentPageSetting(){
		return pageSetting;
	}

	public class PageSettingMenuItem extends JMenuItem{
		public static final long serialVersionUID = 0;

		public PageSettingMenuItem() {
			setText(Messages.MENU_FILE_PAGE_SETTING);
			setIcon(createImageIcon("Open16.gif"));//FIXME
			//setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
			addActionListener(createPageSettingActionListener());
		}

		private ActionListener createPageSettingActionListener() {
			return new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					
					// Open JDialog,
					
					// add center size selector and portlait/landscape,
					// font size
					// mark size
					// columns
				}
			};
		}
	}

}
