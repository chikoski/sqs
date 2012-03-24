package net.sqs2.omr.master;

import java.io.File;

public class NoConfigFilePageMasterException extends FormMasterException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoConfigFilePageMasterException(File file){
		super(file);
	}
}
