/*

 EngineLauncher.java

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

import net.sqs2.net.ClassURLStreamHandlerFactory;
import net.sqs2.omr.session.Session;
import net.sqs2.omr.session.event.MarkReaderSessionMonitorAdapter;
import net.sqs2.omr.swing.MarkReaderPanelController;

public class MarkReaderCommandLineLauncher {

	public static void main(String[] args)throws Exception{
		if(args.length < 1){
			System.err.println("Usage: MarkReaderAppLauncher <sourceDirectoryRoot>");
			return;
		}
		try{
			URL.setURLStreamHandlerFactory(new ClassURLStreamHandlerFactory());
		}catch(Error ignore){}

		File sourceDirectoryRoot = new File(args[0]);
		final MarkReaderController markReaderController = MarkReaderControllerImpl.getInstance(1099);
	
		Session session = markReaderController.createOrReuseSession(sourceDirectoryRoot);
		session.addSessionMonitor(new MarkReaderSessionMonitorAdapter(){
			@Override
			public void notifySessionFinished(File sourceDirectoryRootFile) {
				markReaderController.userShutdown();
            }
		});
		MarkReaderPanelController markReaderPanelController = new MarkReaderPanelController(markReaderController, null);
		markReaderPanelController.userOpen(sourceDirectoryRoot);		
	}


}
