package net.sqs2.omr.result.export;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.session.event.PageEvent;

public class ResultDirectoryUtil {

	public static File createTargetFile(SourceDirectory sourceDirectory, FormArea formArea, String dirname, String filenameBase) {
		return new File(createResultSubDirectory(sourceDirectory, dirname, Integer.toString(formArea.getQuestionIndex())).getAbsolutePath() 
		+ File.separatorChar + filenameBase + "." + AppConstants.FORMAREA_IMAGE_FORMAT);
	}

	public static File createResultSubDirectory(SourceDirectory sourceDirectory, String dirname, String subdirname) {
		File targetDirectory = new File(createResultSubDirectory(sourceDirectory, dirname), subdirname);
		if (!targetDirectory.isDirectory()) {
			targetDirectory.mkdirs();
		}
		return targetDirectory;
	}	

	public static File createResultSubDirectory(SourceDirectory sourceDirectory, String dirname) {
		File sourceDirectoryFile = new File(sourceDirectory.getRoot(), sourceDirectory.getRelativePath());
		String resultSubDirectoryPath = sourceDirectoryFile.getAbsolutePath() + File.separatorChar
				+ AppConstants.RESULT_DIRNAME + File.separatorChar + dirname;
		File resultSubDirectory = new File(resultSubDirectoryPath);
		if (!resultSubDirectory.isDirectory()) {
			resultSubDirectory.mkdirs();
		}
		return resultSubDirectory;
	}
	
	public static boolean isValidOldFileIs(PageEvent pageEvent, File textareaImageFile) {
		if (textareaImageFile.exists()) {
			long textareaImageFileLastModifiled = textareaImageFile.lastModified();
			long targetSourceLastModifiled = pageEvent.getPageTask().getPageID().getFileResourceID()
					.getLastModified();
			long configFileLastModifiled = pageEvent.getPageTask().getConfigHandlerFileResourceID()
					.getLastModified();
			long masterFileLastModifiled = pageEvent.getFormMaster().getLastModified();

			if (targetSourceLastModifiled < textareaImageFileLastModifiled
					&& configFileLastModifiled < textareaImageFileLastModifiled
					&& masterFileLastModifiled < textareaImageFileLastModifiled) {
				return true;
			}
		}
		return false;
	}

	public static PrintWriter createPrintWriter(File file) throws IOException {
		file.getParentFile().mkdirs();
		return new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)),
				"UTF-8"));
	}

}
