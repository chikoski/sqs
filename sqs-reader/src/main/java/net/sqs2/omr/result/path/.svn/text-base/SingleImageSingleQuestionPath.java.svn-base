package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;

public class SingleImageSingleQuestionPath extends SingleImagePath {
	protected long questionIndex = 0;

	public SingleImageSingleQuestionPath(String pathInfo) {
		super(pathInfo);
		if(5 < paramDepth){
			this.questionIndex = URLSafeRLEBase64.decodeToInt(pathInfoArray[5]);
		}
	}
	
	public SingleImageSingleQuestionPath(long sessionID,
			long masterIndex, long spreadSheetIndex, long rowIndex, long imageIndex, long questionIndex) {
		super(sessionID, masterIndex, spreadSheetIndex, rowIndex, imageIndex);
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
