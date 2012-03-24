package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;

public class ImagePath extends SessionPath {

	public class SingleImagePath extends ImagePath {
		
		SingleRowPath singleRowPath;
		long imageIndex;
		
		public SingleImagePath(String pathInfo) {
			super(pathInfo);
			this.singleRowPath = new SingleRowPath(pathInfo);
			if(4 < paramDepth){
				this.imageIndex = URLSafeRLEBase64.decodeToLong(pathInfoArray[4]);
			}
		}
	
		public SingleImagePath(long sessionID, long masterIndex, long spreadSheetIndex, long rowIndex, long imageIndex) {
			super(sessionID);
			this.singleRowPath = new SingleRowPath(sessionID, masterIndex, spreadSheetIndex, rowIndex);
			this.imageIndex = imageIndex;
		}
	
		public SingleRowPath getSingleRowPath(){
			return this.singleRowPath;
		}
	
	
		public long getImageIndex() {
			return this.imageIndex;
		}
	
		public String toString(){
			return this.singleRowPath.toString()+"/"+URLSafeRLEBase64.encode(imageIndex);
		}
	
	
	}

	public ImagePath(String pathInfo) {
		super(pathInfo);
	}

	public ImagePath(long sessionID) {
		super(sessionID);
	}

}