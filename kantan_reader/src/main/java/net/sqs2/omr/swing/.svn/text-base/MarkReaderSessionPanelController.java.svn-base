package net.sqs2.omr.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.sqs2.omr.app.MarkReaderController;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.master.PageMasterException;
import net.sqs2.omr.session.MarkReaderSession;
import net.sqs2.omr.session.MarkReaderSessions;
import net.sqs2.omr.session.event.MarkReaderSessionMonitorAdapter;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.task.PageTask;
import net.sqs2.omr.task.TaskError;
import net.sqs2.omr.task.TaskException;

public class MarkReaderSessionPanelController {
	MarkReaderPanel markReaderPanel;
	final MarkReaderSession markReaderSession;
	MarkReaderSessionPanel markReaderSessionPanel;
	MarkReaderPanelController markReaderPanelController;
	
	RestartPromptDialog pageTaskDialog = null;
	PageTaskErrorTableModel pageTaskErrorTableModel = null;
	
	RestartPromptDialog markAreasErrorDialog = null;
	MarkAreasTableModel noAnswerMarkAreasTableModel = null;
	MarkAreasTableModel multipleAnswersMarkAreasTableModel = null;
	
	private int rowSerialID = 0;
	
	public MarkReaderSessionPanelController(final MarkReaderPanel markReaderPanel,
			final MarkReaderSession markReaderSession, 
			final MarkReaderSessionPanel markReaderSessionPanel, 
			final MarkReaderController markReaderController,
			final MarkReaderPanelController markReaderPanelController,
			final PageTaskErrorTableModel pageTaskErrorTableModel,
			final MarkAreasTableModel noAnswerMarkAreasTableModel,
			final MarkAreasTableModel multipleAnswerMarkAreasTableModel){
		this.markReaderPanel = markReaderPanel;
		this.markReaderSession = markReaderSession;
		this.markReaderSessionPanel = markReaderSessionPanel;
		this.markReaderPanelController = markReaderPanelController;
		this.pageTaskErrorTableModel = pageTaskErrorTableModel;
		this.noAnswerMarkAreasTableModel = noAnswerMarkAreasTableModel;
		this.multipleAnswersMarkAreasTableModel = multipleAnswerMarkAreasTableModel;
		
		markReaderSession.addSessionMonitor(new MarkReaderSessionMonitorAdapter(){
			
			@Override
			public void notifySessionStarted(File sourceDirectoryRootFile) {
				rowSerialID = 0;
				markReaderSessionPanel.reset();
				markReaderSession.getTaskHolder().reset();
				pageTaskErrorTableModel.getDataVector().clear();
				markReaderSession.getSessionProgressModel().getErrorPathToTaskErrorEntryMap().clear();
			}
			
			@Override
			public void notifyErrorDirectoryUnreadable(SourceDirectory sourceDirectory){
				userStop(markReaderSession.getSourceDirectoryRootFile());
				markReaderSessionPanel.setPauseStateGUI();
				JOptionPane.showMessageDialog(markReaderSessionPanel, "指定されたフォルダを読むことができません", "ERROR", JOptionPane.ERROR_MESSAGE);
			}

			@Override
			public void notifyErrorNumOfImages(SourceDirectory sourceDirectory, int numImages){
				processNumErrorOfImages(sourceDirectory, numImages);
			}
			
			@Override
			public void notifyErrorOnPageMaster(PageMasterException ex) {
				ex.printStackTrace();
				userStop(markReaderSession.getSourceDirectoryRootFile());
				markReaderSessionPanel.setPauseStateGUI();
				if(ex.getException() == null){
					JOptionPane.showMessageDialog(markReaderSessionPanel, "調査票ファイル(PDF)が読み込めません:\n"+ex.getPath(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(markReaderSessionPanel, "調査票ファイル(PDF)が読み込めません:\n"+
							ex.getPath()+"\n"+
							ex.getException().getLocalizedMessage(),
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void notifyStoreTask(PageTask pageTask) {
				if(pageTask.getTaskError() == null){
					markReaderSession.getTaskHolder().incrementNumExternalizingTasks();
				}else{
					processPageTaskError(pageTask);
				}
			}
			
			@Override
			public void notifyErrorTaskReproduced(PageTask storedPageTask){
				TaskError error = storedPageTask.getTaskError();
				if(error != null){
					processPageTaskError(storedPageTask);
				}				
			}

			private void processNumErrorOfImages(final SourceDirectory sourceDirectory, final int numImages){
				markReaderSessionPanel.setPauseStateGUI();
				PageMaster master = sourceDirectory.getPageMaster();
				int numPages = master.getNumPages();
				JOptionPane.showMessageDialog(markReaderSessionPanel, 
						"「画像ファイルの数」が「調査票1人分のページ数」で割り切れません\n "+
						sourceDirectory.getDirectory().getAbsolutePath() +
						"\n 画像ファイルの数: " + numImages +
						"\n 調査票1人分のページ数: " + numPages,
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
				userStop(sourceDirectory.getRoot());
			}

			private void processPageTaskError(PageTask pageTask){
				addPageTaskError(pageTask);
				openPageTaskErrorDialog(pageTaskErrorTableModel);				
			}

			private void openPageTaskErrorDialog(final PageTaskErrorTableModel tableModel) {
				
				if(pageTaskDialog != null){
					pageTaskDialog.setVisible(true);
					return;
				}

				pageTaskDialog = new RestartPromptDialog(markReaderPanel.getFrame(),
						Messages.PAGE_TASK_ERROR_DIALOG_TITLE+":"+markReaderSession.getSourceDirectoryRootFile().getAbsolutePath(),
						new PageTaskErrorPanel(tableModel),
						Messages.PAGE_TASK_ERROR_CANCEL_LABEL,
						Messages.PAGE_TASK_ERROR_RESTART_ON_ALL_LABEL,
						Messages.PAGE_TASK_ERROR_RESTART_ON_UPDATED_LABEL);
				
				pageTaskDialog.getCancelButton().addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ev){
						pageTaskDialog.closeDialog();
					}
				});
				
				pageTaskDialog.getRestartOnAllFilesButton().addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ev){
						File file = markReaderSession.getSourceDirectoryRootFile();
						userStop(file);
						try{
							markReaderPanelController.userClear(file);
							markReaderSessionPanel.setPlayStateGUI();
							markReaderPanelController.userOpen(file);
						}catch(Exception ex){
							ex.printStackTrace();
						}
						pageTaskDialog.closeDialog();
					}
				});
					
				pageTaskDialog.getRestartOnUpdatedFilesButton().addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ev){
						File file = markReaderSession.getSourceDirectoryRootFile();
						userStop(file);
						try{
							markReaderSessionPanel.setPlayStateGUI();
							markReaderPanelController.userOpen(file);
						}catch(Exception ex){
							ex.printStackTrace();
						}
						pageTaskDialog.closeDialog();
					}
				});
				
				pageTaskDialog.setVisible(true);
			}

			private void addPageTaskError(PageTask pageTask){
				File root = markReaderSession.getSessionProgressModel().getSourceDirectoryRoot();
				PageID pageID = pageTask.getPageID();
				String path = pageID.getFileResourceID().getRelativePath();

				SessionProgressModel sessionProgressModel = markReaderSession.getSessionProgressModel();
				if(! sessionProgressModel.getErrorPathToTaskErrorEntryMap().containsKey(path)){
					sessionProgressModel.getErrorPathToTaskErrorEntryMap().put(path, new PageTaskErrorEntry(rowSerialID, pageTask.getTaskError()));
					int pageNumber = pageTask.getPageNumber();
					pageTaskErrorTableModel.addRow(rowSerialID+1, pageNumber, root, path, pageTask.getTaskError());
					pageTaskErrorTableModel.fireTableRowsInserted(pageTaskErrorTableModel.getRowCount() - 1, pageTaskErrorTableModel.getRowCount() - 1);
					pageTaskErrorTableModel.sort(0, true);
					rowSerialID++;
				}
			}
			
			@Override
			public void notifySessionExport(File sourceDirectoryRootFile) {
				final MarkReaderSession session = (MarkReaderSession)MarkReaderSessions.get(sourceDirectoryRootFile);
				if(0 == markReaderSession.getSessionProgressModel().getNumErrorPages()){
					markReaderSessionPanel.bindSpreadSheetExportEventProducer(session.getSpreadSheetExportEventProducer(),
							noAnswerMarkAreasTableModel,
							multipleAnswersMarkAreasTableModel);
					markReaderSessionPanel.selectExportTab();
				}
			}
			
			@Override
			public void notifySessionFinished(File sourceDirectoryRootFile) {
				openMarkAreasErrorDialog();					
				markReaderSessionPanel.setPauseStateGUI();				
			}
		});
		
		markReaderSessionPanel.setConfigButtonActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				markReaderPanelController.showDensityConfigurationDialog(markReaderSession.getSessionSource());
			}
		});
		
		markReaderSessionPanel.setCloseButtonActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				userClose();
			}
		});

		markReaderSessionPanel.setRestartButtonActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent ev){
				File sourceDirectoryRootFile = markReaderSession.getSourceDirectoryRootFile();
				userStop(sourceDirectoryRootFile);
				userRestart(isControlKeyDown(ev));
			}

		});
		
		markReaderSessionPanel.setPauseButtonActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				File sourceDirectoryRootFile = markReaderSession.getSourceDirectoryRootFile();
				userStop(sourceDirectoryRootFile);
			}
		});

		final SessionResultController sessionResultController = new SessionResultController(markReaderSession.getSourceDirectoryRootFile(), markReaderSessionPanel.getSessionResultPanel());
		

		markReaderSessionPanel.getSessionResultPanel().getExcelButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				sessionResultController.showExcel();
			}
		});

		markReaderSessionPanel.getSessionResultPanel().getCsvButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				sessionResultController.showCSV();
			}
		});
		markReaderSessionPanel.getSessionResultPanel().getTextareaButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				sessionResultController.showTextarea();
			}
		});							
		
		markReaderSessionPanel.getSessionResultPanel().getChartButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				sessionResultController.showChart();
			}
		});							

	}
	
	public void userClose(){
		File sourceDirectoryRoot = markReaderSession.getSourceDirectoryRootFile();
		markReaderPanelController.userClose(sourceDirectoryRoot);
	}
	
	public void userRestart(boolean reset){
		
		boolean isProgressTabSelected = markReaderSessionPanel.isProgressTabSelected();
		
		markReaderSessionPanel.reset();
		markReaderSessionPanel.setPlayStateGUI();		
		try{
			File sourceDirectoryRootFile = markReaderSession.getSourceDirectoryRootFile();
			if(reset && isProgressTabSelected){
				markReaderPanelController.userClear(sourceDirectoryRootFile);
			}
			markReaderPanelController.userOpen(sourceDirectoryRootFile);
		}catch(TaskException ignore){
			ignore.printStackTrace();
		}catch(IOException ignore){
			ignore.printStackTrace();
		}
		
		markReaderSessionPanel.reset();
	}
	
	public void userStop(File sourceDirectoryRootFile){
		markReaderSessionPanel.setPauseStateGUI();
		markReaderPanelController.userStop(sourceDirectoryRootFile);
	}

	private boolean isControlKeyDown(final ActionEvent ev) {
		return (ev.getModifiers() & ActionEvent.CTRL_MASK) != 0;
	}

	
	void openMarkAreasErrorDialog(){
		
		if(markAreasErrorDialog != null){
			markAreasErrorDialog.setVisible(true);
			return;
		}

		if( noAnswerMarkAreasTableModel.getRowCount() == 0 &&
				multipleAnswersMarkAreasTableModel.getRowCount() == 0){
			return;
		}
		
		MarkAreasTablePanel markAreasTablePanel = new MarkAreasTablePanel(noAnswerMarkAreasTableModel, multipleAnswersMarkAreasTableModel);
		markAreasErrorDialog = new RestartPromptDialog(markReaderPanel.getFrame(),
				Messages.MARK_AREAS_ERROR_DIALOG_TITLE+":"+markReaderSession.getSourceDirectoryRootFile().getAbsolutePath(),
				markAreasTablePanel,
				Messages.PAGE_TASK_ERROR_CANCEL_LABEL,
				Messages.PAGE_TASK_ERROR_RESTART_ON_ALL_LABEL,
				Messages.PAGE_TASK_ERROR_RESTART_ON_UPDATED_LABEL);
		
		markAreasErrorDialog.getCancelButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				markAreasErrorDialog.closeDialog();
			}
		});
		
		markAreasErrorDialog.getRestartOnAllFilesButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				File file = markReaderSession.getSourceDirectoryRootFile();
				userStop(file);
				try{
					markReaderPanelController.userClear(file);
					markReaderSessionPanel.setPlayStateGUI();
					markReaderPanelController.userOpen(file);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				markAreasErrorDialog.closeDialog();
			}
		});
			
		markAreasErrorDialog.getRestartOnUpdatedFilesButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				File file = markReaderSession.getSourceDirectoryRootFile();
				userStop(file);
				try{
					markReaderSessionPanel.setPlayStateGUI();
					markReaderPanelController.userOpen(file);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				markAreasErrorDialog.closeDialog();
			}
		});
		
		markAreasErrorDialog.setVisible(true);
		
	}
}
