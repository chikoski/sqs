package net.sqs2.omr.swing;

import java.io.File;

import net.sqs2.omr.task.TaskError;

public class PageTaskErrorTableModel extends SortableTableModel {
	
	PageTaskErrorTableModel(){
		super(0, PageTaskErrorTable.COLUMNS.length);
	}
	
	public void addRow(int rowID, int pageNumber, File root, String path, TaskError ex){
		addRow(new Object[]{rowID, pageNumber, new PageTaskErrorTableCell(root, path, ex)});
	}
	
	public void clear(){
		getDataVector().clear();
	}
}
