/**
 * ExcelExportModule.java

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

package net.sqs2.omr.result.export.spreadsheet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.Answer;
import net.sqs2.omr.model.MarkAreaAnswer;
import net.sqs2.omr.model.MarkAreaAnswerItem;
import net.sqs2.omr.model.MarkRecognitionConfig;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTaskException;
import net.sqs2.omr.model.Row;
import net.sqs2.omr.model.SourceConfig;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.TextAreaAnswer;
import net.sqs2.omr.result.export.MarkAreaAnswerValueUtil;
import net.sqs2.omr.result.export.SpreadSheetExportUtil;
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
import net.sqs2.spreadsheet.VirtualSpreadSheetWorkbook;
import net.sqs2.util.StringUtil;

import org.apache.commons.collections15.multimap.MultiHashMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;

public class ExcelExportModule implements SpreadSheetOutputEventReciever{

	public static final String SUFFIX = "xlsx";
	public static final short numColumnsMax = 256;

	VirtualSpreadSheetWorkbook spreadSheetWorkbook = null;
	SpreadSheetObjectFactory spreadSheetObjectFactory;

	private static final short NUM_HEADER_ROWS = 6;
	private static final short NUM_HEADER_COLUMNS = 4;
	private static final char ITEM_SEPARATOR_CHAR = ',';

	private short physicalColumnIndexOfBodyPart = 0;
	private int physicalRowIndex = 0;

	private float densityThreshold;
	private float doubleMarkSuppressionThreshold;
	private float noMarkSuppressionThreshold;

	private OutputStream xlsOutputStream;
	
	public ExcelExportModule() {
		if(SUFFIX.equals("xls")){
			this.spreadSheetObjectFactory = new HSSFObjectFactory(this);
		}else if(SUFFIX.equals("xlsx")){
			this.spreadSheetObjectFactory = new XSSFObjectFactory(this);
		}else{
			throw new IllegalArgumentException(SUFFIX);
		}
		
	}

	public ExcelExportModule(OutputStream xlsOutputStream) {
		this();
		this.xlsOutputStream = xlsOutputStream;
	}

	private RichTextString createRichTextString(String value){
		return this.spreadSheetObjectFactory.createRichTextString(value);	
	}

	public VirtualSpreadSheetWorkbook getSpreadSheetWorkbook(){
		return this.spreadSheetWorkbook;
	}

	@Override
	public void startSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
		MarkRecognitionConfig recognitionConfig = ((SourceConfig)sourceDirectoryEvent.getSourceDirectory()
				.getConfiguration().getConfig().getPrimarySourceConfig()).getMarkRecognitionConfig();
		this.densityThreshold = recognitionConfig.getMarkRecognitionDensityThreshold();
		this.doubleMarkSuppressionThreshold = recognitionConfig.getDoubleMarkErrorSuppressionThreshold();
		this.noMarkSuppressionThreshold = recognitionConfig.getNoMarkErrorSuppressionThreshold();
	}
	
	@Override
	public void startSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		
		this.spreadSheetWorkbook = new VirtualSpreadSheetWorkbook(
				this.spreadSheetObjectFactory.createWorkbook(),
				NUM_HEADER_ROWS, NUM_HEADER_COLUMNS);
		initVirtualSheets(spreadSheetEvent.getFormMaster());
		setNorthHeaderCellValues(spreadSheetEvent.getFormMaster());
		this.physicalRowIndex = 0;
	}
	
	public String getSuffix(){
		return this.spreadSheetObjectFactory.getSuffix();
	}

	@Override
	public void endSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		try {
			OutputStream xlsOutputStream = null;
			if(this.xlsOutputStream != null){
				xlsOutputStream = this.xlsOutputStream;
			}else{
				File xlsFile = SpreadSheetExportUtil.createSpreadSheetFile(spreadSheetEvent, getSuffix());
				xlsOutputStream = new BufferedOutputStream(new FileOutputStream(xlsFile));
			}
			this.spreadSheetWorkbook.writeTo(xlsOutputStream);
			xlsOutputStream.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void startRowGroup(RowGroupEvent sourceDirectoryEvent) {
	}

	@Override
	public void startRow(RowEvent rowEvent) {
		FormMaster master = rowEvent.getRowGroupEvent().getFormMaster();
		SourceDirectory sourceDirectory = rowEvent.getRowGroupEvent().getSourceDirectory();
		MultiHashMap<PageID, PageTaskException> taskExceptionMultiHashMap = rowEvent.getTaskExceptionMultiHashMap();
		// int rowIndex = rowEvent.getRowIndex();
		int numSheets = this.spreadSheetWorkbook.getNumSheets();
		Cell[][] cellArray = new Cell[NUM_HEADER_COLUMNS][numSheets];
		for (short columnIndex = 0; columnIndex < NUM_HEADER_COLUMNS; columnIndex++) {
			cellArray[columnIndex] = this.spreadSheetWorkbook.getWestHeaderCellArray(this.physicalRowIndex, columnIndex);
		}

		for (int sheetIndex = 0; sheetIndex < numSheets; sheetIndex++) {
			int rowIndexInThisRowGroup = rowEvent.getRowGroupEvent().getRowIndexBase() + rowEvent.getIndex()
					+ 1;
			cellArray[0][sheetIndex].setCellValue(rowIndexInThisRowGroup);
			String path = sourceDirectory.getRelativePath();
			cellArray[1][sheetIndex].setCellValue(createRichTextString(path));
			String fileNames = createRowMemberFilenames(rowEvent
					.getIndex(), master.getNumPages(), sourceDirectory.getPageIDList());
			cellArray[2][sheetIndex].setCellValue(createRichTextString(fileNames));
			if (taskExceptionMultiHashMap != null) {
				setExceptionCell(taskExceptionMultiHashMap, cellArray, sheetIndex);
			}
		}
		this.physicalColumnIndexOfBodyPart = 0;
	}

	private void setExceptionCell(
			MultiHashMap<PageID, PageTaskException> taskExceptionMultiHashMap,
			Cell[][] cellArray, int sheetIndex) {
		String errorMessage = null;
		for (PageID pageID : taskExceptionMultiHashMap.keySet()) {
			for (PageTaskException pageTaskException : taskExceptionMultiHashMap.get(pageID)) {
				if (errorMessage == null) {
					errorMessage = "";
				} else {
					errorMessage += "\n";
				}
				errorMessage += pageTaskException.getExceptionModel().getPageID().getFileResourceID().getRelativePath() + "="
						+ pageTaskException.getLocalizedMessage();
			}
		}
		Cell errorCell = cellArray[3][sheetIndex];
		errorCell.setCellValue(createRichTextString(errorMessage));
		errorCell.setCellStyle(this.spreadSheetObjectFactory.getErrorCellStyle());
	}

	@Override
	public void endRow(RowEvent rowEvent) {
		this.physicalRowIndex++;
	}

	@Override
	public void startPage(PageEvent rowEvent) {
	}

	@Override
	public void startQuestion(QuestionEvent questionEvent) {
		int rowIndex = questionEvent.getRowEvent().getRowIndex();
		int columnIndex = questionEvent.getQuestionIndex();
		List<FormArea> formAreaList = questionEvent.getFormMaster().getFormAreaList(columnIndex);
		FormArea primaryFormArea = formAreaList.get(0);
		Row row = questionEvent.getRowEvent().getRow();
		Answer answer = row.getAnswer(columnIndex);
		if (answer == null) {
			return;
		}
		if (primaryFormArea.isSelectSingle()) {
			setCellValueAsSelect1(rowIndex, formAreaList, (MarkAreaAnswer) answer, this.densityThreshold,
					this.doubleMarkSuppressionThreshold,
					this.noMarkSuppressionThreshold);
		} else if (primaryFormArea.isSelectMultiple()) {
			setCellValueAsSelect(rowIndex, (MarkAreaAnswer) answer, this.densityThreshold);
		} else if (primaryFormArea.isTextArea()) {
			setCellValueAsTextArea(rowIndex, (TextAreaAnswer) answer);
		}
	}

	@Override
	public void startQuestionItem(QuestionItemEvent questionEvent) {
	}

	
	private void initVirtualSheets(FormMaster master) {

		short sheetIndex = 1;
		short numCurrentColumnsTotal = 0;
		short numVirtualColumnsTotal = 0;

		String sheetName = null;
		
		for (String qid : master.getQIDSet()) {
		
			List<FormArea> formAreaList = master.getFormAreaList(qid);
			if (formAreaList == null || formAreaList.size() == 0 || 
					numColumnsMax - NUM_HEADER_COLUMNS < formAreaList.size()) {
				throw new IllegalArgumentException("formAreaList has invalid size");
			}
			while(true){	
				if (sheetName == null) {
					sheetName = "Sheet" + sheetIndex;
				}
				short numQIDColumns = getNumQIDColumns(formAreaList);
				if (NUM_HEADER_COLUMNS + numCurrentColumnsTotal + numQIDColumns < numColumnsMax) {
					numCurrentColumnsTotal += numQIDColumns;
					numVirtualColumnsTotal += numQIDColumns;
					this.spreadSheetWorkbook.setNumColumns(sheetName, numCurrentColumnsTotal);
					break;
				} else {
					sheetName = null;
					sheetIndex++;
					numCurrentColumnsTotal = 0;
					continue;
				}
			}
		}
	}

	private short getNumQIDColumns(List<FormArea> formAreaList) {
		FormArea primaryFormArea = formAreaList.get(0);
		short numQIDColumns = 0;
		if (primaryFormArea.isSelectMultiple()) {
			numQIDColumns = (short) formAreaList.size();
		} else {
			numQIDColumns = 1;
		}
		return numQIDColumns;
	}

	private void setNorthHeaderCellValues(FormMaster master) {
		int virtualColumnIndex = 0;
		for (String qid : master.getQIDSet()) {
			List<FormArea> formAreaList = master.getFormAreaList(qid);
			if (formAreaList == null || formAreaList.size() == 0) {
				continue;
			}
			FormArea primaryFormArea = formAreaList.get(0);
			if (primaryFormArea.isSelectSingle() || primaryFormArea.isTextArea()) {
				setHeaderCellValuesAsQuestion(virtualColumnIndex, primaryFormArea);
				setHeaderCellValuesAsSelect1(virtualColumnIndex, formAreaList, primaryFormArea);
				virtualColumnIndex++;
			} else if (primaryFormArea.isSelectMultiple()) {
				for (FormArea formArea : formAreaList) {
					setHeaderCellValuesAsQuestion(virtualColumnIndex, primaryFormArea);
					setHeaderCellValuesAsSelect(virtualColumnIndex, formArea);
					virtualColumnIndex++;
				}
			}
		}
	}
	
	private void setHeaderCellValuesAsQuestion(int virtualColumnIndex, FormArea primaryFormArea) {
		Cell pageCell = this.spreadSheetWorkbook.getNorthHeaderCell(0, virtualColumnIndex);
		RichTextString pageString = createRichTextString(Integer.toString(primaryFormArea.getPage()));
		pageCell.setCellValue(pageString);

		Cell qidCell = this.spreadSheetWorkbook.getNorthHeaderCell(1, virtualColumnIndex);
		RichTextString qidString = createRichTextString(primaryFormArea.getQID());
		qidCell.setCellValue(qidString);

		Cell typeCell = this.spreadSheetWorkbook.getNorthHeaderCell(2, virtualColumnIndex);
		RichTextString typeString = createRichTextString(primaryFormArea.getType());
		typeCell.setCellValue(typeString);

		Cell hintsCell = this.spreadSheetWorkbook.getNorthHeaderCell(3, virtualColumnIndex);
		RichTextString hintsString = createRichTextString(StringUtil.join(primaryFormArea.getHints(),
				" "));
		hintsCell.setCellValue(hintsString);
	}

	private void setHeaderCellValuesAsSelect(int virtualColumnIndex, FormArea formArea) {
		Cell itemLabelCell = this.spreadSheetWorkbook.getNorthHeaderCell(4, virtualColumnIndex);
		RichTextString itemLabelString = createRichTextString(formArea.getItemLabel());
		itemLabelCell.setCellValue(itemLabelString);

		Cell itemValueCell = this.spreadSheetWorkbook.getNorthHeaderCell(5, virtualColumnIndex);
		RichTextString itemValueString = createRichTextString(formArea.getItemValue());
		itemValueCell.setCellValue(itemValueString);
	}

	private void setHeaderCellValuesAsSelect1(int virtualColumnIndex, List<FormArea> formAreaList, FormArea primaryFormArea) {
		if (primaryFormArea.isSelectSingle()) {
			StringBuilder itemLabelString = null;
			StringBuilder itemValueString = null;
			for (FormArea formArea : formAreaList) {
				if (itemLabelString == null) {
					itemLabelString = new StringBuilder();
					itemValueString = new StringBuilder();
				} else {
					itemLabelString.append(ITEM_SEPARATOR_CHAR);
					itemValueString.append(ITEM_SEPARATOR_CHAR);
				}
				itemLabelString.append(formArea.getItemLabel());
				itemValueString.append(formArea.getItemValue());
			}
			if (itemLabelString != null) {
				Cell itemLabelCell = this.spreadSheetWorkbook.getNorthHeaderCell(4, virtualColumnIndex);
				itemLabelCell.setCellValue(createRichTextString(itemLabelString.toString()));
				Cell itemValueCell = this.spreadSheetWorkbook.getNorthHeaderCell(5, virtualColumnIndex);
				itemValueCell.setCellValue(createRichTextString(itemValueString.toString()));
			}
		}
	}

	private void setCellValue(Cell cell, String value) {
		try {
			double num = Double.parseDouble(value);
			cell.setCellValue(num);
		} catch (NullPointerException ex) {
			cell.setCellValue(createRichTextString(value));
		} catch (NumberFormatException ex) {
			cell.setCellValue(createRichTextString(value));
		}
	}

	private void setCellValueAsSelect1(int rowIndex, List<FormArea> formAreaList, MarkAreaAnswer markAreaAnswer, float densityThreshold,
			float doubleMarkErrorSuppressionThreshold,
			float noMarkErrorSuppressionThreshold) {
		Cell cell = getBodyCell(rowIndex);
		CellStyle style = this.spreadSheetObjectFactory.getSelect1CellStyle();
		String value = "";

		value = MarkAreaAnswerValueUtil.createSelect1MarkAreaAnswerValueString(densityThreshold,
				doubleMarkErrorSuppressionThreshold, 
				noMarkErrorSuppressionThreshold, markAreaAnswer, formAreaList, ITEM_SEPARATOR_CHAR);
		if ("".equals(value)) {
			style = this.spreadSheetObjectFactory.getNoAnswerCellStyle();
		} else if (0 <= value.indexOf(ITEM_SEPARATOR_CHAR)) {
			style = this.spreadSheetObjectFactory.getMultipleAnswersCellStyle();
		}

		cell.setCellStyle(style);
		setCellValue(cell, value);
		this.physicalColumnIndexOfBodyPart++;
	}

	private void setCellValueAsSelect(int rowIndex, MarkAreaAnswer answer, float densityThreshold) {
		MarkAreaAnswerItem[] markAreaAnswerItemArray = answer.getMarkAreaAnswerItemArray();
		for (MarkAreaAnswerItem markAreaAnswerItem : markAreaAnswerItemArray) {
			Cell cell = getBodyCell(rowIndex);
			cell.setCellStyle(this.spreadSheetObjectFactory.getSelectCellStyle());
			if (markAreaAnswerItem.isSelectMultiSelected(answer, densityThreshold)) {
				setCellValue(cell, "1");
			} else {
				setCellValue(cell, "0");
			}
			this.physicalColumnIndexOfBodyPart++;
		}
	}

	private void setCellValueAsTextArea(int rowIndex, TextAreaAnswer answer) {
		Cell cell = getBodyCell(rowIndex);
		String value = answer.getValue();
		cell.setCellStyle(this.spreadSheetObjectFactory.getTextAreaCellStyle());
		setCellValue(cell, value);
		this.physicalColumnIndexOfBodyPart++;
	}

	private Cell getBodyCell(int rowIndex) {
		Cell cell = this.spreadSheetWorkbook.getBodyCell(this.physicalRowIndex, this.physicalColumnIndexOfBodyPart);
		return cell;
	}

	@Override
	public void startSession(SessionEvent sessionEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endSession(SessionEvent sessionEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startMaster(MasterEvent masterEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endMaster(MasterEvent masterEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endRowGroup(RowGroupEvent rowGroupEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endPage(PageEvent pageEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endQuestion(QuestionEvent questionEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endQuestionItem(QuestionItemEvent questionItemEvent) {
		// TODO Auto-generated method stub
		
	}

	protected static final char ITEM_SEPARATOR = ',';
	
	public static String createRowMemberFilenames(int rowIndex, int numPages, List<PageID> pageIDList) {
		boolean separator = false;
		StringBuilder filenames = new StringBuilder();
		for (int pageIndex = 0; pageIndex < numPages; pageIndex++) {
			PageID pageID = pageIDList.get(rowIndex * numPages + pageIndex);
			if (separator) {
				filenames.append(ITEM_SEPARATOR);
			} else {
				separator = true;
			}
			filenames.append(StringUtil.escapeTSV(new File(pageID.getFileResourceID().getRelativePath())
					.getName()));
		}
		return filenames.toString();
	}



}
