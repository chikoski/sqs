package net.sqs2.omr.result.event;

import net.sqs2.omr.master.FormArea;

public class QuestionItemEvent extends ContentsEvent {
	
	PageEvent pageEvent;
	QuestionEvent questionEvent;
	int pageIndex;
	int itemIndex;
	FormArea formArea;
	
	QuestionItemEvent(QuestionEvent questionEvent){
		this.questionEvent = questionEvent;
	}
	
	public void setPageEvent(PageEvent pageEvent) {
		this.pageEvent = pageEvent;
	}
		
	public PageEvent getPageEvent(){
		return this.pageEvent;
	}

	public void setQuestionEvent(QuestionEvent questionEvent) {
		this.questionEvent = questionEvent;
	}
		
	public QuestionEvent getQuestionEvent(){
		return this.questionEvent;
	}

	public void setItemIndex(int itemIndex){
		this.itemIndex = itemIndex;
	}
	
	public int getItemIndex(){
		return this.itemIndex;
	}

	public int getPageIndex(){
		return this.formArea.getPageIndex();
	}

	public FormArea getFormArea() {
		return formArea;
	}

	public void setFormArea(FormArea formArea) {
		this.formArea = formArea;
	}
	
}
