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

/* $Id: DataURIResolverTestCase.java 750418 2009-03-05 11:03:54Z vhennebert $ */

package org.apache.xmlgraphics.util.uri;

import java.io.ByteArrayInputStream;

import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

/**
 * Test case for the RFC 2397 data URL/URI resolver.
 */
public class DataURIResolverTestCase extends TestCase {

    private static final byte[] TESTDATA = new byte[] { 0, 1, 2, 3, 4, 5 };

    /**
     * Tests DataURLUtil.
     *
     * @throws Exception
     *             if an error occurs
     */
    public void testRFC2397Generator() throws Exception {
        String url = DataURLUtil.createDataURL(new ByteArrayInputStream(
                TESTDATA), null);
        assertEquals("Generated data URL is wrong", "data:;base64,AAECAwQF",
                url);

        url = DataURLUtil.createDataURL(new ByteArrayInputStream(TESTDATA),
                "application/pdf");
        assertEquals("Generated data URL is wrong",
                "data:application/pdf;base64,AAECAwQF", url);
    }

    /**
     * Test the URIResolver contract if the protocol doesn't match. Resolver
     * must return null in this case.
     *
     * @throws Exception
     *             if an error occurs
     */
    public void testNonMatchingContract() throws Exception {
        URIResolver resolver = new DataURIResolver();
        Source src;

        src = resolver.resolve("http://xmlgraphics.apache.org/fop/index.html",
                null);
        assertNull(src);

        src = resolver.resolve("index.html",
                "http://xmlgraphics.apache.org/fop/");
        assertNull(src);
    }

    private static boolean byteCmp(byte[] src, int srcOffset, byte[] cmp) {
        for (int i = 0, c = cmp.length; i < c; i++) {
            if (src[srcOffset + i] != cmp[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Test the DataURIResolver with correct values.
     *
     * @throws Exception
     *             if an error occurs
     */
    public void testDataURLHandling() throws Exception {
        URIResolver resolver = new DataURIResolver();
        actualURLHAndlingTest(resolver);
    }

    static final void actualURLHAndlingTest(URIResolver resolver)
            throws Exception {
        Source src;

        src = resolver.resolve("data:;base64,AAECAwQF", null);
        assertNotNull(src);
        StreamSource streamSource = (StreamSource) src;
        byte[] data = IOUtils.toByteArray(streamSource.getInputStream());
        assertTrue("Decoded data doesn't match the test data", byteCmp(
                TESTDATA, 0, data));

        src = resolver
                .resolve(
                        "data:application/octet-stream;interpreter=fop;base64,AAECAwQF",
                        null);
        assertNotNull(src);
        streamSource = (StreamSource) src;
        assertNotNull(streamSource.getInputStream());
        assertNull(streamSource.getReader());
        data = IOUtils.toByteArray(streamSource.getInputStream());
        assertTrue("Decoded data doesn't match the test data", byteCmp(
                TESTDATA, 0, data));

        src = resolver.resolve("data:,FOP", null);
        assertNotNull(src);
        streamSource = (StreamSource) src;
        assertNull(streamSource.getInputStream());
        assertNotNull(streamSource.getReader());
        String text = IOUtils.toString(streamSource.getReader());
        assertEquals("FOP", text);

        src = resolver.resolve("data:,A%20brief%20note", null);
        assertNotNull(src);
        streamSource = (StreamSource) src;
        text = IOUtils.toString(streamSource.getReader());
        assertEquals("A brief note", text);

        src = resolver.resolve("data:text/plain;charset=iso-8859-7,%be%f9%be", null);
        assertNotNull(src);
        streamSource = (StreamSource) src;
        text = IOUtils.toString(streamSource.getReader());
        assertEquals("\u038e\u03c9\u038e", text);
    }

}
