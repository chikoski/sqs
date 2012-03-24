/**
 * 
 */
package net.sqs2.omr.result.path;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.util.URLSafeRLEBase64;

public class MultiQuestionSelection{
	private List<List<FormArea>> formAreaListList;
	
	public MultiQuestionSelection(MultiQuestionPath multiQuestionPath){
		this(multiQuestionPath.getFormMaster(), multiQuestionPath.getQuestionSelection());
	}
	
	public MultiQuestionSelection(FormMaster formMaster, URLSafeRLEBase64.MultiSelection questionSelection){
		TreeSet<Integer> questionIndexSet = questionSelection.getSelectedIndexTreeSet();
		formAreaListList = new ArrayList<List<FormArea>>(questionIndexSet.size());
		for(int questionIndex: questionIndexSet){
			formAreaListList.add(formMaster.getFormAreaList(questionIndex));
		}
	}
	
	public List<List<FormArea>> getFormAreaListList(){
		return formAreaListList;
	}
}