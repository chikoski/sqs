package net.sqs2.omr.result.path;

import java.util.List;

import net.sqs2.omr.master.FormArea;



public class MultiRowMultiQuestionSelection extends MultiRowSelection {
	
	int numSelected;
	
	MultiQuestionSelection multiQuestionSelection;
	
	public MultiRowMultiQuestionSelection(MultiRowMultiQuestionPath multiRowMultiQuestionPath) {
		super(multiRowMultiQuestionPath);
		multiQuestionSelection = new MultiQuestionSelection(formMaster, multiRowMultiQuestionPath.getQuestionSelection());
		int numSelectedRows = multiRowMultiQuestionPath.getRowSelection().getSelectedIndexTreeSet().size();
		int numSelectedQuestions = multiRowMultiQuestionPath.getQuestionSelection().getSelectedIndexTreeSet().size();
		this.numSelected = numSelectedRows * numSelectedQuestions;
	}

	public MultiRowMultiQuestionPath getMultiRowMultiQuestionPath(){
		return (MultiRowMultiQuestionPath)masterPath;
	}

	public MultiQuestionSelection getMultiQuestionSelection() {
		return this.multiQuestionSelection;
	}
	
	public List<List<FormArea>> getFormAreaListList(){
		return this.multiQuestionSelection.getFormAreaListList();
	}
	
	public int getNumSelected(){
		return this.numSelected;
	}
	
}
