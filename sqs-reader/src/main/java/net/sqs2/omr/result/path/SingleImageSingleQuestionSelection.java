package net.sqs2.omr.result.path;

public class SingleImageSingleQuestionSelection extends SingleImageSelection {

	SingleImageSingleQuestionPath singleImageSingleQuestionPath;

	int questionIndex;
	
	public SingleImageSingleQuestionSelection(SingleImageSingleQuestionPath singleImageSingleQuestionPath) {
		super(singleImageSingleQuestionPath);
		this.singleImageSingleQuestionPath = singleImageSingleQuestionPath;
	}
	
	public SingleImageSingleQuestionPath getSingleImageSingleQuestionPath(){
		return (SingleImageSingleQuestionPath)singleImageSingleQuestionPath;
	}
}
