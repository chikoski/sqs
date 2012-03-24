package net.sqs2.omr.result.path;

import net.sqs2.omr.model.PageID;

public class SingleImageSelection extends SingleRowSelection {

	SingleImagePath singleImagePath;
	PageID pageID;
	
	public SingleImageSelection(SingleImagePath singleImagePath) {
		super(singleImagePath.getSingleRowPath());
		this.singleImagePath = singleImagePath;
		pageID = sourceDirectory.getPageID((int)singleImagePath.getSingleRowPath().getRowIndex());
	}

	public SingleImagePath getSingleImagePath(){
		return this.singleImagePath;
	}
	
}
