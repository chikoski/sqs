package net.sqs2.omr.result.export;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.sqs2.omr.app.App;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.contents.MarkAreaAnswerValueUtil;
import net.sqs2.omr.result.event.PageEvent;
import net.sqs2.omr.result.event.QuestionEvent;
import net.sqs2.omr.result.event.RowEvent;
import net.sqs2.omr.result.event.RowGroupEvent;
import net.sqs2.omr.result.event.SourceDirectoryEvent;
import net.sqs2.omr.result.event.SpreadSheetEvent;
import net.sqs2.omr.result.model.Answer;
import net.sqs2.omr.result.model.MarkAreaAnswer;
import net.sqs2.omr.result.model.MarkAreaAnswerItem;
import net.sqs2.omr.result.model.TextAreaAnswer;
import net.sqs2.omr.source.Config;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.SourceDirectoryConfiguration;
import net.sqs2.omr.source.config.SourceConfig;
import net.sqs2.omr.task.TaskError;
import net.sqs2.util.StringUtil;

import org.apache.commons.collections15.multimap.MultiHashMap;

public class CSVExportModule extends SpreadSheetExportModule {

	public static final String DEFAULT_ENCODING = "MS932";
	private static final char TAB = '\t';
	protected static final char ITEM_SEPARATOR = ',';

	private static final boolean VERBOSE_PRINT_PAGE = true;
	private static final boolean VERBOSE_PRINT_QID = true;
	
	public static final String SUFFIX = "tsv.txt";
	float densityThreshold;
	float recognitionMargin;
	PrintWriter writer;

	public CSVExportModule(){
	}
	
	@Override 
	public void startSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent){
		SourceDirectoryConfiguration sourceDirectoryConfiguration = sourceDirectoryEvent.getSourceDirectory().getConfiguration();
		Config config = sourceDirectoryConfiguration.getConfig();
		SourceConfig sourceConfig = config.getSourceConfig();
		this.densityThreshold = sourceConfig.getMarkRecognitionConfig().getDensity();
		this.recognitionMargin = sourceConfig.getMarkRecognitionConfig().getRecognitionMargin();
	}
	
	@Override
	public void startSpreadSheet(SpreadSheetEvent spreadSheetEvent){
		try{
			File resultDirectory= new File(spreadSheetEvent.getSpreadSheet().getSourceDirectory().getDirectory().getAbsoluteFile(),
					App.getResultDirectoryName());
			resultDirectory.mkdirs();
			
			String csvFile = createSpreadSheetFileName(spreadSheetEvent, SUFFIX);			
			this.writer = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(csvFile)), DEFAULT_ENCODING));
			printCSVHeaderRow(this.writer, spreadSheetEvent.getFormMaster());
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public void endSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		this.writer.close();
	}

	
	@Override
	public void startRowGroup(RowGroupEvent sourceDirectoryEvent) {
	}

	@Override
	public void startPage(PageEvent rowEvent) {
	}

	private void writeHeaderItemLabelRow(PrintWriter csvWriter, FormMaster master) {
		csvWriter.print(TAB);
		csvWriter.print(TAB);
		csvWriter.print(TAB);
		String prevQID = null;

		for (FormArea area : master.getFormAreaList()) {
			if (area.isTextArea()) {
				csvWriter.print(TAB);
				continue;
			}
			switch (area.getTypeCode()) {
			case FormArea.SELECT:
				csvWriter.print(TAB);
				csvWriter.print(StringUtil.escapeTSV(area.getItemLabel()));
				break;
			case FormArea.SELECT1:
				if (area.getQID().equals(prevQID)) {
					csvWriter.print(ITEM_SEPARATOR);
				} else {
					csvWriter.print(TAB);
				}
				prevQID = area.getQID();
				csvWriter.print(StringUtil.escapeTSV(area.getItemLabel()));
				break;
			}
		}
		csvWriter.println();
	}

	private void writeHeaderHintsRow(PrintWriter csvWriter, FormMaster master) {
		csvWriter.print(TAB);
		csvWriter.print(TAB);
		csvWriter.print(TAB);
		for (FormArea area : master.getFormAreaList()) {
			if (area.getTypeCode() == FormArea.SELECT1 && 0 < area.getItemIndex()) {
				continue;
			}
			csvWriter.print(TAB);
			if (!VERBOSE_PRINT_PAGE && area.isMarkArea() && 0 < area.getItemIndex()) {
				continue;
			}
			csvWriter.print(StringUtil.escapeTSV(StringUtil.join(area.getHints(), "")));
		}
		csvWriter.println();
	}

	private void writeHeaderTypeRow(PrintWriter csvWriter, FormMaster master) {
		csvWriter.print(TAB);
		csvWriter.print(TAB);
		csvWriter.print(TAB);
		for (FormArea area : master.getFormAreaList()) {
			if (area.getTypeCode() == FormArea.SELECT1 && 0 < area.getItemIndex()) {
				continue;
			}
			csvWriter.print(TAB);
			if (!VERBOSE_PRINT_QID && area.isMarkArea() && 0 < area.getItemIndex()) {
				continue;
			}
			csvWriter.print(area.getType());
		}
		csvWriter.println();
	}

	private void writeHeaderQIDRow(PrintWriter csvWriter, FormMaster master) {
		csvWriter.print(TAB);
		csvWriter.print(TAB);
		csvWriter.print(TAB);
		for (FormArea area : master.getFormAreaList()) {
			if (area.getTypeCode() == FormArea.SELECT1 && 0 < area.getItemIndex()) {
				continue;
			}
			csvWriter.print(TAB);
			if (!VERBOSE_PRINT_QID && area.isMarkArea() && 0 < area.getItemIndex()) {
				continue;
			}
			csvWriter.print(area.getQID());
		}
		csvWriter.println();
	}

	private void writeHeaderPageRow(PrintWriter csvWriter, FormMaster master) {
		csvWriter.print(TAB);
		csvWriter.print(TAB);
		csvWriter.print(TAB);
		for (FormArea area : master.getFormAreaList()) {
			if (area.getTypeCode() == FormArea.SELECT1 && 0 < area.getItemIndex()) {
				continue;
			}

			csvWriter.print(TAB);

			if (!VERBOSE_PRINT_PAGE && area.isMarkArea() && 0 < area.getItemIndex()) {
				continue;
			}
			csvWriter.print(area.getPage());
		}
		csvWriter.println();
	}

	private void writeHeaderItemValueRow(PrintWriter csvWriter, FormMaster master) {
		csvWriter.print(TAB);
		csvWriter.print(TAB);
		csvWriter.print(TAB);
		String prevQID = null;

		for (FormArea area : master.getFormAreaList()) {
			if (area.isTextArea()) {
				csvWriter.print(TAB);
				continue;
			}
			switch (area.getTypeCode()) {
			case FormArea.SELECT:
				csvWriter.print(TAB);
				csvWriter.print(StringUtil.escapeTSV(area.getItemValue()));
				break;
			case FormArea.SELECT1:
				if (area.getQID().equals(prevQID)) {
					csvWriter.print(ITEM_SEPARATOR);
				} else {
					csvWriter.print(TAB);
				}
				prevQID = area.getQID();
				csvWriter.print(StringUtil.escapeTSV(area.getItemValue()));
				break;
			}
		}
		csvWriter.println();
	}

	private void printCSVHeaderRow(PrintWriter csvWriter, FormMaster master){
		writeHeaderPageRow(csvWriter, master);
		writeHeaderQIDRow(csvWriter, master);
		writeHeaderTypeRow(csvWriter, master);
		writeHeaderHintsRow(csvWriter, master);
		writeHeaderItemLabelRow(csvWriter, master);
		writeHeaderItemValueRow(csvWriter, master);
	}

	
	@Override
	public void startRow(RowEvent rowEvent) {
		int rowIndexInThisSpreadSheet = rowEvent.getRowGroupEvent().getNumPrevRowsTotal() + rowEvent.getRowIndex() + 1;
		this.writer.print(rowIndexInThisSpreadSheet);
		this.writer.print(TAB);
		this.writer.print(rowEvent.getRowGroupEvent().getSourceDirectory().getPath());
		this.writer.print(TAB);
		this.writer.print(rowEvent.createRowMemberFilenames(','));
		this.writer.print(TAB);
		if(rowEvent.getTaskErrorMultiHashMap() != null){
			for(PageID pageID: rowEvent.getTaskErrorMultiHashMap().keySet()){
				for(TaskError error: rowEvent.getTaskErrorMultiHashMap().get(pageID)){
					this.writer.print(error.getSource().getFileResourceID().getRelativePath()+"="+error.getLocalizedMessage());
					this.writer.print(" + ");
				}
			}
		}
	}
	
	@Override
	public void endRow(RowEvent rowEvent) {
		this.writer.print("\n");
	}

	
	@Override
	public void startQuestion(QuestionEvent questionEvent) {
		MultiHashMap<PageID,TaskError> taskErrorMap = questionEvent.getRowEvent().getTaskErrorMultiHashMap();
		
		FormArea defaultFormArea = questionEvent.getDefaultFormArea();
		questionEvent.getDefaultFormArea();
		
		Answer answer = questionEvent.getAnswer();
		if (defaultFormArea.isSelect1()) {
			this.writer.print(TAB);
			if(answer == null || (taskErrorMap != null && 0 < taskErrorMap.size())){
				return;
			}
			
			List<String> valueList = new ArrayList<String>(questionEvent.getFormAreaList().size());
			String value = MarkAreaAnswerValueUtil.createSelect1MarkAreaAnswerValueString(this.densityThreshold, this.recognitionMargin, 
					((MarkAreaAnswer)answer), questionEvent.getFormAreaList(), ',');
			this.writer.print(StringUtil.escapeTSV(value));

			/*
			int size = questionEvent.getFormAreaList().size();
			for(int itemIndex = 0; itemIndex < size; itemIndex++){
				FormArea formArea = questionEvent.getFormAreaList().get(itemIndex);
				MarkAreaAnswerItem answerItem = ((MarkAreaAnswer)answer).getMarkAreaAnswerItem(itemIndex);
				
				if(answerItem.isManualMode()){
					if(answerItem.isManualSelected()){
						valueList.add(formArea.getItemValue());
					}
				}else{
					if(answerItem.getDensity() < this.densityThreshold){
						valueList.add(formArea.getItemValue());						
					}
				}
			}
			this.writer.print(StringUtil.escapeTSV(StringUtil.join(valueList, ",")));
			*/
		}else if (defaultFormArea.isSelect()){
			int size = questionEvent.getFormAreaList().size();			
			for(int itemIndex = 0; itemIndex < size; itemIndex++){
				this.writer.print(TAB);
				if(answer == null || (taskErrorMap != null && 0 < taskErrorMap.size())){
					continue;
				}
				MarkAreaAnswerItem answerItem = ((MarkAreaAnswer)answer).getMarkAreaAnswerItem(itemIndex);
				
				if(answerItem.isManualMode()){
					if(answerItem.isManualSelected()){
						this.writer.print("1");
					}else{
						this.writer.print("0");
					}
				}else{
					if(answerItem.getDensity() < this.densityThreshold){
						this.writer.print("1");
					}else{
						this.writer.print("0");
					}
				}
			}
		}else if (defaultFormArea.isTextArea()) {
			this.writer.print(TAB);
			if(answer == null || (taskErrorMap != null && 0 < taskErrorMap.size())){
				 return;
			 }
			String value = ((TextAreaAnswer)answer).getValue();
			if(value != null){
				this.writer.print(StringUtil.escapeTSV(value));
			}
		}
	}
}
