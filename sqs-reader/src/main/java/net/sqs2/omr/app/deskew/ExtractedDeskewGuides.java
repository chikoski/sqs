package net.sqs2.omr.app.deskew;

import java.awt.geom.Point2D;

public class ExtractedDeskewGuides{

	Point2D[] deskewGuideCenterPoints = new Point2D[4];
	int[] deskewGuideAreaSizes = new int[4];

	public ExtractedDeskewGuides(Point2D headerLeftPoint, Point2D headerRightPoint,
			int headerLeftAreaSize, int headerRightAreaSize,
								Point2D footerLeftPoint, Point2D footerRightPoint,
								int footerLeftAreaSize, int footerRightAreaSize){
		deskewGuideCenterPoints[0] = headerLeftPoint;
		deskewGuideCenterPoints[1] = headerRightPoint;
		deskewGuideCenterPoints[2] = footerLeftPoint;
		deskewGuideCenterPoints[3] = footerRightPoint;
		deskewGuideAreaSizes[0] = headerLeftAreaSize;
		deskewGuideAreaSizes[1] = headerRightAreaSize;
		deskewGuideAreaSizes[2] = footerLeftAreaSize;
		deskewGuideAreaSizes[3] = footerRightAreaSize;
	}

	public Point2D[] getDeskewGuideCenterPoints() {
		return deskewGuideCenterPoints;
	}

	public int[] getDeskewGuideAreaSizes() {
		return deskewGuideAreaSizes;
	}
	
	public String toString(){
		return "DeskewGuide:"+deskewGuideCenterPoints+" = "+deskewGuideAreaSizes;
	}
}