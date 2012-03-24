/**
 * 
 */
package net.sqs2.translator.impl;

import java.awt.Container;

import javax.swing.JFrame;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.render.awt.AWTRenderer;
import org.apache.fop.render.awt.viewer.PreviewPanel;
import org.apache.fop.render.awt.viewer.Renderable;

class PreviewFrame extends JFrame {
	private static final long serialVersionUID = 0L;
	private FOUserAgent userAgent;
	private PreviewPanel previewPanel;

	PreviewFrame(FOUserAgent userAgent) {
		this.setSize(400, 550);
		this.userAgent = userAgent;
	}

	void update(String title, Renderable renderable, AWTRenderer renderer) {
		Container contentPane = this.getContentPane();
		if (0 < contentPane.getComponentCount()) {
			contentPane.remove(0);
		}
		this.previewPanel = new PreviewPanel(this.userAgent, renderable, renderer);
		contentPane.add(this.previewPanel);
		this.setTitle(title);
	}

	void render(Renderable renderable, AWTRenderer renderer) throws FOPException {
		renderer.setRenderable(renderable);
		renderer.clearViewportList();
		renderable.renderTo(this.userAgent, MimeConstants.MIME_FOP_AWT_PREVIEW);
	}

	void show(AWTRenderer renderer) throws FOPException {
		double scale = this.previewPanel.getScaleToFitWindow();
		this.previewPanel.setDisplayMode(PreviewPanel.CONTINUOUS);
		this.previewPanel.setScaleFactor(scale);
		renderer.clearViewportList();
		this.previewPanel.reload();
	}
}