/**
 * 
 */
package net.sqs2.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;


public class ImageThumbnailTableCellRenderer extends JComponent implements TableCellRenderer, TableCellEditor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ImageThumbnailTableCell cell;
	protected boolean isSelected;
	protected boolean hasFocus;
	protected Font font = new Font("Serif", Font.PLAIN, 13);
	protected Color selectedColor;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		this.cell = (ImageThumbnailTableCell) value;
		this.isSelected = isSelected;
		this.hasFocus = hasFocus;

		if (isSelected) {
			setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		return this;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.cell = (ImageThumbnailTableCell) value;
		this.isSelected = isSelected;

		if (isSelected) {
			setForeground(Color.RED); // table.getSelectionForeground()
			super.setBackground(Color.BLUE);
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		return this;
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		int compWidth = this.getWidth();
		int compHeight = this.getHeight();
		if (this.isSelected) {
			g.setColor(getBackground());
			g.fillRect(0, 0, compWidth, compHeight);
		}
		if (this.cell == null) {
			return;
		}

		String path = this.cell.getPath();
		int index = this.cell.getIndex();
		File file = new File(this.cell.getRoot(), path);
		try{
			ThumbnailEntry entryValue = ThumbnailCacheManager.loadImage(file, index);
			Image thumbnail = entryValue.getImage();
			if (thumbnail != null) {
				int w = thumbnail.getWidth(null);
				int h = thumbnail.getHeight(null);
				int x = (compWidth - w) / 2;
				g.drawImage(thumbnail, x, 0, this);
				g.setColor(getForeground());
				g.drawRect(x, 0, w, h);
			}
			FontMetrics fontMetrics = g.getFontMetrics();
			Rectangle2D rect = fontMetrics.getStringBounds(path, g);
			g.drawString(path, (int) ((compWidth - rect.getWidth()) / 2), 64 + 14);
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	public void addCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub

	}

	public void cancelCellEditing() {
		// TODO Auto-generated method stub

	}

	public Object getCellEditorValue() {
		return this.cell;
	}

	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	public void removeCellEditorListener(CellEditorListener l) {
	}

	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	public boolean stopCellEditing() {
		return false;
	}

}
