package net.sqs2.omr.result.servlet.writer;

import java.io.IOException;
import java.io.PrintWriter;

import net.sqs2.omr.result.context.ResultBrowserContext;
import net.sqs2.util.Resource;

public class StatisticsContentsJSONWriter extends StatisticsContentsWriter {
	protected PrintWriter w;

	public StatisticsContentsJSONWriter(PrintWriter w, Resource resource)
			throws IOException {
		super(resource);
		this.w = w;
	}

	@Override
	public void create(ResultBrowserContext contentSelection) {

		if (selectedTableIndexSet == null || selectedTableIndexSet.isEmpty()) {
			this.w.println("contentsDispatcher.statValues = {};");
			return;
		}

		super.create(master, selectedTableIndexSet, selectedRowIndexSet, selectedQuestionIndexSet);

		this.w.print("contentsDispatcher.statValues={");
		boolean hasPrinted = false;
		for (String key : this.values.uniqueSet()) {
			if (hasPrinted) {
				this.w.print(",");
			} else {
				hasPrinted = true;
			}
			this.w.print("'");
			this.w.print(key);
			this.w.print("':");
			this.w.print(this.values.getCount(key));
		}
		this.w.println("};");
	}

}
