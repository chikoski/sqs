package net.sqs2.omr.swing;

import java.awt.Frame;
import java.io.File;

import javax.swing.JPanel;

import net.sqs2.omr.swing.MarkReaderPanelImpl.NoPDFSourceFolderWarningPrompter;
import net.sqs2.swing.DirectoryChooserPanel;

public interface MarkReaderPanel {
	
	public abstract Frame getFrame();

	public abstract MarkReaderSessionPanel getMarkReaderSessionPanel(File targetFile);

	public abstract void removeTabPanel(File sourceDirectoryRoot);

	public abstract DirectoryChooserPanel getDirectoryChooserPanel();

	public abstract void setFinishedTabIcon(final File sourceDirectoryRootFile);

	public abstract NoPDFSourceFolderWarningPrompter getNoPDFSourceFolderWarningPrompter();

	public abstract void setForgroundSessionPanel(File sourceDirectoryRootFile);

	public abstract boolean exitConfirmation();

	public abstract void promptSessionFinished(final File sourceDirectoryRootFile);

	public abstract void addMarkReaderSessionPanel(JPanel sessionPanel, String name);

	public MarkReaderSessionPanel createSessionPanel(File sourceDirectoryRoot, SessionProgressModel markReaderSessionProgressModel);
}