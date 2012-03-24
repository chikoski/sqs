package net.sqs2.omr.result.path;

import java.util.ArrayList;
import java.util.List;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SessionSources;
import net.sqs2.omr.result.model.FormMasterItem;

public class SessionSelection {
	protected SessionSource sessionSource;
	public SessionSelection(SessionPath sessionPath){
		this.sessionSource = SessionSources.getInitializedInstance(sessionPath.getSessionID());
	}

	public SessionSource getSessionSource() {
		return sessionSource;
	}

	public List<FormMasterItem> createFormMasterItemList(){
		List<FormMasterItem> formMasterItemList = new ArrayList<FormMasterItem>(); 
		for(FormMaster formMaster: sessionSource.getContentIndexer().getFormMasterList()){
			formMasterItemList.add(new FormMasterItem(formMaster));	
		}
		return formMasterItemList;
	}

}
