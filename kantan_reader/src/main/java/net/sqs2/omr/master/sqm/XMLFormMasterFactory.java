package net.sqs2.omr.master.sqm;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.master.PageMasterException;
import net.sqs2.omr.master.PageMasterFactory;
import net.sqs2.omr.master.PageMasterMetadata;
import net.sqs2.util.FileResourceID;
import net.sqs2.util.FileUtil;
import net.sqs2.xml.XMLUtil;
import net.sqs2.xmlns.SQSNamespaces;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



public class XMLFormMasterFactory implements PageMasterFactory {
	
	XPathExecutor xpath;
	
	static PageMasterFactory createInstance(){
		return new XMLFormMasterFactory();
	}
	
	public XMLFormMasterFactory(){
	}
	
	public FormMaster create(File sourceDirectoryRoot, String masterPath) throws PageMasterException {
		String masterPathLower = masterPath.toLowerCase();
		if(masterPathLower.endsWith(".sqm")){
			File file = new File(sourceDirectoryRoot, masterPath);
			PageMasterMetadata pageMasterMetadata = new PageMasterMetadata(sourceDirectoryRoot, masterPath, "SQS Master File", 3, file.lastModified());			
			try{
				return createFormMaster(pageMasterMetadata, XMLUtil.createDocumentBuilder().parse(file));
			}catch(IOException ex){				
			}catch(ParserConfigurationException ex){				
			}catch(SAXException ex){				
			}
		}			
		return null;
	}
	
	protected Rectangle createRectangle(Element rect){
		return new Rectangle(Integer.parseInt(xpath.selectAttribute(rect, "x", SQSNamespaces.SVG_URI)),
				Integer.parseInt(xpath.selectAttribute(rect, "y", SQSNamespaces.SVG_URI)),
				Integer.parseInt(xpath.selectAttribute(rect, "width", SQSNamespaces.SVG_URI)),
				Integer.parseInt(xpath.selectAttribute(rect, "height", SQSNamespaces.SVG_URI)));
	}

	private void setMasterMetadata(Element svgNode, FormMaster master) throws TransformerException,XPathExpressionException {
		Element metadataNode = xpath.selectSingleNode(svgNode, "/svg:svg/svg:pageSet/svg:masterPage/svg:metadata");
		setMasterMaster(metadataNode, master);
		setMasterCorners(metadataNode, master);
		setMasterCheckers(metadataNode, master);
	}

	private void setMasterCheckers(Element metadataNode, FormMaster master) throws TransformerException,XPathExpressionException {

		Element upsideDownCheckerHeaderRectangle = null;
		Element upsideDownCheckerFooterRectangle = null;
		Element evenOddCheckerLeftRectangle = null;
		Element evenOddCheckerRightRectangle = null;

		if("1.1".equals(master.getVersion())){
			upsideDownCheckerHeaderRectangle =
				(Element)xpath.selectSingleNode(metadataNode, "master:upsideDownChecker/master:checkerArea[@side='header']/svg:rect");
			upsideDownCheckerFooterRectangle =
				(Element)xpath.selectSingleNode(metadataNode, "master:upsideDownChecker/master:checkerArea[@side='footer']/svg:rect");
			evenOddCheckerLeftRectangle =
				(Element)xpath.selectSingleNode(metadataNode, "master:evenOddChecker/master:checkerArea[@side='left']/svg:rect");
			evenOddCheckerRightRectangle =
				(Element)xpath.selectSingleNode(metadataNode, "master:evenOddChecker/master:checkerArea[@side='right']/svg:rect");
		}else{
			upsideDownCheckerHeaderRectangle =
				(Element)xpath.selectSingleNode(metadataNode, "master:upsideDownChecker/master:checkerArea[@master:side='header']/svg:rect");
			upsideDownCheckerFooterRectangle =
				(Element)xpath.selectSingleNode(metadataNode, "master:upsideDownChecker/master:checkerArea[@master:side='footer']/svg:rect");
			evenOddCheckerLeftRectangle =
				(Element)xpath.selectSingleNode(metadataNode, "master:evenOddChecker/master:checkerArea[@master:side='left']/svg:rect");
			evenOddCheckerRightRectangle =
				(Element)xpath.selectSingleNode(metadataNode, "master:evenOddChecker/master:checkerArea[@master:side='right']/svg:rect");
		}
		
		master.setHeaderCheckArea(createRectangle(upsideDownCheckerHeaderRectangle));
		master.setFooterCheckArea(createRectangle(upsideDownCheckerFooterRectangle));
		master.setFooterLeftRectangle(createRectangle(evenOddCheckerLeftRectangle));
		master.setFooterRightRectangle(createRectangle(evenOddCheckerRightRectangle));
	}
	
	private void setMasterMaster(Element metadataNode, FormMaster master) throws TransformerException,XPathExpressionException {
		Element masterNode = xpath.selectSingleNode(metadataNode, "master:master");
		
		List<Attr> addAttr = new ArrayList<Attr>();
		
		String version = xpath.selectAttribute(masterNode, "version", SQSNamespaces.SQS2007MASTER_URI);
		master.setVersion(version);

			master.setNumPages(Integer.parseInt(xpath.selectAttribute(masterNode, "numPages", SQSNamespaces.SQS2007MASTER_URI)));
			
			String horizontalOffset = xpath.selectAttribute(masterNode, "horizontalOffset", SQSNamespaces.SQS2007MASTER_URI);
			if(horizontalOffset != null){
				try{
					master.setHorizontalOffset(Integer.parseInt(xpath.selectAttribute(masterNode, "horizontalOffset", SQSNamespaces.SQS2007MASTER_URI)));
				}catch(Exception ignore){}
			}
			String verticalOffset = xpath.selectAttribute(masterNode, "verticalOffset", SQSNamespaces.SQS2007MASTER_URI);
			if(verticalOffset != null){
				try{
					master.setVerticalOffset(Integer.parseInt(xpath.selectAttribute(masterNode, "verticalOffset", SQSNamespaces.SQS2007MASTER_URI)));
				}catch(Exception ignore){}
			}
	}

	private void setMasterCorners(Element metadataNode, FormMaster master) throws TransformerException,XPathExpressionException {
		Element cornerNode = xpath.selectSingleNode(metadataNode, "master:corner");	
		Point[] masterGuideCorners = new Point[4]; 
		for(int i = 1; i <= 4; i++){
			masterGuideCorners[i-1] = new Point(
					Integer.parseInt(xpath.selectAttribute(cornerNode, "x"+i, SQSNamespaces.SQS2007MASTER_URI)),
					Integer.parseInt(xpath.selectAttribute(cornerNode, "y"+i, SQSNamespaces.SQS2007MASTER_URI)));
		}
		master.setCorners(masterGuideCorners);
	}


	private ArrayList<FormArea> getAreaListByQID(FormMaster master, String qid) {
		ArrayList<FormArea> areaList = master.getFormAreaList(qid);
		if (areaList == null) {
			areaList = new ArrayList<FormArea>();
			master.putFormAreaList(qid, areaList);
		}
		return areaList;
	}

	protected Document createDocumentBySQM(File sqmFile, String path) throws PageMasterException {
		InputStream sqmInputStream = null;
		try{
			sqmInputStream = new FileInputStream(sqmFile);
			return createDocumentByStream(sqmInputStream, path);
		} catch (IOException ex) {
			throw new PageMasterException(path);
		}finally{
			try{
				sqmInputStream.close();
			}catch(Exception ignore){
			}
		}
	}

	protected Document createDocumentByStream(InputStream sqmInputStream, String path) throws PageMasterException {
		try {	
			return XMLUtil.createDocumentBuilder().parse(new InputSource(new InputStreamReader(sqmInputStream, "UTF-8")));
		} catch (IOException ex) {
			throw new PageMasterException(path);
		} catch (SAXException ex) {
			throw new PageMasterException(path);
		} catch (FactoryConfigurationError ex) {
			throw new RuntimeException(ex);
		} catch (ParserConfigurationException ex) {
			throw new RuntimeException(ex);
		}
	}
		
	public FormMaster createFormMaster(PageMasterMetadata metadata, Document document) throws PageMasterException {
		try {			

			FormMaster master = new FormMaster(new FileResourceID(metadata.getMasterPath(), metadata.getLastModified()), metadata);
			Element svgNode = document.getDocumentElement();

			this.xpath = new XPathExecutor(document, "svg");
			
			setMasterMetadata(svgNode, master);
			
			int prevPage = -1;
			int columnIndex = -1;
			String prevQID = null;
			int areaIndexInPage = 0;
			int itemIndex = 0;

			//NodeList pageElementList = XPathAPI.selectNodeList(svgNode, "svg:pageSet/svg:page");
			NodeList pageElementList = xpath.selectNodeList(svgNode, "svg:pageSet/svg:page");
			
			for(int pageIndex = 0; pageIndex < pageElementList.getLength(); pageIndex++){
				
				//NodeList gElementList = XPathAPI.selectNodeList(pageElementList.item(pageIndex), "svg:g");
				NodeList gElementList = xpath.selectNodeList((Element)pageElementList.item(pageIndex), "svg:g");
				
				ArrayList<FormArea> areaListByPageIndex = new ArrayList<FormArea>();
				master.addFormAreaList(areaListByPageIndex);
		
				for(int gIndex = 0; gIndex < gElementList.getLength(); gIndex++){
					
					Element gElement = (Element)gElementList.item(gIndex);
					
					FormArea area = FormAreaFactory2.create(this.xpath, master, gElement, pageIndex);
					if(area == null){
						throw new PageMasterException(metadata.getMasterPath());
					}
					if (prevPage != area.getPage()) {
						areaIndexInPage = 0;
					}
					if (! area.getQID().equals(prevQID)) {
						master.addQID(area.getQID(), area.getType());
						itemIndex = 0;
						columnIndex ++;
					}
					List<FormArea> areaListByQID = getAreaListByQID(master, area.getQID());

					areaListByPageIndex.add(area);
					areaListByQID.add(area);

					master.getFormAreaList().add(area);
					master.putFormArea(area.getID(), area);

					//System.err.println(area.getID()+"-> "+area.getPage()+"   "+area.getQID()+" - "+areaIndexInPage);
					
					master.setAreaIndexInPage(area.getQID(), areaIndexInPage);
					area.setIndex(columnIndex, itemIndex, areaIndexInPage);

					itemIndex++;
					areaIndexInPage++;
					prevQID = area.getQID();
					prevPage = area.getPage();
	
				}				
			}
			Logger.getLogger("master").info(master.getRelativePath());
			return master;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			throw new PageMasterException("Invalid format: "+metadata.getMasterPath());
		} catch (TransformerException e) {
			throw new PageMasterException("Invalid format: "+metadata.getMasterPath());
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new PageMasterException("No config file: "+metadata.getMasterPath());
		}
	}

}
