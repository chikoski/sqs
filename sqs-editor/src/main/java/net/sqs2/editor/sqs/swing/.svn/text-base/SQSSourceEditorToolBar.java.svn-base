/*

 SQSSourceEditorToolBar.java
 
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
 
 Created on 2004/07/31

 */
package net.sqs2.editor.sqs.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

import net.sqs2.editor.base.swing.Messages;
import net.sqs2.editor.base.swing.SourceEditorToolBar;
import net.sqs2.swing.IconFactory;
import net.sqs2.translator.TranslatorException;

/**
 * @author hiroya
 * 
 */
public class SQSSourceEditorToolBar extends SourceEditorToolBar {
	public static final long serialVersionUID = 0;

	JButton htmlExportButton;
	//JButton previewExportButton;
	JButton pdfExportButton;

	JButton closeButton;
	transient ExportFileLogic logic;

	public SQSSourceEditorToolBar(final SQSSourceEditorMediator mediator) {
		this.logic = new ExportFileLogic(mediator);
		setPreferredSize(new Dimension(660, 24));

		this.htmlExportButton = new JButton(Messages.TOOLBAR_HTML_EXPORT_LABEL, IconFactory.create(
				"formdoc.gif", Messages.TOOLBAR_HTML_EXPORT_LABEL));
		this.htmlExportButton.setPreferredSize(new Dimension(195, 20));

		/*
		this.previewExportButton = new JButton(Messages.TOOLBAR_PDF_PREVIEW_LABEL, IconFactory.create(
				"preview.gif", Messages.TOOLBAR_PDF_PREVIEW_LABEL));
		this.previewExportButton.setPreferredSize(new Dimension(220, 20));
		 */
		this.pdfExportButton = new JButton(Messages.TOOLBAR_PDF_EXPORT_LABEL, IconFactory.create(
				"pdfdoc.gif", Messages.TOOLBAR_PDF_EXPORT_LABEL));
		this.pdfExportButton.setPreferredSize(new Dimension(190, 20));

		this.closeButton = new JButton(MetalIconFactory.getInternalFrameCloseIcon(18));
		this.closeButton.setPreferredSize(new Dimension(19, 20));
		this.closeButton.setBorder(new EmptyBorder(0, 0, 0, 0));

		this.htmlExportButton.setToolTipText(Messages.TOOLBAR_HTML_EXPORT_TOOLTIP);
		//this.previewExportButton.setToolTipText(Messages.TOOLBAR_PDF_PREVIEW_TOOLTIP);
		this.pdfExportButton.setToolTipText(Messages.TOOLBAR_PDF_EXPORT_TOOLTIP);
		this.closeButton.setToolTipText(Messages.TOOLBAR_CLOSE_BUFFER_TOOLTIP);

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				mediator.getMenuBarMediator().close();
			}
		});
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		Box left = Box.createHorizontalBox();
		left.add(Box.createHorizontalStrut(7));
		left.add(htmlExportButton);
		left.add(Box.createHorizontalStrut(7));
		//left.add(previewExportButton);
		//previewExportButton.setEnabled(false);
		left.add(Box.createHorizontalStrut(7));
		left.add(pdfExportButton);

		panel.add(left, BorderLayout.WEST);
		panel.add(closeButton, BorderLayout.EAST);
		add(panel);
		setEnabled(false);
		setupButtonActionListener(mediator);
	}

	public boolean isBasicServiceEnabled() {
		return true;
	}

	public void setEnabled(boolean enabled) {
		htmlExportButton.setEnabled(enabled);
		// previewExportButton.setEnabled(enabled);
		pdfExportButton.setEnabled(enabled);
		closeButton.setVisible(enabled);
	}

	private void setupButtonActionListener(final SQSSourceEditorMediator mediator) {
		this.htmlExportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					logic.exportHTML(mediator.uriResolver);
				} catch (IOException e) {
					showExportFailureMessage(mediator, e);
				} catch (TransformerFactoryConfigurationError e) {
					e.printStackTrace();
				} catch (TranslatorException e) {
					showExportFailureMessage(mediator, e);
				}
			}
		});
		this.pdfExportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					logic.exportPDF(mediator.uriResolver, mediator.getCurrentPageSetting());
				} catch (IOException e) {
					showExportFailureMessage(mediator, e);
				} catch (TransformerFactoryConfigurationError e) {
					e.printStackTrace();
				} catch (TranslatorException e) {
					showExportFailureMessage(mediator, e);
				}
			}
		});
	}

	private void showExportFailureMessage(final SQSSourceEditorMediator mediator, Exception e) {
		mediator.getMenuBarMediator().showError(e, Messages.ERROR_FILE_EXPORT_MESSAGE);
	}

}
