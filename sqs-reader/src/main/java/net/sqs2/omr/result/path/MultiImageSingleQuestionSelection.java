package net.sqs2.omr.result.path;

public class MultiImageSingleQuestionSelection extends MultiImageSelection {

	MultiImageSingleQuestionPath multiImageSingleQuestionPath;
	long questionIndex;
	
	public MultiImageSingleQuestionSelection(MultiImageSingleQuestionPath multiImageSingleQuestionPath) {
		
		super(multiImageSingleQuestionPath);
		this.multiImageSingleQuestionPath = multiImageSingleQuestionPath;
		this.questionIndex = multiImageSingleQuestionPath.getQuestionIndex();
	}
	
	public MultiImageSingleQuestionPath getMultiImageSingleQuestionPath(){
		return (MultiImageSingleQuestionPath)multiImageSingleQuestionPath;
	}

	public long getQuestionIndex() {
		return questionIndex;
	}
	
}
