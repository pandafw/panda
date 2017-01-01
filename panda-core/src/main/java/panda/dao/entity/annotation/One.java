package panda.dao.entity.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yf.frank.wang@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Documented
public @interface One {

	/**
	 * related fields of this entity
	 */
	String[] fields();

	/**
	 * target class
	 */
	Class<?> target();

	/**
	 * the related keys of the target class.
	 * if not supplied, the primary keys of the target class will be used.
	 */
	String[] keys() default {};

}
