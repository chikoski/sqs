package net.sqs2.omr.result.path;

import java.util.List;

import net.sqs2.omr.master.FormArea;

public class SingleRowSingleQuestionSelection extends SingleRowSelection {

	List<FormArea> formAreaList;
	
	public SingleRowSingleQuestionSelection(SingleRowSingleQuestionPath singleRowSingleQuestionPath) {
		super(singleRowSingleQuestionPath);
		formAreaList = formMaster.getFormAreaList(singleRowSingleQuestionPath.getQuestionIndex());
	}
	
	public SingleRowSingleQuestionPath getSingleRowSingleQuestionPath(){
		return (SingleRowSingleQuestionPath)masterPath;
	}

}
