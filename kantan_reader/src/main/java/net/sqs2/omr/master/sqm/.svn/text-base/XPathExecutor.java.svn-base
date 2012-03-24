/**
 * 
 */
package net.sqs2.omr.master.sqm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.sqs2.xmlns.SQSNamespaces;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class XPathExecutor{
	XPath xpath;
	XPathExecutor(Document document, String defaultPrefix){
		XPathFactory factory = XPathFactory.newInstance();
		this.xpath = factory.newXPath();
		this.xpath.setNamespaceContext(getNamespaceContext(document, defaultPrefix));
	}

	private NamespaceContext getNamespaceContext(Document doc, String defaultPrefix){
		  Map<String,String> nsMap = new HashMap<String,String>();
		  Element root = doc.getDocumentElement();
		  NamedNodeMap attrs = root.getAttributes();
		  String xmlns = "xmlns";
		  for(int i = 0; i < attrs.getLength(); i++){
		    Node attr = attrs.item(i);
		    String[] name = attr.getNodeName().split(":");
		    if(xmlns.equals(name[0]))
		      nsMap.put(name.length == 1 ? defaultPrefix : name[1], attr.getNodeValue());
		  }
		  return new HotFixedNamespaceContextImpl(nsMap);
	}

	class NamespaceContextImpl implements NamespaceContext{
		  protected Map<String,String> nsMap;
		  public NamespaceContextImpl(Map<String,String> nsMap){
		    this.nsMap = nsMap;
		  }
		  public String getNamespaceURI(String prefix) {
		    if(prefix == null){
		    	throw new NullPointerException("Null prefix");
		    }
		    if(nsMap.containsKey(prefix)){
		    	String uri = nsMap.get(prefix);
		    	return uri;
		    }
		    return XMLConstants.NULL_NS_URI;
		  }
		  public String getPrefix(String uri) {
		    throw new UnsupportedOperationException();
		  }
		  public Iterator getPrefixes(String namespaceURI) {
		    throw new UnsupportedOperationException();
		  }
	}

	class HotFixedNamespaceContextImpl extends NamespaceContextImpl{
		  public HotFixedNamespaceContextImpl(Map<String,String> nsMap){
		    super(nsMap);
		    nsMap.put("svg", SQSNamespaces.SVG_URI);
		  }
	}

	public NodeList selectNodeList(Element elem, String path)throws XPathExpressionException{
		//NodeList pageElementList = XPathAPI.selectNodeList(svgNode, "svg:pageSet/svg:page");
		NodeList elementList = (NodeList)xpath.evaluate(path, elem, XPathConstants.NODESET);
		return elementList;
	}
	
	public Element selectSingleNode(Element elem, String path)throws XPathExpressionException{
		try{
			//NodeList pageElementList = XPathAPI.selectNodeList(svgNode, "svg:pageSet/svg:page");
			Element element = (Element)xpath.evaluate(path, elem, XPathConstants.NODE);
			return element;
		}catch(XPathExpressionException ex){
			System.err.println("path:"+path);
			System.err.println("elem:"+elem);
			throw ex;
		}
	}
	
	private String select(String a, String b){
		if(a != null && ! "".equals(a)){
			return a;
		}else if(b != null && ! "".equals(b)){
			return b;
		}else{
			return null;
		}
	}

	public String selectAttribute(Element elem, String localName, String nsURI){
		return select(elem.getAttributeNS(nsURI, localName), elem.getAttribute(localName));
	}

	public String selectAttribute(Element elem, String localName){
		return selectAttribute(elem, localName, SQSNamespaces.SQS2007MASTER_URI);
	}

}