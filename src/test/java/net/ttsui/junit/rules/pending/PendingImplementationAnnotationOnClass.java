package net.ttsui.junit.rules.pending;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;

@PendingImplementation("Reason for pending")
public class PendingImplementationAnnotationOnClass {
    @Rule public PendingRule pendingRule = new PendingRule();
    
    @Test public void
    failingTestShouldPass() {
        fail();
    }
    
    @Test(expected=AssertionError.class) public void
    passingTestShouldFail() {
        assertEquals(1, 1);
    }
}