package net.sqs2.omr.swing.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.session.output.MarkReadStatus;

public class MarkReaderSessionResultVisitorPanel extends JPanel{
	private static final long serialVersionUID = 0L;

	private MarkReaderSessionResultVisitorProgressBarPanel sessionResultWalkerProgressBarPanel;
	private SessionResultStatisticsPanel sessionResultStatisticsPanel;

	public MarkReaderSessionResultVisitorPanel() {
		this.sessionResultWalkerProgressBarPanel = new MarkReaderSessionResultVisitorProgressBarPanel(this);
		this.sessionResultStatisticsPanel = new SessionResultStatisticsPanel();
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(3, 3, 3, 3));
		add(this.sessionResultWalkerProgressBarPanel, BorderLayout.NORTH);
		add(this.sessionResultStatisticsPanel, BorderLayout.CENTER);
	}
	
	public MarkReaderSessionResultVisitorProgressBarPanel getSessionExportProgressPanel() {
		return this.sessionResultWalkerProgressBarPanel;
	}
	
	public SessionResultStatisticsPanel getSessionResultStatisticsPanel() {
		return sessionResultStatisticsPanel;
	}

	/*
	private void setState(MarkAreaErrorModel.State state){
		if(state == MarkAreaErrorModel.State.RESET){
			this.reset();
			for (JProgressBar progressBar : this.sessionResultWalkerProgressBarPanel.getProgressBarArray()) {
				if (0 < progressBar.getMaximum()) {
					progressBar.setIndeterminate(true);
				}
			}
		}else if(state == MarkAreaErrorModel.State.FINISHED){
			for (JProgressBar progressBar : this.sessionResultWalkerProgressBarPanel.getProgressBarArray()) {
				if (0 < progressBar.getMaximum()) {
					progressBar.setIndeterminate(false);
				}
			}
		}
	}
	*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sqs2.omr.swing.SessionExportPanel#updateValue(net.sqs2.omr.result
	 * .export.MarkReadStatus)
	 */
	public void updateValue(final MarkReadStatus status) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MarkReaderSessionResultVisitorPanel.this.sessionResultStatisticsPanel.updateValue(status);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sqs2.omr.swing.SessionExportPanel#reset()
	 */
	public void reset() {
		this.sessionResultWalkerProgressBarPanel.reset();
		this.sessionResultStatisticsPanel.reset();
	}

	class SessionResultStatisticsPanel extends JPanel {
		private static final long serialVersionUID = 0L;
		NumberFormatter formatter = new NumberFormatter();
		JLabel labelPages = new JLabel();
		JLabel labelTotal = new JLabel();
		JLabel labelSelect1 = new JLabel();
		JLabel labelSelect = new JLabel();

		SessionResultStatisticsPanel() {
			reset();
			// setPreferredSize(new Dimension(380, 60));
			setBackground(Color.WHITE);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2), new TitledBorder(
					Messages.EXPORT_STATISTICS_LABEL)));

			this.labelSelect1.setToolTipText(Messages.EXPORT_STAT_SELECT1_DETAIL_LABEL);
			this.labelSelect.setToolTipText(Messages.EXPORT_STAT_SELECT_DETAIL_LABEL);
			this.labelPages.setBorder(new EmptyBorder(0, 10, 0, 0));
			add(this.labelPages);
			add(Box.createVerticalStrut(3));
			this.labelTotal.setBorder(new EmptyBorder(0, 10, 0, 0));
			add(this.labelTotal);
			add(Box.createVerticalStrut(2));
			this.labelSelect1.setBorder(new EmptyBorder(0, 20, 0, 0));
			add(this.labelSelect1);
			add(Box.createVerticalStrut(2));
			this.labelSelect.setBorder(new EmptyBorder(0, 20, 0, 0));
			add(this.labelSelect);
		}

		public void reset() {
			this.labelTotal.setText(Messages.EXPORT_STATISTICS_NUMQUESTIONS_TOTAL + "-----");
			this.labelSelect1.setText(Messages.EXPORT_STATISTICS_NUMQUESTIONS_SELECT1
					+ "(---- + -- + --)/----");
			this.labelSelect.setText(Messages.EXPORT_STATISTICS_NUMQUESTIONS_SELECT + "(---)/---");
		}

		public void updateValue(MarkReadStatus status) {
			this.labelPages.setText(Messages.EXPORT_STATISTICS_NUM_PAGES + " " + status.getNumPages()
					+ "  /  " + Messages.SESSION_PROCESS_NUMFILES_ERROR + " : " + status.getNumErrorPages());

			this.labelTotal.setText(Messages.EXPORT_STATISTICS_NUMQUESTIONS_TOTAL + status.getNumQuestions());

			this.labelSelect1.setText(Messages.EXPORT_STATISTICS_NUMQUESTIONS_SELECT1 + "( "
					+ status.getSelect1Status().getNumOneSelectedQuestions() + " + "
					+ status.getSelect1Status().getNumNoAnsweredQuestions() + " + "
					+ status.getSelect1Status().getNumMultipleAnsweredQuestions() + " ) / "
					+ status.getSelect1Status().getNumQuestions());
			this.labelSelect.setText(Messages.EXPORT_STATISTICS_NUMQUESTIONS_SELECT + "( "
					+ status.getSelectStatus().getNumMultipleSelectedMarks() + " ) / "
					+ status.getSelectStatus().getNumQuestions());
		}
	}

	class MarkReadStatusPanel extends JPanel {
		private static final long serialVersionUID = 0L;

		MarkReadStatusPanel() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(Box.createVerticalGlue());
			add(new JLabel(Messages.EXPORT_STATISTICS_NUM_PAGES + ": "));
			add(Box.createVerticalStrut(4));
			add(new JLabel(Messages.EXPORT_STAT_MARK_NUMTOTAL_LABEL + ": "));
			add(Box.createVerticalStrut(4));
			add(new JLabel(Messages.EXPORT_STAT_SELECT1_LABEL + ": "));
			add(Box.createHorizontalStrut(4));
			add(new JLabel(Messages.EXPORT_STAT_SELECT_LABEL + ": "));
			add(Box.createVerticalGlue());
		}
	}
}

class LabelProgressPanel extends JPanel {
	private static final long serialVersionUID = 0L;
	JProgressBar progressBar;
	private String title;
	private Color BACKGROUND = Color.LIGHT_GRAY;
	private Color FOREGROUND = Color.GRAY;

	LabelProgressPanel(String title) {
		this.title = title;
		setBackground(Color.WHITE);
		this.progressBar = new JProgressBar();
		this.progressBar.setBackground(this.BACKGROUND);
		this.progressBar.setForeground(this.FOREGROUND);
		this.progressBar.setStringPainted(true);
		this.progressBar.setString("");
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(1, 1, 1, 1));
		JLabel label = new JLabel(title + "  :  ", SwingConstants.RIGHT);
		label.setPreferredSize(new Dimension(100, 16));
		add(label, BorderLayout.WEST);
		add(this.progressBar);
	}
	
	public String getTitle(){
		return title;
	}

	public JProgressBar getProgressBar() {
		return this.progressBar;
	}
}
