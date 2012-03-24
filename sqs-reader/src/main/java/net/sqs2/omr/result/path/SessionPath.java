package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;

public class SessionPath {

	protected int paramDepth;
	protected String[] pathInfoArray;
	protected long sessionID;
	
	public SessionPath(String pathInfo) {
		pathInfoArray = pathInfo.split("/");
		paramDepth = pathInfoArray.length;
		if(0 < paramDepth){
			this.sessionID = URLSafeRLEBase64.decodeToLong(pathInfoArray[0]);
		}
	}

	public SessionPath(long sessionID) {
		this.sessionID = sessionID;
	}

	public int getParamDepth() {
		return paramDepth;
	}

	public long getSessionID() {
		if(0 < paramDepth){
			return this.sessionID;
		}
		throw new IllegalArgumentException();
	}

	public String toString(){
		return "/"+URLSafeRLEBase64.encode(sessionID);
	}
}
