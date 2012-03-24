/**
 * 
 */
package net.sqs2.omr.result.contents.chart;

import java.util.List;

import net.sqs2.omr.master.FormArea;

public class Legend{
	int questionIndex;
	List<FormArea> formAreaList;
	FormArea defaultFormArea;
	public Legend(int questionIndex, List<FormArea> formAreaList){
		this.questionIndex = questionIndex;
		this.formAreaList = formAreaList;
		this.defaultFormArea = formAreaList.get(0);
	}
	public int getQuestionIndex() {
		return questionIndex;
	}
	public List<FormArea> getFormAreaList() {
		return formAreaList;
	}
	public FormArea getDefaultFormArea() {
		return defaultFormArea;
	}
	
}