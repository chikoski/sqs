/**
 * The original ActionScript source is licensed under MIT License, available at http://wonderfl.net/c/k8MW.
 * This Java class is its translated version by Hiroya Kubo <hiroya@cuc.ac.jp>.
 * @author hiroya
 */
package net.sqs2.geom.impl;

class HomogeneousLinearEQSystem{
	    private int _dimension;
	    private MMatrix _mat;
	    private int[] _colInfo;
	        
	    HomogeneousLinearEQSystem(double[] array){
	        int n = (int)Math.floor(Math.sqrt(array.length));
	        _dimension = n;
	        _mat = MMatrixUtil.makeMatrixFromArray(array, n, n + 1);
	    }
	        
	    public void solve() {
	    	int i = 0, j = 0;
	    	int ii, ik;
	        double t, u;
	        double[] weight = new double[_dimension];
	        int[] colInfo = new int[_dimension + 1];
	        int[] rowInfo = new int[_dimension];
	            
	        for (int k = 0; k <= _dimension; k++){
	            colInfo[k] = k;
	        }
	        for (int k = 0; k < _dimension; k++) {
	            rowInfo[k] = k;
	                
	            u = 0;
	            for (j = 0; j <= _dimension; j++) {
	                t = _mat.getElementAt(k, j);
	                t = (t < 0) ? -t : t;
	                u = (u < t) ? t : u;
	            }
	            
	            weight[k] = 1 / u; // suppose u != 0
	        }
	            
	        for (int k = 0; k < _dimension; k++) {
	            u = Double.NEGATIVE_INFINITY;
	                
	            for (i = k; i < _dimension; i++) {
	                ii = rowInfo[i];
	                t = _mat.getElementAt(ii, k) * weight[ii];
	                t = (t < 0) ? -t : t;
	                if (t > u) {
	                    u = t;
	                    j = i;
	                }
	            }
	            ik = rowInfo[j];
	            if (j != k) {
	                rowInfo[j] = rowInfo[k];
	                rowInfo[k] = ik;
	            }
	            u = _mat.getElementAt(ik, k);
	                
	            if (u == 0) {
	                u = -1;
	                for (j = k + 1; j <= _dimension; j++) {
	                    t = _mat.getElementAt(ik, j);
	                    t = (t < 0) ? - t : t;
	                    if (u < t) {
	                        u = t;
	                        i = j;
	                    }
	                }
	                    
	                if (u == 0) {
	                    _mat = exchangeRows(_mat, rowInfo);
	                    _colInfo = colInfo;
	                    return;
	                }
	                j = colInfo[k];
	                colInfo[k] = colInfo[i];
	                colInfo[i] = j;
	                //
	                for (j = 0; j < _dimension; j++) {
	                    t = _mat.getElementAt(j, k);
	                    _mat.setElementAt(j, k, _mat.getElementAt(j, i));
	                    _mat.setElementAt(j, i, t);
	                }
	                    
	                u = _mat.getElementAt(ik, k);
	            }
	                
	            ik = rowInfo[k];
	            for (j = k; j <= _dimension; j++) {
	                t = _mat.getElementAt(ik, j) / u;
	                _mat.setElementAt(ik, j, t);
	            }
	                
	            for (i = 0; i < _dimension; i++) {
	                if (i != k) {
	                    ii = rowInfo[i];
	                    u = _mat.getElementAt(ii, k);
	                    for (j = k; j <= _dimension; j++) {
	                        t = _mat.getElementAt(ii, j) - _mat.getElementAt(ik, j) * u;
	                        _mat.setElementAt(ii, j, t);
	                    }
	                }
	            }
	            
	        }
	            
	        _mat = exchangeRows(_mat, rowInfo);
	        _colInfo = colInfo;
	    }
	        
	    public MMatrix matrix() {
	        return _mat;
	    }
	        
	    public int[] columnInfo() {
	        return _colInfo;
	    }
	        
	    private MMatrix exchangeRows(MMatrix matrix, int[] rowInfo) {
	        MMatrix copy = new MMatrix(_dimension, _dimension + 1);
	        int i, j, ii;
	            
	        for (i = 0; i < _dimension; i++) {
	            ii = rowInfo[i];
	                
	            for (j = 0; j < _dimension + 1; j++) {
	                copy.setElementAt(i, j, matrix.getElementAt(ii, j));
	            }
	        }
	        return copy;
	    }    
	}
