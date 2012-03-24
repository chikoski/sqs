/**
 * MarkReadStatusReportModule.java

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

import java.util.List;

import net.sqs2.omr.model.Config;
import net.sqs2.omr.model.MarkAreaAnswer;
import net.sqs2.omr.model.MarkAreaAnswerItem;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTaskException;
import net.sqs2.omr.model.SourceConfig;
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
import net.sqs2.omr.session.output.MarkReadStatus.Select1Status;
import net.sqs2.omr.session.output.MarkReadStatus.SelectStatus;

import org.apache.commons.collections15.multimap.MultiHashMap;

public class MarkReadStatusOutputModule implements SpreadSheetOutputEventReciever{

	MarkReadStatus totalMarkReadStatus;

	MarkReadStatus sessionMarkReadStatus;
	MarkReadStatus masterMarkReadStatus;
	MarkReadStatus sourceDirectoryMarkReadStatus;
	MarkReadStatus rowMarkReadStatus;

	float densityThreshold;
	float doubleMarkIgnoranceThreshold;
	float noMarkRecoveryThreshold;

	protected MarkReadStatusOutputModule() {
	}

	public MarkReadStatus getTotalMarkReadStatus() {
		return this.totalMarkReadStatus;
	}

	public MarkReadStatus getSessionMarkReadStatus() {
		return this.sessionMarkReadStatus;
	}

	public MarkReadStatus getMasterMarkReadStatus() {
		return this.masterMarkReadStatus;
	}

	public MarkReadStatus getSourceDirectoryMarkReadStatus() {
		return this.sourceDirectoryMarkReadStatus;
	}

	public MarkReadStatus getRowMarkReadStatus() {
		return this.rowMarkReadStatus;
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
		SourceConfig sourceConfig = (SourceConfig)config.getPrimarySourceConfig();
		this.densityThreshold = sourceConfig.getMarkRecognitionConfig().getMarkRecognitionDensityThreshold();
		this.doubleMarkIgnoranceThreshold = sourceConfig.getMarkRecognitionConfig()
		.getDoubleMarkErrorSuppressionThreshold();
		this.noMarkRecoveryThreshold = sourceConfig.getMarkRecognitionConfig().getNoMarkErrorSuppressionThreshold();
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
		MultiHashMap<PageID, PageTaskException> taskExceptionMap = rowEvent.getTaskExceptionMultiHashMap();
		if (taskExceptionMap != null && 0 < taskExceptionMap.size()) {
			this.totalMarkReadStatus.numErrorPages += taskExceptionMap.size();
		}
		int numPages = rowEvent.getRowGroupEvent().getFormMaster().getNumPages();
		this.totalMarkReadStatus.setNumPages(this.totalMarkReadStatus.getNumPages() + numPages);
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

		if (questionEvent.getPrimaryFormArea().isMarkArea()) {
			int numSelected = 0;
			MarkAreaAnswer answer = (MarkAreaAnswer) questionEvent.getAnswer();

			List<MarkAreaAnswerItem> markedAnswerItems = answer.createMarkAreaAnswerItemSet()
					.getMarkedAnswerItems(this.densityThreshold, this.doubleMarkIgnoranceThreshold, this.noMarkRecoveryThreshold);
			numSelected += markedAnswerItems.size();
			/*
			 * for (int itemIndex = 0; itemIndex <
			 * questionEvent.getFormAreaList().size(); itemIndex++) {
			 * MarkAreaAnswerItem markAreaAnswerItem = ((MarkAreaAnswer)
			 * answer).getMarkAreaAnswerItem(itemIndex);
			 * 
			 * if ((markAreaAnswerItem.isManualMode() &&
			 * markAreaAnswerItem.isManualSelected()) ||
			 * (!markAreaAnswerItem.isManualMode() &&
			 * markAreaAnswerItem.getDensity() < this.densityThreshold)) {
			 * numSelected++; } }
			 */

			if (questionEvent.getPrimaryFormArea().isSelectSingle()) {
				Select1Status select1Status = this.rowMarkReadStatus.getSelect1Status();
				if (numSelected == 0) {
					select1Status.numNoAnsweredQuestions += 1;
				} else if (numSelected == 1) {
					select1Status.numOneSelectedQuestions += 1;
				} else {
					select1Status.numMultiPleSelectedQuestions += 1;
				}
				select1Status.numQuestions++;
			} else if (questionEvent.getPrimaryFormArea().isSelectMultiple()) {
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

	@Override
	public void endSession(SessionEvent sessionEvent) {
		// TODO Auto-generated method stub
		
	}

}
