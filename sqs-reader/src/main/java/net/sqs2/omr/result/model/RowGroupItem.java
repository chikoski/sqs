package net.sqs2.omr.result.model;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.SpreadSheet;

public class RowGroupItem extends ModelItemMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RowGroupItem(SpreadSheet spreadSheet) {

		put(Label.RowGroup.LABEL, (File.separatorChar + spreadSheet.getSourceDirectory().getRelativePath()).replace("\\", "\\\\"));

		if (spreadSheet.getSourceDirectory().isLeaf()) {
			put(Label.RowGroup.ICON, ImageFileName.LEAF_DIRECTORY);
		} else {
			put(Label.RowGroup.ICON, ImageFileName.BRANCH_DIRECTORY);
		}
		PageItemList items = new PageItemList();
		put(Label.RowGroup.ITEMS, items);

		List<PageID> pageIDList = spreadSheet.getPageIDList();
		int numPages = spreadSheet.getFormMaster().getNumPages();

		for (int rowIndex = 0; rowIndex < pageIDList.size() / numPages; rowIndex++) {
			items.add(new RowItem(pageIDList, numPages, spreadSheet.getSourceDirectory().getRelativePath(), rowIndex));
		}
	}
}
