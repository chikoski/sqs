/**
 * 
 */
package net.sqs2.omr.swing;

import java.awt.Color;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.result.event.ContentsEvent;
import net.sqs2.omr.result.event.MasterEvent;
import net.sqs2.omr.result.event.QuestionEvent;
import net.sqs2.omr.result.event.RowEvent;
import net.sqs2.omr.result.event.RowGroupEvent;
import net.sqs2.omr.result.event.SessionEvent;
import net.sqs2.omr.result.event.SourceDirectoryEvent;
import net.sqs2.omr.result.event.SpreadSheetEvent;
import net.sqs2.omr.result.export.MarkReadStatusReportModule;
import net.sqs2.omr.result.model.MarkAreaAnswer;
import net.sqs2.omr.result.model.MarkAreaAnswerItemSet;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSources;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.source.SpreadSheet;
import net.sqs2.omr.source.config.MarkRecognitionConfig;

class SessionExportModel extends MarkReadStatusReportModule {

	/**
	 * 
	 */
	
	public static final Color COLOR_UPDATE_PROGRESS_BAR_FINISHED = new Color(20, 210, 20);
	public static final Color COLOR_UPDATE_PROGRESS = new Color(20, 150, 20);
	
	private final SessionExportPanel sessionExportPanel;
	private final SessionExportProgressPanel sessionExportProgressPanel;

	private int rowIndex;
	private float densityThreshold, recognitionMargin;
	private MarkAreasTableModel noAnswerMarkAreasTableModel;
	private MarkAreasTableModel multipleAnswersMarkAreasTableModel;
	
	private int noAnswerMarkAreasSerial = 0;
	private int multipleAnswersMarkAreasSerial = 0;
	
	SessionExportModel(SessionExportPanel sessionExportPanel, MarkAreasTableModel noAnswerMarkAreasTableModel,
			MarkAreasTableModel multipleAnswersMarkAreasTableModel){
		super();
		this.sessionExportPanel = sessionExportPanel;
		this.sessionExportProgressPanel = sessionExportPanel.getSessionExportProgressPanel();
		this.noAnswerMarkAreasTableModel = noAnswerMarkAreasTableModel;
		this.multipleAnswersMarkAreasTableModel = multipleAnswersMarkAreasTableModel;
	}
	
	void reset() {
		noAnswerMarkAreasTableModel.clear();
		multipleAnswersMarkAreasTableModel.clear();
		this.noAnswerMarkAreasSerial = 0;		
		this.multipleAnswersMarkAreasSerial = 0;
	}
	
	private void updateProgressBar(final JProgressBar progressBar,		
			final int index, final int max, final boolean isFinished) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressBar.setIndeterminate(false);
				progressBar.setMinimum(0);
				progressBar.setMaximum(max);
				progressBar.setValue(index);
				if (isFinished) {
					progressBar.setString("書き出し完了: " + index + " / "
							+ max);
					progressBar.setForeground(COLOR_UPDATE_PROGRESS_BAR_FINISHED);							
				} else {
					progressBar.setString("書き出し中: " + index + " / "
							+ max);
					progressBar.setForeground(COLOR_UPDATE_PROGRESS);
				}

			}
		});
	}

	private void updateProgressValue(LabelProgressPanel panel,
			ContentsEvent event, boolean isStart) {
		boolean isFinished = !isStart
				&& event.getIndex() == event.getNumEvents() - 1;
		JProgressBar progressBar = panel.getProgressBar();
		updateProgressBar(progressBar, event.getIndex() + 1, event.getNumEvents(), isFinished);
	}

	@Override
	public void startSession(SessionEvent sessionEvent) {
		super.startSession(sessionEvent);
		reset();
		this.sessionExportPanel.reset();
		long sessionID = sessionEvent.getSessionID();
		SessionSource sessionSource = SessionSources.get(sessionID);
		//pageTaskAccessor = sessionSource.getSessionSourceContentAccessor().getPageTaskAccessor();
		for (JProgressBar progressBar : this.sessionExportProgressPanel.getProgressBarArray()) {
			if(0 < progressBar.getMaximum()){
				progressBar.setIndeterminate(true);
			}
		}
	}

	@Override
	public void endSession(SessionEvent sessionEvent) {
		super.endSession(sessionEvent);
		for (JProgressBar progressBar : this.sessionExportProgressPanel.getProgressBarArray()) {
			if(0 < progressBar.getMaximum()){
				progressBar.setIndeterminate(false);
			}
		}
	}

	@Override
	public void startMaster(MasterEvent masterEvent) {
		super.startMaster(masterEvent);
		//this.master = masterEvent.getFormMaster();
		updateProgressValue(this.sessionExportProgressPanel.masterLabel, masterEvent, true);
	}

	@Override
	public void endMaster(MasterEvent masterEvent) {
		super.endMaster(masterEvent);
		updateProgressValue(this.sessionExportProgressPanel.masterLabel, masterEvent, false);
	}

	@Override
	public void startSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
		super.startSourceDirectory(sourceDirectoryEvent);
		SourceDirectory sourceDirectory = sourceDirectoryEvent.getSourceDirectory();
		updateProgressValue(this.sessionExportProgressPanel.sourceDirectoryLabel, sourceDirectoryEvent, true);
		MarkRecognitionConfig config = sourceDirectory.getConfiguration().getConfig().getSourceConfig().getMarkRecognitionConfig();
		this.densityThreshold = config.getDensity();
		this.recognitionMargin = config.getRecognitionMargin();
	}

	@Override
	public void endSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {
		super.endSourceDirectory(sourceDirectoryEvent);
		updateProgressValue(this.sessionExportProgressPanel.sourceDirectoryLabel, sourceDirectoryEvent,	false);
	}

	@Override
	public void startSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		super.startSpreadSheet(spreadSheetEvent); // do not remove
	}

	@Override
	public void endSpreadSheet(SpreadSheetEvent spreadSheetEvent) {
		super.endSpreadSheet(spreadSheetEvent); // do not remove
	}

	@Override
	public void startRowGroup(RowGroupEvent rowGroupEvent) {
		super.startRowGroup(rowGroupEvent); // do not remove
		updateProgressValue(this.sessionExportProgressPanel.rowGroupLabel, rowGroupEvent, true);
	}

	@Override
	public void endRowGroup(RowGroupEvent rowGroupEvent) {
		super.endRowGroup(rowGroupEvent); // do not remove
		updateProgressValue(this.sessionExportProgressPanel.rowGroupLabel, rowGroupEvent, false);
	}
	
	@Override
	public void startRow(RowEvent rowEvent) {
		super.startRow(rowEvent); // do not remove
		rowIndex = rowEvent.getRowIndex();
		updateProgressValue(this.sessionExportProgressPanel.rowLabel, rowEvent, true);
	}

	@Override
	public void endRow(RowEvent rowEvent) {
		super.endRow(rowEvent); // do not remove
		updateProgressValue(this.sessionExportProgressPanel.rowLabel, rowEvent, false);
		this.sessionExportPanel.updateValue(getTotalMarkReadStatus());
	}


	@Override
	public void endQuestion(QuestionEvent questionEvent) {
		
		super.endQuestion(questionEvent); // do not remove
		
		FormArea defaultFormArea = questionEvent.getDefaultFormArea();		
		if(defaultFormArea.isMarkArea()){
			
			MarkAreaAnswer answer = (MarkAreaAnswer)questionEvent.getAnswer();
			MarkAreaAnswerItemSet answerItemSet = answer.createMarkAreaAnswerItemSet();
			int columnIndex = questionEvent.getDefaultFormArea().getColumnIndex();
			int pageStart = questionEvent.getDefaultFormArea().getPage();

			if(defaultFormArea.isSelect1()){
				
				boolean[] isMarked = answerItemSet.getIsSelectedBooleanArray(densityThreshold, recognitionMargin);
				int pageEnd = questionEvent.getFormAreaList().get(isMarked.length - 1).getPage();
			
				int numSelected = answerItemSet.getNumSelected(densityThreshold, recognitionMargin);
				if(numSelected == 1){
					return;
				}
				RowGroupEvent rowGroupEvent = questionEvent.getRowEvent().getRowGroupEvent();
				SpreadSheet spreadSheet = rowGroupEvent.getSpreadSheetEvent().getSpreadSheet();
				SourceDirectory rowGroupSourceDirectory = rowGroupEvent.getSourceDirectory();

				if(numSelected == 0){
					noAnswerMarkAreasTableModel.addRow(++noAnswerMarkAreasSerial,
							spreadSheet, rowGroupSourceDirectory,
							pageStart, pageEnd, rowIndex, columnIndex);
				}else{
					multipleAnswersMarkAreasTableModel.addRow(++multipleAnswersMarkAreasSerial,
							spreadSheet, rowGroupSourceDirectory,
							pageStart, pageEnd, rowIndex, columnIndex);
				}

			}else if(defaultFormArea.isSelect()){
		
			}else{
				throw new RuntimeException("invalid formArea type");
			}
			
		}else if(defaultFormArea.isTextArea()){
			
			/*
			List<PageAreaCommand> pageAreaCommandList = ContentsFactoryUtil.createPageAreaCommandListParQuestion(sessionSource, master, sourceDirectory,
					pageTaskAccessor, rowIndex, questionEvent.getColumnIndex());
			PageAreaCommand command = pageAreaCommandList.get(0);
			byte[] bytes = command.getImageByteArray();
			// TODO
			 * 
			 */
			// do nothing
			// do nothing
		}else{
			throw new RuntimeException("invalid formArea type");
		}

	}
}