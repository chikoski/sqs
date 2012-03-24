package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;
import net.sqs2.omr.util.URLSafeRLEBase64.MultiSelection;

public class SingleImageMultiQuestionPath extends SingleImagePath {
	protected MultiSelection questionSelection;

	public SingleImageMultiQuestionPath(String pathInfo) {
		super(pathInfo);
		if(5 < paramDepth){
			this.questionSelection = URLSafeRLEBase64.decodeToMultiSelection(pathInfoArray[5]);
		}
	}
	
	public SingleImageMultiQuestionPath(long sessionID,
			long masterIndex, long spreadSheetIndex, long rowIndex, long imageIndex, URLSafeRLEBase64.MultiSelection questionSelection) {
		super(sessionID, masterIndex, spreadSheetIndex, rowIndex, imageIndex);
		this.questionSelection = questionSelection;
	}

	public MultiSelection getQuestionSelection() {
		if(5 < paramDepth){
			return questionSelection;
		}
		throw new IllegalArgumentException();
	}

	public String toString(){
		return super.toString()+"/"+URLSafeRLEBase64.encode(questionSelection);
	}

}
