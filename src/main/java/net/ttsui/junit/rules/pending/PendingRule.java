package net.ttsui.junit.rules.pending;

import org.junit.experimental.categories.Category;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;


public class PendingRule implements MethodRule {
    
    private final Class<?> pendingCategory;

    public PendingRule(Class<?> category) {
        this.pendingCategory = category;
    }
    
    public PendingRule() { 
        pendingCategory = null;
    }

    @Override
	public Statement apply(Statement base, final FrameworkMethod method, Object target) {
        if (isAnnotatedWithPending(method) || isCategorisedAsPending(method)) {
            return new PendingImplementationStatement(base);
        }
        
        return base;
    }

    private boolean isAnnotatedWithPending(final FrameworkMethod method) {
        return isMethodAnnotatedWithPending(method) 
            || isClassAnnotatedWithPending((Class<?>) method.getMethod().getDeclaringClass());
    }
    
    private boolean isCategorisedAsPending(FrameworkMethod method) {
        return isMethodCategorisedAsPending(method)
            || isClassCategoriedAsPending((Class<?>) method.getMethod().getDeclaringClass());
    }

    private boolean isMethodAnnotatedWithPending(FrameworkMethod method) {
        return method.getAnnotation(PendingImplementation.class) != null;
    }
    
    private boolean isClassAnnotatedWithPending(Class<?> klass) {
        return klass.getAnnotation(PendingImplementation.class) != null;
    }

    private boolean isMethodCategorisedAsPending(FrameworkMethod method) {
        Category annotation = method.getAnnotation(Category.class);
    
        return containsPendingCategory(annotation);
    }

    private boolean isClassCategoriedAsPending(Class<?> klass) {
        Category annotation = klass.getAnnotation(Category.class);
    
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