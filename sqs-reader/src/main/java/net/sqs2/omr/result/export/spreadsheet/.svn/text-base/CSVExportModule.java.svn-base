/**
 * CSVExportModule.java

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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.Answer;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.Config;
import net.sqs2.omr.model.MarkAreaAnswer;
import net.sqs2.omr.model.MarkAreaAnswerItem;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTaskException;
import net.sqs2.omr.model.SourceConfig;
import net.sqs2.omr.model.SourceDirectoryConfiguration;
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
import net.sqs2.util.StringUtil;

import org.apache.commons.collections15.multimap.MultiHashMap;

public class CSVExportModule implements SpreadSheetOutputEventReciever{

	//public static final String DEFAULT_ENCODING = "MS932";
	public static final String DEFAULT_ENCODING = "x-UTF-16LE-BOM";
	private static final char TAB = '\t';
	protected static final char ITEM_SEPARATOR = ',';

	private static final boolean VERBOSE_PRINT_PAGE = true;
	private static final boolean VERBOSE_PRINT_QID = true;

	//public static final String SUFFIX = "tsv.txt";
	public static final String SUFFIX = "csv";
	
	float densityThreshold;
	float doubleMarkSuppressionThreshold;
	float noMarkSuppressionThreshold;;
	
	PrintWriter exportingCsvWriter;
	PrintWriter csvWriter;

	public CSVExportModule() {
	}

	public CSVExportModule(PrintWriter exportingCsvWriter) {
		this.exportingCsvWriter = exportingCsvWriter;
	}

	@Override
	public void startSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
		SourceDirectoryConfiguration sourceDirectoryConfiguration = sourceDirectoryEvent.getSourceDirectory()
				.getConfiguration();
		Config config = sourceDirectoryConfiguration.getConfig();
		SourceConfig sourceConfig = (SourceConfig)config.getPrimarySourceConfig();
		this.densityThreshold = sourceConfig.getMarkRecognitionConfig().getMarkRecognitionDensityThreshold();
		this.doubleMarkSuppressionThreshold = sourceConfig.getMarkRecognitionConfig().getDoubleMarkErrorSuppressionThreshold();
		this.noMarkSuppressionThreshold = sourceConfig.getMarkRecognitionConfig().getNoMarkErrorSuppressionThreshold();
		//super.startSourceDirectory(sourceDirectoryEvent);
	}

	@Override
	public void startSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		try {
			if(this.exportingCsvWriter != null){
				this.csvWriter = this.exportingCsvWriter;
			}else{
				File resultDirectory = new File(spreadSheetEvent.getSpreadSheet().getSourceDirectory()
						.getDirectory().getAbsoluteFile(), AppConstants.RESULT_DIRNAME);
				resultDirectory.mkdirs();
				File csvFile = SpreadSheetExportUtil.createSpreadSheetFile(spreadSheetEvent, SUFFIX);
				this.csvWriter = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(
						new FileOutputStream(csvFile)), DEFAULT_ENCODING));
			}
			printCSVHeaderRow(this.csvWriter, spreadSheetEvent.getFormMaster());
			this.csvWriter.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		//super.startSpreadSheet(spreadSheetEvent);
	}

	@Override
	public void endSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		this.csvWriter.close();
	}

	@Override
	public void startRowGroup(RowGroupEvent rowGroupEvent) {
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

	private void printCSVHeaderRow(PrintWriter csvWriter, FormMaster master) {
		writeHeaderPageRow(csvWriter, master);
		writeHeaderQIDRow(csvWriter, master);
		writeHeaderTypeRow(csvWriter, master);
		writeHeaderHintsRow(csvWriter, master);
		writeHeaderItemLabelRow(csvWriter, master);
		writeHeaderItemValueRow(csvWriter, master);
	}

	@Override
	public void startRow(RowEvent rowEvent) {
		int rowIndexInThisRowGroup = rowEvent.getRowGroupEvent().getRowIndexBase() + rowEvent.getIndex() + 1;
		this.csvWriter.print(rowIndexInThisRowGroup);
		this.csvWriter.print(TAB);
		this.csvWriter.print(rowEvent.getRowGroupEvent().getSourceDirectory().getRelativePath());
		this.csvWriter.print(TAB);
		this.csvWriter.print(rowEvent.createRowMemberFilenames(','));
		this.csvWriter.print(TAB);
		if (rowEvent.getTaskExceptionMultiHashMap() != null) {
			for (PageID pageID : rowEvent.getTaskExceptionMultiHashMap().keySet()) {
				for (PageTaskException ex : rowEvent.getTaskExceptionMultiHashMap().get(pageID)) {
					this.csvWriter.print(ex.getExceptionModel().getPageID().getFileResourceID().getRelativePath() + "="
							+ ex.getLocalizedMessage());
					this.csvWriter.print(" + ");
				}
			}
		}
	}

	@Override
	public void endRow(RowEvent rowEvent) {
		this.csvWriter.print("\n");
	}

	@Override
	public void startQuestion(QuestionEvent questionEvent) {
		MultiHashMap<PageID, PageTaskException> taskExceptionMap = questionEvent.getRowEvent().getTaskExceptionMultiHashMap();

		FormArea primaryFormArea = questionEvent.getPrimaryFormArea();
		questionEvent.getPrimaryFormArea();

		Answer answer = questionEvent.getAnswer();
		if (primaryFormArea.isSelectSingle()) {
			writeSelectSingleAnswer(questionEvent, taskExceptionMap, answer);
		} else if (primaryFormArea.isSelectMultiple()) {
			writeSelectMultipleAnswer(questionEvent, taskExceptionMap, answer);
		} else if (primaryFormArea.isTextArea()) {
			writeTextAreaAnswer(taskExceptionMap, answer);
		}
	}

	private void writeSelectSingleAnswer(QuestionEvent questionEvent,
			MultiHashMap<PageID, PageTaskException> taskExceptionMap, Answer answer) {
		this.csvWriter.print(TAB);
		if (answer == null || (taskExceptionMap != null && 0 < taskExceptionMap.size())) {
			return;
		}
		String value = MarkAreaAnswerValueUtil.createSelect1MarkAreaAnswerValueString(
				this.densityThreshold, this.doubleMarkSuppressionThreshold, this.noMarkSuppressionThreshold, ((MarkAreaAnswer) answer),
				questionEvent.getFormAreaList(), ',');
		this.csvWriter.print(StringUtil.escapeTSV(value));
	}

	private void writeTextAreaAnswer(
			MultiHashMap<PageID, PageTaskException> taskExceptionMap, Answer answer) {
		this.csvWriter.print(TAB);
		if (answer == null || (taskExceptionMap != null && 0 < taskExceptionMap.size())) {
			return;
		}
		String value = ((TextAreaAnswer) answer).getValue();
		if (value != null) {
			this.csvWriter.print(StringUtil.escapeTSV(value));
		}
	}

	private void writeSelectMultipleAnswer(QuestionEvent questionEvent,
			MultiHashMap<PageID, PageTaskException> taskExceptionMap, Answer answer) {
		int size = questionEvent.getFormAreaList().size();
		for (int itemIndex = 0; itemIndex < size; itemIndex++) {
			this.csvWriter.print(TAB);
			if (answer == null || (taskExceptionMap != null && 0 < taskExceptionMap.size())) {
				continue;
			}
			MarkAreaAnswerItem answerItem = ((MarkAreaAnswer) answer).getMarkAreaAnswerItem(itemIndex);

			if (answerItem.isManualMode()) {
				if (answerItem.isManualSelected()) {
					this.csvWriter.print("1");
				} else {
					this.csvWriter.print("0");
				}
			} else {
				if (answerItem.getDensity() < this.densityThreshold) {
					this.csvWriter.print("1");
				} else {
					this.csvWriter.print("0");
				}
			}
		}
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
	public void startQuestionItem(QuestionItemEvent questionItemEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endQuestionItem(QuestionItemEvent questionItemEvent) {
		// TODO Auto-generated method stub
		
	}
}
