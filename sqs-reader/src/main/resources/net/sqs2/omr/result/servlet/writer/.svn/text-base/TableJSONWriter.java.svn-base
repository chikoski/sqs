/**
 * 
 */
package net.sqs2.omr.result.servlet.writer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sqs2.omr.config.SourceConfig;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.result.context.ResultBrowserContext;
import net.sqs2.omr.source.ContentIndexer;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.util.JSONUtil;
import net.sqs2.util.Resource;
import net.sqs2.util.StringUtil;

public class TableJSONWriter extends AbstractJSONWriter {

	public TableJSONWriter(Resource resource) {
		super(resource);
	}

	@Override
	public String create(ResultBrowserContext contentSelection){
		List<List<Map<String, Object>>> tableGroupListList = new ArrayList<List<Map<String, Object>>>();
		for (PageMaster master : contentSelection.getSessionSource().getContentIndexer().getFormMasterList()) {
			List<Map<String, Object>> tableGroupList = createTableGroupListByMaster(master,
					contentSelection.getSessionSource());
			tableGroupListList.add(tableGroupList);
		}
		StringBuilder sb = new StringBuilder();
		JSONUtil.printAsJSON(sb, tableGroupListList);
		return sb.toString();
	}

	private List<Map<String, Object>> createTableGroupListByMaster(PageMaster master, SessionSource sessionSource) {
		int maxDepth = -1;
		Map<Integer, List<SourceDirectory>> map = new HashMap<Integer, List<SourceDirectory>>();
		ContentIndexer sessionSourceContentIndexer = sessionSource.getContentIndexer();
		int numSourceDirectories = sessionSourceContentIndexer.getNumSourceDirectoriesInFlattenList(master);
		for (int index = 0; index < numSourceDirectories; index++ ) {
			SourceDirectory sourceDirectory = sessionSourceContentIndexer.getSourceDirectoryFromFlattenList(master, index);	
			int depth = (sourceDirectory.getRelativePath().equals("")) ? 1 : StringUtil.split(
					sourceDirectory.getRelativePath(), File.separatorChar).size() + 1;
			maxDepth = Math.max(depth, maxDepth);
			List<SourceDirectory> list = map.get(depth);
			if (list == null) {
				list = new ArrayList<SourceDirectory>();
				map.put(depth, list);
			}
			list.add(sourceDirectory);
		}
		return createTableGroupListByMaster(map, maxDepth);
	}

	private List<Map<String, Object>> createTableGroupListByMaster(Map<Integer, List<SourceDirectory>> map, int maxDepth) {
		List<Map<String, Object>> folderGroupEntryList = new ArrayList<Map<String, Object>>();
		int indexBase = 0;
		for (int i = 0; i <= maxDepth; i++) {
			List<SourceDirectory> sourceDirectoryList = map.get(i);
			if (sourceDirectoryList != null) {
				folderGroupEntryList.add(createFolderGroupEntry(sourceDirectoryList, i, indexBase));
				indexBase += sourceDirectoryList.size();
			}
		}
		return folderGroupEntryList;
	}

	private Map<String, Object> createFolderGroupEntry(List<SourceDirectory> folderList, int depth, int indexBase) {
		Map<String, Object> folderGroupEntry = new HashMap<String, Object>();
		folderGroupEntry.put("optgroup", "depth" + depth);
		folderGroupEntry.put("icon", "triangle.down.gif");
		folderGroupEntry.put("items", createTableEntryList(folderList, indexBase));
		return folderGroupEntry;
	}

	private List<Map<String, Object>> createTableEntryList(List<SourceDirectory> sourceDirectoryList, int indexBase) {
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		for (SourceDirectory sourceDirectory : sourceDirectoryList) {
			ret.add(createTableEntry(sourceDirectory));
		}
		return ret;
	}

	private Map<String, Object> createTableEntry(SourceDirectory sourceDirectory) {
		Map<String, Object> folderEntry = new HashMap<String, Object>();
		if (sourceDirectory.isLeaf()) {
			folderEntry.put("icon", "dir.gif");
		} else {
			folderEntry.put("icon", "dir0.gif");
		}
		folderEntry.put("text", File.separatorChar + sourceDirectory.getRelativePath());
		float densityThreshold = ((SourceConfig)sourceDirectory.getConfiguration().getConfig().getPrimarySourceConfig())
				.getMarkRecognitionConfig().getDensity();
		folderEntry.put("densityThreshold", densityThreshold);
		if (!sourceDirectory.isLeaf()) {
			folderEntry.put("disabled", "true"); // FIXME! remove this line when
			// item selection relation
			// has implemented
		}
		return folderEntry;
	}
}
