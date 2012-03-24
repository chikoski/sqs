package net.sqs2.omr.result.path;

import java.io.File;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.model.FormMasterItem;
import net.sqs2.omr.result.model.QuestionItemList;

public class SingleMasterSelection extends SessionSelection{

	protected SingleMasterPath masterPath; 
	protected FormMaster formMaster;
	
	String title;
	
	public SingleMasterSelection(SingleMasterPath masterPath){
		super(masterPath);
		this.masterPath = masterPath;
		this.formMaster = (FormMaster) sessionSource.getContentIndexer().getFormMasterList().get((int)masterPath.getMasterIndex());
	}
	
	public AnswerPath getSingleMasterPath(){
		return masterPath;
	}
	public FormMaster getFormMaster() {
		return formMaster;
	}

	public String getTitle(){
		if(title == null){
			title = new File(getFormMaster().getFileResourceID().getRelativePath()).getName();
		}
		return title;
	}
	
	public FormMasterItem createMasterItem(){
		return new FormMasterItem(this.formMaster);
	}

	public QuestionItemList createQuestionItemList(){
		return new QuestionItemList(this.formMaster);		
	}
	
}
