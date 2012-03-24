/*

 ImageSourceImpl.java

 Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2004/07/16

 */
package net.sqs2.omr.logic;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import net.sqs2.image.ImageTranslationFilter;
import net.sqs2.image.ImageUtil;

/**
 * @author hiroya
 * 
 */
public class PageSource extends ImageTranslationFilter{
	private static final float OVER_SAMPLING_FACTOR = 2.0f;
	private static AffineTransformOp operator;
	static{
		AffineTransform t = AffineTransform.getScaleInstance(1 / OVER_SAMPLING_FACTOR/* / this.scale*/, 1 / OVER_SAMPLING_FACTOR/* / this.scale*/);
		operator = new AffineTransformOp(t, new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR));
	}

	//private float scale = 1.0f;

	public PageSource(BufferedImage image, Point[] master, Point[] guide/*, float scale*/) {
		super(image, master, guide);
		//this.setScale(scale);
	}
	
	public Polygon createRectPolygon(Rectangle rect) {
		return createRectPolygon(rect.x, rect.y, rect.width, rect.height);
	}
	
	public Polygon createRectPolygon(float x, float y, float w, float h) {
		int _x = (int)x;
		int _y = (int)y;
		int _w = (int)w;
		int _h = (int)h;
		Point2D[] pointArray = new Point2D[]{getPoint(_x, _y), getPoint(_x+_w, _y), getPoint(_x+_w, _y+_h), getPoint(_x, _y+_h)};
		return createPolygon(pointArray);
	}

	public Polygon createPolygon(Point2D[] pointArray) {
		Polygon polygon = new Polygon();
		for(Point2D point: pointArray){
			polygon.addPoint((int)point.getX(), (int)point.getY());
		}
		polygon.addPoint((int)pointArray[0].getX(), (int)pointArray[0].getY());
		return polygon;
	}

	/*
	public void setScale(float scale){
		this.scale = scale;
		AffineTransform t = AffineTransform.getScaleInstance(1 / OVER_SAMPLING_FACTOR / this.scale, 1 / OVER_SAMPLING_FACTOR / this.scale);
		this.operator = new AffineTransformOp(t, new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR));
	}
	 */
	
	public BufferedImage cropImage(Rectangle rect) throws PageSourceException{
		return this.cropImage(rect.x, rect.y, rect.width, rect.height);
	}

	public BufferedImage cropImage(float x, float y, float w, float h) throws PageSourceException{
		BufferedImage image = new BufferedImage((int)(w * OVER_SAMPLING_FACTOR),
				(int)(h * OVER_SAMPLING_FACTOR),
				BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		Point2D.Float p = new Point2D.Float();
		int[] rgbarr = new int[3];
		for (int j = (int)(h * OVER_SAMPLING_FACTOR) - 1; 0 <= j; j--) {
			for (int i = (int)(w * OVER_SAMPLING_FACTOR) - 1; 0 <= i; i--) {
				try{
					ImageUtil.rgb2arr(getRGB(x + i / OVER_SAMPLING_FACTOR, y + j / OVER_SAMPLING_FACTOR, p), rgbarr);
					raster.setPixel(i, j, rgbarr);
				}catch(ArrayIndexOutOfBoundsException ex){
					throw new PageSourceException(null, image.getWidth(), image.getHeight(), x + i / OVER_SAMPLING_FACTOR, y + j / OVER_SAMPLING_FACTOR);
				}
			}
		}
		BufferedImage newImage = new BufferedImage((int)w, (int)h, BufferedImage.TYPE_INT_RGB);
		operator.filter(image, newImage);
		image.flush();
		return newImage;
	}

	/**
	 * @return grayscale value (0-255)
	 */
	public int getGrayscaleDensity(Rectangle rect, float resolutionScaleFactor) throws PageSourceException{
		 int total = 0;
		 Point2D p = new Point2D.Float();
		 float factor = OVER_SAMPLING_FACTOR * resolutionScaleFactor;
		 int w = (int)(rect.width * factor) - 1;
		 int h = (int)(rect.height * factor) - 1;
		 for (int i = w; 0 <= i; i--) {
			 for (int j = h ; 0 <= j; j--) {
				 try{
					 total += ImageUtil.rgb2gray(getRGB(rect.x + i / factor, rect.y + j / factor, p));
				 }catch(ArrayIndexOutOfBoundsException ex){
					 throw new PageSourceException(null, this.image.getWidth(), this.image.getHeight(), rect.x + i / factor, rect.y + j / factor);
				 }
			 }
		 }
		 return (int)(total / ((w+1)*(h+1)));
	 }
	
	/**
	 * @return grayscale value (0-255)
	 */
	public int getGrayscaleDensityWithHorizontalFilter(Rectangle rect, float resolutionScaleFactor) throws PageSourceException{
		 int total = 0;
		 int height = 0;
		 Point2D p = new Point2D.Float();
		 float factor = resolutionScaleFactor;
		 int w = (int)(rect.width * factor) - 1;
		 int h = (int)(rect.height * factor) - 1;
		 for (int j = h ; 0 <= j; j--) {
			 int horizontalTotal = 0;
			 for (int i = w; 0 <= i; i--) {
				 try{
					 horizontalTotal += ImageUtil.rgb2gray(getRGB(rect.x + i / factor, rect.y + j / factor, p));
				 }catch(ArrayIndexOutOfBoundsException ex){
					 throw new PageSourceException(null, this.image.getWidth(), this.image.getHeight(), rect.x + i / factor, rect.y + j / factor);
				 }
			 }
			 if(horizontalTotal < w * 200){
				 height++;
				 total += horizontalTotal;
			 }
		 }
		 if(height == 0){
			 return 255;
		 }else{
			 return (int)(total / (height * (w+1)));
		 }
	 }

	/**
	 * @return grayscale value (0-255)
	 */
	public int getGrayscaleDensityWithVerticalFilter(Rectangle rect, float resolutionScaleFactor) throws PageSourceException{
		 int total = 0;
		 int width = 0;
		 Point2D p = new Point2D.Float();
		 float factor = resolutionScaleFactor;
		 int w = (int)(rect.width * factor) - 1;
		 int h = (int)(rect.height * factor) - 1;

		 for (int i = w; 0 <= i; i--) {
			 int verticalTotal = 0;
			 for (int j = h ; 0 <= j; j--) {
				 try{
					 verticalTotal += ImageUtil.rgb2gray(getRGB(rect.x + i / factor, rect.y + j / factor, p));
				 }catch(ArrayIndexOutOfBoundsException ex){
					 throw new PageSourceException(null, this.image.getWidth(), this.image.getHeight(), rect.x + i / factor, rect.y + j / factor);
				 }
			 }
			 if(verticalTotal < h * 200){
				 width++;
				 total += verticalTotal;
			 }
		 }

		 if(width == 0){
			 return 255;
		 }else{
			 return (int)(total / (width * (h+1)));
		 }
	 }

	 public int getGrayscaleDensity(Rectangle rect) throws PageSourceException{
		 return getGrayscaleDensity(rect, 1.0f);
	 }
	 
	 final static int WHITE_VALUE = 255; 
	 final static int WINDOW_EDGE_LENGTH = 2; 
	 final static int WINDOW_SIZE = 1 + WINDOW_EDGE_LENGTH * 2; 
	 final static int WINDOW_AREA = WINDOW_SIZE * WINDOW_SIZE;
	 
	 public class MarkSenseResult{
		 int w, h;
		 char[][] matrix;
		 int[] histgram;
		 int value;
		 
		 void init(int w, int h){
			 this.w = w;
			 this.h = h;
			 matrix = new char[h][w];
			 histgram = new int[16];
		 }
		 
		 float getValueDensity(){
			 return value / 255.0f;
		 }
		 
		 public String toString(){
			 StringBuilder sb = new StringBuilder(64);
			 for (int y = WINDOW_EDGE_LENGTH ; y < h - WINDOW_EDGE_LENGTH; y++) {
				 for (int x = WINDOW_EDGE_LENGTH; x < w - WINDOW_EDGE_LENGTH; x++) {
					 sb.append(matrix[y][x]);
				 }
				 sb.append("\n");
			 }
			 for(int i=15; 0 <=i; i--){
				 sb.append("\t");
				 sb.append(histgram[i]);
			 }
			 sb.append("\n");
			 return sb.toString();
		 }
	 }
	 
	 final static int WINDOW_DENSITY_THRESHOLD_WHITE = 80; 
	 final static int WINDOW_DENSITY_THRESHOLD_BLACK = 50; 
	 
	 public int getGrayscaleDensityWithFilter2(Rectangle rect, float resolutionScaleFactor) throws PageSourceException{
		 int w = (int)(rect.width * resolutionScaleFactor);
		 int h = (int)(rect.height * resolutionScaleFactor);
		 MarkSenseResult result = new MarkSenseResult();
		 result.init(w,h);
		 int[] src = createMarkAreaSource(rect, w, h);
		 int total = 0;
		 for (int y = WINDOW_EDGE_LENGTH ; y < h - WINDOW_EDGE_LENGTH; y++) {
			 for (int x = WINDOW_EDGE_LENGTH; x < w - WINDOW_EDGE_LENGTH; x++) {
				 int p = y * w + x;
				 int pp = p - w;
				 int pn = p + w;
				 int value;
				 if(WINDOW_EDGE_LENGTH == 2){
					 value = src[pp - 2] + src[pp - 1] + src[pp] + src[pp + 1] + src[pp + 2] +
					   src[p - 2] + src[p - 1]  + src[p] + src[p + 1] + src[p + 2]  +
					   src[pn - 2] + src[pn - 1] + src[pn] + src[pn + 1] + src[pn + 2];
				 }else if(WINDOW_EDGE_LENGTH == 1){
					 value = src[pp - 1] + src[pp ] + src[pp + 1] +
					   src[p - 1]  + src[p] + src[p + 1] +
					   src[pn - 1] + src[pn] + src[pn + 1];
				 }
				 
				 result.histgram[value / WINDOW_AREA / 16]++;
				 
				 if(value < WINDOW_DENSITY_THRESHOLD_BLACK * WINDOW_AREA){
					 result.matrix[y][x] = '#';
				 }else if(value < WINDOW_DENSITY_THRESHOLD_WHITE * WINDOW_AREA){
					 result.matrix[y][x] = '+';
					 total += WHITE_VALUE * WINDOW_AREA ;
				 }else{
					 result.matrix[y][x] = '-';
					 total += WHITE_VALUE * WINDOW_AREA ;
					 //total += value;
				 }
			 }
		 }
		 System.err.println(result.toString());
		 int ret = (int)(total / ((w - WINDOW_EDGE_LENGTH * 2) * (h - WINDOW_EDGE_LENGTH * 2) * WINDOW_AREA));
		 System.err.println(ret/255.0f);
		 return ret;
	 }
	 
	 public int getGrayscaleDensityWithFilter(Rectangle rect, float noMarkThreshold, float resolutionScaleFactor) throws PageSourceException{
		 int w = (int)(rect.width * resolutionScaleFactor);
		 int h = (int)(rect.height * resolutionScaleFactor);
		 int[] src = createMarkAreaSource(rect, w, h);
		 int total = 0;
		 for (int y = WINDOW_EDGE_LENGTH ; y < h - WINDOW_EDGE_LENGTH; y++) {
			 for (int x = WINDOW_EDGE_LENGTH; x < w - WINDOW_EDGE_LENGTH; x++) {
				 int p = y * w + x;
				 int pp = p - w;
				 int pn = p + w;
				 int value;
				 if(WINDOW_EDGE_LENGTH == 2){
					 value = src[pp - 2] + src[pp - 1] + src[pp] + src[pp + 1] + src[pp + 2] +
					   src[p - 2] + src[p - 1]  + src[p] + src[p + 1] + src[p + 2]  +
					   src[pn - 2] + src[pn - 1] + src[pn] + src[pn + 1] + src[pn + 2];
				 }else if(WINDOW_EDGE_LENGTH == 1){
					 value = src[pp - 1] + src[pp ] + src[pp + 1] +
					   src[p - 1]  + src[p] + src[p + 1] +
					   src[pn - 1] + src[pn] + src[pn + 1];
				 }
				 
				 if(value < noMarkThreshold * WINDOW_AREA * 255){
				 }else{
					 total += WHITE_VALUE * WINDOW_AREA ;
				 }
			 }
		 }
		 return (int)(total / ((w - WINDOW_EDGE_LENGTH * 2) * (h - WINDOW_EDGE_LENGTH * 2) * WINDOW_AREA));
	 }
	 
	 private int[] createMarkAreaSource(Rectangle rect, int w, int h) throws PageSourceException{
		 Point2D p = new Point2D.Float();
		 int[] src = new int[w * h];
		 for (int y = h - 1 ; 0 <= y; y--) {
			 float yy = rect.y + rect.height * y / (float)h;
			 for (int x = w - 1; 0 <= x; x--) {
				 try{
					 src[y * w + x] = ImageUtil.rgb2gray(getRGB(rect.x + rect.width * x  / (float)w, yy, p));
				 }catch(ArrayIndexOutOfBoundsException ex){
					 throw new PageSourceException(null, this.image.getWidth(), this.image.getHeight(), rect.x + rect.width * x  / (float)w, yy);
				 }
			 }
		 }
		 return src;
	 }

}
