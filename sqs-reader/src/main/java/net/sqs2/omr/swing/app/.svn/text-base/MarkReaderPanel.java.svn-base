package net.sqs2.omr.swing.app;

import java.awt.Frame;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import net.sqs2.omr.session.service.PageTaskExecutionProgressModel;

public interface MarkReaderPanel {

	public abstract Frame getFrame();

	public abstract MarkReaderSessionPanel getMarkReaderSessionPanel(File targetFile);

	public abstract void removeTabPanel(File sourceDirectoryRoot);

	public abstract JFileChooser getDirectoryChooser();

	public abstract void setFinishedTabIcon(final File sourceDirectoryRootFile);

	public abstract NoPDFSourceFolderWarningPrompter getNoPDFSourceFolderWarningPrompter();

	public abstract void setForgroundSessionPanel(File sourceDirectoryRootFile);

	public abstract boolean exitConfirmation();

	public abstract void promptSessionFinished(final File sourceDirectoryRootFile);

	public abstract void addMarkReaderSessionPanel(JPanel sessionPanel, String name);

	public MarkReaderSessionPanel createSessionPanel(File sourceDirectoryRoot, PageTaskExecutionProgressModel markReaderSessionProgressModel);

}
