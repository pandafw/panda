package panda.mvc.annotation.param;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import panda.mvc.ParamAdaptor;

/**
 * 可以声明在入口函数的参数上。 描述，应该对应到 HTTP 请求哪一个参数
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
@Documented
public @interface Param {
	/**
	 * parameter name
	 */
	String value() default "";

	/**
	 * cast format
	 */
	String format() default "";

	/**
	 * string strip
	 */
	char strip() default ParamAdaptor.ORIGINAL;
}
