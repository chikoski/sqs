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

/* $Id: AbstractFOPTextElementBridge.java 766594 2009-04-20 06:50:59Z jeremias $ */

package org.apache.fop.svg;

import org.w3c.dom.Element;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.SVGTextElementBridge;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.TextNode;
import org.apache.batik.gvt.TextPainter;

/**
 * Bridge class for the &lt;text> element.
 * This bridge will use the direct text painter if the text
 * for the element is simple.
 *
 * @author <a href="mailto:keiron@aftexsw.com">Keiron Liddle</a>
 */
public abstract class AbstractFOPTextElementBridge extends SVGTextElementBridge {

    /** text painter */
    protected TextPainter textPainter;

    /**
     * Main constructor
     *
     * @param textPainter the text painter
     */
    public AbstractFOPTextElementBridge(TextPainter textPainter) {
        this.textPainter = textPainter;
    }

    /**
     * Create a text element bridge.
     *
     * This set the text painter on the node if the text is simple.
     * @param ctx the bridge context
     * @param e the svg element
     * @return the text graphics node created by the super class
     */
    public GraphicsNode createGraphicsNode(BridgeContext ctx, Element e) {
        GraphicsNode node = super.createGraphicsNode(ctx, e);
        if (node != null) {
            //Set our own text painter
            ((TextNode)node).setTextPainter(textPainter);
        }
        return node;
    }

}

