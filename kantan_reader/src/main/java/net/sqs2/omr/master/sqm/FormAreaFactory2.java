/*

 FormAreaFactory.java
 
 Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
 Created on 2007/01/11

 */
package net.sqs2.omr.master.sqm;

import java.awt.Rectangle;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.xml.DOMUtil;
import net.sqs2.xmlns.SQSNamespaces;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FormAreaFactory2 implements FormMasterConstants2{

	public static FormArea create(XPathExecutor xpath, FormMaster master, Element gElem, int pageIndex)throws TransformerException,XPathExpressionException{		
		
		Element xformUCElem = xpath.selectSingleNode(gElem, METADATA_STAR_ELEMENT_REPRESENTATION); 
		if(xformUCElem == null){
			return null;
		}

		FormArea area = new FormArea();
		
		area.setPage(pageIndex+1);
		String type = null;
		type = xformUCElem.getAttributeNS(SQSNamespaces.SQS2004_URI, "pxform-type");
		if(type == null || type.equals("")){
			type = xformUCElem.getLocalName();
		}
		area.setType(type);
		area.setTypeCode(getTypeCode(area.getType()));
		area.setQID(xformUCElem.getAttributeNS(SQSNamespaces.SQS2004_URI, "qid").intern());
		area.setID(gElem.getAttribute("id").intern());
		area.setRect(createRectangle(xpath, gElem, master));
		area.setLabel(xpath.selectSingleNode(xformUCElem, "xforms:label").getTextContent().trim().intern());
		area.setHint(xpath.selectSingleNode(xformUCElem, "xforms:hint").getTextContent().trim().intern());
		area.setHints(getHints(xpath, gElem));
		if(area.getTypeCode() == FormArea.SELECT1 || area.getTypeCode() == FormArea.SELECT){
			area.setItemLabel(getItemLabel(xformUCElem).intern());
			area.setItemValue(getItemValue(xformUCElem).intern());
		}
		return area;
	}
	
	private static int getTypeCode(String type) {
        if ("select1".equals(type)) {
            return FormArea.SELECT1;
        } else if ("select".equals(type)) {
            return FormArea.SELECT;
        } else if ("textarea".equals(type)) {
            return FormArea.TEXTAREA;
        } else if ("input".equals(type)) {
            return FormArea.INPUT;
        } else {
        	//return -1;
            throw new RuntimeException("type:"+type);
        }
    }
	
	
	private static Rectangle createRectangle(XPathExecutor xpath, Element gElem, FormMaster master)throws TransformerException,XPathExpressionException{
		Element rnode = xpath.selectSingleNode(gElem, RECT_ELEMENT_REPRESENTATION);
		int x = (int)Float.parseFloat(xpath.selectAttribute(rnode, "x", SQSNamespaces.SVG_URI)) + master.getHorizontalOffset();
		int y = (int)Float.parseFloat(xpath.selectAttribute(rnode, "y", SQSNamespaces.SVG_URI)) + master.getVerticalOffset();
		int width = (int)Float.parseFloat(xpath.selectAttribute(rnode, "width", SQSNamespaces.SVG_URI));
		int height = (int)Float.parseFloat(xpath.selectAttribute(rnode, "height", SQSNamespaces.SVG_URI));
		return new Rectangle(x, y, width, height);
		//45, 062
		//return new Rectangle(x, y, width, height); //FIXME!
	}
	
	private static String[] getHints(XPathExecutor xpath, Element gElem)throws TransformerException,XPathExpressionException{
		NodeList list = xpath.selectNodeList(gElem, RECT_METADATA_STAR_HINT_ELEMENT_REPRESENTATION);
		String[] hints = new String[list.getLength()];
		for (int i = 0; i < hints.length; i++) {
			Element hElem = (Element) list.item(i);
			hints[i] = hElem.getTextContent().trim().intern();
		}
		return hints;
	}
	
	private static String getItemLabel(Element xformUCElem){
		 try {
			 return DOMUtil.getDescendantElement(xformUCElem, LABEL_PATH).getTextContent().trim();
         } catch (AbstractMethodError ex) {
             return "";
         }
	}
	
	private static String getItemValue(Element xformUCElem){
		try {
			return DOMUtil.getDescendantElement(xformUCElem, VALUE_PATH).getTextContent().trim();
		} catch (AbstractMethodError ex) {
		    return "";
		}
	}
}
