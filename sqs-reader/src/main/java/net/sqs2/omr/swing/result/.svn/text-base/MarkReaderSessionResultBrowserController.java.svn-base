package net.sqs2.omr.swing.result;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sqs2.browser.Browser;
import net.sqs2.omr.app.command.OpenResultBrowserCommand;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.util.FileUtil;

import org.mortbay.util.URIUtil;

public class MarkReaderSessionResultBrowserController {

	MarkReaderSession markReaderSession;
	MarkReaderSessionResultBrowserPanel resultPanel;
	File textAreaIndexFile = null;

	public MarkReaderSessionResultBrowserController(MarkReaderSession markReaderSession, MarkReaderSessionResultBrowserPanel resultPanel) {
		this.markReaderSession = markReaderSession;
		this.resultPanel = resultPanel;
	}

	public void init() {
		List<Integer> textAreaColumnIndexList = getTextAreaColumnIndexList();
		int numTextAreaColumn = textAreaColumnIndexList.size();
		if (numTextAreaColumn == 0) {
			this.resultPanel.textareaButton.setEnabled(false);
		} else if (1 == numTextAreaColumn) {
			this.textAreaIndexFile = new File(getTextAreaIndexPath(textAreaColumnIndexList.get(0)));
		} else {
			this.textAreaIndexFile = new File(getTextAreaIndexPath(null));
		}
	}

	private String getTextAreaIndexPath(Integer columnIndex) {
		String resultDir = this.markReaderSession.getSourceDirectoryRootFile().getAbsolutePath()
				+ File.separatorChar + AppConstants.RESULT_DIRNAME;
		String textareatDir = resultDir + File.separatorChar + "TEXTAREA";
		if (columnIndex == null) {
			return textareatDir + File.separatorChar + "index.html";
		} else {
			return textareatDir + File.separatorChar + columnIndex.toString() + File.separatorChar
					+ "index.html";
		}
	}

	private void showBySuffix(String suffix) {
		try {
			List<File> files = FileUtil.find(this.markReaderSession.getSourceDirectoryRootFile()
					.getAbsolutePath()
					+ File.separatorChar + AppConstants.RESULT_DIRNAME, suffix);
			if (0 < files.size()) {
				File file = files.get(0);

				URL url = convertFileToURL(file);
				Browser.showDocument(file, url);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private URL convertFileToURL(File file) throws MalformedURLException {
		URL url = null;
		if (File.separatorChar == '\\') {
			url = new URL("file:///"
					+ URIUtil.encodeString(null, file.getAbsolutePath().replace("\\", "/"), "MS932"));
		} else {
			url = file.toURI().toURL();
		}
		return url;
	}

	private void showIndex(File file) {
		try {
			if (file.exists()) {
				URL url = convertFileToURL(file);
				Browser.showDocument(file, url);
			}
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void openResultBrowser(){
		openResultBrowser(markReaderSession.getSessionID());
	}

	private void openResultBrowser(long sessionID) {
		new OpenResultBrowserCommand(sessionID).call();
	}

	public void showExcel() {
		showBySuffix(".xls");
	}

	public void showCSV() {
		showBySuffix("tsv.txt");
	}

	private List<Integer> getTextAreaColumnIndexList() {
		List<Integer> textAreaColumnIndexList = new ArrayList<Integer>();
		List<FormMaster> pageMasterList = this.markReaderSession.getSessionSource()
				.getContentIndexer().getFormMasterList();
		if (1 == pageMasterList.size()) {
			FormMaster master = (FormMaster) pageMasterList.get(0);
			for (FormArea formArea : master.getFormAreaList()) {
				if (formArea.isTextArea()) {
					textAreaColumnIndexList.add(formArea.getQuestionIndex());
				}
			}
		}
		return textAreaColumnIndexList;
	}

	public void showTextarea() {
		showIndex(this.textAreaIndexFile);
	}

	public void showChart() {
		String resultDir = this.markReaderSession.getSourceDirectoryRootFile().getAbsolutePath()
				+ File.separatorChar + AppConstants.RESULT_DIRNAME;
		String chartDir = resultDir + File.separatorChar + "CHART";
		File file = new File(chartDir + File.separatorChar + "index.html");
		showIndex(file);
	}

}
