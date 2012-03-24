package net.sqs2.omr.result.tree;

import net.sqs2.omr.result.tree.PathInfoParser.FormMasterListPathItem;

public class FormMasterListPathInfoParser extends SessionPathInfoParser {

	public FormMasterListPathInfoParser(String pathInfo){
		super(pathInfo);
	}
	
	protected FormMasterListPathItem parse(){
		return new FormMasterListPathItem(pathInfo, sessionID);
	}
}

