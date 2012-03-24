package net.sqs2.omr.result.path;

import java.util.ArrayList;
import java.util.List;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.Row;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.SpreadSheet;
import net.sqs2.omr.result.model.RowItem;
import net.sqs2.omr.result.model.RowItemList;

public class MultiRowSelection extends MultiSpreadSheetSelection{
	
	List<SelectedRowID> rowIDList;
	
	public MultiRowSelection(MultiRowPath multiRowPath) {
		super(multiRowPath);
		rowIDList = createRowIDList(multiRowPath);
	}
	
	public MultiRowPath getMultiRowPath(){
		return (MultiRowPath)masterPath;
	}

	private List<SelectedRowID> createRowIDList(MultiRowPath multiRowPath){
		List<SelectedRowID> rowIDList = new ArrayList<SelectedRowID>(multiRowPath.getRowSelection().getSelectedIndexTreeSet().size());
		int tableIndex = 0;
		int baseRowIndexCombined = 0;
		
		SourceDirectory currentSourceDirectory = flattenSelectedSourceDirectoryList.get(0);
		SpreadSheet spreadSheet = new SpreadSheet(sessionSource.getSessionID(), formMaster, currentSourceDirectory);
		
		for(int rowIndexCombined: multiRowPath.getRowSelection().getSelectedIndexTreeSet()){
			while(baseRowIndexCombined + currentSourceDirectory.getNumPageIDs() <= rowIndexCombined){
				baseRowIndexCombined += currentSourceDirectory.getNumPageIDs();
				tableIndex++;
				currentSourceDirectory = flattenSelectedSourceDirectoryList.get(tableIndex);
				spreadSheet = new SpreadSheet(sessionSource.getSessionID(), formMaster, currentSourceDirectory);
			}
			int rowIndex = rowIndexCombined - baseRowIndexCombined;
			SelectedRowID rowID = new SelectedRowID(spreadSheet, rowIndex); 
			rowIDList.add(rowID);
		}
		return rowIDList;
	}

	public List<SelectedRowID> getSelectedRowIDList() {
		return rowIDList;
	}
	
	public RowItemList createRowItemList(){
		RowItemList rowItemList = new RowItemList();
		int numPages = formMaster.getNumPages();
		for(SelectedRowID selectedRowID: rowIDList){
			FormMaster formMaster = selectedRowID.getSpreadSheet().getFormMaster();
			SourceDirectory sourceDirectory = selectedRowID.getSpreadSheet().getSourceDirectory();
			int rowIndex = (int)selectedRowID.getRowIndex();
			String sourceDirectoryPath = sourceDirectory.getRelativePath();
			Row row = sessionSource.getContentAccessor().getRowAccessor().get(formMaster.getRelativePath(),
					sourceDirectoryPath, rowIndex);
			
			List<PageID> pageIDList = new ArrayList<PageID>();
			for(int index = 0; index < numPages; index++){
				PageID pageID = sourceDirectory.getPageID(index + rowIndex * numPages);
				pageIDList.add(pageID);
			}
						
			rowItemList.add(new RowItem(pageIDList, numPages, sourceDirectoryPath, rowIndex));
		}
		return rowItemList;
	}

}
