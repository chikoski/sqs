package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;
import net.sqs2.omr.util.URLSafeRLEBase64.MultiSelection;

public class MultiImagePath extends ImagePath {
	
	SingleRowPath singleRowPath;
	MultiSelection imageSelection;
	
	public MultiImagePath(String pathInfo) {
		super(pathInfo);
		this.singleRowPath = new SingleRowPath(pathInfo);
		if(4 < paramDepth){
			this.imageSelection = URLSafeRLEBase64.decodeToMultiSelection(pathInfoArray[4]);
		}
	}

	public MultiImagePath(long sessionID, long masterIndex, long spreadSheetIndex, long rowIndex, MultiSelection imageSelection) {
		super(sessionID);
		this.singleRowPath = new SingleRowPath(sessionID, masterIndex, spreadSheetIndex, rowIndex);
		this.imageSelection = imageSelection;
	}

	public SingleRowPath getSingleRowPath(){
		return this.singleRowPath;
	}

	public MultiSelection getImageSelection() {
		return this.imageSelection;
	}

	public String toString(){
		return this.singleRowPath.toString()+"/"+URLSafeRLEBase64.encode(imageSelection);
	}


}
