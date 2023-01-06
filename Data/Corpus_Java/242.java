/*
 The MIT License

 Copyright (c) 2010-2017 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.junit.quickcheck.generator.java.util.function;

import java.util.function.Predicate;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.experimental.results.PrintableResult.*;
import static org.junit.experimental.results.ResultMatchers.*;

public class PredicatePropertyParameterTest {
    @Test public void definiteArgType() {
        assertThat(testResult(DefiniteArgType.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class DefiniteArgType {
        @Property public void x(Predicate<String> p) {
            p.test("abc");
        }
    }

    @Test public void callingDefaultPredicateMethod() {
        assertThat(testResult(CallingDefaultPredicateMethod.class), isSuccessful());
    }

    @RunWith(JUnitQuickcheck.class)
    public static class CallingDefaultPredicateMethod {
        @Property public <T, U extends T> void and(Predicate<U> first, Predicate<T> second, U arg) {
            boolean firstResult = first.test(arg);
            boolean secondResult = second.test(arg);

            assertEquals(firstResult && secondResult, first.and(second).test(arg));
        }

        @Property public <T> void negate(Predicate<T> p, T arg) {
            boolean result = p.test(arg);

            assertEquals(!result, p.negate().test(arg));
        }
    }
}
