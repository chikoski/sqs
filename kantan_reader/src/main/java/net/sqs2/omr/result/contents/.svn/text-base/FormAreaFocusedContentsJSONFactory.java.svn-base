/**
 * 
 */
package net.sqs2.omr.result.contents;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.model.MarkAreaAnswer;
import net.sqs2.omr.result.model.Row;
import net.sqs2.omr.result.model.TextAreaAnswer;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.task.PageAreaCommand;

public class FormAreaFocusedContentsJSONFactory extends FormAreaContentsJSONFactory{
	String prevSourceDirectoryPath = null;
	int numPrintedAnswerItems = 0;
	int answerItemIndex;
	int numMaxAnswerItems;

	public FormAreaFocusedContentsJSONFactory(PrintWriter w, SessionSource sessionSource,
			int answerItemIndex,
			int numMaxAnswerItems,
			boolean isMSIE)throws IOException{
		super(w, sessionSource);
		this.answerItemIndex = answerItemIndex;
		this.numMaxAnswerItems = numMaxAnswerItems;
		this.isMSIE = isMSIE;
	}

	protected void incrementNumPrintedAnswerItems(boolean isPrintable, boolean isInRange, int rowIndex, int numColumns, int columnIndex){
		/*
		if(this.numPrintedAnswerItems % param.getNumMaxAnswerItems() == 0){
			int cellIndex = numColumns * rowIndex + columnIndex;
			this.pageStartIndexList.add(cellIndex);
		}*/
		this.numPrintedAnswerItems++;
	}
	
	boolean isPrintableAnswerItem(float densityThreshold, Row row, int rowIndex, int columnIndex){
		return true;
	}

	boolean hasPageStartIndexPrinted = false;

	@Override
	boolean isSkipTable(int numColumnsSelected, int rowIndexBase, int numRows){
		return this.answerItemIndex != 0 && ! isFocusedTable(numColumnsSelected, rowIndexBase, numRows);
	}
	
	@Override
	public void create(FormMaster master,
			Set<Integer> selectedTableIndexSet,
			Set<Integer> selectedRowIndexSet,
			Set<Integer> selectedQuestionIndexSet){
		this.w.print("contentsHandler.answerItemSource = [");// open master
		this.w.println("[");// open first table 
		super.create(master,
				selectedTableIndexSet,
				selectedRowIndexSet,
				selectedQuestionIndexSet);
		this.w.println("]");// close last table
		this.w.println("];");// close master
		
		this.w.print("contentsHandler.pageStartIndexArray = [");
		int numRows = selectedRowIndexSet.size();
		int numQuestions = selectedQuestionIndexSet.size();
		int numCells = numRows * numQuestions;
		for(int cellIndex = 0; cellIndex < numCells; cellIndex += this.numMaxAnswerItems){
			if(cellIndex != 0){
				this.w.print(",");
			}
			this.w.print(cellIndex);
		}
		this.w.println("];");

		/*
			if(this.numPrintedAnswerItems % param.getNumMaxAnswerItems() == 0){
				int cellIndex = numColumns * rowIndex + columnIndex;
				this.pageStartIndexList.add(cellIndex);
			}
			
			if(this.hasPageStartIndexPrinted){
				this.w.print(',');
			}else{
				this.hasPageStartIndexPrinted = true;
			}
			this.w.print(pageStartIndex);
		}*/

	}

	/*
	boolean hasStartedTable = false;
	protected void startTable(){
		if(this.hasStartedTable){
			this.w.print(',');
		}else{
			this.hasStartedTable = true;
		}
		this.w.print('[');
	}
	protected void endTable(){
		this.w.print(']');
	}
	*/
	
	boolean isFocusedTable(int numColumnsSelected, int rowIndexBase, int numRows){
		if(numColumnsSelected == 0){
			return false;
		}
		int answerRowIndexStart = this.answerItemIndex / numColumnsSelected;
		int answerRowIndexEnd = (this.answerItemIndex+this.numMaxAnswerItems) / numColumnsSelected;
		return isOverwrapped(rowIndexBase, rowIndexBase + numRows, answerRowIndexStart, answerRowIndexEnd);
	}
	
	boolean isFocusedRow(Set<Integer> selectedQuestionIndexSet, int rowIndex){
		int numColumnsSelected = selectedQuestionIndexSet.size();
		if(numColumnsSelected == 0){
			return false;
		}
		int answerRowIndexStart = this.answerItemIndex / numColumnsSelected;
		int answerRowIndexEnd = (this.answerItemIndex+this.numMaxAnswerItems - 1) / numColumnsSelected;
		//System.err.println(""+answerRowIndexStart +"<="+ rowIndex +" && "+ rowIndex +"<="+ answerRowIndexEnd+" : "+(answerRowIndexStart <= rowIndex && rowIndex <= answerRowIndexEnd));
		return answerRowIndexStart <= rowIndex && rowIndex <= answerRowIndexEnd;
	}
	
	boolean isFocusedColumn(Set<Integer> selectedQuestionIndexSet, int selectedRowIndex, int selectedColumnIndex){
		int numColumnsSelected = selectedQuestionIndexSet.size();
		int answerItemIndex = numColumnsSelected * selectedRowIndex + selectedColumnIndex;
		int answerItemIndexStart = this.answerItemIndex;
		int answerItemIndexEnd = answerItemIndexStart + this.numMaxAnswerItems;
		return answerItemIndexStart <= answerItemIndex && answerItemIndex < answerItemIndexEnd;
	}
	
	void processRow(FormMaster master,
			Set<Integer> selectedQuestionIndexSet, SourceDirectory sourceDirectory, int selectedTableIndex, int tableIndex, int selectedRowIndex, int rowIndex){
		
		if(this.prevSourceDirectoryPath != null && ! this.prevSourceDirectoryPath.equals(sourceDirectory.getPath())){
			this.w.print("],[");//close prev table, open next table
			this.hasStartedRow = false;
		}
		this.prevSourceDirectoryPath = sourceDirectory.getPath(); 
		
		printRowSeparator();
		
		int masterIndex = 0;//TODO: master.getMasterMetadata().getIndex()
		
		if(! isFocusedRow(selectedQuestionIndexSet, selectedRowIndex)){
			this.w.print("null");
			return;
		}

		Row row = (Row)this.rowAccessor.get(master.getRelativePath(), sourceDirectory.getPath(), rowIndex);
		
		List<PageAreaCommand> pageAreaCommandListParRow = ContentsFactoryUtil.createPageAreaCommandListParRow(master, sourceDirectory,
				this.pageTaskAccessor, rowIndex);

		if(isErrorRow(master, row, pageAreaCommandListParRow)){
			this.w.print("null");
			return;
		}
		
		startRow();
		
		boolean hasColumnPrinted = false;
		int formAreaIndex = 0;
		int selectedColumnIndex = 0;
		float densityThreshold = sourceDirectory.getConfiguration().getConfig().getSourceConfig().getMarkRecognitionConfig().getDensity();
		
		for(int columnIndex = 0; columnIndex < master.getNumColumns(); columnIndex++){
			List<FormArea> formAreaList = master.getFormAreaList(columnIndex); 
			//boolean isInFocusRange = isInFocusRange(param, rowIndex, master.getNumColumns(), columnIndex);

			if(! selectedQuestionIndexSet.contains(columnIndex)){
				formAreaIndex += formAreaList.size();
				continue;
			}

			if(hasColumnPrinted){
				this.w.print(',');
			}else{
				hasColumnPrinted = true;
			}
			
			boolean isFocusedColumn = isFocusedColumn(selectedQuestionIndexSet, selectedRowIndex, selectedColumnIndex);
			boolean isPrintableAnswerItem = isPrintableAnswerItem(densityThreshold, row, rowIndex, columnIndex); 
			if(isFocusedColumn){
				incrementNumPrintedAnswerItems(isPrintableAnswerItem, isFocusedColumn, rowIndex, master.getNumColumns(), columnIndex);
				FormArea defaultFormArea = formAreaList.get(0);
				
				if(defaultFormArea.isMarkArea()){
					writeMarkAreaAnswer((MarkAreaAnswer)row.getAnswer(columnIndex),
							pageAreaCommandListParRow,
							formAreaIndex,
							createQueryParamString(masterIndex, tableIndex, rowIndex, columnIndex),
							densityThreshold);
				}else if(defaultFormArea.isTextArea()){
					writeTextAreaAnswer((TextAreaAnswer)row.getAnswer(columnIndex),
							pageAreaCommandListParRow,
							formAreaIndex, 
							createQueryParamString(masterIndex, tableIndex, rowIndex, columnIndex));
				}else{
					throw new RuntimeException(defaultFormArea.getType());
				}
			}else{
				this.w.print("null");
			}
			formAreaIndex += formAreaList.size();
			selectedColumnIndex++;
		}
		endRow();
	}
}
