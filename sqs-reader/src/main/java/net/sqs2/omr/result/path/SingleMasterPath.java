package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;

public class SingleMasterPath extends MasterPath {

	protected long masterIndex;

	public SingleMasterPath(long sessionID, long masterIndex){
		super(sessionID);
		this.masterIndex = masterIndex;
	}

	public SingleMasterPath(String pathInfo){
		super(pathInfo);
		if(1 < paramDepth){
			this.masterIndex = URLSafeRLEBase64.decodeToLong(pathInfoArray[0]);
		}
	}
	
	public long getMasterIndex() {
		if(1 < getParamDepth()){
			return this.masterIndex;	
		}
		throw new IllegalArgumentException();
	}
	

	public String toString() {
		return super.toString()+"/"+URLSafeRLEBase64.encode(masterIndex);
	}

}