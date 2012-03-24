package net.sqs2.omr.session.init;

import java.io.File;


public class ImageFilesFoundEvent extends SessionSourceInitializationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int numAddedImages;
	File sourceDirectory;

	public ImageFilesFoundEvent(Object source, int numAddedImages, File sourceDirectory) {
		super(source);
		this.numAddedImages = numAddedImages;
		this.sourceDirectory = sourceDirectory;
	}

	public int getNumAddedImages() {
		return numAddedImages;
	}

	public File getSourceDirectory() {
		return sourceDirectory;
	}

}
