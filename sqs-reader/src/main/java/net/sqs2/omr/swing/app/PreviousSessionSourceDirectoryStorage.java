package net.sqs2.omr.swing.app;

import java.io.File;

public class PreviousSessionSourceDirectoryStorage{

	private File defaultSourceDirectoryRoot;

	// private static final String PROPERTYNAME_DEFAULT_SOURCE_DIRECTORY_ROOT =
	// "defaultSourceDirectoryRoot";

	public PreviousSessionSourceDirectoryStorage() {
	}

	public void setDefaultSourceDirectoryRoot(File defaultSourceDirectoryRoot) {
		// firePropertyChange(PROPERTYNAME_DEFAULT_SOURCE_DIRECTORY_ROOT,
		// this.defaultSourceDirectoryRoot, defaultSourceDirectoryRoot);
		this.defaultSourceDirectoryRoot = defaultSourceDirectoryRoot;
	}

	public File getDefaultSourceDirectoryRoot() {
		return this.defaultSourceDirectoryRoot;
	}

}
