/**
 * SpreadSheetEvent.java

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

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.SpreadSheet;

public class SpreadSheetEvent extends ResultEvent {
	SourceDirectoryEvent sourceDirectoryEvent;
	FormMaster master;
	SpreadSheet spreadSheet;
	
	public SpreadSheetEvent(SourceDirectoryEvent sourceDirectoryEvent, FormMaster master, int numEvents) {
		this.sourceDirectoryEvent = sourceDirectoryEvent;
		this.master = master;
		this.numEvents = numEvents;
	}

	public SourceDirectoryEvent getSourceDirectoryEvent() {
		return this.sourceDirectoryEvent;
	}

	public void setSpreadSheet(SpreadSheet spreadSheet) {
		this.spreadSheet = spreadSheet;
	}

	public SpreadSheet getSpreadSheet() {
		return this.spreadSheet;
	}

	public FormMaster getFormMaster() {
		return this.master;
	}
	
}
