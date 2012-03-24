package net.sqs2.omr.session.init;

import net.sqs2.omr.model.SourceDirectory;


public class InvalidNumImagesException extends SessionSourceException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int numImages;

	public InvalidNumImagesException(SourceDirectory sourceDirectory, int numImages) {
		super(sourceDirectory);
		this.numImages = numImages;
	}

	public int getNumImages() {
		return this.numImages;
	}
}
