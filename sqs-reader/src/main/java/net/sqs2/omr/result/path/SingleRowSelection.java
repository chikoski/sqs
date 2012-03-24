package net.sqs2.omr.result.path;

import java.util.ArrayList;
import java.util.List;

import net.sqs2.omr.model.PageID;
import net.sqs2.omr.result.model.RowItem;

public class SingleRowSelection extends SingleSpreadSheetSelection {

	protected SelectedRowID rowID;
	List<PageID> pageIDList;
	
	public SingleRowSelection(SingleRowPath singleRowPath) {
		super(singleRowPath);
		long rowIndex = singleRowPath.getRowIndex();
		rowID = new SelectedRowID(spreadSheet, rowIndex);
		pageIDList = createPageIDList();
	}
		
	public SingleRowPath getSingleRowPath(){
		return (SingleRowPath)masterPath;
	}
	
	private List<PageID> createPageIDList(){
		List<PageID> pageIDList = new ArrayList<PageID>();
		long indexStart = formMaster.getNumPages() * rowID.getRowIndex();
		long indexEnd = formMaster.getNumPages() * (rowID.getRowIndex() + 1) - 1;
		for(long index = indexStart; index <= indexEnd; index++){
			pageIDList.add(sourceDirectory.getPageID((int)index));
		}
		return pageIDList;
	}

	public SelectedRowID getRowID() {
		return rowID;
	}
	
	public RowItem createRowItem(){
		return new RowItem(pageIDList, formMaster.getNumPages(), sourceDirectory.getRelativePath(), rowID.getRowIndex());
	}

}
