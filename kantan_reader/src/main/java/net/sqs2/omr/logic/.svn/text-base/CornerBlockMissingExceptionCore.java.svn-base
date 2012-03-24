package net.sqs2.omr.logic;

import net.sqs2.omr.swing.Messages;
import net.sqs2.omr.task.TaskExceptionCore;

public class CornerBlockMissingExceptionCore extends TaskExceptionCore {

	private static final long serialVersionUID = 0L;
	
	int type;
	
	public CornerBlockMissingExceptionCore(String pageID, int width, int height, int type){
		super(pageID, width, height);
		this.type = type;
	}
	
	public String getLocalizedMessage(){
		return Messages.SESSION_ERROR_PAGEGUIDEBLOCKMISSING;
	}
	
	@Override
	public String getDescription() {
		if(type == 0){
			return "check page header";
		}else{
			return "check page footer";
		}
		
	}

}
