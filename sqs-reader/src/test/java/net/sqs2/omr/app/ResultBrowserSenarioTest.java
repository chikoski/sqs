package net.sqs2.omr.app;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import net.sqs2.net.NetworkUtil;
import net.sqs2.omr.app.command.RemoveResultFoldersCommand;
import net.sqs2.omr.result.httpd.SQSHttpdManager;
import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.omr.session.service.MarkReaderSessions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class ResultBrowserSenarioTest extends AbstractMarkReaderSenarioTest{
	
	static File targetDirectory = sourceDirectoryRoot2;
	static long sid;
	
	//@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		AbstractMarkReaderSenarioTest.setUpBeforeClass();
		Thread t = new Thread(new Runnable(){
			public void run(){
				try{
					SQSHttpdManager.initHttpds();
				}catch(Exception ignore){
					ignore.printStackTrace();
				}
			}
		});
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}
	
	//@BeforeMethod
	public void before()throws Exception{
		startSession(targetDirectory);
	}
	
	//@AfterMethod
	public void after()throws Exception{
		closeSessionSource(targetDirectory);
		new RemoveResultFoldersCommand(targetDirectory).call();
	}
	
	public void testSelectMode()throws Exception{ 
		MarkReaderSession session = MarkReaderSessions.get(targetDirectory);
		long sessionID = session.getSessionID();
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("sid", Long.toString(sessionID)));
		qparams.add(new BasicNameValuePair("m", Integer.toString(0)));
		qparams.add(new BasicNameValuePair("t", Integer.toString(0)));
		qparams.add(new BasicNameValuePair("u", "t"));		
		assertEquals("rHandler.updateOptions([]);", getContent(sessionID, "/e", qparams).trim());
	}
	
	public void testSelectMaster()throws Exception{ 
		MarkReaderSession session = MarkReaderSessions.get(targetDirectory);
		long sessionID = session.getSessionID();
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("sid", Long.toString(sessionID)));
		qparams.add(new BasicNameValuePair("m", Integer.toString(0)));
		qparams.add(new BasicNameValuePair("u", "m"));		
		assertEquals("rHandler.updateOptions([]);", getContent(sessionID, "/e", qparams).trim());
	}
	
	public void testSelectTable()throws Exception{ 
		MarkReaderSession session = MarkReaderSessions.get(targetDirectory);
		long sessionID = session.getSessionID();
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("sid", Long.toString(sessionID)));
		qparams.add(new BasicNameValuePair("m", Integer.toString(0)));
		qparams.add(new BasicNameValuePair("t", Integer.toString(0)));
		qparams.add(new BasicNameValuePair("u", "t"));		
		assertEquals("rHandler.updateOptions([]);", getContent(sessionID, "/e", qparams).trim());
	}
	
	public void testSelectRow()throws Exception{ 
		MarkReaderSession session = MarkReaderSessions.get(targetDirectory);
		long sessionID = session.getSessionID();
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("sid", Long.toString(sessionID)));
		qparams.add(new BasicNameValuePair("m", Integer.toString(0)));
		qparams.add(new BasicNameValuePair("t", Integer.toString(0)));
		qparams.add(new BasicNameValuePair("u", "t"));		
		assertEquals("rHandler.updateOptions([]);", getContent(sessionID, "/e", qparams).trim());
	}
	
	public void testSelectQuestion()throws Exception{ 
		MarkReaderSession session = MarkReaderSessions.get(targetDirectory);
		long sessionID = session.getSessionID();
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("sid", Long.toString(sessionID)));
		qparams.add(new BasicNameValuePair("m", Integer.toString(0)));
		qparams.add(new BasicNameValuePair("t", Integer.toString(0)));
		qparams.add(new BasicNameValuePair("u", "t"));		
		assertEquals("rHandler.updateOptions([]);", getContent(sessionID, "/e", qparams).trim());
	}
	
	
	private String getContent(InputStream inputStream, String encoding){
		StringWriter out = new StringWriter();
		try{
			InputStreamReader in = new InputStreamReader(new BufferedInputStream(inputStream), encoding);					
			char[] data = new char[4096];
			int len = 0;
			while( 0 < (len = in.read(data, 0, 4096))){
				out.write(data, 0, len);
			}
			if(in != null){
				in.close();
			}
			return out.toString();
		}catch(IOException ignore){
			ignore.printStackTrace();
			return null;
		}
	}

	private String getContent(long sessionID, String path, List<NameValuePair> qparams){
		try{
			URI uri = URIUtils.createURI("http", NetworkUtil.Inet4.LOOPBACK_ADDRESS, 
					SQSHttpdManager.HTTP_PORT_FOR_LOCAL, path, 
					URLEncodedUtils.format(qparams, "UTF-8"), null);

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(uri));
			
			HttpEntity entity = response.getEntity();
			if (entity != null){
				String contentType = entity.getContentType().getValue();
				String textEncoding = contentType.substring(contentType.lastIndexOf("=")+1);
				return getContent(entity.getContent(), textEncoding);
			}else{
				fail("entity == null");
			}
		}catch(Exception ex){
			ex.printStackTrace();
			fail();
		}
		return null;
	}
	
	/* 
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
	*/
	
	//@Test
	public void testDummy(){
		assertTrue(true);
	} 
}
