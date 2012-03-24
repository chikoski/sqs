/**
 * 
 */
package net.sqs2.omr.session.service;

import java.util.ArrayList;
import java.util.List;

import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.model.MarkReaderConfiguration;
import net.sqs2.omr.result.export.chart.ChartExportModule;
import net.sqs2.omr.result.export.html.HTMLReportExportModule;
import net.sqs2.omr.result.export.spreadsheet.CSVExportModule;
import net.sqs2.omr.result.export.spreadsheet.ExcelExportModule;
import net.sqs2.omr.result.export.textarea.TextAreaExportModule;
import net.sqs2.omr.session.event.SpreadSheetOutputEventReciever;
import net.sqs2.omr.session.output.DummyExportModule;

public class OutputEventRecieversFactory{
	
	public enum Mode{BASE, GUI, DEBUG}
	
	protected Mode mode = null;
	
	public OutputEventRecieversFactory(Mode _mode){
		this.mode = _mode;
	}
	
	public List<SpreadSheetOutputEventReciever> create() {
		if(mode == null){
			throw new RuntimeException("Illegal State: mode = null");
		}
		switch(mode){
		case BASE:
			return createBaseExportModules();
		case GUI:
			return createUserSpecifiedExportModules();
		case DEBUG:
			return createDebugModeExportModules();
		default:
			throw new RuntimeException("Illegal State: mode = null");
		}
	}
	
	protected static List<SpreadSheetOutputEventReciever> createBaseExportModules() {
		List<SpreadSheetOutputEventReciever> recievers = new ArrayList<SpreadSheetOutputEventReciever>();
		recievers.add(new CSVExportModule());
		recievers.add(new ExcelExportModule());
		recievers.add(new HTMLReportExportModule(AppConstants.SKIN_ID));
		recievers.add(new ChartExportModule(AppConstants.SKIN_ID));
		recievers.add(new TextAreaExportModule(AppConstants.SKIN_ID));
		return recievers;
	}
	
	protected static List<SpreadSheetOutputEventReciever> createDebugModeExportModules() {
		List<SpreadSheetOutputEventReciever> recievers = new ArrayList<SpreadSheetOutputEventReciever>();
		recievers.add(new DummyExportModule());
		return recievers; 
	}

	protected static List<SpreadSheetOutputEventReciever> createUserSpecifiedExportModules() {
		List<SpreadSheetOutputEventReciever> recievers = new ArrayList<SpreadSheetOutputEventReciever>();
		if ( MarkReaderConfiguration.isEnabled(MarkReaderConfiguration.KEY_SPREADSHEET)) {
			recievers.add(new CSVExportModule());
			recievers.add(new ExcelExportModule());
		}
		if ( MarkReaderConfiguration.isEnabled(MarkReaderConfiguration.KEY_TEXTAREA)
				|| MarkReaderConfiguration.isEnabled(MarkReaderConfiguration.KEY_CHART)) {
			recievers.add(new HTMLReportExportModule(AppConstants.SKIN_ID));
		
			if ( MarkReaderConfiguration.isEnabled(MarkReaderConfiguration.KEY_CHART)) {
				//recievers.add(new OutputModule());
				recievers.add(new ChartExportModule(AppConstants.SKIN_ID));
			}
			if ( MarkReaderConfiguration.isEnabled(MarkReaderConfiguration.KEY_TEXTAREA)) {
				recievers.add(new TextAreaExportModule(AppConstants.SKIN_ID));
			}
		}
		return recievers; 
	}

}