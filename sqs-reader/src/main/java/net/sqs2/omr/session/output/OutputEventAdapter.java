/**
 * ExportEventAdapter.java

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

import net.sqs2.omr.session.event.MasterEvent;
import net.sqs2.omr.session.event.OutputEventReciever;
import net.sqs2.omr.session.event.SessionEvent;
import net.sqs2.omr.session.event.SourceDirectoryEvent;

public abstract class OutputEventAdapter implements OutputEventReciever {

	public OutputEventAdapter() {
		super();
	}

	public void startSession(SessionEvent sessionEvent) {
		// TODO Auto-generated method stub

	}

	public void startMaster(MasterEvent masterEvent) {
		// TODO Auto-generated method stub

	}

	public void startSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
		// TODO Auto-generated method stub

	}

	public void endSession(SessionEvent sessionEvent) {
		// TODO Auto-generated method stub

	}

	public void endMaster(MasterEvent masterEvent) {
		// TODO Auto-generated method stub

	}

	public void endSourceDirectory(SourceDirectoryEvent spreadSheetEvent) {
		// TODO Auto-generated method stub

	}

}
