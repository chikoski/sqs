/**
 * PieChart.java

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
import net.sqs2.omr.result.builder.StatisticsResult;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class PieChart {
	
	public static void write(OutputStream outputStream, int width, int height, int questionIndex, List<FormArea> formAreaList, StatisticsResult statisticsResult) {
		// (1)create dataset
		DefaultPieDataset data = new DefaultPieDataset();
		for (FormArea formArea : formAreaList) {
			int itemIndex = formArea.getItemIndex();
			String key = questionIndex + "," + itemIndex;
			int count = statisticsResult.getCount(key);
			String label = formArea.getItemLabel() + ": " + count;
			data.setValue(label, count);
		}

		int count = statisticsResult.getCount(questionIndex + ",-1");
		data.setValue(ChartConstants.NO_ANSWER + ": " + count, count);

		// String title = StringUtil.join(formAreaList.get(0).getHints(), "\n");
		String title = "";
		JFreeChart chart = ChartFactory.createPieChart(title, data, true, true, false);
		try {
			ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}
}
