/*
 * 

 ExigridAboutDialog.java

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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.sqs2.browser.Browser;

public class DocumentDialog extends JDialog {
	private static final long serialVersionUID = 0L;

	public DocumentDialog(JFrame frame, DocumentDialogModel model) {
		super(frame, model.title, true);
		this.setSize(model.width, model.height);
		this.getContentPane().add(new AboutPanelFrame(model.url));
	}

	class ButtonPanel extends JPanel {
		private static final long serialVersionUID = 0L;

		ButtonPanel(String label) {
			JButton okButton = new JButton(label);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					DocumentDialog.this.setVisible(false);
				}
			});
			this.add(okButton);
		}
	}

	class AboutPanelFrame extends JPanel {
		private static final long serialVersionUID = 0L;

		AboutPanelFrame(URL url) {
			this.setBorder(new EmptyBorder(10, 10, 0, 10));
			setLayout(new BorderLayout());

			JPanel okButtonPanel = new ButtonPanel("OK");

			try {
				setLayout(new BorderLayout());
				add(new AboutPanel(url), BorderLayout.CENTER);
				add(okButtonPanel, BorderLayout.SOUTH);
			} catch (IOException ignore) {
				ignore.printStackTrace();
			}
		}
	}

	class AboutPanel extends JPanel {
		private static final long serialVersionUID = 0L;

		JEditorPane html = null;

		AboutPanel(URL url) throws IOException {
			html = new JEditorPane();
			html.setEditable(false);
			html.setContentType("text/html");
			html.setPage(url);
			html.addHyperlinkListener(createHyperLinkListener());
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.getViewport().add(html);
			setLayout(new BorderLayout());
			add(scrollPane, BorderLayout.CENTER);

			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					DocumentDialog.this.setVisible(false);
				}
			});
		}

		HyperlinkListener createHyperLinkListener() {
			return new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						Browser.showDocument(null, e.getURL());
					}
				}
			};
		}

	}
}
