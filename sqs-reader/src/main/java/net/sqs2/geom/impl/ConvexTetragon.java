/**
 * The original ActionScript source is licensed under MIT License, available at http://wonderfl.net/c/k8MW.
 * This Java class is its translated version by Hiroya Kubo <hiroya@cuc.ac.jp>.
 * @author hiroya
 */

package net.sqs2.geom.impl;

import java.awt.Point;
import java.awt.Polygon;

public class ConvexTetragon extends Polygon{

	private static final long serialVersionUID = 1L;

	public ConvexTetragon() {
		super();
	}

	public ConvexTetragon(int[] xpoints, int[] ypoints, int npoints) {
		super(xpoints, ypoints, npoints);
	}
	
	public Point getPointAt(int index){
		return new Point(xpoints[index], ypoints[index]);
	}
}

