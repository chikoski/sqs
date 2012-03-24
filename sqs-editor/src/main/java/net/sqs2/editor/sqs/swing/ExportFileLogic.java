/*

 ExportFileLogic.java
 
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
 
 Created on 2004/08/18

 */
package net.sqs2.editor.sqs.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.URIResolver;

import net.sqs2.browser.Browser;
import net.sqs2.editor.base.swing.Messages;
import net.sqs2.editor.base.swing.SourceEditorMediator;
import net.sqs2.editor.httpd.SQSHttpdManager;
import net.sqs2.source.SQSSource;
import net.sqs2.swing.LoggerConsoleFrame;
import net.sqs2.translator.AbstractTranslator;
import net.sqs2.translator.TranslatorException;
import net.sqs2.translator.impl.PageSetting;
import net.sqs2.translator.impl.PageSettingImpl;
import net.sqs2.translator.impl.SQSToHTMLTranslator;
import net.sqs2.translator.impl.SQSToPDFTranslator;
import net.sqs2.translator.impl.SQSToPreviewTranslator;
import net.sqs2.translator.impl.TranslatorJarURIContext;

public class ExportFileLogic {
	private SourceEditorMediator mediator;
	private Thread exportWorkerThread;
	// private SQSToPreviewTranslator sqsToPreviewTranslator;
	// private PreviewDialog previewDialog;

	private static final String groupID = "sqs";
	private static final String appID = "SQS_SourceEditor_2_1";
	private static final String fopURL = TranslatorJarURIContext.getFOPBaseURI();
	private static final String xsltURL = TranslatorJarURIContext.getXSLTBaseURI();
	
	public ExportFileLogic(SourceEditorMediator mediator) {
		this.mediator = mediator;
	}

	private LoggerConsoleFrame createLogger() {
		final LoggerConsoleFrame logger = new LoggerConsoleFrame(mediator.getTitle() + " Log", "Cancel", "OK");
		logger.setFinished(false);
		logger.getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				logger.setVisible(false);
				logger.setFinished(true);
				if (exportWorkerThread != null && exportWorkerThread.isAlive()
						&& !exportWorkerThread.isInterrupted()) {
					exportWorkerThread.interrupt();
				}
			}
		});
		logger.info("start translation.");
		logger.setSize(400, 300);
		logger.setVisible(true);
		return logger;
	}

	public synchronized void preview(String title, URIResolver uriResolver, PageSetting pageSetting) throws TranslatorException, IOException {
		/*
		 * if(this.sqsToPreviewTranslator == null){ this.sqsToPreviewTranslator
		 * = new SQSToPreviewTranslator(); } if(this.previewDialog == null){
		 * this.previewDialog = new
		 * PreviewDialog(SQSToPreviewTranslator.createFOUserAgent()); }
		 */
		new SQSToPreviewTranslator(groupID, appID, fopURL, xsltURL, title, uriResolver, pageSetting).translate(createSQSInputStream(),
				null, uriResolver);
	}

	public void exportPDF(URIResolver uriResolver, PageSetting pageSetting) throws TranslatorException, IOException {
		exportPDF(createTemporaryFile(".pdf"), uriResolver, pageSetting);
	}

	public void exportPDF(File exportingFile, URIResolver uriResolver, PageSetting pageSetting) throws TranslatorException, IOException {
		translate(new SQSToPDFTranslator(groupID, appID, fopURL, xsltURL, exportingFile.getName(), uriResolver, pageSetting),
				exportingFile, uriResolver);
	}

	public void exportHTML(URIResolver uriResolver) throws TranslatorException, IOException {
		exportHTML(createTemporaryFile(".xhtml"), uriResolver);
	}

	public void exportHTML(File exportingFile, URIResolver uriResolver) throws TranslatorException, IOException {
		translate(new SQSToHTMLTranslator(xsltURL), exportingFile, uriResolver);
	}

	private File createTemporaryFile(String suffix) throws IOException {
		File targetFile = File.createTempFile("sqs-draft-", suffix);
		targetFile.deleteOnExit();
		return targetFile;
	}

	public synchronized void translate(final AbstractTranslator translator, final File targetFile, final URIResolver uriResolver) throws TransformerFactoryConfigurationError {
		ThreadGroup threadGroup = new ThreadGroup("translate");
		threadGroup.setMaxPriority(Thread.MIN_PRIORITY);
		this.exportWorkerThread = new Thread(threadGroup, "exportWorkerThread") {
			public void run() {
				try {
					String systemId = null; // TODO: URI relative path should resolve.
					if (targetFile == null) {
						return;
					}
					InputStream sqsInputStream = createSQSInputStream();
					OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(targetFile));
					translate(translator, outputStream, systemId, sqsInputStream, uriResolver);
					Browser.showDocument(targetFile, SQSHttpdManager.getHttpd().createTempFileURL(
							targetFile));
					/*
					 * if(launcher != null &&
					 * isBrowserAccessibleDirectory(tgtFile.getAbsolutePath())){
					 * launcher.showDocument(); }
					 */
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				} catch (IOException ex) {
					ex.printStackTrace();
					mediator.createSourceEditorMenuBarMediator().showError(ex, Messages.ERROR_FILE_TRANSFORM);
				} catch (TranslatorException ex) {
					ex.printStackTrace();
					mediator.createSourceEditorMenuBarMediator().showError(ex, Messages.ERROR_FILE_TRANSFORM);
				} catch (Exception ex) {
					ex.printStackTrace();
					mediator.createSourceEditorMenuBarMediator().showError(ex, Messages.ERROR_FILE_TRANSFORM);
				}
			}
		};
		this.exportWorkerThread.start();
	}

	private InputStream createSQSInputStream() throws IOException {
		return ((SQSSource) mediator.getSourceEditorTabbedPane().getCurrentEditingSource())
				.createInputStream();
	}

	private synchronized void translate(final AbstractTranslator translator, final OutputStream tmpOutputStream, final String systemId, final InputStream sqsInputStream, final URIResolver uriResolver) throws TranslatorException {
		mediator.getToolBar().setEnabled(false);
		final LoggerConsoleFrame logger = createLogger();
		try {
			translator.execute(sqsInputStream, systemId, tmpOutputStream, uriResolver);
		} catch (TranslatorException ex) {
			if (ex.getCause() instanceof TransformerException) {
				showTransformerException(logger, ex);
			}
			throw ex;
		} catch (IOException ex) {
			if (ex.getCause() instanceof TransformerException) {
			}
			ex.printStackTrace();
		} finally {
			tearOffTranslation(tmpOutputStream, sqsInputStream, logger);
		}
	}

	private void tearOffTranslation(final OutputStream tmpOutputStream, final InputStream sqsInputStream, final LoggerConsoleFrame logger) {
		mediator.getToolBar().setEnabled(true);
		logger.setFinished(true);
		if (!logger.hasError()) {
			logger.setVisible(false);
		}
		try {
			sqsInputStream.close();
			tmpOutputStream.close();
		} catch (IOException ignore) {
		}
	}

	private void showTransformerException(final LoggerConsoleFrame logger, TranslatorException ex) {
		TransformerException cause = (TransformerException) ex.getCause();
		logger.error(cause.getMessageAndLocation());
		JOptionPane.showMessageDialog(mediator.getFrame(), new Object[] {
				Messages.ERROR_FILE_TRANSFORM_MESSAGE + ":",
				Messages.VOCABRUARY_LOCATION + ":" + cause.getLocationAsString(),
				Messages.VOCABRUARY_CONTENT + ":" + cause.getLocalizedMessage() },
				Messages.ERROR_FILE_TRANSFORM, JOptionPane.ERROR_MESSAGE);
	}

}
