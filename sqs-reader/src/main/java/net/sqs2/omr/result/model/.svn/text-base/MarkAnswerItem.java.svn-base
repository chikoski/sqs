package net.sqs2.omr.result.model;

import java.util.List;

import net.sqs2.omr.model.MarkAreaAnswer;
import net.sqs2.omr.model.MarkAreaAnswerItem;
import net.sqs2.omr.model.PageAreaResult;

public class MarkAnswerItem extends FormAreaItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MarkAnswerItem(MarkAreaAnswer markAreaAnswer, List<PageAreaResult> pageAreaResultListParRow, int formAreaIndex, String queryParamString, float densityThreshold, boolean isBase64ImageSourceSupported) {

		MarkAreaAnswerItem[] itemArray = markAreaAnswer.getMarkAreaAnswerItemArray();
		if (markAreaAnswer.isManualMode()) {
			put(Label.MarkAnswer.MANUAL_SELECT_MODE, 1);
		}
		ModelItemList<ModelItemMap> itemMaps = new ModelItemList<ModelItemMap>(); 
		put(Label.MarkAnswer.ITEMS, itemMaps);
		
		int itemIndex = 0;
		int numMarked = 0;
		for (MarkAreaAnswerItem item : itemArray) {
			ModelItemMap itemMap = new ModelItemMap(); 
			if (item == null) {
				itemMap.put(Label.MarkAnswer.MarkItem.MANUAL_DELETED, 1);
				continue;
			}
			if (markAreaAnswer.isManualMode()) {
				if (item.isManualSelected()) {
					itemMap.put(Label.MarkAnswer.MarkItem.MANUAL_SELECTED, 1);
				} else {
					itemMap.put(Label.MarkAnswer.MarkItem.MANUAL_SELECTED, 0);
				}
			}

			PageAreaResult pageAreaResult = pageAreaResultListParRow.get(formAreaIndex);
			if (pageAreaResult != null) {
				if (isBase64ImageSourceSupported) {
					String value = queryParamString+"&i="+itemIndex;
					itemMap.put(Label.MarkAnswer.MarkItem.IMAGE_SOURCE_URI, value);
				} else {
					itemMap.put(Label.MarkAnswer.MarkItem.IMAGE_SOURCE_URI, createBase64ImageSrc(pageAreaResult));
				}
			}
			itemMap.put(Label.MarkAnswer.MarkItem.MARK_DENSITY, item.getDensity());
			if (item.getDensity() < densityThreshold) {
				numMarked++;
			}
			formAreaIndex++;
			itemIndex++;
			itemMaps.add(itemMap);
		}
	}
}
