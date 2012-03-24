package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;
import net.sqs2.omr.util.URLSafeRLEBase64.MultiSelection;

public class MultiImageSingleQuestionPath extends MultiImagePath {
	protected long questionIndex = 0;

	public MultiImageSingleQuestionPath(String pathInfo) {
		super(pathInfo);
		if(5 < paramDepth){
			this.questionIndex = URLSafeRLEBase64.decodeToInt(pathInfoArray[5]);
		}
	}
	
	public MultiImageSingleQuestionPath(long sessionID,
			long masterIndex, long spreadSheetIndex, long rowIndex, MultiSelection imageSelection, long questionIndex) {
		super(sessionID, masterIndex, spreadSheetIndex, rowIndex, imageSelection);
		this.questionIndex = questionIndex;
	}

	public long getQuestionIndex() {
		if(5 < paramDepth){
			return questionIndex;
		}
		throw new IllegalArgumentException();
	}

	public String toString(){
		return super.toString()+"/"+URLSafeRLEBase64.encode(questionIndex);
	}

}
