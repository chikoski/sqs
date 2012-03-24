/**
 * 
 */
package net.sqs2.omr.swing.preview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.swing.JTable;

import net.sqs2.omr.app.deskew.DeskewGuideMissingExceptionModel;
import net.sqs2.omr.app.deskew.PageFrameDistortionExceptionModel;
import net.sqs2.omr.app.deskew.PageImageSourceException.PageSourceExceptionModel;
import net.sqs2.omr.app.deskew.PageSequenceInvalidExceptionModel;
import net.sqs2.omr.app.deskew.PageUpsideDownExceptionModel;
import net.sqs2.omr.model.PageTaskException;
import net.sqs2.omr.model.PageTaskExceptionModel;
import net.sqs2.omr.session.model.PageTaskExceptionTableCell;
import net.sqs2.swing.ImageThumbnailTableCellRenderer;
import net.sqs2.swing.ThumbnailCacheManager;
import net.sqs2.swing.ThumbnailEntry;

class PageTaskErrorTableCellRenderer extends ImageThumbnailTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean isSelected;
	boolean hasFocus;
	Font font = new Font("Serif", Font.PLAIN, 13);
	Color selectedColor;

	PageTaskErrorTableCellRenderer() {
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		return this;
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		PageTaskException taskException = ((PageTaskExceptionTableCell) this.cell).getTaskException();
		if (taskException == null) {
			return;
		}
		PageTaskExceptionModel model = taskException.getExceptionModel();
		String message = model.getLocalizedMessage();
		g.drawString(message, 0, getHeight() - 3);

		int compWidth = this.getWidth();
		String path = this.cell.getPath();
		int index = this.cell.getIndex();
		File file = new File(this.cell.getRoot(), path);
		try{
			ThumbnailEntry entryValue = ThumbnailCacheManager.loadImage(file, index);
			Image thumbnail = entryValue.getImage();
			if (thumbnail == null) {
				return;
			}
			int w = thumbnail.getWidth(null);
			int h = thumbnail.getHeight(null);

			int x0 = (compWidth - w) / 2;
			int y0 = 0;

			if (model instanceof PageFrameDistortionExceptionModel) {
				PageFrameDistortionExceptionModel m = (PageFrameDistortionExceptionModel) model;
				Point2D[] corners = m.getCorners();

				String detailUpper = "(" + corners[0].getX() + "," + corners[0].getY() + ")(" + corners[1].getX() + ","
						+ corners[1].getY() + ")";
				g.drawString(detailUpper, 0, 14 - 3);
				String detailLower = "(" + corners[2].getX() + "," + corners[2].getY() + ")(" + corners[3].getX() + ","
						+ corners[3].getY() + ")";
				g.drawString(detailLower, 0, 28 - 3);

				int originalImageWidth = entryValue.getOriginalImageWidth();
				int originalImageHeight = entryValue.getOriginalImageHeight();

				g.setColor(new Color(255, 0, 0, 100));
				g.drawPolygon(
						new int[] {(int)( x0 + corners[0].getX() * w / originalImageWidth),
								(int)(x0 + corners[1].getX() * w / originalImageWidth),
								(int)(x0 + corners[3].getX() * w / originalImageWidth),
								(int)(x0 + corners[2].getX() * w / originalImageWidth),
								(int)(x0 + corners[0].getX() * w / originalImageWidth), }, 
								new int[] {
								(int)(y0 + corners[0].getY() * h / originalImageHeight),
								(int)(y0 + corners[1].getY() * h / originalImageHeight),
								(int)(y0 + corners[3].getY() * h / originalImageHeight),
								(int)(y0 + corners[2].getY() * h / originalImageHeight),
								(int)(y0 + corners[0].getY() * h / originalImageHeight), }, 5);

			} else if (model instanceof PageSourceExceptionModel) {
				PageSourceExceptionModel m = (PageSourceExceptionModel) model;
				String detail = m.getPageID() + ":(" + m.getX() + "," + m.getY() + ") [" + m.getWidth() + ","
						+ m.getHeight() + "]";
				g.drawString(detail, 0, 14 - 3);
			} else if (model instanceof PageUpsideDownExceptionModel) {
				//PageUpsideDownExceptionModel c = (PageUpsideDownExceptionModel) model;
				// TODO

			} else if (model instanceof DeskewGuideMissingExceptionModel) {
				//CornerBlockMissingExceptionModel m = (CornerBlockMissingExceptionModel) model;
				// TODO

			} else if (model instanceof PageSequenceInvalidExceptionModel) {
				// TODO
				PageSequenceInvalidExceptionModel m = (PageSequenceInvalidExceptionModel) model;
				m.getCorners();

			}
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

}
