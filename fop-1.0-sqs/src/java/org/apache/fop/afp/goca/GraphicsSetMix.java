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

/* $Id: GraphicsSetMix.java 746664 2009-02-22 12:40:44Z jeremias $ */

package org.apache.fop.afp.goca;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Sets the foreground mix mode.
 */
public class GraphicsSetMix extends AbstractGraphicsDrawingOrder {

    public static final byte MODE_DEFAULT = 0x00;
    public static final byte MODE_OVERPAINT = 0x02;

    /** the mix mode value */
    private final byte mode;

    /**
     * Main constructor
     *
     * @param mode the mix mode value
     */
    public GraphicsSetMix(byte mode) {
        this.mode = mode;
    }

    /** {@inheritDoc} */
    public void writeToStream(OutputStream os) throws IOException {
        byte[] data = new byte[] {
           0x0C, // GSMX order code
           mode // MODE (mix mode value)
        };
        os.write(data);
    }

    /** {@inheritDoc} */
    public String toString() {
        return "GraphicsSetMix{mode=" + mode + "}";
    }

    /** {@inheritDoc} */
    byte getOrderCode() {
        return 0x0C;
    }

    /** {@inheritDoc} */
    public int getDataLength() {
        return 2;
    }

}
