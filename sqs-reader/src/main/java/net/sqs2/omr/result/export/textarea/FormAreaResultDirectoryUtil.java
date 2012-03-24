package net.sqs2.omr.result.export.textarea;

import java.io.File;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.result.export.ResultDirectoryUtil;
import net.sqs2.omr.session.event.QuestionEvent;
import net.sqs2.omr.session.event.RowEvent;

public class FormAreaResultDirectoryUtil {
	public static File createFormAreaRowFile(QuestionEvent questionEvent, FormArea formArea) {
		RowEvent rowEvent = questionEvent.getRowEvent();
		// int rowIndex = rowEvent.getRowIndex();
		int rowIndexInRowGroup = rowEvent.getRowIndex() - rowEvent.getRowGroupEvent().getRowIndexBase();
		SourceDirectory sourceDirectory = rowEvent.getRowGroupEvent().getSourceDirectory();
		if(formArea.isTextArea()){
			File textareaRowFile = createTextAreaRowFile(sourceDirectory, formArea, rowIndexInRowGroup);
			return textareaRowFile;
		}else{
			File markAreaRowFile = createMarkAreaRowFile(sourceDirectory, formArea, rowIndexInRowGroup);
			return markAreaRowFile;
		}
	}
		
	static private File createTextAreaRowFile(SourceDirectory sourceDirectory, FormArea formArea, int rowIndexInRowGroup) {
		File textAreaResultSubDirectory = ResultDirectoryUtil.createResultSubDirectory(sourceDirectory, "TEXTAREA");
		File textAreaColumnDirectory = new File(textAreaResultSubDirectory, Integer.toString(formArea.getQuestionIndex()));
		if (!textAreaColumnDirectory.isDirectory()) {
			textAreaColumnDirectory.mkdirs();
		}
		return new File(textAreaColumnDirectory, Integer.toString(rowIndexInRowGroup) + "." + AppConstants.FORMAREA_IMAGE_FORMAT);
	}

	static private File createMarkAreaRowFile(SourceDirectory sourceDirectory, FormArea formArea, int rowIndexInRowGroup) {
		File markAreaResultSubDirectory = ResultDirectoryUtil.createResultSubDirectory(sourceDirectory, "MARKAREA");
		File markAreaColumnDirectory = new File(markAreaResultSubDirectory, Integer.toString(formArea.getQuestionIndex()));
		if (!markAreaColumnDirectory.isDirectory()) {
			markAreaColumnDirectory.mkdirs();
		}
		return new File(markAreaColumnDirectory, 
				Integer.toString(rowIndexInRowGroup) + '-' +
				Integer.toString(formArea.getQuestionIndex()) + "." + AppConstants.FORMAREA_IMAGE_FORMAT);
	}
}
