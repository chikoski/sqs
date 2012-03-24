/**
 * The original ActionScript source is licensed under MIT License, available at http://wonderfl.net/c/k8MW.
 * This Java class is its translated version by Hiroya Kubo <hiroya@cuc.ac.jp>.
 * @author hiroya
 */

package net.sqs2.geom.impl;

class MMatrixUtil
{        
    public static MMatrix makeMatrixFromArray(double[] array, int row, int column) {
        MMatrix mat = new MMatrix(row, column);
        int i, j;
            
            
        for (i = 0; i < row; i++) {
            for (j = 0; j < column; j++) {
                mat.setElementAt(i, j, array[i * column + j]);
            }
        }
        return mat;
    }
}