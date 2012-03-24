package net.sqs2.omr.result.event;

public class SessionSourceEvent extends ContentsEvent {
	private long sessionID;
	public SessionSourceEvent(long sessionID){
		this.sessionID = sessionID;
	}
	
	public long getSessionID(){
		return this.sessionID;
	}
}
