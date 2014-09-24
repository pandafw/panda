package panda.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import panda.mvc.ViewMaker;
import panda.mvc.impl.DefaultViewMaker;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface ViewBy {

	Class<? extends ViewMaker> type() default DefaultViewMaker.class;

	String[] args();

}
