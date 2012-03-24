package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;

/**
 * This class is used in EditAnswerServlet(EditTextArea)
 * @author hiroya
 *
 */
public class SingleRowSingleQuestionPath extends SingleRowPath {

	protected int questionIndex;

	public SingleRowSingleQuestionPath(String pathInfo) {
		super(pathInfo);
		if(4 < paramDepth){
			this.questionIndex = URLSafeRLEBase64.decodeToInt(pathInfoArray[4]);
		}
	}

	public SingleRowSingleQuestionPath(long sessionID, int masterIndex, int spreadSheetIndex, int rowIndex, int questionIndex) {
		super(sessionID, masterIndex, spreadSheetIndex, rowIndex);
		this.questionIndex = questionIndex;
	}

	public int getQuestionIndex() {
		if(4 < paramDepth){
			return this.questionIndex;
		}
		throw new IllegalArgumentException();
	}

	public String toString(){
		return super.toString()+"/"+URLSafeRLEBase64.encode(questionIndex);
	}

}