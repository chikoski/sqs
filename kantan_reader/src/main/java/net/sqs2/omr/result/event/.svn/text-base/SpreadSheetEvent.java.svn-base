package net.sqs2.omr.result.event;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.source.SpreadSheet;

public class SpreadSheetEvent extends ContentsEvent{
	SourceDirectoryEvent sourceDirectoryEvent;
	FormMaster master;
	SpreadSheet spreadSheet;
	
	public SpreadSheetEvent(SourceDirectoryEvent sourceDirectoryEvent, FormMaster master, int numEvents){
		this.sourceDirectoryEvent = sourceDirectoryEvent;
		this.master = master;
		this.numEvents = numEvents;
	}
	
	public SourceDirectoryEvent getSourceDirectoryEvent(){
		return this.sourceDirectoryEvent;
	}
	
	public void setSpreadSheet(SpreadSheet spreadSheet){
		this.spreadSheet = spreadSheet;
	}

	public SpreadSheet getSpreadSheet(){
		return this.spreadSheet;
	}
	
	public FormMaster getFormMaster(){
		return this.master;
	}
}
