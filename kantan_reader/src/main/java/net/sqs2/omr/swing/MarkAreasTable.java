/**
 * 
 */
package net.sqs2.omr.swing;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.sqs2.omr.source.PageID;

class MarkAreasTable extends JTable{
	
	public static final Object[][] COLUMNS = new Object[][]{
			{"ID", 42, JTableConstants.ALIGN_RIGHT_TOOLTIP_RENDERER},
			{"表", 42, JTableConstants.ALIGN_RIGHT_TOOLTIP_RENDERER},
			{"行", 32, JTableConstants.ALIGN_RIGHT_TOOLTIP_RENDERER},
			{"頁", 28, JTableConstants.ALIGN_RIGHT_TOOLTIP_RENDERER},
			{"問", 110, JTableConstants.ALIGN_LEFT_TOOLTIP_RENDERER},
			{"マーク欄", 244, JTableConstants.MARKAREAS_RENDERER}
			};
	
	MarkAreasTable(MarkAreasTableModel tableModel){
		setModel(tableModel);
		initTableColumnModel(getColumnModel());
		setRowHeight(48);
		
		addMouseListener(new MouseAdapter() {
			  public void mouseClicked(final MouseEvent me) {
			    if(me.getClickCount() == 2) {
			      Point pt = me.getPoint();
			      int row = rowAtPoint(pt);
			      //int column = columnAtPoint(pt);
			      MarkAreasTableCell cell = (MarkAreasTableCell) getModel().getValueAt(row, 5);
			      
			      List<PageID> pageIDList = cell.getPageIDList();
			      
			      String path = pageIDList.get(0).getFileResourceID().getRelativePath();
			      File imageFile = new File(cell.getRowGroupSourceDirectory().getRoot(), path); 
			      try{
			    	  new ImageViewApp(imageFile, JFrame.DISPOSE_ON_CLOSE);
			      }catch(Exception ex){
			    	  ex.printStackTrace();
			      }
			      /*
			      JOptionPane.showMessageDialog(PageTaskErrorTable.this, file.getAbsolutePath(), "title",
			                                    JOptionPane.INFORMATION_MESSAGE);
			      */
			    }
			  }
		});

	}
	

	public boolean isCellEditable(int row, int column) {
		//return column == 5;
		return false;
	}
	
	private TableColumnModel initTableColumnModel(TableColumnModel model){
		for(int columnIndex = 0; columnIndex < COLUMNS.length; columnIndex++){
			Object[] row = COLUMNS[columnIndex];
			TableColumn c = model.getColumn(columnIndex);
			c.setHeaderValue(row[0]);
			c.setPreferredWidth((Integer)row[1]);
			if(2 < row.length && row[2] != null){
				c.setCellRenderer((TableCellRenderer)row[2]);
				if(row[2] instanceof TableCellEditor){
					c.setCellEditor((TableCellEditor)row[2]);
				}
			}
		}
		return model;
	}
}