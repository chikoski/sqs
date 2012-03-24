package net.sqs2.omr.swing.pagetask;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TaskQueueViewPanel extends JPanel {
	
	public static void main(String[] args){
		JFrame frame = new JFrame();
		JPanel panel = new TaskQueueViewPanel();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	TaskQueueViewPanel(){
		setPreferredSize(new Dimension(400,400));
	}
	

}
