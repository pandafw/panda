package panda.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import panda.mvc.ActionChainMaker;
import panda.mvc.impl.DefaultActionChainMaker;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface ChainBy {

	Class<? extends ActionChainMaker> type() default DefaultActionChainMaker.class;

	String[] args();

}
