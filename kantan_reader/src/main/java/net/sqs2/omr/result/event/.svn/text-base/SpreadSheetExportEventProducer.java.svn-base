package net.sqs2.omr.result.event;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.model.Row;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.source.SpreadSheet;
import net.sqs2.omr.task.PageTask;

public class SpreadSheetExportEventProducer extends ExportEventProducer{
		
	SpreadSheetExportEventFilter spreadSheetContentsEventFilter;
	ArrayList<SpreadSheetExportEventConsumer> consumerList = new ArrayList<SpreadSheetExportEventConsumer>();
	
	public void addEventConsumer(SpreadSheetExportEventConsumer consumer){
		this.consumerList.add(consumer);
	}

	public SpreadSheetExportEventProducer(SessionSource sessionSource){
		super(sessionSource);
	}

	public SpreadSheetExportEventProducer(SessionSource sessionSource, SpreadSheetExportEventFilter filter){
		super(sessionSource, filter);
		this.spreadSheetContentsEventFilter = filter;
	}
	
	public void produceSpreadSheetEvent(SpreadSheet spreadSheet){
		SourceDirectory sourceDirectory = spreadSheet.getSourceDirectory();
		FormMaster master = (FormMaster)sourceDirectory.getPageMaster();
		SourceDirectoryEvent sourceDirectoryEvent = new SourceDirectoryEvent(null, master, 0);
		sourceDirectoryEvent.setSourceDirectory(sourceDirectory);
		sourceDirectoryEvent.setIndex(0);
		sourceDirectoryEvent.setStart();
		processSourceDirectory(sourceDirectoryEvent, master);
	}

	@Override
	protected void processSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent, FormMaster master){
		SpreadSheetEvent spreadSheetEvent = new SpreadSheetEvent(sourceDirectoryEvent, master, 1);
		
		SpreadSheet spreadSheet = new SpreadSheet(sourceDirectoryEvent.getSourceDirectory()); 
		spreadSheetEvent.setStart();
		spreadSheetEvent.setIndex(0);
		spreadSheetEvent.setSpreadSheet(spreadSheet);

		startSpreadSheet(spreadSheetEvent);
		processSpreadSheet(spreadSheetEvent, master);
		
		spreadSheetEvent.setEnd();
		endSpreadSheet(spreadSheetEvent);
	}
	

	protected void processSpreadSheet(SpreadSheetEvent spreadSheetEvent, FormMaster master){
		SourceDirectory targetSourceDirectory = spreadSheetEvent.getSpreadSheet().getSourceDirectory();
		List<SourceDirectory> sourceDirectoryList = targetSourceDirectory.getDescendentSourceDirectoryList();
		RowGroupEvent rowGroupEvent = new RowGroupEvent(spreadSheetEvent, master, spreadSheetEvent.getSpreadSheet(), sourceDirectoryList.size() + 1);
		int numPrevRowsTotal = 0;
		
		System.err.println("----------------");
		System.err.println("SpreadSheet "+targetSourceDirectory.getPath());
		System.err.println("descendent:");
		
		for(int sourceDirectoryIndex = 0; sourceDirectoryIndex < sourceDirectoryList.size() ; sourceDirectoryIndex++){
			SourceDirectory sourceDirectory = sourceDirectoryList.get(sourceDirectoryIndex);
			rowGroupEvent.setStart();
			rowGroupEvent.setIndex(sourceDirectoryIndex);
			rowGroupEvent.setNumPrevRowsTotal(numPrevRowsTotal);
			rowGroupEvent.setParentSourceDirectory(targetSourceDirectory);
			rowGroupEvent.setSourceDirectory(sourceDirectory);
			if(this.spreadSheetContentsEventFilter != null && ! this.spreadSheetContentsEventFilter.filter(rowGroupEvent)){
				return;
			}
			startRowGroup(rowGroupEvent);
			processRowGroup(rowGroupEvent, master);
			rowGroupEvent.setEnd();
			endRowGroup(rowGroupEvent);
			numPrevRowsTotal += sourceDirectory.getNumPageIDs() / master.getNumPages();
		}
		
		System.err.println("self:");
		rowGroupEvent.setIndex(sourceDirectoryList.size());
		rowGroupEvent.setNumPrevRowsTotal(numPrevRowsTotal);
		rowGroupEvent.setSourceDirectory(targetSourceDirectory);
		rowGroupEvent.setParentSourceDirectory(targetSourceDirectory);
		rowGroupEvent.setStart();
		if(this.spreadSheetContentsEventFilter != null && ! this.spreadSheetContentsEventFilter.filter(rowGroupEvent)){
			return;
		}
		startRowGroup(rowGroupEvent);
		processRowGroup(rowGroupEvent, master);
		rowGroupEvent.setEnd();
		endRowGroup(rowGroupEvent);
		
		System.err.println("----------------");
	}

	protected void processRowGroup(RowGroupEvent rowGroupEvent, FormMaster master){
		SourceDirectory sourceDirectory = rowGroupEvent.getSourceDirectory();
		System.err.println("RowGroup: "+sourceDirectory.getPath());// TODO: remove this line
		int numPages = master.getNumPages();
		List<PageID> pageIDList = sourceDirectory.getPageIDList();
		int numRows = pageIDList.size() / numPages;
		RowEvent rowEvent = new RowEvent(rowGroupEvent, numRows);
		for(int rowIndex = 0; rowIndex < numRows; rowIndex++){
			rowEvent.setStart();
			rowEvent.setIndex(rowIndex);
			try{
				Row row = (Row)rowAccessor.get(master.getRelativePath(), sourceDirectory.getPath(), rowIndex);
				rowEvent.setRow(row);
				rowEvent.setRowIndex(rowIndex);
				rowEvent.setPageIDList(pageIDList);

				if(this.spreadSheetContentsEventFilter != null && ! this.spreadSheetContentsEventFilter.filter(rowEvent)){
					return;
				}
			
				if(row == null){
					Logger.getAnonymousLogger().severe("row == null");
					return;
				}

				rowEvent.setTaskErrorMultiHashMap(row.getTaskErrorMultiHashMap());

				System.err.print(" "+rowEvent.getRowIndex());// TODO: remove this line
				startRow(rowEvent);
				processRow(rowEvent, master);
				rowEvent.setEnd();
				endRow(rowEvent);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		System.err.println();// TODO: remove this line
	}

	protected void processRow(RowEvent rowEvent, FormMaster master){
		int numColumns = master.getNumColumns();
		int numPages = master.getNumPages();
		PageEvent pageEvent = new PageEvent(rowEvent, master);
		QuestionEvent questionEvent = new QuestionEvent(rowEvent, master);
		int prevPageIndex = -1;
		
		List<PageID> pageIDList = rowEvent.getPageIDList();
			
		for(int columnIndex = 0; columnIndex < numColumns; columnIndex++){
			List<FormArea> formAreaList = master.getFormAreaList(columnIndex);
			int pageIndex = formAreaList.get(0).getPageIndex();
			if(prevPageIndex != pageIndex){
				
				if(prevPageIndex == -1){
					pageEvent.setEnd();
					endPage(pageEvent);
				}
				
				pageEvent.setStart();
				int pageNumber = pageIndex + 1;
				PageID pageID = pageIDList.get(numPages * rowEvent.getRowIndex() + pageIndex);
				PageTask pageTask = this.taskAccessor.get(master, pageNumber, pageID);
				pageEvent.setPageTask(pageTask);
				pageEvent.setPageIndex(pageIndex);
				
				if(this.spreadSheetContentsEventFilter != null && ! this.spreadSheetContentsEventFilter.filter(pageEvent)){
					return;
				}
				startPage(pageEvent);
			}
			questionEvent.setStart();
			questionEvent.setIndex(columnIndex);
			questionEvent.setColumnIndex(columnIndex);
			questionEvent.setFormAreaList(formAreaList);
			if(this.spreadSheetContentsEventFilter != null && ! this.spreadSheetContentsEventFilter.filter(questionEvent)){
				return;
			}

			if(questionEvent.getAnswer() == null){
				Logger.getAnonymousLogger().severe("answer == null");
				return;
			}

			startQuestion(questionEvent);
			processQuestion(pageEvent, questionEvent, master, formAreaList);
			questionEvent.setEnd();
			endQuestion(questionEvent);
			
			prevPageIndex = pageIndex;
		}
		
		if(prevPageIndex != -1){
			pageEvent.setEnd();
			endPage(pageEvent);
		}	

	}

	protected void processQuestion(PageEvent pageEvent, QuestionEvent questionEvent, FormMaster master, List<FormArea> formAreaList){
		int numItems = formAreaList.size();
		QuestionItemEvent questionItemEvent = new QuestionItemEvent(questionEvent);
		for(int itemIndex = 0; itemIndex < numItems; itemIndex++){
			questionItemEvent.setStart();
			questionItemEvent.setPageEvent(pageEvent);
			questionItemEvent.setItemIndex(itemIndex);
			FormArea formArea = formAreaList.get(itemIndex);
			questionItemEvent.setFormArea(formArea);
			startQuestionItem(questionItemEvent);
			processQuestionItem(questionItemEvent, master, formAreaList.get(itemIndex));
			questionItemEvent.setEnd();
			endQuestionItem(questionItemEvent);
		}
	}

	protected void processQuestionItem(QuestionItemEvent questionItemEvent, FormMaster master, FormArea formArea){
		// do nothing
	}
	
	

	public void startSession(SessionEvent sessionEvent){
		for(ExportEventConsumer consumer: this.consumerList){
			consumer.startSession(sessionEvent);
		}
	}
	
	public void endSession(SessionEvent sessionEvent){
		for(ExportEventConsumer consumer: this.consumerList){
			consumer.endSession(sessionEvent);
		}
	}
	
	public void startMaster(MasterEvent masterEvent){
		for(ExportEventConsumer consumer: this.consumerList){
			consumer.startMaster(masterEvent);
		}
	}
	
	public void endMaster(MasterEvent masterEvent){
		for(ExportEventConsumer consumer: this.consumerList){
			consumer.endMaster(masterEvent);
		}
	}
	
	public void startSourceDirectory(SourceDirectoryEvent spreadSheetEvent){
		for(ExportEventConsumer consumer: this.consumerList){
			consumer.startSourceDirectory(spreadSheetEvent);
		}		
	}
	
	public void endSourceDirectory(SourceDirectoryEvent spreadSheetEvent){
		for(ExportEventConsumer consumer: this.consumerList){
			consumer.endSourceDirectory(spreadSheetEvent);
		}
	}

	

	public void startSpreadSheet(SpreadSheetEvent spreadSheetEvent){
		for(SpreadSheetExportEventConsumer consumer: this.consumerList){
			consumer.startSpreadSheet(spreadSheetEvent);
		}
	}

	public void endSpreadSheet(SpreadSheetEvent spreadSheetEvent){
		for(SpreadSheetExportEventConsumer consumer: this.consumerList){
			consumer.endSpreadSheet(spreadSheetEvent);
		}
	}

	public void startRowGroup(RowGroupEvent rowGroupEvent){
		for(SpreadSheetExportEventConsumer consumer: this.consumerList){
			consumer.startRowGroup(rowGroupEvent);
		}
	}
	
	public void endRowGroup(RowGroupEvent sourceDirectoryEvent){
		for(SpreadSheetExportEventConsumer consumer: this.consumerList){
			consumer.endRowGroup(sourceDirectoryEvent);
		}
	}
	
	public void startRow(RowEvent rowEvent){
		for(SpreadSheetExportEventConsumer consumer: this.consumerList){
			consumer.startRow(rowEvent);
		}
	}
	
	public void endRow(RowEvent rowEvent){
		for(SpreadSheetExportEventConsumer consumer: this.consumerList){
			consumer.endRow(rowEvent);
		}
	}

	public void startPage(PageEvent pageEvent){
		for(SpreadSheetExportEventConsumer consumer: this.consumerList){
			consumer.startPage(pageEvent);
		}
	}

	public void endPage(PageEvent pageEvent){
		for(SpreadSheetExportEventConsumer consumer: this.consumerList){
			consumer.endPage(pageEvent);
		}
	}

	public void startQuestion(QuestionEvent questionEvent){
		for(SpreadSheetExportEventConsumer consumer: this.consumerList){
			consumer.startQuestion(questionEvent);
		}
	}
	
	public void endQuestion(QuestionEvent questionEvent){
		for(SpreadSheetExportEventConsumer consumer: this.consumerList){
			consumer.endQuestion(questionEvent);
		}
	}

	public void startQuestionItem(QuestionItemEvent questionItemEvent){
		for(SpreadSheetExportEventConsumer consumer: this.consumerList){
			consumer.startQuestionItem(questionItemEvent);
		}
	}
	
	public void endQuestionItem(QuestionItemEvent questionItemEvent){
		for(SpreadSheetExportEventConsumer consumer: this.consumerList){
			consumer.endQuestionItem(questionItemEvent);
		}
	}
}
