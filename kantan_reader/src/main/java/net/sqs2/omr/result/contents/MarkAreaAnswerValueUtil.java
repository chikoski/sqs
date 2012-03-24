/**
 * 
 */
package net.sqs2.omr.result.contents;

import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.result.model.MarkAreaAnswer;
import net.sqs2.omr.result.model.MarkAreaAnswerItemSet;

public class MarkAreaAnswerValueUtil{

	public static String createSelect1MarkAreaAnswerValueString(float densityThreshold, float recognitionMargin,
			MarkAreaAnswer markAreaAnswer, List<FormArea> formAreaList, char separator) {
		StringBuilder builder = new StringBuilder();
		
		MarkAreaAnswerItemSet markAreaAnswerItemSet = markAreaAnswer.createMarkAreaAnswerItemSet();
		boolean[] isSelectedBooleanArray = markAreaAnswerItemSet.getIsSelectedBooleanArray(densityThreshold, recognitionMargin);
		
		boolean hasValuePrinted = false;

		for(int itemIndex = 0; itemIndex < formAreaList.size(); itemIndex++){
			boolean isSelected = isSelectedBooleanArray[itemIndex];
			if(isSelected){
				String value = formAreaList.get(itemIndex).getItemValue();
				if(hasValuePrinted){
					builder.append(separator);
				}else{
					hasValuePrinted = true;
				}
				builder.append(value);
			}
		}
		return builder.toString();
	}
	
	/*
	public static String createMarkAreaAnswerValue(float densityThreshold,
			MarkAreaAnswer markAreaAnswer, List<FormArea> formAreaList, char separator) {
		StringBuilder builder = new StringBuilder();
		boolean hasValuePrinted = false;
		
		for(int itemIndex = 0; itemIndex < formAreaList.size(); itemIndex++){
			String value = getMarkAreaAnswerItemValue(densityThreshold, formAreaList, markAreaAnswer, itemIndex);
			if(value != null){
				if(hasValuePrinted){
					builder.append(separator);
				}else{
					hasValuePrinted = true;
				}
				builder.append(value);
			}
		}
		return builder.toString();
	}

	public static String getMarkAreaAnswerItemValue(float densityThreshold,
			List<FormArea> formAreaList, MarkAreaAnswer markAreaAnswer,
			int itemIndex) {
		String value = null;
		MarkAreaAnswerItem item = markAreaAnswer.getMarkAreaAnswerItem(itemIndex);
		if((markAreaAnswer.isManualMode() && item.isManualSelected()) ||
				(! markAreaAnswer.isManualMode() && item.getDensity() < densityThreshold)){
			if(formAreaList.get(itemIndex).isSelect1()){
				value = formAreaList.get(itemIndex).getItemValue();
			}else if(formAreaList.get(itemIndex).isSelect()){
				value = "1";
			}
		}
		return value;
	}
	*/
	
}