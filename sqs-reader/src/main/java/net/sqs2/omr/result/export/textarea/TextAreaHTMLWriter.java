/**
 * TextAreaExporter.java

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

package net.sqs2.omr.result.export.textarea;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.result.export.HTMLWriter;
import net.sqs2.omr.result.export.ResultDirectoryUtil;
import net.sqs2.omr.result.export.html.HTMLReportExportModule;
import net.sqs2.util.StringUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TextAreaHTMLWriter extends HTMLWriter {

	public static final int NUM_IMAGES_PAR_PAGE = 50;

	Map<String, String> textAreaValueMap = new HashMap<String, String>();

	SourceDirectory sourceDirectory;
	File textAreaDirectoryFile;
	Template textAreaIndexTemplate;
	Template textAreaColumnTemplate;
	Template textAreaRowRangeTemplate;

	public TextAreaHTMLWriter(SourceDirectory sourceDirectory, File textAreaDirectoryFile, String skinName) throws IOException {
		super(skinName);
		this.sourceDirectory = sourceDirectory;
		this.textAreaDirectoryFile = textAreaDirectoryFile;
		this.textAreaIndexTemplate = this.loader.getTemplate("textAreaIndex.ftl", "UTF-8");
		this.textAreaColumnTemplate = this.loader.getTemplate("textAreaColumn.ftl", "UTF-8");
		this.textAreaRowRangeTemplate = this.loader.getTemplate("textAreaRowRange.ftl", "UTF-8");
	}

	private HashMap<String, Object> createFTLParameters(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		super.registFTLParameters(map);
		for(String key: new String[]{
				"rowNumberPrefixLabel",
				"rowNumberSuffixLabel",
		}){
			map.put(key, Messages._("result.textAreaIndex."+key));
		}
		map.put("skin", AppConstants.GROUP_ID);
		return map;
	}

	public void writeTextAreaIndexFile(FormMaster master, String spreadSheetSourceDirectoryPath) throws IOException, TemplateException {
		HashMap<String, Object> map = createFTLParameters();
		File textAreaIndexFile = new File(this.textAreaDirectoryFile, "index.html");
		List<FormArea> textareas = new LinkedList<FormArea>();

		for (String qid : master.getQIDSet()) {
			FormArea primaryFormArea = master.getFormAreaList(qid).get(0);
			if (primaryFormArea.isTextArea()) {
				textareas.add(primaryFormArea);
			}
		}
		
		registTextAreaIndexEntry(map, master, spreadSheetSourceDirectoryPath, textareas);

		map.put("sourceDirectory", this.sourceDirectory);
		
		PrintWriter textAreaIndexWriter = ResultDirectoryUtil.createPrintWriter(textAreaIndexFile);
		this.textAreaIndexTemplate.process(map, textAreaIndexWriter);
		textAreaIndexWriter.close();
	}

	void writeTextAreaColumnIndexFile(FormMaster master, TextAreaColumn textAreaColumn, String spreadSheetSourceDirectoryPath) throws IOException, TemplateException {
		HashMap<String, Object> map = createFTLParameters();
		HTMLReportExportModule.registTitle(master, map);
		registTextAreaColumnEntry(map, textAreaColumn, spreadSheetSourceDirectoryPath);
		writeTextAreaColumnFile(textAreaColumn, map);
	}

	void writeTextAreaRowRangeFile(FormMaster master, TextAreaColumn textAreaColumn, 
			TextAreaRowGroup textAreaRowGroup, TextAreaRowRange textAreaRowRange, String path) throws IOException, TemplateException {
		HashMap<String,Object> map = createFTLParameters();
		writeTextAreaRowRangeFile(map, master, textAreaColumn, textAreaRowGroup, textAreaRowRange, path);
	}			
	
	private HashMap<String, Object> registTextAreaColumnEntry(HashMap<String, Object> map, TextAreaColumn textAreaColumn, String spreadSheetSourceDirectoryPath) {
		map.put("textAreaColumnMetadata", textAreaColumn.getTextAreaColumnMetadata());
		map.put("textAreaColumn", textAreaColumn);
		map.put("path", spreadSheetSourceDirectoryPath);
		
		int numPathComponents = StringUtil.split(spreadSheetSourceDirectoryPath, File.separatorChar).size();
		
		StringBuilder returnPath = createRelativePathToParent(numPathComponents + 2);
		textAreaColumn.getTextAreaColumnMetadata().getSourceDirectoryPath();

		map.put("returnPath", returnPath.toString());
		map.put("resultFolder", AppConstants.RESULT_DIRNAME);
		return map;
	}

	private StringBuilder createRelativePathToParent(int numPathComponents) {
		StringBuilder returnPath = null;
		for(int i = 0; i < numPathComponents; i++){
			if(returnPath == null){
				returnPath = new StringBuilder();
			}else{
				returnPath.append("/");
			}
			returnPath.append("..");
		}
		return returnPath;
	}

	private HashMap<String, Object> registTextAreaIndexEntry(HashMap<String, Object> map, FormMaster master, String path, List<FormArea> textareas) {
		HTMLReportExportModule.registTitle(master, map);
		map.put("path", path);
		map.put("master", master);
		map.put("textareas", textareas);
		return map;
	}

	private void writeTextAreaColumnFile(TextAreaColumn textAreaColumn, HashMap<String, Object> map) throws IOException, TemplateException {
		File textAreaColumnFile = new File(new File(this.textAreaDirectoryFile, 
				Integer.toString(textAreaColumn.getTextAreaColumnMetadata().getQuestionIndex())), "index.html");
		PrintWriter textAreaColumnWriter = ResultDirectoryUtil.createPrintWriter(textAreaColumnFile);
		this.textAreaColumnTemplate.process(map, textAreaColumnWriter);
		textAreaColumnWriter.close();
	}

	private void writeTextAreaRowRangeFile(HashMap<String, Object> map, FormMaster master, TextAreaColumn textAreaColumn, 
			TextAreaRowGroup textAreaRowGroup, TextAreaRowRange textAreaRowRange, String path) throws IOException, TemplateException {
		registTextAreaRowRangeEntry(map, master, textAreaColumn, textAreaRowGroup, textAreaRowRange, path);
		File textAreaColumnDirectoryFile = new File(this.textAreaDirectoryFile, 
				Integer.toString(textAreaColumn.getTextAreaColumnMetadata().getQuestionIndex()));
		textAreaColumnDirectoryFile.mkdirs();
		File textAreaRowRangeFile = new File(textAreaColumnDirectoryFile, 
				textAreaRowRange.getTextAreaRowRangeMetadata().getRowRangeIndex()+ ".html");
		PrintWriter textAreaRowRangeWriter = ResultDirectoryUtil.createPrintWriter(textAreaRowRangeFile);
		this.textAreaRowRangeTemplate.process(map, textAreaRowRangeWriter);
		textAreaRowRangeWriter.close();
	}

	private void registTextAreaRowRangeEntry(HashMap<String, Object> map,
			FormMaster master,
			TextAreaColumn textAreaColumn,
			TextAreaRowGroup textAreaRowGroup,
			TextAreaRowRange textAreaRowRange,
			String path) {
		HTMLReportExportModule.registTitle(master, map);
		map.put("numRowRangePages", textAreaColumn.getNumRowRangePages());
		map.put("textAreaColumnMetadata", textAreaColumn.getTextAreaColumnMetadata());
		map.put("textAreaRowRangeMetadata", textAreaRowRange.getTextAreaRowRangeMetadata());
		map.put("textAreaImageItemList", textAreaRowRange.getTextAreaImageItemList());
		map.put("path", path);
	}
}
