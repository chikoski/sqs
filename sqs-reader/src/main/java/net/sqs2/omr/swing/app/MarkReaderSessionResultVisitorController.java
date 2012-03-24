package net.sqs2.omr.swing.app;

import java.awt.Color;
import java.util.logging.Logger;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.result.export.model.MarkAreaErrorModel;
import net.sqs2.omr.session.event.MasterEvent;
import net.sqs2.omr.session.event.PageEvent;
import net.sqs2.omr.session.event.QuestionEvent;
import net.sqs2.omr.session.event.QuestionItemEvent;
import net.sqs2.omr.session.event.ResultEvent;
import net.sqs2.omr.session.event.RowEvent;
import net.sqs2.omr.session.event.RowGroupEvent;
import net.sqs2.omr.session.event.SessionEvent;
import net.sqs2.omr.session.event.SourceDirectoryEvent;
import net.sqs2.omr.session.event.SpreadSheetEvent;
import net.sqs2.omr.session.event.SpreadSheetOutputEventReciever;

public class MarkReaderSessionResultVisitorController implements SpreadSheetOutputEventReciever{
	
	public static final Color COLOR_UPDATE_PROGRESS_BAR_FINISHED = new Color(20, 210, 20);
	public static final Color COLOR_UPDATE_PROGRESS = new Color(20, 150, 20);
	
	MarkReaderSessionResultVisitorPanel panel;
	MarkAreaErrorModel markAreaErrorModel;
	
	public MarkReaderSessionResultVisitorController(MarkReaderSessionResultVisitorPanel panel, MarkAreaErrorModel markAreaErrorModel){
		this.panel = panel;
		this.markAreaErrorModel = markAreaErrorModel;
	}
	
	@Override
	public void startMaster(MasterEvent masterEvent) {
		updateProgressValue(this.panel.getSessionExportProgressPanel().masterLabel, masterEvent, true);
	}

	@Override
	public void endMaster(MasterEvent masterEvent) {
		updateProgressValue(this.panel.getSessionExportProgressPanel().masterLabel, masterEvent, false);
	}
	
	@Override
	public void startSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent){
		updateProgressValue(this.panel.getSessionExportProgressPanel().sourceDirectoryLabel, sourceDirectoryEvent, true);
	}

	@Override
	public void endSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
		updateProgressValue(this.panel.getSessionExportProgressPanel().sourceDirectoryLabel, sourceDirectoryEvent, false);
	}
	
	@Override
	public void startRowGroup(RowGroupEvent rowGroupEvent) {
		updateProgressValue(this.panel.getSessionExportProgressPanel().rowGroupLabel, rowGroupEvent, true);
	}

	@Override
	public void endRowGroup(RowGroupEvent rowGroupEvent) {
		updateProgressValue(this.panel.getSessionExportProgressPanel().rowGroupLabel, rowGroupEvent, false);
	}

	@Override
	public void startRow(RowEvent rowEvent) {
		//this.markAreaErrorModel.setRowIndex(rowEvent.getRowIndex());
		updateProgressValue(this.panel.getSessionExportProgressPanel().rowLabel, rowEvent, true);
	}

	@Override
	public void endRow(RowEvent rowEvent) {
		updateProgressValue(this.panel.getSessionExportProgressPanel().rowLabel, rowEvent, false);
		this.panel.getSessionResultStatisticsPanel().updateValue(this.markAreaErrorModel.getTotalMarkReadStatus());
	}
	
	private void updateProgressBar(final JProgressBar progressBar, final int index, final int max, final boolean isFinished) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressBar.setIndeterminate(false);
				progressBar.setMinimum(0);
				progressBar.setMaximum(max);
				progressBar.setValue(index);
				if (isFinished) {
					progressBar.setString(Messages.EXPORT_FINISHED_LABEL+": " + index + " / " + max);
					progressBar.setForeground(COLOR_UPDATE_PROGRESS_BAR_FINISHED);
				} else {
					progressBar.setString(Messages.EXPORT_PROGRESS_LABEL+": " + index + " / " + max);
					progressBar.setForeground(COLOR_UPDATE_PROGRESS);
				}
			}
		});
	}

	private void updateProgressValue(LabelProgressPanel panel, ResultEvent event, boolean isStart) {
		boolean isFinished = !isStart && (event.getIndex()+1 == event.getNumEvents()); 
		JProgressBar progressBar = panel.getProgressBar();
		updateProgressBar(progressBar, event.getIndex() + 1, event.getNumEvents(), isFinished);
		Logger.getLogger(panel.getTitle()).info(""+(1+event.getIndex())+"/"+event.getNumEvents()+" "+isFinished);
	}

	@Override
	public void startSession(SessionEvent sessionEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endSession(SessionEvent sessionEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
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
	public void endPage(PageEvent pageEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startQuestion(QuestionEvent questionEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endQuestion(QuestionEvent questionEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startQuestionItem(QuestionItemEvent questionItemEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endQuestionItem(QuestionItemEvent questionItemEvent) {
		// TODO Auto-generated method stub
		
	}

}
