package panda.dao.entity.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface Index {
	String name() default Strings.EMPTY;

	String[] fields() default {};

	boolean unique() default false;
}
