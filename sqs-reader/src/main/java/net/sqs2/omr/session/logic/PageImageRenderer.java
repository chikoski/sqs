/*

 PageImageFactory.java

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

 Created on 2009/01/11

 */
package net.sqs2.omr.session.logic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.sqs2.image.ImageFactory;
import net.sqs2.omr.app.deskew.PageFrameExceptionModel;
import net.sqs2.omr.app.deskew.DeskewedImageSource;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.ConfigSchemeException;
import net.sqs2.omr.model.DeskewGuideAreaConfig;
import net.sqs2.omr.model.FormAreaResult;
import net.sqs2.omr.model.FrameConfig;
import net.sqs2.omr.model.MarkRecognitionConfig;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTask;
import net.sqs2.omr.model.PageTaskAccessor;
import net.sqs2.omr.model.PageTaskException;
import net.sqs2.omr.model.PageTaskExceptionModel;
import net.sqs2.omr.model.PageTaskResult;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SourceConfig;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.SourceDirectoryConfiguration;

import org.apache.commons.io.FilenameUtils;

public class PageImageRenderer {

	public static BufferedImage createImage(long sessionID, SessionSource sessionSource, FormMaster master, SourceDirectory rowGroupSourceDirectory, int rowIndex, int focusedColumnIndex, int pageIndex, Rectangle scope) throws IOException,ConfigSchemeException {

		float densityThreshold = ((SourceConfig)rowGroupSourceDirectory.getConfiguration().getConfig().getPrimarySourceConfig())
				.getMarkRecognitionConfig().getMarkRecognitionDensityThreshold();

		PageID pageID =	 rowGroupSourceDirectory.getPageIDList().get(
				rowIndex * master.getNumPages() + pageIndex);
		File file = new File(rowGroupSourceDirectory.getRoot(), pageID.getFileResourceID().getRelativePath());

		BufferedImage src = ImageFactory.createImage(FilenameUtils.getExtension(file.getName()), file, pageID.getIndexInFile());
		Logger.getLogger("PageImageRenderer").info("create image : " + file);
		BufferedImage image = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
		copyImage(src, image);
		if (scope != null) {
			//PageTaskAccessor pageTaskAccessor = new PageTaskAccessor(sessionSource.getRootDirectory());
			PageTaskAccessor pageTaskAccessor = sessionSource.getContentAccessor().getPageTaskAccessor(); 
			OMRPageTask pageTask = pageTaskAccessor.get(pageIndex + 1, pageID);
			SourceDirectoryConfiguration configHandler = getSourceDirectoryConfiguration(sessionSource, pageTask);
			SourceConfig sourceConfig = (SourceConfig)configHandler.getConfig().getSourceConfig(pageTask.getPageID().getFileResourceID().getRelativePath(), pageTask.getProcessingPageNumber());
			drawPageState(pageTask, sourceConfig, pageIndex, densityThreshold, master, image, focusedColumnIndex, scope); // TODO: draw double-mark ignorance and  no-mark recovery
			pageTaskAccessor.flush();
		}
		image.getGraphics().finalize();
		return image;
	}

	private static SourceDirectoryConfiguration getSourceDirectoryConfiguration(SessionSource sessionSource, PageTask pageTask) throws IOException, ConfigSchemeException {
		return sessionSource.getConfiguration(pageTask.getConfigHandlerFileResourceID().getRelativePath(),
				pageTask.getConfigHandlerFileResourceID().getLastModified());
	}

	private static void copyImage(BufferedImage src, BufferedImage image) {
		Graphics2D g = (Graphics2D) image.getGraphics();
		int w = image.getWidth();
		int h = image.getHeight();
		g.drawImage(src, 0, 0, w, h, null);
		src.flush();
	}

	private static final Color HEADER_FOOTER_COLOR = new Color(0, 100, 0, 50);
	private static final Color TEXTAREA_COLOR = new Color(0, 0, 120, 50);
	private static final Color CORNER_COLOR = new Color(120, 0, 120, 50);

	private static final Color NO_MARKED_COLOR = new Color(0, 0, 180, 50);
	private static final Color MARKED_COLOR = new Color(180, 0, 0, 70);

	private static final Color FOCUSED_NO_MARKED_COLOR = new Color(0, 0, 200, 100);
	private static final Color FOCUSED_MARKED_COLOR = new Color(200, 0, 0, 100);
	private static final Color FOCUSED_SCOPE_COLOR = new Color(255, 0, 0, 255);

	private static void drawPageState(PageTask pageTask,
			SourceConfig sourceConfig,
			//SourceDirectoryConfiguration configHandler, 
			int pageIndex, 
			float densityThreshold, 
			FormMaster master, 
			BufferedImage image, 
			int focusedColumnIndex, 
			Rectangle scope) throws IOException {

		PageTaskResult pageTaskResult = pageTask.getPageTaskResult();
		PageTaskException pageTaskException = pageTask.getPageTaskException();

		FrameConfig frameConfig = sourceConfig.getFrameConfig();
		DeskewGuideAreaConfig deskewGuideAreaConfig = frameConfig.getDeskewGuideAreaConfig();
		MarkRecognitionConfig markRecognizationConfig = sourceConfig.getMarkRecognitionConfig();

		float headerVerticalMargin = deskewGuideAreaConfig.getHeaderVerticalMargin();
		float footerVerticalMargin = deskewGuideAreaConfig.getFooterVerticalMargin();
		float deskewGuideAreaHeight = deskewGuideAreaConfig.getHeight();
		float deskewGuideAreaHorizontalMargin = deskewGuideAreaConfig.getHeaderVerticalMargin();

		int w = image.getWidth();
		int h = image.getHeight();
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(HEADER_FOOTER_COLOR);
		g.fillRect((int) (w * deskewGuideAreaHorizontalMargin), (int) (h * headerVerticalMargin), (int) (w - 2
				* w * deskewGuideAreaHorizontalMargin), (int) (h * deskewGuideAreaHeight));
		g.fillRect((int) (w * deskewGuideAreaHorizontalMargin), (int) (h - h
				* (footerVerticalMargin + deskewGuideAreaHeight) - 1), (int) (w - 2 * w
				* deskewGuideAreaHorizontalMargin), (int) (h * deskewGuideAreaHeight));

		if (pageTaskResult != null) {
			Point2D[] corners = pageTaskResult.getDeskewGuideCenterPoints();
			DeskewedImageSource pageSource = drawCorners(master, corners, image, g);
			drawFormAreas(pageIndex, densityThreshold, (FormMaster) master, pageTaskResult, g,
					markRecognizationConfig, pageSource, focusedColumnIndex, scope);
		}
		if (pageTaskException != null) {
			PageTaskExceptionModel model = pageTaskException.getExceptionModel();
			if (model instanceof PageFrameExceptionModel) {
				PageFrameExceptionModel m = (PageFrameExceptionModel) model;
				drawCorners(master, m.getCorners(), image, g);
			}
		}
	}

	private static void drawFormAreas(int pageIndex, float densityThreshold, FormMaster master, PageTaskResult pageTaskResult, Graphics2D g, MarkRecognitionConfig markRecognizationConfig, DeskewedImageSource pageSource, int focusedColumnIndex, Rectangle scope) {
		int formAreaIndexInPage = 0;

		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;

		for (FormArea formArea : master.getFormAreaListByPageIndex(pageIndex)) {
			FormAreaResult result = (FormAreaResult) pageTaskResult.getPageAreaResultList().get(
					formAreaIndexInPage);
			if (formArea.isMarkArea()) {
				if (focusedColumnIndex == formArea.getQuestionIndex()) {

					Rectangle rect = formArea.getRect();

					Point2D p1 = pageSource.getPoint((int) rect.getX(), (int) rect.getY());
					Point2D p2 = pageSource.getPoint((int) (rect.getX() + rect.getWidth()), (int) (rect
							.getY() + rect.getHeight()));

					minX = Math.min(minX, (int) p1.getX());
					minY = Math.min(minY, (int) p1.getY());
					maxX = Math.max(maxX, (int) p2.getX());
					maxY = Math.max(maxY, (int) p2.getY());

					if (result.getDensity() < densityThreshold) {
						g.setColor(FOCUSED_MARKED_COLOR);
					} else {
						g.setColor(FOCUSED_NO_MARKED_COLOR);
					}

				} else {
					if (result.getDensity() < densityThreshold) {
						g.setColor(MARKED_COLOR);
					} else {
						g.setColor(NO_MARKED_COLOR);
					}
				}

				g.fillPolygon(pageSource.createRectPolygon(getExtendedRectangle(formArea.getRect(),
						markRecognizationConfig.getHorizontalMargin(), markRecognizationConfig
								.getVerticalMargin())));
				g.drawPolygon(pageSource.createRectPolygon(getExtendedRectangle(formArea.getRect(),
						markRecognizationConfig.getHorizontalMargin() + 3, markRecognizationConfig
								.getVerticalMargin() + 3)));

			} else {
				g.setColor(TEXTAREA_COLOR);
				g.fillPolygon(pageSource.createRectPolygon(formArea.getRect()));
			}
			formAreaIndexInPage++;
		}

		if (scope != null) {
			int borderMarginX = 20;
			int borderMarginY = 3;
			int margin = 40;

			int x = minX - borderMarginX;
			int y = minY - borderMarginY;
			int width = maxX - minX + borderMarginX * 2;
			int height = maxY - minY + borderMarginY * 2;

			scope.x = minX - margin;
			scope.y = minY - margin;
			scope.width = maxX - minX + margin * 2;
			scope.height = maxY - minY + margin * 2;

			Stroke stroke = new BasicStroke(4.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 2.0f,
					new float[] { 4.0f, 8.0f }, 0.0f);
			g.setStroke(stroke);
			g.setColor(FOCUSED_SCOPE_COLOR);
			g.drawRoundRect(x, y, width, height, 20, 20);
		}

	}

	private static DeskewedImageSource drawCorners(FormMaster master, Point2D[] corners, BufferedImage image, Graphics2D g) {
		DeskewedImageSource pageSource = new DeskewedImageSource(image, master.getDeskewGuideCenterPoints(), corners);
		g.setColor(CORNER_COLOR);
		g.setStroke(new BasicStroke(5));
		double size = 40;
		for (int y = 0; y < 2; y++) {
			int i = y * 2;
			g.fillPolygon(pageSource.createPolygon(new Point2D[] { corners[i],
					new Point2D.Double(corners[i].getX() - 1 * size, corners[i].getY() + 1 * size),
					new Point2D.Double(corners[i].getX() - 1 * size, corners[i].getY() - 1 * size) }));
			i++;
			g.fillPolygon(pageSource.createPolygon(new Point2D[] { corners[i],
					new Point2D.Double(corners[i].getX() + 1 * size, corners[i].getY() - 1 * size),
					new Point2D.Double(corners[i].getX() + 1 * size, corners[i].getY() + 1 * size) }));
		}
		return pageSource;
	}

	private static Rectangle getExtendedRectangle(Rectangle rect, float horizontal, float vertical) {
		return new Rectangle((int)(rect.x - horizontal),
				(int)(rect.y - vertical),
				(int)(rect.width + horizontal * 2),
				(int)(rect.height + vertical * 2));
	}
}
