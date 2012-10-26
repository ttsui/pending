package net.ttsui.junit.rules.pending;

import org.junit.experimental.categories.Category;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class PendingRule implements TestRule {
    private final Class<?> pendingCategory;

    public PendingRule(Class<?> category) {
        this.pendingCategory = category;
    }

    public PendingRule() {
        pendingCategory = null;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        if (isAnnotatedWithPending(description) || isCategorisedAsPending(description)) {
            return new PendingImplementationStatement(base);
        }

        return base;
    }

    private boolean isAnnotatedWithPending(final Description description) {
        return isMethodAnnotatedWithPending(description)
            || isClassAnnotatedWithPending(description.getTestClass());
    }

    private boolean isCategorisedAsPending(Description description) {
        return isMethodCategorisedAsPending(description)
            || isClassCategoriedAsPending(description.getTestClass());
    }

    private boolean isMethodAnnotatedWithPending(Description description) {
        return description.getAnnotation(PendingImplementation.class) != null;
    }

    private boolean isClassAnnotatedWithPending(Class<?> testClass) {
        return testClass.getAnnotation(PendingImplementation.class) != null;
    }

    private boolean isMethodCategorisedAsPending(Description description) {
        Category annotation = description.getAnnotation(Category.class);
        return containsPendingCategory(annotation);
    }

    private boolean isClassCategoriedAsPending(Class<?> testClass) {
        Category annotation = testClass.getAnnotation(Category.class);
        return containsPendingCategory(annotation);
    }

    private boolean containsPendingCategory(Category annotation) {
        if (annotation == null) {
            return false;
        }

        for (Class<?> category : annotation.value()) {
            if (category == pendingCategory) {
                return true;
            }
        }

        return false;
    }
}
