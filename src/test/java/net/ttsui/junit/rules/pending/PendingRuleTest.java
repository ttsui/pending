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
        failingTestAnnotatedWithPendingImplemenationShouldPass() throws Throwable {
            final PendingImplementation annotation = context.mock(PendingImplementation.class);
            
            context.checking(new Expectations() {{
                oneOf(base).evaluate(); will(throwException(new AssertionError()));
                
                allowing(frameworkMethod).getAnnotation(PendingImplementation.class); will(returnValue(annotation));
            }});
            
            PendingRule rule = new PendingRule();
            rule.apply(base, frameworkMethod, null);
            
            context.assertIsSatisfied();
        }
        
        @Test(expected=AssertionError.class) public void
        passingTestAnnotatedWithPendingImplementationShouldFail() throws Throwable {
            final PendingImplementation annotation = context.mock(PendingImplementation.class);
            
            context.checking(new Expectations() {{
                allowing(base).evaluate();
                allowing(frameworkMethod).getAnnotation(PendingImplementation.class); will(returnValue(annotation));
            }});
            
            PendingRule rule = new PendingRule();
            rule.apply(base, frameworkMethod, null);
        }
        
        @Test public void
        returnsOriginalStatementForTestsNotAnnotatedWithPendingImplementation() {
            
            context.checking(new Expectations() {{
                oneOf(frameworkMethod).getAnnotation(PendingImplementation.class); will(returnValue(null));
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

    @PendingImplementation("Reason for pending")
    public static class PendingImplementationAnnotationOnClass {
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
}
