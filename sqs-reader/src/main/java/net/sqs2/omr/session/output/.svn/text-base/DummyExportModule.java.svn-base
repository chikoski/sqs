/**
 * DummyExportModule.java

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

import java.util.logging.Logger;

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

public class DummyExportModule implements SpreadSheetOutputEventReciever{

	public DummyExportModule() {
	}

	@Override
	public void startSession(SessionEvent sessionEvent) {
		Logger.getLogger(getClass().getName()).info(
				"+Session:" + sessionEvent.getSessionID());
	}

	@Override
	public void endSession(SessionEvent sessionEvent) {
		Logger.getLogger(getClass().getName()).info(
				"-Session:" + sessionEvent.getSessionID());
	}

	@Override
	public void startMaster(MasterEvent masterEvent) {
		Logger.getLogger(getClass().getName()).info(
				"+Master:" + masterEvent.getFormMaster().getFileResourceID().getRelativePath());
	}

	@Override
	public void endMaster(MasterEvent masterEvent) {
		Logger.getLogger(getClass().getName()).info(
				"-Master:" + masterEvent.getFormMaster().getFileResourceID().getRelativePath());
	}

	@Override
	public void startSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
		Logger.getLogger(getClass().getName()).info(
				"+SourceDirectory:" + sourceDirectoryEvent.getSourceDirectory().getRelativePath());
	}

	@Override
	public void endSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
		Logger.getLogger(getClass().getName()).info(
				"-SourceDirectory:" + sourceDirectoryEvent.getSourceDirectory().getRelativePath());
	}


	@Override
	public void startSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		Logger.getLogger(getClass().getName()).info(
				"+SpreadSheet:" + spreadSheetEvent.getSourceDirectoryEvent().getSourceDirectory().getRelativePath());
	}

	@Override
	public void endSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		Logger.getLogger(getClass().getName()).info(
				"-SpreadSheet:" + spreadSheetEvent.getSourceDirectoryEvent().getSourceDirectory().getRelativePath());
	}

	@Override
	public void startRowGroup(RowGroupEvent rowGroupEvent) {
		Logger.getLogger(getClass().getName()).info("+RowGroup:" + rowGroupEvent.getSourceDirectory().getRelativePath());
	}

	@Override
	public void endRowGroup(RowGroupEvent rowGroupEvent) {
		Logger.getLogger(getClass().getName()).info("-RowGroup:" + rowGroupEvent.getSourceDirectory().getRelativePath());
	}

	@Override
	public void startRow(RowEvent rowEvent) {
		Logger.getLogger(getClass().getName()).info("+Row:" + rowEvent.getRowIndex());
	}

	@Override
	public void endRow(RowEvent rowEvent) {
		Logger.getLogger(getClass().getName()).info("-Row:" + rowEvent.getRowIndex());
	}

	@Override
	public void startPage(PageEvent pageEvent) {
		//Logger.getLogger(getClass().getName()).info("+Page:" + pageEvent.getPageIndex());
	}

	@Override
	public void endPage(PageEvent pageEvent) {
		//Logger.getLogger(getClass().getName()).info("-Page:" + pageEvent.getPageIndex());
	}

	@Override
	public void startQuestion(QuestionEvent questionEvent) {
		//Logger.getLogger(getClass().getName()).info(
		//		"+Q:" + questionEvent.getColumnIndex() + " " + questionEvent.getPrimaryFormArea());
	}

	@Override
	public void endQuestion(QuestionEvent questionEvent) {
		//Logger.getLogger(getClass().getName()).info(
		//		"-Q:" + questionEvent.getColumnIndex() + " " + questionEvent.getPrimaryFormArea());
	}

	@Override
	public void startQuestionItem(QuestionItemEvent questionItemEvent) {
	}

	@Override
	public void endQuestionItem(QuestionItemEvent questionItemEvent) {
	}

}
