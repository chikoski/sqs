/**
 * 
 */
package net.sqs2.omr.swing;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class HorizontalAlignmentTableCellRenderer implements TableCellRenderer {
	private final TableCellRenderer renderer;
	int alignment;

	public HorizontalAlignmentTableCellRenderer(TableCellRenderer renderer, int alignment) {
		this.renderer = renderer;
		this.alignment = alignment;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = renderer.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		if (c instanceof JLabel) {
			initLabel((JLabel) c, row, value.toString());
		}
		return c;
	}

	private void initLabel(JLabel l, int row, String value) {
		l.setHorizontalAlignment(alignment);
		l.setToolTipText(value);
	}
}