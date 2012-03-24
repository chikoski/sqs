package net.sqs2.omr.swing.result;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.swing.Images;

public class MarkReaderSessionResultBrowserPanel extends JPanel {
	private static final long serialVersionUID = 0L;

	FormMaster master;
	
	JButton resultBrowserButton;
	JButton masterButton;
	
	JButton excelButton;
	JButton csvButton;
	JButton textareaButton;
	JButton chartButton;

	public MarkReaderSessionResultBrowserPanel(FormMaster master) {
		this();
		this.master = master;
	}

	public MarkReaderSessionResultBrowserPanel() {
		this.resultBrowserButton = new JButton(Messages.RESULT_BROWSER_BUTTON_LABEL);
		this.masterButton = new JButton(Messages.RESULT_MASTER_LABEL);
		this.excelButton = new JButton("Excel", Images.PAGE_EXCEL_ICON);
		this.csvButton = new JButton("CSV", Images.PAGE_EXCEL_ICON);
		this.textareaButton = new JButton(Messages.RESULT_TEXTAREA_LABEL, Images.PAGE_WHITE_EDIT_ICON);
		this.chartButton = new JButton(Messages.RESULT_CHART_LABEL, Images.CHART_PIE_ICON);
		setLayout(new BorderLayout());
		add(new CenterPanel(), BorderLayout.CENTER);
	}

	public JButton getResultBrowserButton() {
		return this.resultBrowserButton;
	}

	public JButton getMasterButton() {
		return this.masterButton;
	}

	public JButton getExcelButton() {
		return this.excelButton;
	}

	public JButton getCsvButton() {
		return this.csvButton;
	}

	public JButton getTextareaButton() {
		return this.textareaButton;
	}

	public JButton getChartButton() {
		return this.chartButton;
	}

	class CenterPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		// private static final long serialVersionUID = 0L;
		CenterPanel() {
			setBackground(Color.WHITE);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(Box.createVerticalGlue());
			if (MarkReaderSessionResultBrowserPanel.this.master != null) {
				JPanel u = new UpperPanel();
				add(u);
			}

			add(new MiddlePanel());
			add(new LowerPanel());
			add(Box.createVerticalGlue());
		}

		class UpperPanel extends JPanel {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public UpperPanel() {
				setBackground(Color.WHITE);
				JTextField pathField = new JTextField(MarkReaderSessionResultBrowserPanel.this.master.getFileResourceID()
						.getRelativePath());
				pathField.setEditable(false);
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				add(Box.createHorizontalGlue());
				add(new ItemPanel(Messages.RESULT_MASTER_LABEL,
						new JComponent[] { MarkReaderSessionResultBrowserPanel.this.masterButton }));
				add(Box.createVerticalStrut(5));
				add(pathField);
				add(Box.createHorizontalGlue());
			}
		}

		class MiddlePanel extends JPanel {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public MiddlePanel() {
				resultBrowserButton.setEnabled(false);
				setBackground(Color.WHITE);
				setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
				add(Box.createHorizontalGlue());
				add(resultBrowserButton);
				add(Box.createHorizontalGlue());
			}
		}

		class LowerPanel extends JPanel {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public LowerPanel() {
				setBackground(Color.WHITE);
				setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
				add(Box.createHorizontalGlue());
				add(new ItemPanel(Messages.RESULT_TABLE_LABEL, new JComponent[] {
						MarkReaderSessionResultBrowserPanel.this.excelButton, MarkReaderSessionResultBrowserPanel.this.csvButton }));
				add(Box.createHorizontalStrut(4));
				add(new ItemPanel(Messages.RESULT_TEXTAREA_LABEL,
						new JComponent[] { MarkReaderSessionResultBrowserPanel.this.textareaButton }));
				add(Box.createHorizontalStrut(4));
				add(new ItemPanel(Messages.RESULT_CHART_LABEL,
						new JComponent[] { MarkReaderSessionResultBrowserPanel.this.chartButton }));
				add(Box.createHorizontalGlue());
			}
		}
	}

	static class ItemPanel extends JPanel {
		private static final long serialVersionUID = 0L;
		String title;

		ItemPanel(String title, JComponent[] componentArray) {
			this.title = title;
			setBackground(Color.WHITE);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBorder(new TitledBorder(title));
			boolean isFirst = true;
			for (JComponent coponent : componentArray) {
				if (isFirst) {
					isFirst = false;
				} else {
					add(Box.createVerticalStrut(16));
				}
				add(coponent);
			}
		}
	}
}
