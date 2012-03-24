package net.sqs2.omr.result.contents;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.sqs2.omr.result.model.Answer;
import net.sqs2.omr.result.model.MarkAreaAnswer;
import net.sqs2.omr.result.model.MarkAreaAnswerItem;
import net.sqs2.omr.result.model.Row;
import net.sqs2.omr.source.SessionSource;

public class ErrorFormAreaContentsJSONFactory extends FormAreaFocusedContentsJSONFactory {
	
	boolean checkNoAnswer = false;
	boolean checkMultiAnswer = false;
	
	List<Integer> pageStartIndexList = new ArrayList<Integer>();
	
	public ErrorFormAreaContentsJSONFactory(PrintWriter w, SessionSource sessionSource, 
			int answerItemIndex,
			int numMaxAnswerItems,
			boolean isMSIE, 
			boolean checkNoAnswer,
			boolean checkMultiAnswer)throws IOException{
		super(w, sessionSource, answerItemIndex, numMaxAnswerItems, isMSIE);
		this.checkNoAnswer = checkNoAnswer;
		this.checkMultiAnswer = checkMultiAnswer;
	}

	@Override
	protected void incrementNumPrintedAnswerItems(boolean isPrintable, boolean isInRange, int rowIndex, int numColumns, int columnIndex){
		if(this.numPrintedAnswerItems % this.numMaxAnswerItems == 0){
			int cellIndex = numColumns * rowIndex + columnIndex;
			this.pageStartIndexList.add(cellIndex);
		}
		//if(isPrintable){
			this.numPrintedAnswerItems++;
		//}
	}

	@Override
	boolean isPrintableAnswerItem(float densityThreshold, Row row, int rowIndex, int columnIndex){
		Answer answer = row.getAnswer(columnIndex);
		
		if(answer instanceof MarkAreaAnswer){
			MarkAreaAnswer markAreaAnswer = (MarkAreaAnswer)answer;
			MarkAreaAnswerItem[] itemArray = markAreaAnswer.getMarkAreaAnswerItemArray();
			int numMarked = getNumMarked(densityThreshold, markAreaAnswer.isManualMode(), itemArray);
			if((this.checkNoAnswer && 0 == numMarked) || (this.checkMultiAnswer && 1 < numMarked)){
				return true;
			}
		}
		return false; 
	}

	private int getNumMarked(float densityThreshold, boolean isManualMode, MarkAreaAnswerItem[] itemArray) {
		int numMarked = 0;
		for(MarkAreaAnswerItem item: itemArray){
			if(item == null){
				continue;
			}
			if(isManualMode){
				if(item.isManualSelected()){
					numMarked++;
				}
			}else if(item.getDensity() < densityThreshold){
				numMarked++;
			}
		}
		return numMarked;
	}
	
}
