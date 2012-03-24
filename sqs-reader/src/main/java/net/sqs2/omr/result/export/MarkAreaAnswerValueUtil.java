/**
 * 
 */
package net.sqs2.omr.result.export;

import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.model.MarkAreaAnswer;
import net.sqs2.omr.model.MarkAreaAnswerItemSet;

public class MarkAreaAnswerValueUtil {

	public static String createSelect1MarkAreaAnswerValueString(float densityThreshold, float doubleMarkSuppressionThreshold, 
			float noMarkSuppressionThreshold, MarkAreaAnswer markAreaAnswer, List<FormArea> formAreaList, char separator) {
		StringBuilder builder = new StringBuilder();

		MarkAreaAnswerItemSet markAreaAnswerItemSet = markAreaAnswer.createMarkAreaAnswerItemSet();
		boolean[] isSelectedBooleanArray = markAreaAnswerItemSet.getIsSelectedBooleanArray(densityThreshold,
				doubleMarkSuppressionThreshold, noMarkSuppressionThreshold );

		boolean hasValuePrinted = false;

		for (int itemIndex = 0; itemIndex < formAreaList.size(); itemIndex++) {
			boolean isSelected = isSelectedBooleanArray[itemIndex];
			if (isSelected) {
				String value = formAreaList.get(itemIndex).getItemValue();
				if (hasValuePrinted) {
					builder.append(separator);
				} else {
					hasValuePrinted = true;
				}
				builder.append(value);
			}
		}
		return builder.toString();
	}

}
