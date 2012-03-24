package net.sqs2.omr.result.path;

import java.util.List;

import net.sqs2.omr.master.FormArea;

public class MultiRowSingleQuestionSelection extends MultiRowSelection {

	List<FormArea> formAreaList;
	
	public MultiRowSingleQuestionSelection(MultiRowSingleQuestionPath multiRowSingleQuestionPath) {
		super(multiRowSingleQuestionPath);
		int questionIndex = multiRowSingleQuestionPath.getQuestionIndex();
		formAreaList = formMaster.getFormAreaList(questionIndex);
	}

	public MultiRowSingleQuestionPath getMultiRowSingleQuestionPath(){
		return (MultiRowSingleQuestionPath)masterPath;
	}

	public List<FormArea> getFormAreaList() {
		return formAreaList;
	}
	
	public FormArea getPrimaryFormArea() {
		return formAreaList.get(0);
	}

}
