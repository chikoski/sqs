package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;
import net.sqs2.omr.util.URLSafeRLEBase64.MultiSelection;

public class MultiImageMultiQuestionPath extends MultiImagePath {
	protected MultiSelection questionSelection;

	public MultiImageMultiQuestionPath(String pathInfo) {
		super(pathInfo);
		if(5 < paramDepth){
			this.questionSelection = URLSafeRLEBase64.decodeToMultiSelection(pathInfoArray[5]);
		}
	}
	
	public MultiImageMultiQuestionPath(long sessionID,
			long masterIndex, long spreadSheetIndex, long rowIndex, MultiSelection imageSelection, MultiSelection questionSelection) {
		super(sessionID, masterIndex, spreadSheetIndex, rowIndex, imageSelection);
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
