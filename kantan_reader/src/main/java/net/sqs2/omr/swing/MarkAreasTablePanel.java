package net.sqs2.omr.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class MarkAreasTablePanel extends JPanel {
	
	private static final long serialVersionUID = 0L;
	
	JTabbedPane tabbedPane;
	MarkAreasTable noAnswerMarkAreasTable;
	MarkAreasTable multipleAnswersMarkAreasTable;
	TitledBorder titledBorder = null;
		
	public MarkAreasTablePanel(MarkAreasTableModel noAnswerMarkAreaTableModel, MarkAreasTableModel multipleAnswersMarkAreaTableModel){
		titledBorder = new TitledBorder("以下を確認の上、必要に応じて画像修正/再処理を実行してください");
		setPreferredSize(new Dimension(390, 230));
		setLayout(new BorderLayout());
		setBorder(new CompoundBorder(new EmptyBorder(5,5,5,5), new CompoundBorder(titledBorder, new EmptyBorder(3,3,3,3))));
						
		noAnswerMarkAreasTable = new MarkAreasTable(noAnswerMarkAreaTableModel);
		multipleAnswersMarkAreasTable = new MarkAreasTable(multipleAnswersMarkAreaTableModel);
		JScrollPane noAnswerMarkAreasTableScrollPane = new JScrollPane(noAnswerMarkAreasTable);
		JScrollPane multipleAnswersMarkAreasTableScrollPane = new JScrollPane(multipleAnswersMarkAreasTable);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.add("無回答", noAnswerMarkAreasTableScrollPane);
		tabbedPane.add("多重回答", multipleAnswersMarkAreasTableScrollPane);

		add(tabbedPane);

	}
	
}
