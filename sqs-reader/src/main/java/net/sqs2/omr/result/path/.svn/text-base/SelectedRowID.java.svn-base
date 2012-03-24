package net.sqs2.omr.result.path;

import java.io.Serializable;
import java.util.List;

import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.SpreadSheet;

public class SelectedRowID implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private SpreadSheet spreadSheet;
	private long rowIndex;

	public SelectedRowID(SpreadSheet spreadSheet, long rowIndex) {
		this.spreadSheet = spreadSheet;
		this.rowIndex = rowIndex;
	}

	public SpreadSheet getSpreadSheet() {
		return this.spreadSheet;
	}

	public long getRowIndex() {
		return this.rowIndex;
	}

	public String toName() {
		List<PageID> pageIDList = spreadSheet.getSourceDirectory().getPageIDList();
		int numPages = spreadSheet.getFormMaster().getNumPages();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(this.rowIndex+1);
		sb.append("] ");
		int size = pageIDList.size();
		if(size == 1){
			sb.append(pageIDList.get((int)(numPages*this.rowIndex)));
		}else if(1 < size){
			sb.append(pageIDList.get((int)(numPages*this.rowIndex)));
			sb.append('-');
			sb.append(pageIDList.get((int)(numPages * this.rowIndex + numPages - 1)));
		}
		return sb.toString();
	}

	@Override
	public String toString(){
		return spreadSheet.getSourceDirectory().getRelativePath()+'\t'+rowIndex;
	}

	@Override
	public boolean equals(Object o){
		try{
			SelectedRowID rowID = (SelectedRowID)o;
			return rowIndex == rowID.getRowIndex() &&
					spreadSheet.getSourceDirectory().getRelativePath().equals(rowID.getSpreadSheet().getSourceDirectory().getRelativePath());
			
		}catch(ClassCastException ignore){
			return false;
		}
	}
}
