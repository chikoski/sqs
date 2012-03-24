/**
 * 
 */
package net.sqs2.omr.swing.preview;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.session.model.PageTaskExceptionTableCell;
import net.sqs2.omr.session.model.PageTaskExceptionTableModel;
import net.sqs2.swing.ImageViewApp;
import net.sqs2.swing.JTableConstants;

class PageTaskErrorTable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Object[][] COLUMNS = new Object[][] {
			{ Messages.PAGETASK_ERROR_TABLE_ID_COL_LABEL, 42, JTableConstants.ALIGN_RIGHT_TOOLTIP_RENDERER },
			{ Messages.PAGETASK_ERROR_TABLE_PAGE_COL_LABEL, 28, JTableConstants.ALIGN_RIGHT_TOOLTIP_RENDERER },
			{ Messages.PAGETASK_ERROR_TABLE_REASON_COL_LABEL, 422,
					MarkReaderSessionTableConstants.PAGETASK_ERROR_RENDERER } };

	PageTaskErrorTable(PageTaskExceptionTableModel tableModel) {
		setModel(tableModel);
		initTableColumnModel(getColumnModel());
		setRowHeight(95);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent me) {
				if (me.getClickCount() == 2) {
					Point pt = me.getPoint();
					int row = rowAtPoint(pt);
					// int column = columnAtPoint(pt);
					PageTaskExceptionTableCell cell = (PageTaskExceptionTableCell) getModel().getValueAt(row, 2);
					File imageFile = new File(cell.getRoot(), cell.getPath());
					try {
						new ImageViewApp(imageFile, cell.getIndex(), JFrame.DISPOSE_ON_CLOSE);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					/*
					 * JOptionPane.showMessageDialog(PageTaskErrorTable.this,
					 * file.getAbsolutePath(), "title",
					 * JOptionPane.INFORMATION_MESSAGE);
					 */
				}
			}
		});
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	private TableColumnModel initTableColumnModel(TableColumnModel model) {
		for (int columnIndex = 0; columnIndex < COLUMNS.length; columnIndex++) {
			Object[] row = COLUMNS[columnIndex];
			TableColumn c = model.getColumn(columnIndex);
			c.setHeaderValue(row[0]);
			c.setPreferredWidth((Integer) row[1]);
			if (2 < row.length && row[2] != null) {
				c.setCellRenderer((TableCellRenderer) row[2]);
				if (row[2] instanceof TableCellEditor) {
					c.setCellEditor((TableCellEditor) row[2]);
				}
			}
		}
		return model;
	}
}
