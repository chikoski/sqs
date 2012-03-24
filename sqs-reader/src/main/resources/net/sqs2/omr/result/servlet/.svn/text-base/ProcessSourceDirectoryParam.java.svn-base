/**
 * 
 */
package net.sqs2.omr.result.servlet;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sqs2.omr.model.SourceDirectory;

class ProcessSourceDirectoryParam {

	List<SourceDirectory> flattenSourceDirectoryList;
	Set<Integer> selectedTableIndexSet;
	Set<Integer> selectedRowIndexSet;
	Set<Integer> selectedQuestionIndexSet;

	int rowIndexBase = 0;
	int currentRowIndex = -1;
	int selectedRowIndex = 0;
	int selectedTableIndex = 0;

	Iterator<Integer> selectedRowIndexIterator;

	private ProcessSourceDirectoryParam(List<SourceDirectory> flattenSourceDirectoryList,
			Set<Integer> selectedTableIndexSet, Set<Integer> selectedRowIndexSet,
			Set<Integer> selectedQuestionIndexSet) {
		this.flattenSourceDirectoryList = flattenSourceDirectoryList;
		this.selectedTableIndexSet = selectedTableIndexSet;
		this.selectedQuestionIndexSet = selectedQuestionIndexSet;
		this.selectedRowIndexIterator = selectedRowIndexSet.iterator();
	}
	
	public List<SourceDirectory> getFlattenSourceDirectoryList(){
		return flattenSourceDirectoryList;
	}

	public Set<Integer> getSelectedTableIndexSet() {
		return selectedTableIndexSet;
	}

	public void setSelectedTableIndexSet(Set<Integer> selectedTableIndexSet) {
		this.selectedTableIndexSet = selectedTableIndexSet;
	}

	public Set<Integer> getSelectedRowIndexSet() {
		return selectedRowIndexSet;
	}

	public void setSelectedRowIndexSet(Set<Integer> selectedRowIndexSet) {
		this.selectedRowIndexSet = selectedRowIndexSet;
	}

	public Set<Integer> getSelectedQuestionIndexSet() {
		return selectedQuestionIndexSet;
	}

	public void setSelectedQuestionIndexSet(Set<Integer> selectedQuestionIndexSet) {
		this.selectedQuestionIndexSet = selectedQuestionIndexSet;
	}

	public int getRowIndexBase() {
		return rowIndexBase;
	}

	public void setRowIndexBase(int rowIndexBase) {
		this.rowIndexBase = rowIndexBase;
	}

	public int getCurrentRowIndex() {
		return currentRowIndex;
	}

	public void setCurrentRowIndex(int currentRowIndex) {
		this.currentRowIndex = currentRowIndex;
	}

	public int getSelectedRowIndex() {
		return selectedRowIndex;
	}

	public void setSelectedRowIndex(int selectedRowIndex) {
		this.selectedRowIndex = selectedRowIndex;
	}

	public int getSelectedTableIndex() {
		return selectedTableIndex;
	}

	public void setSelectedTableIndex(int selectedTableIndex) {
		this.selectedTableIndex = selectedTableIndex;
	}

	public Iterator<Integer> getSelectedRowIndexIterator() {
		return selectedRowIndexIterator;
	}

	public void setSelectedRowIndexIterator(
			Iterator<Integer> selectedRowIndexIterator) {
		this.selectedRowIndexIterator = selectedRowIndexIterator;
	}

	public void setFlattenSourceDirectoryList(
			List<SourceDirectory> flattenSourceDirectoryList) {
		this.flattenSourceDirectoryList = flattenSourceDirectoryList;
	}
	
	
}
