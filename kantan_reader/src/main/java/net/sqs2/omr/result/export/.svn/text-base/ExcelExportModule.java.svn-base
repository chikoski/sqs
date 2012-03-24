package net.sqs2.omr.result.export;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sqs2.hssf.VirtualSheetHSSFAdapter;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.contents.AbstractContentsFactory;
import net.sqs2.omr.result.contents.MarkAreaAnswerValueUtil;
import net.sqs2.omr.result.event.PageEvent;
import net.sqs2.omr.result.event.QuestionEvent;
import net.sqs2.omr.result.event.QuestionItemEvent;
import net.sqs2.omr.result.event.RowEvent;
import net.sqs2.omr.result.event.RowGroupEvent;
import net.sqs2.omr.result.event.SourceDirectoryEvent;
import net.sqs2.omr.result.event.SpreadSheetEvent;
import net.sqs2.omr.result.model.Answer;
import net.sqs2.omr.result.model.MarkAreaAnswer;
import net.sqs2.omr.result.model.MarkAreaAnswerItem;
import net.sqs2.omr.result.model.Row;
import net.sqs2.omr.result.model.TextAreaAnswer;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.source.config.MarkRecognitionConfig;
import net.sqs2.omr.task.TaskError;
import net.sqs2.util.StringUtil;

import org.apache.commons.collections15.multimap.MultiHashMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.util.HSSFColor;

public class ExcelExportModule extends SpreadSheetExportModule {

	private static final short NUM_HEADER_ROWS = 6; 
	private static final short NUM_HEADER_COLUMNS = 4;
	private static final char ITEM_SEPARATOR_CHAR = ',';
	
	private VirtualSheetHSSFAdapter adapter = null;
	
	private short physicalColumnIndexOfBodyPart = 0;
	private int  physicalRowIndex = 0;

	private float densityThreshold;
	private float recognitionMargin;
	
	public static final String SUFFIX = "xls";
	
	public ExcelExportModule(){
	}
	
	private void initVirtualSheets(FormMaster master) {
		
		short sheetIndex = 1;
		short numColumns = 0;

		String sheetName = null;
		for(String qid: master.getQIDSet()){
			if(sheetName == null){
				sheetName = "Sheet"+sheetIndex;
			}
			List<FormArea> formAreaList = master.getFormAreaList(qid);
			if(formAreaList == null || formAreaList.size() == 0){
				continue;
			}
			
			FormArea defaultFormArea = formAreaList.get(0);
			short numQIDColumns = 0;
			
			if(defaultFormArea.isSelect()){
				numQIDColumns = (short)formAreaList.size();
			}else{
				numQIDColumns = 1;
			}
			
			if(NUM_HEADER_COLUMNS + numColumns + numQIDColumns < VirtualSheetHSSFAdapter.NUM_COLUMNS_MAX){
				this.adapter.setNumColumns(sheetName, (short)(numColumns += numQIDColumns));
			}else{
				this.adapter.setNumColumns(sheetName, numQIDColumns);
				sheetName = null;
				sheetIndex++;
				numColumns = numQIDColumns;
			}
		}
	}

	private void setNorthHeaderCellValues(FormMaster master) {
		int virtualColumnIndex = 0;
		for(String qid: master.getQIDSet()){
			List<FormArea> formAreaList = master.getFormAreaList(qid);
			if(formAreaList == null || formAreaList.size() == 0){
				continue;
			}
			FormArea defaultFormArea = formAreaList.get(0);
			if(defaultFormArea.isSelect1() || defaultFormArea.isTextArea()){
				setHeaderCellValuesAsQuestion(virtualColumnIndex, defaultFormArea);
				setHeaderCellValuesAsSelect1(virtualColumnIndex, formAreaList, defaultFormArea);
				virtualColumnIndex ++;
			}else if(defaultFormArea.isSelect()){
				for(FormArea formArea: formAreaList){
					setHeaderCellValuesAsQuestion(virtualColumnIndex, defaultFormArea);
					setHeaderCellValuesAsSelect(virtualColumnIndex, formArea);
					virtualColumnIndex ++;
				}
			}
		}
	}

	private void setHeaderCellValuesAsQuestion(int virtualColumnIndex, FormArea defaultFormArea) {
		HSSFCell pageCell = this.adapter.getNorthHeaderCell(0, virtualColumnIndex);
		HSSFRichTextString pageString = new HSSFRichTextString(Integer.toString(defaultFormArea.getPage())); 
		pageCell.setCellValue(pageString);

		HSSFCell qidCell = this.adapter.getNorthHeaderCell(1, virtualColumnIndex);
		HSSFRichTextString qidString = new HSSFRichTextString(defaultFormArea.getQID()); 
		qidCell.setCellValue(qidString);

		HSSFCell typeCell = this.adapter.getNorthHeaderCell(2, virtualColumnIndex);
		HSSFRichTextString typeString = new HSSFRichTextString(defaultFormArea.getType()); 
		typeCell.setCellValue(typeString);

		HSSFCell hintsCell = this.adapter.getNorthHeaderCell(3, virtualColumnIndex);
		HSSFRichTextString hintsString = new HSSFRichTextString(StringUtil.join(defaultFormArea.getHints()," ")); 
		hintsCell.setCellValue(hintsString);
	}

	private void setHeaderCellValuesAsSelect(int virtualColumnIndex, FormArea formArea) {
		HSSFCell itemLabelCell = this.adapter.getNorthHeaderCell(4, virtualColumnIndex);
		HSSFRichTextString itemLabelString = new HSSFRichTextString(formArea.getItemLabel()); 
		itemLabelCell.setCellValue(itemLabelString);

		HSSFCell itemValueCell = this.adapter.getNorthHeaderCell(5, virtualColumnIndex);
		HSSFRichTextString itemValueString = new HSSFRichTextString(formArea.getItemValue()); 
		itemValueCell.setCellValue(itemValueString);
	}

	private void setHeaderCellValuesAsSelect1(int virtualColumnIndex, List<FormArea> formAreaList, FormArea defaultFormArea) {
		if(defaultFormArea.isSelect1()){
			StringBuilder itemLabelString = null;
			StringBuilder itemValueString = null;
			for(FormArea formArea: formAreaList){
				if(itemLabelString == null){
					itemLabelString = new StringBuilder();
					itemValueString = new StringBuilder();
				}else{
					itemLabelString.append(ITEM_SEPARATOR_CHAR);
					itemValueString.append(ITEM_SEPARATOR_CHAR);
				}
				itemLabelString.append(formArea.getItemLabel());
				itemValueString.append(formArea.getItemValue());
			}
			if(itemLabelString != null){
				HSSFCell itemLabelCell = this.adapter.getNorthHeaderCell(4, virtualColumnIndex);
				itemLabelCell.setCellValue(new HSSFRichTextString(itemLabelString.toString()));
				HSSFCell itemValueCell = this.adapter.getNorthHeaderCell(5, virtualColumnIndex);
				itemValueCell.setCellValue(new HSSFRichTextString(itemValueString.toString()));
			}
		}
	}
	
	private HSSFCellStyle getCellStyle(short fontColor, short backgroundColor) {
		String key = fontColor+","+backgroundColor;
		HSSFCellStyle style = this.adapter.getCellStyleCache().get(key);
		if(style == null){
			style = createCellStyle(fontColor, backgroundColor);
			this.adapter.getCellStyleCache().put(key, style);
		}
		return style;
	}
	
	private HSSFCellStyle createCellStyle(short fontColor, short backgroundColor) {
		HSSFFont font = getFont(fontColor);
		HSSFCellStyle style = this.adapter.createCellStyle();
		style.setFont(font);
		style.setFillForegroundColor(backgroundColor);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return style;
	}
	
	private HSSFFont getFont(short fontColor) {
		HSSFFont font = this.adapter.getFontCache().get(fontColor);
		if(font == null){
			font = this.adapter.createFont();
			font.setColor(fontColor);
			this.adapter.getFontCache().put(fontColor, font);
		}
		return font;
	}
	
	private void setCellValue(HSSFCell cell, String value) {
		try{
			double num = Double.parseDouble(value);
			cell.setCellValue(num);
		}catch(NullPointerException ex){			
			cell.setCellValue(new HSSFRichTextString( value ));
		}catch(NumberFormatException ex){			
			cell.setCellValue(new HSSFRichTextString( value ));	
		}
	}

	private void setCellValueAsSelect1(int rowIndex, 
			List<FormArea> formAreaList, MarkAreaAnswer markAreaAnswer, float densityThreshold, float recognitionMargin) {
	    HSSFCell cell = getBodyCell(rowIndex);
	    HSSFCellStyle style = getCellStyle(HSSFColor.BLACK.index, HSSFColor.WHITE.index);
	    String value = "";

	    value = MarkAreaAnswerValueUtil.createSelect1MarkAreaAnswerValueString(densityThreshold, recognitionMargin,
	    		markAreaAnswer, formAreaList, ITEM_SEPARATOR_CHAR);
    	if("".equals(value)){
    		style = getCellStyle(HSSFColor.BLACK.index, HSSFColor.YELLOW.index);	
    	} else if(0 <= value.indexOf(ITEM_SEPARATOR_CHAR)){
    		style = getCellStyle(HSSFColor.BLACK.index, HSSFColor.LIGHT_ORANGE.index);
        }
    	
	    cell.setCellStyle(style);
	    setCellValue(cell, value);
		this.physicalColumnIndexOfBodyPart ++;
    }

	private void setCellValueAsSelect(int rowIndex, MarkAreaAnswer answer, float densityThreshold) {
	    MarkAreaAnswerItem[] markAreaAnswerItemArray = answer.getMarkAreaAnswerItemArray();
	    for(MarkAreaAnswerItem markAreaAnswerItem : markAreaAnswerItemArray){
	    	HSSFCell cell = getBodyCell(rowIndex);
	    	cell.setCellStyle(getCellStyle(HSSFColor.DARK_BLUE.index, HSSFColor.WHITE.index));
	    	if(markAreaAnswerItem.isSelectMultiSelected(answer, densityThreshold)){
	    		setCellValue(cell, "1");
	    	}else{
	    		setCellValue(cell, "0");
	    	}
	    	this.physicalColumnIndexOfBodyPart ++;
	    }
    }

	private void setCellValueAsTextArea(int rowIndex, TextAreaAnswer answer) {
	    HSSFCell cell = getBodyCell(rowIndex);
	    String value = answer.getValue();
	    cell.setCellStyle(getCellStyle(HSSFColor.BLACK.index, HSSFColor.GREY_25_PERCENT.index));
	    setCellValue(cell, value);
	    this.physicalColumnIndexOfBodyPart ++;
    }

	private HSSFCell getBodyCell(int rowIndex) {
		HSSFCell cell = this.adapter.getBodyCell(this.physicalRowIndex, this.physicalColumnIndexOfBodyPart);
		return cell;
	}
	
	@Override 
	public void startSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent){
		MarkRecognitionConfig recognitionConfig = sourceDirectoryEvent.getSourceDirectory().getConfiguration().getConfig().getSourceConfig().getMarkRecognitionConfig();
		this.densityThreshold = recognitionConfig.getDensity();
		this.recognitionMargin = recognitionConfig.getRecognitionMargin();
	}
	
	@Override
	public void startSpreadSheet(SpreadSheetEvent spreadSheetEvent){
		this.adapter = new VirtualSheetHSSFAdapter(NUM_HEADER_ROWS, NUM_HEADER_COLUMNS);
		initVirtualSheets(spreadSheetEvent.getFormMaster());
		setNorthHeaderCellValues(spreadSheetEvent.getFormMaster());
	}
	
	@Override
	public void endSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		try{
			String xlsFile = createSpreadSheetFileName(spreadSheetEvent, SUFFIX);
			OutputStream xlsOutputStream = new BufferedOutputStream(new FileOutputStream(new File(xlsFile)));
			this.adapter.writeTo(xlsOutputStream);
			xlsOutputStream.close();
		}catch(IOException ex){}
	}

	
	@Override
	public void startRowGroup(RowGroupEvent sourceDirectoryEvent) {
	}

	@Override
	public void startRow(RowEvent rowEvent) {
		FormMaster master = rowEvent.getRowGroupEvent().getFormMaster();
		SourceDirectory sourceDirectory = rowEvent.getRowGroupEvent().getSourceDirectory();
		MultiHashMap<PageID,TaskError> taskErrorMultiHashMap = rowEvent.getTaskErrorMultiHashMap();
		int rowIndex = rowEvent.getRowIndex();
		int numSheets = this.adapter.getNumSheets();
		HSSFCell[][] cellArray = new HSSFCell[NUM_HEADER_COLUMNS][numSheets];
		for(short columnIndex = 0; columnIndex < NUM_HEADER_COLUMNS; columnIndex++){
			cellArray[columnIndex] = this.adapter.getWestHeaderCellArray(this.physicalRowIndex, (short)columnIndex);	
		}
		
		for(int sheetIndex = 0; sheetIndex < numSheets; sheetIndex++){
			int rowIndexInThisSpreadSheet = rowEvent.getRowGroupEvent().getNumPrevRowsTotal() + rowEvent.getRowIndex() + 1;
			cellArray[0][sheetIndex].setCellValue(rowIndexInThisSpreadSheet);
			String path = sourceDirectory.getPath();
			cellArray[1][sheetIndex].setCellValue(new HSSFRichTextString(path));
			String fileNames = AbstractContentsFactory.ContentsFactoryUtil2.createRowMemberFilenames(rowIndex, 
					master.getNumPages(), sourceDirectory.getPageIDList());
			cellArray[2][sheetIndex].setCellValue(new HSSFRichTextString(fileNames));
			if(taskErrorMultiHashMap != null){
				String errorMessage = null;
				for(PageID pageID: taskErrorMultiHashMap.keySet()){
					for(TaskError pageTaskError: taskErrorMultiHashMap.get(pageID)){
						if(errorMessage == null){
							errorMessage = "";
						}else{
							errorMessage += "\n";
						}
						errorMessage += pageTaskError.getSource().getFileResourceID().getRelativePath()+"="+pageTaskError.getLocalizedMessage();
					}
				}
				HSSFCell errorCell = cellArray[3][sheetIndex];
				errorCell.setCellValue(new HSSFRichTextString(errorMessage));
				errorCell.setCellStyle(getCellStyle(HSSFColor.RED.index, HSSFColor.WHITE.index));
			}
		}
		this.physicalColumnIndexOfBodyPart = 0;
	}
	
	@Override
	public void endRow(RowEvent rowEvent){
		this.physicalRowIndex++;
	}

	@Override
	public void startPage(PageEvent rowEvent) {
	}

	@Override
	public void startQuestion(QuestionEvent questionEvent) {
		int rowIndex = questionEvent.getRowEvent().getRowIndex();
		int columnIndex = questionEvent.getColumnIndex();
		List<FormArea> formAreaList = questionEvent.getFormMaster().getFormAreaList(columnIndex);
		FormArea defaultFormArea = formAreaList.get(0);
		Row row = questionEvent.getRowEvent().getRow();
		Answer answer = row.getAnswer(columnIndex);
		if(answer == null){
			return;
		}
		if(defaultFormArea.isSelect1()){
			setCellValueAsSelect1(rowIndex, 
					formAreaList, (MarkAreaAnswer)answer, this.densityThreshold, this.recognitionMargin) ;
		}else if(defaultFormArea.isSelect()){
			setCellValueAsSelect(rowIndex, (MarkAreaAnswer)answer, this.densityThreshold) ;
		}else if(defaultFormArea.isTextArea()){
			setCellValueAsTextArea(rowIndex, (TextAreaAnswer)answer);
		}
	}

	@Override
	public void startQuestionItem(QuestionItemEvent questionEvent) {
	}

}
