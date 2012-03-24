/*

 SQSFileChooser.java
 
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
 
 Created on 2004/08/21

 */
package net.sqs2.editor.sqs.swing;

import java.io.File;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.metal.MetalIconFactory;

import net.sqs2.editor.base.swing.Messages;
import net.sqs2.editor.base.swing.SuffixBasedFileChooser;
import net.sqs2.swing.IconFactory;
import net.sqs2.swing.SuffixBasedFileFilter;
import net.sqs2.swing.SwingUtil;

/**
 * @author hiroya
 * 
 */
public class SQSFileChooser extends SuffixBasedFileChooser {
	public static final long serialVersionUID = 0;
	public static final String DEFAULT_FILENAME = "newfile";

	static Icon sqsIcon;
	static Icon pdfIcon;
	static Icon textImageIcon;
	static Icon srcImageIcon;
	static Icon treeFolderIcon;
	static Icon defaultFileIcon;
	static Icon csvIcon;

	String defaultSuffix;
	String baseuri;

	public SQSFileChooser() {
		init();
	}

	public SQSFileChooser(SuffixBasedFileFilter filter) {
		super(filter);
		init();
	}

	private void init() {
		if (SQSFileChooser.sqsIcon == null) {
			SQSFileChooser.pdfIcon = IconFactory.create("pdfdoc.gif");
			SQSFileChooser.sqsIcon = IconFactory.create("sqs-tiny.gif");
			SQSFileChooser.textImageIcon = IconFactory.create("image1.gif");
			SQSFileChooser.srcImageIcon = IconFactory.create("image2.gif");
			SQSFileChooser.csvIcon = IconFactory.create("csv.gif");
			SQSFileChooser.treeFolderIcon = new MetalIconFactory.TreeFolderIcon();
			SQSFileChooser.defaultFileIcon = new MetalIconFactory.TreeLeafIcon();
		}
	}

	public String getSuffix() {
		return ((SuffixBasedFileFilter) this.getFileFilter()).getSuffix();
	}

	public static JFileChooser createExportFileChooser() {
		SuffixBasedFileFilter pdfFilter = SwingUtil.createSuffixBasedFileFilter(".pdf",
				Messages.FILECHOOSER_PDFMARKSHEET_DESCRIPTION);
		SuffixBasedFileFilter htmlFilter = SwingUtil.createSuffixBasedFileFilter(".html",
				Messages.FILECHOOSER_HTMLFORM_DESCRIPTION);
		JFileChooser exportFileChooser = new SQSFileChooser(pdfFilter);
		exportFileChooser.setDialogTitle(Messages.FILECHOOSER_SAVE_SQSSOURCE_TITLE);
		exportFileChooser.addChoosableFileFilter(htmlFilter);
		exportFileChooser.addChoosableFileFilter(pdfFilter);
		return exportFileChooser;
	}

	/**
	 * @param title
	 * @param selectedFile
	 * @return
	 */
	private static SuffixBasedFileChooser createFileChooser(String title, String suffix, String description, File selectedFile) {
		SuffixBasedFileFilter filter = SwingUtil.createSuffixBasedFileFilter(suffix, description);
		SuffixBasedFileChooser chooser = new SQSFileChooser(filter);
		chooser.setDialogTitle(title);
		if (selectedFile != null) {
			if (selectedFile.isDirectory()) {
				chooser.setSelectedFile(selectedFile.getParentFile());
			} else {
				chooser.setCurrentDirectory(selectedFile.getParentFile());
				chooser.setSelectedFile(selectedFile);
			}
		} else {
			if (suffix != null) {
				chooser.setSelectedFile(new File(chooser.getCurrentDirectory().getAbsolutePath(),
						DEFAULT_FILENAME + suffix));
			}
		}
		return chooser;
	}

	private static SuffixBasedFileChooser createFileChooser(String title, String[][] suffixDescriptionEntryArray, File selectedFile) {
		SuffixBasedFileChooser chooser = new SQSFileChooser();
		for (String[] entry : suffixDescriptionEntryArray) {
			SuffixBasedFileFilter filter = SwingUtil.createSuffixBasedFileFilter(entry[0], entry[1]);
			chooser.addChoosableFileFilter(filter);
		}

		chooser.setDialogTitle(title);
		if (selectedFile != null) {
			if (selectedFile.isDirectory()) {
				chooser.setSelectedFile(selectedFile.getParentFile());
			} else {
				chooser.setCurrentDirectory(selectedFile.getParentFile());
				chooser.setSelectedFile(selectedFile);
			}
		} else {
			String suffix = suffixDescriptionEntryArray[0][0];
			chooser.setSelectedFile(new File(chooser.getCurrentDirectory().getAbsolutePath(),
					DEFAULT_FILENAME + suffix));
		}
		return chooser;
	}

	public static SuffixBasedFileChooser createOpenSQSSourceChooser(File selectedFile) {
		return createFileChooser(Messages.FILECHOOSER_OPEN_SQSSOURCE_TITLE, new String[][] {
				{ ".pdf", Messages.FILECHOOSER_SQSPDF_DESCRIPTION },
				{ ".sqs", Messages.FILECHOOSER_SQSSOURCE_DESCRIPTION } }, selectedFile);
	}

	public static SuffixBasedFileChooser createSaveSQSFileChooser(File selectedFile) {
		return createFileChooser(Messages.FILECHOOSER_SAVE_SQSSOURCE_TITLE, ".sqs",
				Messages.FILECHOOSER_SQSSOURCE_DESCRIPTION, selectedFile);
	}

	public static SuffixBasedFileChooser createSavePDFFileChooser(File selectedFile) {
		return createFileChooser(Messages.FILECHOOSER_SAVE_PDFMARKSHEET_TITLE, ".pdf",
				Messages.FILECHOOSER_PDFMARKSHEET_DESCRIPTION, selectedFile);
	}

	public static SuffixBasedFileChooser createSaveSQMFileChooser(File selectedFile) {
		return createFileChooser(Messages.FILECHOOSER_SAVE_SQSMASTER_TITLE, ".sqm",
				Messages.FILECHOOSER_SQSMASTER_DESCRIPTION, selectedFile);
	}

	public static SuffixBasedFileChooser createSaveReaderResultFolderChooser(File dir) {
		SuffixBasedFileChooser sqsFolderChooser = new SQSFileChooser(null);
		sqsFolderChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		// sqsFolderChooser.setSelectedFile(dir);
		if (dir != null) {
			sqsFolderChooser.setCurrentDirectory(dir);
		}
		sqsFolderChooser.setDialogTitle(Messages.FILECHOOSER_OPEN_SQSFOLDER_TITLE);
		SuffixBasedFileFilter folderFilter = SwingUtil.createSuffixBasedFileFilter(null,
				Messages.FILECHOOSER_RESULTFOLDER_DESCRIPTION);
		sqsFolderChooser.addChoosableFileFilter(folderFilter);
		return sqsFolderChooser;
	}

	public static SuffixBasedFileChooser createOpenSQSFolderChooser(File dir) {
		SuffixBasedFileFilter sqsFolderFilter = SwingUtil.createSuffixBasedFileFilter(null,
				Messages.FILECHOOSER_SQSFOLDER_DESCRIPTION);
		SuffixBasedFileChooser sqsFolderChooser = new SQSFileChooser(null);
		sqsFolderChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		// sqsFolderChooser.setSelectedFile(dir);
		if (dir != null) {
			sqsFolderChooser.setCurrentDirectory(dir);
		}
		sqsFolderChooser.setDialogTitle(Messages.FILECHOOSER_OPEN_SQSFOLDER_TITLE);
		sqsFolderChooser.addChoosableFileFilter(sqsFolderFilter);
		return sqsFolderChooser;
	}

	static final Object[][] SUFFIX_ICON_MAP = new Object[][] { { ".tiff", srcImageIcon },
			{ ".jpg", srcImageIcon }, { ".jpeg", srcImageIcon }, { ".gif", srcImageIcon },
			{ ".png", textImageIcon }, { ".sqs", sqsIcon }, { ".pdf", pdfIcon }, { "-csv.txt", csvIcon } };

	public FileView createFileView() {
		return new SQSFileView();
	}

	static class SQSFileView extends FileView {
		public Icon getIcon(File f) {
			String name = f.getName();
			if (f.isDirectory()) {
				return treeFolderIcon;
			} else {
				for (int i = 0; i < SUFFIX_ICON_MAP.length; i++) {
					Object[] entry = SUFFIX_ICON_MAP[i];
					String suffix = (String) entry[0];
					if (name.endsWith(suffix)) {
						return (Icon) entry[1];
					}
				}
				return defaultFileIcon;

			}
		}
	}
}
