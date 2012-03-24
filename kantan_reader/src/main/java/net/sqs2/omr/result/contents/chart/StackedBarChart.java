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

public class StackedBarChart {
	public static void write(OutputStream outputStream, int width, int height, LegendHandler legendHandler,
			StatisticsContentsFactory statisticsContentsFactory) {
		// (1)create dataset
		Legend[] legendArray = legendHandler.legendArray;
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(FormArea formArea0: legendArray[0].formAreaList){
			int itemIndex0 = formArea0.getItemIndex();
			for(FormArea formArea1: legendArray[1].formAreaList){
				int itemIndex1 = formArea1.getItemIndex();
				String key = createKey(legendHandler.getAxis(), itemIndex0, itemIndex1);
				int count = statisticsContentsFactory.getCount(key);
				dataset.addValue(count, formArea1.getItemLabel(), formArea0.getItemLabel());
			}
			if(legendArray[0].defaultFormArea.isSelect1()){
				String key = createKey(legendHandler.getAxis(), itemIndex0, -1);
				int count = statisticsContentsFactory.getCount(key);
				dataset.addValue(count, ChartConstants.NO_ANSWER, formArea0.getItemLabel());
			}
		}
		if(legendArray[1].defaultFormArea.isSelect1()){
			for(FormArea formArea1: legendArray[1].formAreaList){
				int itemIndex1 = formArea1.getItemIndex();
				String key = createKey(legendHandler.getAxis(), -1, itemIndex1);
				int count = statisticsContentsFactory.getCount(key);
				dataset.addValue(count, formArea1.getItemLabel(), ChartConstants.NO_ANSWER);
			}
		}
		if(legendArray[1].defaultFormArea.isSelect1()){
			String key = createKey(legendHandler.getAxis(), -1, -1);
			int count = statisticsContentsFactory.getCount(key);
			dataset.addValue(count, ChartConstants.NO_ANSWER, ChartConstants.NO_ANSWER);
		}

		//String title = StringUtil.join(formAreaList.get(0).getHints(), "\n");
		String title = "";
		// (2)create JFreeChart instance
		//JFreeChart chart = ChartFactory.createBarChart(StringUtil.join(formAreaList.get(0).getHints(), "\n"),
		JFreeChart chart = ChartFactory.createStackedBarChart(title, 
				legendArray[0].defaultFormArea.getLabel(),
				legendArray[1].defaultFormArea.getLabel(),
				dataset, PlotOrientation.HORIZONTAL, true, false, false);

		// (3)create chart image
		try {
			ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
		} catch (IOException ioEx) {
		ioEx.printStackTrace();
		}
	}
	
	public static String createKey(String axis, int a, int b){
		if("1,0".equals(axis)){
			return ","+b+","+a;
		}else{
			return ","+a+","+b;
		}
	}


}