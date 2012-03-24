package net.sqs2.omr.swing;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

import net.sqs2.omr.app.MarkReaderController;
import net.sqs2.omr.session.MarkReaderSession;
import net.sqs2.omr.session.SessionException;
import net.sqs2.omr.session.event.MarkReaderSessionMonitorAdapter;
import net.sqs2.omr.source.GenericSourceConfig;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SourceDirectoryConfiguration;
import net.sqs2.omr.swing.MarkReaderPanelImpl.NoPDFSourceFolderWarningPrompter;
import net.sqs2.omr.task.TaskException;
import net.sqs2.swing.DirectoryChooserEventListener;
import net.sqs2.swing.FileChooserEvent;
import net.sqs2.swing.FileDropTargetDecorator;

public class MarkReaderPanelController {
	
	MarkReaderController markReaderController;
	MarkReaderPanelImpl markReaderPanel = null;

	public MarkReaderPanelController(final MarkReaderController markReaderController, final MarkReaderPanelImpl markReaderPanel){
		this.markReaderController = markReaderController;
		if(markReaderPanel == null){
			return;
		}
		this.markReaderPanel = markReaderPanel;
		this.markReaderPanel.getDirectoryChooserPanel().addDirectoryChooserEventListener(new OMRDirectoryChooserEventListener());
		
		new FileDropTargetDecorator(this.markReaderPanel){
			@Override
			public void dragEnter(DropTargetDragEvent e) {
				MarkReaderPanelController.this.dragEnter();
			}
			
			@Override
			public void dragOver(DropTargetDragEvent e) {
			}
			
			@Override
			public void dragExit(DropTargetEvent e) {
				MarkReaderPanelController.this.dragExit();
			}

			public void drop(File sourceDirectoryRootFile){
				markReaderPanel.setBorder(markReaderPanel.getDefaultBorder());
				userOpenWrapper(sourceDirectoryRootFile);
			}
		};
	}
	
	private class OMRDirectoryChooserEventListener implements DirectoryChooserEventListener{
		public void chooseDirectory(FileChooserEvent ev){
			final File sourceDirectoryRootFile = ev.getFile();
			userOpenWrapper(sourceDirectoryRootFile);
		}
	}
	
	public void userOpenWrapper(final File sourceDirectoryRootFile){
		try{
			userOpen(sourceDirectoryRootFile);
		}catch(IOException ex){
			ex.printStackTrace();
		}catch(TaskException ex){
			ex.printStackTrace();
		}
	}
	
	protected MarkReaderSessionPanel createSessionPanel(File sourceDirectoryRoot, SessionProgressModel markReaderSessionProgressModel){
		markReaderPanel.getCardLayout().show(markReaderPanel, "tab");
		return markReaderPanel.createSessionPanel(sourceDirectoryRoot, markReaderSessionProgressModel);
	}
	
	public void showDensityConfigurationDialog(SessionSource sessionSource){
		SourceDirectoryConfiguration config = sessionSource.getSessionSourceContentAccessor().getSourceDirectory("").getConfiguration();
		MarkRecognitionConfigurationDialog dialog = new MarkRecognitionConfigurationDialog(markReaderPanel.getFrame(), new File(sessionSource.getRootDirectory(), GenericSourceConfig.SOURCE_CONFIG_FILENAME), config);
		dialog.pack();
		dialog.setVisible(true);
	}
	
	public void userOpen(final File sourceDirectoryRootFile)throws IOException,TaskException{
		try{
			MarkReaderSession session = (MarkReaderSession)this.markReaderController.createOrReuseSession(sourceDirectoryRootFile);
			
			if(! session.isNew()){				
				if(session.isStoppedOrFinished()){
					this.markReaderController.userOpen(session);
					return;
				}else{
					if(this.markReaderPanel == null){
						return;
					}
					this.markReaderPanel.setForgroundSessionPanel(sourceDirectoryRootFile);
					return;
				}
			}
			
			this.markReaderController.userOpen(session);

			if(this.markReaderPanel == null){
				// non GUI mode
				return;
			}
			
			this.markReaderPanel.setRunningTabIcon(sourceDirectoryRootFile);
			
			SessionProgressModel markReaderSessionProgressModel = session.getSessionProgressModel();  		
			PageTaskErrorTableModel pageTaskErrorTableModel = new PageTaskErrorTableModel(); 
			
			MarkAreasTableModel noAnswerMarkAreasTableModel  = new MarkAreasTableModel();
			MarkAreasTableModel multipleAnswersMarkAreasTableModel  = new MarkAreasTableModel();
			
			final MarkReaderSessionPanel markReaderSessionPanel = createSessionPanel(sourceDirectoryRootFile, markReaderSessionProgressModel);			
			final MarkReaderSessionPanelController markReaderSessionPanelController =
				new MarkReaderSessionPanelController(this.markReaderPanel, session, markReaderSessionPanel,
						markReaderController, this,
						pageTaskErrorTableModel,
						noAnswerMarkAreasTableModel, multipleAnswersMarkAreasTableModel);
			
			int numSameNameSessions = this.markReaderController.countSessionsBySourceDirectory(sourceDirectoryRootFile);
			String name = createTabName(sourceDirectoryRootFile, numSameNameSessions);

			markReaderSessionPanel.setPlayStateGUI();
			this.markReaderPanel.addMarkReaderSessionPanel(markReaderSessionPanel, name);

			session.addSessionMonitor(new MarkReaderSessionMonitorAdapter(){
				@Override
				public void notifySessionFinished(final File sourceDirectoryRootFile){
					SwingUtilities.invokeLater(new Runnable(){
						public void run(){
							markReaderPanel.setFinishedTabIcon(sourceDirectoryRootFile);
							
							if(0 == markReaderSessionPanel.getSessionProgressModel().getNumErrorPages()){
								markReaderPanel.promptSessionFinished(sourceDirectoryRootFile);
								markReaderSessionPanel.selectResultTab();
								//openResultBrowser();
							}
						}
					});
				}});

		}catch(SessionException ex){
			ex.printStackTrace();

			NoPDFSourceFolderWarningPrompter noPDFWarningPrompter = new NoPDFSourceFolderWarningPrompter(markReaderPanel);
			boolean searchParent = noPDFWarningPrompter.prompt(sourceDirectoryRootFile);
			// FIXME! pdfSearch feature 
}
	}

	private String createTabName(final File sourceDirectoryRootFile,
			int numSameNameSessions) {
		String name = null;
		if(1 == numSameNameSessions){
			name = sourceDirectoryRootFile.getName();
		}else if(1 < numSameNameSessions){
			name = sourceDirectoryRootFile.getName()+"("+numSameNameSessions+")";
		}
		return name;
	}
	
	public void userClose(final File sourceDirectoryRoot){
		this.markReaderPanel.removeTabPanel(sourceDirectoryRoot);
		this.markReaderController.userClose(sourceDirectoryRoot);
	}
	
	public void userClear(File sourceDirectoryRoot)throws IOException,TaskException{
		this.markReaderController.userClear(sourceDirectoryRoot);;
	}
	
	public void userStop(File sourceDirectoryRoot){
		this.markReaderController.userStop(sourceDirectoryRoot);;
	}
	
	public void userExitConfirmation(){
		if(this.markReaderPanel.exitConfirmation()){
			try{
				this.markReaderController.userExit();
			}catch(Exception ignore){
				ignore.printStackTrace();
			}
		}
	}
	
	public void dragEnter(){
		this.markReaderPanel.getFrame().toFront();
		this.markReaderPanel.setBorder(this.markReaderPanel.getFocusedBorder());		
	}

	public void dragExit(){
		this.markReaderPanel.setBorder(this.markReaderPanel.getDefaultBorder());
	}
}
