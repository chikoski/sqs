package net.sqs2.omr.swing.preview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JPanel;
import javax.swing.JTable;

import net.sqs2.omr.result.export.model.FormAreaTableCell;

public abstract class FormAreaTableCellRenderer extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected FormAreaTableCell cell;
	protected boolean isSelected;
	boolean hasFocus;
	Font font = new Font("Serif", Font.PLAIN, 13);
	Color selectedColor;

	protected NumberFormat format = new DecimalFormat("000");

	public FormAreaTableCellRenderer() {

		this.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

		});

	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		this.cell = (FormAreaTableCell) value;
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

	protected void drawImage(Graphics g, int y, int compWidth, int imageIndex, int numImages, BufferedImage image) {
		if (image == null) {
			return;
		}
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		int x = 5 + 2 * w * imageIndex;
		g.drawImage(image, x, y, this);
		g.setColor(getForeground());
		g.drawRect(x, y, w, h);
	}

}
