package net.sqs2.omr.model;

import java.io.Serializable;
import java.util.List;

import net.sqs2.omr.master.FormMaster;


public class SpreadSheet implements Serializable{
	public static final long serialVersionUID = 1L;
	
	private long sessionID;
	private FormMaster formMaster;
	private SourceDirectory sourceDirectory;

	protected List<PageID> pageIDList;
	
	public SpreadSheet(long sessionID, FormMaster formMaster, SourceDirectory sourceDirectory) {
		this.sessionID = sessionID;
		this.formMaster = formMaster;
		this.sourceDirectory = sourceDirectory;
	}

	public long getSessionID() {
		return this.sessionID;
	}
	
	public FormMaster getFormMaster(){
		return this.formMaster;
	}

	public SourceDirectory getSourceDirectory() {
		return this.sourceDirectory;
	}

	public int getNumRows() {
		return this.sourceDirectory.getPageIDList().size();
		/*
		if(this.pageIDList == null){
			
		}else{
			return this.pageIDList.size() / this.formMaster.getNumPages();
		}*/
	}
	
	public PageID getPageID(int index) {
		return getPageIDList().get(index);
	}

	public List<PageID> getPageIDList() {
		return this.pageIDList;
	}

	public int getNumPageIDs() {
		return getPageIDList().size();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SpreadSheet)) {
			return false;
		}
		SpreadSheet s = (SpreadSheet) o;
		return this.sourceDirectory.equals(s.getSourceDirectory()) && this.formMaster.equals(s.getFormMaster()) && this.sessionID == s.getSessionID();
	}
	
	@Override
	public int hashCode(){
		return this.sourceDirectory.hashCode() + this.formMaster.hashCode() + ((int)this.sessionID );
	}
}
