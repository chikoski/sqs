package net.sqs2.omr.result.export;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.sqs2.omr.app.App;
import net.sqs2.omr.app.MarkReaderConstants;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.event.QuestionEvent;
import net.sqs2.omr.result.event.QuestionItemEvent;
import net.sqs2.omr.result.event.RowGroupEvent;
import net.sqs2.omr.result.event.SourceDirectoryEvent;
import net.sqs2.omr.result.event.SpreadSheetEvent;
import net.sqs2.omr.result.model.Answer;
import net.sqs2.omr.result.model.MarkAreaAnswer;
import net.sqs2.omr.result.model.MarkAreaAnswerItem;
import net.sqs2.omr.session.MarkReaderSession;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSources;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.source.config.MarkRecognitionConfig;
import net.sqs2.omr.task.PageTask;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class HTMLReportExportModule extends SpreadSheetExportEventAdapter {

    Map<SourceDirectory,Integer> numRowsTotalMap = new HashMap<SourceDirectory,Integer>();
	
    TextAreaImageFileExportModule textAreaImageFileExportModule;
    ChartImageFileExportModule chartImageFileExportModule;
    CSSFileExportModule cssFileExportModule;
	
    float densityThreshold;
    float recognitionMargin;
	
    int[][] valueTotalMatrix;
    int[] numNoValues;
    int[] numMultipleValues;
    long[] targetLastModifiedArray;
	
    String skin;
	
    public HTMLReportExportModule(String skin){
	this.skin = skin;
	this.textAreaImageFileExportModule = new TextAreaImageFileExportModule();
	this.chartImageFileExportModule = new ChartImageFileExportModule();
	
	// XXX
	this.cssFileExportModule = new CSSFileExportModule();
    }
	
    PrintWriter createPrintWriter(File file) throws IOException {
	file.getParentFile().mkdirs();
	return new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)), "UTF-8"));
    }
	
    public int getNumRowsTotal(SourceDirectory sourceDirectory){
	return numRowsTotalMap.get(sourceDirectory);
    }

    private long max(long a, long b){
	if(a <= b){
	    return b;
	}else{
	    return a;
	}
    }

    @Override
    public void startSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
	MarkRecognitionConfig config = sourceDirectoryEvent.getSourceDirectory().getConfiguration().getConfig().getSourceConfig().getMarkRecognitionConfig();
	this.densityThreshold = config.getDensity();
	this.recognitionMargin = config.getRecognitionMargin();
    }
	
    @Override
    public void endSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
    }

    @Override
    public void startRowGroup(RowGroupEvent rowGroupEvent) {
	SourceDirectory sourceDirectory = rowGroupEvent.getSourceDirectory();
	this.numRowsTotalMap.put(sourceDirectory, 0);
    }
	
    @Override
    public void endRowGroup(RowGroupEvent rowGroupEvent) {
	SourceDirectory sourceDirectory = rowGroupEvent.getSourceDirectory();
	int numPages = sourceDirectory.getPageMaster().getNumPages();
	SourceDirectory parentSourceDirectory = rowGroupEvent.getParentSourceDirectory();
	int numRows = sourceDirectory.getNumPageIDs() / numPages;
	this.numRowsTotalMap.put(sourceDirectory, numRows);
	if(parentSourceDirectory != null){
	    Integer parentNumRows = this.numRowsTotalMap.get(parentSourceDirectory);
	    if(parentNumRows != null){
		this.numRowsTotalMap.put(parentSourceDirectory, parentNumRows + numRows);	
	    }
	}
    }

    @Override
    public void startSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
	FormMaster master = spreadSheetEvent.getFormMaster();
		
	this.valueTotalMatrix = new int[master.getNumColumns()][];
	this.numNoValues = new int[master.getNumColumns()];
	this.numMultipleValues = new int[master.getNumColumns()];
	this.targetLastModifiedArray = new long[master.getNumColumns()];
		
	for(int columnIndex = 0; columnIndex < master.getNumColumns(); columnIndex++){
	    List<FormArea> formAreaList = master.getFormAreaList(columnIndex);
	    FormArea formArea = formAreaList.get(0);
	    if(formArea.isSelect1() || formArea.isSelect()){
		this.valueTotalMatrix[columnIndex] = new int[formAreaList.size()];
	    }
	}
    }

    @Override
    public void endSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
	try{
	    Logger.getAnonymousLogger().info("Export: "+spreadSheetEvent.getSourceDirectoryEvent().getSourceDirectory());
	    exportReport(spreadSheetEvent, this.skin);
	}catch (IOException ignore) {
	    ignore.printStackTrace();
	}
    }
	
    @Override
    public void startQuestion(QuestionEvent questionEvent) {
	List<FormArea> formAreaList = questionEvent.getFormAreaList();
	FormArea formArea = formAreaList.get(0);
		
	Answer answer = questionEvent.getRowEvent().getRow().getAnswer(questionEvent.getColumnIndex());
	if(answer == null){
	    return;
	}
	if(formArea.isSelect1()){
	    int numSelectedItemIndex = -1;
	    int numSelected = 0;

	    for(MarkAreaAnswerItem markAreaAnswerItem: ((MarkAreaAnswer)answer).createMarkAreaAnswerItemSet().getMarkedAnswerItems(densityThreshold, recognitionMargin)){
		numSelectedItemIndex = markAreaAnswerItem.getItemIndex();
		numSelected++;
	    }

	    if(numSelected == 0){
		this.numNoValues[formArea.getColumnIndex()] ++;	
	    }else if(numSelected == 1){
		this.valueTotalMatrix[formArea.getColumnIndex()][numSelectedItemIndex] ++;
	    }else{
		this.numMultipleValues[formArea.getColumnIndex()] ++;
	    }
			
	}else if(formArea.isSelect()){
	    for(int itemIndex = 0; itemIndex < formAreaList.size(); itemIndex++){
		MarkAreaAnswerItem markAreaAnswerItem = ((MarkAreaAnswer)answer).getMarkAreaAnswerItem(itemIndex);
		if(markAreaAnswerItem.isSelectMultiSelected(((MarkAreaAnswer)answer), this.densityThreshold)){
		    this.valueTotalMatrix[formArea.getColumnIndex()][itemIndex] ++;
		}
	    }
	}
    }

    @Override
    public void startQuestionItem(QuestionItemEvent questionItemEvent) {
	FormArea formArea = questionItemEvent.getFormArea();
	if(formArea.isTextArea()){
	    //long sessionID = 
	    //	questionItemEvent.getQuestionEvent().getRowEvent().getRowGroupEvent().getSpreadSheetEvent().getSourceDirectoryEvent().getMasterEvent().getSessionEvent().getSessionID();
	    //MarkReaderSession session = (MarkReaderSession)Sessions.get(sessionID);
	    if(MarkReaderSession.isExportTextAreaImageEnabled()){
		this.textAreaImageFileExportModule.exportTextAreaImageFile(questionItemEvent.getPageEvent(), questionItemEvent.getQuestionEvent(), formArea);
	    }
	}else if(formArea.isMarkArea()){
	    PageTask pageTask = questionItemEvent.getPageEvent().getPageTask();
	    long targetFileLastModified = pageTask.getPageID().getFileResourceID().getLastModified();
	    long configHandlerLastModified = pageTask.getConfigHandlerFileResourceID().getLastModified();
	    long masterFileLastModified = questionItemEvent.getQuestionEvent().getFormMaster().getLastModified();
	    long prevTargetLastModified = this.targetLastModifiedArray[formArea.getColumnIndex()];
			
	    this.targetLastModifiedArray[formArea.getColumnIndex()] = max(max(targetFileLastModified, configHandlerLastModified),
									  max(masterFileLastModified, prevTargetLastModified));
	}
    }
	
    /*
      class ResultIndexTemplateLoader extends TemplateLoader {
      ResultIndexTemplateLoader(String skin)throws IOException{
      super(MarkReaderConstants.USER_CUSTOMIZE_CONSTANTS_DIR, "ftl", skin);
      }
      }
    */
    public void exportReport(SpreadSheetEvent spreadSheetEvent, String skin) throws IOException{

	SourceDirectoryEvent sourceDirectoryEvent = spreadSheetEvent.getSourceDirectoryEvent();
	File sourceDirectoryFile = new File(sourceDirectoryEvent.getSourceDirectory().getRoot(), sourceDirectoryEvent.getSourceDirectory().getPath());
	File resultDirectoryFile = new File(sourceDirectoryFile, App.getResultDirectoryName());
	File chartDirectoryFile = new File(sourceDirectoryFile.getAbsolutePath()+File.separatorChar+App.getResultDirectoryName()+File.separatorChar+"CHART");
		
	chartDirectoryFile.mkdirs();

	File resultDirectoryIndexFile = new File(resultDirectoryFile, "index.html");

	PrintWriter resultDirectoryIndexWriter = createPrintWriter(resultDirectoryIndexFile);
		
	try{
	    Map<String,Object> map = new HashMap<String,Object>();
	    map.put("title", createReportTitle(spreadSheetEvent)); // XXX The value should be the spreadsheet's title

	    map.put("csvFileName", SpreadSheetExportModule.createSpreadSheetFileName(spreadSheetEvent, CSVExportModule.SUFFIX));
	    map.put("xlsFileName", SpreadSheetExportModule.createSpreadSheetFileName(spreadSheetEvent, ExcelExportModule.SUFFIX));

	    TemplateLoader loader = new TemplateLoader(MarkReaderConstants.USER_CUSTOMIZE_CONSTANTS_DIR, "ftl");
	    Template resultDirectoryIndexTemplate = loader.getTemplate("index.ftl", "UTF-8");
	    resultDirectoryIndexTemplate.process(map, resultDirectoryIndexWriter);	
			
	    exportReportCore(spreadSheetEvent, sourceDirectoryEvent, skin);
			
	}catch(Exception ex){
	    ex.printStackTrace();
	}

	resultDirectoryIndexWriter.close();
    }
	
    private void exportReportCore(SpreadSheetEvent spreadSheetEvent, SourceDirectoryEvent sourceDirectoryEvent, String skin) throws IOException, TemplateException{
		
	File textAreaDirectoryFile = this.textAreaImageFileExportModule.createTextAreaDirectoryFile(sourceDirectoryEvent.getSourceDirectory());
	File chartDirectoryFile = this.chartImageFileExportModule.createChartDirectoryFile(sourceDirectoryEvent.getSourceDirectory());
		
	TextAreaExporter textAreaExportModule = new TextAreaExporter(this, textAreaDirectoryFile, skin);
	ChartExportModule chartExportModule = new ChartExportModule(this, chartDirectoryFile, skin);

	SessionSource sessionSource = SessionSources.get(spreadSheetEvent.getSourceDirectoryEvent().getMasterEvent().getSessionEvent().getSessionID());

    	for (Map.Entry<String, ArrayList<FormArea>> entry : sourceDirectoryEvent.getFormMaster().getFormAreaListEntrySet()) {
	    String key = entry.getKey();
	    List<FormArea> formAreaList = entry.getValue();
	    FormArea defaultFormArea = formAreaList.get(0);
	    if (defaultFormArea.isTextArea() && MarkReaderSession.isExportTextAreaImageEnabled()) {
		textAreaExportModule.writeTextAreaColumnHTMLFile(sessionSource, defaultFormArea, spreadSheetEvent.getSpreadSheet());
	    }else if(defaultFormArea.isSelect1() && MarkReaderSession.isExportChartImageEnabled()){
		chartExportModule.drawPieChartImage(formAreaList, spreadSheetEvent.getSpreadSheet());
	    }else if(defaultFormArea.isSelect() && MarkReaderSession.isExportChartImageEnabled()){
		chartExportModule.drawBarChartImage(formAreaList, spreadSheetEvent.getSpreadSheet());
	    }
	}
    	
    	chartExportModule.writeChartIndexHTMLFile(chartDirectoryFile, spreadSheetEvent.getSpreadSheet(), MarkReaderSession.isExportChartImageEnabled());
    	
    	if(MarkReaderSession.isExportTextAreaImageEnabled()){
	    SourceDirectory sourceDirectory = spreadSheetEvent.getSourceDirectoryEvent().getSourceDirectory();
	    FormMaster master = (FormMaster)sourceDirectory.getPageMaster();
	    textAreaExportModule.writeTextAreaIndexFile(new HashMap<String,Object>(), master, sourceDirectory.getPath());	
    	}
    }

    public static String createReportTitle(SpreadSheetEvent event){
	return event.getFormMaster().getName();
    }

}
