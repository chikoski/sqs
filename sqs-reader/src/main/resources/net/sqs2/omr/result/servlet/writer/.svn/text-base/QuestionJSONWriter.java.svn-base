/**
 * 
 */
package net.sqs2.omr.result.servlet.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.result.context.ResultBrowserContext;
import net.sqs2.omr.util.JSONUtil;
import net.sqs2.util.Resource;
import net.sqs2.util.StringUtil;

public class QuestionJSONWriter extends AbstractJSONWriter {

	public QuestionJSONWriter(Resource resource) {
		super(resource);
	}

	@Override
	public String create(ResultBrowserContext contentSelection) {
		List<List<Map<String, Object>>> questionListList = new ArrayList<List<Map<String, Object>>>();
		for (PageMaster pageMaster : contentSelection.getSessionSource().getContentIndexer().getFormMasterList()) {
			List<Map<String, Object>> questionList = new ArrayList<Map<String, Object>>();
			FormMaster formMaster = (FormMaster) pageMaster;
			for (String qid : formMaster.getQIDSet()) {
				List<FormArea> formAreaList = formMaster.getFormAreaList(qid);
				FormArea area = formAreaList.get(0);
				Map<String, Object> areaEntry = new HashMap<String, Object>();
				areaEntry.put("label", area.getLabel());
				areaEntry.put("hints", StringUtil.join(area.getHints(), ""));
				areaEntry.put("qid", area.getQID());
				areaEntry.put("type", area.getType());
				areaEntry.put("clazz", area.getClazz());
				areaEntry.put("items", createItemList(formAreaList));
				questionList.add(areaEntry);
			}
			questionListList.add(questionList);
		}
		StringBuilder sb = new StringBuilder();
		JSONUtil.printAsJSON(sb, questionListList);
		return sb.toString();
	}

	List<Map<String, Object>> createItemList(List<FormArea> formAreaList) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(formAreaList.size());
		for (FormArea formArea : formAreaList) {
			list.add(createItem(formArea));
		}
		return list;
	}

	Map<String, Object> createItem(FormArea formArea) {
		Map<String, Object> item = new HashMap<String, Object>();
		if (formArea.isMarkArea()) {
			item.put("l", formArea.getItemLabel());
			item.put("v", formArea.getItemValue());
		}
		item.put("c", formArea.getItemClazz());
		item.put("p", formArea.getPage());
		item.put("x", formArea.getRect().x);
		item.put("y", formArea.getRect().y);
		item.put("w", formArea.getRect().width);
		item.put("h", formArea.getRect().height);
		return item;
	}
}
