/*
 * 
   LoggerTextArea.java

   Copyright 2004 KUBO Hiroya (hiroya@sfc.keio.ac.jp).

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

import java.awt.Font;
import java.awt.Point;

import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.text.PlainDocument;

import org.apache.avalon.framework.logger.Logger;

class LoggerTextArea extends JTextArea implements Logger {
	public static final long serialVersionUID = 0;
	boolean isError = false;
	JViewport viewPort;
	boolean autoScrollToTail = true;

	public LoggerTextArea(String title, int rows, int cols) {
		super(new PlainDocument(), title, rows, cols);
		setFont(new Font("Courier", Font.PLAIN, 10));
	}

	public void setAutoScrollToTail(boolean autoScrollToTail) {
		this.autoScrollToTail = autoScrollToTail;
	}

	public void clear() {
		setText("");
	}

	public void append(String message) {
		append(message, null);
	}

	public void append(String message, Throwable throwable) {
		if (true) {
			if (throwable != null) {
				super.append("" + message);
				super.append("\n");
				while (throwable != null) {
					super.append(throwable.getLocalizedMessage());
					throwable = throwable.getCause();
					super.append("\n");
				}
			} else {
				super.append("" + message);
				super.append("\n");
			}

			if (this.viewPort == null) {
				this.viewPort = (JViewport) this.getParent();
				this.viewPort.setAutoscrolls(true);
			}
			if (autoScrollToTail) {
				viewPort.setViewPosition(new Point(0, (int) (viewPort.getViewSize().getHeight() - viewPort
						.getExtentSize().getHeight())));
			}
		}
		/*
		 * java.awt.Dimension size = this.getSize(); if(0 < size.getHeight()){
		 * Rectangle rect = new Rectangle(0, (int)size.getHeight()-1, 1, 1); }
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#debug(java.lang.String,
	 * java.lang.Throwable)
	 */
	public void debug(String message, Throwable throwable) {
		append("[debug]" + message, throwable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#debug(java.lang.String)
	 */
	public void debug(String message) {
		append("[debug]" + message, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#error(java.lang.String,
	 * java.lang.Throwable)
	 */
	public void error(String message, Throwable throwable) {
		append("[error]" + message, throwable);
		isError = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#error(java.lang.String)
	 */
	public void error(String message) {
		append("[error]" + message);
		isError = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.avalon.framework.logger.Logger#fatalError(java.lang.String,
	 * java.lang.Throwable)
	 */
	public void fatalError(String message, Throwable throwable) {
		append("[fatal]" + message, throwable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.avalon.framework.logger.Logger#fatalError(java.lang.String)
	 */
	public void fatalError(String message) {
		append("[fatal]" + message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.avalon.framework.logger.Logger#getChildLogger(java.lang.String
	 * )
	 */
	public Logger getChildLogger(String message) {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#info(java.lang.String,
	 * java.lang.Throwable)
	 */
	public void info(String message, Throwable throwable) {
		append("[info]" + message, throwable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#info(java.lang.String)
	 */
	public void info(String message) {
		append("[info]" + message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#isDebugEnabled()
	 */
	public boolean isDebugEnabled() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#isErrorEnabled()
	 */
	public boolean isErrorEnabled() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#isFatalErrorEnabled()
	 */
	public boolean isFatalErrorEnabled() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#isInfoEnabled()
	 */
	public boolean isInfoEnabled() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#isWarnEnabled()
	 */
	public boolean isWarnEnabled() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#warn(java.lang.String,
	 * java.lang.Throwable)
	 */
	public void warn(String message, Throwable throwable) {
		append("[warn]" + message, throwable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.logger.Logger#warn(java.lang.String)
	 */
	public void warn(String message) {
		append("[warn]" + message);
	}

}
