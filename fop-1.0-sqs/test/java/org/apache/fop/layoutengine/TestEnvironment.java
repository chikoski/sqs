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

/* $Id: TestEnvironment.java 746664 2009-02-22 12:40:44Z jeremias $ */

package org.apache.fop.layoutengine;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;

import org.apache.fop.apps.FopFactory;

/**
 * Test environment and helper code for running FOP tests.
 */
public class TestEnvironment {

    // configure fopFactory as desired
    private FopFactory fopFactory = FopFactory.newInstance();
    private FopFactory fopFactoryWithBase14Kerning = FopFactory.newInstance();

    private SAXTransformerFactory tfactory
            = (SAXTransformerFactory)SAXTransformerFactory.newInstance();

    private DocumentBuilderFactory domBuilderFactory;

    private Templates testcase2fo;
    private Templates testcase2checks;

    /**
     * Main constructor.
     */
    public TestEnvironment() {
        fopFactory.getFontManager().setBase14KerningEnabled(false);
        fopFactoryWithBase14Kerning.getFontManager().setBase14KerningEnabled(true);
        domBuilderFactory = DocumentBuilderFactory.newInstance();
        domBuilderFactory.setNamespaceAware(true);
        domBuilderFactory.setValidating(false);
    }

    /**
     * Returns the stylesheet for convert extracting the XSL-FO part from the test case.
     * @return the stylesheet
     * @throws TransformerConfigurationException if an error occurs loading the stylesheet
     */
    public Templates getTestcase2FOStylesheet() throws TransformerConfigurationException {
        if (testcase2fo == null) {
            //Load and cache stylesheet
            Source src = new StreamSource(new File("test/layoutengine/testcase2fo.xsl"));
            testcase2fo = tfactory.newTemplates(src);
        }
        return testcase2fo;
    }

    /**
     * Returns the stylesheet for convert extracting the checks from the test case.
     * @return the stylesheet
     * @throws TransformerConfigurationException if an error occurs loading the stylesheet
     */
    public Templates getTestcase2ChecksStylesheet() throws TransformerConfigurationException {
        if (testcase2checks == null) {
            //Load and cache stylesheet
            Source src = new StreamSource(new File("test/layoutengine/testcase2checks.xsl"));
            testcase2checks = tfactory.newTemplates(src);
        }
        return testcase2checks;
    }

    public FopFactory getFopFactory(boolean base14KerningEnabled) {
        FopFactory effFactory = (base14KerningEnabled ? fopFactoryWithBase14Kerning : fopFactory);
        return effFactory;
    }

    public FopFactory getFopFactory(Document testDoc) {
        boolean base14KerningEnabled = isBase14KerningEnabled(testDoc);
        FopFactory effFactory = getFopFactory(base14KerningEnabled);

        boolean strictValidation = isStrictValidation(testDoc);
        effFactory.setStrictValidation(strictValidation);

        return effFactory;
    }

    private boolean isBase14KerningEnabled(Document testDoc) {
        try {
            XObject xo = XPathAPI.eval(testDoc, "/testcase/cfg/base14kerning");
            String s = xo.str();
            return ("true".equalsIgnoreCase(s));
        } catch (TransformerException e) {
            throw new RuntimeException("Error while evaluating XPath expression", e);
        }
    }

    private boolean isStrictValidation(Document testDoc) {
        try {
            XObject xo = XPathAPI.eval(testDoc, "/testcase/cfg/strict-validation");
            return !("false".equalsIgnoreCase(xo.str()));
        } catch (TransformerException e) {
            throw new RuntimeException("Error while evaluating XPath expression", e);
        }
    }

    /**
     * Loads a test case into a DOM document.
     * @param testFile the test file
     * @return the loaded test case
     * @throws IOException if an I/O error occurs loading the test case
     */
    public Document loadTestCase(File testFile)
            throws IOException {
        try {
            DocumentBuilder builder = domBuilderFactory.newDocumentBuilder();
            Document testDoc = builder.parse(testFile);
            return testDoc;
        } catch (Exception e) {
            throw new IOException("Error while loading test case: " + e.getMessage());
        }
    }

    /**
     * Serialize the DOM for later inspection.
     * @param doc the DOM document
     * @param target target file
     * @throws TransformerException if a problem occurs during serialization
     */
    public void saveDOM(Document doc, File target) throws TransformerException {
        Transformer transformer = getTransformerFactory().newTransformer();
        Source src = new DOMSource(doc);
        Result res = new StreamResult(target);
        transformer.transform(src, res);
    }

    /**
     * Returns the SAXTransformerFactory.
     * @return the SAXTransformerFactory
     */
    public SAXTransformerFactory getTransformerFactory() {
        return tfactory;
    }
}
