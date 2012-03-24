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

/* $Id: ListenerTestCase.java 727407 2008-12-17 15:05:45Z jeremias $ */

package org.apache.xmlgraphics.ps.dsc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import org.apache.xmlgraphics.ps.DSCConstants;
import org.apache.xmlgraphics.ps.dsc.events.DSCComment;
import org.apache.xmlgraphics.ps.dsc.events.DSCCommentLanguageLevel;
import org.apache.xmlgraphics.ps.dsc.events.DSCEvent;

/**
 * Tests the listener functionality on the DSC parser.
 */
public class ListenerTestCase extends TestCase {

    /**
     * Tests {@link DSCParser#setFilter(DSCFilter)}.
     * @throws Exception if an error occurs
     */
    public void testFilter() throws Exception {
        InputStream in = getClass().getResourceAsStream("test1.txt");
        try {
            DSCParser parser = new DSCParser(in);
            parser.setFilter(new DSCFilter() {

                public boolean accept(DSCEvent event) {
                    //Filter out all non-DSC comments
                    return !event.isComment();
                }

            });
            while (parser.hasNext()) {
                DSCEvent event = parser.nextEvent();

                if (parser.getCurrentEvent().isComment()) {
                    fail("Filter failed. Comment found.");
                }
            }
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * Tests listeners on DSCParser.
     * @throws Exception if an error occurs
     */
    public void testListeners() throws Exception {
        InputStream in = getClass().getResourceAsStream("test1.txt");
        try {
            final Map results = new java.util.HashMap();
            DSCParser parser = new DSCParser(in);

            //Filter the prolog
            parser.addListener(new DSCListener() {
                public void processEvent(DSCEvent event, DSCParser parser)
                        throws IOException, DSCException {
                    if (event.isDSCComment()) {
                        DSCComment comment = event.asDSCComment();
                        if (DSCConstants.BEGIN_PROLOG.equals(comment.getName())) {
                            //Skip until end of prolog
                            while (parser.hasNext()) {
                                DSCEvent e = parser.nextEvent();
                                if (e.isDSCComment()) {
                                    if (DSCConstants.END_PROLOG.equals(
                                            e.asDSCComment().getName())) {
                                        parser.next();
                                        break;
                                    }
                                }

                            }
                        }
                    }
                }
            });

            //Listener for the language level
            parser.addListener(new DSCListener() {
                public void processEvent(DSCEvent event, DSCParser parser)
                        throws IOException, DSCException {
                    if (event instanceof DSCCommentLanguageLevel) {
                        DSCCommentLanguageLevel level = (DSCCommentLanguageLevel)event;
                        results.put("level", new Integer(level.getLanguageLevel()));
                    }
                }
            });
            int count = 0;
            while (parser.hasNext()) {
                DSCEvent event = parser.nextEvent();
                System.out.println(event);
                count++;
            }
            assertEquals(12, count);
            assertEquals(new Integer(1), results.get("level"));
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

}
