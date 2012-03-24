package org.apache.fop.render.pdf;

import java.awt.geom.Rectangle2D;

import org.w3c.dom.Node;

public class PageRectangle{
	int pageIndex;
	Rectangle2D.Float rectangle;
	
	Node svgMetadataNode;
	
	public PageRectangle(int pageIndex, float x, float y, float w, float h, Node svgMetadataNode){
		this.pageIndex = pageIndex;
		this.rectangle = new Rectangle2D.Float(x, y, w, h);
		this.svgMetadataNode = svgMetadataNode; 
	}
	
	public int getPageIndex(){
		return this.pageIndex;
	}
	
	public double getX(){
		return this.rectangle.getX();
	}
	
	public double getY(){
		return this.rectangle.getY();
	}
	
	public double getWidth(){
		return this.rectangle.getWidth();
	}
	
	public double getHeight(){
		return this.rectangle.getHeight();
	}
	
	public Node getSVGMetadataNode(){
		return this.svgMetadataNode;
	}

}
