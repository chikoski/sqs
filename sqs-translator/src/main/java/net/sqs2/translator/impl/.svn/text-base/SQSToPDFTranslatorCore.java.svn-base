/**
 * 
 */
package net.sqs2.translator.impl;

import java.io.IOException;



import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.transform.URIResolver;

import net.sqs2.net.ClassURLConnection;
import net.sqs2.translator.ParamEntry;
import net.sqs2.translator.TranslatorException;
import net.sqs2.translator.XSLTranslator;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.render.awt.AWTRenderer;
import org.xml.sax.SAXException;

class SQSToPDFTranslatorCore {

	static final String[] SQS2FO = { "cmpl-label.xsl", "cmpl-ref.xsl", "embed-counter.xsl",
		"embed-link.xsl", "convert1.xsl", "convert2.xsl", "convert3.xsl" };

	//private final FOPTranslator fopTranslator;
	private XSLTranslator xslTranslator;
	
	private FopFactory fopFactory;
	private FOUserAgent userAgent;

	private String userCustomizedXSLTFileBaseURI;
	private String language;

	public SQSToPDFTranslatorCore(String fopBaseURL, String baseURI, String userCostomizedXSLTFileBaseURI, String language, 
			URIResolver uriResolver, PageSetting pageSetting)
			throws TranslatorException {
		this.language = language;
		try {
			this.userCustomizedXSLTFileBaseURI = userCostomizedXSLTFileBaseURI;
			this.xslTranslator = new XSLTranslator();
			this.xslTranslator.initialize(new String[] { userCustomizedXSLTFileBaseURI, baseURI },
					SQS2FO, createParameterArrayMap(pageSetting));

			String fopConfigFile = "userconfig_"+language+".xml";
			
			URL fopConfigURL = new URL(fopBaseURL+fopConfigFile);
			InputStream in = null;
			try {
				this.fopFactory = FopFactory.newInstance();
				this.fopFactory.setURIResolver(uriResolver);
				this.fopFactory.getFontManager().setFontBaseURL(TranslatorJarURIContext.getFontBaseURI());
				this.fopFactory.setBaseURL(TranslatorJarURIContext.getXSLTBaseURI());
				
				DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
				//Configuration cfg = cfgBuilder.build(getClass().getClassLoader().getResourceAsStream(new URL(fopConfigFile)));
				in = fopConfigURL.openStream();
				Configuration cfg = cfgBuilder.build(in);
				this.fopFactory.setUserConfig(cfg);
			} catch (MalformedURLException ex) {
				Logger.getLogger(getClass().getName()).warning("ERROR not found:"+fopConfigURL);
				ex.printStackTrace();
			} catch (SAXException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}finally{
				/*
				if(in != null){
					try{
						in.close();
					}catch(IOException ignore){}
				}
				*/
			}

			this.userAgent = createFOUserAgent(pageSetting);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new TranslatorException(ex);
		}
	}
	
	FOUserAgent getUserAgent() {
		return this.userAgent;
	}

	AWTRenderer createAWTRenderer() {
		AWTRenderer renderer = new AWTRenderer();
		renderer.setPreviewDialogDisplayed(false);
		renderer.setUserAgent(this.userAgent);
		this.userAgent.setRendererOverride(renderer);
		return renderer;
	}

	private Map<String, ParamEntry[]> createParameterArrayMap(PageSetting pageSetting) {
		Map<String, ParamEntry[]> ret = new HashMap<String, ParamEntry[]>();
		ret.put("embed-counter.xsl", new ParamEntry[] { new ParamEntry("xhtml.h-attribute..sqs.prefix", "問"),
				new ParamEntry("xhtml.h-attribute..sqs.suffix", "."),
				new ParamEntry("xhtml.h-attribute..sqs.format", "1"),
				new ParamEntry("sqs.counter-attribute..sqs.prefix", "("),
				new ParamEntry("sqs.counter-attribute..sqs.suffix", ")"),
				new ParamEntry("sqs.counter-attribute..sqs.format", "1") });

		ret.put("convert2.xsl", new ParamEntry[] { new ParamEntry("xforms.hint-attribute..sqs.prefix", ""),
				new ParamEntry("xforms.hint-attribute..sqs.suffix", ""),
				new ParamEntry("xforms.hint-attribute..sqs.display", "inline"),
				new ParamEntry("xforms.help-attribute..sqs.prefix", "("),
				new ParamEntry("xforms.help-attribute..sqs.suffix", ")"),
				new ParamEntry("xforms.help-attribute..sqs.display", "inline"),
				new ParamEntry("xforms.alart-attribute..sqs.prefix", "*"),
				new ParamEntry("xforms.alart-attribute..sqs.suffix", ""),
				new ParamEntry("xforms.alart-attribute..sqs.display", "inline") });

		ret.put("convert3.xsl", new ParamEntry[] { new ParamEntry("example-blank-mark-label", " : 空白マーク"),
				new ParamEntry("language", this.language),
				new ParamEntry("example-filled-mark-label", ": 正しいぬりつぶし"),
				new ParamEntry("example-incomplete-mark-label", ": 不十分なぬりつぶし"),
				new ParamEntry("characters-prohibit-line-break", "。．、，’”）｝」』〕】〉》々〜…ーぁぃぅぇぉっゃゅょゎァィゥェォッャュョヮ)'"),
				new ParamEntry("pageMasterPageWidth", Double.toString(pageSetting.getWidth())),//595
				new ParamEntry("pageMasterPageHeight", Double.toString(pageSetting.getHeight())),//842
				new ParamEntry("pageMasterMarginTop", "0"),
				new ParamEntry("pageMasterMarginBottom", "0"),
				new ParamEntry("pageMasterMarginLeft", "0"),
				new ParamEntry("pageMasterMarginRight", "0"),
				new ParamEntry("regionBodyMarginTop", "70"),
				new ParamEntry("regionBodyMarginBottom", "70"),
				new ParamEntry("regionBodyMarginLeft", "44"),
				new ParamEntry("regionBodyMarginRight", "44"),
				new ParamEntry("regionBeforeExtent", "10"),
				new ParamEntry("regionAfterExtent", "60"),
				new ParamEntry("regionStartExtent", "10"),
				new ParamEntry("regionEndExtent", "60"),
				
				new ParamEntry("deskewGuideAreaWidth","434"),
				new ParamEntry("deskewGuideAreaHeight","55"),
				new ParamEntry("deskewGuideBlockWidth","18"),
				new ParamEntry("deskewGuideBlockHeight","18"),

				new ParamEntry("pageSideStartingFrom", "left"),
				new ParamEntry("showStapleMark", "true"),
				new ParamEntry("showPageNumber", "true"),
				new ParamEntry("showEnqtitleBelowPagenum", "true"),
				new ParamEntry("showMarkingExample", "true"),
				new ParamEntry("sides", "duplex"),

				new ParamEntry("fontFamily", "Gothic"),
				new ParamEntry("baseFontSizePt", "11")
		
		/*
		 * “‘（｛「『〔【〈《(￥＄
		 */
		});
		return ret;
	}

	private FOUserAgent createFOUserAgent(PageSetting pageSetting) {
		FOUserAgent userAgent;
		userAgent = this.fopFactory.newFOUserAgent();
		userAgent.setProducer("SQS Translator");
		userAgent.setCreator("SQS Translator");// TODO FOP: CreatorInfo from sqs document
									// xpath /html/head/meta
		userAgent.setAuthor("SQS User");// TODO FOP: AuthorInfo from sqs document
								// xpath /html/head/meta
		userAgent.setCreationDate(new Date());
		userAgent.setTitle("SQS OMR From"); // TODO FOP: Title from sqs document xpath
								// /html/head/title
		userAgent.setKeywords("SQS XML XSL-FO");
		//userAgent.setURIResolver(FOPTranslator.uriResolver);
		userAgent.setBaseURL(TranslatorJarURIContext.getXSLTBaseURI());
		userAgent.getRendererOptions().put("pageSetting", pageSetting);
		return userAgent;
	}

	Fop createFop(FOUserAgent userAgent, String outputFormat) throws FOPException {
		return this.fopFactory.newFop(outputFormat, userAgent);
	}

	Fop createFop(OutputStream pdfOutputStream) throws FOPException {
		return this.fopFactory.newFop(MimeConstants.MIME_PDF, this.userAgent, pdfOutputStream);
	}

	Fop createFopAreaTree(OutputStream areaTreeOutputStream) throws FOPException {
		return this.fopFactory.newFop(MimeConstants.MIME_FOP_AREA_TREE, this.userAgent,
				areaTreeOutputStream);
	}

	void execute(InputStream sqsInputStream, String systemId,  OutputStream foOutputStream, URIResolver uriResolver) throws TranslatorException {
		this.xslTranslator.execute(sqsInputStream, systemId, foOutputStream,  uriResolver);
	}
}