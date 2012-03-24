/**
 * ChartImageWriter.java

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

 Author hiroya
 */

package net.sqs2.omr.result.export.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import net.sqs2.image.ImageManagerUtil;
import net.sqs2.omr.base.MarkReaderJarURIContext;
import net.sqs2.util.FileUtil;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleEdge;

public class ChartImageWriter {

	int width = 640;
	int height = 240;

	BufferedImage[] itemBackgroundImages;

	public ChartImageWriter(int width, int height, File itemBackgroundImageDir) {
		this(width, height, FileUtil.find(itemBackgroundImageDir, ".png").toArray(new File[0]));
	}

	public ChartImageWriter(int width, int height, String type, File itemBackgroundImageDir) {
		this(width, height, FileUtil.find(itemBackgroundImageDir, type).toArray(new File[0]));
	}

	public ChartImageWriter(int width, int height, File[] itemBackgroundImageFiles) {
		this(width, height, createItemBackgroundImages(itemBackgroundImageFiles));
	}

	public ChartImageWriter(int width, int height, BufferedImage[] itemBackgroundImages) {
		this(width, height);
		this.itemBackgroundImages = itemBackgroundImages;
	}

	public ChartImageWriter(int width, int height, String[] itemBackgroundImageNames) {
		this(width, height);
		this.itemBackgroundImages = createItemBackgroundImages(itemBackgroundImageNames);
	}

	public ChartImageWriter(int width, int height) {
		this.width = width;
		this.height = height;
		this.itemBackgroundImages = createItemBackgroundImages(new String[] { "01.png", "02.png", "03.png",
				"04.png",

				"11.png", "12.png", "13.png", "14.png",

				"21.png", "22.png", "23.png", "24.png",

				"31.png", "32.png", "33.png", "34.png",

				"41.png", "42.png", "43.png", "44.png",

				"51.png", "52.png", "53.png", "54.png" });
	}

	private static BufferedImage[] createItemBackgroundImages(File[] itemBackgroundImageFiles) {
		int numItems = itemBackgroundImageFiles.length;
		BufferedImage[] itemBackgroundImages = new BufferedImage[numItems];
		int index = 0;

		for (File itemBackgroundImageFile : itemBackgroundImageFiles) {
			try {
				itemBackgroundImages[index++] = ImageIO.read(itemBackgroundImageFile);
			} catch (IOException ignore) {
				ignore.printStackTrace();
			}
		}
		return itemBackgroundImages;
	}

	private static BufferedImage[] createItemBackgroundImages(String[] itemBackgroundImageNames) {
		int numItems = itemBackgroundImageNames.length;
		BufferedImage[] itemBackgroundImages = new BufferedImage[numItems];
		int index = 0;
		for (String itemBackgroundImageName : itemBackgroundImageNames) {
			URL url = null;
			try {
				url = new URL(MarkReaderJarURIContext.getPatternBaseURI() + itemBackgroundImageName);
				itemBackgroundImages[index++] = ImageManagerUtil.createPattern(url);
			} catch (MalformedURLException ignore) {
				throw new RuntimeException("cannot resolve:" + url);
			}

		}
		return itemBackgroundImages;
	}

	public void savePieChart(OutputStream outputStream, DefaultPieDataset dataSet) {
		String title = "";

		boolean showLegend = true;
		boolean tooltips = true;
		boolean urls = false;

		JFreeChart chart = ChartFactory.createPieChart(title, dataSet, showLegend, tooltips, urls);
		PiePlot piePlot = (PiePlot) chart.getPlot();

		// chart.getLegend().setLegendItemGraphicEdge(RectangleEdge.RIGHT);
		// chart.getLegend().setLegendItemGraphicLocation(RectangleAnchor.RIGHT);
		chart.getLegend().setPosition(RectangleEdge.RIGHT);
		setSectionPaint(dataSet, piePlot);

		try {
			ChartUtilities.writeChartAsPNG(outputStream, chart, this.width, this.height);
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}

	public void saveBarChart(OutputStream outputStream, String categoryAxisLabel, String valueAxisLabel, DefaultCategoryDataset dataSet) {
		String title = "";

		boolean legend = false;
		boolean tooltips = true;
		boolean urls = false;

		JFreeChart chart = ChartFactory.createBarChart(title, categoryAxisLabel, valueAxisLabel, dataSet,
				PlotOrientation.HORIZONTAL, legend, tooltips, urls);
		CategoryPlot plot = chart.getCategoryPlot();
		setSectionPaint(dataSet, plot);

		try {
			ChartUtilities.writeChartAsPNG(outputStream, chart, this.width, this.height);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void setSectionPaint(DefaultCategoryDataset dataSet, CategoryPlot plot) {

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(true);
		renderer.setItemLabelAnchorOffset(10);

		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		if (this.itemBackgroundImages != null && 0 < this.itemBackgroundImages.length) {
			int index = 0;
			Paint outlinePaint = Color.BLACK;
			Stroke outlineStroke = new BasicStroke(1.0f);
			for (@SuppressWarnings("unused") Object key : dataSet.getColumnKeys()) {
				int imageIndex = index % this.itemBackgroundImages.length;
				BufferedImage texture = this.itemBackgroundImages[imageIndex];
				Rectangle2D anchor = new Rectangle2D.Double(0, 0, texture.getWidth(), texture.getHeight());
				TexturePaint fillPaint = new TexturePaint(texture, anchor);
				renderer.setSeriesFillPaint(index, fillPaint);
				renderer.setSeriesPaint(index, fillPaint);
				renderer.setSeriesOutlinePaint(index, outlinePaint);
				renderer.setSeriesOutlineStroke(index, outlineStroke);

				// renderer.setBasePaint(fillPaint);
				// renderer.setBaseOutlineStroke(outlineStroke);
				// renderer.setBaseOutlinePaint(outlinePaint);
			}

		}
	}

	private void setSectionPaint(PieDataset dataSet, PiePlot piePlot) {
		Paint outlinePaint = Color.BLACK;
		Stroke outlineStroke = new BasicStroke(1.0f);

		if (this.itemBackgroundImages != null && 0 < this.itemBackgroundImages.length) {
			int index = 0;
			for (Object key : dataSet.getKeys()) {
				int imageIndex = index % this.itemBackgroundImages.length;
				BufferedImage texture = this.itemBackgroundImages[imageIndex];
				Rectangle2D anchor = new Rectangle2D.Double(0, 0, texture.getWidth(), texture.getHeight());
				TexturePaint fillPaint = new TexturePaint(texture, anchor);
				piePlot.setSectionPaint((Comparable<?>) key, fillPaint);
				piePlot.setSectionOutlinePaint((Comparable<?>) key, outlinePaint);
				piePlot.setSectionOutlineStroke((Comparable<?>) key, outlineStroke);
				index++;
			}
		}
		piePlot.setOutlineVisible(true);
		piePlot.setOutlinePaint(outlinePaint);
		piePlot.setOutlineStroke(outlineStroke);
		piePlot.setSectionOutlinesVisible(true);
		piePlot.setBaseSectionOutlinePaint(outlinePaint);
		piePlot.setBaseSectionOutlineStroke(outlineStroke);
	}

}
