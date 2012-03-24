package net.sqs2.omr.result.path;

public class SpreadSheetPath extends SingleMasterPath {

	public SpreadSheetPath(long sessionID, long masterIndex) {
		super(sessionID, masterIndex);
	}

	public SpreadSheetPath(String pathInfo) {
		super(pathInfo);
	}

}
