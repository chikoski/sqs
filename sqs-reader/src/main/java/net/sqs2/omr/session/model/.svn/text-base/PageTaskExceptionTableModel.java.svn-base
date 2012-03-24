package net.sqs2.omr.session.model;

import java.io.File;

import net.sqs2.omr.model.PageTaskException;
import net.sqs2.swing.SortableTableModel;

public class PageTaskExceptionTableModel extends SortableTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PageTaskExceptionTableModel() {
		super(0, 3);
	}

	public void addRow(int rowID, int pageNumber, File root, String path, int index, PageTaskException ex) {
		addRow(new Object[] { rowID, pageNumber, new PageTaskExceptionTableCell(root, path, index, ex) });
	}

	public void clear() {
		getDataVector().clear();
	}
}
