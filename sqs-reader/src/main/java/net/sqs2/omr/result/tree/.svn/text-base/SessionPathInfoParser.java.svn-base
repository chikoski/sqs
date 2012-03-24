package net.sqs2.omr.result.tree;

import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SessionSources;
import net.sqs2.omr.util.URLSafeRLEBase64;

public class SessionPathInfoParser {
	String pathInfo;
	String[] pathInfoArray = null;
	int paramDepth = 0;
	long sessionID;
	SessionSource sessionSource;
	
	public SessionPathInfoParser(String pathInfo){
		this.pathInfo = pathInfo;
		this.pathInfoArray = pathInfo.substring(1).split("/");
		this.paramDepth = pathInfoArray.length;

		if(this.paramDepth <= 0){
			new IllegalArgumentException(pathInfo);	
		}
		
		this.sessionID = URLSafeRLEBase64.decodeToLong(pathInfoArray[1]);
		this.sessionSource = SessionSources.getInitializedInstance(sessionID);

	}
}
