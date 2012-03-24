package net.sqs2.omr.swing.preview;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.result.export.model.MarkAreasTableModel;
import net.sqs2.omr.swing.Images;

public class MarkAreasTablePanel extends JPanel {

	private static final long serialVersionUID = 0L;

	JTabbedPane tabbedPane;
	MarkAreasTable noAnswerMarkAreasTable;
	MarkAreasTable multipleAnswersMarkAreasTable;
	TitledBorder titledBorder = null;

	public MarkAreasTablePanel(MarkAreasTableModel noAnswerMarkAreaTableModel,
			MarkAreasTableModel multipleAnswersMarkAreaTableModel) {
		this.titledBorder = new TitledBorder(Messages.MARKAREA_ERROR_DIALOG_MSG);
		setPreferredSize(new Dimension(400, 400));
		setLayout(new BorderLayout());
		setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5), new CompoundBorder(this.titledBorder,
				new EmptyBorder(3, 3, 3, 3))));

		this.noAnswerMarkAreasTable = new MarkAreasTable(noAnswerMarkAreaTableModel);
		this.multipleAnswersMarkAreasTable = new MarkAreasTable(multipleAnswersMarkAreaTableModel);
		JScrollPane noAnswerMarkAreasTableScrollPane = new JScrollPane(this.noAnswerMarkAreasTable);
		JScrollPane multipleAnswersMarkAreasTableScrollPane = new JScrollPane(
				this.multipleAnswersMarkAreasTable);

		this.tabbedPane = new JTabbedPane();
		this.tabbedPane.add(Messages.RESULT_NOANSWER_LABEL, noAnswerMarkAreasTableScrollPane);
		this.tabbedPane.setIconAt(0, Images.TAG_YELLOW_ICON);
		this.tabbedPane.add(Messages.RESULT_MULANSWER_LABEL, multipleAnswersMarkAreasTableScrollPane);
		this.tabbedPane.setIconAt(1, Images.TAG_ORANGE_ICON);

		add(this.tabbedPane);
	}

	public void updateTabTitle() {
		this.tabbedPane.setTitleAt(0, Messages.RESULT_NOANSWER_LABEL + " (" + this.noAnswerMarkAreasTable.getRowCount()
				+ ")");
		this.tabbedPane.setTitleAt(1, Messages.RESULT_MULANSWER_LABEL + " ("
				+ this.multipleAnswersMarkAreasTable.getRowCount() + ")");
	}

}
