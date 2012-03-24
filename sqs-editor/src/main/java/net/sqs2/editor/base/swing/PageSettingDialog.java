package net.sqs2.editor.base.swing;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class PageSettingDialog extends JDialog {

	/**
	 * 
	 */
	
	String pageSizeName;
	boolean portlaitOrlandscape;

	private double pageWidth;
	private double pageHeight;
	
	boolean printTitle;
	boolean printPageNumber;
	boolean printStapleGuide;
	boolean printOMRExample;

	double baseFontSize;
	
	
	private static final long serialVersionUID = 1L;

	public static void main(String args[]){		
		PageSettingDialog dialog = new PageSettingDialog();
		dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dialog.setVisible(true);
	} 
	
	PageSettingDialog(){
		setSize(640,480);
	}
}
