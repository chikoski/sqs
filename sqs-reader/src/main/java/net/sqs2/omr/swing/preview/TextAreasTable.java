/**
 * 
 */
package net.sqs2.omr.swing.preview;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.ConfigSchemeException;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SessionSources;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.result.export.model.MarkAreasTableCell;
import net.sqs2.omr.result.export.model.MarkAreasTableModel;
import net.sqs2.omr.session.logic.PageImageRenderer;
import net.sqs2.swing.ImageViewApp;
import net.sqs2.swing.JTableConstants;

class TextAreasTable extends JTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Object[][] COLUMNS = new Object[][] {
			{ "ID", 42, JTableConstants.ALIGN_RIGHT_TOOLTIP_RENDERER },
			{ "表", 42, JTableConstants.ALIGN_RIGHT_TOOLTIP_RENDERER },
			{ "行", 32, JTableConstants.ALIGN_RIGHT_TOOLTIP_RENDERER },
			{ "頁", 28, JTableConstants.ALIGN_RIGHT_TOOLTIP_RENDERER },
			{ "問", 110, JTableConstants.ALIGN_LEFT_TOOLTIP_RENDERER },
			{ "自由記述欄", 244, MarkReaderSessionTableConstants.TEXTAREA_RENDERER } };

	TextAreasTable(MarkAreasTableModel tableModel) {
		setModel(tableModel);
		initTableColumnModel(getColumnModel());
		setRowHeight(48);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent me) {
				if (me.getClickCount() == 2) {
					Point pt = me.getPoint();
					int row = rowAtPoint(pt);
					//int column = columnAtPoint(pt);

					MarkAreasTableCell cell = (MarkAreasTableCell) getModel().getValueAt(row, 5);

					SourceDirectory sourceDirectory = cell.getRowGroupSourceDirectory();
					FormMaster master = (FormMaster) cell.getSpreadSheet().getFormMaster();
					List<FormArea> formAreaList = master.getFormAreaList(cell.getQuestionIndex());

					int pageStart = formAreaList.get(0).getPage();
					int base = cell.getRowGroupRowIndex() * master.getNumPages();

					List<PageID> rowGroupSourceDirectoryPageIDList = cell.getRowGroupSourceDirectory()
							.getPageIDList();
					PageID pageID = rowGroupSourceDirectoryPageIDList.get(pageStart + base - 1);

					String path = pageID.getFileResourceID().getRelativePath();
					File imageFile = new File(sourceDirectory.getRoot(), path);

					long sessionID = cell.getSpreadSheet().getSessionID();
					SessionSource sessionSource = SessionSources.getInitializedInstance(sessionID);
					int pageIndex = master.getFormAreaList(cell.getQuestionIndex()).get(0).getPageIndex();

					Rectangle scope = new Rectangle();
					try {
						BufferedImage image = PageImageRenderer.createImage(sessionID, sessionSource, master,
								cell.getRowGroupSourceDirectory(), cell.getRowGroupRowIndex(), cell
										.getQuestionIndex(), pageIndex, scope);

						openImageViewApp(imageFile, image, scope);
					} catch (IOException ex) {
						ex.printStackTrace();
					} catch (ConfigSchemeException ex) {
						ex.printStackTrace();
					}

				}
			}
		});

	}

	void openImageViewApp(File imageFile, BufferedImage image, Rectangle scope) {
		try {
			new ImageViewApp(imageFile, image, scope, WindowConstants.DISPOSE_ON_CLOSE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
