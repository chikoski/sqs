package net.sqs2.omr.session.event;

import net.sqs2.omr.master.FormMaster;

public class MasterEvent extends ResultEvent {
	SessionEvent sessionEvent;
	FormMaster master;

	public MasterEvent(SessionEvent sessionEvent, int numEvents) {
		this.sessionEvent = sessionEvent;
		this.numEvents = numEvents;
	}

	public SessionEvent getSessionEvent() {
		return this.sessionEvent;
	}

	public void setFormMaster(FormMaster master) {
		this.master = master;
	}

	public FormMaster getFormMaster() {
		return this.master;
	}

}
