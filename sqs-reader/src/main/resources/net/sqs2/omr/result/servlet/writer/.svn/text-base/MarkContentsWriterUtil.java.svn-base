/**
 * 
 */
package net.sqs2.omr.result.servlet.writer;

import java.io.File;
import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.model.MarkAreaAnswer;
import net.sqs2.omr.model.MarkAreaAnswerItem;
import net.sqs2.omr.model.PageID;
import net.sqs2.util.StringUtil;

public class MarkContentsWriterUtil {

	protected static final char ITEM_SEPARATOR = ',';
	
	public static String createRowMemberFilenames(int rowIndex, int numPages, List<PageID> pageIDList) {
		boolean separator = false;
		StringBuilder filenames = new StringBuilder();
		for (int pageIndex = 0; pageIndex < numPages; pageIndex++) {
			PageID pageID = pageIDList.get(rowIndex * numPages + pageIndex);
			if (separator) {
				filenames.append(ITEM_SEPARATOR);
			} else {
				separator = true;
			}
			filenames.append(StringUtil.escapeTSV(new File(pageID.getFileResourceID().getRelativePath())
					.getName()));
		}
		return filenames.toString();
	}

	@SuppressWarnings("unused")
	private int getMarkAreaAnswer(List<FormArea> formAreaList, MarkAreaAnswer markAreaAnswer, float threshold) {
		MarkAreaAnswerItem[] itemArray = markAreaAnswer.getMarkAreaAnswerItemArray();
		if (markAreaAnswer.isManualMode()) {
			for (MarkAreaAnswerItem item : itemArray) {
				if (item.isManualSelected()) {
					return item.getItemIndex();
				}
			}
			return -1;
		} else {
			for (MarkAreaAnswerItem item : itemArray) {
				if (item.getDensity() < threshold) {
					return item.getItemIndex();
				}
			}
			return -1;
		}
	}
}