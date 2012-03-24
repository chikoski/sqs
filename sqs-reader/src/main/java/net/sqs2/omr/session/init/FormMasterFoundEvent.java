package net.sqs2.omr.session.init;

import net.sqs2.omr.master.FormMaster;

public class FormMasterFoundEvent extends SessionSourceInitializationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	FormMaster master;
	public FormMasterFoundEvent(Object source, FormMaster master) {
		super(source);
		this.master = master;
	}
	public FormMaster getMaster() {
		return master;
	}

}
