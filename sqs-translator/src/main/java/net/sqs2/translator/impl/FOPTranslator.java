package net.sqs2.translator.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import net.sqs2.net.ClassURLStreamHandlerFactory;
import net.sqs2.translator.AbstractTranslator;
import net.sqs2.translator.TranslatorException;
import net.sqs2.util.FileUtil;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.fop.render.awt.AWTRenderer;
import org.apache.fop.render.awt.viewer.Renderable;
import org.xml.sax.ContentHandler;

public abstract class FOPTranslator extends AbstractTranslator {

	static {
		try {
			URL.setURLStreamHandlerFactory(ClassURLStreamHandlerFactory.getSingleton());
		} catch (Error ignore) {
		}
	}

	private final static boolean FOP_OUTPUT_DEBUG = false;

	protected String fopURL = null;
	protected String language = null;
	private String groupID = null;
	private String appID = null;
	private String xsltBaseURL = null;
	protected URIResolver uriResolver;
	
	private Future<SQSToPDFTranslatorCore> translatorCoreFuture = null;

	private PreviewFrame previewFrame;

	public FOPTranslator(String groupID, String appID, String fopURL, String xsltBaseURL, 
			URIResolver uriResolver, PageSetting pageSetting)
	throws TranslatorException{
		this(groupID, appID, fopURL, xsltBaseURL, Locale.getDefault().getLanguage(), 
				uriResolver, pageSetting);
	}
	
	public FOPTranslator(final String groupID, final String appID, final String fopURL, final String xsltBaseURL, final String language, 
			final URIResolver uriResolver, final PageSetting pageSetting)
	throws TranslatorException {
		super();
		this.groupID = groupID;
		this.appID = appID;
		this.fopURL = fopURL;
		this.xsltBaseURL = xsltBaseURL;
		this.language = language;
		this.uriResolver = uriResolver;
		
		this.translatorCoreFuture = Executors.newSingleThreadExecutor().submit(new Callable<SQSToPDFTranslatorCore>(){
			@Override
			public SQSToPDFTranslatorCore call()throws Exception {
				try {
					String userCustomizedXSLTFileBaseURIString = XSLTFileBaseUtil.userCustomizedURI(groupID, appID);
					return new SQSToPDFTranslatorCore(fopURL, xsltBaseURL, userCustomizedXSLTFileBaseURIString, language, uriResolver, pageSetting);
				} catch (TranslatorException ex) {
					ex.printStackTrace();
				}
				return null;
			}
		});
	}
	
	protected SQSToPDFTranslatorCore getTranslatorCore(){
		try{
			return this.translatorCoreFuture.get();
		}catch(ExecutionException ex){
			ex.printStackTrace();
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}
		return null;
	}

	public void setCanceled(boolean isCanceled){
		if (getTranslatorCore() != null) {
			//this.core.getUserAgent().setCanceled(isCanceled);	
			try{
				getTranslatorCore().getUserAgent().getRendererOverride().stopRenderer();
			}catch(IOException ignore){
				ignore.printStackTrace();
			}
		}
	}

	synchronized private byte[] createFOBytes(byte[] sourceBytes, String systemId, String language, URIResolver uriResolver) throws TranslatorException, IOException {
		ByteArrayInputStream sqsInputStream = new ByteArrayInputStream(sourceBytes);
		ByteArrayOutputStream foOutputStream = new ByteArrayOutputStream(65536);
		getTranslatorCore().execute(sqsInputStream, systemId, foOutputStream, uriResolver);
		sqsInputStream.close();
		sqsInputStream = null;

		foOutputStream.flush();
		byte[] foBytes = foOutputStream.toByteArray();
		foOutputStream.close();
		foOutputStream = null;
		return foBytes;
	}
	
	private byte[] createSourceBytes(InputStream sourceInputStream) throws IOException {
		ByteArrayOutputStream sourceOutputStream = new ByteArrayOutputStream(4096);
		FileUtil.pipe(sourceInputStream, sourceOutputStream);
		sourceInputStream.close();
		sourceInputStream = null;
		sourceOutputStream.flush();
		byte[] sourceBytes = sourceOutputStream.toByteArray();
		sourceOutputStream.close();
		sourceOutputStream = null;
		return sourceBytes;
	}

	@Override
	public void execute(InputStream sqsSourceInputStream, String systemId, OutputStream pdfOutputStream, URIResolver uriResolver) throws TranslatorException {
		try {

			byte[] sqsSourceBytes = createSourceBytes(sqsSourceInputStream);
			byte[] foBytes = createFOBytes(sqsSourceBytes, systemId, this.language, uriResolver);
			ByteArrayInputStream foInputStream = new ByteArrayInputStream(foBytes);

			if (FOP_OUTPUT_DEBUG) {
				File tmpFile = new File("c:\\tmp\\sqs.fo");
				OutputStream foTest = new BufferedOutputStream(new FileOutputStream(tmpFile));
				FileUtil.connect(foInputStream, foTest);
				foTest.close();
				foInputStream.reset();
			}

			translate(sqsSourceBytes, foInputStream, systemId, pdfOutputStream);

		} catch (IOException ex) {
			ex.printStackTrace();
			throw new TranslatorException(ex);
		}
	}

	protected abstract void translate(byte[] sqsSourceBytes, ByteArrayInputStream foInputStream, String systemId, OutputStream pdfOutputStream) throws TranslatorException;

	protected void render(ContentHandler handler, InputStream foInputStream, String systemId) throws TranslatorException, IOException {
		try {
			// System.out.println("pdfRawDataBytes:"+ pdfRawDataBytes.length);

			// Setup JAXP using identity transformer
			TransformerFactory factory = TransformerFactory.newInstance();
			factory.setURIResolver(uriResolver);
			Transformer transformer = factory.newTransformer();

			// Setup input stream
			Source foInputSource = new StreamSource(foInputStream, systemId);

			// Resulting SAX events (the generated FO) must be piped through to
			// FOP
			Result res = new SAXResult(handler);
			// Start XSLT transformation and FO processing
			transformer.transform(foInputSource, res);

		} catch (TransformerException ex) {
			ex.printStackTrace();
			throw new TranslatorException(ex);
		}
	}
	
	PreviewFrame getPreviewFrame(String title, Renderable renderable, AWTRenderer renderer) {
		if (this.previewFrame == null) {
			this.previewFrame = new PreviewFrame(getTranslatorCore().getUserAgent());
		}
		this.previewFrame.update(title, renderable, renderer);
		return previewFrame;
	}

}
