/**
 * 
 */
package net.sqs2.omr.swing;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

class RestartPromptDialog extends JDialog{
	
	private static final long serialVersionUID = 0L;

	JButton cancelButton;
	JButton restartOnUpdatedFilesButton = null;
	JButton restartOnAllFilesButton = null;
	
	RestartPromptDialog(Frame frame, String title, JPanel panel, String cancelButtonLabel, String restartOnAllLabel, String restartOnUpdatedLabel){
		super(frame, false);
		Image transparent16x16 = ImageManager.TRANSPARENT_16X16;
		setIconImage(transparent16x16);
		setTitle(title);
		setLayout(new BorderLayout());
		OKButtonPanel okButtonPanel = new OKButtonPanel(cancelButtonLabel, restartOnAllLabel, restartOnUpdatedLabel);
		add(panel, BorderLayout.CENTER);
		add(okButtonPanel, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		AbstractAction act = new AbstractAction("OK") {
			  public void actionPerformed(ActionEvent e) {
			    setVisible(false);
			  }
			};
		InputMap imap = getRootPane().getInputMap(
		JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close-it");
		getRootPane().getActionMap().put("close-it", act);
		pack();
	}
	
	public JButton getCancelButton(){
		return this.cancelButton;
	}
	
	public JButton getRestartOnUpdatedFilesButton() {
		return restartOnUpdatedFilesButton;
	}

	public JButton getRestartOnAllFilesButton() {
		return restartOnAllFilesButton;
	}

	class OKButtonPanel extends JPanel{
		private static final long serialVersionUID = 0L;
		OKButtonPanel(String cancelButtonLabel, String restartOnAllLabel, String restartOnUpdatedLabel){
			cancelButton = new JButton(cancelButtonLabel);
			restartOnAllFilesButton = new JButton(restartOnAllLabel);
			restartOnUpdatedFilesButton = new JButton(restartOnUpdatedLabel);

			setBorder(new EmptyBorder(3, 10, 8, 10));
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(Box.createHorizontalGlue());
			add(cancelButton);
			add(Box.createHorizontalStrut(16));
			add(restartOnAllFilesButton);
			add(Box.createHorizontalStrut(16));
			add(restartOnUpdatedFilesButton);
			add(Box.createHorizontalGlue());
		}
	}
	
	void closeDialog(){
		this.setVisible(false);
	}
	
	public static void main(String args[]){
		RestartPromptDialog dialog = new RestartPromptDialog(null, "", null, "cancel", "restart on all", "restart on updated"); // model 
		dialog.setVisible(true);
	}
	
	/*
	public void bindSpreadSheetExportEventProducer(SpreadSheetExportEventProducer spreadSheetExportEventProducer){
		fixPagePanel.bindSpreadSheetExportEventProducer(spreadSheetExportEventProducer);
	}*/

}