package net.sqs2.geom.impl;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import net.sqs2.geom.ProjectionTranslator;


public class ProjectionTranslatorImpl implements ProjectionTranslator {

	private Point2D[] deskewMasterPoints;
	private Point2D[] deskewImagePoints;
	private Point2D[] deskewTranslatedPoints;

	private double masterWidth;
	private double masterHeight;
	private double ox;
	private double oy;
	private double ax;
	private double ay;
	private double bx;
	private double by;
	private double cx;
	private double cy;
	private double dx;
	private double dy;

	public ProjectionTranslatorImpl() {
		super();
	}

	public ProjectionTranslatorImpl(Point2D[] deskewMasterPoints, Point2D[] deskewImagePoints) {
		this.deskewMasterPoints = deskewMasterPoints;
		this.deskewImagePoints = deskewImagePoints;
		this.deskewTranslatedPoints = new Point2D[4];
		init();
	}

	public Point2D[] getDeskewTranslatedPoints() {
		return this.deskewTranslatedPoints;
	}

	public Point2D[] getDeskewImagePoints() {
		return this.deskewImagePoints;
	}

	public Point2D[] getDeskewMasterPoints() {
		return this.deskewMasterPoints;
	}
	
	static final int ALGORITHM = 0;

	private void init() {
		this.masterWidth = this.deskewMasterPoints[1].getX() - this.deskewMasterPoints[0].getX();
		this.masterHeight = this.deskewMasterPoints[2].getY() - this.deskewMasterPoints[0].getY();
		this.ox = this.deskewMasterPoints[0].getX();
		this.oy = this.deskewMasterPoints[0].getY();

		this.deskewTranslatedPoints[0] = this.deskewImagePoints[0];
		this.deskewTranslatedPoints[1] = this.deskewImagePoints[1];
		switch(ALGORITHM){
			case 0:
			this.deskewTranslatedPoints[2] = this.deskewImagePoints[2];
			this.deskewTranslatedPoints[3] = this.deskewImagePoints[3];
			break;
			case 1:
			double cxs = (this.deskewImagePoints[3].getX() - this.deskewImagePoints[2].getX())
			* (this.deskewMasterPoints[0].getX() - this.deskewMasterPoints[2].getX()) / this.masterWidth;
			double cys = (this.deskewImagePoints[3].getY() - this.deskewImagePoints[2].getY())
			* (this.deskewMasterPoints[0].getX() - this.deskewMasterPoints[2].getX()) / this.masterWidth;
			this.deskewTranslatedPoints[2] = new Point2D.Double(this.deskewImagePoints[2].getX() + cxs, this.deskewImagePoints[2].getY() + cys);
			this.deskewTranslatedPoints[3] = new Point2D.Double(this.deskewImagePoints[3].getX() + cxs, this.deskewImagePoints[3].getY() + cys);
			break;
		}
		
		setABCD();
	}

	private void setABCD() {
		this.ax = this.deskewTranslatedPoints[1].getX() - this.deskewTranslatedPoints[0].getX();
		this.ay = this.deskewTranslatedPoints[1].getY() - this.deskewTranslatedPoints[0].getY();
		this.bx = this.deskewTranslatedPoints[2].getX() - this.deskewTranslatedPoints[0].getX();
		this.by = this.deskewTranslatedPoints[2].getY() - this.deskewTranslatedPoints[0].getY();
		this.cx = this.deskewTranslatedPoints[3].getX() - this.deskewTranslatedPoints[2].getX();
		this.cy = this.deskewTranslatedPoints[3].getY() - this.deskewTranslatedPoints[2].getY();
		this.dx = this.deskewTranslatedPoints[3].getX() - this.deskewTranslatedPoints[1].getX();
		this.dy = this.deskewTranslatedPoints[3].getY() - this.deskewTranslatedPoints[1].getY();
	}

	public Point2D getPoint(final float x, final float y, Point2D ret) {
		double s = ((x - this.ox) / this.masterWidth);
		double t = ((y - this.oy) / this.masterHeight);
		double aax = this.deskewTranslatedPoints[0].getX() + this.ax * s;
		double aay = this.deskewTranslatedPoints[0].getY() + this.ay * t;
		double bbx = this.deskewTranslatedPoints[0].getX() + this.bx * s;
		double bby = this.deskewTranslatedPoints[0].getY() + this.by * t;
		double acx = this.deskewTranslatedPoints[2].getX() + this.cx * s - aax;
		double acy = this.deskewTranslatedPoints[2].getY() + this.cy * t - aay;
		double bdx = this.deskewTranslatedPoints[1].getX() + this.dx * s - bbx;
		double bdy = this.deskewTranslatedPoints[1].getY() + this.dy * t - bby;

		double b = acy * bdx - acx * bdy;
		if (acx == 0 || b == 0) {
			ret.setLocation(aax, bby);
		} else {
			double q = (acx * (bby - aay) - acy * (bbx - aax)) / b;
			double p = (bbx + bdx * q - aax) / acx;
			ret.setLocation(aax + acx * p, aay + acy * p);
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see net.sqs2.image.ProjectionTranlator#getPoint(int, int)
	 */
	@Override
	public Point2D getPoint(final int x, final int y) {
		return this.getPoint((float) x, (float) y, new Point2D.Float());
	}

	@Override
	public Point2D getPoint(int x, int y, Point2D p) {
		return this.getPoint((float) x, (float) y, p);
	}

	@Override
	public Point2D getPoint(int x, int y, Point p) {
		return this.getPoint((float) x, (float) y, p);
	}


	public void translateTo(Point2D[] deskewTranslatedPoints, Rectangle rect) {
		deskewTranslatedPoints[0] = getPoint(rect.x, rect.y, deskewTranslatedPoints[0]);
		deskewTranslatedPoints[1] = getPoint(rect.x + rect.width, rect.y, deskewTranslatedPoints[1]);
		deskewTranslatedPoints[2] = getPoint(rect.x, rect.y + rect.height, deskewTranslatedPoints[2]);
		deskewTranslatedPoints[3] = getPoint(rect.x + rect.width, rect.y + rect.height, deskewTranslatedPoints[3]);
	}

	public Point2D[] translate(Rectangle rect) {
		Point2D[] deskewTranslatedPoints = new Point2D[4];
		deskewTranslatedPoints[0] = getPoint(rect.x, rect.y, new Point2D.Float());
		deskewTranslatedPoints[1] = getPoint(rect.x + rect.width, rect.y, new Point2D.Float());
		deskewTranslatedPoints[2] = getPoint(rect.x, rect.y + rect.height, new Point2D.Float());
		deskewTranslatedPoints[3] = getPoint(rect.x + rect.width, rect.y + rect.height, new Point2D.Float());
		return deskewTranslatedPoints;
	}

}
