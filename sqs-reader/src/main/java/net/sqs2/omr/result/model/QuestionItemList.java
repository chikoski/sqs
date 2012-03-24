package net.sqs2.omr.result.model;

import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.util.StringUtil;

public class QuestionItemList extends FormAreaItemList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QuestionItemList(FormMaster formMaster) {
		for (String qid : formMaster.getQIDSet()) {
			List<FormArea> formAreaList = formMaster.getFormAreaList(qid);
			FormArea area = formAreaList.get(0);
			ModelItemMap areaEntry = new ModelItemMap();
			areaEntry.put(Label.Question.LABEL, area.getLabel());
			areaEntry.put(Label.Question.HINTS, StringUtil.join(area.getHints(), ""));
			areaEntry.put(Label.Question.QUESTION_ID, area.getQID());
			areaEntry.put(Label.Question.TYPE, area.getType());
			areaEntry.put(Label.Question.CLASS, area.getClazz());
			areaEntry.put(Label.Question.ITEMS, createItemList(formAreaList));
			add(areaEntry);
		}
	}

	private FormAreaItemList createItemList(List<FormArea> formAreaList) {
		FormAreaItemList list = new FormAreaItemList(formAreaList.size());
		for (FormArea formArea : formAreaList) {
			list.add(createItem(formArea));
		}
		return list;
	}

	private ModelItemMap createItem(FormArea formArea) {
		ModelItemMap item = new ModelItemMap();
		if (formArea.isMarkArea()) {
			item.put(Label.Question.Item.LABEL, formArea.getItemLabel());
			item.put(Label.Question.Item.VALUE, formArea.getItemValue());
		}
		item.put(Label.Question.Item.CLASS, formArea.getItemClazz());
		item.put(Label.Question.Item.PAGE, formArea.getPage());
		item.put(Label.Question.Item.X, formArea.getRect().x);
		item.put(Label.Question.Item.Y, formArea.getRect().y);
		item.put(Label.Question.Item.WIDTH, formArea.getRect().width);
		item.put(Label.Question.Item.HEIGHT, formArea.getRect().height);
		return item;
	}

}
