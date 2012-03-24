/**
 * 
 */
package net.sqs2.omr.result.export.model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.SpreadSheet;

public class MarkAreasTableCell extends FormAreaTableCell {

	public MarkAreasTableCell(SpreadSheet spreadSheet, SourceDirectory rowGroupSourceDirectory,
			int rowGroupRowIndex, int rowIndex, int columnIndex) {
		super(spreadSheet, rowGroupSourceDirectory, rowGroupRowIndex, rowIndex, columnIndex);
	}

	public List<PageID> getPageIDList() {
		FormMaster master = (FormMaster) this.spreadSheet.getFormMaster();
		int numPages = master.getNumPages();
		TreeSet<Integer> set = new TreeSet<Integer>();
		for (FormArea formArea : master.getFormAreaList(this.questionIndex)) {
			int pageIndex = formArea.getPageIndex();
			set.add(pageIndex);
		}

		List<PageID> pageIDList = this.rowGroupSourceDirectory.getPageIDList();

		List<PageID> ret = new ArrayList<PageID>(set.size());
		for (int pageIndex : set) {
			ret.add(pageIDList.get(this.rowIndex * numPages + pageIndex));
		}
		return ret;
	}

}
