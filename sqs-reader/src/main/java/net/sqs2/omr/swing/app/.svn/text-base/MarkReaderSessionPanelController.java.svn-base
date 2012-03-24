package net.sqs2.omr.swing.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import net.sqs2.event.EventListener;
import net.sqs2.omr.app.MarkReaderApp;
import net.sqs2.omr.base.Messages;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTaskException;
import net.sqs2.omr.model.PageTaskResult;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.SpreadSheet;
import net.sqs2.omr.result.export.model.MarkAreaErrorModel;
import net.sqs2.omr.session.event.SessionEvent;
import net.sqs2.omr.session.init.SessionSourceInitializationEvent;
import net.sqs2.omr.session.model.PageTaskExceptionEntry;
import net.sqs2.omr.session.model.PageTaskExceptionTableModel;
import net.sqs2.omr.session.scan.PageTaskProducedEvent;
import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.omr.session.service.PageTaskCommittedEvent;
import net.sqs2.omr.session.service.PageTaskExecutionProgressModel;
import net.sqs2.omr.swing.Images;
import net.sqs2.omr.swing.preview.MarkAreasTablePanel;
import net.sqs2.omr.swing.preview.PageTaskErrorPanel;
import net.sqs2.omr.swing.result.MarkReaderSessionResultBrowserController;

public class MarkReaderSessionPanelController {
	private MarkReaderApp markReaderApp;
	private final MarkReaderSession markReaderSession;

	private MarkReaderPanel markReaderPanel;
	private MarkReaderSessionPanel markReaderSessionPanel;
	private PageTaskExceptionTableModel pageTaskErrorTableModel;

	private MarkReaderPanelController markReaderPanelController;
	
	private RestartPromptDialog pageTaskErrorDialog = null;

	private MarkAreasTablePanel markAreasTablePanel = null;
	private RestartPromptDialog markAreasErrorDialog = null;
	private MarkAreaErrorModel markAreaErrorModel = null;


	private int rowSerialID = 0;

	public void setMarkAreaErrorModel(MarkAreaErrorModel markAreaErrorModel){
		this.markAreaErrorModel = markAreaErrorModel;
	}
	
	public MarkReaderSessionPanelController(
			final MarkReaderApp markReaderApp,
			final MarkReaderSession markReaderSession,
			
			final MarkReaderPanel markReaderPanel,
			final MarkReaderPanelController markReaderPanelController,
			final MarkReaderSessionPanel markReaderSessionPanel,
			final PageTaskExceptionTableModel pageTaskErrorTableModel) {
		this.markReaderApp = markReaderApp;
		this.markReaderSession = markReaderSession;
		
		this.markReaderPanel = markReaderPanel;
		this.markReaderPanelController = markReaderPanelController;
		this.markReaderSessionPanel = markReaderSessionPanel;
		this.pageTaskErrorTableModel = pageTaskErrorTableModel;
		
		final MarkReaderSessionResultBrowserController sessionResultController = 
			new MarkReaderSessionResultBrowserController(markReaderSession, markReaderSessionPanel.getResultBrowserPanel());
		
		markReaderSession.getPageTaskProducedEventSource().addListener(new EventListener<PageTaskProducedEvent>(){
			@Override
			public void eventHappened(PageTaskProducedEvent e) {
				OMRPageTask pageTask = e.getPageTask();
				if(! e.hasSuccess()){
					PageTaskException error = pageTask.getPageTaskException();
					if (error != null) {
						processPageTaskError(pageTask);
					}
				}
			}
		}
		);	

		markReaderSession.getPageTaskCommitedEventSource().addListener(new EventListener<PageTaskCommittedEvent>(){
			@Override
			public void eventHappened(PageTaskCommittedEvent e) {
				OMRPageTask pageTask = e.getPageTask();
				PageTaskException error = pageTask.getPageTaskException();
				if (error == null) {
					markReaderSession.getTaskHolder().incrementNumExternalizingTasks();
				} else {
					processPageTaskError(pageTask);
				}
			}
		}
		);	

		markReaderSession.getSessionSourceInitializationEventSource().addListener(new EventListener<SessionSourceInitializationEvent>() {

			@Override
			public void eventHappened(SessionSourceInitializationEvent event) {
				//FIXME:
				
			}
			/*
			@Override
			public void unreadableSourceDirectoryErrorThrown(SourceDirectory sourceDirectory) {
				stopSession(markReaderSession.getSourceDirectoryRootFile());
				markReaderSessionPanel.setPauseStateGUI();
				JOptionPane.showMessageDialog(markReaderSessionPanel, Messages.SESSION_ERROR_UNREADABLE, "ERROR",
						JOptionPane.ERROR_MESSAGE);
			}

			@Override
			public void numOfImagesErrorThrown(SpreadSheet spreadSheet, int numImages) {
				processNumErrorOfImages(spreadSheet, numImages);
			}

			@Override
			public void invalidFormMasterErrorThrown(FormMasterException ex) {
				ex.printStackTrace();
				stopSession(markReaderSession.getSourceDirectoryRootFile());
				markReaderSessionPanel.setPauseStateGUI();
				JOptionPane.showMessageDialog(markReaderSessionPanel, Messages.SESSION_ERROR_NOPDFSOURCEFOLDER+":\n"
						+ ex.getFile().getAbsolutePath(), "ERROR",
						JOptionPane.ERROR_MESSAGE);
			}*/
		});
		
		markReaderSession.getSessionEventSource().addListener(new EventListener<SessionEvent>() {

			@Override
			public void eventHappened(SessionEvent event) {
				// TODO Auto-generated method stub
				
			}

			
			/*
			@Override
			public void notifySessionStarted(File sourceDirectoryRootFile) {
				MarkReaderSessionPanelController.this.rowSerialID = 0;
				markReaderSessionPanel.reset();
				markReaderSession.getTaskHolder().clear();
				pageTaskErrorTableModel.getDataVector().clear();
				markReaderSession.getPageTaskExecutionProgressModel().getErrorPathToTaskErrorEntryMap().clear();
			}			

			@Override
			public synchronized void notifyExportingResultStarted(File sourceDirectoryRootFile) {
				//final MarkReaderSession session = (MarkReaderSession) MarkReaderSessions.get(sourceDirectoryRootFile);
				if (0 == markReaderSession.getPageTaskExecutionProgressModel().getNumErrorPages()) {
					sessionResultController.init();
					markReaderSessionPanel.selectExportTab();
				}
			}
			
			@Override
			public synchronized void notifyExportResultDirectoryFinished(File sourceDirectoryRootFile) {
				//openMarkAreasErrorDialog();
				new FinishSessionAppTask().call();
			}

			@Override
			public void notifySessionFinished(PageTaskExecutionProgressModel model) {
				markReaderSessionPanel.setPauseStateGUI();
				File sourceDirectoryRoot = model.getSourceDirectoryRoot();
				markReaderPanel.setFinishedTabIcon(sourceDirectoryRoot);
				if (0 == markReaderSessionPanel.getSessionProgressModel().getNumErrorPages()) {
					markReaderPanel.promptSessionFinished(sourceDirectoryRoot);
					markReaderSessionPanel.selectResultTab();
				}
				
				if (MarkReaderConfiguration.isEnabled(MarkReaderConfiguration.KEY_RESULTBROWSER)) {
					new OpenResultBrowserAppTask(markReaderSession.getSessionID()).call();
				}
			}

			@Override
			public void notifySessionStopped(File sourceDirectoryRootFile) {
				markReaderSessionPanel.setPauseStateGUI();
			}

			@Override
			public void notifySessionResult(File sourceDirectoryRootFile) {
				// TODO Auto-generated method stub
				
			}*/
			
		});

		markReaderSessionPanel.setConfigButtonActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				markReaderPanelController
						.showDensityConfigurationDialog(markReaderSession.getSessionSource());
			}
		});

		markReaderSessionPanel.setCloseButtonActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				closeSession();
			}
		});

		markReaderSessionPanel.setRestartButtonActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent ev) {
				restartSession();
			}

		});

		markReaderSessionPanel.setPauseButtonActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				File sourceDirectoryRootFile = markReaderSession.getSourceDirectoryRootFile();
				stopSession(sourceDirectoryRootFile);
			}
		});

		markReaderSessionPanel.getResultBrowserPanel().getExcelButton().addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						sessionResultController.showExcel();
					}
				});

		markReaderSessionPanel.getResultBrowserPanel().getResultBrowserButton().addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						sessionResultController.openResultBrowser();
					}
				});

		markReaderSessionPanel.getResultBrowserPanel().getCsvButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				sessionResultController.showCSV();
			}
		});
		markReaderSessionPanel.getResultBrowserPanel().getTextareaButton().addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						sessionResultController.showTextarea();
					}
				});

		markReaderSessionPanel.getResultBrowserPanel().getChartButton().addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						sessionResultController.showChart();
					}
				});
	}
	
	private void stopSession(File sourceDirectoryRootFile) {
		this.markReaderSessionPanel.setPauseStateGUI();
		this.markReaderPanelController.stopSession(sourceDirectoryRootFile);
	}

	private void closeSession() {
		File sourceDirectoryRoot = this.markReaderSession.getSourceDirectoryRootFile();
		this.markReaderPanelController.closeSession(sourceDirectoryRoot);
		//this.markReaderSession.closeSession();
	}

	void openMarkAreasErrorDialog() {
		if (this.markAreasErrorDialog != null) {
			this.markAreasErrorDialog.setVisible(true);
			this.markAreasTablePanel.updateTabTitle();
			return;
		}

		if (this.markAreaErrorModel.getNoAnswerMarkAreasTableModel().getRowCount() == 0
				&& this.markAreaErrorModel.getMultipleAnswersMarkAreasTableModel().getRowCount() == 0) {
			return;
		}

		this.markAreasTablePanel = 
			new MarkAreasTablePanel(this.markAreaErrorModel.getNoAnswerMarkAreasTableModel(),
				this.markAreaErrorModel.getMultipleAnswersMarkAreasTableModel());
		this.markAreasErrorDialog = new RestartPromptDialog(this.markReaderPanel.getFrame(),
				Messages.MARKAREA_ERROR_DIALOG_TITLE + ":"
						+ this.markReaderSession.getSourceDirectoryRootFile().getAbsolutePath(),
				this.markAreasTablePanel, Messages.PAGETASK_ERROR_RESTART_ON_ALL_LABEL,
				Messages.PAGETASK_ERROR_RESTART_ON_UPDATED_LABEL, Messages.PAGETASK_ERROR_CONTINUE_LABEL);

		this.markAreasErrorDialog.getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				closeMarkAreaErrorsDialog();
			}
		});

		this.markAreasErrorDialog.getRestartOnAllFilesButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				restartSessionByDialog(true);
			}
		});

		this.markAreasErrorDialog.getRestartOnUpdatedFilesButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				restartSessionByDialog(false);
			}
		});

		this.markAreasTablePanel.updateTabTitle();
		this.markAreasErrorDialog.setVisible(true);
	}

	private void restartSession() {
		File sourceDirectoryRootFile = this.markReaderSession.getSourceDirectoryRootFile();
		stopSession(sourceDirectoryRootFile);
		
		boolean isProgressTabSelected = this.markReaderSessionPanel.isProgressTabSelected();
		if (isProgressTabSelected) {
			try {
				markReaderApp.clearSession(sourceDirectoryRootFile);
			} catch (IOException ignore) {
				ignore.printStackTrace();
			}
		}

		this.markReaderSessionPanel.reset();
		this.markReaderSessionPanel.setPlayStateGUI();
		try{
			this.markReaderSession.startSession();
 		}catch(IOException ignore){
			ignore.printStackTrace();
		}
	}

	private void restartSessionByDialog(boolean isClearMode) {
		File sourceDirectoryRootFile = this.markReaderSession.getSourceDirectoryRootFile();
		stopSession(sourceDirectoryRootFile);
		
		if(isClearMode){
			try{
				markReaderApp.clearSession(sourceDirectoryRootFile);
			}catch(IOException ignore){}
		}
		this.markReaderSessionPanel.setPlayStateGUI();
		try{
			this.markReaderSession.startSession();
		}catch(IOException ex){
		}
		closeMarkAreaErrorsDialog();
		closePageTaskErrorDialog();
	}

	private void closeMarkAreaErrorsDialog() {
		if(this.markAreasErrorDialog != null){
			this.markAreasErrorDialog.dispose();
			this.markAreasErrorDialog = null;
		}
	}

	private void closePageTaskErrorDialog() {
		if(this.pageTaskErrorDialog != null){
			this.pageTaskErrorDialog.dispose();
			this.pageTaskErrorDialog = null;
		}
	}
	
	/*
	private boolean isControlKeyDown(final ActionEvent ev) {
		return (ev.getModifiers() & ActionEvent.CTRL_MASK) != 0;
	}
	 */

	private void processPageTaskError(OMRPageTask pageTask) {
		addPageTaskError(pageTask);
		openPageTaskErrorDialog(pageTaskErrorTableModel);
	}

	private void openPageTaskErrorDialog(final PageTaskExceptionTableModel tableModel) {

		if (MarkReaderSessionPanelController.this.pageTaskErrorDialog != null) {
			MarkReaderSessionPanelController.this.pageTaskErrorDialog.setVisible(true);
			return;
		}

		MarkReaderSessionPanelController.this.pageTaskErrorDialog = new RestartPromptDialog(
				markReaderPanel.getFrame(), Messages.PAGETASK_ERROR_DIALOG_TITLE + ":"
						+ markReaderSession.getSourceDirectoryRootFile().getAbsolutePath(),
				new PageTaskErrorPanel(tableModel), Messages.PAGETASK_ERROR_RESTART_ON_ALL_LABEL,
				Messages.PAGETASK_ERROR_RESTART_ON_UPDATED_LABEL,
				Messages.PAGETASK_ERROR_CANCEL_LABEL);
		MarkReaderSessionPanelController.this.pageTaskErrorDialog.getCancelButton().setIcon(
				Images.CANCEL_ICON);
		MarkReaderSessionPanelController.this.pageTaskErrorDialog.getCancelButton().addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						closePageTaskErrorDialog();
					}
				});

		MarkReaderSessionPanelController.this.pageTaskErrorDialog.getRestartOnAllFilesButton()
				.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						restartSessionByDialog(true);
					}
				});

		MarkReaderSessionPanelController.this.pageTaskErrorDialog.getRestartOnUpdatedFilesButton()
				.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ev) {
						restartSessionByDialog(false);
					}
				});

		MarkReaderSessionPanelController.this.pageTaskErrorDialog.setVisible(true);
	}

	private void addPageTaskError(OMRPageTask pageTask) {
		File root = markReaderSession.getSourceDirectoryRootFile();
		PageID pageID = pageTask.getPageID();
		String path = pageID.getFileResourceID().getRelativePath();

		PageTaskExecutionProgressModel sessionProgressModel = markReaderSession.getPageTaskExecutionProgressModel();
		if (!sessionProgressModel.getErrorPathToTaskErrorEntryMap().containsKey(path)) {
			sessionProgressModel.getErrorPathToTaskErrorEntryMap().put(
					path,
					new PageTaskExceptionEntry(MarkReaderSessionPanelController.this.rowSerialID, pageTask.getPageTaskException()));
			int pageNumber = pageTask.getProcessingPageNumber();
			pageTaskErrorTableModel.addRow(MarkReaderSessionPanelController.this.rowSerialID + 1,
					pageNumber, root, path, pageID.getIndexInFile(), pageTask.getPageTaskException());
			pageTaskErrorTableModel.fireTableRowsInserted(pageTaskErrorTableModel.getRowCount() - 1,
					pageTaskErrorTableModel.getRowCount() - 1);
			pageTaskErrorTableModel.sort(0, true);
			MarkReaderSessionPanelController.this.rowSerialID++;
		}
	}

	private void processNumErrorOfImages(final SpreadSheet spreadSheet, final int numImages) {
		markReaderSessionPanel.setPauseStateGUI();
		SourceDirectory sourceDirectory = spreadSheet.getSourceDirectory();
		FormMaster master = spreadSheet.getFormMaster();
		int numPages = master.getNumPages();
		JOptionPane.showMessageDialog(markReaderSessionPanel, Messages.SESSION_ERROR_INVALID_NUM_OF_IMAGES+"\n "
				+ sourceDirectory.getDirectory().getAbsolutePath() + "\n "+
				Messages.SESSION_ERROR_NUM_OF_IMAGES_LABEL+": " + numImages
				+ "\n "+Messages.SESSION_ERROR_NUM_OF_PAGES_LABEL+": " + numPages, "ERROR", JOptionPane.ERROR_MESSAGE);
		stopSession(sourceDirectory.getRoot());
	}


}
