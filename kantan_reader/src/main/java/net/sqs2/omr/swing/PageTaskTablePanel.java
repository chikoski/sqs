package net.sqs2.omr.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;



public class PageTaskTablePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	PageTaskErrorTable table;
	TitledBorder titledBorder = null;

	public PageTaskTablePanel(PageTaskErrorTableModel tableModel){
		
		titledBorder = new TitledBorder("");
		table = new PageTaskErrorTable(tableModel);
		
		setPreferredSize(new Dimension(390, 230));
		setLayout(new BorderLayout());
		setBorder(new CompoundBorder(new EmptyBorder(5,5,5,5), new CompoundBorder(titledBorder, new EmptyBorder(3,3,3,3))));
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
	}
	
}
