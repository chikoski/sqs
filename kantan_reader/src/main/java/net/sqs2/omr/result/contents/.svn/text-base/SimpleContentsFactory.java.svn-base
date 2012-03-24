/**
 * 
 */
package net.sqs2.omr.result.contents;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.model.RowAccessor;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.task.TaskAccessor;

public abstract class SimpleContentsFactory extends AbstractContentsFactory implements ContentsFactory{

	SessionSource sessionSource;
	
	TaskAccessor pageTaskAccessor;
	RowAccessor rowAccessor;
	
	public SimpleContentsFactory(SessionSource sessionSource) throws IOException {
		this.sessionSource = sessionSource;
		this.pageTaskAccessor = new TaskAccessor(sessionSource.getRootDirectory());
		this.rowAccessor = new RowAccessor(sessionSource.getRootDirectory());
	}
	
	public void create(FormMaster master,
			Set<Integer> selectedTableIndexSet,
			Set<Integer> selectedRowIndexSet,
			Set<Integer> selectedQuestionIndexSet) {
		List<SourceDirectory> flattenSourceDirectoryList = this.sessionSource.getSessionSourceContentIndexer().getSourceDirectoryDepthOrderedListMap().get(master);
		if (selectedRowIndexSet != null && ! selectedRowIndexSet.isEmpty()) {
			processRows(flattenSourceDirectoryList,
					selectedTableIndexSet,
					selectedRowIndexSet,
					selectedQuestionIndexSet);
		} else {
			processAllRows(flattenSourceDirectoryList,
					selectedTableIndexSet,
					selectedRowIndexSet,
					selectedQuestionIndexSet);
		}
	}
	
	boolean isSkipTable(int numColumnsSelected, int rowIndexBase, int numRows){
		return false;
	}
	
	class ProcessSourceDirectoryParam{
		
		List<SourceDirectory> flattenSourceDirectoryList;
		Set<Integer> selectedTableIndexSet;
		Set<Integer> selectedRowIndexSet;
		Set<Integer> selectedQuestionIndexSet;
		
		int rowIndexBase = 0;
		int currentRowIndex = -1;
		int selectedRowIndex = 0;
		int selectedTableIndex = 0;
		
		Iterator<Integer> selectedRowIndexIterator;
		
		ProcessSourceDirectoryParam(List<SourceDirectory> flattenSourceDirectoryList,
				Set<Integer> selectedTableIndexSet,
				Set<Integer> selectedRowIndexSet,
				Set<Integer> selectedQuestionIndexSet){
			this.flattenSourceDirectoryList = flattenSourceDirectoryList;
			this.selectedTableIndexSet = selectedTableIndexSet;
			this.selectedQuestionIndexSet = selectedQuestionIndexSet; 
			this.selectedRowIndexIterator = selectedRowIndexSet.iterator();
		}
	}
	
	private void processRows(List<SourceDirectory> flattenSourceDirectoryList,
			Set<Integer> selectedTableIndexSet,
			Set<Integer> selectedRowIndexSet,
			Set<Integer> selectedQuestionIndexSet) {
		try {
			
			ProcessSourceDirectoryParam param = 
				new ProcessSourceDirectoryParam(flattenSourceDirectoryList,
					selectedTableIndexSet,
					selectedRowIndexSet,
					selectedQuestionIndexSet);
			
			table: for (int tableIndex : selectedTableIndexSet) {
				SourceDirectory sourceDirectory = param.flattenSourceDirectoryList.get(tableIndex);
				processSourceDirectory(param, sourceDirectory, tableIndex);
			}
		} catch (NoSuchElementException ignore) {
		}
	}
	
	private void processSourceDirectory(ProcessSourceDirectoryParam param, SourceDirectory sourceDirectory, int tableIndex){
		
		List<PageID> pageIDList = sourceDirectory.getPageIDList();
		
		int numRows = pageIDList.size() / sourceDirectory.getPageMaster().getNumPages();
		int numColumnsSelected = param.selectedQuestionIndexSet.size();
		if(isSkipTable(numColumnsSelected, param.rowIndexBase, numRows)){
			param.rowIndexBase += numRows;
			return;
		}
		
		while (param.currentRowIndex != -1 || param.selectedRowIndexIterator.hasNext()) {
			if (param.currentRowIndex == -1) {
				param.currentRowIndex = param.selectedRowIndexIterator.next();
			}
								
			int rowIndex = param.currentRowIndex - param.rowIndexBase;
			if (rowIndex < numRows && isRowInSelectedTable(param.rowIndexBase, param.currentRowIndex, numRows)) {
				processRow((FormMaster)sourceDirectory.getPageMaster(),
						param.selectedQuestionIndexSet,
						sourceDirectory, param.selectedTableIndex, tableIndex, param.selectedRowIndex++, rowIndex);
				param.currentRowIndex = -1;
			} else {
				param.selectedRowIndex = 0;
				param.rowIndexBase += numRows;
				param.selectedTableIndex++;
				tableIndex++;
				return;
			}
		}
	}

	private boolean isRowInSelectedTable(int rowIndexBase, int selectedRowIndex, int numRows) {
		return rowIndexBase <= selectedRowIndex && selectedRowIndex < rowIndexBase + numRows;
	}
	
	boolean isOverwrapped(int aMin, int aMax, int bMin, int bMax){
		return ((bMin <= aMax  && aMin <= bMax) || (aMin <= bMax && bMin <= aMax));
	}

	private void processAllRows(List<SourceDirectory> flattenSourceDirectoryList,
			Set<Integer> selectedTableIndexSet,
			Set<Integer> selectedRowIndexSet,
			Set<Integer> selectedQuestionIndexSet) {
		int processedTableIndex = 0;
		for (int tableIndex : selectedTableIndexSet) {
			SourceDirectory sourceDirectory = flattenSourceDirectoryList.get(tableIndex);			
			processAllRows((FormMaster)sourceDirectory.getPageMaster(), selectedQuestionIndexSet, processedTableIndex, tableIndex, sourceDirectory);
			processedTableIndex++;
		}
	}

	/*
	class ProcessContext{
		int tableIndex;
		int processedTableIndex;
	}*/

	private void processAllRows(FormMaster master,
			Set<Integer> processedQuestionIndexSet,
			int processedTableIndex, int tableIndex,
			SourceDirectory sourceDirectory) {

		/*
		List<SourceDirectory> childSourceDirectoryList = sourceDirectory.getChildSourceDirectoryList();
		if(childSourceDirectoryList != null){
			for(SourceDirectory childSourceDirectory: childSourceDirectoryList){
				processAllRows(processedQuestionIndexSet, processedTableIndex, tableIndex, childSourceDirectory);
			}
		}*/
		
		List<PageID> pageIDList = sourceDirectory.getPageIDList();
		int numRows = pageIDList.size() / sourceDirectory.getPageMaster().getNumPages();
		for(int rowIndex = 0; rowIndex < numRows; rowIndex++){
			processRow(master, processedQuestionIndexSet, sourceDirectory, processedTableIndex, tableIndex, rowIndex, rowIndex);
		}
	}
	
	abstract void processRow(FormMaster master,
			Set<Integer> selectedQuestionIndexSet,
			SourceDirectory sourceDirectory, 
			int processedTableIndex, int tableIndex, int selectedRowIndex, int rowIndex);

}
