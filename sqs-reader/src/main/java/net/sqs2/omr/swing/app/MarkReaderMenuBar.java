/**
 *  MarkReaderMenuBar.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2007/04/29
 Author hiroya
 */
package net.sqs2.omr.swing.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.MarkReaderConfiguration;
import net.sqs2.omr.model.MarkReaderConstants;
import net.sqs2.omr.swing.Images;
import net.sqs2.swing.DocumentDialogModel;
import net.sqs2.swing.HelpMenu;
import net.sqs2.swing.MessageDialogModel;

public class MarkReaderMenuBar extends JMenuBar {
	static private final long serialVersionUID = 0L;

	JFrame frame;
	MarkReaderPanel panel;
	MarkReaderPanelController markReaderPanelController;

	JCheckBoxMenuItem ConfigCheckbox;
	JCheckBoxMenuItem soundConfigCheckbox;
	JCheckBoxMenuItem exportTextAreaImageConfigCheckbox;
	JCheckBoxMenuItem exportChartImageConfigCheckbox;
	JCheckBoxMenuItem openResultBrowserConfigCheckbox;
	JCheckBoxMenuItem exportSpreadSheetConfigCheckbox;
	JCheckBoxMenuItem searchPageMasterFromAncestorDirectoryConfigCheckBox;
	JMenuItem openMenuItem;
	JMenuItem openLogMenuItem;
	JMenuItem deleteResultFoldersMenuItem;

	public MarkReaderMenuBar(JFrame frame, MarkReaderPanel panel,
			MarkReaderPanelController markReaderPanelController) {
		this.frame = frame;
		this.panel = panel;
		this.markReaderPanelController = markReaderPanelController;
		add(createFileMenu());
		add(createConfigMenu());
		add(createHelpMenu());
	}


	private JMenuItem createOpenMenuItem() {
		JMenuItem openMenuItem = new JMenuItem(Messages.MENU_FILE_OPEN, KeyEvent.VK_O);
		openMenuItem.setIcon(Images.OPEN_ICON);
		openMenuItem.addActionListener(new OpenMenuActionListener());
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		return openMenuItem;
	}

	private JMenuItem createOpenLogMenuItem() {
		JMenuItem openLogMenuItem = new JMenuItem(Messages.MENU_FILE_LOGOPEN, KeyEvent.VK_L);
		openLogMenuItem.setEnabled(false);
		// openMenuItem.setIcon(ImageIcons.OPEN_ICON);
		openLogMenuItem.addActionListener(new OpenLogMenuActionListener());
		openLogMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		return openLogMenuItem;
	}

	private JMenuItem createDeleteResultFoldersMenuItem() {
		JMenuItem deleteResultFolderMenuItem = new JMenuItem(Messages.MENU_DELETE_RESULT_FOLDERS,
				KeyEvent.VK_D);
		deleteResultFolderMenuItem.setEnabled(true);
		// openMenuItem.setIcon(ImageIcons.OPEN_ICON);
		deleteResultFolderMenuItem.addActionListener(new DeleteResultFolderActionListener());
		deleteResultFolderMenuItem.setAccelerator(KeyStroke
				.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		return deleteResultFolderMenuItem;
	}

	private JMenu createFileMenu() {
		this.openMenuItem = createOpenMenuItem();
		this.openLogMenuItem = createOpenLogMenuItem();
		this.deleteResultFoldersMenuItem = createDeleteResultFoldersMenuItem();
		JMenu fileMenu = new JMenu(Messages.MENU_FILE);
		fileMenu.getAccessibleContext().setAccessibleDescription("File Menu");
		JMenuItem exitMenuItem = new JMenuItem(Messages.MENU_FILE_QUIT, KeyEvent.VK_Q);
		exitMenuItem.addActionListener(new ExitMenuActionListener());
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(this.openMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(this.openLogMenuItem);
		fileMenu.add(this.deleteResultFoldersMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
		return fileMenu;
	}
	
	private class OpenMenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			String path = MarkReaderConfiguration.getSingleton().get(
					AppConstants.SOURCE_DIRECTORY_ROOT_KEY_IN_PREFERENCES,
					MarkReaderConstants.DEFAULT_SOURCEDIRECTORY_PATH);
			JFileChooser dchooser = MarkReaderMenuBar.this.panel.getDirectoryChooser();
			if (path != null) {
				dchooser.setSelectedFile(new File(path));
			}
			if(JFileChooser.APPROVE_OPTION == dchooser.showOpenDialog(frame)){
				markReaderPanelController.openAndStartSession(dchooser.getSelectedFile());
			}
		}
	}

	private class OpenLogMenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			// show LogMenu
		}
	}

	private class DeleteResultFolderActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			MarkReaderMenuBar.this.markReaderPanelController.removeResultFolders();
		}
	}

	private class ExitMenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			MarkReaderMenuBar.this.markReaderPanelController.promptExitConfirmation();
		}
	}

	private JMenu createConfigMenu() {
		JMenu configMenu = new JMenu(Messages.MENU_CONFIG);
		configMenu.setMnemonic(KeyEvent.VK_C);

		JMenuItem authConfigMenuItem = new JMenuItem("HttpdAuth Config ...");
		authConfigMenuItem.addActionListener(new ConfigMenuActionListener());
		authConfigMenuItem.setIcon(Images.AUTH_ICON);
		authConfigMenuItem.setEnabled(false);
		// configMenu.add(authConfigMenuItem);

		JMenuItem sourceConfigMenuItem = new JMenuItem(Messages.MENU_CONFIG_SOURCEFOLDER);
		sourceConfigMenuItem.setIcon(Images.PREF_ICON);
		sourceConfigMenuItem.addActionListener(new ConfigMenuActionListener());
		sourceConfigMenuItem.setEnabled(true);

		this.ConfigCheckbox = new ParallelConfigCheckBox(Messages.MENU_CONFIG_ENABLE_CLUSTER);

		this.soundConfigCheckbox = new SoundConfigCheckBox(Messages.MENU_CONFIG_SOUND);
		this.searchPageMasterFromAncestorDirectoryConfigCheckBox = new SearchPageMasterFromAncestorDirectoryConfigCheckBox(
				Messages.MENU_CONFIG_SERARCH_ANCESTOR_DIRECTORY);
		this.exportTextAreaImageConfigCheckbox = new ExportTextAreaImageConfigCheckBox(
				Messages.MENU_CONFIG_EXPORTTEXTAREAIMAGE);
		this.exportChartImageConfigCheckbox = new ExportChartImageConfigCheckBox(
				Messages.MENU_CONFIG_EXPORTCHARTIMAGE);
		this.exportSpreadSheetConfigCheckbox = new ExportSpreadSheetConfigCheckBox(
				Messages.MENU_CONFIG_EXPORTSPREADSHEET);
		this.openResultBrowserConfigCheckbox = new OpenResultBrowserConfigCheckBox(
				Messages.MENU_CONFIG_OPENRESULTBROWSER);

		this.openResultBrowserConfigCheckbox.setEnabled(true);

		configMenu.add(sourceConfigMenuItem);
		configMenu.addSeparator();
		configMenu.add(this.searchPageMasterFromAncestorDirectoryConfigCheckBox);
		configMenu.add(this.ConfigCheckbox);
		configMenu.add(this.soundConfigCheckbox);
		configMenu.addSeparator();
		configMenu.add(this.exportSpreadSheetConfigCheckbox);
		configMenu.add(this.exportTextAreaImageConfigCheckbox);
		configMenu.add(this.exportChartImageConfigCheckbox);
		configMenu.add(this.openResultBrowserConfigCheckbox);
		return configMenu;
	}

	abstract class AbstractConfigCheckBox extends JCheckBoxMenuItem {
		private static final long serialVersionUID = 0L;
		String key;

		AbstractConfigCheckBox(String title, String key) {
			super(title);
			this.key = key;
			boolean startValue = MarkReaderConfiguration.isEnabled(key);
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					toggle();
				}
			});
			setSelected(startValue);
		}

		void toggle() {
			boolean isSelected = this.isSelected();
			MarkReaderConfiguration.getSingleton().putBoolean(this.key, isSelected);
		}
	}

	class ParallelConfigCheckBox extends AbstractConfigCheckBox {
		private static final long serialVersionUID = 0L;

		ParallelConfigCheckBox(String title) {
			super(title, MarkReaderConfiguration.KEY_PARALLEL);
		}
	}

	class SoundConfigCheckBox extends AbstractConfigCheckBox {
		private static final long serialVersionUID = 0L;

		SoundConfigCheckBox(String title) {
			super(title, MarkReaderConfiguration.KEY_SOUND);
		}
	}

	class ExportTextAreaImageConfigCheckBox extends AbstractConfigCheckBox {
		private static final long serialVersionUID = 0L;

		ExportTextAreaImageConfigCheckBox(String title) {
			super(title, MarkReaderConfiguration.KEY_TEXTAREA);
		}
	}

	class ExportChartImageConfigCheckBox extends AbstractConfigCheckBox {
		private static final long serialVersionUID = 0L;

		ExportChartImageConfigCheckBox(String title) {
			super(title, MarkReaderConfiguration.KEY_CHARTIMAGE);
		}
	}

	class ExportSpreadSheetConfigCheckBox extends AbstractConfigCheckBox {
		private static final long serialVersionUID = 0L;

		ExportSpreadSheetConfigCheckBox(String title) {
			super(title, MarkReaderConfiguration.KEY_SPREADSHEET);
		}
	}

	class OpenResultBrowserConfigCheckBox extends AbstractConfigCheckBox {
		private static final long serialVersionUID = 0L;

		OpenResultBrowserConfigCheckBox(String title) {
			super(title, MarkReaderConfiguration.KEY_RESULTBROWSER);
		}
	}

	class SearchPageMasterFromAncestorDirectoryConfigCheckBox extends AbstractConfigCheckBox {
		private static final long serialVersionUID = 0L;

		SearchPageMasterFromAncestorDirectoryConfigCheckBox(String title) {
			super(title, MarkReaderConfiguration.KEY_SEARCHANCESTOR);
		}
	}

	class ConfigMenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			MarkRecognitionConfigurationDialog dialog = new MarkRecognitionConfigurationDialog(
					MarkReaderMenuBar.this.frame, null);
			dialog.pack();
			dialog.setVisible(true);
		}
	}

	private JMenu createHelpMenu() {
		URL helpSiteURL = null;
		try {
			helpSiteURL = new URL("http://dev.sqs2.net/wiki/sqs/SQS_Manual");
			//helpSiteURL = new URL("http://www.cmr.sfc.keio.ac.jp/sess2009/help.html");
			//Copyright(C) 2003-2009 KUBO Hiroya and other contributors. All rights reserved.
			//
		} catch (Exception ignore) {
		}
		String baseURI = "class://"+MarkReaderMenuBar.class.getCanonicalName()+"/";
		return new ExigridHelpMenu(this.frame, baseURI, helpSiteURL);
	}

	public class ExigridHelpMenu extends HelpMenu {
		private static final long serialVersionUID = 0L;
		public static final String ABOUT_DOC_HTML_RELPATH = "doc/about.html";
		
		ExigridHelpMenu(JFrame frame, String baseURL, URL helpSiteURL) {
			super(frame, Images.HELP_ICON, helpSiteURL, Images.INFO_ICON, new MessageDialogModel(
					new String[] { AppConstants.BUILD_NAME, AppConstants.COPYRIGHT_NOTICE, "\n",
							"Licensed under the Apache License, Version 2.0 (the \"License\");",
							"you may not use this file except in compliance with the License.",
							"You may obtain a copy of the License at", "\n",
							"   http://www.apache.org/licenses/LICENSE-2.0", "\n",
							"Unless required by applicable law or agreed to in writing, software",
							"distributed under the License is distributed on an \"AS IS\" BASIS,",
							"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.",
							"See the License for the specific language governing permissions and",
							"limitations under the License." }), Images.ABOUT_ICON, new DocumentDialogModel(
					createURL(ABOUT_DOC_HTML_RELPATH, baseURL), "About", 640, 480));
		}
	}

	static URL createURL(String href, String baseURL) {
		try {
			return new URL(baseURL + href);
		} catch (MalformedURLException ex) {
			throw new RuntimeException(ex);
		}
	}

	class AppConfigs{
		
	}
}
