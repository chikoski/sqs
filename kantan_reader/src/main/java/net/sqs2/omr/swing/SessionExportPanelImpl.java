package net.sqs2.omr.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;

import net.sqs2.omr.result.event.SpreadSheetExportEventProducer;
import net.sqs2.omr.result.export.MarkReadStatus;

public class SessionExportPanelImpl extends JPanel implements SessionExportPanel {
	private static final long serialVersionUID = 0L;

	private SessionExportProgressPanel sessionExportProgressPanel;
	private SessionExportStatisticsPanel sessionExportStatisticsPanel;

	//private MarkReadStatusPanel markReadStatusPanel;

	
	public SessionExportPanelImpl() {
		this.sessionExportProgressPanel = new SessionExportProgressPanel(this);
		this.sessionExportStatisticsPanel = new SessionExportStatisticsPanel();
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(3, 3, 3, 3));
		add(this.sessionExportProgressPanel, BorderLayout.NORTH);
		add(this.sessionExportStatisticsPanel, BorderLayout.CENTER);
	}
	
	public SessionExportProgressPanel getSessionExportProgressPanel(){
		return sessionExportProgressPanel;
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.swing.SessionExportPanel#updateValue(net.sqs2.omr.result.export.MarkReadStatus)
	 */
	public void updateValue(final MarkReadStatus status) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				sessionExportStatisticsPanel.updateValue(status);
			}
		});
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.swing.SessionExportPanel#reset()
	 */
	public void reset() {
		this.sessionExportProgressPanel.reset();
		this.sessionExportStatisticsPanel.reset();
	}
	
	/* (non-Javadoc)
	 * @see net.sqs2.omr.swing.SessionExportPanel#bindSpreadSheetExportEventProducer(net.sqs2.omr.result.event.SpreadSheetExportEventProducer, net.sqs2.omr.swing.MarkAreasTableModel, net.sqs2.omr.swing.MarkAreasTableModel)
	 */
	public void bindSpreadSheetExportEventProducer(SpreadSheetExportEventProducer eventProducer, 
			MarkAreasTableModel noAnswerMarkAreasTableModel,
			MarkAreasTableModel multipleAnswersAnswerMarkAreasTableModel) {
		eventProducer.addEventConsumer(new SessionExportModel(this, noAnswerMarkAreasTableModel, multipleAnswersAnswerMarkAreasTableModel));
	}
	
	class SessionExportStatisticsPanel extends JPanel {
		private static final long serialVersionUID = 0L;
		NumberFormatter formatter = new NumberFormatter();
		JLabel labelPages = new JLabel();
		JLabel labelTotal = new JLabel();
		JLabel labelSelect1 = new JLabel();
		JLabel labelSelect = new JLabel();

		SessionExportStatisticsPanel() {
			reset();
			// setPreferredSize(new Dimension(380, 60));
			setBackground(Color.WHITE);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2),
					new TitledBorder(Messages.EXPORT_STATISTICS_LABEL)));

			labelSelect1.setToolTipText(Messages.EXPORT_STAT_SELECT1_DETAIL_LABEL);
			labelSelect.setToolTipText(Messages.EXPORT_STAT_SELECT_DETAIL_LABEL);
			labelPages.setBorder(new EmptyBorder(0, 10, 0, 0));
			add(labelPages);
			add(Box.createVerticalStrut(3));
			labelTotal.setBorder(new EmptyBorder(0, 10, 0, 0));
			add(labelTotal);
			add(Box.createVerticalStrut(2));
			labelSelect1.setBorder(new EmptyBorder(0, 20, 0, 0));
			add(labelSelect1);			
			add(Box.createVerticalStrut(2));
			labelSelect.setBorder(new EmptyBorder(0, 20, 0, 0));
			add(labelSelect);
			/*
			 * add(new JLabel("")); add(Box.createVerticalStrut(4)); add(new
			 * JLabel("択一選択マーク数+無回答数+多重マーク数/設問数:  "));
			 * add(Box.createHorizontalStrut(4)); add(new
			 * JLabel("複数選択マーク数/設問数: "));
			 */
		}
		
		public void reset() {
			labelTotal.setText(Messages.EXPORT_STATISTICS_NUMQUESTIONS_TOTAL+ "-----");
			labelSelect1.setText(Messages.EXPORT_STATISTICS_NUMQUESTIONS_SELECT1+ "(---- + -- + --)/----");
			labelSelect.setText(Messages.EXPORT_STATISTICS_NUMQUESTIONS_SELECT+ "(---)/---");
		}
		
		public void updateValue(MarkReadStatus status){
			labelPages.setText(Messages.EXPORT_STATISTICS_NUM_PAGES
					+ " " + status.getNumPages() + "  /  "+Messages.SESSION_PROCESS_NUMFILES_ERROR+" : "+ status.getNumErrorPages() );
			
			labelTotal.setText(Messages.EXPORT_STATISTICS_NUMQUESTIONS_TOTAL
					+ status.getNumQuestions());
			
			labelSelect1.setText(Messages.EXPORT_STATISTICS_NUMQUESTIONS_SELECT1 + "( "
					+ status.getSelect1Status().getNumOneSelectedQuestions() 
					+ " + "
					+ status.getSelect1Status().getNumNoAnsweredQuestions()
					+ " + "
					+ status.getSelect1Status().getNumMultipleAnsweredQuestions()
					+ " ) / "
					+ status.getSelect1Status().getNumQuestions());
			labelSelect.setText(Messages.EXPORT_STATISTICS_NUMQUESTIONS_SELECT
					+ "( "
					+ status.getSelectStatus().getNumMultipleSelectedMarks() + " ) / "
					+ status.getSelectStatus().getNumQuestions());
		}
	}


	class MarkReadStatusPanel extends JPanel {
		private static final long serialVersionUID = 0L;

		MarkReadStatusPanel() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(Box.createVerticalGlue());
			add(new JLabel(Messages.EXPORT_STATISTICS_NUM_PAGES+": "));
			add(Box.createVerticalStrut(4));
			add(new JLabel(Messages.EXPORT_STAT_MARK_NUMTOTAL_LABEL+": "));
			add(Box.createVerticalStrut(4));
			add(new JLabel(Messages.EXPORT_STAT_SELECT1_LABEL+": "));
			add(Box.createHorizontalStrut(4));
			add(new JLabel(Messages.EXPORT_STAT_SELECT_LABEL+": "));
			add(Box.createVerticalGlue());
		}
	}
}

class LabelProgressPanel extends JPanel {
	private static final long serialVersionUID = 0L;
	JProgressBar progressBar;
	private Color BACKGROUND = Color.LIGHT_GRAY;
	private Color FOREGROUND = Color.GRAY;
	
	LabelProgressPanel(String title) {
		setBackground(Color.WHITE);
		this.progressBar = new JProgressBar();
		this.progressBar.setBackground(BACKGROUND);
		this.progressBar.setForeground(FOREGROUND);
		this.progressBar.setStringPainted(true);
		this.progressBar.setString("");
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(1, 1, 1, 1));
		JLabel label = new JLabel(title + "  :  ", JLabel.RIGHT);
		label.setPreferredSize(new Dimension(100, 16));
		add(label, BorderLayout.WEST);
		add(this.progressBar);
	}

	public JProgressBar getProgressBar() {
		return this.progressBar;
	}
}

