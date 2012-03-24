/**
 * 
 */
package net.sqs2.omr.result.servlet.writer;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTaskAccessor;
import net.sqs2.omr.model.RowAccessor;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.result.context.ResultBrowserContext;
import net.sqs2.omr.util.ArrayUtil;

public abstract class SimpleContentsWriter{

	ResultBrowserContext contentSelection;
	PageTaskAccessor pageTaskAccessor;
	RowAccessor rowAccessor;

	public SimpleContentsWriter(ResultBrowserContext contentSelection) throws IOException {
		this.contentSelection = contentSelection;
		this.pageTaskAccessor = contentSelection.getSessionSource().getContentAccessor().getPageTaskAccessor();
		this.rowAccessor = contentSelection.getSessionSource().getContentAccessor().getRowAccessor();
	}
	
	public void create() {
		if (ArrayUtil.existsTrue(this.contentSelection.getRowSelectionArray())) {
			processRows();
		} else {
			processAllRows();
		}
	}

	boolean isSkippableTable(int numColumnsSelected, int rowIndexBase, int numRows) {
		return false;
	}

	private void processRows() {
		try {
			for (int tableIndex = 0; tableIndex < this.contentSelection.getRowSelectionArray().length; tableIndex++) {
				processSourceDirectory(tableIndex);
			}
		} catch (NoSuchElementException ignore) {
		}
	}

	private void processSourceDirectory(int tableIndex) {
		SourceDirectory sourceDirectory = this.contentSelection.getSelectedSourceDirectoryList().get(tableIndex); 
		List<PageID> pageIDList = sourceDirectory.getPageIDList();

		int numRows = pageIDList.size() / sourceDirectory.getFormMaster().getNumPages();
		boolean isOneOreMoreQuestionsSelected = ArrayUtil.existsTrue(this.contentSelection.getQuestionSelectionArray());
		if (! isOneOreMoreQuestionsSelected) {//isSkippableTable(numColumnsSelected, param.getRowIndexBase(), numRows)
			param.setRowIndexBase(param.getRowIndexBase() + numRows);
			return;
		}

		while (param.getCurrentRowIndex() != -1 || param.getSelectedRowIndexIterator().hasNext()) {
			if (param.getCurrentRowIndex() == -1) {
				param.setCurrentRowIndex(param.getSelectedRowIndexIterator().next());
			}

			int rowIndex = param.getCurrentRowIndex() - param.getRowIndexBase();
			if (rowIndex < numRows
					&& isRowInSelectedTable(param.getRowIndexBase(), param.getCurrentRowIndex(), numRows)) {
				processRow((FormMaster) sourceDirectory.getFormMaster(), param.getSelectedQuestionIndexSet(),
						sourceDirectory, param.getSelectedTableIndex(), tableIndex, param.getSelectedRowIndex()+1,
						rowIndex);//CHECK: COMPARE THIS LINE with repository HEAD!
				param.setCurrentRowIndex(-1);
			} else {
				param.setSelectedRowIndex(0);
				param.setRowIndexBase(param.getRowIndexBase()+numRows);
				param.setSelectedTableIndex(param.getSelectedTableIndex()+1);
				tableIndex++;
				return;
			}
		}
	}

	private boolean isRowInSelectedTable(int rowIndexBase, int selectedRowIndex, int numRows) {
		return rowIndexBase <= selectedRowIndex && selectedRowIndex < rowIndexBase + numRows;
	}

	boolean isOverwrapped(int aMin, int aMax, int bMin, int bMax) {
		return ((bMin <= aMax && aMin <= bMax) || (aMin <= bMax && bMin <= aMax));
	}

	private void processAllRows() {
		int processedTableIndex = 0;
		for (SourceDirectory sourceDirectory : this.contentSelection.getSelectedSourceDirectoryList()) {
			processAllRows(sourceDirectory);
			processedTableIndex++;
		}
	}

	private void processAllRows(SourceDirectory sourceDirectory) {
		List<PageID> pageIDList = sourceDirectory.getPageIDList();
		int numRows = pageIDList.size() / sourceDirectory.getFormMaster().getNumPages();
		for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
			processRow(rowIndex);
		}
	}

	abstract void processRow(int rowIndex);

}
