/**
 * SpreadSheetExportEventAdapter.java

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


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.Answer;
import net.sqs2.omr.model.MarkAreaAnswer;
import net.sqs2.omr.model.MarkAreaAnswerItem;
import net.sqs2.omr.model.MarkRecognitionConfig;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.model.SourceConfig;
import net.sqs2.omr.session.event.PageEvent;
import net.sqs2.omr.session.event.QuestionEvent;
import net.sqs2.omr.session.event.QuestionItemEvent;
import net.sqs2.omr.session.event.RowEvent;
import net.sqs2.omr.session.event.RowGroupEvent;
import net.sqs2.omr.session.event.SourceDirectoryEvent;
import net.sqs2.omr.session.event.SpreadSheetEvent;
import net.sqs2.omr.session.event.SpreadSheetOutputEventReciever;

public final class OutputModule extends OutputEventAdapter implements SpreadSheetOutputEventReciever {

	private float densityThreshold;
	private float doubleMarkErrorSuppressionThreshold;
	private float noMarkErrorSuppressionThreshold;
	
	private int[][] valueTotalMatrix;
	private int[] numNoValues;
	private int[] numMultipleValues;
	private long[] targetLastModifiedArray;

	private static final Map<Long,OutputModule> map = new HashMap<Long,OutputModule>();
	
	public OutputModule(long sessionID){
		map.put(sessionID, this);
	}
	
	public static OutputModule getInstance(long sessionID){
		return map.get(sessionID);
	}
	
	public float getDensityThreshold() {
		return densityThreshold;
	}

	public float getDoubleMarkErrorSuppressionThreshold() {
		return doubleMarkErrorSuppressionThreshold;
	}

	public float getNoMarkErrorSuppressionThreshold() {
		return noMarkErrorSuppressionThreshold;
	}

	public int[][] getValueTotalMatrix() {
		return valueTotalMatrix;
	}

	public int[] getNumNoValues() {
		return numNoValues;
	}

	public int[] getNumMultipleValues() {
		return numMultipleValues;
	}

	public long[] getTargetLastModifiedArray() {
		return targetLastModifiedArray;
	}

	@Override
	public void startQuestion(QuestionEvent questionEvent) {
		List<FormArea> formAreaList = questionEvent.getFormAreaList();
		FormArea formArea = formAreaList.get(0);

		Answer answer = questionEvent.getRowEvent().getRow().getAnswer(questionEvent.getQuestionIndex());
		if (answer == null) {
			return;
		}
		if (formArea.isSelectSingle()) {
			int numSelectedItemIndex = -1;
			int numSelected = 0;

			List<MarkAreaAnswerItem> markAreaAnswerItemList =
				((MarkAreaAnswer) answer).createMarkAreaAnswerItemSet().getMarkedAnswerItems(this.densityThreshold,
					this.doubleMarkErrorSuppressionThreshold, this.noMarkErrorSuppressionThreshold);
			
			for (MarkAreaAnswerItem markAreaAnswerItem : markAreaAnswerItemList){ 
				numSelectedItemIndex = markAreaAnswerItem.getItemIndex();
				numSelected++;
			}

			if (numSelected == 0) {
				this.numNoValues[formArea.getQuestionIndex()]++;
			} else if (numSelected == 1) {
				this.valueTotalMatrix[formArea.getQuestionIndex()][numSelectedItemIndex]++;
			} else {
				this.numMultipleValues[formArea.getQuestionIndex()]++;
			}

		} else if (formArea.isSelectMultiple()) {
			for (int itemIndex = 0; itemIndex < formAreaList.size(); itemIndex++) {
				MarkAreaAnswerItem markAreaAnswerItem = ((MarkAreaAnswer) answer)
						.getMarkAreaAnswerItem(itemIndex);
				if (markAreaAnswerItem
						.isSelectMultiSelected(((MarkAreaAnswer) answer), this.densityThreshold)) {
					this.valueTotalMatrix[formArea.getQuestionIndex()][itemIndex]++;
				}
			}
		}
	}

	@Override
	public void startQuestionItem(QuestionItemEvent questionItemEvent) {
		FormArea formArea = questionItemEvent.getFormArea();
		if (formArea.isTextArea()) {
		} else if (formArea.isMarkArea()) {
			OMRPageTask pageTask = questionItemEvent.getPageEvent().getPageTask();
			long targetFileLastModified = pageTask.getPageID().getFileResourceID().getLastModified();
			long configHandlerLastModified = pageTask.getConfigHandlerFileResourceID().getLastModified();
			long masterFileLastModified = questionItemEvent.getQuestionEvent().getFormMaster()
					.getLastModified();
			long prevTargetLastModified = this.targetLastModifiedArray[formArea.getQuestionIndex()];

			this.targetLastModifiedArray[formArea.getQuestionIndex()] = max(max(targetFileLastModified,
					configHandlerLastModified), max(masterFileLastModified, prevTargetLastModified));
		}
	}

	public void endPage(PageEvent pageEvent) {
		// TODO Auto-generated method stub

	}

	public void endQuestion(QuestionEvent questionEvent) {
		// TODO Auto-generated method stub

	}

	public void endQuestionItem(QuestionItemEvent questionItemEvent) {
		// TODO Auto-generated method stub

	}

	public void endRow(RowEvent rowEvent) {
		// TODO Auto-generated method stub

	}

	public void endRowGroup(RowGroupEvent rowGroupEvent) {
		// TODO Auto-generated method stub

	}

	public void startPage(PageEvent pageEvent) {
		// TODO Auto-generated method stub

	}

	public void startRow(RowEvent rowEvent) {
		// TODO Auto-generated method stub

	}

	public void startRowGroup(RowGroupEvent rowGroupEvent) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void startSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
		MarkRecognitionConfig config = ((SourceConfig)sourceDirectoryEvent.getSourceDirectory().getConfiguration()
		.getConfig().getPrimarySourceConfig()).getMarkRecognitionConfig();
		this.densityThreshold = config.getMarkRecognitionDensityThreshold();
		this.doubleMarkErrorSuppressionThreshold = config.getDoubleMarkErrorSuppressionThreshold();
		this.noMarkErrorSuppressionThreshold = config.getNoMarkErrorSuppressionThreshold();
	}

	@Override
	public void endSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
	}


	@Override
	public void startSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		FormMaster master = spreadSheetEvent.getFormMaster();

		this.valueTotalMatrix = new int[master.getNumQuestions()][];
		this.numNoValues = new int[master.getNumQuestions()];
		this.numMultipleValues = new int[master.getNumQuestions()];
		this.targetLastModifiedArray = new long[master.getNumQuestions()];

		for (int columnIndex = 0; columnIndex < master.getNumQuestions(); columnIndex++) {
			List<FormArea> formAreaList = master.getFormAreaList(columnIndex);
			FormArea formArea = formAreaList.get(0);
			if (formArea.isSelectSingle() || formArea.isSelectMultiple()) {
				this.valueTotalMatrix[columnIndex] = new int[formAreaList.size()];
			}
		}
	}

	@Override
	public void endSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
	}
	
	private long max(long a, long b) {
		if (a <= b) {
			return b;
		} else {
			return a;
		}
	}

}
