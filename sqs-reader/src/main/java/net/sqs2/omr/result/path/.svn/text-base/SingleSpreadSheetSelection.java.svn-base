package net.sqs2.omr.result.path;

import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.SpreadSheet;
import net.sqs2.omr.result.model.SpreadSheetItem;

public class SingleSpreadSheetSelection extends SingleMasterSelection{

	protected SourceDirectory sourceDirectory;
	protected SpreadSheet spreadSheet;
	
	public SingleSpreadSheetSelection(SingleSpreadSheetPath singleSpreadSheetPath) {
		super(singleSpreadSheetPath);
		sourceDirectory = sessionSource.getContentIndexer().getFlattenSourceDirectoryList(formMaster).get((int)singleSpreadSheetPath.getFlattenSourceDirectoryIndex());
		spreadSheet = new SpreadSheet(sessionSource.getSessionID(), formMaster, sourceDirectory);
	}

	public SingleSpreadSheetPath getSingleSpreadSheetPath(){
		return (SingleSpreadSheetPath)masterPath;
	}

	public SourceDirectory getSourceDirectory() {
		return sourceDirectory;
	}

	public SpreadSheet getSpreadSheet() {
		return spreadSheet;
	}
	
	public String getTitle(){
		if(title == null){
			return title = sourceDirectory.getDirectory().getName();
		}else{
			return title;
		}
	}

	public SpreadSheetItem createSpreadSheetItem(){
		return new SpreadSheetItem(sourceDirectory);
	}

}
