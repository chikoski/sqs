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

/* $Id: TIFFDocumentHandler.java 820672 2009-10-01 14:48:27Z jeremias $ */

package org.apache.fop.render.bitmap;

import org.apache.fop.apps.MimeConstants;
import org.apache.fop.render.intermediate.IFDocumentHandler;
import org.apache.fop.render.intermediate.IFDocumentHandlerConfigurator;

/**
 * {@link IFDocumentHandler} implementation that produces TIFF files.
 */
public class TIFFDocumentHandler extends AbstractBitmapDocumentHandler {

    /** {@inheritDoc} */
    public String getMimeType() {
        return MimeConstants.MIME_TIFF;
    }

    /** {@inheritDoc} */
    public String getDefaultExtension() {
        return "tif";
    }

    /** {@inheritDoc} */
    public IFDocumentHandlerConfigurator getConfigurator() {
        return new TIFFRendererConfigurator(getUserAgent());
    }

}
