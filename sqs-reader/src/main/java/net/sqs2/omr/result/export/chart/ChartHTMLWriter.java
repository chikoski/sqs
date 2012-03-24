/**
 * ChartHTMLWriter.java

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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.SpreadSheet;
import net.sqs2.omr.result.export.HTMLWriter;
import net.sqs2.omr.result.export.ResultDirectoryUtil;
import net.sqs2.omr.result.export.html.HTMLReportExportModule;
import net.sqs2.omr.session.output.OutputModule;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import freemarker.template.Template;
import freemarker.template.TemplateException;

class ChartHTMLWriter extends HTMLWriter {
	private final OutputModule outputModule;
	File chartDirectoryFile;
	ChartImageWriter chartImageFactory;

	ChartHTMLWriter(long sessionID, File chartDirectoryFile, String skin) throws IOException {
		super(skin);
		this.outputModule = OutputModule.getInstance(sessionID);
		this.chartDirectoryFile = chartDirectoryFile;
		this.chartImageFactory = new ChartImageWriter(640, 200);
	}

	
	void drawBarChartImage(List<FormArea> formAreaList, SpreadSheet spreadSheet) throws IOException, TemplateException {
		SourceDirectory sourceDirectory = spreadSheet.getSourceDirectory();
		FormArea primaryFormArea = formAreaList.get(0);
		int columnIndex = primaryFormArea.getQuestionIndex();
		File chartImageFile = ResultDirectoryUtil.createTargetFile(sourceDirectory, primaryFormArea, "CHART", "bar");
		if (chartImageFile.exists()
				&& this.outputModule.getTargetLastModifiedArray()[columnIndex] < chartImageFile.lastModified()) {
			return;
		}

		int[] numAnswers = this.outputModule.getValueTotalMatrix()[columnIndex];
		DefaultCategoryDataset barDataset = new DefaultCategoryDataset();

		for (int itemIndex = 0; itemIndex < formAreaList.size(); itemIndex++) {
			FormArea formArea = formAreaList.get(itemIndex);
			int value = numAnswers[itemIndex];
			String label = formArea.getItemLabel();
			barDataset.setValue(value, "", createLabel(itemIndex, label, value));
		}

		OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(chartImageFile));
		this.chartImageFactory.saveBarChart(outputStream, ChartConstants.ITEM_LABEL,
				ChartConstants.UNIT_LABEL, barDataset);
		outputStream.close();
	}

	void drawPieChartImage(List<FormArea> formAreaList, SpreadSheet spreadSheet) throws IOException, TemplateException {
		SourceDirectory sourceDirectory = spreadSheet.getSourceDirectory();
		FormArea primaryFormArea = formAreaList.get(0);

		int columnIndex = primaryFormArea.getQuestionIndex();

		File chartImageFile = ResultDirectoryUtil.createTargetFile(sourceDirectory, primaryFormArea, "CHART", "pie");

		if (chartImageFile.exists()
				&& this.outputModule.getTargetLastModifiedArray()[columnIndex] < chartImageFile.lastModified()) {
			return;
		}

		int[] numAnswers = this.outputModule.getValueTotalMatrix()[columnIndex];
		int numNoAnswers = this.outputModule.getNumNoValues()[columnIndex];
		int numMultipleAnswers = this.outputModule.getNumMultipleValues()[columnIndex];

		int total = spreadSheet.getNumRows() - numMultipleAnswers;

		if (0 == total) {
			return;
		}

		DefaultPieDataset pieDataset = new DefaultPieDataset();

		for (int itemIndex = 0; itemIndex < formAreaList.size(); itemIndex++) {
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

	void writeChartIndexHTMLFile(File chartDirectoryFile, SpreadSheet spreadSheet, boolean isExportChartImageMode) throws IOException, TemplateException {
		SourceDirectory sourceDirectory = spreadSheet.getSourceDirectory();
		FormMaster master = (FormMaster) spreadSheet.getFormMaster();
		List<SelectMultipleChartData> charts = new ArrayList<SelectMultipleChartData>();
		HashMap<String, Object> map = new HashMap<String, Object>();

		for (String qid : master.getQIDSet()) {
			List<FormArea> formAreaList = master.getFormAreaList(qid);
			List<ChartItem> chartItemList = new ArrayList<ChartItem>(formAreaList.size());
			FormArea primaryFormArea = formAreaList.get(0);
			int columnIndex = primaryFormArea.getQuestionIndex();
			int[] numAnswers = this.outputModule.getValueTotalMatrix()[columnIndex];

			if (primaryFormArea.isSelectSingle() || primaryFormArea.isSelectMultiple()) {
				for (int itemIndex = 0; itemIndex < formAreaList.size(); itemIndex++) {
					FormArea formArea = formAreaList.get(itemIndex);
					int value = numAnswers[itemIndex];
					chartItemList.add(new ChartItem(formArea, value));
				}

				if (primaryFormArea.isSelectSingle()) {
					int numNoAnswers = this.outputModule.getNumNoValues()[columnIndex];
					int numMultipleAnswers = this.outputModule.getNumMultipleValues()[columnIndex];
					SelectMultipleChartData chartData = new SelectSingleChartData(primaryFormArea, chartItemList,
							numNoAnswers, numMultipleAnswers);
					charts.add(chartData);
				} else if (primaryFormArea.isSelectMultiple()) {
					SelectMultipleChartData chartData = new SelectMultipleChartData(primaryFormArea, chartItemList);
					charts.add(chartData);
				}
			}
		}

		int numPages = spreadSheet.getFormMaster().getNumPages();
		int numRows = sourceDirectory.getNumPageIDsTotal() / numPages;

		HTMLReportExportModule.registTitle(master, map);
		map.put("exportChartImageMode", isExportChartImageMode);
		map.put("path", sourceDirectory.getRelativePath());
		map.put("master", master);
		map.put("numRows", numRows);
		map.put("charts", charts);
		map.put("skin", AppConstants.GROUP_ID);
		registChartIndexParameters(map);
		File chartIndexFile = new File(chartDirectoryFile, "index.html");
		PrintWriter chartDirectoryIndexWriter = ResultDirectoryUtil.createPrintWriter(chartIndexFile);
		Template chartIndexTemplate = this.loader.getTemplate("chartIndex.ftl", "UTF-8");
		chartIndexTemplate.process(map, chartDirectoryIndexWriter);
		chartDirectoryIndexWriter.close();
	}
	
	private void registChartIndexParameters(HashMap<String, Object> map){
		registFTLParameters(map);
		for(String key: new String[]{
				"noAnswerLabel",
				"errorLabel",
				"totalLabel",
				"unitLabel",
				"selectSingleLabel",
				"selectMultipleLabel",
				"exceptErrorDescriptionLabel"}){
			map.put(key, Messages._("result.chartIndex."+key));
		}	
	}

	String createLabel(int itemIndex, String label, int value, int total) {
		int percent = 100 * value / total;
		return label + " = " + Integer.toString(value) + ChartConstants.UNIT_LABEL + '(' + percent + "%)";
	}

	String createLabel(String label, int value, int total) {
		if (0 < total) {
			int percent = 100 * value / total;
			return label + " = " + Integer.toString(value) + ChartConstants.UNIT_LABEL + '(' + percent + "%)";
		} else {
			return label + " = " + Integer.toString(value) + ChartConstants.UNIT_LABEL;
		}
	}

	String createLabel(int itemIndex, String label, int value) {
		return Integer.toString(itemIndex + 1) + ':' + label + " = " + Integer.toString(value)
				+ ChartConstants.UNIT_LABEL;
	}

	public class ChartItem {
		FormArea formArea;
		int value;

		ChartItem(FormArea formArea, int value) {
			this.formArea = formArea;
			this.value = value;
		}

		public FormArea getFormArea() {
			return this.formArea;
		}

		public int getValue() {
			return this.value;
		}
	}

	public class SelectMultipleChartData {
		private FormArea primaryFormArea;
		private List<ChartItem> chartItemList;

		SelectMultipleChartData(FormArea primaryFormArea, List<ChartItem> chartitemList) {
			this.primaryFormArea = primaryFormArea;
			this.chartItemList = chartitemList;
		}

		public FormArea getPrimaryFormArea() {
			return this.primaryFormArea;
		}

		public List<ChartItem> getChartItemList() {
			return this.chartItemList;
		}
	}

	public class SelectSingleChartData extends SelectMultipleChartData {
		private int numNoAnswers, numMultipleAnswers;

		SelectSingleChartData(FormArea primaryFormArea, List<ChartItem> chartItemList, int numNoAnswers,
				int numMultipleAnswers) {
			super(primaryFormArea, chartItemList);
			this.numNoAnswers = numNoAnswers;
			this.numMultipleAnswers = numMultipleAnswers;
		}

		public int getNumNoAnswers() {
			return this.numNoAnswers;
		}

		public int getNumMultipleAnswers() {
			return this.numMultipleAnswers;
		}
	}
}
