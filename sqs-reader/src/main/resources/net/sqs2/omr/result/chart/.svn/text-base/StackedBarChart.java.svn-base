/**
 * StackedBarChart.java

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

package net.sqs2.omr.result.chart;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.result.servlet.writer.StatisticsContentsWriter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class StackedBarChart {
	public static void write(OutputStream outputStream, int width, int height, LegendHandler legendHandler, StatisticsContentsWriter statisticsContentsFactory) {
		// (1)create dataset
		List<Legend> legendList = legendHandler.getLegendList();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Legend legend0 = legendList.get(0);
		Legend legend1 = legendList.get(1);
		for (FormArea formArea0 : legend0.formAreaList) {
			int itemIndex0 = formArea0.getItemIndex();
			for (FormArea formArea1 : legend1.formAreaList) {
				int itemIndex1 = formArea1.getItemIndex();
				String key = createKey(legendHandler.getAxis(), itemIndex0, itemIndex1);
				int count = statisticsContentsFactory.getCount(key);
				dataset.addValue(count, formArea1.getItemLabel(), formArea0.getItemLabel());
			}
			if (legend0.primaryFormArea.isSelectSingle()) {
				String key = createKey(legendHandler.getAxis(), itemIndex0, -1);
				int count = statisticsContentsFactory.getCount(key);
				dataset.addValue(count, ChartConstants.NO_ANSWER, formArea0.getItemLabel());
			}
		}
		if (legend1.primaryFormArea.isSelectSingle()) {
			for (FormArea formArea1 : legend1.formAreaList) {
				int itemIndex1 = formArea1.getItemIndex();
				String key = createKey(legendHandler.getAxis(), -1, itemIndex1);
				int count = statisticsContentsFactory.getCount(key);
				dataset.addValue(count, formArea1.getItemLabel(), ChartConstants.NO_ANSWER);
			}
		}
		if (legend1.primaryFormArea.isSelectSingle()) {
			String key = createKey(legendHandler.getAxis(), -1, -1);
			int count = statisticsContentsFactory.getCount(key);
			dataset.addValue(count, ChartConstants.NO_ANSWER, ChartConstants.NO_ANSWER);
		}

		// String title = StringUtil.join(formAreaList.get(0).getHints(), "\n");
		String title = "";
		// (2)create JFreeChart instance
		// JFreeChart chart =
		// ChartFactory.createBarChart(StringUtil.join(formAreaList.get(0).getHints(),
		// "\n"),
		JFreeChart chart = ChartFactory.createStackedBarChart(title, legend0.primaryFormArea
				.getLabel(), legend1.primaryFormArea.getLabel(), dataset, PlotOrientation.HORIZONTAL,
				true, false, false);

		// (3)create chart image
		try {
			ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}

	public static String createKey(String axis, int a, int b) {
		if ("1,0".equals(axis)) {
			return "," + b + "," + a;
		} else {
			return "," + a + "," + b;
		}
	}

}
