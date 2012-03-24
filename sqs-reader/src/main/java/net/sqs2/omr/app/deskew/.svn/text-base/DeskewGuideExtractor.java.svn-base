/*

 PageFrameDeskewGuideArea.java

 Copyright 2009 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2009/01/11

 */
package net.sqs2.omr.app.deskew;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.TreeSet;

import net.sqs2.image.ImageSilhouetteExtract;
import net.sqs2.image.ImageUtil;
import net.sqs2.omr.model.DeskewGuideAreaConfig;
import net.sqs2.omr.model.PageID;

public class DeskewGuideExtractor {
	final static String LABEL = ".0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	final static int LABEL_LENGTH = LABEL.length();

	public static final int BITMAP_WIDTH = 600;
	public static final int BITMAP_HEIGHT = 60;

	public static final int HEADER_AREA_TYPE_INDEX = 0;
	public static final int FOOTER_AREA_TYPE_INDEX = 1;
	
	BufferedImage image;
	PageID pageID;
	DeskewGuideAreaConfig config;

	int imageWidth;
	int imageHeight;

	int horizontalMargin;
	int deskewGuideAreaHeight;
	int headerVerticalMargin;
	int footerVerticalMargin;

	int[] numSilhouettes = new int[2];
	int[][] silhouetteIndexArray = new int[2][];
	boolean[][] bitmaps;

	public DeskewGuideExtractor(BufferedImage image, PageID pageID, DeskewGuideAreaConfig config) {
		this.image = image;
		this.pageID = pageID;
		this.config = config;

		this.imageWidth = image.getWidth();
		this.imageHeight = image.getHeight();

		this.horizontalMargin = (int) (this.imageWidth * config.getHorizontalMargin());
		this.deskewGuideAreaHeight = (int) (this.imageHeight * config.getHeight());
		this.headerVerticalMargin = (int) (this.imageHeight * config.getHeaderVerticalMargin());
		this.footerVerticalMargin = (int) (this.imageHeight * config.getFooterVerticalMargin());
		this.bitmaps = createBitmap(image, config);
	}

	public ExtractedDeskewGuides extractDeskewGuides() throws PageFrameException {
		DeskewGuide[] header = extractDeskewGuidePair(HEADER_AREA_TYPE_INDEX);
		DeskewGuide[] footer = extractDeskewGuidePair(FOOTER_AREA_TYPE_INDEX);
		return new ExtractedDeskewGuides(header[0].getPoint(), header[1].getPoint(),
				header[0].getAreaSize(), header[1].getAreaSize(),
				footer[0].getPoint(), footer[1].getPoint(), 
				footer[0].getAreaSize(), footer[1].getAreaSize());
	}
	
	class DeskewGuide{
		Point2D point;
		int areaSize;
		DeskewGuide(Point2D point, int area){
			this.point = point;
			this.areaSize = area;
		}
		public Point2D getPoint() {
			return point;
		}
		public int getAreaSize() {
			return areaSize;
		}
	}

	/*
	private DeskewGuide getDeskewGuideLeft(int[] xMinArray, int[] xMaxArray, int[] yMinArray, int[] yMaxArray, 
			int[] areaArray, int silhouetteIndexMin, int silhouetteIndexMax) {
		Point2D leftPoint = new Point2D.Float(
				(xMinArray[silhouetteIndexMin] + xMaxArray[silhouetteIndexMin]) / 2,
				(yMinArray[silhouetteIndexMin] + yMaxArray[silhouetteIndexMin]) / 2
				);
		int leftAreaSize = areaArray[silhouetteIndexMin];
		return new DeskewGuide(leftPoint, leftAreaSize);
	}
	
	private DeskewGuide getDeskewGuideRight(int[] xMinArray, int[] xMaxArray, int[] yMinArray, int[] yMaxArray, 
			int[] areaArray, int silhouetteIndexMin, int silhouetteIndexMax) {
		Point2D rightPoint = new Point2D.Float(
				(xMinArray[silhouetteIndexMax] + xMaxArray[silhouetteIndexMax]) / 2,
				(yMinArray[silhouetteIndexMax] + yMaxArray[silhouetteIndexMax]) / 2
				);
		int rightAreaSize = areaArray[silhouetteIndexMax];
		return new DeskewGuide(rightPoint, rightAreaSize);
	}*/

	private void printDebugInformation(int type, int[] areaArray) {
		printDebugInformation(type, areaArray, 3);
	}
	
	private void printDebugInformation(int type, int[] areaArray, int step) {
		boolean[] b = this.bitmaps[type];
		int[] s = this.silhouetteIndexArray[type];
		for (int y = 0; y < BITMAP_HEIGHT; y+=step) {
			for (int x = 0; x < BITMAP_WIDTH; x+=step) {
				int pixelIndex = x + y * BITMAP_WIDTH;
				int silhouetteIndex = s[pixelIndex];
				if (0 < silhouetteIndex) {
					if (0 < areaArray[silhouetteIndex]) {
						System.err.print("@");
					} else {
						System.err.print("#");
					}
				} else if (b[pixelIndex]) {
					System.err.print("+");
				} else {
					System.err.print("-");
				}
			}
			System.err.println();
		}
		System.err.println();
	}

	/*
	private Point getDeskewGuideCenterPoint(int type, int[] xMinArray, int[] xMaxArray, int[] yMinArray, int[] yMaxArray, int silhouetteIndex) {
		Rectangle focusAreaRect = null;
		if (type == HEADER_AREA_TYPE_INDEX) {
			int x0 = translateX(xMinArray[silhouetteIndex]);
			int y0 = translateHeaderY(yMinArray[silhouetteIndex]);
			int x1 = translateX(xMaxArray[silhouetteIndex]);
			int y1 = translateHeaderY(yMaxArray[silhouetteIndex]);
			focusAreaRect = new Rectangle(x0 - 3, y0 - 3, x1 - x0 + 9, y1 - y0 + 9);
		}else if (type == FOOTER_AREA_TYPE_INDEX) {
			int x0 = translateX(xMinArray[silhouetteIndex]);
			int y0 = translateFooterY(yMinArray[silhouetteIndex]);
			int x1 = translateX(xMaxArray[silhouetteIndex]);
			int y1 = translateFooterY(yMaxArray[silhouetteIndex]);
			focusAreaRect = new Rectangle(x0 - 3, y1 - 3, x1 - x0 + 9, y0 - y1 + 9);
		}
		return getDeskewGuideCenterPoint(type);
	}

	 */
	
	private int translateX(int x) {
		return this.horizontalMargin + (x) * (this.imageWidth - this.horizontalMargin * 2) / BITMAP_WIDTH;
	}

	private int translateY(int y, int type) {
		switch(type){
		case HEADER_AREA_TYPE_INDEX:
			return translateHeaderY(y);
		case FOOTER_AREA_TYPE_INDEX:
			return translateFooterY(y);
		default:
			throw new IllegalArgumentException("type:"+type);
		}
	}
	private int translateHeaderY(int y) {
		return this.headerVerticalMargin + (y) * this.deskewGuideAreaHeight / BITMAP_HEIGHT;
	}

	private int translateFooterY(int y) {
		return this.imageHeight
				- (this.footerVerticalMargin + (y) * this.deskewGuideAreaHeight / BITMAP_HEIGHT);
	}
	
	private boolean[][] createBitmap(BufferedImage image, DeskewGuideAreaConfig config) {

		boolean[][] booleanValueBitmap = new boolean[2][BITMAP_WIDTH * BITMAP_HEIGHT];

		int[][] grayscaleValueBitmap = new int[2][BITMAP_WIDTH * BITMAP_HEIGHT];
		int[][] numPixelsInOriginalImage = new int[2][BITMAP_WIDTH * BITMAP_HEIGHT];

		for (int y = this.headerVerticalMargin; y < this.deskewGuideAreaHeight + this.headerVerticalMargin; y++) {
			int bitmapY = (y - this.headerVerticalMargin) * BITMAP_HEIGHT / this.deskewGuideAreaHeight;
			for (int x = this.horizontalMargin; x < this.imageWidth - this.horizontalMargin; x++) {
				int bitmapX = (x - this.horizontalMargin) * BITMAP_WIDTH
						/ (this.imageWidth - 2 * this.horizontalMargin);
				int pixelIndex = bitmapX + bitmapY * BITMAP_WIDTH;
				grayscaleValueBitmap[0][pixelIndex] += ImageUtil.rgb2gray(image.getRGB(x, y));
				numPixelsInOriginalImage[0][pixelIndex]++;
			}
		}

		for (int y = this.footerVerticalMargin; y < this.deskewGuideAreaHeight + this.footerVerticalMargin; y++) {
			int bitmapY = (y - this.footerVerticalMargin) * BITMAP_HEIGHT / this.deskewGuideAreaHeight;
			for (int x = this.horizontalMargin; x < this.imageWidth - this.horizontalMargin; x++) {
				int bitmapX = (x - this.horizontalMargin) * BITMAP_WIDTH
						/ (this.imageWidth - 2 * this.horizontalMargin);
				int pixelIndex = bitmapX + bitmapY * BITMAP_WIDTH;
				grayscaleValueBitmap[1][pixelIndex] += ImageUtil.rgb2gray(image.getRGB(x, this.imageHeight
						- 1 - y));
				numPixelsInOriginalImage[1][pixelIndex]++;
			}
		}

		for (int type = 0; type < 2; type++) {
			int[] grayscaleValueBitmapByType = grayscaleValueBitmap[type];
			int[] numPixelsInOriginalImageByType = numPixelsInOriginalImage[type];
			boolean[] booleanValueBitmapByType = booleanValueBitmap[type];
			for (int y = 0; y < BITMAP_HEIGHT; y++) {
				for (int x = 0; x < BITMAP_WIDTH; x++) {
					int index = x + y * BITMAP_WIDTH;
					if (numPixelsInOriginalImage[type][index] != 0
							&& grayscaleValueBitmapByType[index] / numPixelsInOriginalImageByType[index] < 255 * config
									.getDensity()) {
						booleanValueBitmapByType[index] = true;
						// System.err.print("T");
					} else {
						// System.err.print("-");
					}
				}
				// System.err.println();
			}
			// System.err.println();
		}

		return booleanValueBitmap;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder(1024);

		for (int type = 0; type < 2; type++) {
			int pixelIndex = 0;
			int[] a = this.silhouetteIndexArray[type];
			if (a == null) {
				continue;
			}

			builder.append("################################################################\n");
			if (this.numSilhouettes[type] == 2) {
				builder.append("OK: " + this.numSilhouettes[type]);
			} else {
				builder.append("NG: " + this.numSilhouettes[type]);
			}
			builder.append("\n");

			for (int y = 0; y < BITMAP_HEIGHT; y++) {
				for (int x = 0; x < BITMAP_WIDTH; x++) {
					builder.append(getLabelChar(a[pixelIndex++]));
				}
				builder.append("\n");
			}
		}
		return builder.toString();
	}

	private static char getLabelChar(int p) {
		char c;
		if (p < LABEL_LENGTH) {
			c = LABEL.charAt(p);
		} else {
			c = '*';
		}
		return c;
	}
	
	class Silhouette implements Comparable<Silhouette>{
		int silhouetteIndex;
		int areaSize;
		int x, y;
		Silhouette(int silhouetteIndex, int areaSize, int x, int y){
			this.silhouetteIndex = silhouetteIndex;
			this.areaSize = areaSize;
			this.x = x;
			this.y = y;
		}
		@Override
		public int compareTo(Silhouette o) {
			int ret = this.areaSize - o.areaSize;
			if(ret != 0){
				return ret;
			}
			int r = this.x - o.x;
			if(0 < r){
				return 1;
			}else if(r < 0){
				return -1;
			}
			r = this.y - o.y;
			if(0 < r){
				return 1;
			}else if(r < 0){
				return -1;
			}
			return 0;
		}
		
		public int hashCode(){
			return this.silhouetteIndex;
		}
		
		public String toString(){
			return "Silhouette["+silhouetteIndex+"]=("+x+","+y+":"+areaSize+")";
		}
	}
	
	private DeskewGuide[] extractDeskewGuidePair(int type)throws PageFrameException {
		ImageSilhouetteExtract ise = new ImageSilhouetteExtract(bitmaps[type], BITMAP_WIDTH, BITMAP_HEIGHT);
		
		silhouetteIndexArray[type] = ise.getSilhouetteIndexArray();

		int[] areaArray = ise.getAreaArray();
		int[] xMinArray = new int[areaArray.length];
		int[] xMaxArray = new int[areaArray.length];
		int[] yMinArray = new int[areaArray.length];
		int[] yMaxArray = new int[areaArray.length];

		int[] gxTotal = new int[areaArray.length];
		int[] gyTotal = new int[areaArray.length];

		initXYMinMaxArray(type, areaArray, xMinArray, xMaxArray, yMinArray, yMaxArray, gxTotal, gyTotal);
		
		TreeSet<Silhouette> silhouetteSet = new TreeSet<Silhouette>(); 
		
		for (int silhouetteIndex = areaArray.length - 1; 0 <= silhouetteIndex; silhouetteIndex--) {
			int areaSize = areaArray[silhouetteIndex];
			if (8 <= areaSize) {
				int w = xMaxArray[silhouetteIndex] - xMinArray[silhouetteIndex] + 1;
				int h = yMaxArray[silhouetteIndex] - yMinArray[silhouetteIndex] + 1;
				int x = gxTotal[silhouetteIndex]/areaSize;
				int y = gyTotal[silhouetteIndex]/areaSize;
				if (w * h * 0.75 < areaSize) {
					if(type == HEADER_AREA_TYPE_INDEX){
						if(Math.abs(w - h) <= Math.max(w, h)*0.5){ 
							Silhouette s = new Silhouette(silhouetteIndex, areaSize, x, y);
							silhouetteSet.add(s);
						}
					}else{
						if(Math.abs(w*2 - h) <= Math.max(w*2, h)*0.6){// 16-11 5 <= (16, 11)5 
							Silhouette s = new Silhouette(silhouetteIndex, areaSize, x, y);
							silhouetteSet.add(s);
						}
					}
				} else {
					// remove noizy silhouette
					areaArray[silhouetteIndex] = 0;
				}
			} else {
				// remove smaller silhouette
				areaArray[silhouetteIndex] = 0;
			}
		}

		if (silhouetteSet.size() < 2) {
			printDebugInformation(type, areaArray);
			throw new PageFrameException(new DeskewGuideMissingExceptionModel(pageID, image.getWidth(), image.getHeight(), type));
		}

		Silhouette s1 = silhouetteSet.last();
		Silhouette s2 = silhouetteSet.lower(s1);
		
		if(s1.x == s2.x){
			printDebugInformation(type, areaArray);
			throw new PageFrameException(new DeskewGuideMissingExceptionModel(pageID, image.getWidth(), image.getHeight(), type));
		}
		
		Silhouette left = (s1.x < s2.x)?s1:s2;
		Silhouette right = (s1.x < s2.x)?s2:s1;
		
		DeskewGuide deskewGuideLeft = new DeskewGuide(new Point2D.Float(translateX(left.x), translateY(left.y, type)), left.areaSize);
		DeskewGuide deskewGuideRight = new DeskewGuide(new Point2D.Float(translateX(right.x), translateY(right.y, type)), right.areaSize);
		
		return new DeskewGuide[]{deskewGuideLeft,deskewGuideRight};
	}
	
	private void initXYMinMaxArray(int type, int[] areaArray, int[] xMinArray, int[] xMaxArray, int[] yMinArray, int[] yMaxArray, int[] gxTotal, int[] gyTotal) {

		for (int silhouetteIndex = areaArray.length - 1; 0 <= silhouetteIndex; silhouetteIndex--) {
		 	xMinArray[silhouetteIndex] = Integer.MAX_VALUE;
			xMaxArray[silhouetteIndex] = Integer.MIN_VALUE;
			yMinArray[silhouetteIndex] = Integer.MAX_VALUE;
			yMaxArray[silhouetteIndex] = Integer.MIN_VALUE;
		}

		int pixelIndex = BITMAP_WIDTH * BITMAP_HEIGHT - 1;
		int[] s = silhouetteIndexArray[type];
		for (int y = BITMAP_HEIGHT - 1; 0 <= y; y--) {
			for (int x = BITMAP_WIDTH - 1; 0 <= x; x--) {
				int silhouetteIndex = s[pixelIndex];
				int area = areaArray[silhouetteIndex];
				if (0 < area) {
					xMinArray[silhouetteIndex] = Math.min(xMinArray[silhouetteIndex], x);
					xMaxArray[silhouetteIndex] = Math.max(xMaxArray[silhouetteIndex], x);
					yMinArray[silhouetteIndex] = Math.min(yMinArray[silhouetteIndex], y);
					yMaxArray[silhouetteIndex] = Math.max(yMaxArray[silhouetteIndex], y);
					gxTotal[silhouetteIndex] += x;
					gyTotal[silhouetteIndex] += y;
				} else {
					s[pixelIndex] = 0;
				}
				pixelIndex--;
			}
		}
	}
}
