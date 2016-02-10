package panda.ioc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IocInject {

	/**
	 * 规则: type:value
	 * <p/>
	 * type 类型
	 * <p/>
	 * 对应的值
	 * <p/>
	 * 如: <code>ref:dao</code> 代表引用另外一个对象
	 * <p/>
	 * 如: <code>env:OS</code> 获取环境变量OS,即操作系统的名字 <b>缺省情况下,为 "ref:fieldName", fieldName为字段的名字</b>
	 * 
	 * @see panda.ioc.meta.IocValue
	 * @return 需要注入的值的表达式
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
