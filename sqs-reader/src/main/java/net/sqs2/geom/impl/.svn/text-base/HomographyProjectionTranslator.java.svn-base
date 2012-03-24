/**
 * The original ActionScript source is licensed under MIT License, available at http://wonderfl.net/c/k8MW.
 * This Java class is its translated version by Hiroya Kubo <hiroya@cuc.ac.jp>.
 * @author hiroya
 */

package net.sqs2.geom.impl;

import java.awt.Point;
import java.awt.geom.Point2D;

import net.sqs2.geom.ProjectionTranslator;


public class HomographyProjectionTranslator implements ProjectionTranslator{

		ProjectionMatrix _pm;
		ConvexTetragon _ctDomain;
		ConvexTetragon _ctRegion;
		
		public HomographyProjectionTranslator(Point[] src, Point[] dst){
			this(new ConvexTetragon(
					new int[]{src[0].x, src[1].x, src[2].x, src[3].x},
					new int[]{src[0].y, src[1].y, src[2].y, src[3].y}, 4),
				new ConvexTetragon(
						new int[]{dst[0].x, dst[1].x, dst[2].x, dst[3].x},
						new int[]{dst[0].y, dst[1].y, dst[2].y, dst[3].y}, 4));
		}

		public HomographyProjectionTranslator(Point2D[] src, Point2D[] dst){
			this(new ConvexTetragon(
					new int[]{(int)src[0].getX(), (int)src[1].getX(), (int)src[2].getX(), (int)src[3].getX()},
					new int[]{(int)src[0].getY(), (int)src[1].getY(), (int)src[2].getY(), (int)src[3].getY()}, 4),
					
					new ConvexTetragon(
							new int[]{(int)dst[0].getX(), (int)dst[1].getX(), (int)dst[2].getX(), (int)dst[3].getX()},
							new int[]{(int)dst[0].getY(), (int)dst[1].getY(), (int)dst[2].getY(), (int)dst[3].getY()}, 4));
		}

		public HomographyProjectionTranslator(ConvexTetragon src, ConvexTetragon dst){
			_pm = new ProjectionMatrix();
			_ctDomain = src;
			_ctRegion = dst;
			calculateProjectionMatrix(null);
		}

		private void calculateProjectionMatrix(Point p){
	        setProjectionDomain(_ctDomain);
	        setProjectionRegion(_ctRegion);
	        _pm.calculateProjectionMatrix();
	    }

		 private void setProjectionDomain(ConvexTetragon domain){
			 Point p;
	         p = domain.getPointAt(0);
	         _pm.setDomainA(p.x, p.y);
	         p = domain.getPointAt(1);
	         _pm.setDomainB(p.x, p.y);
	         p = domain.getPointAt(2);
	         _pm.setDomainC(p.x, p.y);
	         p = domain.getPointAt(3);
	         _pm.setDomainD(p.x, p.y);            
	     }
	     
	     private void setProjectionRegion(ConvexTetragon region){
	    	 Point p;
	         
	         p = region.getPointAt(0);
	         _pm.setRegionA(p.x, p.y);
	         p = region.getPointAt(1);
	         _pm.setRegionB(p.x, p.y);
	         p = region.getPointAt(2);
	         _pm.setRegionC(p.x, p.y);
	         p = region.getPointAt(3);
	         _pm.setRegionD(p.x, p.y);
	     }
	     
		@Override
		public Point2D getPoint(int x, int y) {
			Point2D ret = _pm.convert(new Point2D.Double(x,y));
			return ret;
		}

		@Override
		public Point2D getPoint(int x, int y, Point2D p) {
			p.setLocation(x, y);
			Point2D ret = _pm.convert(p);
			return ret;
		}

		@Override
		public Point2D getPoint(int x, int y, Point p) {
			p.setLocation(x, y);
			Point2D ret = _pm.convert(p);
			return ret;
		}
		
	}