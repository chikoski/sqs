package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;

/**
 * This class is used in ExportSpreadSheetServlet
 * @author hiroya
 *
 */
public class SingleSpreadSheetPath extends SpreadSheetPath {

	protected long spreadSheetIndex;

	public SingleSpreadSheetPath(String pathInfo) {
		super(pathInfo);
		if(2 < paramDepth){
			this.spreadSheetIndex = URLSafeRLEBase64.decodeToInt(pathInfoArray[2]);
		}
	}
	
	public SingleSpreadSheetPath(long sessionID, long masterIndex, long spreadSheetIndex) {
		super(sessionID, masterIndex);
		this.spreadSheetIndex = spreadSheetIndex;
	}


	public long getFlattenSourceDirectoryIndex() {
		if(2 < paramDepth){
			return this.spreadSheetIndex;
		}
		throw new IllegalArgumentException();
	}

	public String toString(){
		return super.toString()+"/"+URLSafeRLEBase64.encode(spreadSheetIndex);
	}

}