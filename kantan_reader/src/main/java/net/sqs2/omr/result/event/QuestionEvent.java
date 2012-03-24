package net.sqs2.omr.result.event;

import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.model.Answer;
import net.sqs2.omr.result.model.Row;

public class QuestionEvent extends ContentsEvent {
	RowEvent rowEvent;
	FormMaster master;
	int columnIndex;
	List<FormArea> formAreaList;
	//MarkAreaAnswerItemSet markAreaAnswerItemSet;
	
	QuestionEvent(RowEvent rowEvent, FormMaster master){
		this.rowEvent = rowEvent;
		this.master = master;
		this.numEvents = master.getNumColumns();
		//this.markAreaAnswerItemSet = new MarkAreaAnswerItemSet();
		//this.markAreaAnswerItemSet.add(this.rowEvent.getRow().getAnswer(columnIndex));
	}
	/*
	public MarkAreaAnswerItemSet getMarkAreaAnswerItemSet(){
		return this.markAreaAnswerItemSet;
	}*/
	
	public FormMaster getFormMaster(){
		return this.master;
	}
	
	public RowEvent getRowEvent(){
		return this.rowEvent;
	}
	
	public void setColumnIndex(int columnIndex){
		this.columnIndex = columnIndex;
	}
	
	public void setFormAreaList(List<FormArea> formAreaList){
		this.formAreaList = formAreaList;
	}
	
	public FormArea getDefaultFormArea(){
		return this.formAreaList.get(0);
	}
	
	public List<FormArea> getFormAreaList(){
		return this.formAreaList;
	}
	
	public int getColumnIndex(){
		return this.columnIndex;
	}
	
	public Answer getAnswer(){
		Row row = this.rowEvent.getRow();
		return row.getAnswer(this.columnIndex);
	}
}
