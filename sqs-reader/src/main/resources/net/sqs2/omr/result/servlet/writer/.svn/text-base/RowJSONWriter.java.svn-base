/**
 * 
 */
package net.sqs2.omr.result.servlet.writer;

import java.io.File;
import java.util.List;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.result.context.ResultBrowserContext;
import net.sqs2.util.Resource;

public class RowJSONWriter extends TableJSONWriter {

	public RowJSONWriter(Resource resource) {
		super(resource);
	}

	@Override
	public String create(ResultBrowserContext contentSelection){
		int tableSelectionArraySize = contentSelection.getSelectedSourceDirectoryList().size();
		if (contentSelection.getFormMaster() == null || 0 == tableSelectionArraySize) {
			return "rHandler.updateOptions([]);";
		}
		FormMaster master = (FormMaster) contentSelection.getFormMaster();
		List<SourceDirectory> sourceDirectoryListDepthOrdered = contentSelection.getSessionSource()
				.getContentIndexer().getSourceDirectoryDepthOrderedListMap().get(master);
		StringBuilder sb = new StringBuilder();
		sb.append("rHandler.updateOptions([");
		boolean hasSomeTables = false;
		int rowIndexBase = 0;
		for (int tableIndex = 0; tableIndex < tableSelectionArraySize; tableIndex++) {
			if (hasSomeTables) {
				sb.append(",\n");
			} else {
				hasSomeTables = true;
			}
			SourceDirectory sourceDirectory = sourceDirectoryListDepthOrdered.get(tableIndex);
			appendTableAsJSON(sb, sourceDirectory, tableIndex, rowIndexBase);
			rowIndexBase += sourceDirectory.getPageIDList().size();
		}
		sb.append("]);");
		if (rowIndexBase == 0) {
			return "rHandler.updateOptions([]);";
		}
		return sb.toString();
	}

	private void appendTableAsJSON(StringBuilder sb, SourceDirectory sourceDirectory, int tableIndex, int rowIndexBase) {
		sb.append('{');

		sb.append("'optgroup':'");
		sb.append((File.separatorChar + sourceDirectory.getRelativePath()).replace("\\", "\\\\"));
		sb.append("','icon':'");
		if (sourceDirectory.isLeaf()) {
			sb.append("dir.gif");
		} else {
			sb.append("dir0.gif");
		}
		sb.append("','items':[");

		List<PageID> pageIDList = sourceDirectory.getPageIDList();
		int numPages = sourceDirectory.getFormMaster().getNumPages();
		boolean hasSomeRows = false;
		for (int rowIndex = 0; rowIndex < pageIDList.size() / numPages; rowIndex++) {
			if (hasSomeRows) {
				sb.append(",\n");
			} else {
				hasSomeRows = true;
			}
			appendRowAsJSON(sb, pageIDList, numPages, tableIndex, rowIndexBase, rowIndex);
		}
		sb.append("]}");
	}

	private void appendRowAsJSON(StringBuilder sb, List<PageID> pageIDList, int numPages, int tableIndex, int rowIndexBase, int rowIndex) {
		sb.append("{");
		sb.append("'items':[");
		boolean hasPrintPageID = false;
		for (int pageIndex = 0; pageIndex < numPages; pageIndex++) {
			if (hasPrintPageID) {
				sb.append(",");
			}
			sb.append("'");
			sb.append(new File(pageIDList.get(pageIndex + (rowIndex * numPages)).getFileResourceID()
					.getRelativePath()).getName());
			sb.append("'");
			hasPrintPageID = true;
		}
		sb.append("]");

		sb.append(",");
		sb.append("'icon':'user.png'");
		sb.append(",");
		sb.append("'t':");
		sb.append(tableIndex);
		sb.append("}");
	}

}
