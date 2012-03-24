/**
 * 
 */
package net.sqs2.omr.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.File;

import javax.swing.JTable;

import net.sqs2.omr.logic.CornerBlockMissingExceptionCore;
import net.sqs2.omr.logic.PageFrameDistortionExceptionCore;
import net.sqs2.omr.logic.PageSequenceInvalidExceptionCore;
import net.sqs2.omr.logic.PageUpsideDownExceptionCore;
import net.sqs2.omr.logic.PageSourceException.PageSourceExceptionCore;
import net.sqs2.omr.swing.ThumbnailCacheManager.ThumbnailEntry;
import net.sqs2.omr.task.TaskError;
import net.sqs2.omr.task.TaskExceptionCore;

class PageTaskErrorTableCellRenderer extends ImageThumbnailTableCellRenderer{

	boolean isSelected;
	boolean hasFocus;
	Font font = new Font("Serif", Font.PLAIN, 13);
	Color selectedColor;

	PageTaskErrorTableCellRenderer(){
	}
	
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		return this;
	}	
	
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		
		TaskError taskError = ((PageTaskErrorTableCell) cell).getTaskError();
		if(taskError == null){
			return;
		}
		TaskExceptionCore core = taskError.getExceptionCore();
		String message = core.getLocalizedMessage();
		g.drawString(message, 0, getHeight() - 3);

		
		int compWidth = this.getWidth();
		String path = cell.getPath();
	    File file = new File( cell.getRoot(), path);
	    ThumbnailEntry entryValue = ThumbnailCacheManager.loadImage(file);
	    Image thumbnail = entryValue.getImage();
	    if(thumbnail == null){
	    	return;
	    }
	    int w = thumbnail.getWidth(null);
	    int h = thumbnail.getHeight(null);

	    int x0 = (compWidth - w)/2;
	    int y0 = 0;
	    
		if(core instanceof PageFrameDistortionExceptionCore){
			PageFrameDistortionExceptionCore c = (PageFrameDistortionExceptionCore)core;
			Point[] corners = c.getCorners();
			
			String detailUpper = "("+corners[0].x+","+corners[0].y+")("+corners[1].x+","+corners[1].y+")";
			g.drawString(detailUpper, 0, 14 - 3);
			String detailLower = "("+corners[2].x+","+corners[2].y+")("+corners[3].x+","+corners[3].y+")";
			g.drawString(detailLower, 0, 28 - 3);

			int originalImageWidth = entryValue.getOriginalImageWidth();
			int originalImageHeight = entryValue.getOriginalImageHeight();
			
			g.setColor(new Color(255,0,0,100));
			g.drawPolygon(new int[]{
					x0 + corners[0].x * w / originalImageWidth,
					x0 + corners[1].x * w / originalImageWidth,
					x0 + corners[3].x * w / originalImageWidth,
					x0 + corners[2].x * w / originalImageWidth,
					x0 + corners[0].x * w / originalImageWidth,
			}, new int[]{
					y0 + corners[0].y * h / originalImageHeight,
					y0 + corners[1].y * h / originalImageHeight,
					y0 + corners[3].y * h / originalImageHeight,
					y0 + corners[2].y * h / originalImageHeight,
					y0 + corners[0].y * h / originalImageHeight,
			}, 5);
			
			
		}else if(core instanceof PageSourceExceptionCore){
			PageSourceExceptionCore c = (PageSourceExceptionCore)core;
			String detail = c.getPageID()+":("+c.getX()+","+c.getY()+") ["+c.getWidth()+","+c.getHeight()+"]";
			g.drawString(detail, 0, 14 - 3);
		}else if(core instanceof PageUpsideDownExceptionCore){
			PageUpsideDownExceptionCore c = (PageUpsideDownExceptionCore)core;
			//TODO

		}else if(core instanceof CornerBlockMissingExceptionCore){
			CornerBlockMissingExceptionCore c = (CornerBlockMissingExceptionCore)core;
			//TODO

		}else if(core instanceof PageSequenceInvalidExceptionCore){
			//TODO
			PageSequenceInvalidExceptionCore c = (PageSequenceInvalidExceptionCore)core;
			c.getCorners();

		}
	}
	
}