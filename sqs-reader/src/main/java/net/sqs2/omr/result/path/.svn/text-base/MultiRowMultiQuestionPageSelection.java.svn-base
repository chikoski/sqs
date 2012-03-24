package net.sqs2.omr.result.path;

import java.util.ArrayList;
import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.model.Answer;
import net.sqs2.omr.model.Row;
import net.sqs2.omr.model.RowAccessor;

public class MultiRowMultiQuestionPageSelection extends MultiRowMultiQuestionSelection {

	public static final int NUM_MAX_ITEMS_WITHIN_A_PAGE = 50;
	List<Answer> answerList;
	
	public MultiRowMultiQuestionPageSelection(MultiRowMultiQuestionPagePath multiRowMultiQuestionPagePath) {
		this(multiRowMultiQuestionPagePath, NUM_MAX_ITEMS_WITHIN_A_PAGE);
	}
	
	public MultiRowMultiQuestionPageSelection(MultiRowMultiQuestionPagePath multiRowMultiQuestionPagePath, int numMaxItems) {
		super(multiRowMultiQuestionPagePath);
		
		int startIndex = multiRowMultiQuestionPagePath.getStartIndex();
		int endIndex = multiRowMultiQuestionPagePath.getEndIndex();
		
		RowAccessor rowAccessor = sessionSource.getContentAccessor().getRowAccessor();

		if(0 <= startIndex && endIndex < 0){
			answerList = forwardSearch(rowAccessor, numMaxItems);
		}else if(0 <= endIndex && startIndex < 0){
			answerList = backwardSearch(rowAccessor, numMaxItems);
		}else{
			throw new IllegalArgumentException();
		}
	}
	
	public MultiRowMultiQuestionPagePath getMultiRowMultiQuestionPagePath(){
		return (MultiRowMultiQuestionPagePath)masterPath;
	}

	private ArrayList<Answer> forwardSearch(RowAccessor rowAccessor, int numMaxItems){
		// forward search
		return forwardSearch(rowAccessor, new ArrayList<Answer>(), numMaxItems);	
	}
	
	private ArrayList<Answer> forwardSearch(RowAccessor rowAccessor, ArrayList<Answer> answerList, int numMaxItems){
		// forward search
		int numFocused = 0;
		ITEM_LOOP: for(SelectedRowID rowID: rowIDList){
			Row row = rowAccessor.get(formMaster.getRelativePath(), rowID.getSpreadSheet().getSourceDirectory().getRelativePath(), (int)rowID.getRowIndex());
			for(List<FormArea> formAreaList: getFormAreaListList()){
				if(numFocused < numMaxItems){
					int questionIndex = formAreaList.get(0).getQuestionIndex();
					answerList.add(row.getAnswer(questionIndex));
					numFocused++;
				}else{
					break ITEM_LOOP;
				}
			}
		}
		return answerList;
	}
	
	private ArrayList<Answer> backwardSearch(RowAccessor rowAccessor, int numMaxItems){
		// forward search
		return backwardSearch(rowAccessor, new ArrayList<Answer>(), numMaxItems);	
	}
	
	private ArrayList<Answer> backwardSearch(RowAccessor rowAccessor, ArrayList<Answer> answerList, int numMaxItems){
		// backward search
		int numFocused = 0;
		ITEM_LOOP: for(int rowIndex = rowIDList.size() - 1; 0 <= rowIndex; rowIndex--){
			SelectedRowID rowID = rowIDList.get(rowIndex);
			Row row = rowAccessor.get(formMaster.getRelativePath(), rowID.getSpreadSheet().getSourceDirectory().getRelativePath(), (int)rowID.getRowIndex());
			List<List<FormArea>> formAreaListList = getFormAreaListList();
			for(int columnIndex = formAreaListList.size() - 1; 0 <= columnIndex; columnIndex--){
				 List<FormArea> formAreaList = formAreaListList.get(columnIndex);
				if(numFocused < numMaxItems){
					int questionIndex = formAreaList.get(0).getQuestionIndex();
					answerList.add(row.getAnswer(questionIndex));
					numFocused++;
				}else{
					break ITEM_LOOP;
				}
			}
		}
		return answerList;
	}
}
