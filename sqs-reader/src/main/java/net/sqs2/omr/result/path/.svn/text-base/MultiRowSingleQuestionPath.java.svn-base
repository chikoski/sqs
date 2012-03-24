package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;

/**
 * This class is used in ChartImageServlet(SimpleBarChart or SimplePieChart)
 * @author hiroya
 *
 */
public class MultiRowSingleQuestionPath extends MultiRowPath {

	protected int questionIndex;

	public MultiRowSingleQuestionPath(String pathInfo) {
		super(pathInfo);
		try{
			if(4 < paramDepth){
				this.questionIndex = Integer.parseInt(pathInfoArray[4]);
			}
		}catch(NumberFormatException ignore){
			paramDepth = -1;
		}
	}
	
	public MultiRowSingleQuestionPath(long sessionID,
			int masterIndex, URLSafeRLEBase64.MultiSelection spreadSheetSelection, URLSafeRLEBase64.MultiSelection rowSelection, int questionIndex) {
		super(sessionID, masterIndex, spreadSheetSelection, rowSelection);
		this.questionIndex = questionIndex;
	}

	public int getQuestionIndex() {
		if(4 < paramDepth){
			return questionIndex;
		}
		throw new IllegalArgumentException();
	}

	public String toString(){
		return super.toString()+"/"+questionIndex;
	}

}