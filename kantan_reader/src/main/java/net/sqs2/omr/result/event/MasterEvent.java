package net.sqs2.omr.result.event;

import net.sqs2.omr.master.FormMaster;

public class MasterEvent extends ContentsEvent {
	SessionEvent sessionEvent;
	FormMaster master;
		
	MasterEvent(SessionEvent sessionEvent, int numEvents){
		this.sessionEvent = sessionEvent;
		this.numEvents = numEvents;
	}
	
	public SessionEvent getSessionEvent(){
		return this.sessionEvent;
	}
	
	void setFormMaster(FormMaster master){
		this.master = master;
	}

	public FormMaster getFormMaster(){
		return this.master;
	}
	
}
