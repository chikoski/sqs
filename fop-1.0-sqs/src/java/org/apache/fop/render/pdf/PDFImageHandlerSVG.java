/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id: PDFImageHandlerSVG.java 830293 2009-10-27 19:07:52Z vhennebert $ */

package org.apache.fop.render.pdf;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.IOException;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.SVGConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.image.loader.batik.BatikImageFlavors;
import org.apache.fop.render.ImageHandler;
import org.apache.fop.render.RenderingContext;
import org.apache.fop.render.pdf.PDFLogicalStructureHandler.MarkedContentInfo;
import org.apache.fop.svg.PDFAElementBridge;
import org.apache.fop.svg.PDFBridgeContext;
import org.apache.fop.svg.PDFGraphics2D;
import org.apache.fop.svg.SVGEventProducer;
import org.apache.fop.svg.SVGUserAgent;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Image Handler implementation which handles SVG images.
 */
public class PDFImageHandlerSVG implements ImageHandler {

    /** logging instance */
    private static Log log = LogFactory.getLog(PDFImageHandlerSVG.class);

    /** {@inheritDoc} */
    public void handleImage(RenderingContext context, Image image, Rectangle pos)
                throws IOException {
        PDFRenderingContext pdfContext = (PDFRenderingContext)context;
        PDFContentGenerator generator = pdfContext.getGenerator();
        ImageXMLDOM imageSVG = (ImageXMLDOM)image;

        FOUserAgent userAgent = context.getUserAgent();
        final float deviceResolution = userAgent.getTargetResolution();
        if (log.isDebugEnabled()) {
            log.debug("Generating SVG at " + deviceResolution + "dpi.");
        }

        final float uaResolution = userAgent.getSourceResolution();
        SVGUserAgent ua = new SVGUserAgent(userAgent, new AffineTransform());

        //Scale for higher resolution on-the-fly images from Batik
        double s = uaResolution / deviceResolution;
        AffineTransform resolutionScaling = new AffineTransform();
        resolutionScaling.scale(s, s);

        GVTBuilder builder = new GVTBuilder();

        //Controls whether text painted by Batik is generated using text or path operations
        boolean strokeText = false;
        //TODO connect with configuration elsewhere.

        BridgeContext ctx = new PDFBridgeContext(ua,
                (strokeText ? null : pdfContext.getFontInfo()),
                userAgent.getFactory().getImageManager(),
                userAgent.getImageSessionContext(),
                new AffineTransform());

        GraphicsNode root;
        try {
            root = builder.build(ctx, imageSVG.getDocument());
            builder = null;
        } catch (Exception e) {
            SVGEventProducer eventProducer = SVGEventProducer.Provider.get(
                    context.getUserAgent().getEventBroadcaster());
            eventProducer.svgNotBuilt(this, e, image.getInfo().getOriginalURI());
            return;
        }
        // get the 'width' and 'height' attributes of the SVG document
        float w = (float)ctx.getDocumentSize().getWidth() * 1000f;
        float h = (float)ctx.getDocumentSize().getHeight() * 1000f;

        float sx = pos.width / w;
        float sy = pos.height / h;

        //Scaling and translation for the bounding box of the image
        AffineTransform scaling = new AffineTransform(
                sx, 0, 0, sy, pos.x / 1000f, pos.y / 1000f);

        //Transformation matrix that establishes the local coordinate system for the SVG graphic
        //in relation to the current coordinate system
        AffineTransform imageTransform = new AffineTransform();
        imageTransform.concatenate(scaling);
        imageTransform.concatenate(resolutionScaling);

        /*
         * Clip to the svg area.
         * Note: To have the svg overlay (under) a text area then use
         * an fo:block-container
         */
        generator.comment("SVG setup");
        generator.saveGraphicsState();
        if (context.getUserAgent().isAccessibilityEnabled()) {
            MarkedContentInfo mci = pdfContext.getMarkedContentInfo();
            generator.beginMarkedContentSequence(mci.tag, mci.mcid);
        }
        generator.setColor(Color.black, false);
        generator.setColor(Color.black, true);

        if (!scaling.isIdentity()) {
            generator.comment("viewbox");
            generator.add(CTMHelper.toPDFString(scaling, false) + " cm\n");
        }

        //SVGSVGElement svg = ((SVGDocument)doc).getRootElement();

        PDFGraphics2D graphics = new PDFGraphics2D(true, pdfContext.getFontInfo(),
                generator.getDocument(),
                generator.getResourceContext(), pdfContext.getPage().referencePDF(),
                "", 0);
        graphics.setGraphicContext(new org.apache.xmlgraphics.java2d.GraphicContext());

        if (!resolutionScaling.isIdentity()) {
            generator.comment("resolution scaling for " + uaResolution
                        + " -> " + deviceResolution + "\n");
            generator.add(
                    CTMHelper.toPDFString(resolutionScaling, false) + " cm\n");
            graphics.scale(1 / s, 1 / s);
        }

        generator.comment("SVG start");

        //Save state and update coordinate system for the SVG image
        generator.getState().save();
        generator.getState().concatenate(imageTransform);

        // Calcurate where to set the origin point of SVG image
        Point2D ctxo = generator.getState().getTransform().transform(new Point2D.Float(0, 0), null);
        
        storeSVGElementIDToPageRectangle(context.getUserAgent(), imageSVG.getDocument(), 
                pdfContext.getPage().getPageIndex(),
                (float)ctxo.getX(),
                (float)ctxo.getY(),
                (float)ctx.getDocumentSize().getWidth(),
                (float)ctx.getDocumentSize().getHeight());

        //Now that we have the complete transformation matrix for the image, we can update the
        //transformation matrix for the AElementBridge.
        PDFAElementBridge aBridge = (PDFAElementBridge)ctx.getBridge(
                SVGDOMImplementation.SVG_NAMESPACE_URI, SVGConstants.SVG_A_TAG);
        aBridge.getCurrentTransform().setTransform(generator.getState().getTransform());

        graphics.setPaintingState(generator.getState());
        graphics.setOutputStream(generator.getOutputStream());
        try {
            root.paint(graphics);
            generator.add(graphics.getString());
        } catch (Exception e) {
            SVGEventProducer eventProducer = SVGEventProducer.Provider.get(
                    context.getUserAgent().getEventBroadcaster());
            eventProducer.svgRenderingError(this, e, image.getInfo().getOriginalURI());
        }
        generator.getState().restore();
        if (context.getUserAgent().isAccessibilityEnabled()) {
            generator.restoreGraphicsStateAccess();
        } else {
            generator.restoreGraphicsState();
        }
        generator.comment("SVG end");
    }

	private void storeSVGElementIDToPageRectangle(FOUserAgent ua, Document doc, int pageIndex, 
			float x, float y, float w, float h) {
		Node idNode = doc.getFirstChild().getAttributes().getNamedItem("id");
        if(idNode != null){
        	String id = ((Attr)idNode).getValue();
        	SVGElementIDToPageRectangleMap map = SVGElementIDToPageRectangleMap.getInstance();
        	map.put(ua, pageIndex, id, new PageRectangle(pageIndex, x, y, w, h, getMetadata(doc)));
        }
	}

	private Node getMetadata(Document doc){
		final String SVG_URI = "http://www.w3.org/2000/svg";
		//return doc.getFirstChild().getFirstChild();
		Node svgNode = doc.getFirstChild();//(Element)(doc.getElementsByTagNameNS(SVG_URI, "svg").item(0));
		if(svgNode.getNodeType() == Node.ELEMENT_NODE){
			NodeList metadataNodeList = ((Element)svgNode).getElementsByTagNameNS(SVG_URI, "metadata");
			if(metadataNodeList == null || 0 == metadataNodeList.getLength()){
				return null;
			}
			return metadataNodeList.item(0);
		}
		return null;
	}

    /** {@inheritDoc} */
    public int getPriority() {
        return 400;
    }

    /** {@inheritDoc} */
    public Class getSupportedImageClass() {
        return ImageXMLDOM.class;
    }

    /** {@inheritDoc} */
    public ImageFlavor[] getSupportedImageFlavors() {
        return new ImageFlavor[] {
                BatikImageFlavors.SVG_DOM
            };
    }

    /** {@inheritDoc} */
    public boolean isCompatible(RenderingContext targetContext, Image image) {
        return (image == null
                || (image instanceof ImageXMLDOM
                        && image.getFlavor().isCompatible(BatikImageFlavors.SVG_DOM)))
                && targetContext instanceof PDFRenderingContext;
    }

}
