package net.sqs2.omr.swing.preview;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.sqs2.omr.session.model.PageTaskExceptionTableModel;

public class PageTaskErrorPanel extends JPanel {

	private static final long serialVersionUID = 0L;
	PageTaskErrorTable table;
	TitledBorder titledBorder = null;

	public PageTaskErrorPanel(PageTaskExceptionTableModel tableModel) {

		this.titledBorder = new TitledBorder("以下の画像ファイルのエラーを修正し、再処理を実行してください");

		this.table = new PageTaskErrorTable(tableModel);

		setPreferredSize(new Dimension(400, 400));
		setLayout(new BorderLayout());
		setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5), new CompoundBorder(this.titledBorder,
				new EmptyBorder(3, 3, 3, 3))));
		JScrollPane scrollPane = new JScrollPane(this.table);
		add(scrollPane);
	}

	/*
	public static void main(String[] args) {
		PageTaskErrorTableModel model = new PageTaskErrorTableModel();
		// CornerBlockMissingExceptionCore core = new
		// CornerBlockMissingExceptionCore(PageID.createID(path, lastModified,
		// index, numPagesInFile), 0, 0, 0);
		model.addRow(0, 1, new File("/home/hiroya/Desktop/sqs-sample-images"), "a001.tif", 0, null);
		JPanel panel = new PageTaskErrorPanel(model);
		JFrame frame = new JFrame();
		frame.add(panel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}*/

}
