/*

 PageSourceImpl.java

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
package net.sqs2.omr.app.deskew;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import net.sqs2.image.ImageUtil;

/**
 * @author hiroya
 * 
 */
public class DeskewedImageSource extends ImageTranslationFilter {
	final static float OVER_SAMPLING_FACTOR = 2.0f;
	
	final static int NUM_PIXELS_IN_CONVOLUTION = 15;
	final static int WHITE_VALUE = 255;
	
	private static AffineTransformOp operator;
	static {
		AffineTransform transform = AffineTransform.getScaleInstance(1 / OVER_SAMPLING_FACTOR, 1 / OVER_SAMPLING_FACTOR);
		operator = new AffineTransformOp(transform, new RenderingHints(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR));
	}

	public DeskewedImageSource(BufferedImage image, Point2D[] deskewMasterPoints, Point2D[] deskewGuidePoints) {
		super(image, deskewMasterPoints, deskewGuidePoints);
	}

	public Polygon createRectPolygon(Rectangle rect) {
		return createRectPolygon(rect.x, rect.y, rect.width, rect.height);
	}

	public Polygon createRectPolygon(float _x, float _y, float _w, float _h) {
		int x = (int) _x;
		int y = (int) _y;
		int w = (int) _w;
		int h = (int) _h;
		Point2D[] pointArray = new Point2D[] { getPoint(x, y), 
				getPoint(x + w, y),
				getPoint(x + w, y + h), 
				getPoint(x, y + h) };
		return createPolygon(pointArray);
	}

	public Polygon createPolygon(Point2D[] pointArray) {
		Polygon polygon = new Polygon();
		for (Point2D point : pointArray) {
			polygon.addPoint((int) point.getX(), (int) point.getY());
		}
		polygon.addPoint((int) pointArray[0].getX(), (int) pointArray[0].getY());
		return polygon;
	}

	public BufferedImage cropImage(Rectangle rect) throws PageImageSourceException {
		return this.cropImage(rect.x, rect.y, rect.width, rect.height);
	}

	public BufferedImage cropImage(float x, float y, float w, float h) throws PageImageSourceException {
		BufferedImage image = new BufferedImage((int) (w * OVER_SAMPLING_FACTOR),
				(int) (h * OVER_SAMPLING_FACTOR), BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		Point2D.Float p = new Point2D.Float();
		int[] rgbarr = new int[3];
		for (int j = (int) (h * OVER_SAMPLING_FACTOR) - 1; 0 <= j; j--) {
			for (int i = (int) (w * OVER_SAMPLING_FACTOR) - 1; 0 <= i; i--) {
				try {
					ImageUtil.rgb2arr(getRGB(x + i / OVER_SAMPLING_FACTOR, y + j / OVER_SAMPLING_FACTOR, p),
							rgbarr);
					raster.setPixel(i, j, rgbarr);
				} catch (ArrayIndexOutOfBoundsException ex) {
					throw new PageImageSourceException(null, image.getWidth(), image.getHeight(), x + i
							/ OVER_SAMPLING_FACTOR, y + j / OVER_SAMPLING_FACTOR);
				}
			}
		}
		BufferedImage newImage = new BufferedImage((int) w, (int) h, BufferedImage.TYPE_INT_RGB);
		operator.filter(image, newImage);
		image.flush();
		return newImage;
	}

	/**
	 * @return grayscale value (0-255)
	 */
	public int getGrayscaleDensity(Rectangle2D rect, float resolutionScaleFactor) throws PageImageSourceException {
		int total = 0;
		Point2D p = new Point2D.Float();
		float factor = OVER_SAMPLING_FACTOR * resolutionScaleFactor;
		int w = (int) (rect.getWidth() * factor) - 1;
		int h = (int) (rect.getHeight() * factor) - 1;
		for (int i = w; 0 <= i; i--) {
			for (int j = h; 0 <= j; j--) {
				try {
					total += ImageUtil.rgb2gray(getRGB( (int)(rect.getX() + i / factor), (int)(rect.getY() + j / factor), p));
				} catch (ArrayIndexOutOfBoundsException ex) {
					/*
					throw new PageImageSourceException(null, (int)this.image.getWidth(), (int)this.image.getHeight(), (int)(rect.getX()
							+ i / factor), (int)(rect.getY() + j / factor));
							*/
				}
			}
		}
		return (total / ((w + 1) * (h + 1)));
	}

	/**
	 * @return grayscale value (0-255)
	 */
	public int calcMarkAreaDensityWithHorizontalSlices(Rectangle rect, float densityThreshold, float resolutionScaleFactor) throws PageImageSourceException {
		int total = 0;
		int height = 0;
		Point2D p = new Point2D.Float();
		float factor = resolutionScaleFactor;
		int w = (int) (rect.width * factor) - 1;
		int h = (int) (rect.height * factor) - 1;
		for (int j = h; 0 <= j; j--) {
			int horizontalTotal = 0;
			for (int i = w; 0 <= i; i--) {
				try {
					horizontalTotal += ImageUtil
							.rgb2gray(getRGB(rect.x + i / factor, rect.y + j / factor, p));
				} catch (ArrayIndexOutOfBoundsException ex) {
					throw new PageImageSourceException(null, this.image.getWidth(), this.image.getHeight(), rect.x
							+ i / factor, rect.y + j / factor);
				}
			}
			if (horizontalTotal < w * densityThreshold * WHITE_VALUE) {
				height++;
				total += horizontalTotal;
			}
		}
		if (height == 0) {
			return WHITE_VALUE;
		} else {
			return (total / (height * (w + 1)));
		}
	}

	/**
	 * @return grayscale value (0-255)
	 */
	public int calcMarkAreaDensityWithVerticalSlices(Rectangle rect, float densityThreshold, float resolutionScaleFactor) throws PageImageSourceException {
		int total = 0;
		int width = 0;
		Point2D p = new Point2D.Float();
		float factor = resolutionScaleFactor;
		int w = (int) (rect.width * factor) - 1;
		int h = (int) (rect.height * factor) - 1;

		for (int i = w; 0 <= i; i--) {
			int verticalTotal = 0;
			for (int j = h; 0 <= j; j--) {
				try {
					verticalTotal += ImageUtil.rgb2gray(getRGB(rect.x + i / factor, rect.y + j / factor, p));
				} catch (ArrayIndexOutOfBoundsException ex) {
					throw new PageImageSourceException(null, this.image.getWidth(), this.image.getHeight(), rect.x
							+ i / factor, rect.y + j / factor);
				}
			}
			if (verticalTotal < h * densityThreshold * WHITE_VALUE) {
				width++;
				total += verticalTotal;
			}
		}

		if (width == 0) {
			return WHITE_VALUE;
		} else {
			return (total / (width * (h + 1)));
		}
	}

	public int calcMarkAreaDensity(Rectangle2D rect) throws PageImageSourceException {
		return getGrayscaleDensity(rect, 1.0f);
	}

	public class MarkSenseResult {
		int w, h;
		char[][] pixels;
		int value;

		MarkSenseResult(int w, int h) {
			this.w = w;
			this.h = h;
			this.pixels = new char[h][w];
		}

		float getValueDensity() {
			return (float)this.value / WHITE_VALUE;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(64);
			sb.append("  ");
			for (int x = 0; x < this.w; x++) {
				sb.append(x%10);
			}
			sb.append("\n");
			for (int y = 0; y < this.h; y++) {
				sb.append(y%10);
				sb.append(' ');
				for (int x = 0; x < this.w; x++) {
					sb.append(this.pixels[y][x]);
				}
				sb.append("\n");
			}
			return sb.toString();
		}
	}

	/**
	 *           -2         -1        0         +1         +2
	 *     +------------+---------+---------+---------+------------+
	 *  -w |farNorthWest|northWest|  north  |northEast|farNorthEast| 
	 *     +------------+---------+---------+---------+------------+
	 *   0 |   farWest  |   West  |  CENTER |  East   |   farEast  |
	 *     +------------+---------+---------+---------+------------+
	 *  +w |farSouthWest|southWest|  south  |southEast|farSouthEast|
	 *     +------------+---------+---------+---------+------------+
	 *  
	 * @param rect
	 * @param densityThreshold
	 * @param resolutionScaleFactor
	 * @return
	 * @throws PageImageSourceException
	 */
	public int calcConvolution5x3AverageMarkAreaDensityWithDebugOut(Rectangle rect, float densityThreshold, float resolutionScaleFactor) throws PageImageSourceException {
		
		int w = (int) (rect.width * resolutionScaleFactor);
		int h = (int) (rect.height * resolutionScaleFactor);

		MarkSenseResult result = new MarkSenseResult(w - 4, h - 2); // for debug

		int[] bitmapSource = createSamplingBitmapOfMarkArea(rect, w, h);
		int totalValueOfMarkAreaPixels = 0;
		for (int y = 1; y < h - 1; y++) {
			for (int x = 2; x < w - 2; x++) {
				int centerIndex = x + y * w;
				int northIndex = centerIndex - w;
				int southIndex = centerIndex + w;
				int totalValueOf5x3ConvolutionPixels = 
					bitmapSource[northIndex - 2] +  // farNorthWest
					bitmapSource[northIndex - 1] +  // northWest
					bitmapSource[northIndex] +      // north 
					bitmapSource[northIndex + 1] +  // northEast
					bitmapSource[northIndex + 2] +  // farNorthEast
					bitmapSource[centerIndex - 2] + // farWest
					bitmapSource[centerIndex - 1] + // east
					bitmapSource[centerIndex] +     // center
					bitmapSource[centerIndex + 1] + // east
					bitmapSource[centerIndex + 2] + // farEast
					bitmapSource[southIndex - 2] +  // farSouthWest
					bitmapSource[southIndex - 1] +  // southWest
					bitmapSource[southIndex] +      // south
					bitmapSource[southIndex + 1] +  // southEast
					bitmapSource[southIndex + 2];   // farSouthEast
				
				if ((float) totalValueOf5x3ConvolutionPixels < densityThreshold * WHITE_VALUE * NUM_PIXELS_IN_CONVOLUTION) {
					result.pixels[y - 1][x - 2] = '#';
				}else if (bitmapSource[centerIndex] < densityThreshold * WHITE_VALUE) {
					result.pixels[y - 1][x - 2] = '/';
					totalValueOfMarkAreaPixels += WHITE_VALUE * NUM_PIXELS_IN_CONVOLUTION;
				} else {
					result.pixels[y - 1][x - 2] = '-';
					totalValueOfMarkAreaPixels += WHITE_VALUE * NUM_PIXELS_IN_CONVOLUTION;
				}
			}
		}
		
		float ret = (totalValueOfMarkAreaPixels / ((w - 4) * (h - 2) * NUM_PIXELS_IN_CONVOLUTION));

		System.err.print(result.toString());
		System.err.println("================ "+ret / WHITE_VALUE);

		return (int)ret;
	}


	/**
	 *           -2         -1        0         +1         +2
	 *     +------------+---------+---------+---------+------------+
	 *  -w |farNorthWest|northWest|  north  |northEast|farNorthEast| 
	 *     +------------+---------+---------+---------+------------+
	 *   0 |   farWest  |   West  |  CENTER |  East   |   farEast  |
	 *     +------------+---------+---------+---------+------------+
	 *  +w |farSouthWest|southWest|  south  |southEast|farSouthEast|
	 *     +------------+---------+---------+---------+------------+
	 *  
	 * @param rect
	 * @param densityThreshold
	 * @param resolutionScaleFactor
	 * @return
	 * @throws PageImageSourceException
	 */
	public int calcConvolution5x3AverageMarkAreaDensity(Rectangle rect, float densityThreshold, float resolutionScaleFactor) throws PageImageSourceException {
		
		int w = (int) (rect.width * resolutionScaleFactor);
		int h = (int) (rect.height * resolutionScaleFactor);
		int[] bitmapSource = createSamplingBitmapOfMarkArea(rect, w, h);
		int totalValueOfMarkAreaPixels = 0;
		
		for (int y = 1; y < h - 1; y++) {
			for (int x = 2; x < w - 2; x++) {
				int centerIndex = x + y * w;
				int northIndex = centerIndex - w;
				int southIndex = centerIndex + w;
				int totalValueOf5x3ConvolutionPixels = 
					bitmapSource[northIndex - 2] +  // farNorthWest
					bitmapSource[northIndex - 1] +  // northWest
					bitmapSource[northIndex] +      // north 
					bitmapSource[northIndex + 1] +  // northEast
					bitmapSource[northIndex + 2] +  // farNorthEast
					bitmapSource[centerIndex - 2] + // farWest
					bitmapSource[centerIndex - 1] + // east
					bitmapSource[centerIndex] +     // center
					bitmapSource[centerIndex + 1] + // east
					bitmapSource[centerIndex + 2] + // farEast
					bitmapSource[southIndex - 2] +  // farSouthWest
					bitmapSource[southIndex - 1] +  // southWest
					bitmapSource[southIndex] +      // south
					bitmapSource[southIndex + 1] +  // southEast
					bitmapSource[southIndex + 2];   // farSouthEast

				if ((float) totalValueOf5x3ConvolutionPixels < densityThreshold * WHITE_VALUE * NUM_PIXELS_IN_CONVOLUTION) {
					// totalValueOfMarkAreaPixels += 0; // black pixel
				} else {
					totalValueOfMarkAreaPixels += WHITE_VALUE * NUM_PIXELS_IN_CONVOLUTION; // white pixel
				}
			}
		}
		return (totalValueOfMarkAreaPixels / ((w - 4) * (h - 2)* NUM_PIXELS_IN_CONVOLUTION));
	}

	private int[] createSamplingBitmapOfMarkArea(Rectangle rect, int w, int h) throws PageImageSourceException {
		Point2D p = new Point2D.Float();
		int[] src = new int[w * h];
		for (int y = h - 1; 0 <= y; y--) {
			int base = y * w;
			float yy = rect.y + rect.height * y / (float) h;
			for (int x = w - 1; 0 <= x; x--) {
				try {
					src[base + x] = ImageUtil.rgb2gray(getRGB(rect.x + rect.width * x / (float) w, yy, p));
				} catch (ArrayIndexOutOfBoundsException ex) {
					throw new PageImageSourceException(null, this.image.getWidth(), this.image.getHeight(), rect.x
							+ rect.width * x / (float) w, yy);
				}
			}
		}
		return src;
	}

}
