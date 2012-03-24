/**
 * 
 */
package net.sqs2.omr.swing.app;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.sqs2.omr.base.Messages;

class MarkReaderSessionResultVisitorProgressBarPanel extends JPanel {
	private static final long serialVersionUID = 0L;
	LabelProgressPanel masterLabel;
	LabelProgressPanel sourceDirectoryLabel;
	LabelProgressPanel spreadSheetLabel;
	LabelProgressPanel rowGroupLabel;
	LabelProgressPanel rowLabel;

	private JProgressBar[] progressBarArray = new JProgressBar[5];

	public MarkReaderSessionResultVisitorProgressBarPanel(MarkReaderSessionResultVisitorPanel sessionExportPanel) {

		this.masterLabel = new LabelProgressPanel(Messages.ITEM_MASTER_LABEL);
		this.sourceDirectoryLabel = new LabelProgressPanel(Messages.ITEM_FOLDER_LABEL);
		this.rowGroupLabel = new LabelProgressPanel(Messages.ITEM_ROWGROUP_LABEL);
		this.rowLabel = new LabelProgressPanel(Messages.ITEM_ROW_LABEL);

		this.progressBarArray = new JProgressBar[] { this.masterLabel.getProgressBar(),
				this.sourceDirectoryLabel.getProgressBar(),
				this.rowGroupLabel.getProgressBar(), this.rowLabel.getProgressBar()
		};
		setBackground(Color.WHITE);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2), new TitledBorder(
				Messages.SESSION_EXPORT_LABEL)));

		add(this.masterLabel);
		add(this.sourceDirectoryLabel);
		add(this.rowGroupLabel);
		add(this.rowLabel);
	}

	JProgressBar[] getProgressBarArray() {
		return this.progressBarArray;
	}

	public void reset() {
		for (JProgressBar progressBar : this.progressBarArray) {
			progressBar.setStringPainted(true);
			progressBar.setValue(0);
			progressBar.setString("");
			progressBar.setIndeterminate(false);
			progressBar.setForeground(Color.GRAY);
		}
	}
}
