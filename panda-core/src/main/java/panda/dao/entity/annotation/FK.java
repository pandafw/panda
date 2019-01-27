package panda.dao.entity.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface FK {
	public static final String NO_ACTION = "NO ACTION";
	public static final String RESTRICT = "RESTRICT";
	public static final String SET_NULL = "SET NULL";
	public static final String SET_DEFAULT = "SET DEFAULT";
	public static final String CASCADE = "CASCADE";

	String name() default "";

	/**
	 * target class
	 */
	Class<?> target();
	
	/**
	 * fields
	 */
	String[] fields() default {};

	/**
	 * ON DELETE ACTION
	 */
	String onDelete() default "";

	/**
	 * ON UPDATE ACTION
	 */
	String onUpdate() default "";
}
