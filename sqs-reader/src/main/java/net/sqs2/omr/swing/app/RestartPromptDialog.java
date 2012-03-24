/**
 * 
 */
package net.sqs2.omr.swing.app;

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
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import net.sqs2.omr.swing.Images;

public class RestartPromptDialog extends JDialog {

	private static final long serialVersionUID = 0L;

	JButton cancelButton;
	JButton restartOnUpdatedFilesButton = null;
	JButton restartOnAllFilesButton = null;

	public RestartPromptDialog(Frame frame, String title, JPanel panel, String restartOnAllLabel,
			String restartOnUpdatedLabel, String cancelButtonLabel) {
		super(frame, false);
		Image transparent16x16 = Images.TRANSPARENT_16X16;
		setIconImage(transparent16x16);
		setTitle(title);
		setLayout(new BorderLayout());
		OKButtonPanel okButtonPanel = new OKButtonPanel(restartOnAllLabel, restartOnUpdatedLabel,
				cancelButtonLabel);
		add(panel, BorderLayout.CENTER);
		add(okButtonPanel, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		AbstractAction act = new AbstractAction("OK") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		};
		InputMap imap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close-it");
		getRootPane().getActionMap().put("close-it", act);
		pack();
	}

	public JButton getCancelButton() {
		return this.cancelButton;
	}

	public JButton getRestartOnUpdatedFilesButton() {
		return this.restartOnUpdatedFilesButton;
	}

	public JButton getRestartOnAllFilesButton() {
		return this.restartOnAllFilesButton;
	}

	class OKButtonPanel extends JPanel {
		private static final long serialVersionUID = 0L;

		OKButtonPanel(String restartOnAllLabel, String restartOnUpdatedLabel, String cancelButtonLabel) {
			RestartPromptDialog.this.restartOnAllFilesButton = new JButton(restartOnAllLabel,
					Images.REFRESH_ICON);
			RestartPromptDialog.this.restartOnUpdatedFilesButton = new JButton(restartOnUpdatedLabel,
					Images.REFRESH_ICON);
			RestartPromptDialog.this.cancelButton = new JButton(cancelButtonLabel, Images.ACCEPT_ICON);

			setBorder(new EmptyBorder(3, 10, 8, 10));
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(Box.createHorizontalGlue());
			add(RestartPromptDialog.this.restartOnUpdatedFilesButton);
			add(Box.createHorizontalStrut(10));
			add(RestartPromptDialog.this.restartOnAllFilesButton);
			add(Box.createHorizontalStrut(20));
			add(RestartPromptDialog.this.cancelButton);
			add(Box.createHorizontalGlue());
			RestartPromptDialog.this.cancelButton.setFocusCycleRoot(true);
		}
	}

	public static void main(String args[]) {
		RestartPromptDialog dialog = new RestartPromptDialog(null, "", null, "cancel", "restart on all",
				"restart on updated"); // model
		dialog.setVisible(true);
	}

	/*
	 * public void
	 * bindSpreadSheetExportEventProducer(SpreadSheetExportEventProducer
	 * spreadSheetExportEventProducer){
	 * fixPagePanel.bindSpreadSheetExportEventProducer
	 * (spreadSheetExportEventProducer); }
	 */

}
