package net.sqs2.omr.model;

import java.util.List;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.util.FileResourceID;

public interface ContentIndexer {

	public abstract void sortSourceDirectoryList(); 
	public abstract void clear();

	public abstract SourceDirectoryConfiguration getConfigHandler(FileResourceID fileResourceID);

	public abstract void setConfiguration(FileResourceID fileResourceID, SourceDirectoryConfiguration configHandler);

	public abstract void putFormMaster(FileResourceID fileResourceID, FormMaster master);
	public abstract List<FormMaster> getFormMasterList();
	public abstract FormMaster getFormMaster(FileResourceID fileResourceID);

	public abstract void addToSourceDirectoryFlattenList(FormMaster pageMaster, SourceDirectory sourceDirectory);	
	public abstract List<SourceDirectory> getFlattenSourceDirectoryList(FormMaster master);
	public abstract SourceDirectory getSourceDirectoryFromFlattenList(FormMaster pageMaster, int flattenIndex);
	public abstract int getNumSourceDirectoriesInFlattenList(FormMaster pageMaster);
	
	public abstract SourceDirectory getSourceDirectoryRoot(FormMaster master);

	public abstract void putRowIndex(PageID pageID, int rowIndex);
	public abstract void removeRowID(PageID pageID);
	public abstract Integer getRowIndex(PageID pageID);


}