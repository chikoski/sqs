package net.sqs2.omr.result.path;

public class SingleImageMultiQuestionSelection extends SingleImageSelection {

	SingleImageMultiQuestionPath singleImageMultiQuestionPath;
	MultiQuestionSelection multiQuestionSelection;
	
	public SingleImageMultiQuestionSelection(SingleImageMultiQuestionPath singleImageMultiQuestionPath) {
		super(singleImageMultiQuestionPath);
		this.singleImageMultiQuestionPath = singleImageMultiQuestionPath;
		this.multiQuestionSelection = new MultiQuestionSelection(formMaster, singleImageMultiQuestionPath.getQuestionSelection());
	}
	
	public SingleImageMultiQuestionPath getSingleImageMultiQuestionPath(){
		return (SingleImageMultiQuestionPath)singleImageMultiQuestionPath;
	}

	public MultiQuestionSelection getMultiQuestionSelection() {
		return multiQuestionSelection;
	}
	
	
}
