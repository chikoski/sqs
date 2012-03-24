/*
 * 
   LoggerConsoleFrame.java

   Copyright 2004 KUBO Hiroya (hiroya@cuc.ac.jp).

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package net.sqs2.swing;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import org.apache.avalon.framework.logger.Logger;

class AbstractLogger extends JFrame implements Logger {
	public static final long serialVersionUID = 0;
	Logger logger;
	boolean debugEnabled;
	boolean hasError = false;
	boolean hasWarn = false;
	boolean isSucceed = false;

	public Logger getLogger() {
		return logger;
	}

	public void setDebugEnabled(boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#debug(java.lang.String,
	 * java.lang.Throwable)
	 */
	public void debug(String arg0, Throwable arg1) {
		if (debugEnabled) {
			logger.debug(arg0, arg1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#debug(java.lang.String)
	 */
	public void debug(String arg0) {
		if (debugEnabled) {
			logger.debug(arg0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#error(java.lang.String,
	 * java.lang.Throwable)
	 */
	public void error(String arg0, Throwable arg1) {
		logger.error(arg0, arg1);
		hasError = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#error(java.lang.String)
	 */
	public void error(String arg0) {
		logger.error(arg0);
		hasError = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.avalon.framework.logger.Logger#fatalError(java.lang.String,
	 * java.lang.Throwable)
	 */
	public void fatalError(String arg0, Throwable arg1) {
		logger.fatalError(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.avalon.framework.logger.Logger#fatalError(java.lang.String)
	 */
	public void fatalError(String arg0) {
		logger.fatalError(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.avalon.framework.logger.Logger#getChildLogger(java.lang.String
	 * )
	 */
	public Logger getChildLogger(String arg0) {
		return logger.getChildLogger(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#info(java.lang.String,
	 * java.lang.Throwable)
	 */
	public void info(String arg0, Throwable arg1) {
		logger.info(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#info(java.lang.String)
	 */
	public void info(String arg0) {
		logger.info(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#isDebugEnabled()
	 */
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#isErrorEnabled()
	 */
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#isFatalErrorEnabled()
	 */
	public boolean isFatalErrorEnabled() {
		return logger.isFatalErrorEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#isInfoEnabled()
	 */
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#isWarnEnabled()
	 */
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#warn(java.lang.String,
	 * java.lang.Throwable)
	 */
	public void warn(String arg0, Throwable arg1) {
		logger.warn(arg0, arg1);
		hasWarn = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#warn(java.lang.String)
	 */
	public void warn(String arg0) {
		logger.warn(arg0);
		hasWarn = true;
	}

	public void setError(Throwable ex) {
		logger.error("", ex);
	}

	public void setError(String message, Throwable ex) {
		logger.error(message, ex);
	}

	public boolean hasError() {
		return hasError;
	}

	public boolean hasWarn() {
		return hasWarn;
	}

	public void setSucceed() {
		if (!hasError) {
			isSucceed = true;
		}
	}
}

class ProgressLogger extends AbstractLogger {
	public static final long serialVersionUID = 0;
	JProgressBar progressBar;

	ProgressLogger() {
		this.progressBar = createProgressBar();
	}

	private JProgressBar createProgressBar() {
		progressBar = new JProgressBar();
		progressBar.setMinimumSize(new Dimension(480, 24));
		return progressBar;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#error(java.lang.String,
	 * java.lang.Throwable)
	 */
	public void error(String arg0, Throwable arg1) {
		super.error(arg0, arg1);
		progressBar.setForeground(Color.red);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#error(java.lang.String)
	 */
	public void error(String arg0) {
		super.error(arg0);
		progressBar.setForeground(Color.red);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.avalon.framework.logger.Logger#fatalError(java.lang.String,
	 * java.lang.Throwable)
	 */
	public void fatalError(String arg0, Throwable arg1) {
		super.fatalError(arg0, arg1);
		progressBar.setForeground(Color.red);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.avalon.framework.logger.Logger#fatalError(java.lang.String)
	 */
	public void fatalError(String arg0) {
		super.fatalError(arg0);
		progressBar.setForeground(Color.red);
	}

	public void warn(String arg0, Throwable arg1) {
		super.warn(arg0, arg1);
		progressBar.setForeground(Color.magenta);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#warn(java.lang.String)
	 */
	public void warn(String arg0) {
		super.warn(arg0);
		progressBar.setForeground(Color.magenta);
	}

	public void setFinished(boolean isFinished) {
		if (isFinished) {
			if (isSucceed) {
				if (hasWarn) {
					progressBar.setForeground(java.awt.Color.magenta);
				} else {
					progressBar.setForeground(java.awt.Color.green);
				}
			} else {
				if (hasError) {
					progressBar.setForeground(java.awt.Color.red);
				}
			}
			progressBar.setMaximum(100);
			progressBar.setValue(100);
			progressBar.setIndeterminate(false);
		} else {
			if (logger instanceof LoggerTextArea) {
				((LoggerTextArea) logger).clear();
			}
			hasError = false;
			isSucceed = false;
			progressBar.setForeground(SwingUtil.FORGROUND_COLOR);
			progressBar.setIndeterminate(true);
		}
	}

}

class ButtonPanelFunction {
	/**
	 * @return
	 */
	static Box createButtonPanel(JButton okButton, JButton cancelButton) {
		Box buttonPanel = Box.createHorizontalBox();
		buttonPanel.add(Box.createHorizontalStrut(64));
		buttonPanel.add(cancelButton);
		buttonPanel.add(Box.createHorizontalStrut(64));
		buttonPanel.add(okButton);
		buttonPanel.add(Box.createHorizontalStrut(64));
		buttonPanel.setBorder(new EmptyBorder(5, 3, 5, 3));
		return buttonPanel;
	}

	/**
	 * @param cancelButtonLabel
	 */
	static JButton createCancelButton(String cancelButtonLabel) {
		JButton cancelButton = new JButton(cancelButtonLabel);
		cancelButton.setEnabled(true);
		return cancelButton;
	}

	/**
	 * @param finishButtonLabel
	 */
	static JButton createOKButton(String finishButtonLabel) {
		JButton okButton = new JButton(finishButtonLabel);
		okButton.setEnabled(false);
		return okButton;
	}
}

class ButtonControllableProgressLogger extends ProgressLogger {
	public static final long serialVersionUID = 0;
	ButtonControllableProgressLogger self = null;
	JButton okButton, cancelButton;
	JComponent base;
	boolean isFinished = false;

	ButtonControllableProgressLogger(String title, String cancelButtonLabel, String finishButtonLabel) {
		self = this;
		this.base = Box.createVerticalBox();
		base.setBorder(new EmptyBorder(15, 5, 10, 5));
		base.add(this.progressBar);
		this.okButton = ButtonPanelFunction.createOKButton(finishButtonLabel);
		this.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				self.setVisible(false);
			}
		});
		this.cancelButton = ButtonPanelFunction.createCancelButton(cancelButtonLabel);
		base.add(ButtonPanelFunction.createButtonPanel(this.okButton, this.cancelButton));
	}

	public JButton getOKButton() {
		return okButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		super.setFinished(isFinished);
		if (isFinished) {
			okButton.setEnabled(true);
			cancelButton.setEnabled(false);
		} else {
			okButton.setEnabled(false);
			cancelButton.setEnabled(true);
		}
		this.isFinished = isFinished;
	}
}

public class LoggerConsoleFrame extends ButtonControllableProgressLogger {
	public static final long serialVersionUID = 0;
	JScrollPane scrollPane;

	public LoggerConsoleFrame(String title, String cancelButtonLabel, String finishButtonLabel) {
		super(title, cancelButtonLabel, finishButtonLabel);
		setTitle(title);
		this.logger = createLogger(title);
		this.scrollPane = createScrollPane();
		this.addWindowFocusListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				if (logger instanceof LoggerTextArea) {
					((LoggerTextArea) logger).setAutoScrollToTail(false);
				}
			}

			public void windowLostFocus(WindowEvent e) {
				if (logger instanceof LoggerTextArea) {
					((LoggerTextArea) logger).setAutoScrollToTail(true);
				}
			}
		});
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent ev) {
				doLayout();
				repaint();
			}
		});
		initialize();
	}

	public Logger createLogger(String title) {
		return new LoggerTextArea(title, 1, 32);
	}

	public JComponent getBase() {
		return base;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void initialize() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(this.base, BorderLayout.NORTH);
		getContentPane().add(this.scrollPane, BorderLayout.CENTER);
		/*
		 * this.addComponentListener(new ComponentAdapter(){ public void
		 * componentResized(ComponentEvent ev){
		 * scrollPane.setSize(getSize().width-10, getSize().height-90);
		 * if(logger instanceof LoggerTextArea){
		 * ((LoggerTextArea)logger).setSize(getSize().width-10,
		 * getSize().height-90); } //pack(); } });
		 */
		setSize(480, 300);
		// setVisible(true);
	}

	/**
	 * 
	 */
	private JScrollPane createScrollPane() {
		if (logger instanceof LoggerTextArea) {
			JScrollPane scrollPane = new JScrollPane((LoggerTextArea) logger);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			// scrollPane.setPreferredSize(new Dimension(440, 0));
			// scrollPane.setMinimumSize(new Dimension(440, 0));
			return scrollPane;
		} else {
			return new JScrollPane();
		}
	}
	/**
	 * 
	 */
}
