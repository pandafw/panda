package panda.dao.entity.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import panda.dao.sql.JdbcTypes;
import panda.lang.Strings;

/**
 * Column definition for create table
 * @author yf.frank.wang@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface ColDefine {

	/**
	 * jdbc type
	 * 
	 * @see java.sql.Types
	 */
	String type() default JdbcTypes.VARCHAR;

	int size() default 0;

	int scale() default 0;

	boolean notNull() default false;

	boolean unsigned() default false;

	/**
	 * real database column type
	 */
	String dbType() default Strings.EMPTY;
}
