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

/* $Id: ImageUtilTestCase.java 750418 2009-03-05 11:03:54Z vhennebert $ */

package org.apache.xmlgraphics.image.loader;

import junit.framework.TestCase;

import org.apache.xmlgraphics.image.loader.util.ImageUtil;

/**
 * Tests for the ImageUtil class.
 */
public class ImageUtilTestCase extends TestCase {

    /**
     * Tests {@link ImageUtil.needPageIndexFromURI(String)}.
     * @throws Exception if an error occurs
     */
    public void testNeedPageIndex() throws Exception {
        int pageIndex;

        pageIndex = ImageUtil.needPageIndexFromURI("http://localhost/images/scan1.tif");
        assertEquals(0, pageIndex);
        pageIndex = ImageUtil.needPageIndexFromURI("http://localhost/images/scan1.tif#page=3");
        assertEquals(2, pageIndex);
        pageIndex = ImageUtil.needPageIndexFromURI("http://localhost/images/scan1.tif#page=0");
        assertEquals(0, pageIndex);
        pageIndex = ImageUtil.needPageIndexFromURI("http://localhost/images/scan1.tif#page=");
        assertEquals(0, pageIndex);
        pageIndex = ImageUtil.needPageIndexFromURI("http://localhost/images/scan1.tif#page=x");
        assertEquals(0, pageIndex);
        pageIndex = ImageUtil.needPageIndexFromURI("http://localhost/images/scan1.tif#page=-1");
        assertEquals(0, pageIndex);
        pageIndex = ImageUtil.needPageIndexFromURI("#page=2");
        assertEquals(1, pageIndex);

        //Not a valid URI
        try {
            pageIndex = ImageUtil.needPageIndexFromURI("C:\\images\\scan1.tif#page=44");
            fail("Invalid URI. Method must fail.");
        } catch (IllegalArgumentException e) {
            //expected
        }
        //Valid URI
        pageIndex = ImageUtil.needPageIndexFromURI("file:///C:/images/scan1.tif#page=44");
        assertEquals(43, pageIndex);
    }

    /**
     * Tests {@link ImageUtil.getPageIndexFromURI(String)}.
     * @throws Exception if an error occurs
     */
    public void testGetPageIndex() throws Exception {
        Integer pageIndex;

        pageIndex = ImageUtil.getPageIndexFromURI("http://localhost/images/scan1.tif");
        assertNull(pageIndex);
        pageIndex = ImageUtil.getPageIndexFromURI("http://localhost/images/scan1.tif#page=3");
        assertEquals(2, pageIndex.intValue());
        //Note: no detailed test anymore as this is tested through needPageIndexFromURI().

        //getPageIndexFromURI only works on URIs, so ignore anything that doesn't have a '#'
        pageIndex = ImageUtil.getPageIndexFromURI("C:\\Temp\\scan1.tif");
        assertNull(pageIndex);
    }

}
