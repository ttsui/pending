package net.ttsui.junit.rules.pending;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PendingRuleSemanticsTest {
    private final Mockery context = new Mockery() {{ setImposteriser(ClassImposteriser.INSTANCE); }};
    private final Statement base = context.mock(Statement.class);
    private final Description description = context.mock(Description.class);

    @Test public void
    failingTestAnnotatedWithPendingImplemenationShouldPass() throws Throwable {
        final PendingImplementation annotation = context.mock(PendingImplementation.class);

        context.checking(new Expectations() {{
            oneOf(base).evaluate(); will(throwException(new Throwable()));

            allowing(description).getAnnotation(Category.class); will(returnValue(null));
            allowing(description).getAnnotation(PendingImplementation.class); will(returnValue(annotation));
        }});

        PendingRule rule = new PendingRule();
        rule.apply(base, description).evaluate();

        context.assertIsSatisfied();
    }

    @Test(expected=AssertionError.class) public void
    passingTestAnnotatedWithPendingImplementationShouldFail() throws Throwable {
        final PendingImplementation annotation = context.mock(PendingImplementation.class);

        context.checking(new Expectations() {{
            allowing(base).evaluate();
            allowing(description).getAnnotation(Category.class); will(returnValue(null));
            allowing(description).getAnnotation(PendingImplementation.class); will(returnValue(annotation));
        }});

        PendingRule rule = new PendingRule();
        rule.apply(base, description).evaluate();
    }

    @Test public void
    returnsOriginalStatementForTestsNotAnnotatedWithPendingImplementation() {

        context.checking(new Expectations() {{
            allowing(description).getAnnotation(Category.class); will(returnValue(null));
            allowing(description).getAnnotation(PendingImplementation.class); will(returnValue(null));
            allowing(description).getTestClass(); will(returnValue(TestClassWithoutAnnotation.class));
        }});

        PendingRule rule = new PendingRule();
        Statement returnedStatement = rule.apply(base, description);

        assertThat(returnedStatement, is(base));
    }

    private class TestClassWithoutAnnotation {
        @SuppressWarnings("unused") public void testMethodWithoutAnnotation() { }
    }
}
