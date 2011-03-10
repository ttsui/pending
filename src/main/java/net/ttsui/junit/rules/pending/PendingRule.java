package net.ttsui.junit.rules.pending;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;


public class PendingRule implements MethodRule {
    
    @Override
	public Statement apply(Statement base, final FrameworkMethod method, Object target) {
        if (isAnnotatedWithPending(method)) {
            try {
                base.evaluate();
            } catch (Throwable e) {
                return noOpStatement();
            }
            
            throw new AssertionError("Unexpected success");
        }
        
        return base;
    }

    private boolean isAnnotatedWithPending(final FrameworkMethod method) {
        return isMethodAnnotatedWithPending(method) || isClassAnnotatedWithPending(method.getMethod().getDeclaringClass());
    }
    
    private boolean isClassAnnotatedWithPending(Class<?> klass) {
        return klass.getAnnotation(Pending.class) != null;
    }
    
    private boolean isMethodAnnotatedWithPending(FrameworkMethod method) {
        return method.getAnnotation(Pending.class) != null;
    }
    
    private Statement noOpStatement() {
        return new Statement() {
            @Override public void evaluate() throws Throwable { }
        };
    }

}