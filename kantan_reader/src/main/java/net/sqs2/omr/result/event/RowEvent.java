package net.sqs2.omr.result.event;

import java.io.File;
import java.util.List;

import net.sqs2.omr.result.model.Row;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.task.TaskError;
import net.sqs2.util.StringUtil;

import org.apache.commons.collections15.multimap.MultiHashMap;

public class RowEvent extends ContentsEvent {
	
	RowGroupEvent rowGroupEvent;
	int rowIndex;
	Row row;
	List<PageID> pageIDList;
	MultiHashMap<PageID,TaskError> taskErrorMultiHashMap;
	
	public RowEvent(RowGroupEvent sourceDirectoryEvent, int numEvents){
		this.rowGroupEvent  = sourceDirectoryEvent;
		this.numEvents = numEvents;
	}
	
	public RowGroupEvent getRowGroupEvent(){
		return this.rowGroupEvent;
	}
	
	public void setRow(Row row){
		this.row = row;
	}
	
	public Row getRow(){
		return this.row;
	}
	
	public void setRowIndex(int rowIndex){
		this.rowIndex = rowIndex;
	}

	public int getRowIndex(){
		return this.rowIndex;
	}
	
	public MultiHashMap<PageID,TaskError> getTaskErrorMultiHashMap(){
		return this.taskErrorMultiHashMap;
	}

	public void setTaskErrorMultiHashMap(MultiHashMap<PageID,TaskError> map){
		this.taskErrorMultiHashMap = taskErrorMultiHashMap;
	}
	
	public List<PageID> getPageIDList(){
		return this.pageIDList;
	}

	public void setPageIDList(List<PageID> pageIDList){
		this.pageIDList = pageIDList;
	}
	
	public String createRowMemberFilenames(char itemSeparator) {
		boolean separator = false;
		int numPages = this.rowGroupEvent.getFormMaster().getNumPages();
		StringBuilder filenames = new StringBuilder();
		for (int pageIndex = 0; pageIndex < numPages; pageIndex++) {
			PageID pageID = this.pageIDList.get(this.rowIndex * numPages + pageIndex);
			if (separator) {
				filenames.append(itemSeparator);
			} else {
				separator = true;
			}
			filenames.append(StringUtil.escapeTSV(new File(pageID.getFileResourceID().getRelativePath()).getName()));
		}
		return filenames.toString();
	}
}
