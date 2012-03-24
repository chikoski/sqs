/**
 * 
 */
package net.sqs2.omr.result.contents.chart;

import java.io.IOException;
import java.io.OutputStream;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.result.contents.StatisticsContentsFactory;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChart {
	public static void write(OutputStream outputStream, int width, int height, Legend legend,
			StatisticsContentsFactory statisticsContentsFactory) {
		// (1)create dataset
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(FormArea formArea: legend.formAreaList){
			int itemIndex = formArea.getItemIndex();
			String key = legend.questionIndex+","+itemIndex;
			dataset.addValue(statisticsContentsFactory.getCount(key), "", formArea.getItemLabel());
		}
		
		if(legend.defaultFormArea.isSelect1()){
			int count = statisticsContentsFactory.getCount(legend.questionIndex+",-1");
			dataset.addValue(count, "", ChartConstants.NO_ANSWER);
		}

		//String title = StringUtil.join(formAreaList.get(0).getHints(), "\n");
		String title = "";
		JFreeChart chart = ChartFactory.createBarChart(title,
					"", ChartConstants.NO_ANSWER, dataset, PlotOrientation.HORIZONTAL, false, true, false);
		try {
			ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
		} catch (IOException ioEx) {
		ioEx.printStackTrace();
		}
	}
}