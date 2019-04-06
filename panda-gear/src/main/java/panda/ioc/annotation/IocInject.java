package panda.ioc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IocInject {

	/**
	 * Bean Name or Bean Value
	 * <pre>
	 *   String Value: starts with single quote (ex: "'abc" => "abc")
	 *   Reference IocObject: starts with sharp (ex: "#dao" => ioc object of "dao")
	 *   EL Expression: $(...) or %(...)
	 *   JSON Object: !{...}
	 *   JSON Array: ![...]
	 * </pre>
	 * @return Bean Name
	 */
	String value() default "";
	
	/**
	 * Bean type
	 * This is ignored if value is specified.
	 * 
	 * @return Bean type
	 */
	Class<?> type() default Object.class;

	/**
	 * @return required or not
	 */
	boolean required() default true;
}
