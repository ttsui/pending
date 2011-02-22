package net.ttsui.junit.rules;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;


public class PendingRule implements MethodRule {
    @Override
	public Statement apply(Statement base, final FrameworkMethod method, Object target) {
        if (isAnnotatedWithPending(method)) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    System.out.println(String.format("Pending test: %s()", method.getName()));
                }
            };
        }
        
        return base;
    }

    private boolean isAnnotatedWithPending(final FrameworkMethod method) {
        return isClassAnnotatedWithPending(method.getMethod().getDeclaringClass()) || isMethodAnnotatedWithPending(method);
    }
    
    private boolean isClassAnnotatedWithPending(Class<?> klass) {
        return klass.getAnnotation(Pending.class) != null;
    }
    
    private boolean isMethodAnnotatedWithPending(FrameworkMethod method) {
        return method.getAnnotation(Pending.class) != null;
    }
}