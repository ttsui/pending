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
        if (isCategorisedAsPending(method) || isAnnotatedWithPending(method)) {
            try {
                base.evaluate();
            } catch (Throwable e) {
                return noOpStatement();
            }
            
            throw new AssertionError("Unexpected success");
        }
        
        return base;
    }

    private boolean isCategorisedAsPending(FrameworkMethod method) {
        Category annotation = method.getAnnotation(Category.class);
    
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

    private boolean isAnnotatedWithPending(final FrameworkMethod method) {
        return isMethodAnnotatedWithPending(method) || isClassAnnotatedWithPending(method.getMethod().getDeclaringClass());
    }
    
    private boolean isClassAnnotatedWithPending(Class<?> klass) {
        return klass.getAnnotation(PendingImplementation.class) != null;
    }
    
    private boolean isMethodAnnotatedWithPending(FrameworkMethod method) {
        return method.getAnnotation(PendingImplementation.class) != null;
    }
    
    private Statement noOpStatement() {
        return new Statement() {
            @Override public void evaluate() throws Throwable { }
        };
    }

}