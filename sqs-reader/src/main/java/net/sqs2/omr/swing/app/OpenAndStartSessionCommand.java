/*

 OpenAndStartSessionAction.java

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

 */
package net.sqs2.omr.swing.app;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;

import javax.swing.SwingUtilities;

import net.sqs2.event.EventListener;
import net.sqs2.omr.app.MarkReaderApp;
import net.sqs2.omr.base.MarkReaderJarURIContext;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.MarkReaderConfiguration;
import net.sqs2.omr.model.SessionSourceState;
import net.sqs2.omr.session.event.SessionEvent;
import net.sqs2.omr.session.model.PageTaskExceptionTableModel;
import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.omr.session.service.MarkReaderSessions;
import net.sqs2.omr.session.service.OutputEventRecieversFactory;
import net.sqs2.omr.session.service.PageTaskExecutionProgressModel;
import net.sqs2.omr.sound.SessionFanfare;

public class OpenAndStartSessionCommand implements Runnable{
	
	private final MarkReaderPanelController markReaderPanelController;
	private MarkReaderApp markReaderApp;
	private File sourceDirectoryRootFile;
	private MarkReaderPanelImpl markReaderPanel;

	public OpenAndStartSessionCommand(MarkReaderPanelController markReaderPanelController, MarkReaderApp markReaderApp, File sourceDirectoryRootFile,
			MarkReaderPanelImpl markReaderPanel){
		this.markReaderPanelController = markReaderPanelController;
		this.markReaderApp = markReaderApp;
		this.sourceDirectoryRootFile = sourceDirectoryRootFile;
		this.markReaderPanel = markReaderPanel;
	}

	private String createTabName(final File sourceDirectoryRootFile, int numSameNameSessions) {
		String name = null;
		if (1 == numSameNameSessions) {
			name = sourceDirectoryRootFile.getName();
		} else if (1 < numSameNameSessions) {
			name = sourceDirectoryRootFile.getName() + "(" + numSameNameSessions + ")";
		}
		return name;
	}

	public synchronized void actionPerformed(ActionEvent ev){
		openAndStartSession();
	}
	
	public void run(){
		openAndStartSession();
	}

	private void openAndStartSession() {
		try {
			MarkReaderSession session = MarkReaderSessions.get(sourceDirectoryRootFile);
			if (session != null) {
				startPredefinedSession(session);
				return;
			}

			final MarkReaderSession newSession = (MarkReaderSession) this.markReaderApp.createSession(sourceDirectoryRootFile,
					new OutputEventRecieversFactory(OutputEventRecieversFactory.Mode.GUI));
			
			startFanfare();
			
			final PageTaskExecutionProgressModel markReaderSessionProgressModel = newSession.getPageTaskExecutionProgressModel();
			final MarkReaderSessionPanel markReaderSessionPanel = markReaderPanelController.createSessionPanel(sourceDirectoryRootFile,
					markReaderSessionProgressModel);

			setUpRunningGUIState(markReaderSessionPanel);
			
			final MarkReaderSessionPanelController markReaderSessionPanelController = createMarkReaderSessionPanelController(markReaderApp, newSession,
					markReaderPanel, markReaderPanelController, markReaderSessionPanel);

			setUpGUIStateChangedEventHandler(newSession,
					markReaderSessionPanel, markReaderSessionPanelController);

			newSession.startSession();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void setUpGUIStateChangedEventHandler(
			final MarkReaderSession newSession,
			final MarkReaderSessionPanel markReaderSessionPanel,
			final MarkReaderSessionPanelController markReaderSessionPanelController) {
		newSession.getSessionEventSource().addListener(new EventListener<SessionEvent>(){

			@Override
			public void eventHappened(SessionEvent event) {
				// TODO Auto-generated method stub
				
			}
			/*
			@Override
			public void notifyExportingResultStarted(File sourceDirectoryRootFile) {
				
				ResultVisitor resultVisitor = newSession.getSessionResultVisitor();
				MarkAreaErrorModel markAreaErrorModel = new MarkAreaErrorModel();
				resultVisitor.addEventConsumer(markAreaErrorModel);
				resultVisitor.addEventConsumer(new MarkReaderSessionResultVisitorController(markReaderSessionPanel.getSessionResultWalkerPanel(), markAreaErrorModel));
				markReaderSessionPanelController.setMarkAreaErrorModel(markAreaErrorModel);
				newSession.setMarkAreaErrorModel(markAreaErrorModel);
				
				SQSHttpdManager.initHttpds();
			}
			
			private void logSessionElapsedTime(PageTaskExecutionProgressModel model) {
				long elapsedTimePageTaskExecution = model.getTimePageTaskFinished() - model.getTimePageTaskProduced();
				int totalPages = model.getNumTargetPages();
				int reusedPages = model.getNumReusedPages();
				int targetPages = totalPages - reusedPages;
				float pageParSec = ( targetPages * 1000.0f / elapsedTimePageTaskExecution);
				
				if(0 < totalPages){
					Logger.getLogger(getClass().getName()).info("**** Number of Processed PageTasks: "+targetPages+"="+totalPages+"-"+reusedPages);
				}
				Logger.getLogger(getClass().getName()).info("**** PageTask Execution Finished in: "+( elapsedTimePageTaskExecution / 1000.0)+" sec");
				Logger.getLogger(getClass().getName()).info("**** Process Rate: "+ pageParSec+" imgs/sec");
				Logger.getLogger(getClass().getName()).info("**** TimeElapsed: "+ model.getTimeElapsedString());
			}

			@Override
			public void notifySessionFinished(PageTaskExecutionProgressModel model) {
				logSessionElapsedTime(model);
			}
			 */
		});
	}

	private void setUpRunningGUIState(
			final MarkReaderSessionPanel markReaderSessionPanel) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				int numSameNameSessions = MarkReaderSessions.countSessionsBySourceDirectory(sourceDirectoryRootFile);
				String name = createTabName(sourceDirectoryRootFile, numSameNameSessions);
				markReaderSessionPanel.setPlayStateGUI();
				markReaderPanel.setRunningTabIcon(sourceDirectoryRootFile);
				markReaderPanel.addMarkReaderSessionPanel(markReaderSessionPanel, name);
			}
		});
	}

	private void startFanfare() throws MalformedURLException {
		SessionFanfare fanfare = new SessionFanfare(new URL(MarkReaderJarURIContext.getSoundBaseURI()
				+ AppConstants.SESSION_START_FANFARE_SOUND_FILENAME));
		fanfare.startFanfare();
	}

	private void startPredefinedSession(MarkReaderSession session)
			throws IOException {
		SessionSourceState state = session.getSessionSourceState();
		if (state.equals(SessionSourceState.NOT_INITIALIZED) ) {
			session.startSession();
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				markReaderPanel.setForgroundSessionPanel(sourceDirectoryRootFile);
			}
		});
		return;
	}
	
	private MarkReaderSessionPanelController createMarkReaderSessionPanelController(
			MarkReaderApp markReaderApp,
			MarkReaderSession session,
			MarkReaderPanel markReaderPanel,
			MarkReaderPanelController markReaderPanelController,
			MarkReaderSessionPanel markReaderSessionPanel
			){
		return new MarkReaderSessionPanelController(markReaderApp, session, markReaderPanel,
				markReaderPanelController, markReaderSessionPanel,
				new PageTaskExceptionTableModel());
	}

	public void storeSourceDirectoryRootInPrefs(final File sourceDirectoryRoot) {
		MarkReaderConfiguration.getSingleton().put(AppConstants.SOURCE_DIRECTORY_ROOT_KEY_IN_PREFERENCES,
				sourceDirectoryRoot.getAbsolutePath());
		try {
			MarkReaderConfiguration.getSingleton().flush();
		} catch (IllegalArgumentException ignore) {
			Logger.getLogger(OpenAndStartSessionCommand.class.getName()).info(ignore.getMessage());
		} catch (BackingStoreException ignore) {
			Logger.getLogger(OpenAndStartSessionCommand.class.getName()).severe(ignore.getMessage());
		}
	}

}