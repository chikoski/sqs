/**
 * 
 */
package net.sqs2.omr.swing.preview;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.table.TableCellRenderer;

import net.sqs2.image.ImageFactory;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.ContentAccessor;
import net.sqs2.omr.model.FormAreaResult;
import net.sqs2.omr.model.PageAreaResult;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTaskAccessor;
import net.sqs2.omr.model.SpreadSheet;
import net.sqs2.omr.session.logic.PageAreaResultListUtil;
import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.omr.session.service.MarkReaderSessions;
import net.sqs2.util.StringUtil;

class MarkAreasTableCellRenderer extends FormAreaTableCellRenderer implements TableCellRenderer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MarkAreasTableCellRenderer() {
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

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		int compWidth = this.getWidth();
		int compHeight = this.getHeight();
		// int compHeight = this.getHeight();
		if (this.isSelected) {
			g.setColor(getBackground());
			g.fillRect(0, 0, compWidth, compHeight);
			g.setColor(getForeground());
		}

		SpreadSheet spreadSheet = this.cell.getSpreadSheet();
		FormMaster master = (FormMaster) spreadSheet.getFormMaster();
		int rowGroupRowIndex = this.cell.getRowGroupRowIndex();
		int columnIndex = this.cell.getQuestionIndex();

		List<FormArea> formAreaList = master.getFormAreaList(columnIndex);
		int formAreaListSize = formAreaList.size();

		this.setToolTipText(StringUtil.join(formAreaList.get(0).getHints(), "\n"));

		try {
			MarkReaderSession session = MarkReaderSessions.get(spreadSheet.getSourceDirectory().getRoot());
			ContentAccessor accessor = session.getSessionSource().getContentAccessor();
			PageTaskAccessor pageTaskAccessor = accessor.getPageTaskAccessor();
			List<PageAreaResult> pageAreaResultListParQuestion = PageAreaResultListUtil
					.createPageAreaResultListParQuestion(master, this.cell.getRowGroupSourceDirectory(),
							pageTaskAccessor, rowGroupRowIndex, columnIndex);

			List<PageID> rowGroupSourceDirectoryPageIDList = this.cell.getRowGroupSourceDirectory()
					.getPageIDList();

			int pageStart = formAreaList.get(0).getPage();
			int pageEnd = formAreaList.get(formAreaListSize - 1).getPage();

			int base = rowGroupRowIndex * master.getNumPages();

			StringBuffer path = new StringBuffer();
			if (pageStart == pageEnd) {
				String p = rowGroupSourceDirectoryPageIDList.get(pageStart + base - 1).getFileResourceID()
						.getRelativePath();
				path.append(p);
			} else {
				String p = rowGroupSourceDirectoryPageIDList.get(pageEnd + base - 1).getFileResourceID()
						.getRelativePath();
				path.append(',').append(p);
			}
			g.drawString(path.toString(), 0, 12);

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < formAreaListSize; i++) {
				// FormArea formArea = formAreaList.get(i);
				FormAreaResult result = (FormAreaResult) pageAreaResultListParQuestion.get(i);
				byte[] bytes = result.getImageByteArray();
				BufferedImage image = ImageFactory.createImage(result.getImageType(), bytes, 0);
				drawImage(g, 14, compWidth, i, formAreaListSize, image);
				image.flush();

				float density = result.getDensity();
				if (sb.length() == 0) {
					sb.append(" ");
				} else {
					sb.append(" | ");
				}
				sb.append(this.format.format(density * 255));
			}
			g.drawString(sb.toString(), 0, 46);

		} catch (IOException ignore) {
			ignore.printStackTrace();
		}
	}
}
