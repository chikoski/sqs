package net.sqs2.omr.result.event;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.source.SpreadSheet;

public class RowGroupEvent extends ContentsEvent {
	
	SpreadSheetEvent spreadSheetEvent;
	FormMaster master;
	SpreadSheet spreadSheet;
	SourceDirectory sourceDirectory;
	SourceDirectory parentSourceDirectory;
	int numPrevRowsTotal;
	
	RowGroupEvent(SpreadSheetEvent spreadSheetEvent, FormMaster master, SpreadSheet spreadSheet, int numEvents){
		this.spreadSheetEvent = spreadSheetEvent;
		this.master = master;
		this.spreadSheet = spreadSheet;
		this.numEvents = numEvents;
	}
	
	public int getNumPrevRowsTotal(){
		return this.numPrevRowsTotal;
	}
	
	public void setNumPrevRowsTotal(int numPrevRowsTotal){
		this.numPrevRowsTotal = numPrevRowsTotal;
	}
	
	public SpreadSheetEvent getSpreadSheetEvent(){
		return this.spreadSheetEvent;
	}
	
	public FormMaster getFormMaster(){
		return this.master;
	}

	void setSourceDirectory(SourceDirectory sourceDirectory){
		this.sourceDirectory = sourceDirectory;
	}
	
	public SourceDirectory getSourceDirectory(){
		return this.sourceDirectory;
	}
	
	void setParentSourceDirectory(SourceDirectory parentSourceDirectory){
		this.parentSourceDirectory = parentSourceDirectory;
	}
	
	public SourceDirectory getParentSourceDirectory(){
		return this.parentSourceDirectory;
	}
	
}
