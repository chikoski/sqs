/**
 * 
 */
package net.sqs2.omr.session;

import java.io.File;

public class SessionException extends Exception{
	File sourceDirectoryRoot;
	String message;
	
	public SessionException(File sourceDirectoryRoot, String message){
		this.sourceDirectoryRoot = sourceDirectoryRoot;
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}
}