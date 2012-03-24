/**
 * 
 */
package net.sqs2.omr.swing;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

class SessionExportProgressPanel extends JPanel {
	/**
	 * 
	 */
	private final SessionExportPanel sessionExportPanel;
	private static final long serialVersionUID = 0L;
	LabelProgressPanel masterLabel;
	LabelProgressPanel sourceDirectoryLabel;
	LabelProgressPanel spreadSheetLabel;
	LabelProgressPanel rowGroupLabel;
	LabelProgressPanel rowLabel;
	//LabelProgressPanel questionLabel;

	private JProgressBar[] progressBarArray = new JProgressBar[5];

	public static final String MASTER_LABEL = "master";
	public static final String SOURCE_FOLDER_LABEL = "source";
	public static final String SPREADSHEET_LABEL = "spreadsheet";
	public static final String ROWGROUP_LABEL = "rowgroup";
	public static final String ROW_LABEL = "row";
	//public static final String QUESTION_LABEL = "question";
	

	public SessionExportProgressPanel(SessionExportPanel sessionExportPanel) {

		this.sessionExportPanel = sessionExportPanel;
		this.masterLabel = new LabelProgressPanel("調査票");
		this.sourceDirectoryLabel = new LabelProgressPanel("フォルダ");
		// this.spreadSheetLabel = new LabelProgressPanel("spreadSheet");
		this.rowGroupLabel = new LabelProgressPanel("行グループ");
		this.rowLabel = new LabelProgressPanel("行");
		//this.questionLabel = new LabelProgressPanel("question");

		this.progressBarArray = new JProgressBar[] {
				this.masterLabel.getProgressBar(),
				this.sourceDirectoryLabel.getProgressBar(),
				// this.spreadSheetLabel.getProgressBar(),
				this.rowGroupLabel.getProgressBar(),
				this.rowLabel.getProgressBar()
				//this.questionLabel.getProgressBar() 
				};
		setBackground(Color.WHITE);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2),
				new TitledBorder(Messages.SESSION_EXPORT_LABEL)));

		add(this.masterLabel);
		add(this.sourceDirectoryLabel);
		// add(this.spreadSheetLabel);
		add(this.rowGroupLabel);
		add(this.rowLabel);
		//add(this.questionLabel);
	}

	JProgressBar[] getProgressBarArray(){
		return progressBarArray;
	}
	
	public void reset() {
				
		for (JProgressBar progressBar : progressBarArray) {
			progressBar.setStringPainted(true);
			progressBar.setValue(0);
			progressBar.setString("");
			progressBar.setIndeterminate(false);
			progressBar.setForeground(Color.GRAY);
		}
	}

}