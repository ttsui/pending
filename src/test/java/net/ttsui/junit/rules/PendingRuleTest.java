package net.ttsui.junit.rules;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class PendingRuleTest {
    
    public static class PendingAnnotationOnMethod {
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

    @Pending("Reason for pending")
    public static class PendingAnnotationOnClass {
        @Rule public MethodRule pendingRule = PendingRule.withOutput();
        
        @Test public void
        failingTestShouldPass() {
            assertEquals(1, 2);
        }
        
        @Test public void
        passingTestShouldContainToPass() {
            assertEquals(1, 1);
        }
    }
}
