/**
 * 
 */
package net.sqs2.omr.result.servlet.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.result.context.ResultBrowserContext;
import net.sqs2.omr.session.logic.MarkAreaPreviewImageConstants;
import net.sqs2.omr.util.JSONUtil;
import net.sqs2.util.Resource;

public class MasterJSONWriter extends AbstractJSONWriter {

	public MasterJSONWriter(Resource resource) {
		super(resource);
	}

	@Override
	public String create(ResultBrowserContext contentSelection){
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (PageMaster master : contentSelection.getSessionSource().getContentIndexer().getFormMasterList()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("text", master.getRelativePath());
			map.put("icon", "file_acrobat.gif");
			map.put("numPages", master.getNumPages());
			map.put("markAreaMarginHorizontal", MarkAreaPreviewImageConstants.HORIZONTAL_MARGIN);
			map.put("markAreaMarginVertical", MarkAreaPreviewImageConstants.VERTICAL_MARGIN);
			list.add(map);
		}
		StringBuilder sb = new StringBuilder();
		JSONUtil.printAsJSON(sb, list);
		return sb.toString();
	}
}
