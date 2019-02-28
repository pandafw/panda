package panda.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@Documented
public @interface To {

	/** default view */
	public String value() default "";

	/** validation error view */
	public String error() default "";
	
	/** fatal view */
	public String fatal() default "";
}
