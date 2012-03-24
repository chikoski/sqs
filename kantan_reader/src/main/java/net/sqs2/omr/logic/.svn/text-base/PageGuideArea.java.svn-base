/**
 * 
 */
package net.sqs2.omr.logic;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import net.sqs2.image.ImageSilhouetteExtract;
import net.sqs2.image.ImageUtil;
import net.sqs2.omr.source.config.PageGuideAreaConfig;

class PageGuideArea{
	
	public static final int BITMAP_WIDTH = 400; 
	public static final int BITMAP_HEIGHT = 40; 

	BufferedImage image;
	String pageID;
	PageGuideAreaConfig config;

	int imageWidth;
	int imageHeight;
	
	int horizontalMargin;
	int pageGuideAreaHeight;
	int headerVerticalMargin;
	int footerVerticalMargin;

	int[] numSilhouettes = new int[2];
	int[][] silhouetteIndexArray = new int[2][];
	boolean[][] bitmaps;
	
	public PageGuideArea(BufferedImage image, String pageID, PageGuideAreaConfig config){
		this.image = image;
		this.pageID = pageID;
		this.config = config;
		
		this.imageWidth = image.getWidth();
		this.imageHeight = image.getHeight();
		
		this.horizontalMargin = (int)(this.imageWidth * config.getHorizontalMargin());
		this.pageGuideAreaHeight = (int)(this.imageHeight * config.getHeight());
		this.headerVerticalMargin = (int)(this.imageHeight * config.getHeaderVerticalMargin());
		this.footerVerticalMargin = (int)(this.imageHeight * config.getFooterVerticalMargin());

		this.bitmaps  = createBitmap(image, config);
	}
	
	public Point[] scanCorners()throws PageFrameException{
		Point[] corners = new Point[4];
		scanCorners(corners, 0);
		scanCorners(corners, 1);
		/*
		System.err.println("0 "+corners[0]);
		System.err.println("1 "+corners[1]);
		System.err.println("2 "+corners[2]);
		System.err.println("3 "+corners[3]);
		*/
		return corners;
	}

	private void scanCorners(Point[] corners, int type) throws PageFrameException{
	    ImageSilhouetteExtract ise = new ImageSilhouetteExtract(this.bitmaps[type], BITMAP_WIDTH, BITMAP_HEIGHT);
	    this.silhouetteIndexArray[type] = ise.getSilhouetteIndexArray();
		int[] areaArray = filterArea(ise, type);

		int[] xMinArray = new int[areaArray.length]; 
		int[] xMaxArray = new int[areaArray.length];
		int[] yMinArray = new int[areaArray.length]; 
		int[] yMaxArray = new int[areaArray.length]; 

		int[] gxTotal = new int[areaArray.length]; 
		int[] gyTotal = new int[areaArray.length];
		
		initXYMinMaxArray(type, areaArray, xMinArray, xMaxArray, yMinArray, yMaxArray, gxTotal, gyTotal);
		
		int silhouetteIndexMin = -1;
		int silhouetteIndexMax = -1;
		int gxMin = Integer.MAX_VALUE;
		int gxMax = Integer.MIN_VALUE;
		
		int W_MIN = (int)(BITMAP_WIDTH * this.config.getBlockRecongnitionFilterConfig().getWidthMin() / (type + 1));
		int W_MAX = (int)(BITMAP_WIDTH * this.config.getBlockRecongnitionFilterConfig().getWidthMax() / (type + 1));
		int H_MIN = (int)(BITMAP_HEIGHT * this.config.getBlockRecongnitionFilterConfig().getHeightMin());
		int H_MAX = (int)(BITMAP_HEIGHT * this.config.getBlockRecongnitionFilterConfig().getHeightMax());
		
		for(int silhouetteIndex = areaArray.length - 1; 0 <= silhouetteIndex; silhouetteIndex--){
			int area = areaArray[silhouetteIndex];
			int w = xMaxArray[silhouetteIndex] - xMinArray[silhouetteIndex];
			int h = yMaxArray[silhouetteIndex] - yMinArray[silhouetteIndex];
			if(0 < area){
				if(W_MIN <= w && w  <= W_MAX && H_MIN <= h && h  <= H_MAX &&
						w * h * config.getBlockRecongnitionFilterConfig().getDensity() <= area){
					int gx = gxTotal[silhouetteIndex] / areaArray[silhouetteIndex];				
					if(gx <= gxMin){
						silhouetteIndexMin = silhouetteIndex;
						gxMin = gx;
					}
					if(gxMax <= gx){
						silhouetteIndexMax = silhouetteIndex;
						gxMax = gx;
					}
				}else{
					areaArray[silhouetteIndex] = 0;
				}
			}
		}
		
		if(this.config.isDebug()){
			printDebugInformation(type, areaArray);
		}
		
		if(silhouetteIndexMin == -1 || silhouetteIndexMax == -1){
			throw new PageFrameException(new CornerBlockMissingExceptionCore(pageID, image.getWidth(), image.getHeight(), type));
		}
		
		setCorners(corners, type, xMinArray, xMaxArray, yMinArray, yMaxArray, silhouetteIndexMin, silhouetteIndexMax);
	}

	private void setCorners(Point[] corners, int type, int[] xMinArray,
			int[] xMaxArray, int[] yMinArray, int[] yMaxArray,
			int silhouetteIndexMin, int silhouetteIndexMax) {
		corners[type*2+0] = getCornerBlockCenterPoint(type, xMinArray, xMaxArray, yMinArray, yMaxArray, silhouetteIndexMin);
		corners[type*2+1] = getCornerBlockCenterPoint(type, xMinArray, xMaxArray, yMinArray, yMaxArray, silhouetteIndexMax);
	}

	private void printDebugInformation(int type, int[] areaArray) {
	    boolean[] b = this.bitmaps[type];
	    int[] s = this.silhouetteIndexArray[type];
	    for(int y = 0; y < BITMAP_HEIGHT; y++){
	    	for(int x = 0; x < BITMAP_WIDTH; x++){
	    		int pixelIndex = x + y * BITMAP_WIDTH;
	    		int silhouetteIndex = s[pixelIndex];
	    		if( 0 < silhouetteIndex ){
	    			if(0 < areaArray[silhouetteIndex]){
	    				//System.err.print("@");
	    			}else{
	    				//System.err.print("#");
	    			}
	    		}else if(b[pixelIndex]){
	    			//System.err.print("+");
	    		}else{
	    			//System.err.print("-");
	    		}
	    	}
	    	//System.err.println();
	    }
	    //System.err.println();
    }

	private Point getCornerBlockCenterPoint(int type, int[] xMinArray, int[] xMaxArray, int[] yMinArray, int[] yMaxArray,
            int silhouetteIndex) {
		Rectangle focusAreaRight = null;
		if(type == 0){
		    int x0 = translateX(xMinArray[silhouetteIndex]);
		    int y0 = translateHeaderY(yMinArray[silhouetteIndex]);
			int x1 = translateX(xMaxArray[silhouetteIndex]);
			int y1 = translateHeaderY(yMaxArray[silhouetteIndex]);
			focusAreaRight = new Rectangle(x0 - 3, y0 - 3, x1 - x0 + 9, y1 - y0 + 9);
		}else{
		    int x0 = translateX(xMinArray[silhouetteIndex]);
			int y0 = translateFooterY(yMinArray[silhouetteIndex]); 
			int x1 = translateX(xMaxArray[silhouetteIndex]);
			int y1 = translateFooterY(yMaxArray[silhouetteIndex]); 
			focusAreaRight = new Rectangle(x0 - 3, y1 - 3, x1 - x0 + 9, y0 - y1 + 9);
		}
		//System.err.println(type+":"+focusAreaRight);
		CornerBlockRecognition bcepRight = new CornerBlockRecognition(this.image, this.config.getBlockRecognitionConfig(), focusAreaRight);
		return bcepRight.getBlockCenterPoint();
    }
	
	private void initXYMinMaxArray(int type, 
            int[] areaArray, 
            int[] xMinArray, int[] xMaxArray, int[] yMinArray,
            int[] yMaxArray,
            int[] gxTotal, int[] gyTotal) {
		
	    for(int silhouetteIndex = areaArray.length - 1; 0 <= silhouetteIndex; silhouetteIndex --){
			xMinArray[silhouetteIndex] = Integer.MAX_VALUE;
			xMaxArray[silhouetteIndex] = Integer.MIN_VALUE;
			yMinArray[silhouetteIndex] = Integer.MAX_VALUE;
			yMaxArray[silhouetteIndex] = Integer.MIN_VALUE;
		}

	    int pixelIndex = BITMAP_WIDTH * BITMAP_HEIGHT - 1;
		int[] s = this.silhouetteIndexArray[type];
		for(int y = BITMAP_HEIGHT - 1; 0 <= y; y--){
			for(int x = BITMAP_WIDTH - 1; 0 <= x; x--){
				int silhouetteIndex = s[pixelIndex];
				int area = areaArray[silhouetteIndex];
				if(0  < area){
					xMinArray[silhouetteIndex] = Math.min(xMinArray[silhouetteIndex], x); 
					xMaxArray[silhouetteIndex] = Math.max(xMaxArray[silhouetteIndex], x);
					yMinArray[silhouetteIndex] = Math.min(yMinArray[silhouetteIndex], y); 
					yMaxArray[silhouetteIndex] = Math.max(yMaxArray[silhouetteIndex], y);
					gxTotal[silhouetteIndex] += x;
					gyTotal[silhouetteIndex] += y;
				}else{
					s[pixelIndex] = 0;
				}
				pixelIndex--;
			}
		}
    }
	
	private int translateX(int x) {
	    return this.horizontalMargin + (x) * (this.imageWidth - this.horizontalMargin * 2) / BITMAP_WIDTH;
    }
	
	private int translateHeaderY(int y) {
	    return this.headerVerticalMargin + (y) * this.pageGuideAreaHeight / BITMAP_HEIGHT;
    }

	private int translateFooterY(int y) {
	    return this.imageHeight - (this.footerVerticalMargin + (y) * this.pageGuideAreaHeight / BITMAP_HEIGHT);
    }

	private int[] filterArea(ImageSilhouetteExtract ise, int type)throws PageFrameException{
		int wMin = (int)Math.floor(this.config.getBlockRecongnitionFilterConfig().getWidthMin() * BITMAP_WIDTH);
		int wMax = (int)Math.ceil(this.config.getBlockRecongnitionFilterConfig().getWidthMax() * BITMAP_WIDTH);
		int hMin = (int)Math.floor(this.config.getBlockRecongnitionFilterConfig().getHeightMin() * BITMAP_HEIGHT);
		int hMax = (int)Math.ceil(this.config.getBlockRecongnitionFilterConfig().getHeightMax() * BITMAP_HEIGHT);

		int aMin = wMin * hMin / (type + 1);
		int aMax = wMax * hMax / (type + 1);

		int[] areaArray = ise.getAreaArray();
		this.numSilhouettes[type] = areaArray.length;

		for(int silhouetteIndex = areaArray.length - 1; 0 <= silhouetteIndex; silhouetteIndex--){
			int area = areaArray[silhouetteIndex];
			//char c = getLabelChar(silhouetteIndex);
			if(area <= aMin || aMax <= area){
				/*
				if(this.config.isDebug()){
					System.err.println(type+"["+c+"] "+ aMin +"   "+area+"   "+aMax);
				}
				*/
				areaArray[silhouetteIndex] = 0;
				this.numSilhouettes[type] --;
			}else{/*
				if(this.config.isDebug()){
					System.err.println(type+"["+c+"] "+ aMin +" < "+area+" < "+aMax);
				}*/
			}
		}
		return areaArray;
	}

	private boolean[][] createBitmap(BufferedImage image,
			PageGuideAreaConfig config) {
		
		boolean[][] booleanValueBitmap = new boolean[2][BITMAP_WIDTH*BITMAP_HEIGHT];
		
		int[][] grayscaleValueBitmap = new int[2][BITMAP_WIDTH*BITMAP_HEIGHT];
		int[][] numPixelsInOriginalImage = new int[2][BITMAP_WIDTH*BITMAP_HEIGHT];

		for(int y = this.headerVerticalMargin; y < this.pageGuideAreaHeight + headerVerticalMargin; y++){
			int bitmapY = (y - headerVerticalMargin) * BITMAP_HEIGHT / this.pageGuideAreaHeight;
			for(int x = this.horizontalMargin; x < this.imageWidth - this.horizontalMargin; x++){
				int bitmapX = (x - this.horizontalMargin ) * BITMAP_WIDTH / (this.imageWidth - 2 * this.horizontalMargin);
				int pixelIndex = bitmapX + bitmapY * BITMAP_WIDTH; 
				grayscaleValueBitmap[0][pixelIndex] += ImageUtil.rgb2gray(image.getRGB(x, y));  
				numPixelsInOriginalImage[0][pixelIndex]++;
			}
		}

		for(int y = this.footerVerticalMargin; y < this.pageGuideAreaHeight + this.footerVerticalMargin; y++){
			int bitmapY = (y - this.footerVerticalMargin) * BITMAP_HEIGHT / this.pageGuideAreaHeight;
			for(int x = this.horizontalMargin; x < this.imageWidth - this.horizontalMargin; x++){
				int bitmapX = (x - this.horizontalMargin ) * BITMAP_WIDTH / (this.imageWidth - 2 * this.horizontalMargin);
				int pixelIndex = bitmapX + bitmapY * BITMAP_WIDTH; 
				grayscaleValueBitmap[1][pixelIndex] += ImageUtil.rgb2gray(image.getRGB(x, this.imageHeight - 1 - y));
				numPixelsInOriginalImage[1][pixelIndex]++;
			}
		}

		for(int type = 0; type < 2; type++){
			int[] grayscaleValueBitmapByType = grayscaleValueBitmap[type];
			int[] numPixelsInOriginalImageByType = numPixelsInOriginalImage[type];
			boolean[] booleanValueBitmapByType = booleanValueBitmap[type];
			for(int y = 0; y < BITMAP_HEIGHT; y++){
				for(int x = 0; x < BITMAP_WIDTH; x++){
					int index = x + y * BITMAP_WIDTH;
					if(numPixelsInOriginalImage[type][index] != 0 &&
							grayscaleValueBitmapByType[index] / numPixelsInOriginalImageByType[index] < 255 * config.getDensity() ){
						booleanValueBitmapByType[index] = true;
						//System.err.print("T");
					}else{
						//System.err.print("-");
					}
		        }
				//System.err.println();
			}
			//System.err.println();
		}
		
		return booleanValueBitmap;
    }

	@Override
	public String toString(){
		
		StringBuilder builder = new StringBuilder(1024); 
		
		for(int type = 0; type < 2; type++){
			int pixelIndex = 0;
			int[] a = this.silhouetteIndexArray[type];
			if(a == null){
				continue;
			}
			
			builder.append("################################################################\n");
			if(this.numSilhouettes[type] == 12){
				builder.append("OK: "+this.numSilhouettes[type]);
			}else{			
				builder.append("NG: "+this.numSilhouettes[type]);
			}
			builder.append("\n");
			
			for(int y = 0; y < BITMAP_HEIGHT; y++){
				for(int x = 0; x < BITMAP_WIDTH; x++){
					builder.append(getLabelChar(a[pixelIndex++]));
				}
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	
	final static String LABEL = ".0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	final static int LABEL_LENGTH = LABEL.length();

	private static char getLabelChar(int p) {
	    char c;
	    if(p < LABEL_LENGTH){
	    	c = LABEL.charAt(p);
	    }else{
	    	c = '*';
	    }
	    return c;
    }
}
