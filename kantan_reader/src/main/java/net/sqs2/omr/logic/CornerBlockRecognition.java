package net.sqs2.omr.logic;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;
import java.util.logging.Logger;

import net.sqs2.image.ImageUtil;
import net.sqs2.omr.source.config.BlockRecognitionConfig;

public class CornerBlockRecognition {
	
	BlockRecognitionConfig config;
	Point blockCenterPoint = null;
	
	public CornerBlockRecognition(BufferedImage image, BlockRecognitionConfig config, Rectangle focusArea){
		this.config = config;
		this.blockCenterPoint = createBlockCenterPoint(image, focusArea);
	}
	
	public Point getBlockCenterPoint(){
		return this.blockCenterPoint;
	}
	
	private Point createBlockCenterPoint(BufferedImage image, Rectangle focusArea){
		return createBlockCenterPoint(image, focusArea, null,
				(int)(focusArea.getWidth() * this.config.getHorizontal()), 
				(int)(focusArea.getHeight() * this.config.getVertical()),
				(int)(255 * this.config.getDensity()));
	}
	
	private static Point createBlockCenterPoint(BufferedImage image, Rectangle focusArea,
			Point centerPoint, int horizontal, int vertical, int threshold){
		
		if(centerPoint == null){
			centerPoint = new Point(focusArea.x + focusArea.width / 2,
					focusArea.y + focusArea.height / 2);
		}else if(focusArea.contains(centerPoint)){
			throw new InvalidParameterException(centerPoint.toString());
		}

		int firstBlackVerticalLineX = scanEdge(image,
				focusArea, centerPoint, -1, 0, horizontal, vertical, threshold);
		int lastBlackVerticalLineX = scanEdge(image,
				focusArea, centerPoint, 1, 0, horizontal, vertical, threshold);
		int firstBlackHorizontalLineY = scanEdge(image,
				focusArea, centerPoint, 0, -1, horizontal, vertical, threshold);
		int lastBlackHorizontalLineY = scanEdge(image,
				focusArea, centerPoint, 0, 1, horizontal, vertical, threshold);
		
		return new Point((firstBlackVerticalLineX + lastBlackVerticalLineX) / 2 + 1,
				(firstBlackHorizontalLineY + lastBlackHorizontalLineY) / 2);
	}
	
	private static int countNumBlackPixelsHorizontal(BufferedImage image, 
			Rectangle focusArea, int y, int threshold){
		int numBlackHorizontalPixels = 0;
		if(image.getHeight() <= y){
			return 0;
		}
		for(int x = focusArea.x; x < focusArea.x + focusArea.width; x++){
			if(x < image.getWidth()){
				if(ImageUtil.rgb2gray(image.getRGB(x, y)) <= threshold){
					numBlackHorizontalPixels++;	
				}
			}else{
				Logger.getAnonymousLogger().severe("x="+x+" y="+y);
			}
		}
		return numBlackHorizontalPixels;
	}
	
	private static int countNumBlackPixelsVertical(BufferedImage image, 
			Rectangle focusArea, int x, int threshold){
		int numBlackVerticalPixels = 0;
		if(image.getWidth() <= x){
			return 0;
		}

		for(int y = focusArea.y; y < focusArea.y + focusArea.height; y++){
			if(x < image.getWidth()){
				if(ImageUtil.rgb2gray(image.getRGB(x, y)) <= threshold){
					numBlackVerticalPixels++;	
				}
			}else{
				Logger.getAnonymousLogger().severe("x="+x+" y="+y);
			}
		}
		return numBlackVerticalPixels;
	}
	
	private static int scanEdge(BufferedImage image, Rectangle focusArea, 
			Point start, int dx, int dy, int horizontal, int vertical, int threshold){
		if(dx == 0){
			if(dy == -1){
				return scanNorth(image, focusArea, start, horizontal, threshold);
			}else if(dy == 1){
				return scanSouth(image, focusArea, start, horizontal, threshold);
			}
		}else if(dy == 0){
			if(dx == -1){
				return scanWest(image, focusArea, start, vertical, threshold);
			}else if(dx == 1){
				return scanEast(image, focusArea, start, vertical, threshold);
			}
		}
		throw new InvalidParameterException("dx="+dx+",dy="+dy);
	}

	private static int scanEast(BufferedImage image, Rectangle focusArea,
			Point start, int vertical, int threshold) {
		for(int x = start.x; x < focusArea.x + focusArea.width; x++){
			int numBlackVerticalPixels = countNumBlackPixelsVertical(image, focusArea, x, threshold);
			if(numBlackVerticalPixels < vertical){
				return x;
			}
		}
		return focusArea.x + focusArea.width - 1;
	}

	private static int scanWest(BufferedImage image, Rectangle focusArea,
			Point start, int vertical, int threshold) {
		for(int x = start.x; focusArea.x <= x; x--){
			int numBlackVerticalPixels = countNumBlackPixelsVertical(image, focusArea, x, threshold);
			if(numBlackVerticalPixels < vertical){
				return x;
			}
		}
		return focusArea.x;
	}

	private static int scanSouth(BufferedImage image, Rectangle focusArea,
			Point start, int horizontal, int threshold) {
		for(int y = start.y; y < focusArea.y + focusArea.height; y++){
			int numBlackHorizontalPixels = countNumBlackPixelsHorizontal(image, focusArea, y, threshold);
			if(numBlackHorizontalPixels < horizontal){
				return y;
			}
		}
		return focusArea.y + focusArea.height - 1;
	}

	final private static int scanNorth(BufferedImage image, Rectangle focusArea,
			Point start, int horizontal, int threshold) {
		for(int y = start.y; focusArea.y <= y; y--){
			int numBlackHorizontalPixels = countNumBlackPixelsHorizontal(image, focusArea, y, threshold);
			if(numBlackHorizontalPixels < horizontal){
				return y;
			}
		}
		return focusArea.y;
	}
}
