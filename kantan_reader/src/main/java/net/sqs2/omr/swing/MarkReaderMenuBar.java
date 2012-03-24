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
package net.sqs2.omr.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.prefs.Preferences;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import net.sqs2.omr.app.App;
import net.sqs2.omr.app.NetworkPeer;
import net.sqs2.omr.session.MarkReaderSession;
import net.sqs2.omr.source.ConfigHandlers;
import net.sqs2.sound.SoundManager;
import net.sqs2.swing.DocumentDialogModel;
import net.sqs2.swing.HelpMenu;
import net.sqs2.swing.MessageDialogModel;

public class MarkReaderMenuBar extends JMenuBar{
	static private final long serialVersionUID = 0L;	

	JFrame frame;
	MarkReaderPanel panel;
	MarkReaderPanelController markReaderPanelController;
	
	JCheckBoxMenuItem soundConfigCheckbox;
	JCheckBoxMenuItem exportTextAreaImageConfigCheckbox;
	JCheckBoxMenuItem exportChartImageConfigCheckbox;
	JCheckBoxMenuItem openResultBrowserConfigCheckbox;
	JCheckBoxMenuItem exportSpreadSheetConfigCheckbox;
	JCheckBoxMenuItem searchPageMasterFromAncestorDirectoryConfigCheckBox;
	//static Resource resource = new Resource("messages");
	JMenuItem openMenuItem;
	JMenuItem openLogMenuItem;
	
	transient Preferences prefs;
	
	public MarkReaderMenuBar(JFrame frame, MarkReaderPanel panel, MarkReaderPanelController markReaderPanelController){
		this.frame = frame;
		this.panel = panel;
		this.markReaderPanelController = markReaderPanelController;
		add(createFileMenu());
		add(createConfigMenu());
		add(createHelpMenu());
	}
	
	public Preferences getPreferences() {
		if(this.prefs == null){
			this.prefs = Preferences.userNodeForPackage(this.getClass());
		}
		return this.prefs;
	}
	
	private JMenuItem createOpenMenuItem(){
		JMenuItem openMenuItem = new JMenuItem(Messages.MENU_FILE_OPEN, KeyEvent.VK_O);
		openMenuItem.setIcon(ImageManager.OPEN_ICON);
		openMenuItem.addActionListener(new OpenMenuActionListener());
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		return openMenuItem;
	}
	
	private JMenuItem createOpenLogMenuItem(){
		JMenuItem openLogMenuItem = new JMenuItem(Messages.MENU_FILE_LOGOPEN, KeyEvent.VK_L);
		openLogMenuItem.setEnabled(false);
		//openMenuItem.setIcon(ImageIcons.OPEN_ICON);
		openLogMenuItem.addActionListener(new OpenLogMenuActionListener());
		openLogMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		return openLogMenuItem;
	}
	
	private JMenu createFileMenu() {
		this.openMenuItem = createOpenMenuItem();
		this.openLogMenuItem = createOpenLogMenuItem();
		
		JMenu fileMenu = new JMenu(Messages.MENU_FILE);
		fileMenu.getAccessibleContext().setAccessibleDescription("File Menu");
		JMenuItem exitMenuItem = new JMenuItem(Messages.MENU_FILE_QUIT, KeyEvent.VK_Q);		
		exitMenuItem.addActionListener(new ExitMenuActionListener());
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(this.openMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(this.openLogMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		return fileMenu;
	}

	private class OpenMenuActionListener implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			panel.getDirectoryChooserPanel().showChooserDialog();
		}
	}

	private class OpenLogMenuActionListener implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			// show LogMenu
		}
	}

	private class ExitMenuActionListener implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			markReaderPanelController.userExitConfirmation();
		}
	}

	private JMenu createConfigMenu(){
		JMenu configMenu = new JMenu(Messages.MENU_CONFIG);
		configMenu.setMnemonic(KeyEvent.VK_C);
		

		JMenuItem authConfigMenuItem = new JMenuItem("HttpdAuth Config ...");
		authConfigMenuItem.addActionListener(new ConfigMenuActionListener());
		authConfigMenuItem.setIcon(ImageManager.AUTH_ICON);
		authConfigMenuItem.setEnabled(false);
		//configMenu.add(authConfigMenuItem);
		
		JMenuItem sourceConfigMenuItem = new JMenuItem(Messages.MENU_CONFIG_SOURCEFOLDER);
		sourceConfigMenuItem.setIcon(ImageManager.PREF_ICON);
		sourceConfigMenuItem.addActionListener(new ConfigMenuActionListener());
		sourceConfigMenuItem.setEnabled(true);

		this.soundConfigCheckbox = new SoundConfigCheckBox(Messages.MENU_CONFIG_SOUND);
		this.searchPageMasterFromAncestorDirectoryConfigCheckBox =
			new SearchPageMasterFromAncestorDirectoryConfigCheckBox(Messages.MENU_CONFIG_SERARCH_ANCESTOR_DIRECTORY);
		this.exportTextAreaImageConfigCheckbox = new ExportTextAreaImageConfigCheckBox(Messages.MENU_CONFIG_EXPORTTEXTAREAIMAGE);
		this.exportChartImageConfigCheckbox = new ExportChartImageConfigCheckBox(Messages.MENU_CONFIG_EXPORTCHARTIMAGE);
		this.exportSpreadSheetConfigCheckbox = new ExportSpreadSheetConfigCheckBox(Messages.MENU_CONFIG_EXPORTSPREADSHEET);
		this.openResultBrowserConfigCheckbox = new OpenResultBrowserConfigCheckBox(Messages.MENU_CONFIG_OPENRESULTBROWSER);
		
		JMenuItem resultFolderNameConfigItem = new JMenuItem("処理結果保存フォルダ名の設定...");
		resultFolderNameConfigItem.setIcon(ImageManager.DIR_ICON);
		resultFolderNameConfigItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				String value = (String)JOptionPane.showInputDialog((Component)panel,
						(Object)"処理結果フォルダ名", (String)"処理結果保存フォルダ名の設定", 
						JOptionPane.QUESTION_MESSAGE, 
						ImageManager.DIR_ICON, App.RESULT_DIRNAMES, App.RESULT_DIRNAMES[0]);
				App.setResultDirectoryName(value);
			}
		});
		
		this.openResultBrowserConfigCheckbox.setEnabled(false);
		
		configMenu.add(this.searchPageMasterFromAncestorDirectoryConfigCheckBox);
		configMenu.add(this.soundConfigCheckbox);
		configMenu.addSeparator();
		configMenu.add(sourceConfigMenuItem);		
		configMenu.add(resultFolderNameConfigItem);
		configMenu.addSeparator();
		configMenu.add(this.exportSpreadSheetConfigCheckbox);
		configMenu.add(this.exportTextAreaImageConfigCheckbox);
		configMenu.add(this.exportChartImageConfigCheckbox);
		configMenu.add(this.openResultBrowserConfigCheckbox);
		return configMenu;
	}

	abstract class AbstractConfigCheckBox extends JCheckBoxMenuItem{
		private static final long serialVersionUID = 0L;
		String id;
		
		AbstractConfigCheckBox(String title, String id) {
			this(title, id, false);
		}
		
		AbstractConfigCheckBox(String title, String id, boolean systemDefaultValue) {
			super(title);
			this.id = id;
			boolean defaultValue = getPreferences().getBoolean(id, systemDefaultValue);
			addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ev){
					toggle();
				}
			});
			setSelected(defaultValue);
			setConfig(defaultValue);
		}
		
		void toggle(){
			boolean isSelected = this.isSelected();
			setConfig(isSelected);
			getPreferences().putBoolean(this.id, isSelected);
		}
		
		abstract void setConfig(boolean isSelected);
	}
	
	class SoundConfigCheckBox extends AbstractConfigCheckBox{
		private static final long serialVersionUID = 0L;
		SoundConfigCheckBox(String title) {
			super(title, "sound", false);
		}
		@Override
		void setConfig(boolean isSelected){
			SoundManager.setEnabled(isSelected);
		}
	}

	class ExportTextAreaImageConfigCheckBox extends AbstractConfigCheckBox{
		private static final long serialVersionUID = 0L;
		ExportTextAreaImageConfigCheckBox(String title) {
			super(title, "textarea", true);
		}
		void setConfig(boolean isSelected){
			MarkReaderSession.setExportTextAreaImageEnabled(isSelected);
		}
	}
	
	class ExportChartImageConfigCheckBox extends AbstractConfigCheckBox{
		private static final long serialVersionUID = 0L;
		ExportChartImageConfigCheckBox(String title) {
			super(title, "chart", true);
		}
		void setConfig(boolean isSelected){
			MarkReaderSession.setExportChartImageEnabled(isSelected);
		}
	}
	
	class ExportSpreadSheetConfigCheckBox extends AbstractConfigCheckBox{
		private static final long serialVersionUID = 0L;
		ExportSpreadSheetConfigCheckBox(String title) {
			super(title, "spreadsheet", true);
		}
		void setConfig(boolean isSelected){
			MarkReaderSession.setExportSpreadSheetEnabled(isSelected);
		}
	}
	
	class OpenResultBrowserConfigCheckBox extends AbstractConfigCheckBox{
		private static final long serialVersionUID = 0L;
		OpenResultBrowserConfigCheckBox(String title) {
			super(title, "resultBrowser", false);
		}
		void setConfig(boolean isSelected){
			MarkReaderSession.setOpenResultBrowserEnabled(isSelected);
		}
	}
	
	class SearchPageMasterFromAncestorDirectoryConfigCheckBox extends AbstractConfigCheckBox{
		private static final long serialVersionUID = 0L;
		SearchPageMasterFromAncestorDirectoryConfigCheckBox(String title) {
			super(title, "searchAncestor", false);
		}
		void setConfig(boolean isSelected){
			MarkReaderSession.setSearchPageMasterFromAncestorDirectory(isSelected);
		}
	}
	

	class ConfigMenuActionListener implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			MarkRecognitionConfigurationDialog dialog = new MarkRecognitionConfigurationDialog(frame, ConfigHandlers.DEFAULT_INSTANCE);
			dialog.pack();
			dialog.setVisible(true);
		}
	}

	private JMenu createHelpMenu(){
		URL helpSiteURL = null;
		try{
			helpSiteURL = new URL("http://sourceforge.jp/projects/sqs-xml/wiki/Help");
		}catch(Exception ignore){}
		return new ExigridHelpMenu(this.frame, NetworkPeer.getBaseURI(), helpSiteURL);
	}
	
	public class ExigridHelpMenu extends HelpMenu{
		private static final long serialVersionUID = 0L;

		ExigridHelpMenu(JFrame frame, String baseURL, URL helpSiteURL){
				super(frame, 
					ImageManager.HELP_ICON,
					helpSiteURL,
					ImageManager.INFO_ICON,
					new MessageDialogModel(new String[]{
							App.BUILD_NAME,
							App.COPYRIGHT_NOTICE,
							"\n",
							"Licensed under the Apache License, Version 2.0 (the \"License\");",
							"you may not use this file except in compliance with the License.",
							"You may obtain a copy of the License at",
							"\n",
							"   http://www.apache.org/licenses/LICENSE-2.0",
							"\n",
							"Unless required by applicable law or agreed to in writing, software",
							"distributed under the License is distributed on an \"AS IS\" BASIS,",
							"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.",
							"See the License for the specific language governing permissions and",
						"limitations under the License."}
					),
					ImageManager.ABOUT_ICON,
					new DocumentDialogModel(createURL("doc/about.html", baseURL), "About", 640, 480));
		}		
	}
	
	static URL createURL(String href, String baseURL){
		try{			
			return new URL(baseURL+href);
		}catch(MalformedURLException ex){
			throw new RuntimeException(ex);
		}
	}

}