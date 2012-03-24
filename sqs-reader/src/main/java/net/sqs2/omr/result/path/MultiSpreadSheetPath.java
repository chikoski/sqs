package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;

public class MultiSpreadSheetPath extends SpreadSheetPath {

	protected URLSafeRLEBase64.MultiSelection spreadSheetSelection = null;

	public MultiSpreadSheetPath(String pathInfo) {
		super(pathInfo);
		try{
			if(2 < paramDepth){
				this.spreadSheetSelection = URLSafeRLEBase64.decodeToMultiSelection(pathInfoArray[2]);
			}
		}catch(NumberFormatException ignore){
			paramDepth = -1;
		}
	}

	public MultiSpreadSheetPath(long sessionID, int masterIndex, URLSafeRLEBase64.MultiSelection spreadSheetSelection) {
		super(sessionID, masterIndex);
		this.spreadSheetSelection = spreadSheetSelection;
	}

	public URLSafeRLEBase64.MultiSelection getFlattenSourceDirectorySelection() {
		if(2 < paramDepth){
			return spreadSheetSelection;
		}
		throw new IllegalArgumentException();
	}

	public String toString(){
		return super.toString()+"/"+URLSafeRLEBase64.encode(spreadSheetSelection);
	}
}