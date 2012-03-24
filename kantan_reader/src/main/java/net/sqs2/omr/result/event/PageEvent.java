package net.sqs2.omr.result.event;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.task.PageTask;

public class PageEvent extends ContentsEvent {
	RowEvent rowEvent;
	FormMaster master;
	int pageIndex;
	PageTask pageTask;
	
	enum ERROR_TYPE{NONE, PAGEFRAME_RECOG_ERROR, PAGEORDER_RECOGMARK_INVALID, PAGE_UPSIEDE_DOWN}
	
	ERROR_TYPE errorType = ERROR_TYPE.NONE;
	
	PageEvent(RowEvent rowEvent, FormMaster master){
		this.rowEvent = rowEvent;
		this.master = master;
	}
	
	void setErrorType(ERROR_TYPE type){
		this.errorType = type;
	}
	
	public RowEvent getRowEvent(){
		return this.rowEvent;
	}
	
	public FormMaster getFormMaster(){
		return this.master;
	}
	
	public void setPageIndex(int pageIndex){
		this.pageIndex = pageIndex;
	}
	
	public int getPageIndex(){
		return this.pageIndex;
	}

	public PageTask getPageTask() {
		return this.pageTask;
	}

	public void setPageTask(PageTask pageTask) {
		this.pageTask = pageTask;
	}
	
}
