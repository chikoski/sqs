/*
 * 
   SwingUtil.java

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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.WindowAdapter;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

public class SwingUtil {

	public final static Color FORGROUND_COLOR = new java.awt.Color(153, 153, 204);

	public static WindowAdapter createConfirmOnExitAdapter(final JFrame frame, final String title, final String message) {
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		return new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent ev) {
				int result = JOptionPane.showConfirmDialog(frame, message, title, JOptionPane.YES_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					frame.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					System.exit(0);
				}
			}
		};
	}

	public static SuffixBasedFileFilter createSuffixBasedFileFilter(final String suffix, final String description) {
		return new SuffixBasedFileFilter(suffix, description);
	}

	public abstract static class Factory {
		public abstract Object create() throws Exception;
	}

	public static Object newInstanceWithWaitPromptFrame(Factory factory, String title, String message) throws Exception {
		JProgressBar progressBar = createProgressBar();
		JPanel panel = createPanel(message, progressBar);
		JFrame frame = createFrame(title, panel);
		Object instance = factory.create();
		closeFrame(frame, progressBar);
		return instance;
	}

	/**
	 * @param message
	 * @param progressBar
	 * @return
	 */
	private static JPanel createPanel(String message, JProgressBar progressBar) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.add(new JLabel(message));
		panel.add(progressBar);
		return panel;
	}

	/**
	 * @return
	 */
	private static JProgressBar createProgressBar() {
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setIndeterminate(false);
		progressBar.setMaximum(100);
		progressBar.setValue(100);
		return progressBar;
	}

	/**
	 * @param frame
	 * @param progressBar
	 */
	private static void closeFrame(JFrame frame, JProgressBar progressBar) {
		frame.setVisible(false);
		frame.setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * @param title
	 * @param frame
	 * @param panel
	 */
	private static JFrame createFrame(String title, JPanel panel) {
		JFrame frame = new JFrame();
		frame.setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setTitle(title);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
		return frame;
	}
}
