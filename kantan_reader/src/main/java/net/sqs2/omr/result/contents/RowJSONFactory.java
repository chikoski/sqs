/**
 * 
 */
package net.sqs2.omr.result.contents;

import java.io.File;
import java.util.List;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.servlet.ResultBrowserServletParam;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.util.Resource;

public class RowJSONFactory extends TableJSONFactory{
	
	public RowJSONFactory(SessionSource sessionSource, Resource resource){
		super(sessionSource, resource);
	}

	@Override
	public String create(ResultBrowserServletParam param){
		if(param.getSelectedMasterIndex() == -1 || 0 == param.getSelectedTableIndexSet().size()){
			return "rHandler.updateOptions([]);";
		}
		FormMaster master = (FormMaster)sessionSource.getSessionSourceContentIndexer().getPageMasterList().get(param.getSelectedMasterIndex());
		List<SourceDirectory> sourceDirectoryListDepthOrdered = this.sessionSource.getSessionSourceContentIndexer().getSourceDirectoryDepthOrderedListMap().get(master);
		StringBuilder sb = new StringBuilder();
		sb.append("rHandler.updateOptions([");
		boolean hasSomeTables = false;
		int rowIndexBase = 0;
		for(int tableIndex : param.getSelectedTableIndexSet()){
			if(hasSomeTables){
				sb.append(",\n");
			}else{
				hasSomeTables = true;
			}
			SourceDirectory sourceDirectory = sourceDirectoryListDepthOrdered.get(tableIndex);
			appendTableAsJSON(sb, sourceDirectory, tableIndex, rowIndexBase);
			rowIndexBase += sourceDirectory.getPageIDList().size();
		}
		sb.append("]);");
		if(rowIndexBase == 0){
			return "rHandler.updateOptions([]);";
		}
		return sb.toString();
	}
	
	private void appendTableAsJSON(StringBuilder sb,
			SourceDirectory sourceDirectory, int tableIndex, int rowIndexBase) {
		sb.append('{');

		sb.append("'optgroup':'");
		sb.append((File.separatorChar+sourceDirectory.getPath()).replace("\\", "\\\\"));
		sb.append("','icon':'");
		if(sourceDirectory.isLeaf()){
			sb.append("dir.gif");
		}else{
			sb.append("dir0.gif");
		}
		sb.append("','items':[");

		List<PageID> pageIDList = sourceDirectory.getPageIDList();
		int numPages = sourceDirectory.getPageMaster().getNumPages();
		boolean hasSomeRows = false;
		for(int rowIndex = 0; rowIndex < pageIDList.size() / numPages; rowIndex++){
			if(hasSomeRows){
				sb.append(",\n");
			 }else{
				 hasSomeRows = true;
			 }
			 appendRowAsJSON(sb, pageIDList, numPages, tableIndex, rowIndexBase, rowIndex);
		}
		sb.append("]}");
	}

	private void appendRowAsJSON(StringBuilder sb, List<PageID> pageIDList,
			int numPages, int tableIndex, int rowIndexBase, int rowIndex) {
		sb.append("{");
		sb.append("'items':[");
		boolean hasPrintPageID = false;
		for(int pageIndex = 0 ; pageIndex < numPages ; pageIndex++){
			if(hasPrintPageID){
				sb.append(",");
			}
			sb.append("'");
			sb.append(new File(pageIDList.get(pageIndex + (rowIndex * numPages)).getFileResourceID().getRelativePath()).getName());
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