/**
 * 
 */
package net.sqs2.omr.result.export;

import java.io.File;

import net.sqs2.omr.app.App;
import net.sqs2.omr.result.event.PageEvent;
import net.sqs2.omr.source.SourceDirectory;

class AreaImageFileExportModule{
	
	AreaImageFileExportModule(){
	}

	protected boolean isValidOldFileIs(PageEvent pageEvent, File textareaImageFile) {
		if(textareaImageFile.exists()){
			long textareaImageFileLastModifiled = textareaImageFile.lastModified();
			long targetSourceLastModifiled = pageEvent.getPageTask().getPageID().getFileResourceID().getLastModified();
			long configFileLastModifiled = pageEvent.getPageTask().getConfigHandlerFileResourceID().getLastModified();
			long masterFileLastModifiled = pageEvent.getFormMaster().getLastModified();
		
			if(targetSourceLastModifiled < textareaImageFileLastModifiled &&
					configFileLastModifiled < textareaImageFileLastModifiled &&
					masterFileLastModifiled < textareaImageFileLastModifiled ){
				return true;
			}
		}
		return false;
	}

	protected File createResultDirectory(SourceDirectory sourceDirectory, String dirname){
		File sourceDirectoryFile = new File(sourceDirectory.getRoot(), sourceDirectory.getPath());
		String targetPath = sourceDirectoryFile.getAbsolutePath() + File.separatorChar + App.getResultDirectoryName() + File.separatorChar + dirname;
		File targetDirectory = new File(targetPath);	
		if(! targetDirectory.isDirectory()){
			targetDirectory.mkdirs();
		}
		return targetDirectory;
	}
	
	protected File createResultDirectory(SourceDirectory sourceDirectory, String dirname, String subdirname){
		File targetDirectory = new File(createResultDirectory(sourceDirectory, dirname), subdirname);	
		if(! targetDirectory.isDirectory()){
			targetDirectory.mkdirs();
		}
		return targetDirectory;
	}
}