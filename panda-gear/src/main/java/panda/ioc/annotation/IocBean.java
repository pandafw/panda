package panda.ioc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import panda.ioc.Scope;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IocBean {

	/**
	 * Ioc Bean Name
	 */
	String name() default "";
	
	/**
	 * alias of name()
	 */
	String value() default "";

	/**
	 * type
	 */
	Class<?> type() default Object.class;
	
	/**
	 * Constructor arguments
	 * 
	 * @see panda.ioc.meta.IocValue
	 */
	String[] args() default {};

	String scope() default Scope.APP;

	boolean singleton() default true;

	/**
	 * initialize method invoked on object is created
	 */
	String create() default "";

	/**
	 * depose method invoked on object is removed from Ioc container
	 */
	String depose() default "";

	/**
	 * fetch method invoked on object is fetched from Ioc container
	 */
	String fetch() default "";

	/**
	 * the name of fields which need to be inject
	 * 
	 * @return field names
	 */
	String[] fields() default {};

	String factory() default "";
}
