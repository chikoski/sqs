package net.sqs2.omr.result.export;

import java.util.logging.Logger;

import net.sqs2.omr.result.event.MasterEvent;
import net.sqs2.omr.result.event.PageEvent;
import net.sqs2.omr.result.event.QuestionEvent;
import net.sqs2.omr.result.event.QuestionItemEvent;
import net.sqs2.omr.result.event.RowEvent;
import net.sqs2.omr.result.event.RowGroupEvent;
import net.sqs2.omr.result.event.SessionEvent;
import net.sqs2.omr.result.event.SourceDirectoryEvent;
import net.sqs2.omr.result.event.SpreadSheetEvent;

public class DummyExportModule extends SpreadSheetExportEventAdapter {

	@Override
	public void startSession(SessionEvent sessionEvent) {
		// TODO Auto-generated method stub
		super.startSession(sessionEvent);
	}
	@Override
	public void endSession(SessionEvent sessionEvent) {
		// TODO Auto-generated method stub
		super.endSession(sessionEvent);
	}

	@Override
	public void startMaster(MasterEvent masterEvent) {
		// TODO Auto-generated method stub
		super.startMaster(masterEvent);
	}
	@Override
	public void endMaster(MasterEvent masterEvent) {
		// TODO Auto-generated method stub
		super.endMaster(masterEvent);
	}


	@Override
	public void endSourceDirectory(SourceDirectoryEvent spreadSheetEvent) {
		// TODO Auto-generated method stub
		super.endSourceDirectory(spreadSheetEvent);
	}
	@Override
	public void startSourceDirectory(SourceDirectoryEvent spreadSheetEvent) {
		// TODO Auto-generated method stub
		super.startSourceDirectory(spreadSheetEvent);
	}
	
	
	@Override
	public void startSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		// TODO Auto-generated method stub
		super.startSpreadSheet(spreadSheetEvent);
		Logger.getAnonymousLogger().info("SpreadSheet:"+spreadSheetEvent.getSourceDirectoryEvent().getSourceDirectory().getPath());
	}
	@Override
	public void endSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		// TODO Auto-generated method stub
		super.endSpreadSheet(spreadSheetEvent);
	}

	
	@Override
	public void startRowGroup(RowGroupEvent rowGroupEvent) {
		// TODO Auto-generated method stub
		super.startRowGroup(rowGroupEvent);
		Logger.getAnonymousLogger().info("RowGroup:"+rowGroupEvent.getSourceDirectory().getPath());
	}
	@Override
	public void endRowGroup(RowGroupEvent rowGroupEvent) {
		// TODO Auto-generated method stub
		super.endRowGroup(rowGroupEvent);
	}
	
	
	@Override
	public void startRow(RowEvent rowEvent) {
		// TODO Auto-generated method stub
		super.startRow(rowEvent);
		Logger.getAnonymousLogger().info("Row:"+rowEvent.getRowIndex());
	}
	@Override
	public void endRow(RowEvent rowEvent) {
		// TODO Auto-generated method stub
		super.endRow(rowEvent);
	}
	

	@Override
	public void startPage(PageEvent pageEvent) {
		// TODO Auto-generated method stub
		super.startPage(pageEvent);
		Logger.getAnonymousLogger().info("Page:"+pageEvent.getPageIndex());
	}
	@Override
	public void endPage(PageEvent pageEvent) {
		// TODO Auto-generated method stub
		super.endPage(pageEvent);
	}
	
	
	@Override
	public void startQuestion(QuestionEvent questionEvent) {
		// TODO Auto-generated method stub
		super.startQuestion(questionEvent);
		Logger.getAnonymousLogger().info("Q:"+questionEvent.getColumnIndex()+" "+questionEvent.getDefaultFormArea());
	}
	@Override
	public void endQuestion(QuestionEvent questionEvent) {
		// TODO Auto-generated method stub
		super.endQuestion(questionEvent);
	}
	
	@Override
	public void startQuestionItem(QuestionItemEvent questionItemEvent) {
		// TODO Auto-generated method stub
		super.startQuestionItem(questionItemEvent);
		//Logger.getAnonymousLogger().info("   item:"+questionItemEvent.getPageIndex());
	}
	@Override
	public void endQuestionItem(QuestionItemEvent questionItemEvent) {
		// TODO Auto-generated method stub
		super.endQuestionItem(questionItemEvent);
	}

}
