/*

 SQSToPreviewTranslator.java

 Copyright 2004 KUBO Hiroya (hiroya@sfc.keio.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2007/09/04

 */
package net.sqs2.translator.impl;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.URIResolver;

import net.sqs2.translator.TranslatorException;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.render.awt.AWTRenderer;
import org.apache.fop.render.awt.viewer.Renderable;

public class SQSToPreviewTranslator extends FOPTranslator {
	/*
	 * static File atFile; static Renderable atRenderable;
	 */
	static AWTRenderer renderer;

	static {
	}

	String title;

	public SQSToPreviewTranslator(String groupID, String appID, String fopURL, String xsltURL, String title, 
			URIResolver uriResolver, PageSetting pageSetting)
			throws TranslatorException {
		super(groupID, appID, fopURL, xsltURL, uriResolver, pageSetting);
		this.title = title;
		try {
			/*
			 * atFile = File.createTempFile("sqs", ".at");
			 * atFile.deleteOnExit(); atRenderable = new
			 * AreaTreeInputHandler(atFile);
			 */
			renderer = getTranslatorCore().createAWTRenderer();
		} catch (Exception ignore) {
			ignore.printStackTrace();
		}
	}

	protected void translate(byte[] sqsSourceBytes, final ByteArrayInputStream foInputStream, final String systemId, OutputStream dummyOutputStream) throws TranslatorException {
		getTranslatorCore().getUserAgent().setBaseURL(systemId);

		Renderable renderable = new Renderable() {
			public void renderTo(FOUserAgent userAgent, String outputFormat) throws FOPException {
				try {
					Fop fop = getTranslatorCore().createFop(userAgent, outputFormat);
					foInputStream.reset();
					render(fop.getDefaultHandler(), foInputStream, systemId);
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (TranslatorException ex) {
					ex.printStackTrace();
				}
			}
		};
		/*
		 * try{ //Create an instance of the target renderer so the XMLRenderer
		 * can use its font setup Renderer targetRenderer =
		 * core.getUserAgent().getRendererFactory().createRenderer(
		 * core.getUserAgent(), MimeConstants.MIME_PDF);
		 * 
		 * //Create the XMLRenderer to create the intermediate format (area tree
		 * XML) XMLRenderer xmlRenderer = new XMLRenderer();
		 * xmlRenderer.setUserAgent(core.getUserAgent());
		 * 
		 * //Tell the XMLRenderer to mimic the target renderer
		 * xmlRenderer.mimicRenderer(targetRenderer);
		 * 
		 * //Make sure the prepared XMLRenderer is used
		 * core.getUserAgent().setRendererOverride(xmlRenderer);
		 * 
		 * OutputStream areaTreeOutputStream = new BufferedOutputStream(new
		 * FileOutputStream(atFile));
		 * 
		 * Fop fop = core.createFopAreaTree(areaTreeOutputStream); //Fop fop =
		 * core.createFopAreaTree(areaTreeOutputStream);
		 * render(fop.getDefaultHandler(), foInputStream);
		 * areaTreeOutputStream.close(); }catch(IOException ex){
		 * ex.printStackTrace(); }catch(FOPException ex){ ex.printStackTrace();
		 * }catch(TranslatorException ex){ ex.printStackTrace(); }
		 */
		PreviewFrame frame = getPreviewFrame(title, renderable, renderer);
		frame.setVisible(true);

		try {
			frame.render(renderable, renderer);
			frame.show(renderer);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/*
	 * 
	 * protected void translate(byte[] sqsSourceBytes, final
	 * ByteArrayInputStream foInputStream, OutputStream dummyOutputStream)throws
	 * TranslatorException{ Renderable renderable = new Renderable(){ public
	 * void renderTo(FOUserAgent userAgent, String outputFormat) throws
	 * FOPException { Fop fop = core.createFop(userAgent, outputFormat); try{
	 * foInputStream.reset(); render(fop, foInputStream); }catch(IOException
	 * ex){ ex.printStackTrace(); }catch(TranslatorException ex){
	 * ex.printStackTrace(); } } }; AWTRenderer renderer =
	 * core.createAWTRenderer(); JFrame frame = core.getFrame(title, renderable,
	 * renderer); frame.setVisible(true);
	 * 
	 * try{ core.render(renderable, renderer); core.show(renderer);
	 * }catch(Exception ex){ ex.printStackTrace(); } }
	 */
}
