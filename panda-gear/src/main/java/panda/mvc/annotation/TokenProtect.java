package panda.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenProtect {
	/**
	 * @return true if need token protect
	 */
	boolean value() default true;

	/**
	 * > 0 for token time stamp check
	 * @return milliseconds
	 */
	int expire() default 0;
}
