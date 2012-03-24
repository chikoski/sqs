/**
 * 
 */
package net.sqs2.omr.result.servlet.writer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.AbstractPageTask;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTaskAccessor;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.result.context.ResultBrowserContext;

public class ErrorPageContentsJSONWriter extends SimpleContentsWriter {
	protected PrintWriter w;
	boolean hasRowPrinted = false;

	public ErrorPageContentsJSONWriter(PrintWriter w, ResultBrowserContext contentSelection) throws IOException {
		super(contentSelection);
		this.w = w;
	}

	@Override
	public void create() {
		this.w.append("contentsDispatcher.errorPageSource = [");
		super.create(master, selectedTableIndexSet, selectedRowIndexSet, selectedQuestionIndexSet);
		this.w.println("];");
	}

	@Override
	void processRow(FormMaster master, Set<Integer> selectedQuestionIndexSet, SourceDirectory sourceDirectory, int selectedTableIndex, int tableIndex, int selectedRowIndex, int rowIndex) {

		if (this.hasRowPrinted) {
			this.w.print(',');
		} else {
			this.hasRowPrinted = true;
		}

		PageTaskAccessor pageTaskAccessor = this.sessionSource.getContentAccessor().getPageTaskAccessor();
		int pageIDIndex = 0;
		List<PageID> pageIDList = sourceDirectory.getPageIDList();

		this.w.print('[');

		for (int pageIndex = 0; pageIndex < master.getNumPages(); pageIndex++) {
			int index = master.getNumPages() * rowIndex + pageIndex;
			PageID pageID = pageIDList.get(index);
			AbstractPageTask pageTask = pageTaskAccessor.get(master, pageIndex + 1, pageID);
			PageTaskError pageTaskError = pageTask.getPageTaskException();
				if (0 < pageIndex) {
				this.w.print(',');
			}
			if (pageTaskError != null) {
				Logger.getLogger(getClass().getName()).info(pageID + "\t" + pageTaskError.getLocalizedMessage());
				this.w.print('\'');
				this.w.print(pageTaskError.getLocalizedMessage());
				this.w.print('\'');
			} else {
				this.w.print("null");
			}
				pageIDIndex++;
		}
		this.w.print(']');
	}
}
