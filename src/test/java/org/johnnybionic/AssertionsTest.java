package org.johnnybionic;

import org.junit.Test;

public class AssertionsTest {

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

}
