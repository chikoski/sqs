package net.sqs2.omr.session.init;

import java.util.EventObject;

import net.sqs2.omr.master.FormMasterException;

public class InvalidSourceDirectoryErrorEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	FormMasterException formMasterException;
	
	public InvalidSourceDirectoryErrorEvent(Object source, FormMasterException exception) {
		super(source);
		this.formMasterException = exception;
	}

	public FormMasterException getFormMasterException() {
		return formMasterException;
	}

}
