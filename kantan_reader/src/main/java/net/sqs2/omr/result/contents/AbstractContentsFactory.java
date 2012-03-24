package net.sqs2.omr.result.contents;

import java.io.File;
import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.model.MarkAreaAnswer;
import net.sqs2.omr.result.model.MarkAreaAnswerItem;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSources;
import net.sqs2.util.StringUtil;

public class AbstractContentsFactory {

	protected static final char ITEM_SEPARATOR = ',';

	public AbstractContentsFactory() {
		super();
	}
	
	public static class ContentsFactoryUtil2{
		
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
				filenames.append(StringUtil.escapeTSV(new File(pageID.getFileResourceID().getRelativePath()).getName()));
			}
			return filenames.toString();
		}

		public int getMarkAreaAnswer(List<FormArea> formAreaList, MarkAreaAnswer markAreaAnswer, float threshold) {
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

	public FormMaster getSelectedFormMaster(long sessionID, int selectedMasterIndex){
		SessionSource sessionSource = SessionSources.get(sessionID);
		return (FormMaster) sessionSource.getSessionSourceContentIndexer().getPageMasterList().get(selectedMasterIndex);
	}

}
