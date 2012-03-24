/**
 * The original ActionScript source is licensed under MIT License, available at http://wonderfl.net/c/k8MW.
 * This Java class is its translated version by Hiroya Kubo <hiroya@cuc.ac.jp>.
 * @author hiroya
 */

package net.sqs2.geom.impl;

import java.awt.geom.Point2D;

class ProjectionMatrix{
    private double _dx0;
    private double _dy0;
    private double _dx1;
    private double _dy1;
    private double _dx2;
    private double _dy2;
    private double _dx3;
    private double _dy3;
        
    private double _rx0;
    private double _ry0;
    private double _rx1;
    private double _ry1;
    private double _rx2;
    private double _ry2;
    private double _rx3;
    private double _ry3;
        
    private double[] _coefficients;
        
    private double _a0;
    private double _b0;
    private double _c0;
        
    private double _a1;
    private double _b1;
    private double _c1;
        
    private double _a2;
    private double _b2;
    private double _c2;
        
    public ProjectionMatrix() {
        
    }
        
    public void setDomainA(double x, double y) {
        _dx0 = x;
        _dy0 = y;
    }
        
    public void setDomainB(double x, double y) {
        _dx1 = x;
        _dy1 = y;
    }
        
    public void setDomainC(double x, double y) {
        _dx2 = x;
        _dy2 = y;
    }
        
    public void setDomainD(double x, double y) {
        _dx3 = x;
        _dy3 = y;
    }
        
    public void setRegionA(double x, double y) {
        _rx0 = x;
        _ry0 = y;
    }
        
    public void setRegionB(double x, double y) {
        _rx1 = x;
        _ry1 = y;
    }

    public void setRegionC(double x, double y) {
        _rx2 = x;
        _ry2 = y;
    }
        
    public void setRegionD(double x, double y) {
        _rx3 = x;
        _ry3 = y;
    }
        
    public void calculateProjectionMatrix() {
        double[] hleqs = new double[]{
            _dx0 * _rx0, _dy0 * _rx0, _rx0, -_dx0, -_dy0, -1, 0, 0, 0,
            _dx1 * _rx1, _dy1 * _rx1, _rx1, -_dx1, -_dy1, -1, 0, 0, 0,
            _dx2 * _rx2, _dy2 * _rx2, _rx2, -_dx2, -_dy2, -1, 0, 0, 0,
            _dx3 * _rx3, _dy3 * _rx3, _rx3, -_dx3, -_dy3, -1, 0, 0, 0,
            _dx0 * _ry0, _dy0 * _ry0, _ry0, 0, 0, 0, -_dx0, -_dy0, -1,
            _dx1 * _ry1, _dy1 * _ry1, _ry1, 0, 0, 0, -_dx1, -_dy1, -1,
            _dx2 * _ry2, _dy2 * _ry2, _ry2, 0, 0, 0, -_dx2, -_dy2, -1,
            _dx3 * _ry3, _dy3 * _ry3, _ry3, 0, 0, 0, -_dx3, -_dy3, -1,
        };
            
        HomogeneousLinearEQSystem solver = new HomogeneousLinearEQSystem(hleqs);
            
        _coefficients = new double[9];
        solver.solve();
        int[] conInfo = solver.columnInfo();
        MMatrix solution = solver.matrix();
        int ii;
        for (int i = 0; i < solution.row(); i++) {
            ii = conInfo[i];
            _coefficients[ii] = - solution.getElementAt(i, solution.column() - 1);
        }
        _coefficients[conInfo[solution.column() - 1]] = 1;
            
        _a0 = _coefficients[0];
        _b0 = _coefficients[1];
        _c0 = _coefficients[2];
        _a1 = _coefficients[3];
        _b1 = _coefficients[4];
        _c1 = _coefficients[5];
        _a2 = _coefficients[6];
        _b2 = _coefficients[7];
        _c2 = _coefficients[8];
    }
        
    public Point2D convert(Point2D point) {
	 double x = point.getX();
	 double y = point.getY();
        return new Point2D.Double(
            (_a1 * x + _b1 * y + _c1) / (_a0 * x + _b0 * y + _c0),
            (_a2 * x + _b2 * y + _c2) / (_a0 * x + _b0 * y + _c0)
        );
    }
}
