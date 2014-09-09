package panda.mvc.annotation.param;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可以声明在入口函数的参数上，表示该参数来自某一个上下文环境的属性
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
@Documented
public @interface Attr {
	/**
	 * 属性的上下文环境
	 */
	String scope() default "";

	/**
	 * 属性的名称
	 */
	String value();
}
