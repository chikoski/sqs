/**
 * SourceDirectoryEvent.java

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

public class SourceDirectoryEvent extends ResultEvent {
	MasterEvent masterEvent;
	FormMaster master;

	SourceDirectoryEvent sourceDirectoryEvent;
	SourceDirectory sourceDirectory;

	public SourceDirectoryEvent(MasterEvent masterEvent, FormMaster master, int numEvents) {
		this.masterEvent = masterEvent;
		this.master = master;
		this.numEvents = numEvents;
	}

	public MasterEvent getMasterEvent() {
		return this.masterEvent;
	}

	void setFormMaster(FormMaster master) {
		this.master = master;
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

}
