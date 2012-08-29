/**
 * 
 */
package net.sqs2.omr.result.export;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sqs2.omr.app.App;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.event.PageEvent;
import net.sqs2.omr.result.event.QuestionEvent;
import net.sqs2.omr.result.event.QuestionItemEvent;
import net.sqs2.omr.result.event.RowEvent;
import net.sqs2.omr.result.event.RowGroupEvent;
import net.sqs2.omr.result.event.SpreadSheetEvent;
import net.sqs2.omr.result.event.SpreadSheetExportEventFilter;
import net.sqs2.omr.result.event.SpreadSheetExportEventProducer;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.source.SpreadSheet;
import freemarker.template.Template;
import freemarker.template.TemplateException;

class TextAreaExporter extends TemplateExporter{
	
	/**
	 * 
	 */
	private final HTMLReportExportModule reportExportModule;
	private Map<String,String> textAreaValueMap = new HashMap<String,String>();
	public static final int NUM_IMAGES_PAR_PAGE = 50;
	
	File textareaIndexFile;
	Template textAreaIndexTemplate;
	Template textAreaColumnTemplate;
	Template textAreaRowRangeTemplate;
	
	TextAreaExporter(HTMLReportExportModule reportExportModule, File textareaDirectoryFile, String skin)throws IOException{
		super(skin);
		this.reportExportModule = reportExportModule;
		this.textareaIndexFile = textareaDirectoryFile;						
		this.textAreaIndexTemplate = this.loader.getTemplate("textAreaIndex.ftl", "UTF-8");
		this.textAreaColumnTemplate = this.loader.getTemplate("textAreaColumn.ftl", "UTF-8");
		this.textAreaRowRangeTemplate = this.loader.getTemplate("textAreaRowRange.ftl", "UTF-8");
	}
	
	int countNumRows(SourceDirectory targetSourceDirectory){
		List<SourceDirectory> sourceDirectoryList = targetSourceDirectory.getDescendentSourceDirectoryList();
		int numPages = targetSourceDirectory.getPageMaster().getNumPages();
		int numRows = 0;
		for(int sourceDirectoryIndex = 0; sourceDirectoryIndex < sourceDirectoryList.size() ; sourceDirectoryIndex++){
			SourceDirectory sourceDirectory = sourceDirectoryList.get(sourceDirectoryIndex);
			numRows += countNumRows(sourceDirectory);
		}
		return numRows + targetSourceDirectory.getPageIDList().size() / numPages;
	}
	
	void writeTextAreaColumnHTMLFile(final SessionSource sessionSource, final FormArea formArea, final SpreadSheet spreadSheet)throws IOException, TemplateException{
		
		SpreadSheetExportEventProducer producer = new SpreadSheetExportEventProducer(sessionSource,  new SpreadSheetExportEventFilter(){
			public boolean filter(SpreadSheetEvent spreadSheetEvent){
				return true;
			}
			
			public boolean filter(RowGroupEvent rowGroupEvent){
				return true;
			}
			
			public boolean filter(RowEvent rowEvent){
				return true;
			}

			public boolean filter(QuestionEvent questionEvent){
				return false;
			}

			public boolean filter(PageEvent pageEvent){
				return false;
			}

			public boolean filter(QuestionItemEvent questionItemEvent){
				return false;
			}
		});
		
		int numRows = reportExportModule.getNumRowsTotal(spreadSheet.getSourceDirectory());
		producer.addEventConsumer(new ColumnRowsWorker(formArea, numRows));
		
		producer.produceSpreadSheetEvent(spreadSheet);
	}
	
	class ColumnRowsWorker extends SpreadSheetExportEventAdapter{

		TextAreaColumnMetadata textAreaColumnMetadata;
		TextAreaColumn textAreaColumn;
		TextAreaRowRangeMetadata textAreaRowRangeMetadata = null;
		ArrayList<TextAreaImageItem> textAreaImageItemList = null;
		int rowRangeIndex = 0;
		ArrayList<TextAreaImageItem> textAreaImageItemListFull = null;
		int numRows;
		FormArea formArea;
		
		ColumnRowsWorker(FormArea formArea, int numRows){
			this.formArea = formArea;
			this.numRows = numRows;
		}
		
		@Override
		public void startSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
			textAreaColumnMetadata = new TextAreaColumnMetadata(formArea, 
					spreadSheetEvent.getSpreadSheet().getSourceDirectory().getPath(),
					"TEXTAREA",
					formArea.getColumnIndex(), numRows,
					(int)Math.ceil(1.0 * numRows / NUM_IMAGES_PAR_PAGE));
			textAreaColumn = new TextAreaColumn(textAreaColumnMetadata);
			textAreaImageItemListFull = new ArrayList<TextAreaImageItem>();
		}
		
		@Override
		public void startRowGroup(RowGroupEvent rowGroupEvent) {
			SourceDirectory sourceDirectory = rowGroupEvent.getSourceDirectory();
/*			
			*/
			textAreaImageItemList = new ArrayList<TextAreaImageItem>();
			rowRangeIndex = 0;
			System.err.println("\t\tRowGroup: "+sourceDirectory);
		}

		@Override
		public void startRow(RowEvent rowEvent) {
			
			String key = formArea.getColumnIndex()+"-"+rowEvent.getIndex();
			String value = textAreaValueMap.get(key);
			
			TextAreaImageItem textAreaImageItem = new TextAreaImageItem(rowEvent.getRowGroupEvent().getSourceDirectory().getPath(),
					rowEvent.getIndex(), value);
			textAreaImageItemList.add(textAreaImageItem);
			textAreaImageItemListFull.add(textAreaImageItem);
			
			int rowIndex = rowEvent.getIndex();
			System.err.println("\t\tRow: "+rowIndex);
			
			System.err.println("\t\t  "+rowIndex +"\tnumRows-1= "+(numRows-1)+"\tnumEvents-1="+(rowEvent.getNumEvents() - 1));
			

			if(rowIndex % NUM_IMAGES_PAR_PAGE == NUM_IMAGES_PAR_PAGE - 1 || rowIndex == rowEvent.getNumEvents() - 1 || textAreaImageItemList.size() == numRows){

				SourceDirectory sourceDirectory = rowEvent.getRowGroupEvent().getSourceDirectory();
				//int numPages = sourceDirectory.getPageMaster().getNumPages();
				int numRows = sourceDirectory.getNumPageIDs();
				
				String subDirectoryPath = null;
				if(sourceDirectory == null || "".equals(sourceDirectory)){
					subDirectoryPath = "TEXTAREA";
				}else{
					String canonicalPath = sourceDirectory.getPath().replaceAll("\\\\", "/");
					subDirectoryPath = "../../../"+canonicalPath+"/"+App.getResultDirectoryName()+"/TEXTAREA/"+formArea.getColumnIndex();
					
					//FIXME!  link corrupt in subdirectory column index
				}

				textAreaRowRangeMetadata = new TextAreaRowRangeMetadata(sourceDirectory,
						subDirectoryPath,
						rowRangeIndex,
						rowRangeIndex * NUM_IMAGES_PAR_PAGE + 1,
						rowIndex + 1);
				
				
				TextAreaRowRange textAreaRowRange = new TextAreaRowRange(textAreaRowRangeMetadata); 
				textAreaColumn.addTextAreaRowRange(textAreaRowRange);

				try{
					writeTextAreaRowRangeFile(new HashMap<String,Object>(),
							textAreaColumnMetadata,
							textAreaRowRangeMetadata,
							textAreaImageItemList,
							sourceDirectory.getPath());
					rowRangeIndex++;
					
					if(rowIndex == rowEvent.getNumEvents() - 1 || textAreaImageItemList.size() == numRows){
						rowRangeIndex = 0;
					}
					
					System.err.println("\t\t****** write "+textAreaImageItemList);
				}catch(TemplateException ex){
					ex.printStackTrace();
				}catch(IOException ex){
					ex.printStackTrace();
				}
				
			}
		}
		
		@Override
		public void endRowGroup(RowGroupEvent rowGroupEvent){
			textAreaImageItemList.clear();
		}

		@Override
		public void endSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
			String path = spreadSheetEvent.getSourceDirectoryEvent().getSourceDirectory().getPath();
			try{
				if(textAreaColumn.getTextAreaRowRangeList().size() == 1){
					writeTextAreaColumnFile(new HashMap<String,Object>(),
							textAreaColumn, 
							textAreaColumnMetadata,
							textAreaRowRangeMetadata,
							textAreaImageItemListFull,
							path);
				}else{
					writeTextAreaColumnFile(new HashMap<String,Object>(), textAreaColumn, path);
				}
			}catch(TemplateException ex){
				ex.printStackTrace();
			}catch(IOException ex){
				ex.printStackTrace();
			}
			textAreaImageItemListFull.clear();
		}
	}
	
	
	private void writeTextAreaColumnFile(HashMap<String,Object> map,
			TextAreaColumn textAreaColumn,
			TextAreaColumnMetadata textAreaRowMetadata,
			TextAreaRowRangeMetadata textAreaRowRangeMetadata,
			ArrayList<TextAreaImageItem> textAreaImageItemList,
			String path
			) throws IOException,TemplateException {
		
		registTextAreaColumnEntry(map, textAreaColumn, textAreaRowRangeMetadata.getPath());
		
		registTextAreaRowRangeEntry(map,
				textAreaRowMetadata,
				textAreaRowRangeMetadata,
				textAreaImageItemList,
				path);
	
		writeTextAreaColumnFile(textAreaColumn, map);
	}

	void writeTextAreaIndexFile(HashMap<String,Object> map, FormMaster master, String path)throws IOException,TemplateException{
		File textareaIndexFile = new File(this.textareaIndexFile, "index.html");
		List<FormArea> textareas = new LinkedList<FormArea>();			
		for(String qid: master.getQIDSet()){
			FormArea defaultFormArea = master.getFormAreaList(qid).get(0);
			if(defaultFormArea.isTextArea()){
				textareas.add(defaultFormArea);
			}
		}
		registTextAreaIndexEntry(map, master, path, textareas);
		map.put("title", master.getTitle());
		map.put("textareas", textareas);
		PrintWriter textAreaIndexWriter = this.reportExportModule.createPrintWriter(textareaIndexFile);
		this.textAreaIndexTemplate.process(map, textAreaIndexWriter);
		textAreaIndexWriter.close();
	}

	private void writeTextAreaColumnFile(HashMap<String,Object> map, TextAreaColumn textAreaColumn, String path) throws IOException,TemplateException {
		registTextAreaColumnEntry(map, textAreaColumn, path);
		writeTextAreaColumnFile(textAreaColumn, map);
	}
	
	private HashMap<String,Object> registTextAreaColumnEntry(HashMap<String,Object> map, TextAreaColumn textAreaColumn, String path){
		map.put("textAreaColumnMetadata", textAreaColumn.getTextAreaColumnMetadata());
		map.put("textAreaColumn", textAreaColumn);
		map.put("path", path);
		return map;
	}
	
	private HashMap<String,Object> registTextAreaIndexEntry(HashMap<String,Object> map, FormMaster master, String path, List<FormArea> textareas){
		map.put("path", path);
		map.put("master", master);
		map.put("textareas", textareas);
		return map;
	}
	
	private void writeTextAreaColumnFile(TextAreaColumn textAreaColumn, HashMap<String,Object> map) throws IOException,TemplateException {
		File textAreaColumnFile = new File(new File(textareaIndexFile, Integer.toString(textAreaColumn.getTextAreaColumnMetadata().getColumnIndex())), "index.html" );
		PrintWriter textAreaColumnWriter = this.reportExportModule.createPrintWriter(textAreaColumnFile);
		textAreaColumnTemplate.process(map, textAreaColumnWriter);
		textAreaColumnWriter.close();
	}
	
	private void writeTextAreaRowRangeFile(HashMap<String,Object> map,
			TextAreaColumnMetadata textAreaColumnMetadata,
			TextAreaRowRangeMetadata textAreaRowRangeMetadata,
			ArrayList<TextAreaImageItem> textAreaImageItemList,
			String path)
			throws IOException,TemplateException{
		registTextAreaRowRangeEntry(map, textAreaColumnMetadata, textAreaRowRangeMetadata,
				textAreaImageItemList,
				path);
		File textAreaColumnDirectoryFile = new File(textareaIndexFile, Integer.toString(textAreaColumnMetadata.getColumnIndex()));
		textAreaColumnDirectoryFile.mkdirs();
		File textAreaRowRangeFile = new File(textAreaColumnDirectoryFile, textAreaRowRangeMetadata.getRowRangeIndex() + ".html" );
		PrintWriter textAreaRowRangeWriter = this.reportExportModule.createPrintWriter(textAreaRowRangeFile);
		textAreaRowRangeTemplate.process(map, textAreaRowRangeWriter);
		textAreaRowRangeWriter.close();
	}

	private void registTextAreaRowRangeEntry(HashMap<String, Object> map,
			TextAreaColumnMetadata textAreaColumnMetadata,
			TextAreaRowRangeMetadata textAreaRowRangeMetadata,
			ArrayList<TextAreaImageItem> textAreaImageItemList,
			String path) {
		map.put("textAreaColumnMetadata", textAreaColumnMetadata);
		map.put("textAreaRowRangeMetadata", textAreaRowRangeMetadata);
		map.put("textAreaImageItemList", textAreaImageItemList);
		map.put("path", path);
	}

	public class TextAreaColumn{
		private TextAreaColumnMetadata textAreaRowMetadata;
		private List<TextAreaRowRange> textAreaRowRangeList;
		
		TextAreaColumn(TextAreaColumnMetadata textAreaRowMetadata){
			this.textAreaRowMetadata = textAreaRowMetadata;
			this.textAreaRowRangeList = new ArrayList<TextAreaRowRange>();
		}
		
		public TextAreaColumnMetadata getTextAreaColumnMetadata(){
			return this.textAreaRowMetadata;
		}
		
		public List<TextAreaRowRange> getTextAreaRowRangeList(){
			return this.textAreaRowRangeList;
		}
		
		public int getTextAreaRowRangeListSize(){
			return this.textAreaRowRangeList.size();
		}
		
		public void addTextAreaRowRange(TextAreaRowRange textAreaRowRange){
			this.textAreaRowRangeList.add(textAreaRowRange);
		}
		
		public void clearTextAreaRowRangeList(){
			this.textAreaRowRangeList.clear();
		}
	}
			
	public class TextAreaColumnMetadata extends ColumnMetadata{
		private int numPages;
		
		TextAreaColumnMetadata(FormArea defaultFormArea, String sourceDirectoryPath, String textAreaDirectoryPath, int columnIndex, int numRows, int numPages){
			super(defaultFormArea, sourceDirectoryPath, textAreaDirectoryPath, columnIndex, numRows);
			this.numPages = numPages;
		}
		
		public String[] getHints(){
			return this.defaultFormArea.getHints();
		}
		
		public int getNumPages(){
			return this.numPages;
		}
	}
	
	public class TextAreaRowRangeMetadata{
		private SourceDirectory sourceDirectory;
		private String path;
		private int rowRangeIndex;
		private int startRow, endRow;
		TextAreaRowRangeMetadata(SourceDirectory sourceDirectory,
				String path,
				int rowRangeIndex, int startRow, int endRow){
			this.sourceDirectory = sourceDirectory;
			this.path = path;
			this.rowRangeIndex = rowRangeIndex;
			this.startRow = startRow;
			this.endRow = endRow;
		}
		
		public String getPath(){
			return this.path;
		}
		public SourceDirectory getSourceDirectory(){
			return this.sourceDirectory;
		}
		public int getRowRangeIndex(){
			return this.rowRangeIndex;
		}
		public int getRowRangePageNum(){
			return this.rowRangeIndex+1;
		}
		public int getStartRow(){
			return this.startRow;
		}
		public int getEndRow(){
			return this.endRow;
		}
	}
	
	public class TextAreaRowRange{
		private TextAreaRowRangeMetadata textAreaRowRangeMetadata;
		private ArrayList<TextAreaImageItem> textAreaImageItemList;
		
		TextAreaRowRange(TextAreaRowRangeMetadata textAreaRowRangeMetadata){
			this.textAreaRowRangeMetadata = textAreaRowRangeMetadata;
		}
		
		public TextAreaRowRangeMetadata getTextAreaRowRangeMetadata(){
			return this.textAreaRowRangeMetadata;
		}
		
		public List<TextAreaImageItem> getTextAreaImageItemList(){
			return this.textAreaImageItemList;
		}
	}

	public class TextAreaImageItem{
		private String path;
		private int rowIndex;
		private String value;
		
		TextAreaImageItem(String path, int rowIndex, String value){
			this.path = path;
			this.rowIndex = rowIndex;
			this.value = value;
		}
		
		public String toString(){
			return "TextAreaImageItem["+path+","+rowIndex+","+value+"]";
		}
		
		public String getPath(){
			return path;
		}

		public int getRowIndex() {
			return rowIndex;
		}

		public String getValue() {
			if(value == null){
				return "";
			}
			return value;
		}
	}
}
