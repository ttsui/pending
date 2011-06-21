package net.ttsui.junit.rules.pending;

import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;


public class SupportJUnitExpectedExceptionTest {
    @Rule public PendingRule pendingRule = new PendingRule();

    @Test public void
    passingTest() {

    }

    @PendingImplementation
    @Test(expected=NullPointerException.class) public void
    unexpectedSuccessTest() {
        throw new NullPointerException();
    }

    @Test public void
    failingTest() {
        fail("test");
    }
}
