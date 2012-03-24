/*
 * 

 ImageTranslationFilter.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package net.sqs2.omr.app.deskew;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import net.sqs2.geom.ProjectionTranslator;
import net.sqs2.geom.impl.HomographyProjectionTranslator;

public class ImageTranslationFilter{

	protected ProjectionTranslator pt;

	protected Point2D[] deskewMasterPoints;
	protected Point2D[] deskewImagePoints;
	protected BufferedImage image;

	public ImageTranslationFilter(BufferedImage image, Point2D[] deskewMasterPoints, Point2D[] deskewImagePoints) {
		pt = new HomographyProjectionTranslator(deskewMasterPoints, deskewImagePoints);
		this.deskewMasterPoints = deskewMasterPoints;
		this.deskewImagePoints = deskewImagePoints;
		this.image = image;
	}
	
	public Point2D[] getDeskewMasterPoints(){
		return this.deskewMasterPoints;
	}
	
	public Point2D[] getDeskewImagePoints(){
		return this.deskewImagePoints;
	}

	public Point2D getPoint(final int x, final int y) {
		Point2D p = this.pt.getPoint(x, y);
		return new Point((int)p.getX(), (int)p.getY());
	}

	public int getRGB(final int x, final int y, Point2D p) {
		Point2D _p = this.pt.getPoint( x, y, p);
		return this.image.getRGB((int) _p.getX(), (int) _p.getY());
	}

	public int getRGB(final float x, final float y, Point2D p) {
		Point2D _p = this.pt.getPoint((int) x, (int) y, p);
		try {
			return this.image.getRGB((int) _p.getX(), (int) _p.getY());
		} catch (ArrayIndexOutOfBoundsException ignore) {
			return 0;
		}
	}

	public void flush() {
		this.image.flush();
	}

}
