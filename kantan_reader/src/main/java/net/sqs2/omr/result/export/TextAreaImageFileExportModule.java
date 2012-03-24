/**
 * 
 */
package net.sqs2.omr.result.export;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.sqs2.omr.app.MarkReaderConstants;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.event.PageEvent;
import net.sqs2.omr.result.event.QuestionEvent;
import net.sqs2.omr.result.event.RowEvent;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.task.PageAreaCommand;
import net.sqs2.omr.task.PageTask;
import net.sqs2.omr.task.TaskResult;

class TextAreaImageFileExportModule extends AreaImageFileExportModule{
	
	TextAreaImageFileExportModule(){
	}
	
	void exportTextAreaImageFile(PageEvent pageEvent, QuestionEvent questionEvent, FormArea formArea) {
		byte[] bytes = getImageBytes(pageEvent, questionEvent, formArea);
		if(bytes == null){
			return;
		}
		File textareaImageFile = createAreaImageRowFile(questionEvent, formArea);
		
		if(isValidOldFileIs(pageEvent, textareaImageFile)){
			return;
		}
		
		try{
			OutputStream textAreaImageOutputStream = new BufferedOutputStream(new FileOutputStream(textareaImageFile));
			textAreaImageOutputStream.write(bytes);
			textAreaImageOutputStream.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	private File createAreaImageRowFile(QuestionEvent questionEvent, FormArea formArea) {
		RowEvent rowEvent = questionEvent.getRowEvent();
		int rowIndex = rowEvent.getRowIndex();
		SourceDirectory sourceDirectory = rowEvent.getRowGroupEvent().getSourceDirectory();
		File textareaImageFile = createTextAreaImageRowFile(sourceDirectory, formArea, rowIndex);
		return textareaImageFile;
	}
	
	File createTextAreaDirectoryFile(SourceDirectory sourceDirectory){
		return createResultDirectory(sourceDirectory, "TEXTAREA");
	}

	private File createTextAreaImageRowFile(SourceDirectory sourceDirectory, FormArea formArea, int rowIndex){
		File areaImageDirectoryFile = createTextAreaDirectoryFile(sourceDirectory);
		File areaImageRowDirectory = new File(areaImageDirectoryFile, Integer.toString(formArea.getColumnIndex()));
		if(! areaImageRowDirectory.isDirectory()){
			areaImageRowDirectory.mkdirs();
		}
		return new File(areaImageRowDirectory, Integer.toString(rowIndex)+"."+MarkReaderConstants.FORMAREA_IMAGE_FORMAT);
	}

	private byte[] getImageBytes(PageEvent pageEvent, QuestionEvent questionEvent, FormArea formArea){
		FormMaster master = questionEvent.getFormMaster();		
		int formAreaIndexInPage = master.getAreaIndexInPage(formArea.getQID());
		PageTask pageTask = pageEvent.getPageTask();
		if(pageTask == null){
			return null;
		}
		TaskResult taskResult = pageTask.getTaskResult();
		if(taskResult == null){
			return null;
		}
		PageAreaCommand command = taskResult.getPageAreaCommandList().get(formAreaIndexInPage);
		return command.getImageByteArray();
	}
	
}