/*

 ConsolePanel.java

 Copyright 2009 KUBO Hiroya (hiroya@cuc.ac.jp).

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
package net.sqs2.omr.swing.app;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.metal.MetalIconFactory;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.swing.Images;

class ConsolePanel extends JPanel {
	private static final long serialVersionUID = 0L;
	protected JTextField sourceDirectoryPathField;
	protected JButton restartButton;
	protected JButton pauseButton;
	protected JButton configButton;
	protected JButton closeButton;

	static class ConsolePanelButton extends JButton {

		static final Border EMPTY_BORDER = new EmptyBorder(6, 1, 6, 1);
		static final Border FOCUSED_BORDER = new LineBorder(Color.ORANGE);

		private static final long serialVersionUID = 0L;

		ConsolePanelButton(String text, Icon icon) {
			super(text, icon);
			setBorder(EMPTY_BORDER);
			setBackground(MarkReaderSessionPanel.DEFAULT_BACKGROUND_COLOR);
			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseEntered(MouseEvent e) {
					setBorder(FOCUSED_BORDER);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					setBorder(EMPTY_BORDER);
				}

			});
		}

	}

	ConsolePanel(File file) {
		setBackground(MarkReaderSessionPanel.DEFAULT_BACKGROUND_COLOR);
		this.restartButton = new ConsolePanelButton(Messages.SESSION_RESTART_LABEL, Images.REFRESH_ICON);
		this.pauseButton = new ConsolePanelButton(Messages.SESSION_STOP_LABEL, Images.STOP_ICON);
		this.sourceDirectoryPathField = new JTextField();
		this.configButton = new ConsolePanelButton(Messages.SESSION_CONFIG_LABEL, Images.CONFIG_ICON);
		this.closeButton = new JButton(MetalIconFactory.getInternalFrameCloseIcon(18));

		this.closeButton.setPreferredSize(new Dimension(30, 20));
		this.closeButton.setBorder(new EmptyBorder(0, 10, 10, 0));
		this.closeButton.setBackground(MarkReaderSessionPanel.DEFAULT_BACKGROUND_COLOR);
		this.closeButton.setToolTipText(Messages.SESSION_CLOSE_TOOLTIP);

		this.sourceDirectoryPathField.setBackground(MarkReaderSessionPanel.DEFAULT_BACKGROUND_COLOR);
		this.sourceDirectoryPathField.setBorder(new CompoundBorder(new EmptyBorder(5, 3, 5, 3),
				new LineBorder(Color.LIGHT_GRAY, 1)));
		this.sourceDirectoryPathField.setEditable(false);
		this.sourceDirectoryPathField.setText(file.getAbsolutePath());
		this.sourceDirectoryPathField.setToolTipText(file.getAbsolutePath());

		JPanel consolePanelUpper = new JPanel();
		consolePanelUpper.setBackground(MarkReaderSessionPanel.DEFAULT_BACKGROUND_COLOR);
		consolePanelUpper.setLayout(new BoxLayout(consolePanelUpper, BoxLayout.X_AXIS));
		consolePanelUpper.add(Box.createHorizontalStrut(20));
		consolePanelUpper.add(this.pauseButton);
		consolePanelUpper.add(Box.createHorizontalStrut(20));
		consolePanelUpper.add(this.restartButton);
		consolePanelUpper.add(Box.createHorizontalStrut(20));
		consolePanelUpper.add(this.configButton);
		consolePanelUpper.add(Box.createHorizontalStrut(10));
		this.restartButton.setEnabled(false);
		this.pauseButton.setEnabled(false);
		this.configButton.setEnabled(true);
		this.pauseButton.setToolTipText(Messages.SESSION_STOP_TOOLTIP);
		this.restartButton.setToolTipText(Messages.SESSION_RESTART_TOOLTIP);
		this.configButton.setToolTipText(Messages.SESSION_CONFIG_TOOLTIP);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(consolePanelUpper);
		add(this.sourceDirectoryPathField);
		add(Box.createHorizontalGlue());
		add(this.closeButton);
	}

	public void setPauseStateGUI() {
		this.pauseButton.setEnabled(false);
		this.restartButton.setEnabled(true);
		this.configButton.setEnabled(true);
		this.pauseButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.restartButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}

	public void setPlayStateGUI() {
		this.pauseButton.setEnabled(true);
		this.restartButton.setEnabled(true);
		this.configButton.setEnabled(false);
		this.pauseButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.restartButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}