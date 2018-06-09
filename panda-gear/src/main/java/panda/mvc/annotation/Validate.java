package panda.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
	/**
	 * The referred field name to show error message.
	 */
	String refer() default "";

	/**
	 * The default error message for this validator. NOTE: It is required to set a message, if you
	 * are not using the message key for 18n lookup!
	 */
	String message() default "";

	/**
	 * The message id to lookup for i18n.
	 */
	String msgId() default "";

	/**
	 * If this is activated, the validator will be used as short-circuit. Adds the
	 * short-circuit="true" attribute value if <tt>true</tt>.
	 */
	boolean shortCircuit() default false;

	/**
	 * JSON String of validator parameters
	 */
	String params() default "";

	/**
	 * validator name
	 */
	String value();
}