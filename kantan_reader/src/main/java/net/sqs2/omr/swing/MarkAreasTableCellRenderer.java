/**
 * 
 */
package net.sqs2.omr.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import net.sqs2.image.ImageFactory;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.contents.ContentsFactoryUtil;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.SpreadSheet;
import net.sqs2.omr.task.FormAreaCommand;
import net.sqs2.omr.task.PageAreaCommand;
import net.sqs2.omr.task.TaskAccessor;
import net.sqs2.util.StringUtil;

class MarkAreasTableCellRenderer extends JComponent implements TableCellRenderer,TableCellEditor {

	MarkAreasTableCell cell;
	boolean isSelected;
	boolean hasFocus;
	Font font = new Font("Serif", Font.PLAIN, 13);
	Color selectedColor;

	ImageViewPanel panel;
	
	public MarkAreasTableCellRenderer(){
		this.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				System.out.println(".");
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
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		this.cell = (MarkAreasTableCell) value;
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

	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		int compWidth = this.getWidth();
		int compHeight = this.getHeight();
		//int compHeight = this.getHeight();
	    if(isSelected){
	    	g.setColor(getBackground());
	    	g.fillRect(0, 0, compWidth, compHeight);
	    	g.setColor(getForeground());
	    }

		SpreadSheet spreadSheet = cell.getSpreadSheet();
		FormMaster master = (FormMaster)spreadSheet.getSourceDirectory().getPageMaster();
		int rowIndex = cell.getRowIndex();
		int columnIndex = cell.getColumnIndex(); 
		
		List<FormArea> formAreaList = master.getFormAreaList(columnIndex);
		int len = formAreaList.size();

		this.setToolTipText(StringUtil.join(formAreaList.get(0).getHints(), "\n"));
		
		try{
			TaskAccessor pageTaskAccessor = new TaskAccessor(spreadSheet.getSourceDirectory().getRoot()); 
			List<PageAreaCommand> pageAreaCommandList = ContentsFactoryUtil.createPageAreaCommandListParQuestion(master, spreadSheet.getSourceDirectory(),
					pageTaskAccessor, rowIndex, columnIndex);
			//List<PageID> pageIDList = new ArrayList<PageID>(1);
			int pageStart = 0, pageEnd = 0;
			
			List<PageID> rowGroupSourceDirectoryPageIDList = cell.getRowGroupSourceDirectory().getPageIDList();
			pageStart = formAreaList.get(0).getPage();
			pageEnd = formAreaList.get(len - 1).getPage();

			int base = rowIndex * master.getNumPages();
			StringBuffer path = new StringBuffer();
			if(pageStart == pageEnd){
				String p = rowGroupSourceDirectoryPageIDList.get(pageStart + base - 1).getFileResourceID().getRelativePath();
				path.append(p);
			}else{
				String p = rowGroupSourceDirectoryPageIDList.get(pageEnd + base - 1).getFileResourceID().getRelativePath();
				path.append(',').append(p);
			}
			g.drawString(path.toString(), 0, 12);
			
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < len; i++){
				FormArea formArea = formAreaList.get(i);
				FormAreaCommand command = (FormAreaCommand)pageAreaCommandList.get(i);
				byte[] bytes = command.getImageByteArray();
				BufferedImage image = ImageFactory.createImage(bytes, 0, command.getImageType());

				drawImage(g, 14, compWidth, i, len, image);
				
				float density = command.getDensity();
				if(sb.length() == 0){
					sb.append(" ");	
				}else{
					sb.append(" | ");
				}
				sb.append(format.format(density*255));
			}
			g.drawString(sb.toString(), 0, 46);

		}catch(IOException ignore){
			ignore.printStackTrace();
		}

		//FontMetrics fontMetrics = g.getFontMetrics();
		// Rectangle2D rect = fontMetrics.getStringBounds(path, g);
		// g.drawString(path, (int)((compWidth - rect.getWidth()) / 2), 64+14);
	}
	
	NumberFormat format = new DecimalFormat("000");
	
	private void drawImage(Graphics g, int y, int compWidth, int imageIndex, int numImages, BufferedImage image){
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

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		SpreadSheet spreadSheet = cell.getSpreadSheet();
		FormMaster master = (FormMaster)spreadSheet.getSourceDirectory().getPageMaster();
		int numPages = master.getNumPages();
		int pageIndex = master.getFormAreaList().get(cell.getColumnIndex()).getPageIndex();
		int index = row * numPages + pageIndex;
		String path = spreadSheet.getSourceDirectory().getPageIDList().get(index).getFileResourceID().getRelativePath();
		File file = new File(spreadSheet.getSourceDirectory().getRoot(), path);
		try{
			return new ImageViewApp.ImageViewAppPanel(file);
		}catch(IOException ex){
			return null;
		}
	}

	public void addCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}

	public void cancelCellEditing() {
		// TODO Auto-generated method stub
		
	}

	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	public void removeCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}

	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	public boolean stopCellEditing() {
		// TODO Auto-generated method stub
		return false;
	}
}
