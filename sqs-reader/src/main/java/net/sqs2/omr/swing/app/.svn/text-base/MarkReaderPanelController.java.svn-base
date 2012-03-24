package net.sqs2.omr.swing.app;

import java.awt.Window;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.sqs2.omr.app.MarkReaderApp;
import net.sqs2.omr.app.command.CountRemovableResultFoldersCommand;
import net.sqs2.omr.app.command.RemoveResultFoldersCommand;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.MarkReaderConfiguration;
import net.sqs2.omr.model.MarkReaderConstants;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SourceDirectoryConfiguration;
import net.sqs2.omr.session.service.PageTaskExecutionProgressModel;
import net.sqs2.swing.FileDropTargetDecorator;
import net.sqs2.swing.process.RemoteWindowPopupDecorator;

public class MarkReaderPanelController {

	MarkReaderApp markReaderApp;
	MarkReaderPanelImpl markReaderPanel = null;

	public MarkReaderPanelController(final MarkReaderApp markReaderApp,
			final MarkReaderPanelImpl markReaderPanel) {
		this.markReaderApp = markReaderApp;
		if (markReaderPanel == null) {
			// Console Mode(No GUI)
			return;
		}

		this.markReaderPanel = markReaderPanel;

		new FileDropTargetDecorator(this.markReaderPanel) {
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

			@Override
			public void drop(File[] sourceDirectories) {
				markReaderPanel.setBorder(markReaderPanel.getDefaultBorder());
				for(File directory: sourceDirectories){
					openAndStartSession(directory);
				}
			}
		};
	}

	public void removeResultFolders() {
		JFileChooser dchooser = this.markReaderPanel.getDirectoryChooser();
		String path = MarkReaderConfiguration.getSingleton().get(
				AppConstants.SOURCE_DIRECTORY_ROOT_KEY_IN_PREFERENCES,
				MarkReaderConstants.DEFAULT_SOURCEDIRECTORY_PATH);
		if (path != null) {
			dchooser.setSelectedFile(new File(path));
		}
		if(showRemoveResultFolderDialog(dchooser)){
			int numRemovableResultFolders = new CountRemovableResultFoldersCommand(dchooser.getSelectedFile()).call();
			if(confirmRemoveResultFolders(numRemovableResultFolders)){
				new RemoveResultFoldersCommand(dchooser.getSelectedFile()).call();
			}
		}
	}
	
	private boolean showRemoveResultFolderDialog(JFileChooser dchooser){
		return JFileChooser.APPROVE_OPTION == dchooser.showOpenDialog(this.markReaderPanel.getFrame());
	}
	
	private boolean confirmRemoveResultFolders(int numRemovableResultFolders){
		if(0 == numRemovableResultFolders){
			JOptionPane.showMessageDialog(this.markReaderPanel.getFrame(), new Object[]{
				"no folders",
			});
			return false;
		}
		return JFileChooser.APPROVE_OPTION == 
			JOptionPane.showConfirmDialog(this.markReaderPanel.getFrame(), new Object[]{
				numRemovableResultFolders+" folders will be removed. continue?",
			});
	}


	public MarkReaderSessionPanel createSessionPanel(File sourceDirectoryRoot, PageTaskExecutionProgressModel markReaderSessionProgressModel) {
		this.markReaderPanel.getCardLayout().show(this.markReaderPanel, "tab");
		return this.markReaderPanel.createSessionPanel(sourceDirectoryRoot, markReaderSessionProgressModel);
	}

	public void showDensityConfigurationDialog(SessionSource sessionSource) {
		SourceDirectoryConfiguration config = sessionSource.getContentAccessor()
		.getSourceDirectory("").getConfiguration();
		File configFile = new File(sessionSource.getRootDirectory().getAbsolutePath() + File.separatorChar
		+ AppConstants.RESULT_DIRNAME + File.separatorChar
		+ AppConstants.SOURCE_CONFIG_FILENAME);
		MarkRecognitionConfigurationDialog dialog = new MarkRecognitionConfigurationDialog(
				this.markReaderPanel.getFrame(), configFile, config);
		dialog.pack();
		dialog.setVisible(true);
	}
	
	public void openAndStartSession(final File sourceDirectoryRootFile){
		new OpenAndStartSessionCommand(this, markReaderApp, sourceDirectoryRootFile, this.markReaderPanel).run();
	}

	public void closeSession(final File sourceDirectoryRoot) {
		this.markReaderPanel.removeTabPanel(sourceDirectoryRoot);
		this.markReaderApp.closeSessionSource(sourceDirectoryRoot);
	}

	public void stopSession(File sourceDirectoryRoot) {
		this.markReaderApp.stopSession(sourceDirectoryRoot);
	}

	public void promptExitConfirmation() {

		if (this.markReaderPanel.getTabbedPane().getTabCount() == 0) {
			doExit();
			return;
		}

		if (this.markReaderPanel.exitConfirmation()) {
			try {
				doExit();
			} catch (Exception ignore) {
				ignore.printStackTrace();
			}
		}
	}

	public void dragEnter() {
		this.markReaderPanel.getFrame().toFront();
		this.markReaderPanel.setBorder(this.markReaderPanel.getFocusedBorder());
	}

	public void dragExit() {
		this.markReaderPanel.setBorder(this.markReaderPanel.getDefaultBorder());
	}
	
	public void doExit() {
		Window window = RemoteWindowPopupDecorator.inactivate(this.markReaderApp.getRMIPort());
		window.setVisible(false);
		window.dispose();
		/*
		try {
			SQSHttpdManager.getEXIgridHttpd().stop();
		} catch (Exception ignore) {
			ignore.printStackTrace();
		}*/
		this.markReaderApp.shutdown();
		System.runFinalization();
		System.exit(0);
	}

}
