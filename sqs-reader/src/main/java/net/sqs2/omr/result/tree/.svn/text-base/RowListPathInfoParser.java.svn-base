package net.sqs2.omr.result.tree;

import java.util.ArrayList;
import java.util.List;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.result.tree.PathInfoParser.RowListPathItem;
import net.sqs2.omr.util.URLSafeRLEBase64;
import net.sqs2.omr.util.URLSafeRLEBase64.MultiSelection;

public class RowListPathInfoParser extends FormMasterListPathInfoParser {
	
	FormMaster formMaster;
	MultiSelection sourceDirectorySelection;
	
	public RowListPathInfoParser(String pathInfo){
		super(pathInfo);
		int formMasterIndex = URLSafeRLEBase64.decodeToInt(pathInfoArray[2]);
		List<FormMaster> formMasterList = sessionSource.getContentIndexer().getFormMasterList(); 
		formMaster = formMasterList.get(formMasterIndex);
		if(paramDepth == 4){
			sourceDirectorySelection = URLSafeRLEBase64.decodeToMultiSelection(pathInfoArray[3]);
		}else{
			throw new IllegalArgumentException(pathInfo);
		}
	}
	
	protected RowListPathItem parse(){
		List<SourceDirectory> sourceDirectoryList = new ArrayList<SourceDirectory>();
		List<SourceDirectory> flattenSourceDirectoryList = sessionSource.getContentIndexer().getFlattenSourceDirectoryList(formMaster);
		for(int sourceDirectoryIndex:sourceDirectorySelection.getSelectedIndexTreeSet()){
			sourceDirectoryList.add(flattenSourceDirectoryList.get(sourceDirectoryIndex));
		}
		return new RowListPathItem(pathInfo, sessionID, formMaster, sourceDirectoryList);
	}
}
