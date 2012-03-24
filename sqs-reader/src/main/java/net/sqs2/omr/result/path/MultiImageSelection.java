package net.sqs2.omr.result.path;

import java.util.List;

import net.sqs2.omr.model.PageID;

public class MultiImageSelection extends SingleRowSelection {

	MultiImagePath multiImagePath;
	List<PageID> pageIDList;
	
	public MultiImageSelection(MultiImagePath multiImagePath) {
		super(multiImagePath.getSingleRowPath());
		this.multiImagePath = multiImagePath;
		for(int imageIndex: multiImagePath.getImageSelection().getSelectedIndexTreeSet()){
				pageIDList.add(sourceDirectory.getPageID(imageIndex));		
		}
	}

	public MultiImagePath getMultiImagePath(){
		return this.multiImagePath;
	}
	
}
