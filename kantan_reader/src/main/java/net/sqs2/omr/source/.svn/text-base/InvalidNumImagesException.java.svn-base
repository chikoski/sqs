package net.sqs2.omr.source;

public class InvalidNumImagesException extends SessionSourceException {
	int numImages;
	public InvalidNumImagesException(SourceDirectory sourceDirectory, int numImages){
		super(sourceDirectory);
		this.numImages = numImages;
	}
	
	public int getNumImages(){
		return this.numImages;
	}
}
