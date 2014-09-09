package panda.mvc.annotation.param;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可以声明在入口函数的参数上。 描述，应该对应到 HTTP 请求哪一个参数
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
@Documented
public @interface Param {
	/**
	 * 对应到 HTTP 参数里的参数名称
	 */
	String value() default "";
}
