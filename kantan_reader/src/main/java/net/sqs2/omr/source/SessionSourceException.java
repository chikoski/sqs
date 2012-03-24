package net.sqs2.omr.source;

public class SessionSourceException extends Exception {
	SourceDirectory sourceDirectory;
	SessionSourceException(SourceDirectory sourceDirectory){
		this.sourceDirectory = sourceDirectory;
	}
	
	public SourceDirectory getSourceDirectory(){
		return sourceDirectory;
	}
}
