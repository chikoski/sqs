package net.sqs2.omr.tools;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.sqs2.swing.FileDropTargetDecorator;

public class SwapFilenamesGUILauncher{
	public static void main(String[] args){
		SwapFilenamesGUILauncher app = new SwapFilenamesGUILauncher();
		new FileDropTargetDecorator(app.panel){
			public void drop(File[] file) {
				try{
					SwapFilenames.swapFilenames(file);
				}catch(IOException ex){
					ex.printStackTrace();
				}
			}
		};
	}
	
	JPanel panel;
	public SwapFilenamesGUILauncher(){
		this.panel = new JPanel(); 
		this.panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.panel.add(new JLabel("Drop a folder or files Here!"));
		JFrame frame = new JFrame();
		frame.setTitle("SwapFilenames");
		frame.add(this.panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
