package net.sqs2.omr.result.servlet.writer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import net.sqs2.omr.model.Row;
import net.sqs2.omr.result.export.model.MarkAreaErrorModel;
import net.sqs2.omr.result.export.model.MarkAreasTableModel;
import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.omr.session.service.MarkReaderSessions;
import net.sqs2.omr.source.SessionSource;

public class ErrorFormAreaContentsJSONWriter extends FocusedFormAreaContentsJSONWriter {

	boolean checkNoAnswer = false;
	boolean checkMultiAnswer = false;
	int numPrintedAnswerItems = 0;

	TreeSet<Integer> errorItemIndexSet = new TreeSet<Integer>();
	List<Integer> startItemIndexArray = new ArrayList<Integer>();

	public ErrorFormAreaContentsJSONWriter(PrintWriter w, SessionSource sessionSource, int answerItemIndex,
			int numMaxAnswerItems, boolean isMSIE, boolean checkNoAnswer, boolean checkMultiAnswer)
			throws IOException {
		super(w, sessionSource, answerItemIndex, numMaxAnswerItems, isMSIE);
		this.checkNoAnswer = checkNoAnswer;
		this.checkMultiAnswer = checkMultiAnswer;
	}

	@Override
	protected void incrementNumPrintedAnswerItems(boolean isPrintable, boolean isInRange, int rowIndex, int numColumns, int columnIndex) {
		if(isPrintable){
			int cellIndex = numColumns * rowIndex + columnIndex;
			this.errorItemIndexSet.add(cellIndex);
			
			if(this.numPrintedAnswerItems % numMaxPrintableAnswerItems == 0){
				startItemIndexArray.add(cellIndex);
			}
			this.numPrintedAnswerItems++;
		}
	}
	
	@Override
	protected List<Integer> createPageStartIndexArray(Set<Integer> selectedRowIndexSet, Set<Integer> selectedQuestionIndexSet){
		return this.startItemIndexArray;
	}

	@Override
	boolean isPrintableAnswerItem(float densityThreshold, Row row, int tableIndex, int rowIndex, int columnIndex) {
		MarkReaderSession markReaderSession = MarkReaderSessions.get(this.sessionSource.getSessionID());
		MarkAreaErrorModel model = markReaderSession.getMarkAreaErrorModel();
		MarkAreasTableModel noAnswerMarkAreasTableModel = model.getNoAnswerMarkAreasTableModel();
		MarkAreasTableModel multiAnswerMarkAreasTableModel = model.getMultipleAnswersMarkAreasTableModel();
		boolean isNoAnswer = null != noAnswerMarkAreasTableModel.getCell(tableIndex, rowIndex, columnIndex);
		boolean isMultiAnswer = null != multiAnswerMarkAreasTableModel.getCell(tableIndex, rowIndex, columnIndex);
		boolean ret = isNoAnswer || isMultiAnswer;
		if (ret){
			Logger.getLogger(getClass().getName()).info("printable:"+tableIndex+","+rowIndex+","+columnIndex);
		}
		return ret;
	}
	
	@Override
	boolean isFocusedTable(int numColumnsSelected, int rowIndexBase, int numRows) {
		if (numColumnsSelected == 0) {
			return false;
		}
		if(this.numMaxPrintableAnswerItems <= this.numPrintedAnswerItems){
			return false;
		}

		int answerRowIndexStart = this.pageStartAnswerIndex / numColumnsSelected;
		int aMax = rowIndexBase + numRows;		
		return (answerRowIndexStart <= aMax);
	}

	@Override
	boolean isFocusedRow(Set<Integer> selectedQuestionIndexSet, int rowIndex) {
		int numColumnsSelected = selectedQuestionIndexSet.size();
		if (numColumnsSelected == 0) {
			return false;
		}
		if(this.numMaxPrintableAnswerItems <= this.numPrintedAnswerItems){
			return false;
		}
		int answerRowIndexStart = this.pageStartAnswerIndex / numColumnsSelected;
		return answerRowIndexStart <= rowIndex;
	}

	@Override
	boolean isFocusedColumn(Set<Integer> selectedQuestionIndexSet, int selectedRowIndex, int selectedColumnIndex) {
		if(this.numMaxPrintableAnswerItems <= this.numPrintedAnswerItems){
			return false;
		}
		int numColumnsSelected = selectedQuestionIndexSet.size();
		int answerItemPageIndex = numColumnsSelected * selectedRowIndex + selectedColumnIndex;
		int answerItemPageIndexStart = this.pageStartAnswerIndex;
		return answerItemPageIndexStart <= answerItemPageIndex;
	}

}
