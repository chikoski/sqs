/**
 * The original ActionScript source is licensed under MIT License, available at http://wonderfl.net/c/k8MW.
 * This Java class is its translated version by Hiroya Kubo <hiroya@cuc.ac.jp>.
 * @author hiroya
 */

package net.sqs2.geom.impl;

class MMatrix {
	    int _column;
	    int _row;
	    double[] _arr;
	        
	    public MMatrix(int row, int column){
	        _row = row;
	        _column = column;
	        _arr = new double[row*column];
	    }
	        
	    public int column(){
	        return _column;
	    }
	        
	    public int row() {
	        return _row;
	    }
	        
	    public double getElementAt(int row, int column){
	        return _arr[row * _column + column];
	    }
	        
	    public void setElementAt(int row, int column, double value) {
	        _arr[row * _column + column] = value;
	    }
	}
