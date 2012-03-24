package net.sqs2.omr.result.path;

public class MultiImageMultiQuestionSelection extends MultiImageSelection {

	MultiImageMultiQuestionPath multiImageMultiQuestionPath;
	MultiQuestionSelection multiQuestionSelection;
	
	public MultiImageMultiQuestionSelection(MultiImageMultiQuestionPath multiImageMultiQuestionPath) {
		super(multiImageMultiQuestionPath);
		this.multiImageMultiQuestionPath = multiImageMultiQuestionPath;
		this.multiQuestionSelection = new MultiQuestionSelection(formMaster, multiImageMultiQuestionPath.getQuestionSelection());
	}
	
	public MultiImageMultiQuestionPath getMultiImageMultiQuestionPath(){
		return (MultiImageMultiQuestionPath)multiImageMultiQuestionPath;
	}

	public MultiQuestionSelection getMultiQuestionSelection() {
		return multiQuestionSelection;
	}
	
}
