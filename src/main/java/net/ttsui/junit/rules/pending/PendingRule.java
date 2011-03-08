package net.ttsui.junit.rules.pending;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;


public class PendingRule implements MethodRule {
    private final boolean showOutput;
    
    public static PendingRule withOutput() {
        return new PendingRule(true);
    }
    
    public PendingRule() {
        this(false);
    }
    
    private PendingRule(boolean showOutput) {
        this.showOutput = showOutput;
    }
    
    @Override
	public Statement apply(Statement base, final FrameworkMethod method, Object target) {
        if (isAnnotatedWithPending(method)) {
            try {
                base.evaluate();
            } catch (Throwable e) {
                return pendingStatementFor(method);
            }
            
            throw new AssertionError();
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
    
    private Statement pendingStatementFor(final FrameworkMethod method) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                if (!showOutput) {
                    return;
                }
                
                Class<?> declaringClass = method.getMethod().getDeclaringClass();
                
                StringBuilder output = new StringBuilder("PENDING TEST: ");
                output.append(format("%s.%s()", declaringClass.getSimpleName(), method.getName()));
                
                String reason = determineReasonFor(method);
                if (!reason.isEmpty()) {
                    output.append(reason);
                }
                
                System.out.println(output.toString());
            }

            private String determineReasonFor(final FrameworkMethod method) {
                List<String> reasons = new ArrayList<String>();
                
                Class<?> declaringClass = method.getMethod().getDeclaringClass();
                if (isClassAnnotatedWithPending(declaringClass)) {
                    String reason = declaringClass.getAnnotation(Pending.class).value();
                    if (!reason.isEmpty()) {
                        reasons.add(reason);
                    }
                }
                
                if (isMethodAnnotatedWithPending(method)) {
                    String reason = method.getAnnotation(Pending.class).value();
                    if (!reason.isEmpty()) {
                        reasons.add(reason);
                    }
                }
                
                if (reasons.isEmpty()) {
                    return "";
                }
                
                return format(" [%s]", join(reasons));
            }
            
            private String join(List<String> strings) {
                if (strings.isEmpty()) {
                    return "";
                }
                
                StringBuilder output = new StringBuilder(strings.get(0));
                
                for (String string : strings.subList(1, strings.size())) {
                    output.append(", ");
                    output.append(string);
                }
                
                return output.toString();
            }
        };
    }

}