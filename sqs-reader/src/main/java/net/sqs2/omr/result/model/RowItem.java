package net.sqs2.omr.result.model;

import java.io.File;
import java.util.List;

import net.sqs2.omr.model.PageID;

public class RowItem extends ModelItemMap {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RowItem(List<PageID> pageIDList, long numPages, String sourceDirectoryPath, long rowIndex){
		PageItemList items = new PageItemList();
		put(Label.Row.ITEMS, items);
		for (int pageIndex = 0; pageIndex < numPages; pageIndex++) {
			items.add(new PageItem(new File(pageIDList.get((int)(pageIndex + (rowIndex * numPages))).getFileResourceID()
					.getRelativePath()).getName()));
		}
		put(Label.Row.ICON, ImageFileName.PERSON);
	}
	
}
