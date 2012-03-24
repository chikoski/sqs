/**
 * 
 */
package net.sqs2.omr.swing.preview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import net.sqs2.image.ImageFactory;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.Answer;
import net.sqs2.omr.model.ContentAccessor;
import net.sqs2.omr.model.PageAreaResult;
import net.sqs2.omr.model.PageTaskAccessor;
import net.sqs2.omr.model.Row;
import net.sqs2.omr.model.RowAccessor;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.TextAreaAnswer;
import net.sqs2.omr.session.logic.PageAreaResultListUtil;
import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.omr.session.service.MarkReaderSessions;

class TextAreaTableCellRenderer extends FormAreaTableCellRenderer implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	JTextArea textarea;
	int margin = 3;

	public TextAreaTableCellRenderer() {
		super();
		setBorder(new EmptyBorder(this.margin, this.margin, this.margin, this.margin));
		setLayout(new BorderLayout());
		this.textarea = new JTextArea();
		this.textarea.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				// TextAreaTableCellRenderer component =
				// (TextAreaTableCellRenderer)e.getSource();
			}

		});
		add(this.textarea, BorderLayout.SOUTH);
	}

	/*
	 * class ImagePanel extends JPanel{
	 * 
	 * BufferedImage image;
	 * 
	 * ImagePanel(){ }
	 * 
	 * public void setImage(BufferedImage image){ this.image = image; }
	 * 
	 * @Override public void paintComponent(Graphics g){ setPreferredSize(new
	 * Dimension(2*margin+this.image.getWidth(),
	 * 2*margin+this.image.getHeight())); g.drawImage(this.image, 0, 0, this); }
	 * }
	 */

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		SourceDirectory sourceDirectory = this.cell.getSpreadSheet().getSourceDirectory();
		FormMaster master = (FormMaster) this.cell.getSpreadSheet().getFormMaster();
		List<FormArea> formAreaList = master.getFormAreaList(this.cell.getQuestionIndex());

		FormArea primaryFormArea = formAreaList.get(0);

		int textAreaImageWidth = (int) primaryFormArea.getRect().getWidth();
		int textAreaImageHeight = (int) primaryFormArea.getRect().getHeight();

		this.setPreferredSize(new Dimension(2 * this.margin + textAreaImageWidth, 2 * this.margin
				+ textAreaImageHeight * 2));
		this.textarea.setPreferredSize(new Dimension(textAreaImageWidth, textAreaImageHeight));

		int compWidth = this.getWidth();
		int compHeight = this.getHeight();
		if (this.isSelected) {
			g.setColor(getBackground());
			g.fillRect(0, 0, compWidth, compHeight);
			g.setColor(getForeground());
		}
		
		MarkReaderSession session = MarkReaderSessions.get(this.cell.getSpreadSheet().getSourceDirectory().getRoot());
		ContentAccessor accessor = session.getSessionSource().getContentAccessor();

		try {
			PageTaskAccessor pageTaskAccessor = accessor.getPageTaskAccessor();

			PageAreaResult textAreaResult = PageAreaResultListUtil.createPageAreaResultListParQuestion(
					master, this.cell.getRowGroupSourceDirectory(), pageTaskAccessor,
					this.cell.getRowGroupRowIndex(), this.cell.getQuestionIndex()).get(0);

			byte[] bytes = textAreaResult.getImageByteArray();
			BufferedImage image = ImageFactory.createImage(textAreaResult.getImageType(), bytes, 0);
			g.drawImage(image, 0, 0, this);
			image.flush();

			RowAccessor rowAccessor = accessor.getRowAccessor();
			Row row = (Row) rowAccessor.get(master.getRelativePath(), sourceDirectory.getRelativePath(), this.cell
					.getRowGroupRowIndex());
			Answer answer = row.getAnswer(this.cell.getQuestionIndex());
			if (answer instanceof TextAreaAnswer) {
				TextAreaAnswer textAreaAnswer = (TextAreaAnswer) answer;
				String value = textAreaAnswer.getValue();
				if (value != null) {
					this.textarea.setText(value);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
