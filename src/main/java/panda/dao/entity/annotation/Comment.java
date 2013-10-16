package panda.dao.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import panda.lang.Strings;

/**
 * Comment for table or field
 * @author yf.frank.wang@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface Comment {
	String value() default Strings.EMPTY;
}
