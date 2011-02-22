package net.ttsui.junit.rules;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;


public class PendingRule implements MethodRule {
    @Override
	public Statement apply(Statement base, final FrameworkMethod method, Object target) {
        if (method.getAnnotation(Pending.class) != null) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    System.out.println(String.format("Pending test: %s()", method.getName()));
                }
            };
        }
        
        return base;
    }
}