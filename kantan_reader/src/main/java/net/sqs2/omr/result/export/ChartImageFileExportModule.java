/**
 * 
 */
package net.sqs2.omr.result.export;

import java.io.File;

import net.sqs2.omr.app.MarkReaderConstants;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.source.SourceDirectory;

class ChartImageFileExportModule extends AreaImageFileExportModule{
	
	File createChartDirectoryFile(SourceDirectory sourceDirectory){
		return createResultDirectory(sourceDirectory, "CHART");
	}
	
	private File createChartDirectoryRowDirectory(SourceDirectory sourceDirectory, FormArea formArea){
		File directory = new File(createChartDirectoryFile(sourceDirectory), Integer.toString(formArea.getColumnIndex()));
		if(! directory.exists()){
			directory.mkdirs();
		}
		return directory;
	}
	
	File createChartImageFile(SourceDirectory sourceDirectory, FormArea formArea, String type){
		return new File(createChartDirectoryRowDirectory(sourceDirectory, formArea), type+"."+MarkReaderConstants.FORMAREA_IMAGE_FORMAT);
	}
}
