package panda.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import panda.mvc.ActionContext;

/**
 * 声明了一个应用所有的模块
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface Modules {

	/**
	 * 每个模块一个类
	 */
	Class<?>[] value() default {};

	/**
	 * 需要扫描的package
	 * <p/>
	 * <b>这个属性不受scan的影响!!</b>
	 */
	String[] packages() default {};

	/**
	 * 是否搜索模块类同包以及子包的其他类
	 */
	boolean scan() default false;
	
	/**
	 * ActionContext Type
	 */
	Class<? extends ActionContext> context() default ActionContext.class;
}
