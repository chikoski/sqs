package net.sqs2.omr.result.export;

import java.util.List;

import net.sqs2.omr.result.event.MasterEvent;
import net.sqs2.omr.result.event.PageEvent;
import net.sqs2.omr.result.event.QuestionEvent;
import net.sqs2.omr.result.event.QuestionItemEvent;
import net.sqs2.omr.result.event.RowEvent;
import net.sqs2.omr.result.event.RowGroupEvent;
import net.sqs2.omr.result.event.SessionEvent;
import net.sqs2.omr.result.event.SourceDirectoryEvent;
import net.sqs2.omr.result.event.SpreadSheetEvent;
import net.sqs2.omr.result.export.MarkReadStatus.Select1Status;
import net.sqs2.omr.result.export.MarkReadStatus.SelectStatus;
import net.sqs2.omr.result.model.MarkAreaAnswer;
import net.sqs2.omr.result.model.MarkAreaAnswerItem;
import net.sqs2.omr.source.Config;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.task.TaskError;

import org.apache.commons.collections15.multimap.MultiHashMap;

public class MarkReadStatusReportModule extends SpreadSheetExportEventAdapter {

	MarkReadStatus totalMarkReadStatus;
	
	MarkReadStatus sessionMarkReadStatus;
	MarkReadStatus masterMarkReadStatus;
	MarkReadStatus sourceDirectoryMarkReadStatus;
	MarkReadStatus rowMarkReadStatus;

	float densityThreshold;
	float recognitionMargin;

	protected MarkReadStatusReportModule(){
	}
	
	public MarkReadStatus getTotalMarkReadStatus() {
		return totalMarkReadStatus;
	}

	public MarkReadStatus getSessionMarkReadStatus() {
		return sessionMarkReadStatus;
	}

	public MarkReadStatus getMasterMarkReadStatus() {
		return masterMarkReadStatus;
	}

	public MarkReadStatus getSourceDirectoryMarkReadStatus() {
		return sourceDirectoryMarkReadStatus;
	}

	public MarkReadStatus getRowMarkReadStatus() {
		return rowMarkReadStatus;
	}

	@Override
	public void startSession(SessionEvent sessionEvent) {
		this.sessionMarkReadStatus = new MarkReadStatus();
		this.totalMarkReadStatus = new MarkReadStatus();		
	}

	@Override
	public void startMaster(MasterEvent masterEvent) {
		this.masterMarkReadStatus = new MarkReadStatus();
	}

	@Override
	public void endMaster(MasterEvent masterEvent) {
		this.sessionMarkReadStatus.add(this.masterMarkReadStatus);
	}

	@Override
	public void startSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
		this.sourceDirectoryMarkReadStatus = new MarkReadStatus();
		Config config = sourceDirectoryEvent.getSourceDirectory().getConfiguration().getConfig();
		this.densityThreshold = config.getSourceConfig().getMarkRecognitionConfig().getDensity(); 
		this.recognitionMargin = config.getSourceConfig().getMarkRecognitionConfig().getRecognitionMargin();
	}

	@Override
	public void endSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
		this.masterMarkReadStatus.add(this.sourceDirectoryMarkReadStatus);
	}

	@Override
	public void startSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
	}

	@Override
	public void endSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		
	}

	@Override
	public void startRowGroup(RowGroupEvent rowGroupEvent) {
	}

	@Override
	public void endRowGroup(RowGroupEvent rowGroupEvent) {
	}

	@Override
	public void startRow(RowEvent rowEvent) {
		this.rowMarkReadStatus = new MarkReadStatus();
	}

	@Override
	public void endRow(RowEvent rowEvent) {
		this.sourceDirectoryMarkReadStatus.add(this.rowMarkReadStatus);
		this.totalMarkReadStatus.add(this.rowMarkReadStatus);
		MultiHashMap<PageID,TaskError> taskErrorMap = rowEvent.getTaskErrorMultiHashMap(); 
		if(taskErrorMap != null && 0 < taskErrorMap.size()){
			this.totalMarkReadStatus.numErrorPages += taskErrorMap.size();
		}
		int numPages = rowEvent.getRowGroupEvent().getFormMaster().getNumPages();
		this.totalMarkReadStatus.setNumPages(this.totalMarkReadStatus.getNumPages()+numPages);
	}

	@Override
	public void startPage(PageEvent pageEvent) {
	}

	@Override
	public void endPage(PageEvent pageEvent) {
	}

	@Override
	public void startQuestion(QuestionEvent questionEvent) {
	}

	@Override
	public void endQuestion(QuestionEvent questionEvent) {
		
		if(questionEvent.getDefaultFormArea().isMarkArea()){
			int numSelected = 0;
			MarkAreaAnswer answer = (MarkAreaAnswer) questionEvent.getAnswer();
			
			List<MarkAreaAnswerItem> markedAnswerItems = answer.createMarkAreaAnswerItemSet().getMarkedAnswerItems(densityThreshold, recognitionMargin);
			numSelected += markedAnswerItems.size();
			/*
			for (int itemIndex = 0; itemIndex < questionEvent.getFormAreaList().size(); itemIndex++) {
				MarkAreaAnswerItem markAreaAnswerItem = ((MarkAreaAnswer) answer).getMarkAreaAnswerItem(itemIndex);
								
				if ((markAreaAnswerItem.isManualMode() && markAreaAnswerItem.isManualSelected())
						|| (!markAreaAnswerItem.isManualMode() && markAreaAnswerItem.getDensity() < this.densityThreshold)) {
					numSelected++;
				}
			}
			*/

			if (questionEvent.getDefaultFormArea().isSelect1()) {
				Select1Status select1Status = this.rowMarkReadStatus.getSelect1Status();
				if (numSelected == 0) {
					select1Status.numNoAnsweredQuestions += 1;
				} else if (numSelected == 1) {
					select1Status.numOneSelectedQuestions += 1;
				} else {
					select1Status.numMultiPleSelectedQuestions += 1;
				}
				select1Status.numQuestions++;
			}else if(questionEvent.getDefaultFormArea().isSelect()) {
				SelectStatus selectStatus = this.rowMarkReadStatus.getSelectStatus();
				selectStatus.numMultipleSelectedMarks += numSelected;
				selectStatus.numQuestions += 1;
			}
		}
	}

	@Override
	public void startQuestionItem(QuestionItemEvent questionItemEvent) {
	}

	@Override
	public void endQuestionItem(QuestionItemEvent questionItemEvent) {
	}

}
