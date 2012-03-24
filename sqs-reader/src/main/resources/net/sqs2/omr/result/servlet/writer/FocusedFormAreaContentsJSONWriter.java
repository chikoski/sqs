/**
 * 
 */
package net.sqs2.omr.result.servlet.writer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sqs2.omr.config.SourceConfig;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.MarkAreaAnswer;
import net.sqs2.omr.model.PageAreaResult;
import net.sqs2.omr.model.Row;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.TextAreaAnswer;
import net.sqs2.omr.result.context.ResultBrowserContext;

public class FocusedFormAreaContentsJSONWriter extends FormAreaContentsJSONWriter {
	String prevSourceDirectoryPath = null;
	int numPrintedAnswerItems = 0;
	int pageStartAnswerIndex;
	int numMaxPrintableAnswerItems;
	
	public FocusedFormAreaContentsJSONWriter(PrintWriter w, ResultBrowserContext contentSelection) throws IOException {
		super(w, contentSelection);
		this.pageStartAnswerIndex = pageStartAnswerIndex;
		this.numMaxPrintableAnswerItems = numMaxPrintableAnswerItems;
		this.isMSIE = isMSIE;
	}

	protected void incrementNumPrintedAnswerItems(boolean isPrintable, boolean isInRange, int rowIndex, int numColumns, int columnIndex) {
		this.numPrintedAnswerItems++;
	}

	boolean isPrintableAnswerItem(float densityThreshold, Row row, int tableIndex, int rowIndex, int columnIndex) {
		return true;
	}

	boolean hasPageStartAnswerIndexPrinted = false;

	@Override
	boolean isSkippableTable(int numColumnsSelected, int rowIndexBase, int numRows) {
		return this.pageStartAnswerIndex != 0 && !isFocusedTable(numColumnsSelected, rowIndexBase, numRows);
	}

	@Override
	public void create(FormMaster master, Set<Integer> selectedTableIndexSet, Set<Integer> selectedRowIndexSet, Set<Integer> selectedQuestionIndexSet) {
		this.w.print("contentsDispatcher.answerItemSource = [");// open master
		this.w.println("[");// open first table
		super.create(master, selectedTableIndexSet, selectedRowIndexSet, selectedQuestionIndexSet);
		this.w.println("]");// close last table
		this.w.println("];");// close master
		
		List<Integer> pageStartIndexArray = createPageStartIndexArray(selectedRowIndexSet, selectedQuestionIndexSet);

		this.w.print("contentsDispatcher.pageStartIndexArray = [");
		for (int index = 0; index < pageStartIndexArray.size() ; index++) {
			if (index != 0) {
				this.w.print(",");
			}
			this.w.print(pageStartIndexArray.get(index));
		}
		this.w.println("];");
		
		/*
		 * if(this.numPrintedAnswerItems % param.getNumMaxAnswerItems() == 0){
		 * int cellIndex = numColumns * rowIndex + columnIndex;
		 * this.pageStartIndexList.add(cellIndex); }
		 * 
		 * if(this.hasPageStartIndexPrinted){ this.w.print(','); }else{
		 * this.hasPageStartIndexPrinted = true; } this.w.print(pageStartIndex);
		 * }
		 */
	}
	
	List<Integer> createPageStartIndexArray(Set<Integer> selectedRowIndexSet, Set<Integer> selectedQuestionIndexSet){
		int numRows = selectedRowIndexSet.size();
		int numQuestions = selectedQuestionIndexSet.size();
		int numCells = numRows * numQuestions;
		List<Integer> ret = new ArrayList<Integer>();
		for (int cellIndex = 0; cellIndex < numCells; cellIndex += this.numMaxPrintableAnswerItems) {
			ret.add(cellIndex);
		}
		return ret;
	}

	boolean isFocusedTable(int numColumnsSelected, int rowIndexBase, int numRows) {
		if (numColumnsSelected == 0) {
			return false;
		}
		int answerRowIndexStart = this.pageStartAnswerIndex / numColumnsSelected;
		int answerRowIndexEnd = (this.pageStartAnswerIndex + this.numMaxPrintableAnswerItems - 1) / numColumnsSelected;
		return isOverwrapped(rowIndexBase, rowIndexBase + numRows, answerRowIndexStart, answerRowIndexEnd);
	}

	boolean isFocusedRow(Set<Integer> selectedQuestionIndexSet, int rowIndex) {
		int numColumnsSelected = selectedQuestionIndexSet.size();
		if (numColumnsSelected == 0) {
			return false;
		}
		int answerRowIndexStart = this.pageStartAnswerIndex / numColumnsSelected;
		int answerRowIndexEnd = (this.pageStartAnswerIndex + this.numMaxPrintableAnswerItems - 1) / numColumnsSelected;
		return answerRowIndexStart <= rowIndex && rowIndex <= answerRowIndexEnd;
	}

	boolean isFocusedColumn(Set<Integer> selectedQuestionIndexSet, int selectedRowIndex, int selectedColumnIndex) {
		int numColumnsSelected = selectedQuestionIndexSet.size();
		int answerItemPageIndex = numColumnsSelected * selectedRowIndex + selectedColumnIndex;
		int answerItemPageIndexStart = this.pageStartAnswerIndex;
		int answerItemPageIndexEnd = answerItemPageIndexStart + this.numMaxPrintableAnswerItems - 1;
		return answerItemPageIndexStart <= answerItemPageIndex && answerItemPageIndex <= answerItemPageIndexEnd;
	}

	@Override
	void processRow(FormMaster master, Set<Integer> selectedQuestionIndexSet, SourceDirectory sourceDirectory, int selectedTableIndex, int tableIndex, int selectedRowIndex, int rowIndex) {

		if (this.prevSourceDirectoryPath != null
				&& !this.prevSourceDirectoryPath.equals(sourceDirectory.getRelativePath())) {
			this.w.print("],[");// close prev table, then open the next table
			this.hasStartedRow = false;
		}
		this.prevSourceDirectoryPath = sourceDirectory.getRelativePath();

		printRowSeparator();

		int masterIndex = 0;// TODO: master.getMasterMetadata().getIndex()

		if (!isFocusedRow(selectedQuestionIndexSet, selectedRowIndex)) {
			this.w.print("null");
			return;
		}

		Row row = (Row) this.rowAccessor.get(master.getRelativePath(), sourceDirectory.getRelativePath(), rowIndex);

		List<PageAreaResult> pageAreaResultListParRow = ContentsWriterUtil
				.createPageAreaResultListParRow(master, sourceDirectory, this.pageTaskAccessor, rowIndex);

		if (isErrorRow(master, row, pageAreaResultListParRow)) {
			this.w.print("null");
			return;
		}

		startRow();

		boolean hasColumnPrinted = false;
		int formAreaIndex = 0;
		int selectedColumnIndex = 0;
		float densityThreshold = ((SourceConfig)sourceDirectory.getConfiguration().getConfig().getPrimarySourceConfig())
				.getMarkRecognitionConfig().getDensity();

		for (int columnIndex = 0; columnIndex < master.getNumQuestions(); columnIndex++) {
			List<FormArea> formAreaList = master.getFormAreaList(columnIndex);
			// boolean isInFocusRange = isInFocusRange(param, rowIndex,
			// master.getNumColumns(), columnIndex);

			if (!selectedQuestionIndexSet.contains(columnIndex)) {
				formAreaIndex += formAreaList.size();
				continue;
			}

			if (hasColumnPrinted) {
				this.w.print(',');
			} else {
				hasColumnPrinted = true;
			}

			boolean isFocusedColumn = isFocusedColumn(selectedQuestionIndexSet, selectedRowIndex,
					selectedColumnIndex);
			if (isFocusedColumn) {
				boolean isPrintableAnswerItem = isPrintableAnswerItem(densityThreshold, row, tableIndex, rowIndex, columnIndex);
				incrementNumPrintedAnswerItems(isPrintableAnswerItem, isFocusedColumn, rowIndex, 
						master.getNumQuestions(), columnIndex);
				FormArea primaryFormArea = formAreaList.get(0);

				if (primaryFormArea.isMarkArea()) {
					writeMarkAreaAnswer((MarkAreaAnswer) row.getAnswer(columnIndex),
							pageAreaResultListParRow, formAreaIndex, createQueryParamString(masterIndex,
									tableIndex, rowIndex, columnIndex), densityThreshold);
				} else if (primaryFormArea.isTextArea()) {
					writeTextAreaAnswer((TextAreaAnswer) row.getAnswer(columnIndex),
							pageAreaResultListParRow, formAreaIndex, createQueryParamString(masterIndex,
									tableIndex, rowIndex, columnIndex));
				} else {
					throw new RuntimeException(primaryFormArea.getType());
				}
			} else {
				this.w.print("null");
			}
			formAreaIndex += formAreaList.size();
			selectedColumnIndex++;
		}
		endRow();
	}
}
