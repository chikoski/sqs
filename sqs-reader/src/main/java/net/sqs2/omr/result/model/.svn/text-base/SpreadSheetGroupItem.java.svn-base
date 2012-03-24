package net.sqs2.omr.result.model;

import java.util.List;

import net.sqs2.omr.model.SourceDirectory;

public class SpreadSheetGroupItem extends ModelItemMap {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SpreadSheetGroupItem(List<SourceDirectory> folderList, int depth, int indexBase){
		put(Label.SpreadSheetGroup.LABEL, "depth" + depth);
		put(Label.ICON, ImageFileName.SPREADSHEET_DEPTH_CATEGORY);
		put(Label.SpreadSheetGroup.ITEMS, createSpreadSheetEntryList(folderList, indexBase));
	}
	
	private SpreadSheetItemList createSpreadSheetEntryList(List<SourceDirectory> sourceDirectoryList, int indexBase) {
		SpreadSheetItemList ret = new SpreadSheetItemList();
		for (SourceDirectory sourceDirectory : sourceDirectoryList) {
			ret.add(new SpreadSheetItem(sourceDirectory));
		}
		return ret;
	}

}
