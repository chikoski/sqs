package net.sqs2.omr.result.path;

import java.util.LinkedList;
import java.util.List;

import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.result.model.SpreadSheetItem;
import net.sqs2.omr.result.model.SpreadSheetItemList;

public class MultiSpreadSheetSelection extends SingleMasterSelection {
	LinkedList<SourceDirectory> flattenSelectedSourceDirectoryList = new LinkedList<SourceDirectory>();
	
	public MultiSpreadSheetSelection(MultiSpreadSheetPath multiSpreadSheetPath) {
		super(multiSpreadSheetPath);
		List<SourceDirectory> flattenSourceDirectoryList = sessionSource.getContentIndexer().getFlattenSourceDirectoryList(formMaster); 
		for(int flattenSourceDirectoryIndex: multiSpreadSheetPath.getFlattenSourceDirectorySelection().getSelectedIndexTreeSet()){
			flattenSelectedSourceDirectoryList.add(flattenSourceDirectoryList.get(flattenSourceDirectoryIndex));
		}
	}
	
	public MultiSpreadSheetPath getMultiSpreadSheetPath(){
		return (MultiSpreadSheetPath)masterPath;
	}

	public List<SourceDirectory> getFlattenSelectedSourceDirectoryList() {
		return flattenSelectedSourceDirectoryList;
	}
	
	public String getTitle(){
		if(title == null){
			if(flattenSelectedSourceDirectoryList.size() == 1){
				return title = flattenSelectedSourceDirectoryList.get(0).getDirectory().getName();
			}else{
				return super.getTitle();
			}
		}else{
			return title;
		}
	}

	public SpreadSheetItemList createFlattenSelectedSpreadSheetItemList(){
		SpreadSheetItemList spreadSheetItemList = new SpreadSheetItemList();
		for(SourceDirectory sourceDirectory: flattenSelectedSourceDirectoryList){
			spreadSheetItemList.add(new SpreadSheetItem(sourceDirectory));	
		}
		return spreadSheetItemList;
	}
}
