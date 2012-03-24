/**
 * 
 */
package net.sqs2.omr.swing;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.source.SpreadSheet;

public class MarkAreasTableCell {
	SpreadSheet spreadSheet;
	SourceDirectory rowGroupSourceDirectory;
	int rowIndex;
	int columnIndex;

	MarkAreasTableCell(SpreadSheet spreadSheet, 
			SourceDirectory rowGroupSourceDirectory,
			int rowIndex,
			int columnIndex) {
		this.spreadSheet = spreadSheet;
		this.rowGroupSourceDirectory = rowGroupSourceDirectory;
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}

	public SpreadSheet getSpreadSheet() {
		return spreadSheet;
	}
	
	public SourceDirectory getRowGroupSourceDirectory(){
		return rowGroupSourceDirectory;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
	
	public int hashCode(){
		return spreadSheet.hashCode() + rowIndex * spreadSheet.getSourceDirectory().getPageMaster().getNumPages() + columnIndex;
	}
	
	public List<PageID> getPageIDList(){
		FormMaster master = (FormMaster)rowGroupSourceDirectory.getPageMaster();
		int numPages = master.getNumPages();
		TreeSet<Integer> set = new TreeSet<Integer>();
		for(FormArea formArea : master.getFormAreaList(columnIndex)){
			int pageIndex = formArea.getPageIndex();
			set.add(pageIndex);
		}
		
		List<PageID> pageIDList = this.rowGroupSourceDirectory.getPageIDList();;
		List<PageID> ret = new ArrayList<PageID>(set.size());  
		for(int pageIndex: set){
			ret.add(pageIDList.get(rowIndex * numPages + pageIndex));
		}
		return ret;
	}

	public boolean equals(Object o){
		if(! (o instanceof MarkAreasTableCell)){
			return false;
		}
		MarkAreasTableCell m = (MarkAreasTableCell)o;
		
		return m.rowIndex == rowIndex && m.columnIndex == columnIndex && m.spreadSheet == spreadSheet;
	}
}