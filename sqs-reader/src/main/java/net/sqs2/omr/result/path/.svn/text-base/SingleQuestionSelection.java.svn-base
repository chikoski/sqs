/**
 * 
 */
package net.sqs2.omr.result.path;

import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;

public class SingleQuestionSelection{
	private List<FormArea> formAreaList;
	
	public SingleQuestionSelection(SingleQuestionPath singleQuestionPath){
		this(singleQuestionPath.getFormMaster(), singleQuestionPath.getQuestionIndex());
	}
	
	public SingleQuestionSelection(FormMaster formMaster, int questionIndex){
		this.formAreaList = formMaster.getFormAreaList(questionIndex);
	}
	
	public List<FormArea> getFormAreaList(){
		return formAreaList;
	}
}