/*
 * 

 ProgressBarPanel.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

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

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

public class ProgressBarPanel extends JPanel {

	private static final long serialVersionUID = 0L;

	private JProgressBar bar;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane();
		frame.add(new ProgressBarPanel("TEST", "test"));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public ProgressBarPanel(String title, String message) {
		// setSize(250, 100);
		this.bar = new JProgressBar();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalGlue());
		add(new CenterVAlignedPanel(new ProgressBarPanelCore(message)));
		add(Box.createVerticalGlue());
	}

	public JProgressBar getProgressBar() {
		return this.bar;
	}

	static class CenterAlignedPanel extends JPanel {
		private static final long serialVersionUID = 0L;

		CenterAlignedPanel(Component comp) {
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(comp);
		}
	}

	static class CenterVAlignedPanel extends JPanel {
		private static final long serialVersionUID = 0L;

		CenterVAlignedPanel(Component comp) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(comp);
		}
	}

	class ProgressBarPanelCore extends JPanel {
		private static final long serialVersionUID = 0L;

		ProgressBarPanelCore(String message) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBorder(new EmptyBorder(10, 10, 10, 10));
			add(new CenterAlignedPanel(new JLabel(message + " " + bar.getValue() + "/"
					+ (bar.getMaximum() - bar.getMinimum()))));
			add(new CenterAlignedPanel(bar));
			add(Box.createVerticalStrut(8));
			add(new CenterAlignedPanel(new JButton("Cancel")));
		}
	}
}
