/**
 * SpreadSheetExportEventConsumer.java

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

package net.sqs2.omr.session.event;

public interface SpreadSheetOutputEventReciever extends OutputEventReciever {

	public abstract void startSpreadSheet(SpreadSheetEvent spreadSheetEvent);

	public abstract void endSpreadSheet(SpreadSheetEvent spreadSheetEvent);

	public abstract void startRowGroup(RowGroupEvent rowGroupEvent);

	public abstract void endRowGroup(RowGroupEvent rowGroupEvent);

	public abstract void startRow(RowEvent rowEvent);

	public abstract void endRow(RowEvent rowEvent);

	public abstract void startPage(PageEvent pageEvent);

	public abstract void endPage(PageEvent pageEvent);

	public abstract void startQuestion(QuestionEvent questionEvent);

	public abstract void endQuestion(QuestionEvent questionEvent);

	public abstract void startQuestionItem(QuestionItemEvent questionItemEvent);

	public abstract void endQuestionItem(QuestionItemEvent questionItemEvent);

}
