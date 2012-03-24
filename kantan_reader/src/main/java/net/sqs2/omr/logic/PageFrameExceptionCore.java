package net.sqs2.omr.logic;

import java.awt.Point;
import java.io.Serializable;

import net.sqs2.omr.task.TaskExceptionCore;

public abstract class PageFrameExceptionCore extends TaskExceptionCore  implements Serializable{

	protected Point[] masterCorners;
	protected Point[] corners;

	public PageFrameExceptionCore(String pageID, int width, int height, Point[] masterCorners, Point[] corners) {
		super(pageID, width, height);
		this.masterCorners = masterCorners;
		this.corners = corners;
	}

	public Point[] getMasterCorners() {
    	return masterCorners;
    }

	public Point[] getCorners() {
    	return corners;
    }
	
}