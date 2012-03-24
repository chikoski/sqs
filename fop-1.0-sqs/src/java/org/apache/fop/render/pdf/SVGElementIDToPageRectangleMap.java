package org.apache.fop.render.pdf;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.fop.apps.FOUserAgent;

public class SVGElementIDToPageRectangleMap{
	private static final long serialVersionUID = 0L;
	private HashMap map = new HashMap();
	
	static SVGElementIDToPageRectangleMap singleton = null;
	
	public String toString(){
		return map.toString();
	}
	
	public static SVGElementIDToPageRectangleMap getInstance(){
		if(SVGElementIDToPageRectangleMap.singleton == null){
			synchronized (SVGElementIDToPageRectangleMap.class){
				if(SVGElementIDToPageRectangleMap.singleton == null){
					SVGElementIDToPageRectangleMap.singleton = new SVGElementIDToPageRectangleMap();
				}
			}
		}
		return SVGElementIDToPageRectangleMap.singleton;
	}
	
	public Map remove(FOUserAgent ua, int pageIndex){
		Map mapByUserAgent = null;
		if((mapByUserAgent = (Map)this.map.get(ua)) != null){
			return (Map)mapByUserAgent.remove(Integer.valueOf(pageIndex));
		}
		return null;
	}
	
	public Map get(FOUserAgent ua, int pageIndex){
		Map mapByUserAgent = null;
		if((mapByUserAgent = (Map)this.map.get(ua)) != null){
			return (Map)mapByUserAgent.get(Integer.valueOf(pageIndex));
		}
		return null;
	}
	
	public void put(FOUserAgent ua, int pageIndex, String id, PageRectangle pageRectangle){
		Map mapByUserAgent = null;
		if((mapByUserAgent = (Map)this.map.get(ua)) == null){
			mapByUserAgent = new LinkedHashMap();
			this.map.put(ua, mapByUserAgent);
		}
		
		Map mapByPageIndex = null;
		if((mapByPageIndex = (Map)mapByUserAgent.get(Integer.valueOf(pageIndex))) == null){
			mapByPageIndex = new LinkedHashMap();
			mapByUserAgent.put(Integer.valueOf(pageIndex), mapByPageIndex);
		}
		mapByPageIndex.put(id, pageRectangle);
	}
	
}
