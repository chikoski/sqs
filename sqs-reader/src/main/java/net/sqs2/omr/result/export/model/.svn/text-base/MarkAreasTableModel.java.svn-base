package net.sqs2.omr.result.export.model;

import java.util.TreeMap;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.SpreadSheet;
import net.sqs2.swing.SortableTableModel;

public class MarkAreasTableModel extends SortableTableModel {

	private static final long serialVersionUID = 1L;
	private TreeMap<TableCellAddress,MarkAreasTableCell> rowMap = new TreeMap<TableCellAddress,MarkAreasTableCell>(); 
	
	public MarkAreasTableModel() {
		super(0, 6);
	}

	public void addRow(int index, SpreadSheet spreadSheet, SourceDirectory rowGroupSourceDirectory, int rowGroupRowIndex, int pageStart, int pageEnd, int tableIndex, int rowIndex, int columnIndex) {
		String pageRange = Integer.toString(pageStart);
		if (pageStart != pageEnd) {
			pageRange = new StringBuilder(pageRange).append('-') + Integer.toString(pageEnd).toString();
		}

		FormMaster master = (FormMaster) spreadSheet.getFormMaster();
		FormArea primaryFormArea = master.getFormAreaList(columnIndex).get(0);

		MarkAreasTableCell cell = new MarkAreasTableCell(spreadSheet, rowGroupSourceDirectory,
				rowGroupRowIndex, rowIndex, columnIndex);

		addRow(new Object[] { index, rowGroupSourceDirectory.getRelativePath(), rowIndex + 1, pageRange,
				primaryFormArea.getQID(), cell });
		sort(0, true);
		this.rowMap.put(new TableCellAddress(tableIndex, rowIndex, columnIndex), cell);
	}
	
	public MarkAreasTableCell getCell(int tableIndex, int rowIndex, int columnIndex){
		return this.rowMap.get(new TableCellAddress(tableIndex, rowIndex, columnIndex));
	}

	public void clear() {
		getDataVector().clear();
	}
}
