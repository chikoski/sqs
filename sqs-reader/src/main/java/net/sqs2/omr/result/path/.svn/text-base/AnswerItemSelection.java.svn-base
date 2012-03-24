package net.sqs2.omr.result.path;

import net.sqs2.omr.master.FormArea;

public class AnswerItemSelection extends SingleRowSingleQuestionSelection {

	FormArea formArea;
	
	public AnswerItemSelection(AnswerItemPath answerItemPath){
		super(answerItemPath);
		formArea = formMaster.getFormAreaList(answerItemPath.getQuestionIndex()).get(answerItemPath.getAnswerItemIndex());
	}
	
	public AnswerItemPath getAnswerItemPath(){
		return (AnswerItemPath)masterPath;
	}
	
	public FormArea getFormArea(){
		return formArea;
	}
}
