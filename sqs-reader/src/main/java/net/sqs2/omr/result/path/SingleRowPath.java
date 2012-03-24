package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;

public class SingleRowPath extends SingleSpreadSheetPath {

	protected long rowIndex;

	public SingleRowPath(String pathInfo) {
		super(pathInfo);
		if(3 < paramDepth){
			this.rowIndex = URLSafeRLEBase64.decodeToInt(pathInfoArray[3]);
		}
	}

	public SingleRowPath(long sessionID, long masterIndex, long spreadSheetIndex, long rowIndex) {
		super(sessionID, masterIndex, spreadSheetIndex);
		this.rowIndex = rowIndex;
	}
	
	public long getRowIndex() {
		if(3 < paramDepth){
			return this.rowIndex;
		}
		throw new IllegalArgumentException();
	}
	
	public String toString(){
		return super.toString()+"/"+URLSafeRLEBase64.encode(rowIndex);
	}

}