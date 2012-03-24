package net.sqs2.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

public class LicenseDocumentsTable extends JTable {

	private static final long serialVersionUID = 1L;

	String[][] DATA = new String[][] { { "author", "author", "license", "url" } };

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JTable table = new LicenseDocumentsTable();

		// table.set

		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		panel.add(table);
	}

	public LicenseDocumentsTable() {

	}
}
