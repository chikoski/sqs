/**
 * 
 */
package net.sqs2.omr.result.contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sqs2.omr.httpd.JSONUtil;
import net.sqs2.omr.logic.MarkAreaPreviewImageConstants;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.result.servlet.ResultBrowserServletParam;
import net.sqs2.omr.source.SessionSource;

public class MasterJSONFactory extends AbstractJSONFactory{
	
	public MasterJSONFactory(SessionSource sessionSource){
		super(sessionSource);
	}
	
	public String create(ResultBrowserServletParam param){
		ArrayList<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(PageMaster master: this.sessionSource.getSessionSourceContentIndexer().getPageMasterList()){
			HashMap<String,Object> map =new HashMap<String,Object>(); 
			map.put("text", master.getRelativePath());
			map.put("icon", "file_acrobat.gif");
			map.put("numPages", master.getNumPages());
			map.put("markAreaMarginHorizontal", MarkAreaPreviewImageConstants.HORIZONTAL_MARGIN);
			map.put("markAreaMarginVertical", MarkAreaPreviewImageConstants.VERTICAL_MARGIN);
			list.add(map);
		}
		StringBuilder sb = new StringBuilder();
		JSONUtil.printJSON(sb, list);
		return sb.toString();
	}
}