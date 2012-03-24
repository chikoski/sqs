/**
 * TextAreaExportModule.java

 Copyright 2009 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Author hiroya
 */

package net.sqs2.omr.result.export.textarea;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.result.export.ResultDirectoryUtil;
import net.sqs2.omr.session.event.MasterEvent;
import net.sqs2.omr.session.event.PageEvent;
import net.sqs2.omr.session.event.QuestionEvent;
import net.sqs2.omr.session.event.QuestionItemEvent;
import net.sqs2.omr.session.event.RowEvent;
import net.sqs2.omr.session.event.RowGroupEvent;
import net.sqs2.omr.session.event.SessionEvent;
import net.sqs2.omr.session.event.SourceDirectoryEvent;
import net.sqs2.omr.session.event.SpreadSheetEvent;
import net.sqs2.omr.session.event.SpreadSheetOutputEventReciever;
import freemarker.template.TemplateException;

public class TextAreaExportModule implements SpreadSheetOutputEventReciever{

	TextAreaHTMLWriter textAreaHTMLWriter = null;
	String skinName;

	int rowIndex;
	int endRowIndex;
	
	boolean newTextAreaRowRangeRequired = false;
	
	TextAreaColumn[] textAreaColumnArray;
	
	public TextAreaExportModule(String skinName) {
		this.skinName = skinName;
	}

	@Override
	public void startSpreadSheet(SpreadSheetEvent spreadSheetEvent){
		String sourceDirectoryPath = spreadSheetEvent.getSpreadSheet().getSourceDirectory().getRelativePath();
		String textAreaDirectoryPath = spreadSheetEvent.getSpreadSheet().getSourceDirectory().getRelativePath()+ 
			File.separator + AppConstants.RESULT_DIRNAME + File.separator + "TEXTAREA";
		FormMaster formMaster = spreadSheetEvent.getFormMaster();
		this.textAreaColumnArray = new TextAreaColumn[formMaster.getNumQuestions()]; 
		int numRows = spreadSheetEvent.getSpreadSheet().getSourceDirectory().getNumPageIDsTotal();
		setUpTextAreaColumns(sourceDirectoryPath, textAreaDirectoryPath, formMaster, numRows);
		this.rowIndex = 0;
		try{
			this.textAreaHTMLWriter = new TextAreaHTMLWriter(spreadSheetEvent.getSpreadSheet().getSourceDirectory(),
					new File(textAreaDirectoryPath), this.skinName);
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	private void setUpTextAreaColumns(String sourceDirectoryPath, String textAreaDirectoryPath,
			FormMaster formMaster, int numRows){
		for (Map.Entry<String, ArrayList<FormArea>> entry : formMaster.getFormAreaListEntrySet()) {
			List<FormArea> formAreaList = entry.getValue();
			FormArea primaryFormArea = formAreaList.get(0);
			if(primaryFormArea.isTextArea()){
				TextAreaColumn textAreaColumn = createTextAreaColumn(
						sourceDirectoryPath, numRows, primaryFormArea);
				this.textAreaColumnArray[primaryFormArea.getQuestionIndex()] = textAreaColumn;
			}
		}
	}

	private TextAreaColumn createTextAreaColumn(String sourceDirectoryPath,
			int numRows, FormArea primaryFormArea) {
		int columnIndex = primaryFormArea.getQuestionIndex();
		//int numRowRangePages = (int) Math.ceil(1.0 * numRows / TextAreaHTMLWriter.NUM_IMAGES_PAR_PAGE);
		
		TextAreaColumnMetadata textAreaColumnMetadata = new TextAreaColumnMetadata(primaryFormArea, 
				sourceDirectoryPath, 
				"TEXTAREA", 
				columnIndex, numRows);
		TextAreaColumn textAreaColumn = new TextAreaColumn(textAreaColumnMetadata);
		return textAreaColumn;
	}
	
	@Override
	public void startRowGroup(RowGroupEvent rowGroupEvent){		
		FormMaster formMaster = rowGroupEvent.getFormMaster();
		int rowIndexBase = rowGroupEvent.getRowIndexBase();
		SourceDirectory rowGroupSourceDirectory = rowGroupEvent.getSourceDirectory();

		for (Map.Entry<String, ArrayList<FormArea>> entry : formMaster.getFormAreaListEntrySet()) {
			List<FormArea> formAreaList = entry.getValue();
			FormArea primaryFormArea = formAreaList.get(0);
			if(primaryFormArea.isTextArea()){
				createAndAddNewTextAreaRowGroup(primaryFormArea.getQuestionIndex(), 
						rowIndexBase,
						rowGroupSourceDirectory);
			}
		}
		this.newTextAreaRowRangeRequired = true;
	}

	private void createAndAddNewTextAreaRowGroup(int columnIndex, int rowIndexBase, SourceDirectory rowGroupSourceDirectory) {		
		TextAreaColumn textAreaColumn = this.textAreaColumnArray[columnIndex];
		textAreaColumn.addTextAreaRowGroup(new TextAreaRowGroup(rowGroupSourceDirectory, rowIndexBase));
	}
	
	@Override
	public void endRowGroup(RowGroupEvent rowGroupEvent) {
	}
	
	@Override
	public void startRow(RowEvent rowEvent){
		RowGroupEvent rowGroupEvent = rowEvent.getRowGroupEvent(); 
		FormMaster formMaster = rowGroupEvent.getFormMaster();
		if(this.newTextAreaRowRangeRequired){					
			this.newTextAreaRowRangeRequired = false;					
			for (Map.Entry<String, ArrayList<FormArea>> entry : formMaster.getFormAreaListEntrySet()) {
				List<FormArea> formAreaList = entry.getValue();
				FormArea primaryFormArea = formAreaList.get(0);
				if(primaryFormArea.isTextArea()){
					SourceDirectory rowGroupSourceDirectory = rowGroupEvent.getSourceDirectory(); 
					int numRowsInThisRowGroup = rowGroupSourceDirectory.getNumPageIDsTotal() / formMaster.getNumPages();
					int numRowsInThisSpreadSheet = rowGroupEvent.getSpreadSheetEvent().getSpreadSheet().getSourceDirectory().getNumPageIDs();
					createAndAddNewTextAreaRowRange(primaryFormArea,						
							numRowsInThisRowGroup,
							numRowsInThisSpreadSheet);
				}
			}
		}else if(this.endRowIndex == rowEvent.getRowIndex()){
			this.newTextAreaRowRangeRequired = true;
		}
	}

	private void createAndAddNewTextAreaRowRange(FormArea primaryFormArea, int numRowsInThisRowGroup, int numRowsInThisSpreadSheet) {
		TextAreaColumn textAreaColumn = this.textAreaColumnArray[primaryFormArea.getQuestionIndex()];
			TextAreaRowGroup textAreaRowGroup = textAreaColumn.getLastTextAreaRowGroup();
		int rowIndexBase = textAreaColumn.getLastTextAreaRowGroup().getRowIndexBaseOfThisRowGroup(); 
		int nextRowRangeIndex = textAreaColumn.getLastTextAreaRowGroup().getTextAreaRowRangeSize();
		int startRowIndex = this.rowIndex - rowIndexBase;
		
		int a1 = rowIndexBase + numRowsInThisRowGroup - 1;
		int a2 = rowIndexBase + TextAreaHTMLWriter.NUM_IMAGES_PAR_PAGE * (nextRowRangeIndex+1) - 1;
		int a = Math.min(a1, a2);
		int b = numRowsInThisSpreadSheet - 1;
		
		this.endRowIndex = (int)Math.min(a, b) - rowIndexBase;
		TextAreaRowRange textAreaRowRange = new TextAreaRowRange(new TextAreaRowRangeMetadata(nextRowRangeIndex, startRowIndex, endRowIndex));
		textAreaRowGroup.addTextAreaRowRange(textAreaRowRange);
	}

	@Override
	public void endRow(RowEvent rowEvent){
		this.rowIndex++;
	}
		
	@Override
	public void startQuestion(QuestionEvent questionEvent) {
		if(! questionEvent.getPrimaryFormArea().isTextArea()){
			return;
		}
		SourceDirectory rowGroupSourceDirectory = questionEvent.getRowEvent().getRowGroupEvent().getSourceDirectory();
		String rowGroupSourceDirectoryPath = rowGroupSourceDirectory.getRelativePath();
		int columnIndex = questionEvent.getPrimaryFormArea().getQuestionIndex();
		
		String key = columnIndex + "-" + rowIndex;
		String value = this.textAreaHTMLWriter.textAreaValueMap.get(key);
		TextAreaImageItem textAreaImageItem = new TextAreaImageItem(rowGroupSourceDirectoryPath,
				this.rowIndex, value);
		TextAreaColumn textAreaColumn = this.textAreaColumnArray[columnIndex];
		TextAreaRowGroup textAreaRowGroup = textAreaColumn.getLastTextAreaRowGroup();
		if(0 < textAreaRowGroup.getTextAreaRowRangeSize()){
			TextAreaRowRange textAreaRowRange = textAreaRowGroup.getLastTextAreaRowRange();
			textAreaRowRange.getTextAreaImageItemList().add(textAreaImageItem);
		}
	}		
	
	public static final boolean DEBUG_EXPORT_MARKAREAS = false;
	
	@Override
	public void startQuestionItem(QuestionItemEvent questionItemEvent){
		RowGroupEvent rowGroupEvent = questionItemEvent.getQuestionEvent().getRowEvent().getRowGroupEvent();
		SourceDirectory rowGroupSourceDirectory = rowGroupEvent.getSourceDirectory();
		SourceDirectory spreadSheetSourceDirectory = rowGroupEvent.getSpreadSheetEvent().getSourceDirectoryEvent().getSourceDirectory();
		FormArea formArea = questionItemEvent.getFormArea();
		if(spreadSheetSourceDirectory.getRelativePath().equals(rowGroupSourceDirectory.getRelativePath())){
			if(questionItemEvent.getItemIndex() == 0 && formArea.isTextArea()){
				FormAreaImageWriter.exportFormAreaImageFile(questionItemEvent);
			}else if(DEBUG_EXPORT_MARKAREAS){
				FormAreaImageWriter.exportFormAreaImageFile(questionItemEvent);
			}
		}
	}

	@Override
	public void endSpreadSheet(SpreadSheetEvent spreadSheetEvent){
		SourceDirectoryEvent sourceDirectoryEvent = spreadSheetEvent.getSourceDirectoryEvent();
		SourceDirectory sourceDirectory = sourceDirectoryEvent.getSourceDirectory();
		File textAreaDirectoryFile = ResultDirectoryUtil.createResultSubDirectory(sourceDirectoryEvent.getSourceDirectory(), "TEXTAREA");
		FormMaster formMaster = (FormMaster)spreadSheetEvent.getFormMaster();
		
		try{			
			if(! textAreaDirectoryFile.exists()){
				// make textAreaDirectory
				// RESULT/TEXTAREA/${colIndex}/
				textAreaDirectoryFile.mkdirs();
			}
			
			// write textAreaDirectoryIndex
			// RESULT/TEXTAREA/index.html
			TextAreaHTMLWriter textAreaHTMLWriter = new TextAreaHTMLWriter(sourceDirectory, textAreaDirectoryFile, skinName);
			textAreaHTMLWriter.writeTextAreaIndexFile(formMaster, sourceDirectory.getRelativePath());
			
			for (Map.Entry<String, ArrayList<FormArea>> entry : sourceDirectoryEvent.getFormMaster()
					.getFormAreaListEntrySet()) {
				List<FormArea> formAreaList = entry.getValue();
				FormArea primaryFormArea = formAreaList.get(0);
				if (primaryFormArea.isTextArea()) {
					int columnIndex = primaryFormArea.getQuestionIndex();
					File textAreaColumnDirectoryFile = new File(textAreaDirectoryFile, Integer.toString(columnIndex)); 			
					if(! textAreaColumnDirectoryFile.exists()){
						// make textAreaColumnDirectory
						// RESULT/TEXTAREA/${colIndex}/
						textAreaColumnDirectoryFile.mkdirs();
					}

					TextAreaColumn textAreaColumn = this.textAreaColumnArray[columnIndex];
					
					// write textAreaColumnDirectoryIndex
					// RESULT/TEXTAREA/${colIndex}/index.html
					// FIXME!: when source and row is different
					
					textAreaHTMLWriter.writeTextAreaColumnIndexFile(formMaster, textAreaColumn, sourceDirectory.getRelativePath());
					
					for(TextAreaRowGroup textAreaRowGroup:textAreaColumn.getTextAreaRowGroupList()){
						for(TextAreaRowRange textAreaRowRange:textAreaRowGroup.getTextAreaRowRangeList()){

							// write textAreaRowRangeFile 					
							// RESULT/TEXTAREA/${colIndex}/${rowRangeIndex}.html
							
							textAreaHTMLWriter.writeTextAreaRowRangeFile(formMaster, 
									textAreaColumn, 
									textAreaRowGroup,
									textAreaRowRange, 
									textAreaRowGroup.getSourceDirectory().getRelativePath());
						}
					}
				}
			}		
			textAreaHTMLWriter.writeTextAreaIndexFile(formMaster, sourceDirectory.getRelativePath());
		}catch(TemplateException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void startPage(PageEvent pageEvent) {
	}

	@Override
	public void endQuestionItem(QuestionItemEvent questionItemEvent) {
	}

	@Override
	public void endMaster(MasterEvent masterEvent) {
	}

	@Override
	public void endSession(SessionEvent sessionEvent) {
	}

	@Override
	public void endSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
	}

	@Override
	public void startMaster(MasterEvent masterEvent) {
	}

	@Override
	public void startSession(SessionEvent sessionEvent) {
	}

	@Override
	public void startSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
	}

	@Override
	public void endPage(PageEvent pageEvent) {
	}

	@Override
	public void endQuestion(QuestionEvent questionEvent) {
	}
}
