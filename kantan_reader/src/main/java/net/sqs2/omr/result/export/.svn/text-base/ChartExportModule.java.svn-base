/**
 * 
 */
package net.sqs2.omr.result.export;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.contents.chart.ChartConstants;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.source.SpreadSheet;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import freemarker.template.Template;
import freemarker.template.TemplateException;

class ChartExportModule extends TemplateExporter{
	/**
	 * 
	 */
	private final HTMLReportExportModule reportExportModule;
	File chartDirectoryFile;
	ChartImageFactory chartImageFactory;
	ChartExportModule(HTMLReportExportModule reportExportModule, File chartDirectoryFile, String skin)throws IOException{
		super(skin);
		this.reportExportModule = reportExportModule;
		this.chartDirectoryFile = chartDirectoryFile;
		this.chartImageFactory = new ChartImageFactory(640, 200);
	}
	
	void drawBarChartImage(List<FormArea> formAreaList, SpreadSheet spreadSheet)throws IOException,TemplateException{
		SourceDirectory sourceDirectory = spreadSheet.getSourceDirectory();
		FormArea defaultFormArea = formAreaList.get(0);			
		int columnIndex = defaultFormArea.getColumnIndex();
		
		File chartImageFile = this.reportExportModule.chartImageFileExportModule.createChartImageFile(sourceDirectory, defaultFormArea, "bar");
		if(chartImageFile.exists() && this.reportExportModule.targetLastModifiedArray[columnIndex] < chartImageFile.lastModified()){
			return;
		}

		//System.out.println("Save Chart: "+defaultFormArea.getColumnIndex() + ":" + defaultFormArea.getQID()+ " / "+master.getQIDSet().size());
		int[] numAnswers = this.reportExportModule.valueTotalMatrix[columnIndex];
		DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
		
		for(int itemIndex = 0; itemIndex < formAreaList.size(); itemIndex++){
			FormArea formArea = formAreaList.get(itemIndex);
			int value = numAnswers[itemIndex];
			String label = formArea.getItemLabel();
			//barDataset.setValue(value, "", createLabel(itemIndex, label, value));
			barDataset.setValue(value, "", createLabel(itemIndex, label, value));
		}
		
		OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(chartImageFile));
		chartImageFactory.saveBarChart(outputStream, ChartConstants.ITEM_LABEL, ChartConstants.UNIT_LABEL, barDataset);
		outputStream.close();
	}
	
	void drawPieChartImage(List<FormArea> formAreaList, SpreadSheet spreadSheet)throws IOException,TemplateException{
		SourceDirectory sourceDirectory = spreadSheet.getSourceDirectory();
		//FormMaster master = (FormMaster) sourceDirectory.getPageMaster();
		FormArea defaultFormArea = formAreaList.get(0);
					
		int columnIndex = defaultFormArea.getColumnIndex();
		
		File chartImageFile = this.reportExportModule.chartImageFileExportModule.createChartImageFile(sourceDirectory, defaultFormArea, "pie");

		if(chartImageFile.exists() && this.reportExportModule.targetLastModifiedArray[columnIndex] < chartImageFile.lastModified()){
			return;
		}

		int[] numAnswers = this.reportExportModule.valueTotalMatrix[columnIndex];
		int numNoAnswers = this.reportExportModule.numNoValues[columnIndex];
		int numMultipleAnswers = this.reportExportModule.numMultipleValues[columnIndex];
		int total = spreadSheet.getNumRows() - numMultipleAnswers;

		if(0 == total){
			return;
		}
		
		//System.out.println("Save Chart: "+defaultFormArea.getColumnIndex() + ":" + defaultFormArea.getQID()+ " / "+master.getQIDSet().size());
		DefaultPieDataset pieDataset = new DefaultPieDataset();					
		
		for(int itemIndex = 0; itemIndex < formAreaList.size(); itemIndex++){
			FormArea formArea = formAreaList.get(itemIndex);
			int value = numAnswers[itemIndex];
			String label = formArea.getItemLabel();
			pieDataset.setValue(createLabel(itemIndex, label, value, total), value);
		}
		
		pieDataset.setValue(createLabel(ChartConstants.NO_ANSWER, numNoAnswers, total), numNoAnswers);

		OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(chartImageFile));
		this.chartImageFactory.savePieChart(outputStream, pieDataset);
		outputStream.close();
	}
	
	void writeChartIndexHTMLFile(File chartDirectoryFile, SpreadSheet spreadSheet,
			boolean isExportChartImageMode)throws IOException,TemplateException{
		SourceDirectory sourceDirectory = spreadSheet.getSourceDirectory();
		FormMaster master = (FormMaster)sourceDirectory.getPageMaster(); 
		File chartIndexFile = new File(chartDirectoryFile, "index.html");
		PrintWriter chartDirectoryIndexWriter= this.reportExportModule.createPrintWriter(chartIndexFile);
		List<SelectChartData> charts = new ArrayList<SelectChartData>();
		HashMap<String,Object> map = new HashMap<String,Object>();

		for(String qid: master.getQIDSet()){
			List<FormArea> formAreaList = master.getFormAreaList(qid);
			List<ChartItem> chartItemList = new ArrayList<ChartItem>(formAreaList.size());
			FormArea defaultFormArea = formAreaList.get(0);
			int columnIndex = defaultFormArea.getColumnIndex();
			int[] numAnswers = this.reportExportModule.valueTotalMatrix[columnIndex];
			
			if(defaultFormArea.isSelect1() || defaultFormArea.isSelect()){
				for(int itemIndex = 0; itemIndex < formAreaList.size(); itemIndex++){
					FormArea formArea = formAreaList.get(itemIndex);
					int value = numAnswers[itemIndex];
					chartItemList.add(new ChartItem(formArea, value));
				}
				
				if(defaultFormArea.isSelect1()){
					int numNoAnswers = this.reportExportModule.numNoValues[columnIndex];
					int numMultipleAnswers = this.reportExportModule.numMultipleValues[columnIndex];
					SelectChartData chartData = new Select1ChartData(defaultFormArea, chartItemList, numNoAnswers, numMultipleAnswers);
					charts.add(chartData);
				}else if(defaultFormArea.isSelect()){
					SelectChartData chartData = new SelectChartData(defaultFormArea, chartItemList);
					charts.add(chartData);
				}
			}
		}
		
		map.put("exportChartImageMode", isExportChartImageMode);
		map.put("path", sourceDirectory.getPath());
		map.put("master", master);
		map.put("numRows", reportExportModule.getNumRowsTotal(sourceDirectory));
		map.put("charts", charts);
		
		Template chartIndexTemplate = this.loader.getTemplate("chartIndex.ftl", "UTF-8");
		chartIndexTemplate.process(map, chartDirectoryIndexWriter);
		chartDirectoryIndexWriter.close();
	}
	
	String createLabel(int itemIndex, String label, int value, int total){
		int percent = 100 * value / total;
		return Integer.toString(itemIndex + 1) + ':' + label + " = " + Integer.toString(value) + ChartConstants.UNIT_LABEL + '('+percent+"%)";
	}
	
	String createLabel(String label, int value, int total){
		if(0 < total){
			int percent = 100 * value / total;
			return label + " = " + Integer.toString(value) + ChartConstants.UNIT_LABEL + '('+percent+"%)";
		}else{
			return label + " = " + Integer.toString(value) + ChartConstants.UNIT_LABEL;
		}
	}
	
	String createLabel(int itemIndex, String label, int value){
		return Integer.toString(itemIndex + 1) + ':' + label + " = " + Integer.toString(value) + ChartConstants.UNIT_LABEL;
	}
	
	public class ChartItem{
		FormArea formArea;
		int value;
		ChartItem(FormArea formArea, int value){
			this.formArea = formArea;
			this.value = value;
		}
		public FormArea getFormArea(){
			return this.formArea;
		}
		
		public int getValue(){
			return this.value;
		}
	}
	
	public class SelectChartData{
		private FormArea defaultFormArea;
		private List<ChartItem> chartItemList;

		SelectChartData(FormArea defaultFormArea, List<ChartItem> chartitemList){
			this.defaultFormArea = defaultFormArea;
			this.chartItemList = chartitemList;
		}
		
		public FormArea getDefaultFormArea(){
			return this.defaultFormArea;
		}
		
		public List<ChartItem> getChartItemList(){
			return this.chartItemList;
		}
	}

	public class Select1ChartData extends SelectChartData{
		private int numNoAnswers, numMultipleAnswers;
		Select1ChartData(FormArea defaultFormArea, List<ChartItem> chartItemList, int numNoAnswers, int numMultipleAnswers){
			super(defaultFormArea, chartItemList);
			this.numNoAnswers = numNoAnswers;
			this.numMultipleAnswers = numMultipleAnswers;
		}
		
		public int getNumNoAnswers(){
			return numNoAnswers; 
		}
		public int getNumMultipleAnswers(){
			return numMultipleAnswers; 
		}
	}
}