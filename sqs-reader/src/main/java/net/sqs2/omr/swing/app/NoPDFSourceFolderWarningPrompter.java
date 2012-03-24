package net.sqs2.omr.swing.app;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.sqs2.omr.base.Messages;

public class NoPDFSourceFolderWarningPrompter {

	JPanel parent;
	boolean returnValue = false;

	public NoPDFSourceFolderWarningPrompter(JPanel parent) {
		this.parent = parent;
	}

	public synchronized boolean prompt(final File sourceDirectoryRoot) {
		if (this.parent == null) {
			throw new RuntimeException("parent is null");
		}
		this.returnValue = JOptionPane.showConfirmDialog(this.parent,
				Messages.SESSION_ERROR_NOPDFSOURCEFOLDER + "\n" + sourceDirectoryRoot.getAbsolutePath(),
				"Warning", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
		return this.returnValue;
	}
}