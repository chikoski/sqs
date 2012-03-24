/**
 * 
 */
package net.sqs2.omr.result.event;

public class SessionSourceContentsEventFilter{
	public boolean filter(SessionEvent sessionEvent){
		return true;
	}
	
	public boolean filter(MasterEvent sessionEvent){
		return true;
	}

	public boolean filter(SourceDirectoryEvent sourceDirectoryEvent){
		return true;
	}
}