package panda.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MVC modules declare
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
@Inherited
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
}
