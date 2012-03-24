package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;

public class MultiRowPath extends MultiSpreadSheetPath {

	protected URLSafeRLEBase64.MultiSelection rowSelection = null;

	public MultiRowPath(String pathInfo) {
		super(pathInfo);
		try{
			if(3 < paramDepth){
				this.rowSelection = URLSafeRLEBase64.decodeToMultiSelection(pathInfoArray[3]);
			}
		}catch(NumberFormatException ignore){
			paramDepth = -1;
		}
	}
	
	public MultiRowPath(long sessionID, int masterIndex, URLSafeRLEBase64.MultiSelection spreadSheetSelection, URLSafeRLEBase64.MultiSelection rowSelection) {
		super(sessionID, masterIndex, spreadSheetSelection);
		this.rowSelection = rowSelection;
	}

	public URLSafeRLEBase64.MultiSelection getRowSelection() {
		if(3 < paramDepth){
			return rowSelection;
		}
		throw new IllegalArgumentException();
	}

	public String toString(){
		return super.toString()+"/"+URLSafeRLEBase64.encode(rowSelection);
	}


}