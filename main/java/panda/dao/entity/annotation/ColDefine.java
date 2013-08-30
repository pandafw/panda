package panda.dao.entity.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import panda.dao.entity.ColType;

/**
 * Column definition for create table
 * @author yf.frank.wang@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface ColDefine {

	/**
	 * abstract column type
	 * 
	 * @see panda.dao.entity.ColType
	 */
	ColType type() default ColType.VARCHAR;

	int size() default 0;

	int scale() default 2;

	boolean notNull() default false;

	boolean unsigned() default false;

	/**
	 * real database column type
	 */
	String dbType() default "";
}
