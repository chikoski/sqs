package net.sqs2.geom.impl;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.sqs2.geom.ProjectionTranslator;



public class InteractiveProjectionTranslatorDemo {

	public static final int _WIDTH = 1000;
	public static final int _HEIGHT = 600;
	
	Model m;
	View v;

	class Model{
		int state = 0;
		final Point[] srcP = new Point[4];
		final Point[] dstP = new Point[4];
		
		final int[] srcPolyX = new int[5];
		final int[] srcPolyY = new int[5];
		final int[] dstPolyX = new int[5];
		final int[] dstPolyY = new int[5];
		
		Polygon srcPoly;
		Polygon dstPoly;
		
		Point src = null;
		Point dst = null;
		
		ProjectionTranslator pt;
	}
	
	class View{
		JPanel panel;
		JFrame frame;
		View(){

		panel = new JPanel(){
			private static final long serialVersionUID = 1L;
			
			private void drawLine(Graphics2D g2d, double x1, double y1, double x2, double y2){
				g2d.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
			}
			
			private void fillOval(Graphics2D g2d, double x, double y, int w, int h){
				g2d.fillOval((int)x, (int)y, w, h);
			}
			
			public void paint(Graphics g){
				Graphics2D g2d = (Graphics2D) g;
				
				g2d.setColor(Color.darkGray);
				g2d.fillRect(0, 0, _WIDTH, _HEIGHT);
				
				g2d.setColor(Color.blue);
				switch(m.state){
				case 8:
				case 7:
				case 6:
				case 5:
				case 4:
					drawLine(g2d, m.srcP[3].getX(), m.srcP[3].getY(), m.srcP[0].getX(), m.srcP[0].getY());
					drawLine(g2d, m.srcP[2].getX(), m.srcP[2].getY(), m.srcP[3].getX(), m.srcP[3].getY());
				case 3:
					drawLine(g2d, m.srcP[1].getX(), m.srcP[1].getY(), m.srcP[2].getX(), m.srcP[2].getY());
				case 2:
					drawLine(g2d, m.srcP[0].getX(), m.srcP[0].getY(), m.srcP[1].getX(), m.srcP[1].getY());
				case 1:
				case 0:
				default:
					break;
				}
				g2d.setColor(Color.red);
				switch(m.state){
				case 8:					
					drawLine(g2d, m.dstP[3].getX(), m.dstP[3].getY(), m.dstP[0].getX(), m.dstP[0].getY());
					drawLine(g2d, m.dstP[2].getX(), m.dstP[2].getY(), m.dstP[3].getX(), m.dstP[3].getY());
				case 7:
					drawLine(g2d, m.dstP[1].getX(), m.dstP[1].getY(), m.dstP[2].getX(), m.dstP[2].getY());
				case 6:
					drawLine(g2d, m.dstP[0].getX(), m.dstP[0].getY(), m.dstP[1].getX(), m.dstP[1].getY());
				case 5:
				case 4:
				default:
				}
				if(m.state == 8){
					if(m.src != null){
						g2d.setColor(Color.cyan);
						fillOval(g2d, m.src.getX() - 3, m.src.getY() - 3, 7, 7);
					}
					if(m.dst != null){
						g2d.setColor(Color.magenta);
						fillOval(g2d, m.dst.getX() - 3, m.dst.getY() - 3, 7, 7);
					}
				}
			}
		};
		panel.setPreferredSize(new Dimension(_WIDTH,_HEIGHT));
		frame = new JFrame();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	}
	
	public static class GeomUtils {
	    public static double cross(double x1, double y1, double x2, double y2) {
	        return x1 * y2 - x2 * y1;
	    }
	    
	    public static double ccw(double x1, double y1, double x2, double y2, double x3, double y3) {
	        return cross(x2 - x1, y2 - y1, x3 - x2, y3 - y2);
	    }

	    public static double ccw(Point2D p1, Point2D p2, Point2D p3) {
	        return ccw(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
	    }
	    
		public static int sign(double x){
			return (x < 0)? -1: 1;
		}
	}
	
	boolean isOutlineConvex(Polygon p){
		// 基準となるCCW値を計算
        double ccw0 = GeomUtils.ccw(
        		p.xpoints[0], p.ypoints[0],
        		p.xpoints[1], p.ypoints[1],
        		p.xpoints[2], p.ypoints[2]
        		);
        if (ccw0 == 0) { // ゼロの場合は凸ではない
        	return false;
        }
        int size = p.npoints - 1;
        for (int i = 1; i < size; i++) {
            double ccw = GeomUtils.ccw(
            		p.xpoints[i], p.ypoints[i],
            		p.xpoints[(i+1)%size], p.ypoints[(i+1)%size],
            		p.xpoints[(i+2)%size], p.ypoints[(i+2)%size]
            	); // CCW値を計算
            if (ccw0 * ccw <= 0) { // 基準値と符号が異なる，またはゼロの場合は凸ではない
            	return false;
            }
        }
        return true;
	}
	
	class Vect2D{
		double x,y;
		Vect2D(int x, int y){
			double l = Math.sqrt(x*x+y*y);
			this.x = x/l;
			this.y = y/l;
		}
	} 
	
	InteractiveProjectionTranslatorDemo(){
		m = new Model();
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				v = new View();
				v.panel.addMouseListener(new MouseAdapter(){
					@Override
					public void mouseReleased(MouseEvent e) {
						if(m.state < 4){
							m.srcP[m.state] = new Point(e.getX(),e.getY());
							m.srcPolyX[m.state] = e.getX();
							m.srcPolyY[m.state] = e.getY();
							if(m.state == 3){
								m.srcPolyX[4] = m.srcPolyX[0];
								m.srcPolyY[4] = m.srcPolyY[0];
								m.srcPoly = new Polygon(m.srcPolyX, m.srcPolyY, 5);
								if(! isOutlineConvex(m.srcPoly)){
									m.state = 0;
									v.frame.getToolkit().beep();
									return;
								}
							}
							m.state++;
						}else if(m.state < 8){
							m.dstP[m.state - 4] = new Point(e.getX(),e.getY());
							m.dstPolyX[m.state - 4] = e.getX();
							m.dstPolyY[m.state - 4] = e.getY();
							if(m.state == 7){
								m.dstPolyX[4] = m.dstPolyX[0];
								m.dstPolyY[4] = m.dstPolyY[0];
								m.dstPoly = new Polygon(m.dstPolyX, m.dstPolyY, 5);
								if(! isOutlineConvex(m.dstPoly)){
									m.state = 4;
									v.frame.getToolkit().beep();
									return;
								}
								m.pt = createProjectedPoint();
							}
							m.state++;
						}else{
							
						}
						v.panel.repaint();
					}
				});

				v.panel.addMouseMotionListener(new MouseMotionAdapter(){
					@Override
					public void mouseMoved(MouseEvent e) {
						if(m.state == 8){
							m.src = e.getPoint();
							Point2D _dst = m.pt.getPoint(e.getX(), e.getY());
							m.dst = new Point((int)_dst.getX(), (int)_dst.getY());
						}
						v.panel.repaint();
					}
				});
			}
		});
	}
	
	ProjectionTranslator createProjectedPoint(){
		if(false){
			return new ProjectionTranslatorImpl(
				new Point2D[]{
						m.srcP[0],
						m.srcP[1],
						m.srcP[3],
						m.srcP[2],
				},
				new Point2D[]{
						m.dstP[0],
						m.dstP[1],
						m.dstP[3],
						m.dstP[2],
				}
			);
		}else{
			return new HomographyProjectionTranslator(m.srcP, m.dstP);
		}
	}

	public static void main(String[] args){
		new InteractiveProjectionTranslatorDemo();	
	}
	
}





