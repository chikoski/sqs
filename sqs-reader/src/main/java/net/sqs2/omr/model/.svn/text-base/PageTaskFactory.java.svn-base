package net.sqs2.omr.model;

import net.sqs2.util.FileResourceID;

public class PageTaskFactory {
	public static OMRPageTask createPageTask(SourceDirectory sourceDirectory, int pageNumber, PageID pageID, long sessionID) {
		try {
			
			FileResourceID configFileResourceID = sourceDirectory.getConfiguration().getConfigFileResourceID();
			FileResourceID defaultFormMasterFileResourceID = sourceDirectory.getDefaultFormMaster().getFileResourceID();
			
			return new OMRPageTask(sourceDirectory.getDirectory().getAbsolutePath(), 
					configFileResourceID, defaultFormMasterFileResourceID, pageNumber,
					pageID, sessionID);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
