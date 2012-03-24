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

/* $Id: CommonURIResolverTestCase.java 750418 2009-03-05 11:03:54Z vhennebert $ */

package org.apache.xmlgraphics.util.uri;

import javax.xml.transform.URIResolver;

import junit.framework.TestCase;

/**
 * Test case for the {@link CommonURIResolver}.
 */
public class CommonURIResolverTestCase extends TestCase {

    /**
     * Test the DataURIResolver with correct values.
     *
     * @throws Exception
     *             if an error occurs
     */
    public void testDataURLHandling() throws Exception {
        URIResolver resolver = CommonURIResolver.getDefaultURIResolver();
        DataURIResolverTestCase.actualURLHAndlingTest(resolver);
    }

}
