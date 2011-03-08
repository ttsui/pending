package net.ttsui.junit.rules.pending;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

@RunWith(Enclosed.class)
public class PendingRuleTest {
    
    public static class PendingRuleSemantics {
        private Mockery context = new Mockery() {{ setImposteriser(ClassImposteriser.INSTANCE); }};
        private final Statement base = context.mock(Statement.class);
        private final FrameworkMethod frameworkMethod = context.mock(FrameworkMethod.class);
        
        @Test public void
        failingTestAnnotatedWithPendingShouldPass() throws Throwable {
            final Pending annotation = context.mock(Pending.class);
            
            context.checking(new Expectations() {{
                oneOf(base).evaluate(); will(throwException(new AssertionError()));
                
                allowing(frameworkMethod).getAnnotation(Pending.class); will(returnValue(annotation));
            }});
            
            PendingRule rule = new PendingRule();
            rule.apply(base, frameworkMethod, null);
            
            context.assertIsSatisfied();
        }
        
        @Test(expected=AssertionError.class) public void
        passingTestAnnotatedWithPendingShouldFail() throws Throwable {
            final Pending annotation = context.mock(Pending.class);
            
            context.checking(new Expectations() {{
                allowing(base).evaluate();
                allowing(frameworkMethod).getAnnotation(Pending.class); will(returnValue(annotation));
            }});
            
            PendingRule rule = new PendingRule();
            rule.apply(base, frameworkMethod, null);
        }
        
        @Test public void
        returnsOriginalStatementForTestsNotAnnotatedWithPending() {
            
            context.checking(new Expectations() {{
                oneOf(frameworkMethod).getAnnotation(Pending.class); will(returnValue(null));
                oneOf(frameworkMethod).getMethod(); will(returnValue(TestClassWithoutAnnotation.class.getDeclaredMethods()[0]));
            }});
            
            PendingRule rule = new PendingRule();
            Statement returnedStatement = rule.apply(base, frameworkMethod, null);
            
            assertThat(returnedStatement, is(base));
        }
        
        private class TestClassWithoutAnnotation {
            @SuppressWarnings("unused") public void testMethodWithoutAnnotation() { }
        }
    }

    @Pending("Reason for pending")
    public static class PendingAnnotationOnClass {
        @Rule public MethodRule pendingRule = PendingRule.withOutput();
        
        @Test public void
        failingTestShouldPass() {
            fail();
        }
        
        @Test(expected=AssertionError.class) public void
        passingTestShouldFail() {
            assertEquals(1, 1);
        }
    }
}
