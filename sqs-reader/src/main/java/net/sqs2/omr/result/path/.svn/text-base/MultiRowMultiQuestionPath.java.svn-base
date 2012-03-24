package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;

/**
 * This class is used in ChartImageServlet(StackedBarChart), CrossTabularTableServlet
 * @author hiroya
 *
 */
public class MultiRowMultiQuestionPath extends MultiRowPath {

	protected URLSafeRLEBase64.MultiSelection questionSelection = null;

	public MultiRowMultiQuestionPath(String pathInfo) {
		super(pathInfo);
		try{
			if(4 < paramDepth){
				this.questionSelection = URLSafeRLEBase64.decodeToMultiSelection(pathInfoArray[4]);
			}
		}catch(NumberFormatException ignore){
			paramDepth = -1;
		}
	}
	
	public MultiRowMultiQuestionPath(long sessionID,
			int masterIndex, URLSafeRLEBase64.MultiSelection spreadSheetSelection, URLSafeRLEBase64.MultiSelection rowSelection, URLSafeRLEBase64.MultiSelection questionSelection) {
		super(sessionID, masterIndex, spreadSheetSelection, rowSelection);
		this.questionSelection = questionSelection;
	}

	public URLSafeRLEBase64.MultiSelection getQuestionSelection() {
		if(4 < paramDepth){
			return questionSelection;
		}
		throw new IllegalArgumentException();
	}
	
	public String toString(){
		return super.toString()+"/"+URLSafeRLEBase64.encode(questionSelection);
	}


}