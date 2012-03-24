package net.sqs2.omr.result.export;

import java.io.File;
import java.io.IOException;

import net.sqs2.omr.app.App;
import net.sqs2.omr.result.event.MasterEvent;
import net.sqs2.omr.result.event.SessionEvent;
import net.sqs2.omr.result.event.SourceDirectoryEvent;
import net.sqs2.omr.result.event.SpreadSheetEvent;
import net.sqs2.util.FileUtil;

public class SpreadSheetExportModule extends SpreadSheetExportEventAdapter {

	public SpreadSheetExportModule() {
		super();
	}

	@Override
	public void startSession(SessionEvent sessionEvent) {
	}

	@Override
	public void startMaster(MasterEvent masterEvent) {
	}

	@Override
	public void startSourceDirectory(SourceDirectoryEvent sourcedirectoryEvent) {
	}

	@Override
	public void endSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent) {		
	}

	public static String createSpreadSheetFileName(SpreadSheetEvent spreadSheetEvent, String suffix) throws IOException {
		String masterName = new File(spreadSheetEvent.getFormMaster().getFileResourceID().getRelativePath()).getName();
		File masterFile = new File(new File(spreadSheetEvent.getSpreadSheet().getSourceDirectory().getDirectory(),
				App.getResultDirectoryName()), masterName);
		return FileUtil.getSuffixReplacedFilePath(masterFile, suffix);
	}

}