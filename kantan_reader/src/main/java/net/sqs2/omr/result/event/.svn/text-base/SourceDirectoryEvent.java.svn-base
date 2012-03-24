package net.sqs2.omr.result.event;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.source.SourceDirectory;

public class SourceDirectoryEvent extends ContentsEvent {
	MasterEvent masterEvent;
	FormMaster master;
	
	SourceDirectoryEvent sourceDirectoryEvent;
	SourceDirectory sourceDirectory;
	
	public SourceDirectoryEvent(MasterEvent masterEvent, FormMaster master, int numEvents){
		this.masterEvent = masterEvent;
		this.master = master;
		this.numEvents = numEvents;
	}
	
	public MasterEvent getMasterEvent(){
		return this.masterEvent;
	}
	
	void setFormMaster(FormMaster master){
		this.master = master;
	}

	public FormMaster getFormMaster(){
		return this.master;
	}
	
	public void setSourceDirectory(SourceDirectory sourceDirectory){
		this.sourceDirectory = sourceDirectory;
	}
	
	public SourceDirectory getSourceDirectory(){
		return this.sourceDirectory;
	}

}
