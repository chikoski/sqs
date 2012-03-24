/**
 * ResultWalker.java

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

package net.sqs2.omr.session.output;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTaskAccessor;
import net.sqs2.omr.model.Row;
import net.sqs2.omr.model.RowAccessor;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SessionSourceState;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.SpreadSheet;
import net.sqs2.omr.session.event.MasterEvent;
import net.sqs2.omr.session.event.OutputEventReciever;
import net.sqs2.omr.session.event.PageEvent;
import net.sqs2.omr.session.event.QuestionEvent;
import net.sqs2.omr.session.event.QuestionItemEvent;
import net.sqs2.omr.session.event.RowEvent;
import net.sqs2.omr.session.event.RowGroupEvent;
import net.sqs2.omr.session.event.SessionEvent;
import net.sqs2.omr.session.event.SourceDirectoryEvent;
import net.sqs2.omr.session.event.SpreadSheetEvent;
import net.sqs2.omr.session.event.SpreadSheetOutputEventReciever;

public class OutputGenerator{
	
	SessionSource sessionSource = null;
	RowAccessor rowAccessor = null;
	PageTaskAccessor taskAccessor = null;
	ArrayList<SpreadSheetOutputEventReciever> consumerList;

	InsideSpreadSheetOutputEventFilter filter;

	OutputModule outputModule;
	
	public OutputGenerator(SessionSource sessionSource) throws IOException{
		this.sessionSource = sessionSource;
		this.rowAccessor = sessionSource.getContentAccessor().getRowAccessor();
		this.taskAccessor = sessionSource.getContentAccessor().getPageTaskAccessor();
		this.consumerList = new ArrayList<SpreadSheetOutputEventReciever>();
		
		long sessionID = sessionSource.getSessionID();
		this.outputModule = new OutputModule(sessionID);
		this.consumerList.add(outputModule);
	}

	public OutputGenerator(SessionSource sessionSource, InsideSpreadSheetOutputEventFilter filter) throws IOException{
		this(sessionSource);
		this.filter = filter;
	}
	
	public void addEventConsumer(SpreadSheetOutputEventReciever consumer) {
		this.consumerList.add(consumer);
	}
	
	public void addEventConsumers(List<SpreadSheetOutputEventReciever> consumers) {
		this.consumerList.addAll(consumers);
	}
	
	public Void call() throws OutputStopException {

		SessionEvent sessionEvent = new SessionEvent(this.sessionSource.getSessionID());
		sessionEvent.setStart();
		sessionEvent.setIndex(0);
		if (this.filter != null && !this.filter.accept(sessionEvent)) {
			return null;
		}
		startSession(sessionEvent);
		processSession(sessionEvent);
		sessionEvent.setEnd();
		endSession(sessionEvent);
		return null;
	}

	public void stop() {
		this.sessionSource.setSessionSourceState(SessionSourceState.STOPPED);
	}

	
	protected void processSession(SessionEvent sessionEvent) throws OutputStopException {
		Logger.getLogger(getClass().getName()).info(" + ProcessSession: " + sessionEvent.getSessionID());

		List<FormMaster> masterList = this.sessionSource.getContentIndexer().getFormMasterList();
		MasterEvent masterEvent = new MasterEvent(sessionEvent, masterList.size());

		for (int masterIndex = 0; masterIndex < masterList.size(); masterIndex++) {
			FormMaster master = masterList.get(masterIndex);
			masterEvent.setStart();
			masterEvent.setIndex(masterIndex);
			masterEvent.setFormMaster((FormMaster) master);
			if (this.filter != null && !this.filter.accept(masterEvent)) {
				return;
			}
			startMaster(masterEvent);
			processMaster(masterEvent, (FormMaster) master);
			masterEvent.setEnd();
			endMaster(masterEvent);
		}
		Logger.getLogger(getClass().getName()).info(" - ProcessSession: " + sessionEvent.getSessionID());
	}

	protected void processMaster(MasterEvent masterEvent, FormMaster master) throws OutputStopException {
		Logger.getLogger(getClass().getName()).info(" + ProcessMaster: " + master);
		List<SourceDirectory> sourceDirectoryList = this.sessionSource.getContentIndexer()
				.getFlattenSourceDirectoryList(master);
		SourceDirectoryEvent sourceDirectoryEvent = new SourceDirectoryEvent(masterEvent, master,
				sourceDirectoryList.size());

		for (int sourceDirectoryIndex = 0; sourceDirectoryIndex < sourceDirectoryList.size(); sourceDirectoryIndex++) {
			SourceDirectory sourceDirectory = sourceDirectoryList.get(sourceDirectoryIndex);
			sourceDirectoryEvent.setStart();
			sourceDirectoryEvent.setIndex(sourceDirectoryIndex);
			sourceDirectoryEvent.setSourceDirectory(sourceDirectory);
			startSourceDirectory(sourceDirectoryEvent);
			Logger.getLogger(getClass().getName()).info(" ++ ProcessMaster: " + master);
			processSourceDirectory(sourceDirectoryEvent, master);
			Logger.getLogger(getClass().getName()).info(" -- ProcessMaster: " + master);
			sourceDirectoryEvent.setEnd();
			endSourceDirectory(sourceDirectoryEvent);
		}
		Logger.getLogger(getClass().getName()).info(" - ProcessMaster: " + master);
	}
	
	protected void processSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent, FormMaster master) throws OutputStopException {
		SpreadSheetEvent spreadSheetEvent = new SpreadSheetEvent(sourceDirectoryEvent, master, 1);
		long sessionID = sourceDirectoryEvent.getMasterEvent().getSessionEvent().getSessionID();
		SpreadSheet spreadSheet = new SpreadSheet(sessionID, master, sourceDirectoryEvent.getSourceDirectory());
		spreadSheetEvent.setStart();
		spreadSheetEvent.setIndex(0);
		spreadSheetEvent.setSpreadSheet(spreadSheet);

		if (this.filter != null && !this.filter.accept(spreadSheetEvent)) {
			return;
		}

		startSpreadSheet(spreadSheetEvent);
		processSpreadSheet(spreadSheetEvent, master);

		spreadSheetEvent.setEnd();
		endSpreadSheet(spreadSheetEvent);
	}

	protected void processSpreadSheet(SpreadSheetEvent spreadSheetEvent, FormMaster master) throws OutputStopException {
		SourceDirectory targetSourceDirectory = spreadSheetEvent.getSpreadSheet().getSourceDirectory();
		List<SourceDirectory> sourceDirectoryList = targetSourceDirectory.getDescendentSourceDirectoryList();
		RowGroupEvent rowGroupEvent = new RowGroupEvent(spreadSheetEvent, master, 
				spreadSheetEvent.getSpreadSheet(), sourceDirectoryList.size() + 1);
		rowGroupEvent.setBaseRowGroup(false);

		int rowIndexBase = 0;		
		
		for (int sourceDirectoryIndex = 0; sourceDirectoryIndex < sourceDirectoryList.size(); sourceDirectoryIndex++) {
			SourceDirectory sourceDirectory = sourceDirectoryList.get(sourceDirectoryIndex);
			rowGroupEvent.setStart();
			rowGroupEvent.setIndex(sourceDirectoryIndex);
			rowGroupEvent.setParentSourceDirectory(targetSourceDirectory);
			rowGroupEvent.setSourceDirectory(sourceDirectory);
			rowGroupEvent.setRowIndexBase(rowIndexBase);
			if (this.filter != null
					&& !this.filter.accept(rowGroupEvent)) {
				continue;
			}
			startRowGroup(rowGroupEvent);
			processRowGroup(rowGroupEvent, rowIndexBase, master);
			rowGroupEvent.setEnd();
			endRowGroup(rowGroupEvent);
			rowIndexBase += sourceDirectory.getNumPageIDs() / master.getNumPages();
		}
		
		rowGroupEvent.setIndex(sourceDirectoryList.size());
		rowGroupEvent.setBaseRowGroup(true);
		rowGroupEvent.setRowIndexBase(rowIndexBase);
		rowGroupEvent.setSourceDirectory(targetSourceDirectory);
		rowGroupEvent.setParentSourceDirectory(targetSourceDirectory);
		rowGroupEvent.setStart();
		if (this.filter == null
				|| this.filter.accept(rowGroupEvent)) {
			startRowGroup(rowGroupEvent);
			processRowGroup(rowGroupEvent, rowIndexBase, master);
			rowGroupEvent.setEnd();
			endRowGroup(rowGroupEvent);
		}
	}

	protected void processRowGroup(RowGroupEvent rowGroupEvent, int rowIndexBase, FormMaster master) {
		SourceDirectory sourceDirectory = rowGroupEvent.getSourceDirectory();
		int numPages = master.getNumPages();
		List<PageID> pageIDList = sourceDirectory.getPageIDList();
		int numRows = pageIDList.size() / numPages;
		RowEvent rowEvent = new RowEvent(rowGroupEvent, numRows);
		String masterPath = master.getRelativePath();
		String sourceDirectoryPath = sourceDirectory.getRelativePath();
		for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
			rowEvent.setStart();
			rowEvent.setIndex(rowIndex);
			try {
				Row row = (Row) this.rowAccessor.get(masterPath, sourceDirectoryPath, rowIndex);
				rowEvent.setRow(row);
				rowEvent.setRowIndex(rowIndex + rowIndexBase);
				rowEvent.setPageIDList(pageIDList);

				if (this.filter != null
						&& !this.filter.accept(rowEvent)) {
					return;
				}

				if (row == null) {
					throw new RuntimeException("row == null[sourceDirectoryPath="+sourceDirectoryPath+" rowIndex="+rowIndex+"]");
				}

				rowEvent.setTaskExceptionMultiHashMap(row.getTaskErrorMultiHashMap());

				startRow(rowEvent);
				processRow(rowEvent, master);
				rowEvent.setEnd();
				endRow(rowEvent);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally{
				this.rowAccessor.flush();
			}
		}
	}

	protected void processRow(RowEvent rowEvent, FormMaster master) throws OutputStopException {
		int numColumns = master.getNumQuestions();
		int numPages = master.getNumPages();
		PageEvent pageEvent = new PageEvent(rowEvent, master);
		QuestionEvent questionEvent = new QuestionEvent(rowEvent, master);
		int prevPageIndex = -1;

		List<PageID> pageIDList = rowEvent.getPageIDList();

		for (int columnIndex = 0; columnIndex < numColumns; columnIndex++) {
			List<FormArea> formAreaList = master.getFormAreaList(columnIndex);
			int pageIndex = formAreaList.get(0).getPageIndex();
			if (prevPageIndex != pageIndex) {

				if (prevPageIndex == -1) {
					pageEvent.setEnd();
					endPage(pageEvent);
				}

				pageEvent.setStart();
				int pageNumber = pageIndex + 1;
				PageID pageID = pageIDList.get(numPages * rowEvent.getIndex() + pageIndex);
				OMRPageTask pageTask = this.taskAccessor.get(pageNumber, pageID);
				pageEvent.setPageTask(pageTask);
				pageEvent.setPageIndex(pageIndex);

				if (this.filter != null
						&& !this.filter.accept(pageEvent)) {
					return;
				}
				startPage(pageEvent);
			}
			questionEvent.setStart();
			questionEvent.setIndex(columnIndex);
			questionEvent.setQuestionIndex(columnIndex);
			questionEvent.setFormAreaList(formAreaList);
			if (this.filter != null
					&& !this.filter.accept(questionEvent)) {
				return;
			}

			if (questionEvent.getAnswer() == null) {
				Logger.getLogger(getClass().getName()).severe("answer == null");
				return;
			}

			startQuestion(questionEvent);
			processQuestion(pageEvent, questionEvent, master, formAreaList);
			questionEvent.setEnd();
			endQuestion(questionEvent);

			prevPageIndex = pageIndex;
		}

		if (prevPageIndex != -1) {
			pageEvent.setEnd();
			endPage(pageEvent);
		}

	}

	protected void processQuestion(PageEvent pageEvent, QuestionEvent questionEvent, FormMaster master, List<FormArea> formAreaList) {
		int numItems = formAreaList.size();
		QuestionItemEvent questionItemEvent = new QuestionItemEvent(questionEvent);
		for (int itemIndex = 0; itemIndex < numItems; itemIndex++) {
			questionItemEvent.setStart();
			questionItemEvent.setPageEvent(pageEvent);
			questionItemEvent.setItemIndex(itemIndex);
			FormArea formArea = formAreaList.get(itemIndex);
			questionItemEvent.setFormArea(formArea);
			startQuestionItem(questionItemEvent);
			processQuestionItem(questionItemEvent, master, formAreaList.get(itemIndex));
			questionItemEvent.setEnd();
			endQuestionItem(questionItemEvent);
		}
	}

	private boolean hasStopped(){
		return this.sessionSource.getSessionSourceState().equals(SessionSourceState.STOPPED);
	}
	
	protected void processQuestionItem(QuestionItemEvent questionItemEvent, FormMaster master, FormArea formArea) {
		// do nothing
	}

	protected void startSession(SessionEvent sessionEvent) {
		for (OutputEventReciever consumer : this.consumerList) {
			consumer.startSession(sessionEvent);
		}
	}

	protected void endSession(SessionEvent sessionEvent) {
		for (OutputEventReciever consumer : this.consumerList) {
			consumer.endSession(sessionEvent);
		}
	}

	protected void startMaster(MasterEvent masterEvent) {
		for (OutputEventReciever consumer : this.consumerList) {
			consumer.startMaster(masterEvent);
		}
	}

	protected void endMaster(MasterEvent masterEvent) {
		for (OutputEventReciever consumer : this.consumerList) {
			consumer.endMaster(masterEvent);
		}
	}

	protected void startSourceDirectory(SourceDirectoryEvent spreadSheetEvent) {
		for (OutputEventReciever consumer : this.consumerList) {
			consumer.startSourceDirectory(spreadSheetEvent);
		}
	}

	protected void endSourceDirectory(SourceDirectoryEvent spreadSheetEvent) {
		for (OutputEventReciever consumer : this.consumerList) {
			consumer.endSourceDirectory(spreadSheetEvent);
		}
	}

	protected void startSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		for (SpreadSheetOutputEventReciever consumer : this.consumerList) {
			consumer.startSpreadSheet(spreadSheetEvent);
		}
	}

	protected void endSpreadSheet(SpreadSheetEvent spreadSheetEvent) throws OutputStopException {
		for (SpreadSheetOutputEventReciever consumer : this.consumerList) {
			consumer.endSpreadSheet(spreadSheetEvent);
		}
		if (hasStopped()) {
			throw new OutputStopException();
		}
	}

	protected void startRowGroup(RowGroupEvent rowGroupEvent) {
		for (SpreadSheetOutputEventReciever consumer : this.consumerList) {
			consumer.startRowGroup(rowGroupEvent);
		}
	}

	protected void endRowGroup(RowGroupEvent sourceDirectoryEvent) throws OutputStopException {
		for (SpreadSheetOutputEventReciever consumer : this.consumerList) {
			consumer.endRowGroup(sourceDirectoryEvent);
		}
		if (hasStopped()) {
			throw new OutputStopException();
		}
	}

	protected void startRow(RowEvent rowEvent) {
		for (SpreadSheetOutputEventReciever consumer : this.consumerList) {
			consumer.startRow(rowEvent);
		}
	}

	protected void endRow(RowEvent rowEvent) throws OutputStopException {
		for (SpreadSheetOutputEventReciever consumer : this.consumerList) {
			consumer.endRow(rowEvent);
		}
		if (hasStopped()) {
			throw new OutputStopException();
		}
	}

	protected void startPage(PageEvent pageEvent) {
		for (SpreadSheetOutputEventReciever consumer : this.consumerList) {
			consumer.startPage(pageEvent);
		}
	}

	protected void endPage(PageEvent pageEvent) throws OutputStopException {
		for (SpreadSheetOutputEventReciever consumer : this.consumerList) {
			consumer.endPage(pageEvent);
		}
		if (hasStopped()) {
			throw new OutputStopException();
		}
	}

	protected void startQuestion(QuestionEvent questionEvent) {
		for (SpreadSheetOutputEventReciever consumer : this.consumerList) {
			consumer.startQuestion(questionEvent);
		}
	}

	protected void endQuestion(QuestionEvent questionEvent) {
		for (SpreadSheetOutputEventReciever consumer : this.consumerList) {
			consumer.endQuestion(questionEvent);
		}
	}

	protected void startQuestionItem(QuestionItemEvent questionItemEvent) {
		for (SpreadSheetOutputEventReciever consumer : this.consumerList) {
			consumer.startQuestionItem(questionItemEvent);
		}
	}

	protected void endQuestionItem(QuestionItemEvent questionItemEvent) {
		for (SpreadSheetOutputEventReciever consumer : this.consumerList) {
			consumer.endQuestionItem(questionItemEvent);
		}
	}
	
	public static class SpreadSheetReportGenerator extends OutputGenerator {
		SpreadSheet spreadSheet = null;
		public SpreadSheetReportGenerator(SessionSource sessionSource, SpreadSheet spreadSheet) throws IOException{
			super(sessionSource);
			this.spreadSheet = spreadSheet;
		}

		@Override
		public Void call() throws OutputStopException {
			SourceDirectory sourceDirectory = spreadSheet.getSourceDirectory();
			FormMaster master = (FormMaster) sourceDirectory.getDefaultFormMaster();

			SessionEvent sessionEvent = new SessionEvent(spreadSheet.getSessionID());
			List<FormMaster> masterList = this.sessionSource.getContentIndexer().getFormMasterList();
			MasterEvent masterEvent = new MasterEvent(sessionEvent, masterList.size());
			SourceDirectoryEvent sourceDirectoryEvent = new SourceDirectoryEvent(masterEvent, master, 0);
			sourceDirectoryEvent.setSourceDirectory(sourceDirectory);
			sourceDirectoryEvent.setIndex(0);
			sourceDirectoryEvent.setStart();
			processSourceDirectory(sourceDirectoryEvent, master);
			return null;
		}
	}

}
