/**
 * RowGroupEvent.java

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
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.SpreadSheet;

public class RowGroupEvent extends ResultEvent {

	SpreadSheetEvent spreadSheetEvent;
	FormMaster master;
	SpreadSheet spreadSheet;
	SourceDirectory sourceDirectory;
	SourceDirectory parentSourceDirectory;
	int rowIndexBase;
	
	boolean isBaseRowGroup;

	public RowGroupEvent(SpreadSheetEvent spreadSheetEvent, FormMaster master, SpreadSheet spreadSheet, int numEvents) {
		this.spreadSheetEvent = spreadSheetEvent;
		this.master = master;
		this.spreadSheet = spreadSheet;
		this.numEvents = numEvents;
	}
	
	public void setBaseRowGroup(boolean isBaseRowGroup){
		this.isBaseRowGroup= isBaseRowGroup;
	}
	
	public boolean isBaseRowGroup(){
		return this.isBaseRowGroup;
	}

	public int getRowIndexBase() {
		return this.rowIndexBase;
	}

	public void setRowIndexBase(int rowIndexBase) {
		this.rowIndexBase = rowIndexBase;
	}

	public SpreadSheetEvent getSpreadSheetEvent() {
		return this.spreadSheetEvent;
	}

	public FormMaster getFormMaster() {
		return this.master;
	}

	public void setSourceDirectory(SourceDirectory sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public SourceDirectory getSourceDirectory() {
		return this.sourceDirectory;
	}

	public void setParentSourceDirectory(SourceDirectory parentSourceDirectory) {
		this.parentSourceDirectory = parentSourceDirectory;
	}

	public SourceDirectory getParentSourceDirectory() {
		return this.parentSourceDirectory;
	}

	@Override
	public String toString() {
		return "RowGroupEvent:[" + this.parentSourceDirectory + " : " + this.sourceDirectory + "]";
	}

}
