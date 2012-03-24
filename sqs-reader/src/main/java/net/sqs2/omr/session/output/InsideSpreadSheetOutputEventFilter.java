/**
 * SpreadSheetExportEventFilter.java

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

import net.sqs2.omr.session.event.PageEvent;
import net.sqs2.omr.session.event.QuestionEvent;
import net.sqs2.omr.session.event.QuestionItemEvent;
import net.sqs2.omr.session.event.RowEvent;
import net.sqs2.omr.session.event.RowGroupEvent;
import net.sqs2.omr.session.event.SpreadSheetEvent;

public class InsideSpreadSheetOutputEventFilter extends OutputEventFilter {

	public boolean accept(SpreadSheetEvent spreadSheetEvent) {
		return true;
	}

	public boolean accept(RowGroupEvent rowGroupEvent) {
		return true;
	}

	public boolean accept(RowEvent rowEvent) {
		return true;
	}

	public boolean accept(QuestionEvent questionEvent) {
		return true;
	}

	public boolean accept(PageEvent pageEvent) {
		return true;
	}

	public boolean accept(QuestionItemEvent questionItemEvent) {
		return true;
	}

}
