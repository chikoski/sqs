/*

 MarkReaderCommandLineLauncher.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on Apr 7, 2007

 */
package net.sqs2.omr.app;

import java.io.File;
import java.net.URL;

import net.sqs2.event.EventListener;
import net.sqs2.net.ClassURLStreamHandlerFactory;
import net.sqs2.omr.session.event.SessionEvent;
import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.omr.session.service.OutputEventRecieversFactory;

public class MarkReaderNoGUILauncher {

	public static void main(String[] args) throws Exception {

		try {
			URL.setURLStreamHandlerFactory(ClassURLStreamHandlerFactory.getSingleton());
		} catch (Error ex) {
			ex.printStackTrace();
		}
		
		boolean isLocalTaskExecutorEnabled = true;

		final MarkReaderApp markReaderApp = new MarkReaderApp(1099, isLocalTaskExecutorEnabled);

		if (args.length == 1) {
			File sourceDirectoryRoot = new File(args[0]);
			MarkReaderSession session = markReaderApp.createSession(sourceDirectoryRoot,
					new OutputEventRecieversFactory(OutputEventRecieversFactory.Mode.BASE));
			session.getSessionEventSource().addListener(new EventListener<SessionEvent>() {

				@Override
				public void eventHappened(SessionEvent event) {
					if(event.isEnd()){
						markReaderApp.shutdown();
						//System.exit(0);
					}
				}
			});
			session.startSession();
		}
	}
}
