package net.sqs2.omr.logic;

import java.io.Serializable;

import net.sqs2.omr.task.TaskException;
import net.sqs2.omr.task.TaskExceptionCore;

public class PageSourceException extends TaskException implements Serializable{
	private static final long serialVersionUID = 0L;
	
	public static class PageSourceExceptionCore extends TaskExceptionCore{
		private static final long serialVersionUID = 0L;
		String pageID;
		float x, y;
		public PageSourceExceptionCore(String pageID, int w, int h, float x, float y) {
			super(pageID, w, h);
			this.pageID = pageID;
			this.x = x;
			this.y = y;
		}
		
		public String getPageID() {
			return pageID;
		}

		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}

		@Override
		public String getDescription() {
			return "PageSourceException";
		}
		@Override
		public String getLocalizedMessage() {
			return "PageSourceException";
		}
	}
	
	public PageSourceException(String pageID, int w, int h, float x, float y){
		super(new PageSourceExceptionCore(pageID, w, h, x, y));
	}
}
