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

/* $Id: MinOptMaxTest.java 893238 2009-12-22 17:20:51Z vhennebert $ */

package org.apache.fop.traits;

import junit.framework.TestCase;

/**
 * Tests the {@link MinOptMaxTest} class.
 */
public class MinOptMaxTest extends TestCase {

    /**
     * Tests that the constant <code>MinOptMax.ZERO</code> is really zero.
     */
    public void testZero() {
        assertEquals(MinOptMax.getInstance(0), MinOptMax.ZERO);
    }

    public void testNewStiffMinOptMax() {
        MinOptMax value = MinOptMax.getInstance(1);
        assertTrue(value.isStiff());
        assertEquals(1, value.getMin());
        assertEquals(1, value.getOpt());
        assertEquals(1, value.getMax());
    }

    public void testNewMinOptMax() {
        MinOptMax value = MinOptMax.getInstance(1, 2, 3);
        assertTrue(value.isElastic());
        assertEquals(1, value.getMin());
        assertEquals(2, value.getOpt());
        assertEquals(3, value.getMax());
    }

    /**
     * Test that it is possible to create stiff instances with the normal factory method.
     */
    public void testNewMinOptMaxStiff() {
        MinOptMax value = MinOptMax.getInstance(1, 1, 1);
        assertTrue(value.isStiff());
        assertEquals(1, value.getMin());
        assertEquals(1, value.getOpt());
        assertEquals(1, value.getMax());
    }

    public void testNewMinOptMaxMinGreaterOpt() {
        try {
            MinOptMax.getInstance(1, 0, 2);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("min (1) > opt (0)", e.getMessage());
        }
    }

    public void testNewMinOptMaxMaxSmallerOpt() {
        try {
            MinOptMax.getInstance(0, 1, 0);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("max (0) < opt (1)", e.getMessage());
        }
    }

    public void testShrinkablility() {
        assertEquals(0, MinOptMax.getInstance(1).getShrink());
        assertEquals(1, MinOptMax.getInstance(1, 2, 2).getShrink());
        assertEquals(2, MinOptMax.getInstance(1, 3, 3).getShrink());
    }

    public void testStrechablilty() {
        assertEquals(0, MinOptMax.getInstance(1).getStretch());
        assertEquals(1, MinOptMax.getInstance(1, 1, 2).getStretch());
        assertEquals(2, MinOptMax.getInstance(1, 1, 3).getStretch());
    }

    public void testPlus() {
        assertEquals(MinOptMax.ZERO,
                MinOptMax.ZERO.plus(MinOptMax.ZERO));
        assertEquals(MinOptMax.getInstance(1, 2, 3),
                MinOptMax.ZERO.plus(MinOptMax.getInstance(1, 2, 3)));
        assertEquals(MinOptMax.getInstance(2, 4, 6),
                MinOptMax.getInstance(1, 2, 3).plus(MinOptMax.getInstance(1, 2, 3)));
        assertEquals(MinOptMax.getInstance(4, 5, 6), MinOptMax.getInstance(1, 2, 3).plus(3));
    }

    public void testMinus() {
        assertEquals(MinOptMax.ZERO,
                MinOptMax.ZERO.minus(MinOptMax.ZERO));
        assertEquals(MinOptMax.getInstance(1, 2, 3),
                MinOptMax.getInstance(1, 2, 3).plus(MinOptMax.ZERO));
        assertEquals(MinOptMax.getInstance(1, 2, 3),
                MinOptMax.getInstance(2, 4, 6).minus(MinOptMax.getInstance(1, 2, 3)));
        assertEquals(MinOptMax.getInstance(1, 2, 3), MinOptMax.getInstance(5, 6, 7).minus(4));
    }

    public void testMinusFail1() {
        try {
            MinOptMax.ZERO.minus(MinOptMax.getInstance(1, 2, 3));
            fail();
        } catch (ArithmeticException e) {
            // Ok
        }
    }

    public void testMinusFail2() {
        try {
            MinOptMax.getInstance(1, 2, 3).minus(MinOptMax.getInstance(1, 3, 3));
            fail();
        } catch (ArithmeticException e) {
            // Ok
        }
    }

    public void testMinusFail3() {
        try {
            MinOptMax.ZERO.minus(MinOptMax.getInstance(1, 1, 2));
            fail();
        } catch (ArithmeticException e) {
            // Ok
        }
    }

    public void testMinusFail4() {
        try {
            MinOptMax.getInstance(1, 2, 3).minus(MinOptMax.getInstance(1, 1, 3));
            fail();
        } catch (ArithmeticException e) {
            // Ok
        }
    }

    public void testMult() {
        assertEquals(MinOptMax.ZERO, MinOptMax.ZERO.mult(0));
        assertEquals(MinOptMax.getInstance(1, 2, 3), MinOptMax.getInstance(1, 2, 3).mult(1));
        assertEquals(MinOptMax.getInstance(2, 4, 6), MinOptMax.getInstance(1, 2, 3).mult(2));
    }

    public void testMultFail() {
        try {
            MinOptMax.getInstance(1, 2, 3).mult(-1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("factor < 0; was: -1", e.getMessage());
        }
    }

    public void testNonZero() {
        assertFalse(MinOptMax.ZERO.isNonZero());
        assertTrue(MinOptMax.getInstance(1).isNonZero());
        assertTrue(MinOptMax.getInstance(1, 2, 3).isNonZero());
    }

    public void testExtendMinimum() {
        assertEquals(MinOptMax.getInstance(1, 1, 1),
                MinOptMax.ZERO.extendMinimum(1));
        assertEquals(MinOptMax.getInstance(1, 2, 3),
                MinOptMax.getInstance(1, 2, 3).extendMinimum(1));
        assertEquals(MinOptMax.getInstance(2, 2, 3),
                MinOptMax.getInstance(1, 2, 3).extendMinimum(2));
        assertEquals(MinOptMax.getInstance(3, 3, 3),
                MinOptMax.getInstance(1, 2, 3).extendMinimum(3));
        assertEquals(MinOptMax.getInstance(4, 4, 4),
                MinOptMax.getInstance(1, 2, 3).extendMinimum(4));
    }

    public void testEquals() {
        MinOptMax number = MinOptMax.getInstance(1, 3, 5);
        assertEquals(number, number);
        assertEquals(number, MinOptMax.getInstance(1, 3, 5));
        assertFalse(number.equals(MinOptMax.getInstance(2, 3, 5)));
        assertFalse(number.equals(MinOptMax.getInstance(1, 4, 5)));
        assertFalse(number.equals(MinOptMax.getInstance(1, 3, 4)));
        assertFalse(number.equals(null));
        assertFalse(number.equals(new Integer(1)));
    }

    public void testHashCode() {
        MinOptMax number = MinOptMax.getInstance(1, 2, 3);
        assertEquals(number.hashCode(), number.hashCode());
        assertEquals(number.hashCode(), MinOptMax.getInstance(1, 2, 3).hashCode());
    }
}
