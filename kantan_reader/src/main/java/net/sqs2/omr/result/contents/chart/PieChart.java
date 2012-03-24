/**
 * 
 */
package net.sqs2.omr.result.contents.chart;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.result.contents.StatisticsContentsFactory;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class PieChart {
	public static void write(OutputStream outputStream, int width, int height, int questionIndex,
			List<FormArea> formAreaList, StatisticsContentsFactory statisticsContentsFactory) {
		// (1)create dataset
		DefaultPieDataset data = new DefaultPieDataset();
		for(FormArea formArea: formAreaList){
			int itemIndex = formArea.getItemIndex();
			String key = questionIndex+","+itemIndex;
			int count = statisticsContentsFactory.getCount(key);
			String label = formArea.getItemLabel()+": "+count;
			data.setValue(label, count);
		}
		
		int count = statisticsContentsFactory.getCount(questionIndex+",-1");
		data.setValue(ChartConstants.NO_ANSWER+": "+count,  count);	

		//String title = StringUtil.join(formAreaList.get(0).getHints(), "\n");
		String title = "";
		JFreeChart chart = ChartFactory.createPieChart(title, data, true, true, false);
		try {
			ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}		
	}
}