/**
 * 
 */
package net.sqs2.omr.swing.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.session.service.PageTaskExecutionProgressModel;
import net.sqs2.omr.swing.pagetask.PageTaskExecutionProgressBar;
import net.sqs2.omr.swing.pagetask.PageTaskExecutionProgressMeter;
import net.sqs2.omr.swing.result.MarkReaderSessionResultBrowserPanel;
import net.sqs2.swing.ImageManager;
import net.sqs2.swing.JTabbedPaneWithBackgroundImage;

public class MarkReaderSessionPanel extends JPanel {

	private static final long serialVersionUID = 0L;

	static final Color DEFAULT_BACKGROUND_COLOR = new Color(224, 240, 255);

	private JTabbedPane tabbedPane;
	protected ConsolePanel consolePanel;

	PageTaskExecutionProgressModel pageTaskExecutionProgressModel;

	PageTaskExecutionProgressPanel pageTaskExecutionProgressPanel;
	SessionResultWalkerBasePanel sessionResultWalkerBasePanel;
	SessionResultBrowserBasePanel sessionResultBrowserBasePanel;

	public MarkReaderSessionResultVisitorPanel getSessionResultWalkerPanel(){
		return this.sessionResultWalkerBasePanel.sessionResultWalkerPanel;
	}

	public MarkReaderSessionResultBrowserPanel getResultBrowserPanel() {
		return this.sessionResultBrowserBasePanel.sessionResultBrowserPanel;
	}

	public MarkReaderSessionPanel(final File sourceDirectoryRoot,
			final PageTaskExecutionProgressModel sessionProgressModel) {
		this.pageTaskExecutionProgressModel = sessionProgressModel;
		this.consolePanel = new ConsolePanel(sourceDirectoryRoot);
		this.tabbedPane = new JTabbedPaneWithBackgroundImage(MarkReaderSessionPanel.DEFAULT_BACKGROUND_COLOR,
				ImageManager.createImage(AppConstants.SUB_ICON));
		this.tabbedPane.setBackground(Color.GRAY);
		this.tabbedPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		this.pageTaskExecutionProgressPanel = new PageTaskExecutionProgressPanel(sessionProgressModel);
		this.sessionResultWalkerBasePanel = new SessionResultWalkerBasePanel();
		this.sessionResultBrowserBasePanel = new SessionResultBrowserBasePanel();
		
		this.tabbedPane.addTab("1. " + Messages.SESSION_READ_LABEL, this.pageTaskExecutionProgressPanel);
		this.tabbedPane.addTab("2. " + Messages.SESSION_EXPORT_LABEL, this.sessionResultWalkerBasePanel);
		this.tabbedPane.addTab("3. " + Messages.SESSION_RESULT_LABEL, this.sessionResultBrowserBasePanel);

		this.tabbedPane.setEnabledAt(1, false);
		this.tabbedPane.setEnabledAt(2, false);

		setLayout(new BorderLayout());
		add(this.consolePanel, BorderLayout.NORTH);
		add(this.tabbedPane, BorderLayout.CENTER);

		this.pageTaskExecutionProgressPanel.setPlayStateGUI();

	}

	class SessionModulePanel extends JPanel {
		private static final long serialVersionUID = 0L;

		SessionModulePanel() {
			setLayout(new BorderLayout());
		}

		protected void setPlayStateGUI() {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			this.repaint();
		}

		protected void setPauseStateGUI() {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			this.repaint();
		}
	}

	class PageTaskExecutionProgressPanel extends SessionModulePanel {
		private static final long serialVersionUID = 0L;

		PageTaskExecutionProgressBar pageTaskExecutionProgressBar;

		public PageTaskExecutionProgressPanel(final PageTaskExecutionProgressModel sessionProgressModel) {
			this.pageTaskExecutionProgressBar = new PageTaskExecutionProgressBar(sessionProgressModel);
			PageTaskExecutionProgressMeter progressMeter = new PageTaskExecutionProgressMeter(sessionProgressModel);
			ProgressPanel progressPanel = new ProgressPanel(progressMeter);
			add(progressPanel, BorderLayout.CENTER);
			add(this.pageTaskExecutionProgressBar, BorderLayout.SOUTH);
			
			//this.pageTaskExecutionProgressBar.addMouseMotionListener(new ProgressBarMouseMotionAdapter());
			this.pageTaskExecutionProgressBar.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent ev) {
					//pageTaskExecutionProgressBar.clear();
				}
			});
			this.pageTaskExecutionProgressBar.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent ev) {
					//pageTaskExecutionProgressBar.clear();
				}
			});
		}

		@Override
		protected void setPlayStateGUI() {
			super.setPlayStateGUI();
			this.pageTaskExecutionProgressBar.setIndeterminate(true);
		}

		@Override
		protected void setPauseStateGUI() {
			super.setPauseStateGUI();
			this.pageTaskExecutionProgressBar.setIndeterminate(false);
		}
	}

	class SessionResultBrowserBasePanel extends SessionModulePanel {
		private static final long serialVersionUID = 0L;
		MarkReaderSessionResultBrowserPanel sessionResultBrowserPanel;

		public SessionResultBrowserBasePanel() {
			sessionResultBrowserPanel = new MarkReaderSessionResultBrowserPanel();
			add(sessionResultBrowserPanel, BorderLayout.CENTER);
		}
	}

	class SessionResultWalkerBasePanel extends SessionModulePanel {
		private static final long serialVersionUID = 0L;
		MarkReaderSessionResultVisitorPanel sessionResultWalkerPanel;

		public SessionResultWalkerBasePanel() {
			this.sessionResultWalkerPanel = new MarkReaderSessionResultVisitorPanel();
			add(this.sessionResultWalkerPanel, BorderLayout.CENTER);
		}

		public void reset() {
			this.sessionResultWalkerPanel.reset();
		}
	}

	class ProgressPanel extends JPanel {
		private static final long serialVersionUID = 0L;
	
		ProgressPanel(PageTaskExecutionProgressMeter progressMeter) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(progressMeter);
			add(Box.createVerticalStrut(10));
			setPreferredSize(new Dimension(150, 150));
		}
	}

	/*
	public void bindSessionResultWalker(SessionResultWalker eventProducer, MarkAreasTableModel noAnswerMarkAreasTableModel, MarkAreasTableModel multipleAnswersAnswerMarkAreasTableModel) {
		this.exportPanel.bindEventConsumer(eventProducer,
				noAnswerMarkAreasTableModel, multipleAnswersAnswerMarkAreasTableModel);
	}*/

	public void reset() {
		this.sessionResultWalkerBasePanel.reset();
	}

	public boolean isProgressTabSelected() {
		return this.tabbedPane.getSelectedIndex() == 0;
	}

	public boolean isExportTabSelected() {
		return this.tabbedPane.getSelectedIndex() == 1;
	}

	public boolean isResultTabSelected() {
		return this.tabbedPane.getSelectedIndex() == 2;
	}

	public void selectProgressTab() {
		this.tabbedPane.setSelectedIndex(0);
		this.tabbedPane.setEnabledAt(1, false);
		this.tabbedPane.setEnabledAt(2, false);
	}

	public void selectExportTab() {
		this.tabbedPane.setEnabledAt(1, true);
		this.tabbedPane.setSelectedIndex(1);
	}

	public void selectResultTab() {
		this.tabbedPane.setEnabledAt(2, true);
		this.tabbedPane.setSelectedIndex(2);
	}

	public PageTaskExecutionProgressModel getSessionProgressModel() {
		return this.pageTaskExecutionProgressModel;
	}

	public void setPauseStateGUI() {
		this.consolePanel.setPauseStateGUI();
		this.pageTaskExecutionProgressPanel.setPauseStateGUI();
		this.sessionResultWalkerBasePanel.setPauseStateGUI();
		this.sessionResultBrowserBasePanel.setPauseStateGUI();
	}

	public void setPlayStateGUI() {
		this.consolePanel.setPlayStateGUI();
		this.pageTaskExecutionProgressPanel.setPlayStateGUI();
		this.sessionResultWalkerBasePanel.setPlayStateGUI();
		this.sessionResultBrowserBasePanel.setPlayStateGUI();
	}

	public void setRestartButtonActionListener(ActionListener listener) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.consolePanel.restartButton.addActionListener(listener);
	}

	public void setPauseButtonActionListener(ActionListener listener) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.consolePanel.pauseButton.addActionListener(listener);
	}

	public void setCloseButtonActionListener(ActionListener listener) {
		this.consolePanel.closeButton.addActionListener(listener);
	}

	public void setConfigButtonActionListener(ActionListener listener) {
		this.consolePanel.configButton.addActionListener(listener);
	}

}
