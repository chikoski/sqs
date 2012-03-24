package net.sqs2.omr.swing;


import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;


public class JTableConstants{
	public static final TableCellRenderer ALIGN_RIGHT_RENDERER = new HorizontalAlignmentTableCellRenderer(new DefaultTableCellRenderer(), JLabel.RIGHT);
	public static final TableCellRenderer ALIGN_RIGHT_TOOLTIP_RENDERER = new HorizontalAlignmentTableCellRenderer(new DefaultTableCellRenderer(), JLabel.RIGHT);
	public static final TableCellRenderer ALIGN_LEFT_TOOLTIP_RENDERER = new HorizontalAlignmentTableCellRenderer(new DefaultTableCellRenderer(), JLabel.LEFT);
	public static final TableCellRenderer IMAGE_THUMBNAIL_RENDERER = new ImageThumbnailTableCellRenderer();
	public static final TableCellRenderer MARKAREAS_RENDERER = new MarkAreasTableCellRenderer();
	public static final TableCellRenderer PAGETASK_ERROR_RENDERER = new PageTaskErrorTableCellRenderer();
}
