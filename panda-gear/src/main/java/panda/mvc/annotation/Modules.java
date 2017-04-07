package panda.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import panda.mvc.ActionContext;

/**
 * MVC modules declare
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface Modules {

	/**
	 * the classes of the module
	 */
	Class<?>[] value() default {};

	/**
	 * additional packages to scan (even if the scan()=false)
	 */
	String[] packages() default {};

	/**
	 * weather to scan the package of the module
	 */
	boolean scan() default false;
	
	/**
	 * ActionContext Type
	 */
	Class<? extends ActionContext> context() default ActionContext.class;
}
