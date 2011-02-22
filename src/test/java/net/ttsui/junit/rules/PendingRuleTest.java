package net.ttsui.junit.rules;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;



public class PendingRuleTest {

    @Rule public MethodRule pendingRule = new PendingRule();
    
    @Pending
    @Test public void
    failingTestAnnotatedWithPendingShouldPass() {
        assertEquals(1, 2);
    }
    
    @Test public void
    passingTestShouldContainToPass() {
        assertEquals(1, 1);
    }
    
    @Test(expected=AssertionError.class) public void
    failingTesShouldContinueToFail() {
        assertEquals(1, 2);
    }
}
