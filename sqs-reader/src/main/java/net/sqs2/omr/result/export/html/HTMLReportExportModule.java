/**
 * HTMLReportExporter.java

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

package net.sqs2.omr.result.export.html;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.result.export.ResultDirectoryUtil;
import net.sqs2.omr.result.export.SpreadSheetExportUtil;
import net.sqs2.omr.result.export.spreadsheet.CSVExportModule;
import net.sqs2.omr.result.export.spreadsheet.ExcelExportModule;
import net.sqs2.omr.session.event.MasterEvent;
import net.sqs2.omr.session.event.PageEvent;
import net.sqs2.omr.session.event.QuestionEvent;
import net.sqs2.omr.session.event.QuestionItemEvent;
import net.sqs2.omr.session.event.RowEvent;
import net.sqs2.omr.session.event.RowGroupEvent;
import net.sqs2.omr.session.event.SessionEvent;
import net.sqs2.omr.session.event.SourceDirectoryEvent;
import net.sqs2.omr.session.event.SpreadSheetEvent;
import net.sqs2.omr.session.event.SpreadSheetOutputEventReciever;
import net.sqs2.omr.util.JarExtender;
import net.sqs2.template.TemplateLoader;
import net.sqs2.util.PathUtil;
import net.sqs2.xml.PrefixResolverImpl;

import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Document;

import freemarker.template.Template;

public class HTMLReportExportModule implements SpreadSheetOutputEventReciever{

	String skinName;

	public HTMLReportExportModule(String skinName) {
		this.skinName = skinName;
	}
	
	public String getSkinName(){
		return skinName;
	}

	@Override
	public void startSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		try{
			exportReport(spreadSheetEvent, this.skinName);
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	public void exportReport(SpreadSheetEvent spreadSheetEvent, String skinName) throws IOException {

		SourceDirectoryEvent sourceDirectoryEvent = spreadSheetEvent.getSourceDirectoryEvent();
		File sourceDirectoryFile = new File(sourceDirectoryEvent.getSourceDirectory().getRoot(),
				sourceDirectoryEvent.getSourceDirectory().getRelativePath());
		File resultDirectoryFile = new File(sourceDirectoryFile, AppConstants.RESULT_DIRNAME);

		File resultDirectoryIndexFile = new File(resultDirectoryFile, "index.html");

		new JarExtender().extend(new String[] { "css/"+skinName+".css" }, resultDirectoryFile);

		PrintWriter resultDirectoryIndexWriter = ResultDirectoryUtil.createPrintWriter(resultDirectoryIndexFile);

		try {
			Map<String, Object> map = new HashMap<String, Object>();

			registTitle(spreadSheetEvent.getFormMaster(), map);
			File csvFileName = SpreadSheetExportUtil.createSpreadSheetFile(spreadSheetEvent,
					CSVExportModule.SUFFIX);
			File xlsFileName = SpreadSheetExportUtil.createSpreadSheetFile(spreadSheetEvent,
					ExcelExportModule.SUFFIX);
					
			String csvFilePath = PathUtil.getRelativePath(csvFileName, resultDirectoryFile, File.separatorChar);
			String xlsFilePath = PathUtil.getRelativePath(xlsFileName, resultDirectoryFile, File.separatorChar);
			
			map.put("path", spreadSheetEvent.getSpreadSheet().getSourceDirectory().getRelativePath());			
			map.put("csvFilePath", csvFilePath);
			map.put("xlsFilePath", xlsFilePath);
			map.put("resultFolderName", AppConstants.RESULT_DIRNAME);
			if(sourceDirectoryEvent.getSourceDirectory().getChildSourceDirectoryList() != null){
				map.put("numChildSourceDirectories", sourceDirectoryEvent.getSourceDirectory().getChildSourceDirectoryList().size());
			}else{
				map.put("numChildSourceDirectories", 0);
			}
			map.put("showSubFolders", Messages.RESULT_DIRECTORYINDEX_SUBFOLDERS_LABEL);
			map.put("sourceDirectory", sourceDirectoryEvent.getSourceDirectory());
			
			TemplateLoader loader = new TemplateLoader(AppConstants.USER_CUSTOMIZED_CONFIG_DIR,
					"ftl", skinName);
			Template resultDirectoryIndexTemplate = loader.getTemplate("index.ftl", "UTF-8");
			registDirectoryIndexParameters(map);
			resultDirectoryIndexTemplate.process(map, resultDirectoryIndexWriter);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		resultDirectoryIndexWriter.close();
	}
	
	private void registDirectoryIndexParameters(Map<String, Object> map){
		for(String key: new String[]{
				"folderPrefixLabel",
				"contentsOfResultLabel",
				"listOfSpreadSheetsLabel",
				"xlsSpreadSheetLabel",
				"csvSpreadSheetLabel",
				"listOfResultsLabel",
				"listOfFreeAnswersLabel",
				"listOfStatisticsLabel"
		}){
			map.put(key, Messages._("result.directoryIndex."+key));
		}
		map.put("skin", AppConstants.GROUP_ID);
	}

	public static void registTitle(FormMaster master, Map<String, Object> map) {
		Document document = master.getPageMasterMetadata().getSourceDocument();
		try {
			if (document != null) {
				PrefixResolver prefixResolver = new PrefixResolverImpl(document.getDocumentElement());
				String title = null;
				XObject xobj = XPathAPI.eval(document.getDocumentElement(),
						"/xhtml2:html/xhtml2:head/xhtml2:title", prefixResolver);
				title = xobj.str();
				map.put("title", title);
				return;
			}
		} catch (TransformerException ignore) {
		}
		map.put("title", "");
	}

	@Override
	public void endPage(PageEvent pageEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endQuestion(QuestionEvent questionEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endQuestionItem(QuestionItemEvent questionItemEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endRow(RowEvent rowEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endRowGroup(RowGroupEvent rowGroupEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startPage(PageEvent pageEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startQuestion(QuestionEvent questionEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startQuestionItem(QuestionItemEvent questionItemEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startRow(RowEvent rowEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startRowGroup(RowGroupEvent rowGroupEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endMaster(MasterEvent masterEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endSession(SessionEvent sessionEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startMaster(MasterEvent masterEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startSession(SessionEvent sessionEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
		// TODO Auto-generated method stub
		
	}
}
