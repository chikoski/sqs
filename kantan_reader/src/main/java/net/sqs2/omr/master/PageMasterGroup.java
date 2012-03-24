package net.sqs2.omr.master;

import java.util.ArrayList;
import java.util.List;

public class PageMasterGroup {
	
	List<PageMaster> pageMasterList = null;
	
	public PageMasterGroup(){
		this.pageMasterList = new ArrayList<PageMaster>();	
	}
	
	public void addPageMaster(PageMaster pageMaster){
		pageMasterList.add(pageMaster);
	}
	
	public List<PageMaster> getPageMasterList(){
		return this.pageMasterList;
	}
}
