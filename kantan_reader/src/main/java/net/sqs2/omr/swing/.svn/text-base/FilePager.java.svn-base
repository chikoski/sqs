package net.sqs2.omr.swing;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import net.sqs2.util.StringUtil;

public class FilePager {
	
	JFrame frame;
	JScrollBar scrollBar;
	JTextArea textarea;
	
	int maxRows = 100;
	
	public static void main(String[] args){
		new FilePager();
	}
	
	class FileContent{
		FileContent(File file){
			
		}
	}
	
	FileContent fileContent;
	
	FilePager(){
		textarea = new JTextArea();
		
		//JViewport viewport = new JViewport();
		//viewport.add(textarea);

		JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.scrollBar = scrollPane.getVerticalScrollBar();    // 縦のスクロールバーを取得
		scrollPane.getViewport().add(textarea);
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.setSize(400,300);
		frame.setVisible(true);
	}
	
	int getLineEndOffset(int offset){
		return textarea.getText().indexOf("\n", offset);
	}
	
	int getLineCount(){
		return StringUtil.count(textarea.getText(), '\n');
	}
	
	void append(String line){
		if( getLineCount() > maxRows ){
			try{
				int len = getLineEndOffset(0);
				textarea.getDocument().remove( 0, len);
			}catch( BadLocationException exp ){}
			}

			try{
				textarea.setCaretPosition( textarea.getText().length() );
				textarea.append( line + "\n");
			}catch( IllegalArgumentException exp ){
		
			}
		
		scrollBar.setValue(scrollBar.getMaximum());
	}	

	public void setMaxRows( int rows ){
		maxRows = rows;
	}

	public int getMaxRows(){
		return maxRows;
	} 
	
}
