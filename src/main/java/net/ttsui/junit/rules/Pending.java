package net.ttsui.junit.rules;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Pending {
	/**
	 * The optional reason why the test is pending.
	 */
	String value() default ""; 
}