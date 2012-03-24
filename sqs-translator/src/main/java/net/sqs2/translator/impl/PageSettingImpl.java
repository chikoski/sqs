/**
 * 
 */
package net.sqs2.translator.impl;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.render.pdf.PageRectangle;
import org.apache.fop.render.pdf.SVGElementIDToPageRectangleMap;

public class PageSettingImpl implements PageSetting{
	private static final String VERSION = "2.1.0";
	private double width;
	private double height;
	private Point2D[] deskewGuideCenterPointArray = new Point2D[4];
	
	private Rectangle2D upsideDownCheckAreaHeader = null;
	private Rectangle2D upsideDownCheckAreaFooter = null;
	private Rectangle2D evenOddCheckAreaLeft = null;
	private Rectangle2D evenOddCheckAreaRight = null;

	private PageSettingImpl(){
		this(595, 842);//A4 portlait size
		//this(284, 420);//hagaki size
	}
	
	public PageSettingImpl(double width, double height){
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void init(SVGElementIDToPageRectangleMap map, FOUserAgent ua)throws SQMSchemeException{
		if(true){
			if(! "auto".equals(ua.getPageWidth())){
				width = Double.parseDouble(ua.getPageWidth());
			}
			if(! "auto".equals(ua.getPageHeight())){
				height = Double.parseDouble(ua.getPageHeight());
			}
			try{
				PageRectangle e0 = (PageRectangle)map.get(ua, 0).get("SQSDeskewGuideNorthWest");
				PageRectangle e1 = (PageRectangle)map.get(ua, 0).get("SQSDeskewGuideNorthEast");
				PageRectangle e2 = (PageRectangle)map.get(ua, 0).get("SQSDeskewGuideSouthWest");
				PageRectangle e3 = (PageRectangle)map.get(ua, 0).get("SQSDeskewGuideSouthEast");
			
				if(e0 != null && e1 != null && e2 != null && e3 != null){
					deskewGuideCenterPointArray[0] = createCenterPointOfRectElement(e0);
					deskewGuideCenterPointArray[1] = createCenterPointOfRectElement(e1);
					deskewGuideCenterPointArray[2] = createCenterPointOfRectElement(e2);
					deskewGuideCenterPointArray[3] = createCenterPointOfRectElement(e3);
					
					upsideDownCheckAreaHeader = createRectangle(e0);
					upsideDownCheckAreaFooter = createRectangle(e2);
					evenOddCheckAreaLeft = createRectangle(e0);
					evenOddCheckAreaRight = createRectangle(e1);
				}else{
					throw new IllegalArgumentException("page master scheme is invalid.");
				}
			}catch(NullPointerException ex){
				//throw new SQMSchemeException();
			}
		}
	}
	
	private Point2D.Double createCenterPointOfRectElement(PageRectangle rect)throws SQMSchemeException{
		if(rect == null){
			throw new SQMSchemeException();
		}
		return new Point2D.Double(rect.getX() + rect.getWidth()/ 2, rect.getY()
				+ rect.getHeight() / 2);
	}
	
	private Rectangle2D createRectangle(PageRectangle rect){
		return new Rectangle2D.Double(rect.getX(), rect.getY(),
				rect.getWidth(), rect.getHeight());
	}
	
	@Override
	public String getVersion(){
		return VERSION;
	}
	
	@Override
	public double getWidth(){
		return width;
	}
	
	@Override
	public double getHeight(){
		return height;
	}
	
	@Deprecated
	@Override
	public Rectangle2D getEvenOddCheckAreaLeft() {
		return evenOddCheckAreaLeft;
	}
	
	@Deprecated
	@Override
	public Rectangle2D getEvenOddCheckAreaRight() {
		return evenOddCheckAreaRight;
	}
	
	@Deprecated
	@Override
	public Point2D[] getDeskewGuideCenterPointArray() {
		return deskewGuideCenterPointArray;
	}
	
	@Deprecated
	@Override
	public Rectangle2D getUpsideDownCheckAreaFooter() {
		return upsideDownCheckAreaFooter;
	}
	
	@Deprecated
	@Override
	public Rectangle2D getUpsideDownCheckAreaHeader() {
		return upsideDownCheckAreaHeader;
	}
}