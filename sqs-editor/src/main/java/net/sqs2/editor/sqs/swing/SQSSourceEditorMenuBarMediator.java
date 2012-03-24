/*

 SQSEditorFrame.java
 
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
 
 Created on Jun 30, 2004

 */
package net.sqs2.editor.sqs.swing;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.xml.transform.URIResolver;

import net.sqs2.editor.SourceEditorJarURIContext;
import net.sqs2.editor.base.swing.Messages;
import net.sqs2.editor.base.swing.SourceEditorMenuBarMediator;
import net.sqs2.exsed.source.Source;
import net.sqs2.net.ClassURIResolver;
import net.sqs2.source.SQSSource;
import net.sqs2.swing.DocumentDialogModel;
import net.sqs2.swing.HelpMenu;
import net.sqs2.swing.MessageDialogModel;
import net.sqs2.translator.TranslatorException;
import net.sqs2.translator.impl.PageSetting;
import net.sqs2.util.FileUtil;

/**
 * @author hiroya
 * 
 */
public class SQSSourceEditorMenuBarMediator extends SourceEditorMenuBarMediator {
	public static final ImageIcon HELP_ICON = createImageIcon("Help16.gif");
	public static final ImageIcon INFO_ICON = createImageIcon("Information16.gif");
	public static final ImageIcon ABOUT_ICON = createImageIcon("About16.gif");

	static final String[] TEMPLATE_LABELS = { Messages.TEMPLATE_CHECK_LABEL,
			Messages.TEMPLATE_DISCOVER_LABEL, Messages.TEMPLATE_IMPROVE_LABEL, Messages.TEMPLATE_SIMPLE_LABEL };

	static final String[][] TEMPLATE_ITEMS = {
			{ Messages.TEMPLATE_CHECK_ACTUALIZATION, Messages.TEMPLATE_CHECK_AGREEMENT,
					Messages.TEMPLATE_CHECK_IMPORTANCE, Messages.TEMPLATE_CHECK_SATISFACTION },
			{ Messages.TEMPLATE_DISCOVER_IMPORTANCE_SATISFACTION,
					Messages.TEMPLATE_DISCOVER_IMPORTANCE_ACTUALIZATION }, { Messages.TEMPLATE_MEZASOU } };

	static final String[][] TEMPLATE_FILES = {
			{ "check-actualization", "check-agreement", "check-importance", "check-satisfaction" },
			{ "discover-importance-satisfaction", "discover-importance-actualization" },
			{ "simple"/* "improve-outcome.sqs" */}, { "simple" } };

	/*
	 * static final boolean[][] TEMPLATE_READONLY = { { true, true, true, true
	 * }, { true, true }, { true}, { false } };
	 */
	static final boolean[][] TEMPLATE_READONLY = { { false, false, false, false }, { false, false },
			{ false }, { false } };

	static final boolean[][] TEMPLATE_ENABLED = { { true, true, true, true }, { true, true }, { false },
			{ true } };

	ExportFileLogic exportFileLogic;
	JFileChooser exportFileChooser;

	public SQSSourceEditorMenuBarMediator(SQSSourceEditorMediator mediator) {
		super(mediator);
		this.exportFileLogic = new ExportFileLogic(getMediator());
		this.exportFileChooser = SQSFileChooser.createExportFileChooser();
	}

	public ExportFileLogic getExportFileLogic() {
		return this.exportFileLogic;
	}

	public JMenu createNewMenuItem() {
		JMenu newMenu = new JMenu();
		newMenu.setIcon(super.createImageIcon("New16.gif"));
		newMenu.setText(Messages.MENU_FILE_NEW);
		newMenu.add(createNewMenu(0));
		newMenu.add(createNewMenu(1));
		newMenu.add(createNewMenu(2));
		newMenu.add(new JSeparator());
		newMenu.add(createNewMenuItem(3, -1));
		return newMenu;
	}

	public JMenuItem createExportMenuItem() {
		JMenuItem exportMenuItem = new JMenuItem();
		exportMenuItem.setText(Messages.MENU_FILE_EXPORT);
		exportMenuItem.setIcon(createImageIcon("Export16.gif"));
		exportMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					getMediator().getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					SQSSource source = (SQSSource) getMediator().getSourceEditorTabbedPane()
							.getCurrentEditingSource();
					File exportingFile = getExportingFile(source.getFile(), ".pdf");
					exportFileLogic.exportPDF(exportingFile, SQSSourceEditorMediator.uriResolver, pageSetting);
				} catch (TranslatorException ex) {
					showError(ex, Messages.ERROR_FILE_EXPORT_MESSAGE);
				} catch (IOException ex) {
					showError(ex, Messages.ERROR_FILE_EXPORT_MESSAGE);
				} finally {
					getMediator().getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});
		return exportMenuItem;
	}

	public File getExportingFile(File sourceFile, String suffix) {
		File targetFile;
		if (sourceFile != null) {
			if (exportFileChooser.getSelectedFile() == null
					|| !exportFileChooser.getSelectedFile().getName().endsWith(suffix)) {
				targetFile = new File(FileUtil.getBasename(sourceFile) + suffix);
				exportFileChooser.setSelectedFile(targetFile);
			}
		} else {
			exportFileChooser.setSelectedFile(new File(exportFileChooser.getCurrentDirectory(),
					SQSFileChooser.DEFAULT_FILENAME + suffix));
		}

		while (true) {
			int state = exportFileChooser.showSaveDialog(getMediator().getFrame());
			if (state != JFileChooser.APPROVE_OPTION) {
				return null;
			}
			targetFile = exportFileChooser.getSelectedFile();
			if (targetFile != null) {
				String selectedName = targetFile.getName().toLowerCase();
				if (selectedName.endsWith(".pdf") || selectedName.endsWith(".html")) {
					break;
				}
			}
			JOptionPane.showMessageDialog(getMediator().getFrame(), new String[] {
					Messages.ERROR_EXPORT_FILE_SUFFIX,
					Messages.VOCABRUARY_CURRENT_FILENAME + ": " + targetFile }, Messages.ERROR_FILE_EXPORT,
					JOptionPane.ERROR_MESSAGE);
		}
		if (avoidOverwriteFile(targetFile)) {
			return null;
		}
		return targetFile;
	}

	public JMenu createNewMenu(int index) {
		JMenu newMenu = new JMenu();
		newMenu.setText(TEMPLATE_LABELS[index]);
		for (int i = 0; i < TEMPLATE_ITEMS[index].length; i++) {
			newMenu.add(createNewMenuItem(index, i));
		}
		return newMenu;
	}

	public JMenuItem createNewMenuItem(int index, int i) {
		JMenuItem newMenuItem = new JMenuItem();
		String label = null;
		String filename = null;
		boolean readonly = false;
		boolean enabled = true;
		String language = Locale.getDefault().getLanguage();
		if (0 <= i) {
			label = TEMPLATE_ITEMS[index][i];
			filename = TEMPLATE_FILES[index][i] + "_" + language + ".sqs";
			readonly = TEMPLATE_READONLY[index][i];
			enabled = TEMPLATE_ENABLED[index][i];
		} else {
			label = TEMPLATE_LABELS[index];
			filename = TEMPLATE_FILES[index][0] + "_" + language + ".sqs";
			readonly = TEMPLATE_READONLY[index][0];
			enabled = TEMPLATE_ENABLED[index][0];
		}
		newMenuItem.setText(label);
		newMenuItem.setEnabled(enabled);
		addActionListener(newMenuItem, filename, readonly, label);
		return newMenuItem;
	}

	private void addActionListener(JMenuItem newMenuItem, final String filename, final boolean readonly, final String title) {
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					getMediator().getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					URL url = new URL(SourceEditorJarURIContext.getTemplateBaseURI() + filename);
					getMediator().getMenuBarMediator().open(url, readonly, title);
					updateMenu();
				} catch (Exception ignore) {
					ignore.printStackTrace();
				} finally {
					getMediator().getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});
	}

	public JFileChooser createSaveAsFileChoser(Source currentSource) {
		JFileChooser saveAsFileChooser = SQSFileChooser.createSaveSQSFileChooser(currentSource.getFile());
		return saveAsFileChooser;
	}

	public JFileChooser createOpenFileChooser(File defaultFolder) {
		JFileChooser openFileChooser = SQSFileChooser.createOpenSQSSourceChooser(defaultFolder);
		return openFileChooser;
	}

	@Override
	public JMenu createHelpMenu() {
		try {
			//URL helpSiteURL = new URL("http://sourceforge.jp/projects/sqs-xml/wiki/Help");
			URL helpSiteURL = new URL("http://dev.sqs2.net/wiki/sqs/SQS_Manual");
			return new ExsedHelpMenu(getMediator().getFrame(), 
					SourceEditorJarURIContext.geBaseURI(),
					helpSiteURL);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public class ExsedHelpMenu extends HelpMenu {
		private static final long serialVersionUID = 0;
		public static final String BUILD_ID = "5";
		public static final String COPYRIGHT_NOTICE = "Copyright(C) 2003-2010 KUBO Hiroya. All rights reserved.";
		public static final String ABOUT_DOC_HTML_RELPATH = "doc/about.html";
		
		ExsedHelpMenu(JFrame frame, String baseURL, URL helpSiteURL) {
			super(frame, HELP_ICON, helpSiteURL, INFO_ICON, new MessageDialogModel(new String[] {
					"SourceEditor2.1", // Constants.RELEASE_ID
					COPYRIGHT_NOTICE, "\n",
					"Licensed under the Apache License, Version 2.0 (the \"License\");",
					"you may not use this file except in compliance with the License.",
					"You may obtain a copy of the License at", "\n",
					"   http://www.apache.org/licenses/LICENSE-2.0", "\n",
					"Unless required by applicable law or agreed to in writing, software",
					"distributed under the License is distributed on an \"AS IS\" BASIS,",
					"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.",
					"See the License for the specific language governing permissions and",
					"limitations under the License." }), ABOUT_ICON, 
					new DocumentDialogModel(createURL(ABOUT_DOC_HTML_RELPATH, baseURL), 
							"About", 640, 480));
		}
	}

	static URL createURL(String href, String baseURL) {
		try {
			return new URL(baseURL + href);
		} catch (MalformedURLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
}
