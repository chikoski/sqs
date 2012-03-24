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

/* $Id: Finalizable.java 750418 2009-03-05 11:03:54Z vhennebert $ */

package org.apache.xmlgraphics.util.io;

/**
 * This interface is used for special FilteredOutputStream classes that won't
 * be closed (since this causes the target OutputStream to be closed, too) but
 * where flush() is not enough, for example because a final marker has to be
 * written to the target stream.
 *
 * @version   $Id: Finalizable.java 750418 2009-03-05 11:03:54Z vhennebert $
 */
public interface Finalizable {

    /**
     * This method can be called instead of close() on a subclass of
     * FilteredOutputStream when a final marker has to be written to the target
     * stream, but close() cannot be called.
     *
     * @exception java.io.IOException  In case of an IO problem
     */
    void finalizeStream()
        throws java.io.IOException;

}
