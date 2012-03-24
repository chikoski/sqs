package net.sqs2.omr.app;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import net.sqs2.omr.model.AppConstants;
import net.sqs2.xml.XMLUtil;
import net.sqs2.xml.XPathSelector;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class MarkReaderExportTextAreaSenarioTest extends AbstractMarkReaderSenarioTest{

	@Test
	public void testTextAreaExportSenario0() {
		try{
			File targetSourceDirectoryRoot = sourceDirectoryRoot0;
			startAndCloseSession(targetSourceDirectoryRoot);
			
			File resultDirectory = new File(targetSourceDirectoryRoot, AppConstants.RESULT_DIRNAME);
			File textareaDirectory = new File(resultDirectory, "TEXTAREA");			
			File textareaDirectoryIndexFile = new File(textareaDirectory, "index.html");
			File row0Directory = new File(textareaDirectory, "0");
			File row0DirectoryIndexFile = new File(row0Directory, "index.html");
			File row0Page0File = new File(row0Directory, "0.html");
			
			assertTrue(textareaDirectory.isDirectory());

			assert0TextareaDirectoryIndex(textareaDirectory,
					textareaDirectoryIndexFile);
			
			assert0TextareaRow0DirectoryIndex(row0DirectoryIndexFile);
			
			assert0Row0Page0File(row0Page0File);
			
		}catch(Exception ex){
			ex.printStackTrace();
			fail();
		}
	}

	private void assert0TextareaDirectoryIndex(File textareaDirectory,
			File textareaDirectoryIndexFile) throws SAXException, IOException,
			ParserConfigurationException, XPathExpressionException {
		
		assertTrue(textareaDirectoryIndexFile.exists());
		
		Document textareaDirectoryIndexDocument = XMLUtil.createDocumentBuilder().parse(textareaDirectoryIndexFile);
		XPathSelector xpath = new XPathSelector(textareaDirectoryIndexDocument, "xhtml");
		assertEquals("SQS 実習帖（09年7月実施）::自由記述欄一覧", 
				selectStringContent(textareaDirectoryIndexDocument, xpath, 
						"/xhtml:html/xhtml:head/xhtml:title"));
		assertEquals("0/index.html", 
				selectSingleElement(textareaDirectoryIndexDocument, xpath, 
				"/xhtml:html/xhtml:body/xhtml:div/xhtml:div/xhtml:ul/xhtml:li[1]/xhtml:a").getAttribute("href"));
	}

	private void assert0TextareaRow0DirectoryIndex(File row0DirectoryIndexFile)
			throws SAXException, IOException, ParserConfigurationException,
			XPathExpressionException {

		assertTrue(row0DirectoryIndexFile.exists());

		Document row0DirectoryIndexDocument = XMLUtil.createDocumentBuilder()
				.parse(row0DirectoryIndexFile);
		XPathSelector xpath = new XPathSelector(row0DirectoryIndexDocument,
				"xhtml");
		assertEquals(":(1):自由記述欄一覧", selectStringContent(
				row0DirectoryIndexDocument, xpath,
				"/xhtml:html/xhtml:head/xhtml:title"));
		assertEquals(
				"0.png",
				selectSingleElement(row0DirectoryIndexDocument, xpath,
						"/xhtml:html/xhtml:body/xhtml:div/xhtml:div/xhtml:ul/xhtml:li[1]/xhtml:img")
						.getAttribute("src"));
	}

	private void assert0Row0Page0File(File row0Page0File) throws SAXException,
			IOException, ParserConfigurationException, XPathExpressionException {

		assertTrue(row0Page0File.exists());

		Document row0DirectoryIndexDocument = XMLUtil.createDocumentBuilder()
				.parse(row0Page0File);
		XPathSelector xpath = new XPathSelector(row0DirectoryIndexDocument,
				"xhtml");
		assertEquals(":(1):[1行目-2行目](Page 1 of 1):自由記述欄一覧",
				selectStringContent(row0DirectoryIndexDocument, xpath,
						"/xhtml:html/xhtml:head/xhtml:title"));
		assertEquals(
				"0.png",
				selectSingleElement(row0DirectoryIndexDocument, xpath,
						"/xhtml:html/xhtml:body/xhtml:div/xhtml:div/xhtml:ul/xhtml:li[1]/xhtml:img")
						.getAttribute("src"));
	}

	/* ------------------------ */
	
	@Test
	public void testTextAreaExportSenario2() {
		try{
			File targetSourceDirectoryRoot = sourceDirectoryRoot2;
			startAndCloseSession(targetSourceDirectoryRoot);
			
			File resultDirectory = new File(targetSourceDirectoryRoot, AppConstants.RESULT_DIRNAME);			
			File textareaDirectory = new File(resultDirectory, "TEXTAREA");			
			File textareaDirectoryIndexFile = new File(textareaDirectory, "index.html");
			File row15Directory = new File(textareaDirectory, "15");
			File row15DirectoryIndexFile = new File(row15Directory, "index.html");
			File row15Page0File = new File(row15Directory, "0.html");
			
			assertTrue(textareaDirectory.isDirectory());

			assert2TextareaDirectoryIndex(textareaDirectory,
					textareaDirectoryIndexFile);
			
			assert2TextareaRow15DirectoryIndex(row15DirectoryIndexFile);
			
			assert2Row15Page0File(row15Page0File);
			
		}catch(Exception ex){
			ex.printStackTrace();
			fail();
		}
	}

	private void assert2TextareaDirectoryIndex(File textareaDirectory,
			File textareaDirectoryIndexFile) throws SAXException, IOException,
			ParserConfigurationException, XPathExpressionException {
		
		assertTrue(textareaDirectoryIndexFile.exists());
		
		Document textareaDirectoryIndexDocument = XMLUtil.createDocumentBuilder().parse(textareaDirectoryIndexFile);
		XPathSelector xpath = new XPathSelector(textareaDirectoryIndexDocument, "xhtml");
		assertEquals("::自由記述欄一覧", 
				selectStringContent(textareaDirectoryIndexDocument, xpath, 
						"/xhtml:html/xhtml:head/xhtml:title"));
		assertEquals("15/index.html", 
				selectSingleElement(textareaDirectoryIndexDocument, xpath, 
				"/xhtml:html/xhtml:body/xhtml:div/xhtml:div/xhtml:ul/xhtml:li[1]/xhtml:a").getAttribute("href"));
	}


	private void assert2TextareaRow15DirectoryIndex(File row15DirectoryIndexFile)
			throws SAXException, IOException, ParserConfigurationException,
			XPathExpressionException {

		assertTrue(row15DirectoryIndexFile.exists());

		Document row15DirectoryIndexDocument = XMLUtil.createDocumentBuilder()
				.parse(row15DirectoryIndexFile);
		XPathSelector xpath = new XPathSelector(row15DirectoryIndexDocument,
				"xhtml");
		assertEquals(":(7):自由記述欄一覧", selectStringContent(
				row15DirectoryIndexDocument, xpath,
				"/xhtml:html/xhtml:head/xhtml:title"));
		assertEquals(
				"../../../A/処理結果/TEXTAREA/15/index.html",
				selectSingleElement(row15DirectoryIndexDocument, xpath,
						"/xhtml:html/xhtml:body/xhtml:div/xhtml:div/xhtml:ul/xhtml:li[1]/xhtml:a")
						.getAttribute("href"));
	}

	private void assert2Row15Page0File(File row15Page0File) throws SAXException,
			IOException, ParserConfigurationException, XPathExpressionException {

		assertTrue(row15Page0File.exists());

		Document row15DirectoryIndexDocument = XMLUtil.createDocumentBuilder()
				.parse(row15Page0File);
		XPathSelector xpath = new XPathSelector(row15DirectoryIndexDocument,
				"xhtml");
		assertEquals("フォルダ C:(7):[1行目--20行目](Page 1 of 3):自由記述欄一覧",
				selectStringContent(row15DirectoryIndexDocument, xpath,
						"/xhtml:html/xhtml:head/xhtml:title"));
		assertEquals(
				"20.png",
				selectSingleElement(row15DirectoryIndexDocument, xpath,
						"/xhtml:html/xhtml:body/xhtml:div/xhtml:div/xhtml:ul/xhtml:li[1]/xhtml:img")
						.getAttribute("src"));
	}

	/* ----------------- */
	private Element selectSingleElement(Document document, XPathSelector xpathSelector, String nodeTest)
	throws XPathExpressionException {
		Element elem = ((Element)xpathSelector.selectSingleNode((Element)document.getDocumentElement(),
				nodeTest));
		return elem;
	}
	
	private String selectStringContent(Document document, XPathSelector xpathSelector, String nodeTest)
		throws XPathExpressionException {
		Element elem = ((Element)xpathSelector.selectSingleNode((Element)document.getDocumentElement(),
				nodeTest));
		return elem.getTextContent();
	}
}
