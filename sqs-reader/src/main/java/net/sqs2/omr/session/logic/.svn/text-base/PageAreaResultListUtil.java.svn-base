package net.sqs2.omr.session.logic;

import java.util.ArrayList;
import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.AbstractPageTask;
import net.sqs2.omr.model.PageAreaResult;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTaskAccessor;
import net.sqs2.omr.model.SourceDirectory;

public class PageAreaResultListUtil {

	public static List<PageAreaResult> createPageAreaResultListParRow(FormMaster master, SourceDirectory sourceDirectory, PageTaskAccessor pageTaskAccessor, int rowIndex) {
		ArrayList<PageAreaResult> pageAreaResultList = new ArrayList<PageAreaResult>();

		for (int pageIndex = 0; pageIndex < master.getNumPages(); pageIndex++) {
			List<FormArea> formAreaList = master.getFormAreaListByPageIndex(pageIndex);
			PageID pageID = sourceDirectory.getPageID(rowIndex * master.getNumPages() + pageIndex);
			AbstractPageTask pageTask = pageTaskAccessor.get(pageIndex + 1, pageID);
			try {
				List<PageAreaResult> pageAreaResultListParPage = pageTask.getPageTaskResult()
						.getPageAreaResultList();
				for (int formAreaIndex = 0; formAreaIndex < formAreaList.size(); formAreaIndex++) {
					PageAreaResult pageAreaResult = pageAreaResultListParPage.get(formAreaIndex);
					pageAreaResultList.add(pageAreaResult);
				}
			} catch (NullPointerException ignore) {
				// TODO ERROR
				ignore.printStackTrace();
			}
		}
		return pageAreaResultList;
	}

	public static List<PageAreaResult> createPageAreaResultListParQuestion(FormMaster master, SourceDirectory rowGroupSourceDirectory, PageTaskAccessor pageTaskAccessor, int rowGroupRowIndex, int columnIndex) {
		List<PageAreaResult> pageAreaResultParQuestion = new ArrayList<PageAreaResult>();
		List<FormArea> formAreaList = master.getFormAreaList(columnIndex);
		int prevPageIndex = -1;
		List<PageAreaResult> pageAreaResultListParPage = null;
		for (FormArea formArea : formAreaList) {
			if (prevPageIndex != formArea.getPageIndex()) {
				PageID pageID = rowGroupSourceDirectory.getPageID(rowGroupRowIndex * master.getNumPages()
						+ formArea.getPageIndex());
				AbstractPageTask pageTask = pageTaskAccessor.get(formArea.getPageIndex() + 1, pageID);
				pageAreaResultListParPage = pageTask.getPageTaskResult().getPageAreaResultList();
			}
			PageAreaResult pageAreaResult = pageAreaResultListParPage.get(formArea.getAreaIndexInPage());
			pageAreaResultParQuestion.add(pageAreaResult);
		}
		return pageAreaResultParQuestion;
	}

}
