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

/* $Id: PSExtensionHandlerFactory.java 679326 2008-07-24 09:35:34Z vhennebert $ */

package org.apache.fop.render.ps.extensions;

import org.apache.fop.util.ContentHandlerFactory;
import org.xml.sax.ContentHandler;

/**
 * Factory for the ContentHandler that handles serialized PSSetupCode instances.
 */
public class PSExtensionHandlerFactory implements ContentHandlerFactory {

    private static final String[] NAMESPACES = new String[] {PSSetupCode.CATEGORY};

    /** {@inheritDoc} */
    public String[] getSupportedNamespaces() {
        return NAMESPACES;
    }

    /** {@inheritDoc} */
    public ContentHandler createContentHandler() {
        return new PSExtensionHandler();
    }

}
