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

/* $Id: IFDocumentHandler.java 820672 2009-10-01 14:48:27Z jeremias $ */

package org.apache.fop.render.intermediate;

import java.awt.Dimension;

import javax.xml.transform.Result;

import org.apache.fop.fonts.FontInfo;

/**
 * Interface used to paint whole documents layouted by Apache FOP.
 * <p>
 * Call sequence:
 * <p>
 * <pre>
 * startDocument()
 *   startDocumentHeader()
 *   [handleExtension()]*
 *   endDocumentHeader()
 *   [
 *   startPageSequence()
 *     [
 *     startPage()
 *       startPageHeader()
 *         [handleExtension()]*
 *       endPageHeader()
 *       startPageContent()
 *         (#box)+
 *       endPageContent()
 *       startPageTrailer()
 *         (addTarget())*
 *       endPageTrailer()
 *     endPage()
 *     ]*
 *   endPageSequence()
 *   ]*
 *   startDocumentTrailer()
 *   [handleExtension()]*
 *   endDocumentTrailer()
 * endDocument()
 *
 * #box:
 * startBox() (#pageContent)+ endBox() |
 * startViewport() (#pageContext)+ endViewport()
 *
 * #pageContent:
 * (
 *   setFont() |
 *   drawText() |
 *   drawRect() |
 *   drawImage() |
 *   TODO etc. etc. |
 *   handleExtensionObject()
 * )
 * </pre>
 */
public interface IFDocumentHandler {

    /**
     * Sets the intermediate format context object.
     * @param context the context object
     */
    void setContext(IFContext context);

    /**
     * Returns the associated intermediate format context object.
     * @return the context object
     */
    IFContext getContext();

    /**
     * Sets the JAXP Result object to receive the generated content.
     * @param result the JAXP Result object to receive the generated content
     * @throws IFException if an error occurs setting up the output
     */
    void setResult(Result result) throws IFException;

    /**
     * Sets the font set to work with.
     * @param fontInfo the font info object
     */
    void setFontInfo(FontInfo fontInfo);

    /**
     * Returns the font set to work with.
     * @return the font info object
     */
    FontInfo getFontInfo();

    /**
     * Sets the default font set (with no custom configuration).
     * @param fontInfo the font info object to populate
     */
    void setDefaultFontInfo(FontInfo fontInfo);

    /**
     * Returns the configurator for this document handler, if any.
     * @return the configurator or null if there's no configurator
     */
    IFDocumentHandlerConfigurator getConfigurator();

    /**
     * Returns a document navigation handler if this feature is supported.
     * @return the document navigation handler or null if not supported
     */
    IFDocumentNavigationHandler getDocumentNavigationHandler();

    /**
     * Indicates whether the painter supports to handle the pages in mixed order rather than
     * ascending order.
     * @return true if out-of-order handling is supported
     */
    boolean supportsPagesOutOfOrder();

    /**
     * Returns the MIME type of the output format that is generated by this implementation.
     * @return the MIME type
     */
    String getMimeType();

    /**
     * Indicates the start of a document. This method may only be called once before any other
     * event method.
     * @throws IFException if an error occurs while handling this event
     */
    void startDocument() throws IFException;

    /**
     * Indicates the end of a document. This method may only be called once after the whole
     * document has been handled. Implementations can release resources (close streams). It is
     * an error to call any event method after this method.
     * @throws IFException if an error occurs while handling this event
     */
    void endDocument() throws IFException;

    /**
     * Indicates the start of the document header. This method is called right after the
     * {@link #startDocument()} method. Extensions sent to this painter between
     * {@link #startDocumentHeader()} and {@link #endDocumentHeader()} apply to the document as
     * a whole (like document metadata).
     * @throws IFException if an error occurs while handling this event
     */
    void startDocumentHeader() throws IFException;

    /**
     * Indicates the end of the document header. This method is called before the first
     * page sequence.
     * @throws IFException if an error occurs while handling this event
     */
    void endDocumentHeader() throws IFException;

    /**
     * Indicates the start of the document trailer. This method is called after the last
     * page sequence. Extensions sent to the painter between
     * {@link #startDocumentTrailer()} and {@link #endDocumentTrailer()} apply to the document as
     * a whole and is used for document-level content that is only known after all pages have
     * been rendered (like named destinations or the bookmark tree).
     * @throws IFException if an error occurs while handling this event
     */
    void startDocumentTrailer() throws IFException;

    /**
     * Indicates the end of the document trailer. This method is called right before the
     * {@link #endDocument()} method.
     * @throws IFException if an error occurs while handling this event
     */
    void endDocumentTrailer() throws IFException;

    /**
     * Indicates the start of a new page sequence.
     * @param id the page sequence's identifier (or null if none is available)
     * @throws IFException if an error occurs while handling this event
     */
    void startPageSequence(String id) throws IFException;
    /**
     * Indicates the end of a page sequence.
     * @throws IFException if an error occurs while handling this event
     */
    void endPageSequence() throws IFException;

    /**
     * Indicates the start of a new page.
     * @param index the index of the page (0-based)
     * @param name the page name (usually the formatted page number)
     * @param pageMasterName the name of the simple-page-master that generated this page
     * @param size the size of the page (equivalent to the MediaBox in PDF)
     * @throws IFException if an error occurs while handling this event
     */
    void startPage(int index, String name, String pageMasterName, Dimension size)
        throws IFException;

    /**
     * Indicates the end of a page
     * @throws IFException if an error occurs while handling this event
     */
    void endPage() throws IFException;

    /**
     * Indicates the start of the page header.
     * @throws IFException if an error occurs while handling this event
     */
    void startPageHeader() throws IFException;

    /**
     * Indicates the end of the page header.
     * @throws IFException if an error occurs while handling this event
     */
    void endPageHeader() throws IFException;

    /**
     * Indicates the start of the page content. The method returns an {@link IFPainter} interface
     * which is used to paint the page contents.
     * @throws IFException if an error occurs while handling this event
     * @return the IFPainter for the page content
     */
    IFPainter startPageContent() throws IFException;

    /**
     * Indicates the end of the page content. Calls to the {@link IFPainter} returned by the
     * respective {@link #startPageContent()} method are illegal.
     * @throws IFException if an error occurs while handling this event
     */
    void endPageContent() throws IFException;

    /**
     * Indicates the start of the page trailer. The page trailer is used for writing down page
     * elements which are only know after handling the page itself (like PDF targets).
     * @throws IFException if an error occurs while handling this event
     */
    void startPageTrailer() throws IFException;

    /**
     * Indicates the end of the page trailer.
     * @throws IFException if an error occurs while handling this event
     */
    void endPageTrailer() throws IFException;

    /**
     * Handles an extension object. This can be a DOM document or any arbitrary
     * object. If an implementation doesn't know how to handle a particular extension it is simply
     * ignored.
     * @param extension the extension object
     * @throws IFException if an error occurs while handling this event
     */
    void handleExtensionObject(Object extension) throws IFException;

    //TODO Prototype the following:
    //ContentHandler handleExtension() throws Exception
}
