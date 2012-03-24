package net.sqs2.omr.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


public class PageTaskErrorPanel extends JPanel {
	
	private static final long serialVersionUID = 0L; 
	PageTaskErrorTable table;
	TitledBorder titledBorder = null;
		
	public PageTaskErrorPanel(PageTaskErrorTableModel tableModel){
		
		titledBorder = new TitledBorder("以下の画像ファイルのエラーを修正し、再処理を実行してください");
		
		table = new PageTaskErrorTable(tableModel);
		
		setPreferredSize(new Dimension(390, 230));
		setLayout(new BorderLayout());
		setBorder(new CompoundBorder(new EmptyBorder(5,5,5,5), new CompoundBorder(titledBorder, new EmptyBorder(3,3,3,3))));
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
	}
	
	public static void main(String[] args){
		PageTaskErrorTableModel model = new PageTaskErrorTableModel();
		//CornerBlockMissingExceptionCore core = new CornerBlockMissingExceptionCore(PageID.createID(path, lastModified, index, numPagesInFile), 0, 0, 0);
		model.addRow(0, 1, new File("/home/hiroya/Desktop/sqs-sample-images"), "a001.tif", null);
		JPanel panel = new PageTaskErrorPanel(model);
		JFrame frame = new JFrame();
		frame.add(panel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
}
