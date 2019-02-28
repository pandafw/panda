package panda.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import panda.ioc.aop.MirrorFactory;
import panda.mvc.IocProvider;

/**
 * define the IocProvider of the main module
 * 
 * @see panda.mvc.IocProvider
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface IocBy {

	/**
	 * the class of IocProvider
	 */
	Class<? extends IocProvider> type();

	/**
	 * the arguments passed to create() method of IocProvider
	 */
	String[] args();
	
	/**
	 * The MirrorFactory class for AOP
	 */
	Class<? extends MirrorFactory> mirror() default MirrorFactory.class;
}
