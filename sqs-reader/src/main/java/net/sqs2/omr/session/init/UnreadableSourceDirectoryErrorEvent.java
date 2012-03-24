package net.sqs2.omr.session.init;

import java.util.EventObject;

import net.sqs2.omr.model.SourceDirectory;

public class UnreadableSourceDirectoryErrorEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SourceDirectory sourceDirectory;
	public UnreadableSourceDirectoryErrorEvent(Object source, SourceDirectory sourceDirectory){
		super(source);
		this.sourceDirectory = sourceDirectory;
	}
	public SourceDirectory getSourceDirectory() {
		return sourceDirectory;
	}
	
}
