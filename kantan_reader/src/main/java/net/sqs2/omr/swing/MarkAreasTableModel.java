package net.sqs2.omr.swing;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.source.SpreadSheet;

public class MarkAreasTableModel extends SortableTableModel {
	
	MarkAreasTableModel(){
		super(0, 6);
	}
	
	public void addRow(int index,
			SpreadSheet spreadSheet,
			SourceDirectory rowGroupSourceDirectory,
			int pageStart,
			int pageEnd,
			int rowIndex,
			int columnIndex){
		String pageRange = Integer.toString(pageStart);
		if(pageStart != pageEnd){
			pageRange = new StringBuilder(pageRange).append('-')+Integer.toString(pageEnd).toString();
		}
		
		FormMaster master = (FormMaster)rowGroupSourceDirectory.getPageMaster();
		FormArea defaultFormArea = master.getFormAreaList(columnIndex).get(0);
		
		MarkAreasTableCell cell = new MarkAreasTableCell(spreadSheet, rowGroupSourceDirectory, rowIndex, columnIndex);
		
		addRow(new Object[]{
				index,
				rowGroupSourceDirectory.getPath(),
				rowIndex + 1, 
				pageRange, 
				defaultFormArea.getQID(), 
				cell});
		sort(0, true);
	}
	
	public void clear(){
		getDataVector().clear();
	}
}
